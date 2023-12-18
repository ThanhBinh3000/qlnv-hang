package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBBKetThucNKDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBBKetThucNKHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBBKetThucNKReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkBBKetThucNKService;
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
public class HhNkBBKetThucNKServiceImpl extends BaseServiceImpl implements HhNkBBKetThucNKService {
    @Autowired
    private HhNkBBKetThucNKHdrRepository hdrRepository;
    @Autowired
    private HhNkBBKetThucNKDtlRepository dtlRepository;

    @Override
    public Page<HhNkBBKetThucNKHdr> searchPage(HhNkBBKetThucNKReq req) throws Exception {
        return null;
    }

    public Page<HhNkBBKetThucNKHdrDTO> search(CustomUserDetails currentUser, HhNkBBKetThucNKReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkBBKetThucNKHdrDTO> searchDto = null;
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public List<HhNkBBKetThucNKHdrListDTO> searchList(CustomUserDetails currentUser, HhNkBBKetThucNKReq req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        return hdrRepository.searchList(req);
    }

    @Override
    public HhNkBBKetThucNKHdr create(HhNkBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        HhNkBBKetThucNKHdr data = new HhNkBBKetThucNKHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getHhNkBBKetThucNKDtl().forEach(e -> {
            e.setHhNkBBKetThucNKHdr(data);
        });
        HhNkBBKetThucNKHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBKT-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public HhNkBBKetThucNKHdr update(HhNkBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<HhNkBBKetThucNKHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBBKetThucNKHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setHhNkBBKetThucNKDtl(req.getHhNkBBKetThucNKDtl());
        HhNkBBKetThucNKHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBKT-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        return update;
    }

    @Override
    public HhNkBBKetThucNKHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<HhNkBBKetThucNKHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkBBKetThucNKHdr data = optional.get();
        return data;
    }

    @Override
    public HhNkBBKetThucNKHdr approve(HhNkBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        HhNkBBKetThucNKHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                hdr.setNguoiPDuyetTvqt(userInfo.getId());
                hdr.setNgayPDuyetTvqt(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                hdr.setNguoiPDuyetTvqt(userInfo.getId());
                hdr.setNgayPDuyetTvqt(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                hdr.setNguoiPDuyetKt(userInfo.getId());
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDC:
                hdr.setNguoiPDuyetKt(userInfo.getId());
                hdr.setNgayPDuyetKt(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDCC:
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        HhNkBBKetThucNKHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        HhNkBBKetThucNKHdr detail = detail(id);
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
    public void export(HhNkBBKetThucNKReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<HhNkBBKetThucNKHdrDTO> page = search(currentUser, objReq);
        List<HhNkBBKetThucNKHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhNkBBKetThucNKHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HhNkBBKetThucNKReq objReq) throws Exception {
        HhNkBBKetThucNKHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
