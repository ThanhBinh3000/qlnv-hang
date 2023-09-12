package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbHoSoBienBanDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbHoSoKyThuatHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbHoSoTaiLieuDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbHoSoKyThuatHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbHoSoKyThuat;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbHoSoKyThuatHdrPreview;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbHoSoKyThuatHdrServiceImpl extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbHoSoKyThuatHdrServiceImpl.class);

    @Autowired
    private DcnbHoSoKyThuatHdrRepository dcnbHoSoKyThuatHdrRepository;

    @Autowired
    private DcnbHoSoTaiLieuDtlRepository dcnbHoSoKyThuatDtlRepository;

    private THKeHoachDieuChuyenNoiBoCucDtlRepository tHKeHoachDieuChuyenNoiBoCucDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private LuuKhoClient luuKhoClient;
    @Autowired
    private DcnbHoSoBienBanDtlRepository dcnbHoSoBienBanDtlRepository;

    public Page<DcnbHoSoKyThuatHdr> searchPage(CustomUserDetails currentUser, SearchDcnbHoSoKyThuat req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbHoSoKyThuatHdr> search = dcnbHoSoKyThuatHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public DcnbHoSoKyThuatHdr save(CustomUserDetails currentUser, DcnbHoSoKyThuatHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbHoSoKyThuatHdr> optional = dcnbHoSoKyThuatHdrRepository.findFirstBySoHoSoKyThuat(objReq.getSoHoSoKyThuat());
        if (optional.isPresent() && objReq.getSoHoSoKyThuat().split("/").length == 1) {
            throw new Exception("số hồ sơ kỹ thuật đã tồn tại");
        }
        DcnbHoSoKyThuatHdr data = new DcnbHoSoKyThuatHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        if (objReq.getDanhSachHoSoBienBan() != null) {
        }
        if (objReq.getDanhSachHoSoTaiLieu() != null) {
        }
        DcnbHoSoKyThuatHdr created = dcnbHoSoKyThuatHdrRepository.save(data);
        return created;
    }

    @Transactional
    public DcnbHoSoKyThuatHdr update(CustomUserDetails currentUser, DcnbHoSoKyThuatHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbHoSoKyThuatHdr> optional = dcnbHoSoKyThuatHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbHoSoKyThuatHdr> soDxuat = dcnbHoSoKyThuatHdrRepository.findFirstBySoHoSoKyThuat(objReq.getSoHoSoKyThuat());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoHoSoKyThuat())) {
            if (soDxuat.isPresent() && objReq.getSoHoSoKyThuat().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số hồ sơ kỹ thuật đã tồn tại");
                }
            }
        }

        DcnbHoSoKyThuatHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachHoSoTaiLieu(objReq.getDanhSachHoSoTaiLieu());
        data.setDanhSachHoSoBienBan(objReq.getDanhSachHoSoBienBan());

        if (objReq.getDanhSachHoSoTaiLieu() != null) {
        }
        if (objReq.getDanhSachHoSoBienBan() != null) {
        }
        DcnbHoSoKyThuatHdr created = dcnbHoSoKyThuatHdrRepository.save(data);
        return created;
    }


    public List<DcnbHoSoKyThuatHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbHoSoKyThuatHdr> optional = dcnbHoSoKyThuatHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbHoSoKyThuatHdr> allById = dcnbHoSoKyThuatHdrRepository.findAllById(ids);
        return allById;
    }

    public DcnbHoSoKyThuatHdr details(Long id) throws Exception {
        List<DcnbHoSoKyThuatHdr> details = details(Arrays.asList(id));
        DcnbHoSoKyThuatHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getDanhSachHoSoTaiLieu());
            Hibernate.initialize(result.getDanhSachHoSoBienBan());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbHoSoKyThuatHdr> optional = dcnbHoSoKyThuatHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbHoSoKyThuatHdr data = optional.get();
        List<DcnbHoSoTaiLieuDtl> list = dcnbHoSoKyThuatDtlRepository.findByHoSoKyThuatHdrId(data.getId());
        dcnbHoSoKyThuatDtlRepository.deleteAll(list);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbHoSoKyThuatHdr.TABLE_NAME + "_CAN_CU"));
        dcnbHoSoKyThuatHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbHoSoKyThuatHdr> list = dcnbHoSoKyThuatHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbHoSoKyThuatHdr::getId).collect(Collectors.toList());
        List<DcnbHoSoTaiLieuDtl> listPhuongAn = dcnbHoSoKyThuatDtlRepository.findByHoSoKyThuatHdrIdIn(listId);
        dcnbHoSoKyThuatDtlRepository.deleteAll(listPhuongAn);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbHoSoKyThuatHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<DcnbHoSoKyThuatHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbHoSoKyThuatHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbHoSoKyThuatHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.TUCHOI_TBP_TVQT:
                optional.get().setNgayPduyetTvqt(LocalDate.now());
                optional.get().setNguoiPduyetIdTvqt(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPduyetTvqt(LocalDate.now());
                optional.get().setNguoiPduyetIdTvqt(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbHoSoKyThuatHdr created = dcnbHoSoKyThuatHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbHoSoKyThuat objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbHoSoKyThuatHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbHoSoKyThuatHdr> data = page.getContent();

        String title = "Danh sách kế hoạch điều chuyển nội bộ ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbHoSoKyThuatHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
//            objs[1] = dx.getNam();
//            objs[2] = dx.getSoDxuat();
//            objs[3] = dx.getNgayLapKh();
//            objs[4] = dx.getNgayDuyetLdcc();
//            objs[5] = dx.getTenLoaiDc();
            objs[6] = dx.getTenDvi();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbHoSoTaiLieuDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbHoSoTaiLieuDtl> optional = dcnbHoSoKyThuatDtlRepository.findByHoSoKyThuatHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }

    public ReportTemplateResponse preview(DcnbHoSoKyThuatHdrReq objReq) throws Exception {
        var dcnbHoSoKyThuatHdr = dcnbHoSoKyThuatHdrRepository.findById(objReq.getId());
        if (!dcnbHoSoKyThuatHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        var dcnbHoSoTaiLieuDtlList = dcnbHoSoKyThuatDtlRepository.findByHoSoKyThuatHdrId(dcnbHoSoKyThuatHdr.get().getId());
        var dcnbHoSoBienBanDtlList = dcnbHoSoBienBanDtlRepository.findByHoSoKyThuatHdrId(dcnbHoSoKyThuatHdr.get().getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBangKeCanHangPreview = setDataToPreview(dcnbHoSoKyThuatHdr, dcnbHoSoTaiLieuDtlList, dcnbHoSoBienBanDtlList);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBangKeCanHangPreview);
    }

    private DcnbHoSoKyThuatHdrPreview setDataToPreview(Optional<DcnbHoSoKyThuatHdr> dcnbHoSoKyThuatHdr,
                                                       List<DcnbHoSoTaiLieuDtl> dcnbHoSoTaiLieuDtlList,
                                                       List<DcnbHoSoBienBanDtl> dcnbHoSoBienBanDtlList) {
        return DcnbHoSoKyThuatHdrPreview.builder()
                .soHoSoKyThuat(dcnbHoSoKyThuatHdr.get().getSoHoSoKyThuat())
                .chungLoaiHangHoa("Chủng loại hàng DTQG")
                .namKhoach("Năm kế hoạch")
                .tenNganKho(dcnbHoSoKyThuatHdr.get().getTenNganKho())
                .tenLoKho(dcnbHoSoKyThuatHdr.get().getTenLoKho())
                .tenDiemKho(dcnbHoSoKyThuatHdr.get().getTenDiemKho())
                .tenDvi(dcnbHoSoKyThuatHdr.get().getTenDvi())
                .ngayTao(dcnbHoSoKyThuatHdr.get().getNgayTao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tenCbthskt(dcnbHoSoKyThuatHdr.get().getTenCbthsktKx())
                .ngayDuyetHskt(dcnbHoSoKyThuatHdr.get().getNgayDuyetHskt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .danhSachHoSoTaiLieu(dcnbHoSoTaiLieuDtlList)
                .danhSachHoSoBienBan(dcnbHoSoBienBanDtlList)
                .build();
    }
}
