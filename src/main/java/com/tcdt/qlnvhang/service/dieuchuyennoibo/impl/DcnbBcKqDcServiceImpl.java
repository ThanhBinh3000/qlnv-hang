package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbKqDcDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbKeHoachDcDtlBcDTO;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbKqDcService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbBcKqDcServiceImpl implements DcnbBbKqDcService {
    @Autowired
    private DcnbBcKqDcHdrRepository hdrRepository;
    @Autowired
    private DcnbBcKqDcDtlRepository dtlRepository;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;
    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;
    @Autowired
    private DcnbBbGiaoNhanHdrRepository dcnbBbGiaoNhanHdrRepository;
    @Autowired
    private DcnbBbNhapDayKhoHdrRepository dcnbBbNhapDayKhoHdrRepository;
    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;
    @Autowired
    private DcnbPhieuXuatKhoHdrRepository dcnbPhieuXuatKhoHdrRepository;
    @Autowired
    private DcnbPhieuNhapKhoHdrRepository dcnbPhieuNhapKhoHdrRepository;
    @Autowired
    private DcnbPhieuXuatKhoDtlRepository dcnbPhieuXuatKhoDtlRepository;
    @Autowired
    private DcnbPhieuNhapKhoDtlRepository dcnbPhieuNhapKhoDtlRepository;
    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Override
    public Page<DcnbBcKqDcHdr> search(CustomUserDetails currentUser, DcnbBbKqDcSearch req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBcKqDcHdr> searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public List<DcnbBcKqDcHdr> searchList(CustomUserDetails currentUser, DcnbBbKqDcSearch req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        return hdrRepository.searchList(req);
    }

    @Override
    public DcnbBcKqDcHdr create(DcnbBbKqDcDTO objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        QlnvDmDonvi cqt = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBcKqDcHdr> optional = hdrRepository.findFirstBySoBc(objReq.getSoBc());
        if (optional.isPresent() && objReq.getSoBc() != null) {
            throw new Exception("Số báo cáo đã tồn tại");
        }
        DcnbBcKqDcHdr data = new DcnbBcKqDcHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(cqt.getMaDvi());
        data.setTenDvi(cqt.getTenDvi());
        // Biên bản lấy mẫu thì auto thay đổi thủ kho
        data.setTrangThai(Contains.DUTHAO);
        data.setNgayTao(LocalDateTime.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        objReq.getDanhSachKetQua().forEach(e -> {
            e.setDcnbBcKqDcHdr(data);
        });
        DcnbBcKqDcHdr created = hdrRepository.save(data);
//        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BCKQNX-" + currentUser.getUser().getDvqlTenVietTat();
//        created.setSoBc(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBcKqDcHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBcKqDcHdr update(DcnbBbKqDcDTO objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBcKqDcHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbBcKqDcHdr> soDxuat = hdrRepository.findFirstBySoBc(objReq.getSoBc());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBc())) {
            if (soDxuat.isPresent()) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số báo cáo đã tồn tại");
                }
            }
        }
        DcnbBcKqDcHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachKetQua(objReq.getDanhSachKetQua());
        DcnbBcKqDcHdr created = hdrRepository.save(data);
//        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BCKQNX-" + currentUser.getUser().getDvqlTenVietTat();
//        created.setSoBc(so);
        hdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBcKqDcHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBcKqDcHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKem);
        return created;
    }

    @Override
    public DcnbBcKqDcHdr detail(Long id) throws Exception {
        Optional<DcnbBcKqDcHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Dữ liệu không tồn tại");
        }
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(id, Collections.singleton(DcnbBcKqDcHdr.TABLE_NAME));
        optional.get().setFileDinhKems(canCu);
        return optional.get();
    }

    @Override
    public void approve(StatusReq statusReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBcKqDcHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBcKqDcHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBcKqDcHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBcKqDcHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBcKqDcHdr created = hdrRepository.save(optional.get());
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbBcKqDcHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBcKqDcHdr data = optional.get();
        List<DcnbBcKqDcDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        fileDinhKemService.delete(id, Lists.newArrayList(DcnbBcKqDcDtl.TABLE_NAME));
        hdrRepository.delete(data);
    }

    @Override
    public void export(DcnbBbKqDcSearch objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbBcKqDcHdr> page = this.search(currentUser, objReq);
        List<DcnbBcKqDcHdr> data = page.getContent();

        String title = "Danh sách báo cáo kết quả điều chuyển";
        String[] rowsName = new String[]{"STT", "Năm báo cáo", "Số báo cáo", "Tên báo cáo", "Ngày báo cáo", "Số BB ghi nhận thừa/thiếu", "Số QĐ xuất/nhập ĐC của Cục", "Ngày QĐ của Cục", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBcKqDcHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoBc();
            objs[3] = dx.getTenBc();
            objs[4] = dx.getNgayBc();
            objs[5] = dx.getSoBbThuaThieu();
            objs[6] = dx.getSoQdDcCuc();
            objs[7] = dx.getNgayKyQd();
            objs[8] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public void deleteMulti(List<Long> ids) {

    }

    @Override
    public List<DcnbBcKqDcDtl> thongTinNhapXuatHangChiCuc(DcnbBbKqDcSearch objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        objReq.setMaDvi(currentUser.getDvql());
        List<DcnbKeHoachDcDtl> loaiVthhs = dcnbKeHoachDcDtlRepository.findByQuyetDinhDcCuaCuc(objReq.getSoQdinhCuc(), objReq.getMaDvi());
        List<DcnbBcKqDcDtl> kqdcs = new ArrayList<>();
        for (DcnbKeHoachDcDtl loaiVthh : loaiVthhs) {
            DcnbBcKqDcDtl kqdc = new DcnbBcKqDcDtl();
            kqdc.setKeHoachDcDtlId(loaiVthh.getId());
            kqdc.setLoaiVthh(loaiVthh.getLoaiVthh());
            kqdc.setTenLoaiVthh(loaiVthh.getTenLoaiVthh());
            kqdc.setCloaiVthh(loaiVthh.getCloaiVthh());
            kqdc.setTenCloaiVthh(loaiVthh.getTenCloaiVthh());
            kqdc.setDonViTinh(loaiVthh.getDonViTinh());
            kqdc.setMaDiemKho(loaiVthh.getMaDiemKho());
            kqdc.setTenDiemKho(loaiVthh.getTenDiemKho());
            kqdc.setMaNhaKho(loaiVthh.getMaNhaKho());
            kqdc.setTenNhaKho(loaiVthh.getTenNhaKho());
            kqdc.setMaNganKho(loaiVthh.getMaNganKho());
            kqdc.setTenNganKho(loaiVthh.getTenNganKho());
            kqdc.setMaLoKho(loaiVthh.getMaLoKho());
            kqdc.setTenLoKho(loaiVthh.getTenLoKho());
            kqdc.setSlTon(loaiVthh.getTonKho());
            kqdc.setNamNhap(loaiVthh.getNamNhap());

            kqdc.setMaDviNhan(loaiVthh.getMaChiCucNhan());
            kqdc.setTenDviNhan(loaiVthh.getTenChiCucNhan());
            kqdc.setMaDiemKhoNhan(loaiVthh.getMaDiemKhoNhan());
            kqdc.setTenDiemKhoNhan(loaiVthh.getTenDiemKhoNhan());
            kqdc.setMaNhaKhoNhan(loaiVthh.getMaNhaKhoNhan());
            kqdc.setTenNhaKhoNhan(loaiVthh.getTenNhaKhoNhan());
            kqdc.setMaNganKhoNhan(loaiVthh.getMaNganKhoNhan());
            kqdc.setTenNganKhoNhan(loaiVthh.getTenNganKhoNhan());
            kqdc.setMaLoKhoNhan(loaiVthh.getMaLoKhoNhan());
            kqdc.setTenLoKhoNhan(loaiVthh.getTenLoKhoNhan());
            kqdc.setSlDieuChuyenQd(loaiVthh.getSoLuongDc());
            // tính số lượng xuất thực tế
            List<DcnbPhieuXuatKhoHdr> pxks = dcnbPhieuXuatKhoHdrRepository.findBySoQuyetDinhAndKeHoachDtlId(objReq.getSoQdinhCuc(),loaiVthh.getId());
            long slXuatTtSum = pxks.stream().map(DcnbPhieuXuatKhoHdr::getTongSoLuong).mapToLong(BigDecimal::longValue).sum();
            kqdc.setSlXuatTt(new BigDecimal(slXuatTtSum));
            // tính số lượng nhập thực tế
            List<DcnbPhieuNhapKhoHdr> pnks = dcnbPhieuNhapKhoHdrRepository.findBySoQuyetDinhAndKeHoachDtlId(objReq.getSoQdinhCuc(),loaiVthh.getId());
            long slNhapTttSum = pnks.stream().map(DcnbPhieuNhapKhoHdr::getTongSoLuong).mapToLong(BigDecimal::longValue).sum();
            kqdc.setSlNhapTt(new BigDecimal(slNhapTttSum));
            kqdc.setKinhPhiTheoQd(loaiVthh.getDuToanKphi());
            // tính kinh phí xuất thực tế
            List<DcnbPhieuXuatKhoDtl> pxkds = dcnbPhieuXuatKhoDtlRepository.findBySoQuyetDinhAndKeHoachDtlId(objReq.getSoQdinhCuc(),loaiVthh.getId());
            long kinhPhiXuatTt = pxkds.stream().map(DcnbPhieuXuatKhoDtl::getKinhPhiDcTt).mapToLong(BigDecimal::longValue).sum();
            kqdc.setKinhPhiXuatTt(new BigDecimal(kinhPhiXuatTt));
            // tính kinh phí nhập thực tế
            List<DcnbPhieuNhapKhoDtl> pnkds = dcnbPhieuNhapKhoDtlRepository.findBySoQuyetDinhAndKeHoachDtlId(objReq.getSoQdinhCuc(),loaiVthh.getId());
            long kinhPhiNhapTt = pnkds.stream().map(DcnbPhieuNhapKhoDtl::getThucTeKinhPhi).mapToLong(BigDecimal::longValue).sum();
            kqdc.setKinhPhiNhapTt(new BigDecimal(kinhPhiNhapTt));

            if (dcnbBbGiaoNhanHdrRepository.findByKeHoachDcDtlId(kqdc.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                kqdc.setKetQua(Contains.DA_HOAN_THANH);
            } else if (dcnbBbNhapDayKhoHdrRepository.findByKeHoachDcDtlId(kqdc.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                QlnvDmVattu byMa = qlnvDmVattuRepository.findByMa(kqdc.getCloaiVthh());
                if ("VT".equals(byMa.getLoaiHang())) { // là vật từ thì đang thực hiện
                    kqdc.setKetQua(Contains.DANG_THUC_HIEN);
                } else {
                    kqdc.setKetQua(Contains.DA_HOAN_THANH);
                }
            } else if (!dcnbBienBanLayMauHdrRepository.findByKeHoachDcDtlIdAndType(kqdc.getKeHoachDcDtlId(), "01").isEmpty()) { // có biên bản lấy mẫu
                kqdc.setKetQua(Contains.DANG_THUC_HIEN);
            } else {
                kqdc.setKetQua(Contains.CHUA_THUC_HIEN);
            }
            kqdcs.add(kqdc);
        }
        return kqdcs;
    }

    @Override
    public List<DcnbBcKqDcDtl> thongTinNhapXuatHangCuc(DcnbBbKqDcSearch objReq) throws Exception {
        if (objReq.getIds() != null) {
            List<Long> ids = Arrays.stream(objReq.getHdrIds().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            return dtlRepository.findByHdrIdIn(ids);
        }
        return new ArrayList<>();
//        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
//        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
//        List<DcnbBcKqDcDtl> result = new ArrayList<>();
//        for (QlnvDmDonvi cqt : donvis) {
//            objReq.setMaDvi(cqt.getMaDvi());
//            List<DcnbBcKqDcDtl> dcnbBcKqDcDtlsXuat = dtlRepository.thongTinXuatNhapHangCuc(objReq);
//            result.addAll(dcnbBcKqDcDtlsXuat);
//        }
//        return result;
    }

    @Override
    public void finish(StatusReq statusReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBcKqDcHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBcKqDcHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        optional.get().setTrangThai(Contains.DA_HOAN_THANH);
        hdrRepository.save(optional.get());
    }
}
