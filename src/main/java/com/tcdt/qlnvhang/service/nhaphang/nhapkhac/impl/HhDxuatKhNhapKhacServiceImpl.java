package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhThopKhNhapKhacRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtlReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhDxuatKhNhapKhacService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class HhDxuatKhNhapKhacServiceImpl extends BaseServiceImpl implements HhDxuatKhNhapKhacService {
    private static final String CAN_CU = "_CAN_CU";
    @Autowired
    private HhDxuatKhNhapKhacHdrRepository hhDxuatKhNhapKhacHdrRepository;
    @Autowired
    private HhDxuatKhNhapKhacDtlRepository hhDxuatKhNhapKhacDtlRepository;
    @Autowired
    private HhThopKhNhapKhacRepository hhThopKhNhapKhacRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<HhDxuatKhNhapKhacHdr> timKiem(HhDxuatKhNhapKhacSearch req) throws Exception {
        req.setTuNgayDxuatStr(convertFullDateToString(req.getTuNgayDxuat()));
        req.setDenNgayDxuatStr(convertFullDateToString(req.getDenNgayDxuat()));
        req.setTuNgayDuyetStr(convertFullDateToString(req.getTuNgayDuyet()));
        req.setDenNgayDxuatStr(convertFullDateToString(req.getDenNgayDuyet()));
        UserInfo userInfo = UserUtils.getUserInfo();
        req.setDvql(userInfo.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhDxuatKhNhapKhacHdr> data = hhDxuatKhNhapKhacHdrRepository.search(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f -> {
            if(f.getThopId() != null){
                Optional<HhThopKhNhapKhac> thop = hhThopKhNhapKhacRepository.findById(f.getThopId());
                if (thop.isPresent()){
                    f.setMaTh(thop.get().getMaTh());
                    f.setTrangThaiTh(thop.get().getTrangThai());
                }
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            if (f.getTrangThaiTh() != null) {
                f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            } else {
                f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.CHUATONGHOP.getTen());
            }
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
            f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
        });
        return data;
    }

    @Override
    @Transactional
    public HhDxuatKhNhapKhacHdr themMoi(HhDxuatKhNhapKhacHdrReq req) throws Exception {
        if (!StringUtils.isEmpty(req.getSoDxuat())) {
            Optional<HhDxuatKhNhapKhacHdr> qOptional = hhDxuatKhNhapKhacHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (qOptional.isPresent()) {
                throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
            }
        }
        HhDxuatKhNhapKhacHdr dataMap = new ModelMapper().map(req, HhDxuatKhNhapKhacHdr.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
        HhDxuatKhNhapKhacHdr created = hhDxuatKhNhapKhacHdrRepository.save(dataMap);
        luuFile(req, created);
        luuChiTiet(req, created);
        return created;
    }

    @Override
    @Transactional
    public HhDxuatKhNhapKhacHdr capNhat(HhDxuatKhNhapKhacHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<HhDxuatKhNhapKhacHdr> qOptional = hhDxuatKhNhapKhacHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (!StringUtils.isEmpty(req.getSoDxuat())) {
            Optional<HhDxuatKhNhapKhacHdr> deXuat = hhDxuatKhNhapKhacHdrRepository.findBySoDxuat(req.getSoDxuat());
            if (deXuat.isPresent() && (!deXuat.get().getId().equals(req.getId()))) {
                throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");

            }
        }
        HhDxuatKhNhapKhacHdr data = qOptional.get();
        BeanUtils.copyProperties(req, data);
        data.setNgaySua(getDateTimeNow());
        data.setNguoiSua(getUser().getUsername());
        luuFile(req, data);
        luuChiTiet(req, data);
        return hhDxuatKhNhapKhacHdrRepository.save(data);
    }

    @Override
    public HhDxuatKhNhapKhacDTO chiTiet(Long id) throws Exception {
        Optional<HhDxuatKhNhapKhacHdr> qOptional = hhDxuatKhNhapKhacHdrRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDviDxuat()));
        qOptional.get().setTenLoaiHinhNx(mapLoaiHinhNx.get(qOptional.get().getLoaiHinhNx()));
        qOptional.get().setTenKieuNx(mapKieuNx.get(qOptional.get().getKieuNx()));
        qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThaiTh()));
        qOptional.get().setCanCuPhapLy(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhDxuatKhNhapKhacHdr.TABLE_NAME + CAN_CU)));
        qOptional.get().setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhDxuatKhNhapKhacHdr.TABLE_NAME)));
        List<HhDxuatKhNhapKhacDtl> dtls = hhDxuatKhNhapKhacDtlRepository.findAllByHdrId(qOptional.get().getId());
        dtls.forEach(dtl -> {
            dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
            dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
            dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
            dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
            dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
            dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
        });
        HhDxuatKhNhapKhacDTO data = new HhDxuatKhNhapKhacDTO();
        data.setHdr(qOptional.get());
        data.setDtl(dtls);
        return data;
    }

    @Override
    public HhDxuatKhNhapKhacHdr pheDuyet(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhDxuatKhNhapKhacHdr> qOptional = hhDxuatKhNhapKhacHdrRepository.findById(stReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = stReq.getTrangThai() + qOptional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                qOptional.get().setNguoiGuiDuyet(getUser().getUsername());
                qOptional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.TU_CHOI_CBV + Contains.DA_DUYET_LDC:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                qOptional.get().setLyDoTuChoi(stReq.getLyDo());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
            case Contains.DA_DUYET_CBV + Contains.DA_DUYET_LDC:
            case Contains.DA_TAO_CBV + Contains.DU_THAO:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        qOptional.get().setTrangThai(stReq.getTrangThai());
        return hhDxuatKhNhapKhacHdrRepository.save(qOptional.get());
    }

    @Override
    @Transactional
    public void xoa(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhDxuatKhNhapKhacHdr> qOptional = hhDxuatKhNhapKhacHdrRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)
                && !qOptional.get().getTrangThai().equals(Contains.TU_CHOI_CBV)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME + CAN_CU));
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME));
        hhDxuatKhNhapKhacDtlRepository.deleteAllByHdrId(qOptional.get().getId());
        hhDxuatKhNhapKhacHdrRepository.delete(qOptional.get());
    }

    @Override
    @Transactional
    public void xoaNhieu(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList())) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<HhDxuatKhNhapKhacHdr> listHdr = hhDxuatKhNhapKhacHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (listHdr.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhDxuatKhNhapKhacHdr hdr: listHdr) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_CBV)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME + CAN_CU));
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME));
            hhDxuatKhNhapKhacDtlRepository.deleteAllByHdrId(hdr.getId());
            hhDxuatKhNhapKhacHdrRepository.delete(hdr);
        }
    }

    @Override
    public void xuatFile(HhDxuatKhNhapKhacSearch req , HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhDxuatKhNhapKhacHdr> page = timKiem(req);
        List<HhDxuatKhNhapKhacHdr> data = page.getContent();
        String filename = "danh-sach-ke-hoach-nhap_khac.xlsx";
        String title = "Danh sách kế hoạch nhập khác";
        String[] rowsName;
        if (userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)){
            rowsName = new String[]{"STT",  "Năm kế hoạch","Số công văn/tờ trình", "Đơn vị đề xuất", "Ngày đề xuất", "Ngày duyệt đề xuất", "Loại hàng DTQG",
                    "Tổng SL đề xuất", "ĐVT", "Trích yếu", "Trạng thái đề xuất", "Trạng thái tổng hợp", "Mã tổng hợp"};
        } else {
            rowsName = new String[]{"STT",  "Năm kế hoạch","Số công văn/tờ trình", "Ngày đề xuất", "Ngày duyệt đề xuất", "Loại hàng DTQG",
                    "Tổng SL đề xuất", "ĐVT", "Trích yếu", "Trạng thái đề xuất"};
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxuatKhNhapKhacHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKhoach();
            objs[2] = dx.getSoDxuat();
            if (userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)){
                objs[3] = dx.getTenDvi();
                objs[4] = convertDate(dx.getNgayDxuat());
                objs[5] = convertDate(dx.getNgayPduyet());
                objs[6] = dx.getTenLoaiVthh();
                objs[7] = dx.getTongSlNhap();
                objs[8] = dx.getDvt();
                objs[9] = dx.getTrichYeu();
                objs[10] = dx.getTenTrangThai();
                objs[11] = dx.getTenTrangThaiTh();
                objs[12] = dx.getMaTh();
            } else {
                objs[3] = convertDate(dx.getNgayDxuat());
                objs[4] = convertDate(dx.getNgayPduyet());
                objs[5] = dx.getTenLoaiVthh();
                objs[6] = dx.getTongSlNhap();
                objs[7] = dx.getDvt();
                objs[8] = dx.getTrichYeu();
                objs[9] = dx.getTenTrangThai();
            }
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<HhDxuatKhNhapKhacDTO> findAllByTrangThaiAndTrangThaiTh() {
        List<String> trangThais = new ArrayList<>();
        trangThais.add(Contains.DA_DUYET_CBV);
        trangThais.add(Contains.DA_TAO_CBV);
        List<HhDxuatKhNhapKhacHdr> listHdr = hhDxuatKhNhapKhacHdrRepository.findAllByTrangThaiInAndTrangThaiTh(trangThais, Contains.CHUATONGHOP);
        List<HhDxuatKhNhapKhacDTO> result = new ArrayList<>();
        listHdr.forEach(item ->{
            HhDxuatKhNhapKhacDTO dataChild = new HhDxuatKhNhapKhacDTO();
            Map<String, String> mapVthh = getListDanhMucHangHoa();
            Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
            Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
            item.setTenLoaiVthh(mapVthh.get(item.getLoaiVthh()));
            item.setTenLoaiHinhNx(mapLoaiHinhNx.get(item.getLoaiHinhNx()));
            item.setTenKieuNx(mapKieuNx.get(item.getKieuNx()));
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDviDxuat()));
            dataChild.setHdr(item);
            List<HhDxuatKhNhapKhacDtl> listDtl = hhDxuatKhNhapKhacDtlRepository.findAllByHdrId(item.getId());
            listDtl.forEach(dtl ->{
                dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
                dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
                dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
                dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
                dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
                dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
            });
            dataChild.setDtl(listDtl);
            result.add(dataChild);
        });
        return result;
    }

    private void luuChiTiet(HhDxuatKhNhapKhacHdrReq req, HhDxuatKhNhapKhacHdr created) {
        hhDxuatKhNhapKhacDtlRepository.deleteAllByHdrId(created.getId());
        BigDecimal tongSlNhap = BigDecimal.ZERO;
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        if (req.getDetails() != null) {
            for (HhDxuatKhNhapKhacDtlReq dtl : req.getDetails()) {
                HhDxuatKhNhapKhacDtl dataChild = new ModelMapper().map(dtl, HhDxuatKhNhapKhacDtl.class);
                dataChild.setHdrId(created.getId());
                if (dataChild.getDonGia() != null) {
                    if (dataChild.getSlDoiThua() != null) {
                        tongSlNhap = tongSlNhap.add(dataChild.getSlDoiThua());
                        tongThanhTien = tongThanhTien.add(dataChild.getSlDoiThua().multiply(dataChild.getDonGia()));
                    } else if(dataChild.getSlTonKhoThucTe() != null) {
                        tongSlNhap = tongSlNhap.add(dataChild.getSlTonKhoThucTe().subtract(dataChild.getSlTonKho()) );
                        tongThanhTien = tongThanhTien.add((dataChild.getSlTonKhoThucTe().subtract(dataChild.getSlTonKho())).multiply(dataChild.getDonGia()));
                    } else if (dataChild.getSlNhap() != null) {
                        tongSlNhap = tongSlNhap.add(dataChild.getSlNhap());
                        tongThanhTien = tongThanhTien.add(dataChild.getSlNhap().multiply(dataChild.getDonGia()));
                    }
                }
                hhDxuatKhNhapKhacDtlRepository.save(dataChild);
            }
        }
        created.setTongSlNhap(tongSlNhap);
        created.setTongThanhTien(tongThanhTien);
        hhDxuatKhNhapKhacHdrRepository.save(created);
    }

    private void luuFile(HhDxuatKhNhapKhacHdrReq req, HhDxuatKhNhapKhacHdr created) {
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME + CAN_CU));
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhDxuatKhNhapKhacHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), HhDxuatKhNhapKhacHdr.TABLE_NAME + CAN_CU);
        }
        if (!DataUtils.isNullObject(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhDxuatKhNhapKhacHdr.TABLE_NAME);
        }
    }
}
