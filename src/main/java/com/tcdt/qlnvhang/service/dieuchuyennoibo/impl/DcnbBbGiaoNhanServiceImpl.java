package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbGiaoNhanDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbGiaoNhanHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBbGiaoNhanHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbGiaoNhanService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DcnbBbGiaoNhanServiceImpl extends BaseServiceImpl implements DcnbBbGiaoNhanService {

    @Autowired
    private DcnbBbGiaoNhanHdrRepository hdrRepository;

    @Autowired
    private DcnbBbGiaoNhanDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    @Autowired
    public DocxToPdfConverter docxToPdfConverter;

    @Override
    public Page<DcnbBbGiaoNhanHdr> searchPage(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public Page<DcnbBbGiaoNhanHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBbGiaoNhanHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setMaDvi(dvql.substring(0,6));
            req.setTrangThai(Contains.DADUYET_LDC);
        } else {
            req.setMaDvi(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBbGiaoNhanHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public DcnbBbGiaoNhanHdr create(DcnbBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp cục");
        }
//        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findFirstBySoBb(req.getSoBb());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBbGiaoNhanHdr data = new DcnbBbGiaoNhanHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getDanhSachDaiDien().forEach(e -> {
            e.setParent(data);
        });
        req.getDanhSachBangKe().forEach(e -> {
            e.setParent(data);
        });
        DcnbBbGiaoNhanHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBGN-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), DcnbBbGiaoNhanHdr.TABLE_NAME + "_CC");
        List<FileDinhKem> dinhkem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBbGiaoNhanHdr.TABLE_NAME + "_DK");
        created.setFileCanCu(canCu);
        created.setFileDinhKems(dinhkem);
        return created;
    }

    @Override
    public DcnbBbGiaoNhanHdr update(DcnbBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp cục");
        }
        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbGiaoNhanHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setDanhSachDaiDien(req.getDanhSachDaiDien());
        data.setDanhSachBangKe(req.getDanhSachBangKe());
        DcnbBbGiaoNhanHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBGN-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBbGiaoNhanHdr.TABLE_NAME + "_CC"));
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBbGiaoNhanHdr.TABLE_NAME + "_DK"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), update.getId(), DcnbBbGiaoNhanHdr.TABLE_NAME + "_CC");
        List<FileDinhKem> dinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbBbGiaoNhanHdr.TABLE_NAME + "_DK");
        update.setFileCanCu(canCu);
        update.setFileDinhKems(dinhKem);
        return update;
    }

    @Override
    public DcnbBbGiaoNhanHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbGiaoNhanHdr data = optional.get();
        data.setFileCanCu(fileDinhKemService.search(id, Collections.singleton(DcnbBbGiaoNhanHdr.TABLE_NAME + "_CC")));
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBbGiaoNhanHdr.TABLE_NAME + "_DK")));
        return data;
    }

    @Override
    public DcnbBbGiaoNhanHdr approve(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr approve(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBbGiaoNhanHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.DUTHAO + Contains.CHODUYET_LDC:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBbGiaoNhanHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBbGiaoNhanHdr detail = detail(id);
        hdrRepository.delete(detail);
        dtlRepository.deleteAllByHdrId(id);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (listMulti != null && !listMulti.isEmpty()) {
            listMulti.forEach(i -> {
                try {
                    delete(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new Exception("List id is null");
        }
    }

    @Override
    public void export(DcnbBbGiaoNhanHdrReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBbGiaoNhanHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBbGiaoNhanHdrDTO> data = page.getContent();

        String title = "Danh sách biên bản giao nhận";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chuyển", "Năm KH", "Thời hạn điều chuyển", "Điểm kho", "Lô kho", "Số HSKT", "Số BB giao nhận", "Số BB kết thúc NK", "Ngày kết thúc NK", "Số BBLM/BGM", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-giao-nhan.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBbGiaoNhanHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoHoSoKt();
            objs[7] = dx.getSoBienBanGiaoNhan();
            objs[8] = dx.getSoBienBanKetThucNk();
            objs[9] = dx.getNgayKetThucNk();
            objs[10] = dx.getSoBienBanLayMau();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
    @Override
    public ReportTemplateResponse preview(DcnbBbGiaoNhanHdrReq objReq) throws Exception {
        var dcnbBbGiaoNhanHdr = hdrRepository.findById(objReq.getId());
        if (!dcnbBbGiaoNhanHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        var dcnbBbGiaoNhanDtl = dtlRepository.findByHdrId(dcnbBbGiaoNhanHdr.get().getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBbGiaoNhanHdrPreview = setDataToPreview(dcnbBbGiaoNhanHdr, dcnbBbGiaoNhanDtl);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBbGiaoNhanHdrPreview);
    }

    private DcnbBbGiaoNhanHdrPreview setDataToPreview(Optional<DcnbBbGiaoNhanHdr> dcnbBBKetThucNKHdr, List<DcnbBbGiaoNhanDtl> dcnbBbGiaoNhanDtl) {
        var daiDienBenGiaoHang = dcnbBbGiaoNhanDtl.stream().findFirst().filter(s -> s.getType().equals("Đại diện bên giao hàng"));
        var daiDienCucDtnn = dcnbBbGiaoNhanDtl.stream().findFirst().filter(s -> s.getType().equals("Đại diện cục DTNN KV"));
        var daiDienChiCucDtnn = dcnbBbGiaoNhanDtl.stream().findFirst().filter(s -> s.getType().equals("Đại diện chi cục DTNN KV"));
        return DcnbBbGiaoNhanHdrPreview.builder()
                .chungLoaiHangHoa(dcnbBBKetThucNKHdr.get().getCloaiVthh())
                .donViCungCapHang(dcnbBBKetThucNKHdr.get().getTenDvi())
                .quyChuanTieuChuan("")
                .ngayLap(dcnbBBKetThucNKHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .chiCuc(dcnbBBKetThucNKHdr.get().getTenDvi())
                .tenDvi(dcnbBBKetThucNKHdr.get().getTenDvi())
                .tenLanhDao(dcnbBBKetThucNKHdr.get().getTenLanhDao())
                .tenCanBo(dcnbBBKetThucNKHdr.get().getTenCanBo())
                .tenLanhDaoChiCuc(dcnbBBKetThucNKHdr.get().getTenLanhDao())
                .hoVatenDvCungCapHang(dcnbBBKetThucNKHdr.get().getTenLanhDao())
                .tongSoLuongThucNhap(dcnbBBKetThucNKHdr.get().getSoLuongQdDcCuc())
                .dviTinh(dcnbBBKetThucNKHdr.get().getDviTinh())
                .tenDiemKho(dcnbBBKetThucNKHdr.get().getTenDiemKho())
                .daiDienDonViCungCapHang(dcnbBBKetThucNKHdr.get().getTenDvi())
                .truongBpKtbq("")
                .daiDienCty(daiDienBenGiaoHang.get().getHoVaTen())
                .daiDienCucDtnn(daiDienCucDtnn.get().getHoVaTen())
                .daiDienChiCucDtnn(daiDienChiCucDtnn.get().getHoVaTen())
                .build();
    }
}