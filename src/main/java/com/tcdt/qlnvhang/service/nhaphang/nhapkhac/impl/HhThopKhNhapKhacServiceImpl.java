package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhQdPdNhapKhacHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhThopKhNhapKhacRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhThopKhNhapKhacService;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HhThopKhNhapKhacServiceImpl extends BaseServiceImpl implements HhThopKhNhapKhacService {
    @Autowired
    private HhThopKhNhapKhacRepository hhThopKhNhapKhacRepository;
    @Autowired
    private HhDxuatKhNhapKhacHdrRepository hhDxuatKhNhapKhacHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private HhDxuatKhNhapKhacDtlRepository hhDxuatKhNhapKhacDtlRepository;
    @Autowired
    private HhQdPdNhapKhacHdrRepository hhQdPdNhapKhacHdrRepository;

    @Override
    public Page<HhThopKhNhapKhac> timKiem(HhThopKhNhapKhacSearch req) {
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String,String> hashMapLoaiNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        req.setTuNgayThStr(convertFullDateToString(req.getTuNgayTh()));
        req.setDenNgayThStr(convertFullDateToString(req.getDenNgayTh()));
        req.setTuNgayKyStr(convertFullDateToString(req.getTuNgayKy()));
        req.setDenNgayKyStr(convertFullDateToString(req.getDenNgayKy()));
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhThopKhNhapKhac> data = hhThopKhNhapKhacRepository.search(req, pageable);
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
            List<HhDxuatKhNhapKhacHdr> listDxuat = hhDxuatKhNhapKhacHdrRepository.findAllByThopId(f.getId());
            listDxuat.forEach(i -> {
                i.setTenLoaiHinhNx(hashMapLoaiNx.get(i.getLoaiHinhNx()));
                i.setTenKieuNx(hashMapKieuNx.get(i.getKieuNx()));
                i.setTenLoaiVthh(mapVthh.get(i.getLoaiVthh()));
                i.setTenDvi(mapDmucDvi.get(i.getMaDviDxuat()));
                List<HhDxuatKhNhapKhacDtl> dtls = hhDxuatKhNhapKhacDtlRepository.findAllByHdrId(i.getId());
                dtls.forEach(dtl -> {
                    dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
                    dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
                    dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
                    dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
                    dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
                    dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
                });
                i.setDetails(dtls);
            });
            f.setDxHdr(listDxuat);
        });
        return data;
    }

    @Override
    public List<HhThopKhNhapKhac> layDsTongHopChuaTaoQd() {
        List<HhThopKhNhapKhac> thop = hhThopKhNhapKhacRepository.findAllByTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String,String> hashMapLoaiNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        thop.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
            List<HhDxuatKhNhapKhacHdr> listDxuat = hhDxuatKhNhapKhacHdrRepository.findAllByThopId(f.getId());
            listDxuat.forEach(i -> {
                i.setTenLoaiHinhNx(hashMapLoaiNx.get(i.getLoaiHinhNx()));
                i.setTenKieuNx(hashMapKieuNx.get(i.getKieuNx()));
                i.setTenLoaiVthh(mapVthh.get(i.getLoaiVthh()));
                i.setTenDvi(mapDmucDvi.get(i.getMaDviDxuat()));
                List<HhDxuatKhNhapKhacDtl> dtls = hhDxuatKhNhapKhacDtlRepository.findAllByHdrId(i.getId());
                dtls.forEach(dtl -> {
                    dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
                    dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
                    dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
                    dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
                    dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
                    dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
                });
                i.setDetails(dtls);
            });
            f.setDxHdr(listDxuat);
        });
        return thop;
    }

    @Override
    public List<HhDxuatKhNhapKhacHdr> layDsDxuatChuaTongHop(HhThopKhNhapKhacSearch req) {
        if (req.getNamKhoach() != null && req.getLoaiHinhNx() != null && req.getLoaiVthh() != null) {
            List<String> trangThai = new ArrayList<>();
            trangThai.add(Contains.DA_DUYET_CBV);
            trangThai.add(Contains.DA_TAO_CBV);
            List<HhDxuatKhNhapKhacHdr> data = hhDxuatKhNhapKhacHdrRepository.findAllByNamKhoachAndLoaiHinhNxAndLoaiVthhAndTrangThaiInAndTrangThaiTh(
                    req.getNamKhoach(), req.getLoaiHinhNx(), req.getLoaiVthh(), trangThai, Contains.CHUATONGHOP);
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
            data.forEach(f -> {
                f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
            });
            return data;
            }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public HhThopKhNhapKhac themMoi(HhThopKhNhapKhacReq req) throws Exception {
        HhThopKhNhapKhac dataMap = new ModelMapper().map(req, HhThopKhNhapKhac.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNgayTh(getDateTimeNow());
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setTrangThai(Contains.CHUATAO_QD);
        HhThopKhNhapKhac created = hhThopKhNhapKhacRepository.save(dataMap);
        luuFile(req, created);
        luuChiTiet(req, created);
        return created;
    }

    @Override
    public HhThopKhNhapKhac capNhat(HhThopKhNhapKhacReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }
        Optional<HhThopKhNhapKhac> hhThopKhNhapKhac = hhThopKhNhapKhacRepository.findById(req.getId());
        if (!hhThopKhNhapKhac.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        hhThopKhNhapKhac.get().setNgaySua(getDateTimeNow());
        hhThopKhNhapKhac.get().setNguoiSua(getUser().getUsername());
        hhThopKhNhapKhac.get().setNoiDungTh(req.getNoiDungTh());
        luuFile(req, hhThopKhNhapKhac.get());
        return hhThopKhNhapKhacRepository.save(hhThopKhNhapKhac.get());
    }

    @Override
    public HhThopKhNhapKhacDTO chiTiet(Long id) throws Exception {
        Optional<HhThopKhNhapKhac> hhThopKhNhapKhac = hhThopKhNhapKhacRepository.findById(id);
        if (!hhThopKhNhapKhac.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        HhThopKhNhapKhacDTO data = new HhThopKhNhapKhacDTO();
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String,String> hashMapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        hhThopKhNhapKhac.get().setTenLoaiHinhNx(mapLoaiHinhNx.get(hhThopKhNhapKhac.get().getLoaiHinhNx()));
        hhThopKhNhapKhac.get().setTenLoaiVthh(mapVthh.get(hhThopKhNhapKhac.get().getLoaiVthh()));
        hhThopKhNhapKhac.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(hhThopKhNhapKhac.get().getTrangThai()));
        hhThopKhNhapKhac.get().setFileDinhKems(fileDinhKemService.search(hhThopKhNhapKhac.get().getId(), Collections.singletonList(HhThopKhNhapKhac.TABLE_NAME)));
        data.setHdr(hhThopKhNhapKhac.get());
        List<HhDxuatKhNhapKhacHdr> dtls = hhDxuatKhNhapKhacHdrRepository.findAllByThopId(hhThopKhNhapKhac.get().getId());
        dtls.forEach(i -> {
            i.setTenLoaiHinhNx(mapLoaiHinhNx.get(i.getLoaiHinhNx()));
            i.setTenKieuNx(hashMapKieuNx.get(i.getKieuNx()));
            i.setTenLoaiVthh(mapVthh.get(i.getLoaiVthh()));
            i.setTenDvi(mapDmucDvi.get(i.getMaDviDxuat()));
            List<HhDxuatKhNhapKhacDtl> dxuatKhNhapKhacDtls = hhDxuatKhNhapKhacDtlRepository.findAllByHdrId(i.getId());
            dxuatKhNhapKhacDtls.forEach(dtl -> {
                dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
                dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
                dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
                dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
                dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
                dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
            });
            i.setDetails(dxuatKhNhapKhacDtls);
        });
        data.setDtl(dtls);
        return data;
    }

    @Override
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (idSearchReq.getId() == null) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhThopKhNhapKhac> hhThopKhNhapKhac = hhThopKhNhapKhacRepository.findById(idSearchReq.getId());
        if (!hhThopKhNhapKhac.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (hhThopKhNhapKhac.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
            throw new Exception("Chỉ thực hiện xóa với bản ghi chưa ban hành quyết định.");
        }
        List<HhDxuatKhNhapKhacHdr> dxuatKhNhapKhacHdrList =  hhDxuatKhNhapKhacHdrRepository.findAllByThopId(hhThopKhNhapKhac.get().getId());
        dxuatKhNhapKhacHdrList.forEach(item -> {
            item.setThopId(null);
            item.setTrangThaiTh(Contains.CHUATONGHOP);
            hhDxuatKhNhapKhacHdrRepository.save(item);
        });
        fileDinhKemService.delete(hhThopKhNhapKhac.get().getId(), Lists.newArrayList(HhThopKhNhapKhac.TABLE_NAME));
        Optional<HhQdPdNhapKhacHdr> qdPdNhapKhacHdr = hhQdPdNhapKhacHdrRepository.findByIdTh(hhThopKhNhapKhac.get().getId());
        if (qdPdNhapKhacHdr.isPresent() && !qdPdNhapKhacHdr.get().getTrangThai().equals(Contains.BAN_HANH)) {
            hhQdPdNhapKhacHdrRepository.delete(qdPdNhapKhacHdr.get());
        }
        hhThopKhNhapKhacRepository.delete(hhThopKhNhapKhac.get());
    }

    @Override
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhThopKhNhapKhac> list = hhThopKhNhapKhacRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhThopKhNhapKhac item : list) {
            if (!item.getTrangThai().equals(Contains.CHUATAO_QD)) {
                throw new Exception("Chỉ thực hiện xóa với bản ghi chưa được tạo quyết định.");
            }
            List<HhDxuatKhNhapKhacHdr> dxuatKhNhapKhacHdrList =  hhDxuatKhNhapKhacHdrRepository.findAllByThopId(item.getId());
            dxuatKhNhapKhacHdrList.forEach(data -> {
                data.setThopId(null);
                data.setTrangThaiTh(Contains.CHUATONGHOP);
                hhDxuatKhNhapKhacHdrRepository.save(data);
            });
            fileDinhKemService.delete(item.getId(), Lists.newArrayList(HhThopKhNhapKhac.TABLE_NAME));
        }
        hhThopKhNhapKhacRepository.deleteAll(list);
    }

    @Override
    public void exportList(HhThopKhNhapKhacSearch objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhThopKhNhapKhac> page = timKiem(objReq);
        List<HhThopKhNhapKhac> data = page.getContent();
        String filename = "Ds_tong_hop_dx_kh_nhap_khac.xlsx";
        String title = "Danh sách tổng hợp đề xuất kế hoạch nhập khác";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Mã tổng hợp", "Ngày tổng hợp", "Số quyết định",
        "Ngày ký quyết định", "Loại hàng hóa", "Tổng SL đề xuất", "ĐVT", "Nội dung tổng hợp", "Trạng thái"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhThopKhNhapKhac th = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = th.getNamKhoach();
            objs[2] = th.getMaTh();
            objs[3] = convertDate(th.getNgayTh());
            objs[4] = th.getSoQd();
            objs[5] = convertDate(th.getNgayKyQd());
            objs[6] = th.getTenLoaiVthh();
            objs[7] = th.getTongSlNhap();
            objs[8] = th.getDvt();
            objs[9] = th.getNoiDungTh();
            objs[10] = th.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    private void luuFile(HhThopKhNhapKhacReq req, HhThopKhNhapKhac created) {
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhThopKhNhapKhac.TABLE_NAME));
        if (!DataUtils.isNullObject(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhThopKhNhapKhac.TABLE_NAME);
        }
    }
    private void luuChiTiet(HhThopKhNhapKhacReq req, HhThopKhNhapKhac created) {
        BigDecimal tongSl = BigDecimal.ZERO;
        if (req.getDetails() != null && !req.getDetails().isEmpty()) {
            for (HhDxuatKhNhapKhacHdrReq item: req.getDetails()) {
                Optional<HhDxuatKhNhapKhacHdr> dx = hhDxuatKhNhapKhacHdrRepository.findById(item.getId());
                if(dx.isPresent()) {
                    dx.get().setThopId(created.getId());
                    dx.get().setTrangThaiTh(created.getTrangThai());
                    tongSl = tongSl.add(dx.get().getTongSlNhap());
                    created.setDvt(dx.get().getDvt());
                    hhDxuatKhNhapKhacHdrRepository.save(dx.get());
                }
            }
        }
        created.setTongSlNhap(tongSl);
        hhThopKhNhapKhacRepository.save(created);
    }
}
