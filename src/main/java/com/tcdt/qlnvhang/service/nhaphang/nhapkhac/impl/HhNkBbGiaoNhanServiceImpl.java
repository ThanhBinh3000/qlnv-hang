package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBbGiaoNhanDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBBKetThucNKReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkBbGiaoNhanService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class HhNkBbGiaoNhanServiceImpl extends BaseServiceImpl implements HhNkBbGiaoNhanService {

    @Autowired
    private HhNkBbGiaoNhanHdrRepository hdrRepository;

    @Autowired
    private HhNkBbGiaoNhanDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    @Override
    public Page<HhNkBbGiaoNhanHdr> searchPage(HhNkBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public Page<HhNkBbGiaoNhanHdrDTO> searchPage(CustomUserDetails currentUser, HhNkBbGiaoNhanHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkBbGiaoNhanHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public HhNkBbGiaoNhanHdr create(HhNkBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp cục");
        }
//        Optional<HhNkBbGiaoNhanHdr> optional = hdrRepository.findFirstBySoBb(req.getSoBb());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        HhNkBbGiaoNhanHdr data = new HhNkBbGiaoNhanHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getDanhSachDaiDien().forEach(e -> {
            e.setParent(data);
        });
        req.getDanhSachBangKe().forEach(e -> {
            e.setParent(data);
        });
        HhNkBbGiaoNhanHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBGN-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), HhNkBbGiaoNhanHdr.TABLE_NAME + "_CC");
        List<FileDinhKem> dinhkem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), HhNkBbGiaoNhanHdr.TABLE_NAME + "_DK");
        created.setFileCanCu(canCu);
        created.setFileDinhKems(dinhkem);
        return created;
    }

    @Override
    public HhNkBbGiaoNhanHdr update(HhNkBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp cục");
        }
        Optional<HhNkBbGiaoNhanHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBbGiaoNhanHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setDanhSachDaiDien(req.getDanhSachDaiDien());
        data.setDanhSachBangKe(req.getDanhSachBangKe());
        HhNkBbGiaoNhanHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBGN-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(HhNkBbGiaoNhanHdr.TABLE_NAME + "_CC"));
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(HhNkBbGiaoNhanHdr.TABLE_NAME + "_DK"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), update.getId(), HhNkBbGiaoNhanHdr.TABLE_NAME + "_CC");
        List<FileDinhKem> dinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), HhNkBbGiaoNhanHdr.TABLE_NAME + "_DK");
        update.setFileCanCu(canCu);
        update.setFileDinhKems(dinhKem);
        return update;
    }

    @Override
    public HhNkBbGiaoNhanHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<HhNkBbGiaoNhanHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBbGiaoNhanHdr data = optional.get();
        data.setFileCanCu(fileDinhKemService.search(id, Collections.singleton(HhNkBbGiaoNhanHdr.TABLE_NAME + "_CC")));
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(HhNkBbGiaoNhanHdr.TABLE_NAME + "_DK")));
        return data;
    }

    @Override
    public HhNkBbGiaoNhanHdr approve(HhNkBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        HhNkBbGiaoNhanHdr hdr = detail(req.getId());
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
        HhNkBbGiaoNhanHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        HhNkBbGiaoNhanHdr detail = detail(id);
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
    public void export(HhNkBbGiaoNhanHdrReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<HhNkBbGiaoNhanHdrDTO> page = searchPage(currentUser, objReq);
        List<HhNkBbGiaoNhanHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhNkBbGiaoNhanHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public ReportTemplateResponse preview(HhNkBbGiaoNhanHdrReq objReq) throws Exception {
        HhNkBbGiaoNhanHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}