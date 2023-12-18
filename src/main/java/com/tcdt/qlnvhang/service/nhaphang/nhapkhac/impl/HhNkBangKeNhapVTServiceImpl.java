package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatNk;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBangKeNhapVTDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkBangKeNhapVTHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeNhapVTReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkBangKeNhapVTService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhNkBangKeNhapVTServiceImpl extends BaseServiceImpl implements HhNkBangKeNhapVTService {
    @Autowired
    private HhNkBangKeNhapVTHdrRepository hdrRepository;
    @Autowired
    private HhNkBangKeNhapVTDtlRepository dtlRepository;
    @Autowired
    private DcnbPhieuNhapKhoHdrRepository dcnbPhieuNhapKhoHdrRepository;

    @Override
    public Page<HhNkBangKeNhapVTHdr> searchPage(HhNkBangKeNhapVTReq req) throws Exception {
        return null;
    }

    public Page<HhNkBangKeNhapVTHdrDTO> searchPage(CustomUserDetails currentUser, HhNkBangKeNhapVTReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkBangKeNhapVTHdrDTO> searchDto = null;
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public HhNkBangKeNhapVTHdr create(HhNkBangKeNhapVTReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        HhNkBangKeNhapVTHdr data = new HhNkBangKeNhapVTHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(dvql);
        data.setTenDvi(userInfo.getTenDvi());
        objReq.getHhNkBangKeNhapVTDtl().forEach(e -> e.setHhNkBangKeNhapVTHdr(data));
        HhNkBangKeNhapVTHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKNVT-" + userInfo.getDvqlTenVietTat();
        created.setSoBangKe(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public HhNkBangKeNhapVTHdr update(HhNkBangKeNhapVTReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<HhNkBangKeNhapVTHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        HhNkBangKeNhapVTHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setHhNkBangKeNhapVTDtl(objReq.getHhNkBangKeNhapVTDtl());
        HhNkBangKeNhapVTHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKNVT-" + userInfo.getDvqlTenVietTat();
        created.setSoBangKe(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public HhNkBangKeNhapVTHdr detail(Long id) throws Exception {
        if (id == null)
            throw new Exception("Tham số không hợp lệ.");
        Optional<HhNkBangKeNhapVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional.get();
    }

    @Override
    public HhNkBangKeNhapVTHdr approve(HhNkBangKeNhapVTReq objReq) throws Exception {
        return null;
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        HhNkBangKeNhapVTHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<HhNkBangKeNhapVTHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public HhNkBangKeNhapVTHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<HhNkBangKeNhapVTHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.TUCHOI_TBP_TVQT:
                optional.get().setNgayPDuyetTvqt(LocalDate.now());
                optional.get().setNguoiPDuyetTvqt(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPDuyetTvqt(LocalDate.now());
                optional.get().setNguoiPDuyetTvqt(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());

                Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(optional.get().getPhieuNhapKhoId());
                if (dcnbPhieuNhapKhoHdr.isPresent()) {
                    dcnbPhieuNhapKhoHdr.get().setBangKeVtId(optional.get().getId());
                    dcnbPhieuNhapKhoHdr.get().setSoBangKeVt(optional.get().getSoBangKe());
                    dcnbPhieuNhapKhoHdrRepository.save(dcnbPhieuNhapKhoHdr.get());
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhNkBangKeNhapVTHdr created = hdrRepository.save(optional.get());
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<HhNkBangKeNhapVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        HhNkBangKeNhapVTHdr data = optional.get();
        List<HhNkBangKeNhapVTDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        List<HhNkBangKeNhapVTHdr> list = hdrRepository.findAllByIdIn(listMulti);

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(HhNkBangKeNhapVTHdr::getId).collect(Collectors.toList());
        List<HhNkBangKeNhapVTDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
        dtlRepository.deleteAll(listBangKe);
    }

    @Override
    public void export(HhNkBangKeNhapVTReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<HhNkBangKeNhapVTHdrDTO> page = searchPage(currentUser, objReq);
        List<HhNkBangKeNhapVTHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhNkBangKeNhapVTHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HhNkBangKeNhapVTReq objReq) throws Exception {
        HhNkBangKeNhapVTHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
