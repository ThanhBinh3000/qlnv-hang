package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBangKeCanHangDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBangKeCanHangHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeCanHangHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeCanHangHdrDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhNkBangKeCanHangServiceImpl extends BaseServiceImpl {

    @Autowired
    private HhNkBangKeCanHangHdrRepository hhnkBangKeCanHangHdrRepository;

    @Autowired
    private HhNkBangKeCanHangDtlRepository hhnkBangKeCanHangDtlRepository;

    @Autowired
    private HhNkPhieuNhapKhoHdrRepository hhnkPhieuNhapKhoHdrRepository;

    public Page<HhNkBangKeCanHangHdrDTO> searchPage(CustomUserDetails currentUser, SearchBangKeCanHang req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        Page<HhNkBangKeCanHangHdrDTO> searchDto = hhnkBangKeCanHangHdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Transactional
    public HhNkBangKeCanHangHdr save(CustomUserDetails currentUser, HhNkBangKeCanHangHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<HhNkBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//            throw new Exception("Số bảng kê đã tồn tại");
//        }
        HhNkBangKeCanHangHdr data = new HhNkBangKeCanHangHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        if (objReq.getHhNkBangKeCanHangDtl() != null) {
            objReq.getHhNkBangKeCanHangDtl().forEach(e -> e.setHhNkBangKeCanHangHdr(data));
        }
        HhNkBangKeCanHangHdr created = hhnkBangKeCanHangHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKCH-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBangKe(so);
        hhnkBangKeCanHangHdrRepository.save(created);
        return created;
    }

    @Transactional
    public HhNkBangKeCanHangHdr update(CustomUserDetails currentUser, HhNkBangKeCanHangHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<HhNkBangKeCanHangHdr> optional = hhnkBangKeCanHangHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<HhNkBangKeCanHangHdr> soDxuat = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
//            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }

        HhNkBangKeCanHangHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setHhNkBangKeCanHangDtl(objReq.getHhNkBangKeCanHangDtl());
        HhNkBangKeCanHangHdr created = hhnkBangKeCanHangHdrRepository.save(data);
        String soBangKe = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKCH-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBangKe(soBangKe);
        hhnkBangKeCanHangHdrRepository.save(created);
        return created;
    }


    public List<HhNkBangKeCanHangHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<HhNkBangKeCanHangHdr> optional = hhnkBangKeCanHangHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<HhNkBangKeCanHangHdr> allById = hhnkBangKeCanHangHdrRepository.findAllById(ids);
        return allById;
    }

    public HhNkBangKeCanHangHdr details(Long id) throws Exception {
        List<HhNkBangKeCanHangHdr> details = details(Arrays.asList(id));
        HhNkBangKeCanHangHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getHhNkBangKeCanHangDtl());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<HhNkBangKeCanHangHdr> optional = hhnkBangKeCanHangHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        HhNkBangKeCanHangHdr data = optional.get();
        List<HhNkBangKeCanHangDtl> list = hhnkBangKeCanHangDtlRepository.findByHdrId(data.getId());
        hhnkBangKeCanHangDtlRepository.deleteAll(list);
        hhnkBangKeCanHangHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<HhNkBangKeCanHangHdr> list = hhnkBangKeCanHangHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(HhNkBangKeCanHangHdr::getId).collect(Collectors.toList());
        List<HhNkBangKeCanHangDtl> listBangKe = hhnkBangKeCanHangDtlRepository.findByHdrIdIn(listId);
        hhnkBangKeCanHangDtlRepository.deleteAll(listBangKe);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        HhNkBangKeCanHangHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<HhNkBangKeCanHangHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public HhNkBangKeCanHangHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<HhNkBangKeCanHangHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                Optional<HhNkPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = hhnkPhieuNhapKhoHdrRepository.findById(optional.get().getPhieuNhapKhoId());
                if (dcnbPhieuNhapKhoHdr.isPresent()) {
                    dcnbPhieuNhapKhoHdr.get().setBangKeCanHangId(optional.get().getId());
                    dcnbPhieuNhapKhoHdr.get().setSoBangKeCanHang(optional.get().getSoBangKe());
                    hhnkPhieuNhapKhoHdrRepository.save(dcnbPhieuNhapKhoHdr.get());
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhNkBangKeCanHangHdr created = hhnkBangKeCanHangHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchBangKeCanHang objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
        Page<HhNkBangKeCanHangHdrDTO> page = hhnkBangKeCanHangHdrRepository.searchPage(objReq, pageable);
        List<HhNkBangKeCanHangHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhNkBangKeCanHangHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdPdNk();
            objs[2] = dx.getNam();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[7] = dx.getSoBangKe();
            objs[9] = dx.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<HhNkBangKeCanHangDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<HhNkBangKeCanHangDtl> optional = hhnkBangKeCanHangDtlRepository.findByHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }

    public ReportTemplateResponse preview(HhNkBangKeCanHangHdrReq objReq) throws Exception {
        HhNkBangKeCanHangHdr optional = details(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
