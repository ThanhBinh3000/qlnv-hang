package com.tcdt.qlnvhang.service.impl;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;

import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.*;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.*;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.service.feign.BaoCaoClient;
import com.tcdt.qlnvhang.table.DmDonViDTO;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.HhDxKhlcntDsgthauReport;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;

@Service
public class HhDxuatKhLcntHdrServiceImpl extends BaseServiceImpl implements HhDxuatKhLcntHdrService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

    @Autowired
    private HhDxuatKhLcntDsgtDtlRepository hhDxuatKhLcntDsgtDtlRepository;

    @Autowired
    private HhDxKhlcntDsgthauCtietRepository hhDxKhlcntDsgthauCtietRepository;

    @Autowired
    private HhDxKhlcntDsgthauCtietVtRepository hhDxKhlcntDsgthauCtietVtRepository;

    @Autowired
    private HhDxKhlcntDsgthauCtietVt1Repository hhDxKhlcntDsgthauCtietVt1Repository;

    @Autowired
    private HhDxuatKhLcntCcxdgDtlRepository hhDxuatKhLcntCcxdgDtlRepository;

    @Autowired
    private HhDxKhLcntThopDtlRepository hhDxKhLcntThopDtlRepository;
    @Autowired
    DocxToPdfConverter docxToPdfConverter;
    @Autowired
    private BaoCaoClient baoCaoClient;


    Long shgtNext = new Long(0);

    @Override
    @Transactional
    public HhDxuatKhLcntHdr create(HhDxuatKhLcntHdrReq objReq) throws Exception {
        if (!StringUtils.isEmpty(objReq.getSoDxuat())) {
            Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
            if (qOptional.isPresent()) {
                throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
            }
        }
        // Add danh sach file dinh kem o Master
        List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
        if (objReq.getFileDinhKemReq() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
        dataMap.setNgayTao(objReq.getNgayTao());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setFileDinhKems(fileDinhKemList);
        this.validateData(dataMap, dataMap.getTrangThai());
        hhDxuatKhLcntHdrRepository.save(dataMap);
        if (objReq.getLoaiVthh() != null && !objReq.getLoaiVthh().startsWith("02")) {
            saveDetailLuongThuc(objReq, dataMap.getId());
        } else {
            saveDetail(objReq, dataMap.getId());
        }

        // Lưu quyết định căn cứ
        for (HhDxuatKhLcntCcxdgDtlReq cc : objReq.getCcXdgReq()) {
            HhDxuatKhLcntCcxdgDtl data = ObjectMapperUtils.map(cc, HhDxuatKhLcntCcxdgDtl.class);
            List<FileDKemJoinDxKhLcntCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhLcntCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdDxKhlcnt(dataMap.getId());

            hhDxuatKhLcntCcxdgDtlRepository.save(data);
        }
        return dataMap;
    }

    public void validateData(HhDxuatKhLcntHdr objHdr, String trangThai) throws Exception {
        if (objHdr.getLoaiVthh() != null && objHdr.getLoaiVthh().startsWith("02")) {

        } else {
            if (trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId())) {
                HhDxuatKhLcntHdr dXuat = hhDxuatKhLcntHdrRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhoachAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKhoach(), objHdr.getMaDvi(), NhapXuatHangTrangThaiEnum.DUTHAO.getId());
                if (!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())) {
                    throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
                }
            }
            if (trangThai.equals(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId())) {
                for (HhDxKhlcntDsgthau goiThau : objHdr.getDsGtDtlList()) {
                    for (HhDxKhlcntDsgthauCtiet chiCuc: goiThau.getChildren()) {
                        BigDecimal aLong = hhDxuatKhLcntHdrRepository.countSLDalenKh(objHdr.getNamKhoach(), objHdr.getLoaiVthh(), chiCuc.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
                        BigDecimal soLuongTotal = aLong.add(chiCuc.getSoLuong());
                        if (soLuongTotal.compareTo(chiCuc.getSoLuongTheoChiTieu()) > 0) {
                            throw new Exception(chiCuc.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
                        }
                    }

                }
            }
        }
    }

    @Transactional
    void saveDetailLuongThuc(HhDxuatKhLcntHdrReq objReq, Long idHdr) {
        hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(idHdr);
        Map<String, List<HhDxuatKhLcntDsgtDtlReq>> mapGoiThau = new HashMap<>();
        for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()) {
            if (!mapGoiThau.containsKey(gt.getGoiThau())) {
                List<HhDxuatKhLcntDsgtDtlReq> newList = new ArrayList<>();
                newList.add(gt);
                mapGoiThau.put(gt.getGoiThau(), newList);
            } else {
                mapGoiThau.get(gt.getGoiThau()).add(gt);
            }
        }
        for (List<HhDxuatKhLcntDsgtDtlReq> listData : mapGoiThau.values()) {
            HhDxKhlcntDsgthau gthau = setGoiThau(listData, objReq, idHdr);
            hhDxuatKhLcntDsgtDtlRepository.save(gthau);
            hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gthau.getId());
            AtomicReference<BigDecimal> soLuong = new AtomicReference<>(BigDecimal.ZERO);
            AtomicReference<BigDecimal> thanhTienDx = new AtomicReference<>(BigDecimal.ZERO);
            AtomicReference<BigDecimal> thanhTien = new AtomicReference<>(BigDecimal.ZERO);
            listData.forEach(cuc -> {
                for (HhDxuatKhLcntDsgthauDtlCtietReq child : cuc.getChildren()) {
                    if (child.getSoLuong() != null) {
                        soLuong.updateAndGet(v -> v.add(child.getSoLuong()));
                        if (child.getDonGiaTamTinh() != null) {
                            thanhTienDx.updateAndGet(v -> v.add(child.getSoLuong().multiply(child.getDonGiaTamTinh())));
                        }
                        if (child.getDonGia() != null) {
                            thanhTien.updateAndGet(v -> v.add(child.getSoLuong().multiply(child.getDonGia())));
                        }
                    }
                    HhDxKhlcntDsgthauCtiet dsgthauCtiet = new ModelMapper().map(child, HhDxKhlcntDsgthauCtiet.class);
                    dsgthauCtiet.setIdGoiThau(gthau.getId());
                    hhDxKhlcntDsgthauCtietRepository.save(dsgthauCtiet);
                    hhDxKhlcntDsgthauCtietVtRepository.deleteAllByIdGoiThauCtiet(dsgthauCtiet.getId());
                    for (HhDxuatKhLcntDsgthauDtlCtietVtReq vt : child.getChildren()) {
                        HhDxKhlcntDsgthauCtietVt vtMap = new ModelMapper().map(vt, HhDxKhlcntDsgthauCtietVt.class);
                        vtMap.setId(null);
                        vtMap.setIdGoiThauCtiet(dsgthauCtiet.getId());
                        hhDxKhlcntDsgthauCtietVtRepository.save(vtMap);
                        hhDxKhlcntDsgthauCtietVt1Repository.deleteAllByIdGoiThauCtietVt(vt.getId());
                    }
                }
            });
            gthau.setSoLuong(soLuong.get());
            gthau.setThanhTien(thanhTien.get());
            gthau.setThanhTienDx(thanhTienDx.get());
            hhDxuatKhLcntDsgtDtlRepository.save(gthau);

        }
    }

    HhDxKhlcntDsgthau setGoiThau (List<HhDxuatKhLcntDsgtDtlReq> listData, HhDxuatKhLcntHdrReq objReq, Long idHdr) {
        HhDxKhlcntDsgthau gthau = new HhDxKhlcntDsgthau();
        gthau.setGoiThau(listData.get(0).getGoiThau());
//        gthau.setDonGiaVat(listData.get(0).getDonGiaVat());
//        gthau.setDonGiaTamTinh(listData.get(0).getDonGiaTamTinh());
        gthau.setMaDvi(objReq.getMaDvi());
        gthau.setLoaiVthh(objReq.getLoaiVthh());
        gthau.setCloaiVthh(objReq.getCloaiVthh());
        gthau.setDviTinh("kg");
        gthau.setIdDxKhlcnt(idHdr);
        return gthau;
    }


    @Transactional
    void saveDetail(HhDxuatKhLcntHdrReq objReq, Long idHdr) {
        // Xóa tất cả các gói thầu cũ và lưu mới
        hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(idHdr);
        // Lưu danh sách Gói thầu (Vật tư) và Gói thầu + Chi cục ( Lương thực )
        for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()) {
            HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
            data.setId(null);
            data.setIdDxKhlcnt(idHdr);
            if (data.getDonGiaTamTinh() != null && data.getSoLuong() != null) {
                BigDecimal thanhTien = data.getDonGiaTamTinh().multiply(data.getSoLuong());
                data.setThanhTien(thanhTien);
            }
            hhDxuatKhLcntDsgtDtlRepository.save(data);
            hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(data.getId());
            // Lưu danh sách gói thầu Cục ( Vật tư ) và Điểm kho ( Lương thực )
            for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()) {
                HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
                dataDdNhap.setId(null);
                dataDdNhap.setIdGoiThau(data.getId());
                hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
                hhDxKhlcntDsgthauCtietVtRepository.deleteAllByIdGoiThauCtiet(ddNhap.getId());
                // Lưu danh sách gói thầu gồm các Chi Cục ( Vật tư )
                for (HhDxuatKhLcntDsgthauDtlCtietVtReq vt : ddNhap.getChildren()) {
                    HhDxKhlcntDsgthauCtietVt vtMap = new ModelMapper().map(vt, HhDxKhlcntDsgthauCtietVt.class);
                    vtMap.setId(null);
                    vtMap.setIdGoiThauCtiet(dataDdNhap.getId());
                    hhDxKhlcntDsgthauCtietVtRepository.save(vtMap);
                    hhDxKhlcntDsgthauCtietVt1Repository.deleteAllByIdGoiThauCtietVt(vt.getId());
                    // Lưu Điểm kho ( Lương thực )
                    for (HhDxuatKhLcntDsgthauDtlCtietVt1Req dk : vt.getChildren()) {
                        HhDxKhlcntDsgthauCtietVt1 dk1 = new ModelMapper().map(dk, HhDxKhlcntDsgthauCtietVt1.class);
                        dk1.setIdGoiThauCtietVt(vtMap.getId());
                        hhDxKhlcntDsgthauCtietVt1Repository.save(dk1);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public HhDxuatKhLcntHdr update(HhDxuatKhLcntHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (!StringUtils.isEmpty(objReq.getSoDxuat())) {
            Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
            if (deXuat.isPresent()) {
                if (!deXuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
                }
            }
        }


        // Add danh sach file dinh kem o Master
        List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
        if (objReq.getFileDinhKemReq() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhDxuatKhLcntHdr dataDTB = qOptional.get();
        HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

        updateObjectToObject(dataDTB, dataMap);

        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSua(getUser().getUsername());
        dataDTB.setFileDinhKems(fileDinhKemList);

        hhDxuatKhLcntHdrRepository.save(dataDTB);

        if (objReq.getLoaiVthh() != null && !objReq.getLoaiVthh().startsWith("02")) {
            saveDetailLuongThuc(objReq, dataMap.getId());
        } else {
            saveDetail(objReq, dataMap.getId());
        }

        // Xóa tât cả các căn cứ xác định giá cũ và lưu mới
        hhDxuatKhLcntCcxdgDtlRepository.deleteAllByIdDxKhlcnt(dataDTB.getId());
        for (HhDxuatKhLcntCcxdgDtlReq cc : objReq.getCcXdgReq()) {
            HhDxuatKhLcntCcxdgDtl data = ObjectMapperUtils.map(cc, HhDxuatKhLcntCcxdgDtl.class);
            List<FileDKemJoinDxKhLcntCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhLcntCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhLcntCcxdgDtl.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdDxKhlcnt(dataDTB.getId());

            hhDxuatKhLcntCcxdgDtlRepository.save(data);
        }

        return dataDTB;
    }

    @Override
    public HhDxuatKhLcntHdr detail(Long ids) throws Exception {

        Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(ids);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenDviLapDx(mapDmucDvi.get(qOptional.get().getMaDviLapDx()) + " - " + mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setCcXdgDtlList(hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(qOptional.get().getId()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setDsGtDtlList(getDsGthau(qOptional.get().getId(), mapVthh, mapDmucDvi));
        return qOptional.get();
    }

    private List<HhDxKhlcntDsgthau> getDsGthau (Long dxId, Map<String, String> mapVthh, Map<String, String> mapDmucDvi) {
        List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcntOrderByGoiThau(dxId);
        for (HhDxKhlcntDsgthau dsG : dsGthauList) {
            dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
            dsG.setTenCloaiVthh(mapVthh.get(dsG.getCloaiVthh()));
            if (dsG.getDonGiaVat() != null && dsG.getSoLuong() !=null) {
                dsG.setDonGiaVatStr(docxToPdfConverter.convertBigDecimalToStr(dsG.getDonGiaVat()));
                dsG.setThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(dsG.getDonGiaVat().multiply(dsG.getSoLuong())));
            }
            List<HhDxKhlcntDsgthauCtiet> listDdNhap = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
            listDdNhap.forEach(f -> {
                f.setGoiThau(dsG.getGoiThau());
                f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : mapDmucDvi.get(f.getMaDiemKho()));
                if (dsG.getDonGiaVat() != null) {
                    f.setThanhTien(dsG.getDonGiaVat().multiply(f.getSoLuong()));
                    f.setThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(dsG.getDonGiaVat().multiply(f.getSoLuong())));
                }
                List<HhDxKhlcntDsgthauCtietVt> byIdGoiThauCtiet = hhDxKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(f.getId());
                byIdGoiThauCtiet.forEach(x -> {
                    x.setTenDvi(StringUtils.isEmpty(x.getMaDvi()) ? null : mapDmucDvi.get(x.getMaDvi()));
                    List<HhDxKhlcntDsgthauCtietVt1> byIdGoiThauCtiet1 = hhDxKhlcntDsgthauCtietVt1Repository.findByIdGoiThauCtietVt(x.getId());
                    byIdGoiThauCtiet1.forEach(y -> {
                        y.setTenDvi(StringUtils.isEmpty(y.getMaDvi()) ? null : mapDmucDvi.get(y.getMaDvi()));
                    });
                    x.setChildren(byIdGoiThauCtiet1);
                });
                f.setChildren(byIdGoiThauCtiet);
            });
            dsG.setChildren(listDdNhap);
        }
        return dsGthauList;
    }

    @Override
    public HhDxuatKhLcntHdr detail(String soDx) throws Exception {
        Optional<HhDxuatKhLcntHdr> data = hhDxuatKhLcntHdrRepository.findBySoDxuat(soDx);
        return data.get();
    }

//    @Override
//    public ReportTemplateResponse preview(HhDxuatKhLcntHdrReq hhDxuatKhLcntHdrReq) throws Exception {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        ReportTemplate model = findByTenFile(hhDxuatKhLcntHdrReq.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//        List<String> listDvi = new ArrayList<>();
//        listDvi.add(hhDxuatKhLcntHdrReq.getMaDvi());
//        listDvi.add(hhDxuatKhLcntHdrReq.getMaDvi().substring(0, hhDxuatKhLcntHdrReq.getMaDvi().length() - 2));
//
//        HhDxuatKhLcntHdrPreview object = new ModelMapper().map(hhDxuatKhLcntHdrReq, HhDxuatKhLcntHdrPreview.class);
//        Map<String, DmDonViDTO> mapDmucDvi = getListDanhMucDviByMadviIn(listDvi, "01");
//        Map<String, String> mapVthh = getListDanhMucHangHoa();
//        Map<String, String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
//        Map<String, String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
//        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
//        Map<String, String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
//
//        object.setTenDvi(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi()).getTenDvi());
//        object.setDiaChiDvi(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi()).getDiaChi());
//        object.setTenDviCha(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi().substring(0, hhDxuatKhLcntHdrReq.getMaDvi().length() - 2)).getTenDvi());
//        object.setTenLoaiVthh(mapVthh.get(hhDxuatKhLcntHdrReq.getLoaiVthh()));
//        object.setTenCloaiVthh(mapVthh.get(hhDxuatKhLcntHdrReq.getCloaiVthh()));
//        object.setTenHthucLcnt(hashMapHtLcnt.get(hhDxuatKhLcntHdrReq.getHthucLcnt()));
//        object.setTenNguonVon(hashMapNguonVon.get(hhDxuatKhLcntHdrReq.getNguonVon()));
//        object.setTenLoaiHdong(hashMapLoaiHdong.get(hhDxuatKhLcntHdrReq.getLoaiHdong()));
//        object.setTenPthucLcnt(hashMapPthucDthau.get(hhDxuatKhLcntHdrReq.getPthucLcnt()));
//        object.getFileDinhKems().addAll(hhDxuatKhLcntHdrReq.getFileDinhKemReq());
//        object.setTgianBdauTchuc(hhDxuatKhLcntHdrReq.getTgianBdauTchuc() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianBdauTchuc()) : null);
//        object.setTgianDthau(hhDxuatKhLcntHdrReq.getTgianDthau() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianDthau()) : null);
//        object.setTgianMthau(hhDxuatKhLcntHdrReq.getTgianMthau() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianMthau()) : null);
//        object.setTgianNhang(hhDxuatKhLcntHdrReq.getTgianNhang() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianNhang()) : null);
//        object.setSoGoiThau(hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(hhDxuatKhLcntHdrReq.getId()));
//        String diaDiem = "";
//        if (object.getId() != null) {
//            List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(object.getId());
//            for (HhDxKhlcntDsgthau dsG : dsGthauList) {
//                diaDiem = "";
//                HhDxKhlcntDsgthauReport data = new ModelMapper().map(dsG, HhDxKhlcntDsgthauReport.class);
//                object.setTongSl(docxToPdfConverter.convertNullToZero(object.getTongSl()) + docxToPdfConverter.convertNullToZero(data.getSoLuong()));
//                object.setTongThanhTien(docxToPdfConverter.convertNullToZero(object.getTongThanhTien()).add((docxToPdfConverter.convertNullToZero(BigDecimal.valueOf(data.getSoLuong())).multiply(docxToPdfConverter.convertNullToZero(dsG.getDonGiaTamTinh())).multiply(BigDecimal.valueOf(1000)))));
//                data.setThanhTien(docxToPdfConverter.convertNullToZero(BigDecimal.valueOf(data.getSoLuong())).multiply(dsG.getDonGiaTamTinh()).multiply(BigDecimal.valueOf(1000)));
//                List<HhDxKhlcntDsgthauCtiet> dsCtiet = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
//                for (HhDxKhlcntDsgthauCtiet dsCt : dsCtiet) {
//                    List<String>  str = new ArrayList<>(Arrays.asList(dsCt.getDiaDiemNhap().split(",")));
//                    for (int i = 0; i < str.size(); i++) {
//                        diaDiem = diaDiem + str.get(i) + " - ";
//                    }
//                }
//                data.setDiaDiemNhapKho(diaDiem + object.getTenDvi() + " - " + object.getDiaChiDvi());
//                object.getDsGtDtlList().add(data);
//            }
//        }
//        Map<String, String> fieldValues = new HashMap<>();
//        fieldValues = docxToPdfConverter.convertObjectToMap(object);
//        List<String> tenCanCuPhapLy = new ArrayList<>();
//        if (object.getFileDinhKems().size() > 0) {
//            for (FileDinhKemReq ten : object.getFileDinhKems()) {
//                tenCanCuPhapLy.add(ten.getNoiDung());
//            }
//        }
//        StringBuilder fileDinhKems = new StringBuilder();
//        for (String ten : tenCanCuPhapLy) {
//            fileDinhKems.append("- ").append(ten).append("\n");
//        }
//        String fileDinhKemsTextString = fileDinhKems.toString();
//        fieldValues.put("fileDinhKems", fileDinhKemsTextString);
//
//        List<String> dsGthau = new ArrayList<>();
//        if (object.getListDsGthau().size() > 0) {
//            for (ListDsGthauDTO ten : object.getListDsGthau()) {
//                dsGthau.add(ten.getTenDvi() + ": " + ten.getSoLuong());
//            }
//        }
//        StringBuilder listDsGthau = new StringBuilder();
//        for (String ten : dsGthau) {
//            listDsGthau.append("+ ").append(ten).append(" tấn").append("\n");
//        }
//        String listDsGthauTextString = listDsGthau.toString();
//        fieldValues.put("listDsGthau", listDsGthauTextString);
//
//        List<String> tableValues = docxToPdfConverter.convertDataReplaceToTable(object.getDsGtDtlList());
//        System.out.println(tableValues);
//        return docxToPdfConverter.convertDocxToPdf(inputStream, fieldValues, tableValues);
//    }

    @Override
    public ReportTemplateResponse preview(HhDxuatKhLcntHdrReq hhDxuatKhLcntHdrReq) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ReportTemplate model = findByTenFile(hhDxuatKhLcntHdrReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

        List<String> listDvi = new ArrayList<>();
        listDvi.add(hhDxuatKhLcntHdrReq.getMaDvi());
        listDvi.add(hhDxuatKhLcntHdrReq.getMaDvi().substring(0, hhDxuatKhLcntHdrReq.getMaDvi().length() - 2));

        HhDxuatKhLcntHdrPreview object = new ModelMapper().map(hhDxuatKhLcntHdrReq, HhDxuatKhLcntHdrPreview.class);
        Map<String, DmDonViDTO> mapDmucDvi = getListDanhMucDviByMadviIn(listDvi, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
        Map<String, String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
        Map<String, String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");

        object.setTenDvi(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi()).getTenDvi());
        object.setDiaChiDvi(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi()).getDiaChi());
        object.setTenDviCha(mapDmucDvi.get(hhDxuatKhLcntHdrReq.getMaDvi().substring(0, hhDxuatKhLcntHdrReq.getMaDvi().length() - 2)).getTenDvi());
        object.setTenLoaiVthh(mapVthh.get(hhDxuatKhLcntHdrReq.getLoaiVthh()));
        object.setTenCloaiVthh(mapVthh.get(hhDxuatKhLcntHdrReq.getCloaiVthh()));
        object.setTenHthucLcnt(hashMapHtLcnt.get(hhDxuatKhLcntHdrReq.getHthucLcnt()));
        object.setTenNguonVon(hashMapNguonVon.get(hhDxuatKhLcntHdrReq.getNguonVon()));
        object.setTenLoaiHdong(hashMapLoaiHdong.get(hhDxuatKhLcntHdrReq.getLoaiHdong()));
        object.setTenPthucLcnt(hashMapPthucDthau.get(hhDxuatKhLcntHdrReq.getPthucLcnt()));
        object.getFileDinhKems().addAll(hhDxuatKhLcntHdrReq.getFileDinhKemReq());
        object.setTgianBdauTchuc(hhDxuatKhLcntHdrReq.getTgianBdauTchuc() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianBdauTchuc()) : null);
        object.setTgianDthau(hhDxuatKhLcntHdrReq.getTgianDthau() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianDthau()) : null);
        object.setTgianMthau(hhDxuatKhLcntHdrReq.getTgianMthau() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianMthau()) : null);
        object.setTgianNhang(hhDxuatKhLcntHdrReq.getTgianNhang() != null ? formatter.format(hhDxuatKhLcntHdrReq.getTgianNhang()) : null);
        object.setSoGoiThau(hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(hhDxuatKhLcntHdrReq.getId()));
        String diaDiem = "";
        if (object.getId() != null) {
            List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcntOrderByGoiThau(object.getId());
            for (HhDxKhlcntDsgthau dsG : dsGthauList) {
                diaDiem = "";
                HhDxKhlcntDsgthauReport data = new ModelMapper().map(dsG, HhDxKhlcntDsgthauReport.class);
                object.setTongSl(docxToPdfConverter.convertNullToZero(object.getTongSl()) + docxToPdfConverter.convertNullToZero(data.getSoLuong()));
                object.setTongThanhTien(docxToPdfConverter.convertNullToZero(object.getTongThanhTien()).add((docxToPdfConverter.convertNullToZero(BigDecimal.valueOf(data.getSoLuong())).multiply(docxToPdfConverter.convertNullToZero(dsG.getDonGiaTamTinh())).multiply(BigDecimal.valueOf(1000)))));
                data.setThanhTien(docxToPdfConverter.convertNullToZero(BigDecimal.valueOf(data.getSoLuong())).multiply(dsG.getDonGiaTamTinh()).multiply(BigDecimal.valueOf(1000)));
                List<HhDxKhlcntDsgthauCtiet> dsCtiet = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
                for (HhDxKhlcntDsgthauCtiet dsCt : dsCtiet) {
                    List<String>  str = new ArrayList<>(Arrays.asList(dsCt.getDiaDiemNhap().split(",")));
                    for (int i = 0; i < str.size(); i++) {
                        diaDiem = diaDiem + str.get(i) + " - ";
                    }
                }
                data.setDiaDiemNhapKho(diaDiem + object.getTenDvi() + " - " + object.getDiaChiDvi());
                object.getDsGtDtlList().add(data);
            }
        }

        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

    @Override
    public ReportTemplateResponse previewVt(HhDxuatKhLcntHdrReq hhDxuatKhLcntHdrReq) throws Exception {
        Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(hhDxuatKhLcntHdrReq.getId());
        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }
        Map<String, String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
        Map<String, String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
        ReportTemplate model = findByTenFile(hhDxuatKhLcntHdrReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        HhDxuatKhLcntHdrPreview object = new ModelMapper().map(qOptional.get(), HhDxuatKhLcntHdrPreview.class);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<HhDxKhlcntDsgthau> dsgthau = getDsGthau(hhDxuatKhLcntHdrReq.getId(), mapVthh, mapDmucDvi);
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        BigDecimal tongThucHien = BigDecimal.ZERO;
        BigDecimal tongDx = BigDecimal.ZERO;
        for (HhDxKhlcntDsgthau hhDxKhlcntDsgthau : dsgthau) {
            for (HhDxKhlcntDsgthauCtiet child : hhDxKhlcntDsgthau.getChildren()) {
                tongThanhTien = tongThanhTien.add(child.getThanhTien());
                tongThucHien = tongThucHien.add(child.getSoLuongDaMua());
                tongDx = tongDx.add(child.getSoLuong());
            }
        }
        object.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStrNotDecimal(tongThanhTien));
        object.setTongThucHien(docxToPdfConverter.convertBigDecimalToStrNotDecimal(tongThucHien));
        object.setTongDeXuat(docxToPdfConverter.convertBigDecimalToStrNotDecimal(tongDx));
        object.setTenHthucLcnt(StringUtils.isEmpty(object.getHthucLcnt()) ? null : hashMapHtLcnt.get(object.getHthucLcnt()));
        object.setTenPthucLcnt(StringUtils.isEmpty(object.getPthucLcnt()) ? null : hashMapPthucDthau.get(object.getPthucLcnt()));
        object.setDsGtVt(dsgthau);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }


    @Override
    public Page<HhDxuatKhLcntHdr> colection(HhDxuatKhLcntSearchReq objReq, HttpServletRequest req) throws Exception {
        int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
        int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
        Pageable pageable = PageRequest.of(page, limit);

        Page<HhDxuatKhLcntHdr> qhKho = hhDxuatKhLcntHdrRepository
                .findAll(HhDxuatKhLcntSpecification.buildSearchQuery(objReq), pageable);

        // Lay danh muc dung chung
        Map<String, String> mapDmucDvi = getMapTenDvi();
        for (HhDxuatKhLcntHdr hdr : qhKho.getContent()) {
            hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
        }

        return qhKho;
    }

    @Override
    @Transactional
    public HhDxuatKhLcntHdr approve(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        HhDxuatKhLcntHdr optional = this.detail(stReq.getId());

        if (optional.getLoaiVthh().startsWith("02")) {
            String status = stReq.getTrangThai() + optional.getTrangThai();
            switch (status) {
                case Contains.CHODUYET_LDV + Contains.DUTHAO:
                    optional.setNguoiGuiDuyet(getUser().getUsername());
                    optional.setNgayGuiDuyet(getDateTimeNow());
                    break;
                case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                    optional.setNguoiGuiDuyet(getUser().getUsername());
                    optional.setNgayGuiDuyet(getDateTimeNow());
                    break;
                case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    optional.setLdoTuchoi(stReq.getLyDo());
                    break;
                case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        } else {
            String status = stReq.getTrangThai() + optional.getTrangThai();
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                    this.validateData(optional, Contains.CHODUYET_TP);
                    optional.setNguoiGuiDuyet(getUser().getUsername());
                    optional.setNgayGuiDuyet(getDateTimeNow());
                    break;
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                    this.validateData(optional, Contains.CHODUYET_TP);
                    optional.setNguoiGuiDuyet(getUser().getUsername());
                    optional.setNgayGuiDuyet(getDateTimeNow());
                    break;
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    this.validateData(optional, Contains.CHODUYET_TP);
                    optional.setNguoiGuiDuyet(getUser().getUsername());
                    optional.setNgayGuiDuyet(getDateTimeNow());
                    break;
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    optional.setLdoTuchoi(stReq.getLyDo());
                    break;
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    optional.setLdoTuchoi(stReq.getLyDo());
                    break;
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                    this.validateData(optional, stReq.getTrangThai());
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    break;
                case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                    this.validateData(optional, stReq.getTrangThai());
                    optional.setNguoiPduyet(getUser().getUsername());
                    optional.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        }

        optional.setTrangThai(stReq.getTrangThai());
        return hhDxuatKhLcntHdrRepository.save(optional);
    }

    @Override
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        Optional<HhDxuatKhLcntHdr> optional = hhDxuatKhLcntHdrRepository.findById(idSearchReq.getId());

        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        List<HhDxKhlcntDsgthau> goiThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(idSearchReq.getId());
        if (goiThau != null && goiThau.size() > 0) {
            for (HhDxKhlcntDsgthau ct : goiThau) {
                List<HhDxKhlcntDsgthauCtiet> dsCtiet = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(ct.getId());
                hhDxKhlcntDsgthauCtietRepository.deleteAll(dsCtiet);
                for (HhDxKhlcntDsgthauCtiet ctiet : dsCtiet) {
                    List<HhDxKhlcntDsgthauCtietVt> dsCtietVt = hhDxKhlcntDsgthauCtietVtRepository.findByIdGoiThauCtiet(ctiet.getId());
                    hhDxKhlcntDsgthauCtietVtRepository.deleteAll(dsCtietVt);
                    for (HhDxKhlcntDsgthauCtietVt ctietVt : dsCtietVt) {
                        hhDxKhlcntDsgthauCtietVt1Repository.deleteAllByIdGoiThauCtietVt(ctietVt.getId());
                    }
                }
            }
            hhDxuatKhLcntDsgtDtlRepository.deleteAll(goiThau);
        }
        hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(idSearchReq.getId());
        List<HhDxuatKhLcntCcxdgDtl> cc = hhDxuatKhLcntCcxdgDtlRepository.findByIdDxKhlcnt(idSearchReq.getId());
        if (cc != null && cc.size() > 0) {
            hhDxuatKhLcntCcxdgDtlRepository.deleteAll(cc);
        }
        hhDxuatKhLcntHdrRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhDxuatKhLcntHdr> listDx = hhDxuatKhLcntHdrRepository.findByIdIn(idSearchReq.getIdList());
        if (listDx.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhDxuatKhLcntHdr dx : listDx) {
            if (!dx.getTrangThai().equals(Contains.DUTHAO)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDV)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            List<HhDxKhlcntDsgthau> goiThau = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcnt(dx.getId());
            List<Long> listGoiThau = goiThau.stream().map(HhDxKhlcntDsgthau::getId).collect(Collectors.toList());
            hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThauIn(listGoiThau);
        }
        hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcntIn(idSearchReq.getIdList());
        hhDxuatKhLcntCcxdgDtlRepository.deleteAllByIdDxKhlcntIn(idSearchReq.getIdList());
        hhDxuatKhLcntHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());

    }

    @Override
    public void exportToExcel(IdSearchReq searchReq, HttpServletResponse response) throws Exception {
        // Tao form excel
        String title = "Danh sách gói thầu";
        String[] rowsName = new String[]{"STT", "Gói thầu", "Số lượng (tấn)", "Địa điểm nhập kho",
                "Đơn giá (đồng/kg)", "Thành tiền (đồng)", "Bằng chữ"};
//		List<HhDxuatKhLcntDsgtDtl> dsgtDtls = hhDxuatKhLcntDsgtDtlRepository.findByIdHdr(searchReq.getId());

        List<HhDxKhlcntDsgthau> dsgtDtls = null;

        if (dsgtDtls.isEmpty())
            throw new UnsupportedOperationException("Không tìm thấy dữ liệu");

//		String filename = "Dexuat_Danhsachgoithau_" + dsgtDtls.get(0).getParent().getSoDxuat() + ".xlsx";
        String filename = "todo";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < dsgtDtls.size(); i++) {
            HhDxKhlcntDsgthau dsgtDtl = dsgtDtls.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dsgtDtl.getGoiThau();
            objs[2] = dsgtDtl.getSoLuong().multiply(Contains.getDVTinh(Contains.DVT_KG))
                    .divide(Contains.getDVTinh(Contains.DVT_TAN)).setScale(0, RoundingMode.HALF_UP);
//			objs[3] = dsgtDtl.getDiaDiemNhap();
            objs[4] = dsgtDtl.getDonGiaVat();
            objs[5] = dsgtDtl.getThanhTien();
            objs[6] = MoneyConvert.doctienBangChu(dsgtDtl.getThanhTien().toString(), "");
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public void exportDsKhlcnt(HhDxuatKhLcntSearchReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhDxuatKhLcntHdr> page = this.timKiem(req);
        List<HhDxuatKhLcntHdr> data = page.getContent();

        String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
        String[] rowsName = new String[]{"STT", "Số tờ trình", "Ngày đề xuất", "Trích yếu", "Số QĐ giao chỉ tiêu", "Năm kế hoạch", "Hàng hóa", "Chủng loại hàng hóa", "Số gói thầu", "Trạng thái của đề xuất"};
        String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxuatKhLcntHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoDxuat();
            objs[2] = dx.getNgayGuiDuyet();
            objs[3] = dx.getTrichYeu();
            objs[4] = dx.getSoQd();
            objs[5] = dx.getNamKhoach();
            objs[6] = dx.getLoaiVthh();
            objs[7] = dx.getCloaiVthh();
            objs[8] = dx.getSoGoiThau();
            objs[9] = dx.getTrangThai();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public void exportList(HhDxuatKhLcntSearchReq objReq, HttpServletResponse response) throws Exception {

        String cDvi = getUser().getCapDvi();
        if (Contains.CAP_TONG_CUC.equals(cDvi)) {
            this.exportToExcelTongCuc(objReq, response);
        } else {
            this.exportToExcelCuc(objReq, response);
        }
    }

    public void exportToExcelTongCuc(HhDxuatKhLcntSearchReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhDxuatKhLcntHdr> page = this.timKiem(objReq);
        List<HhDxuatKhLcntHdr> data = page.getContent();
        String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau_tong_cuc.xlsx";
        String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
        String[] rowsName;
        if (objReq.getLoaiVthh().startsWith("02")) {
            rowsName = new String[]{"STT", "Số kế hoạch/ tờ trình", "Năm kế hoạch", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ giao chỉ tiêu", "Loại hàng DTQG",
                    "Tổng số gói thầu", "Số gói thầu đã trúng", "SL HĐ đã ký", "Số QĐ duyệt KHLCNT", "Thời hạn thực hiện dự án", "Trạng thái đề xuất"};
        } else {
            rowsName = new String[]{"STT", "Số tờ trình", "Năm kế hoạch", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ giao chỉ tiêu", "Loại hàng DTQG",
                    "Chủng loại hàng DQTG", "Tổng số gói thầu", "Số gói thầu đã trúng", "SL HĐ đã ký", "Số QĐ duyệt KHLCNT", "Thời hạn nhập kho", "Trạng thái đề xuất", "Mã tổng hợp"};
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxuatKhLcntHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoDxuat();
            objs[2] = dx.getNamKhoach();
            objs[3] = convertDate(dx.getNgayTao());
            objs[4] = convertDate(dx.getNgayPduyet());
            objs[5] = dx.getSoQd();
            objs[6] = dx.getTenLoaiVthh();
            if (objReq.getLoaiVthh().startsWith("02")) {
                objs[7] = dx.getSoGoiThau();
                objs[8] = dx.getSoGthauTrung();
                objs[10] = dx.getSoQdPdKqLcnt();
                objs[11] = dx.getTgianNhang();
                objs[12] = dx.getTenTrangThai();
            } else {
                objs[7] = dx.getTenCloaiVthh();
                objs[8] = dx.getSoGoiThau();
                objs[9] = dx.getSoGthauTrung();
                objs[11] = dx.getSoQdPdKqLcnt();
                objs[12] = dx.getTgianNhang();
                objs[13] = dx.getTenTrangThai();
                if (dx.getTrangThaiTh().equals(Contains.CHUATONGHOP)) {
                    objs[14] = dx.getTenTrangThaiTh();
                } else {
                    objs[14] = dx.getMaTh();
                }
            }
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    public void exportToExcelCuc(HhDxuatKhLcntSearchReq objReq, HttpServletResponse response) throws Exception {

        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhDxuatKhLcntHdr> page = this.timKiem(objReq);
        List<HhDxuatKhLcntHdr> data = page.getContent();

        String title = "Danh sách kế hoạch đề xuất lựa chọn nhà thầu";
        String[] rowsName = new String[]{"STT", "Số kế hoạch/ tờ trình", "Năm kế hoạch", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ giao chỉ tiêu", "Loại hàng hóa",
                "Chủng loại hàng hóa", "Tổng số gói thầu", "Số gói thầu đã trúng", "SL HĐ đã ký", "Số QĐ duyệt KHLCNT", "Thời hạn nhập kho", "Trạng thái"};
        String filename = "Danh_sach_ke_hoach_de_xuat_lua_chon_nha_thau_cuc.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxuatKhLcntHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoDxuat();
            objs[2] = dx.getNamKhoach();
            objs[3] = convertDate(dx.getNgayTao());
            objs[4] = convertDate(dx.getNgayPduyet());
            objs[5] = dx.getSoQd();
            objs[6] = dx.getTenLoaiVthh();
            objs[7] = dx.getTenCloaiVthh();
            objs[8] = dx.getSoGoiThau();
            objs[9] = dx.getSoGthauTrung();
            objs[11] = dx.getSoQdPdKqLcnt();
            objs[12] = dx.getTgianNhang();
            objs[13] = dx.getTenTrangThai();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public Page<HhDxuatKhLcntHdr> timKiem(HhDxuatKhLcntSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxuatKhLcntHdr> page = hhDxuatKhLcntHdrRepository.select(req.getNamKh(), req.getSoTr(), req.getSoQd(), convertFullDateToString(req.getTuNgayKy()), convertFullDateToString(req.getDenNgayKy()), convertFullDateToString(req.getTuNgayTao()), convertFullDateToString(req.getDenNgayTao()), req.getLoaiVthh(), req.getTrichYeu(), req.getTrangThai(), req.getTrangThaiTh(), req.getMaDvi(), pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<Long> ids = page.getContent().stream().map(HhDxuatKhLcntHdr::getId).collect(Collectors.toList());
        List<Object[]> listGthau;
        if (req.getLoaiVthh() != null && req.getLoaiVthh().startsWith("02")) {
            listGthau = hhDxuatKhLcntHdrRepository.getQdPdKhLcntVt(ids, NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
        } else {
            listGthau = hhDxuatKhLcntHdrRepository.getQdPdKhLcnt(ids, NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
        }
        Map<Long, Map<String, String>> hhDxKhlcnt = new HashMap<>();
        for (Object[] it : listGthau) {
            Map<String, String> dataQd = new HashMap<>();
            dataQd.put("idQdPdKqLcnt", it[1].toString());
            dataQd.put("soQdPdKqLcnt", it[2].toString());
            dataQd.put("soGthauTrung", it[3].toString());
            hhDxKhlcnt.put(Long.parseLong(it[0].toString()), dataQd);
        }
        page.getContent().forEach(f -> {
            f.setQdGiaoChiTieuId(hhDxuatKhLcntHdrRepository.getIdByKhLcnt(f.getId(), f.getNamKhoach()));
            Optional<HhDxKhLcntThopDtl> thopDtl = hhDxKhLcntThopDtlRepository.findByIdDxHdr(f.getId());
            thopDtl.ifPresent(hhDxKhLcntThopDtl -> f.setMaTh(hhDxKhLcntThopDtl.getIdThopHdr()));
            f.setSoGoiThau(hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(f.getId()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : mapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            Map<String, String> stringStringMap = hhDxKhlcnt.get(f.getId());
            if (!ObjectUtils.isEmpty(stringStringMap)) {
                f.setIdQdPdKqLcnt(Long.parseLong(stringStringMap.get("idQdPdKqLcnt")));
                f.setSoQdPdKqLcnt(stringStringMap.get("soQdPdKqLcnt"));
                f.setSoGthauTrung(Integer.valueOf(stringStringMap.get("soGthauTrung")));
            }
        });
        return page;
    }

    @Override
    @Transactional
    public HhDxuatKhLcntHdr createVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception {

        if (!StringUtils.isEmpty(objReq.getSoDxuat())) {
            Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
            if (qOptional.isPresent()) {
                throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
            }
        }


        // Add danh sach file dinh kem o Master
        List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
        if (objReq.getFileDinhKemReq() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhDxuatKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhLcntHdr.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.TAO_MOI);
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setFileDinhKems(fileDinhKemList);
        dataMap.setTrangThaiTh(Contains.CHUATAO_QD);

        hhDxuatKhLcntHdrRepository.save(dataMap);
        // Lưu danh sách gói thầu
        for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()) {
            HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
            data.setIdDxKhlcnt(dataMap.getId());
            BigDecimal thanhTien = data.getDonGiaVat().multiply(data.getSoLuong());
            data.setThanhTien(thanhTien);
            hhDxuatKhLcntDsgtDtlRepository.save(data);
            // Lưu chi tiết danh sách gói thaauff ( địa điểm nhập )
            for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()) {
                HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
                dataDdNhap.setIdGoiThau(data.getId());
//				dataDdNhap.setThanhTien(dataDdNhap.getDonGia().multiply(dataDdNhap.getSoLuong()));
                hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
            }
        }

        // Add danh sach goi thau
//		List<HhDxuatKhLcntDsgtDtl> dtls2 = ObjectMapperUtils.mapAll(objReq.getChildren2(), HhDxuatKhLcntDsgtDtl.class);
//		UnitScaler.reverseFormatList(dtls2, Contains.DVT_TAN);

//		dataMap.setChildren2(dtls2);
//

//
//		for (int i = 0; i < dataMap.getChildren2().size(); i++){
//			HhDxuatKhLcntDsgtDtlReq dsgThau = objReq.getChildren2().get(i);
//			HhDxuatKhLcntDsgtDtl dsgThauSaved =  dataMap.getChildren2().get(i);
//			for (int j = 0; j < dsgThau.getDanhSachDiaDiemNhap().size(); j++){
//				HhDxuatKhLcntVtuDtlCtiet ddNhap = ObjectMapperUtils.map(dsgThau.getDanhSachDiaDiemNhap().get(j), HhDxuatKhLcntVtuDtlCtiet.class);
//				ddNhap.setIdGoiThau(dsgThauSaved.getId());
//				hhDxuatKhLcntVtuDtlCtietRepository.save(ddNhap);
//			}
//		}
        return dataMap;
    }

    @Override
    @Transactional
    public HhDxuatKhLcntHdr updateVatTu(HhDxuatKhLcntHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (!StringUtils.isEmpty(objReq.getSoDxuat())) {
            Optional<HhDxuatKhLcntHdr> deXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoDxuat());
            if (deXuat.isPresent()) {
                if (!deXuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
                }
            }
        }

        List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
        if (objReq.getFileDinhKemReq() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhLcntHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhDxuatKhLcntHdr dataDTB = qOptional.get();
        HhDxuatKhLcntHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhLcntHdr.class);

        updateObjectToObject(dataDTB, dataMap);
//
        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSua(getUser().getUsername());
        dataDTB.setFileDinhKems(fileDinhKemList);

        hhDxuatKhLcntHdrRepository.save(dataDTB);
        hhDxuatKhLcntDsgtDtlRepository.deleteAllByIdDxKhlcnt(dataDTB.getId());
        // Lưu danh sách gói thầu
        for (HhDxuatKhLcntDsgtDtlReq gt : objReq.getDsGtReq()) {
            HhDxKhlcntDsgthau data = new ModelMapper().map(gt, HhDxKhlcntDsgthau.class);
            hhDxKhlcntDsgthauCtietRepository.deleteAllByIdGoiThau(gt.getId());
            data.setId(null);
            data.setIdDxKhlcnt(dataDTB.getId());
            BigDecimal thanhTien = data.getDonGiaVat().multiply(data.getSoLuong());
            data.setThanhTien(thanhTien);
            hhDxuatKhLcntDsgtDtlRepository.save(data);
            // Lưu chi tiết danh sách gói thaauff ( địa điểm nhập )
            for (HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : gt.getChildren()) {
                HhDxKhlcntDsgthauCtiet dataDdNhap = new ModelMapper().map(ddNhap, HhDxKhlcntDsgthauCtiet.class);
                dataDdNhap.setId(null);
                dataDdNhap.setIdGoiThau(data.getId());
                hhDxKhlcntDsgthauCtietRepository.save(dataDdNhap);
            }
        }
        return dataDTB;
    }

    @Override
    public HhDxuatKhLcntHdr detailVatTu(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        }
        Optional<HhDxuatKhLcntHdr> qOptional = hhDxuatKhLcntHdrRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
        Map<String, String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
        Map<String, String> hashMapHtLcnt = getListDanhMucChung("HT_LCNT");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(StringUtils.isEmpty(qOptional.get().getMaDvi()) ? null : mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

        // Quy doi don vi kg = tan

//		UnitScaler.formatList(dtls2, Contains.DVT_TAN);


        List<HhDxKhlcntDsgthau> dsGthauList = hhDxuatKhLcntDsgtDtlRepository.findByIdDxKhlcntOrderByGoiThau(qOptional.get().getId());
        for (HhDxKhlcntDsgthau dsG : dsGthauList) {
            dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
            dsG.setTenHthucLcnt(StringUtils.isEmpty(dsG.getHthucLcnt()) ? null : hashMapHtLcnt.get(dsG.getHthucLcnt()));
            dsG.setTenPthucLcnt(StringUtils.isEmpty(dsG.getPthucLcnt()) ? null : hashMapPthucDthau.get(dsG.getPthucLcnt()));
            dsG.setTenLoaiHdong(StringUtils.isEmpty(dsG.getLoaiHdong()) ? null : hashMapLoaiHdong.get(dsG.getLoaiHdong()));
            dsG.setTenNguonVon(StringUtils.isEmpty(dsG.getNguonVon()) ? null : hashMapNguonVon.get(dsG.getNguonVon()));
            List<HhDxKhlcntDsgthauCtiet> listDdNhap = hhDxKhlcntDsgthauCtietRepository.findByIdGoiThau(dsG.getId());
            for (int i = 0; i < listDdNhap.size(); i++) {
                listDdNhap.get(i).setTenDvi(StringUtils.isEmpty(listDdNhap.get(i).getMaDvi()) ? null : mapDmucDvi.get(listDdNhap.get(i).getMaDvi()));
                listDdNhap.get(i).setTenDiemKho(StringUtils.isEmpty(listDdNhap.get(i).getMaDiemKho()) ? null : mapDmucDvi.get(listDdNhap.get(i).getMaDiemKho()));
            }
            dsG.setChildren(listDdNhap);
        }
        qOptional.get().setDsGtDtlList(dsGthauList);

        return qOptional.get();
    }

    @Override
    public Page<HhDxuatKhLcntHdr> selectDropdown(HhDxuatKhLcntSearchReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxuatKhLcntHdr> page = hhDxuatKhLcntHdrRepository.selectDropdown(req.getNamKh(), req.getMaDvi(), req.getListTrangThai(), req.getListTrangThaiTh(), pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        page.getContent().forEach(f -> {
            f.setSoGoiThau(hhDxuatKhLcntDsgtDtlRepository.countByIdDxKhlcnt(f.getId()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : mapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
        });
        return page;
    }

    @Override
    public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) throws Exception {
        return hhDxuatKhLcntHdrRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
    }

    @Override
    public BigDecimal getGiaBanToiDa(String cloaiVhtt, String maDvi, String namKhoach) {
        if (cloaiVhtt.startsWith("02")) {
            return hhDxuatKhLcntHdrRepository.getGiaBanToiDaVt(cloaiVhtt, namKhoach);
        }
        return hhDxuatKhLcntHdrRepository.getGiaBanToiDaLt(cloaiVhtt, maDvi, namKhoach);
    }
}
