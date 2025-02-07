package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBienBanTinhKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanTinhKho;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBienBanTinhKhoHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DcnbBienBanTinhKhoServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbBienBanTinhKhoHdrRepository dcnbBienBanTinhKhoHdrRepository;
    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;
    @Autowired
    private DcnbPhieuKnChatLuongHdrRepository dcnbPhieuKnChatLuongHdrRepository;
    @Autowired
    private DcnbBienBanTinhKhoDtlRepository dcnbBienBanTinhKhoDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    public Page<DcnbBienBanTinhKhoHdrDTO> searchPage(CustomUserDetails currentUser, SearchDcnbBienBanTinhKho req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBienBanTinhKhoHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        searchDto = dcnbBienBanTinhKhoHdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Transactional
    public DcnbBienBanTinhKhoHdr save(CustomUserDetails currentUser, DcnbBienBanTinhKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBienBanTinhKhoHdr> optional = dcnbBienBanTinhKhoHdrRepository.findFirstBySoBbTinhKho(objReq.getSoBbTinhKho());
//        if (optional.isPresent() && objReq.getSoBbTinhKho().split("/").length == 1) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }
        List<DcnbBienBanTinhKhoHdr> lists = new ArrayList<>();
        if(StringUtils.isEmpty(objReq.getMaLoKho())){
            lists = dcnbBienBanTinhKhoHdrRepository.findByMaDviAndSoQdinhDcAndMaNganKho(currentUser.getDvql(), objReq.getSoQdinhDcc(), objReq.getMaNganKho());
        }else {
            lists = dcnbBienBanTinhKhoHdrRepository.findByMaDviAndSoQdinhDcAndMaLoKho(currentUser.getDvql(), objReq.getSoQdinhDcc(), objReq.getMaLoKho());
        }
        if(!lists.isEmpty()){
            throw new Exception("Ngăn Lô kho đã được khởi tạo!");
        }
        DcnbBienBanTinhKhoHdr data = new DcnbBienBanTinhKhoHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        data.setType(objReq.getType());
        objReq.getDcnbBienBanTinhKhoDtl().forEach(e -> e.setDcnbBienBanTinhKhoHdr(data));
        DcnbBienBanTinhKhoHdr created = dcnbBienBanTinhKhoHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBTK-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBbTinhKho(so);
        dcnbBienBanTinhKhoHdrRepository.save(created);
        List<FileDinhKem> bienBanTinhKhoDaKy = fileDinhKemService.saveListFileDinhKem(objReq.getFileBbTinhKhoDaKy(), created.getId(), DcnbBienBanTinhKhoHdr.TABLE_NAME + "_BB_TINH_KHO_DA_KY");
        data.setFileBbTinhKhoDaKy(bienBanTinhKhoDaKy);
        return created;
    }

    @Transactional
    public DcnbBienBanTinhKhoHdr update(CustomUserDetails currentUser, DcnbBienBanTinhKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBienBanTinhKhoHdr> optional = dcnbBienBanTinhKhoHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBienBanTinhKhoHdr> soBbTinhKho = dcnbBienBanTinhKhoHdrRepository.findFirstBySoBbTinhKho(objReq.getSoBbTinhKho());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBbTinhKho())) {
//            if (soBbTinhKho.isPresent() && objReq.getSoBbTinhKho().split("/").length == 1) {
//                if (!soBbTinhKho.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }

        DcnbBienBanTinhKhoHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBienBanTinhKhoDtl(objReq.getDcnbBienBanTinhKhoDtl());
        if (objReq.getDcnbBienBanTinhKhoDtl() != null) {
        }
        DcnbBienBanTinhKhoHdr created = dcnbBienBanTinhKhoHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBTK-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBbTinhKho(so);
        dcnbBienBanTinhKhoHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanTinhKhoHdr.TABLE_NAME + "_BB_TINH_KHO_DA_KY"));
        List<FileDinhKem> bienBanTinhKhoDaKy = fileDinhKemService.saveListFileDinhKem(objReq.getFileBbTinhKhoDaKy(), created.getId(), DcnbBienBanTinhKhoHdr.TABLE_NAME + "_BB_TINH_KHO_DA_KY");
        data.setFileBbTinhKhoDaKy(bienBanTinhKhoDaKy);
        return created;
    }


    public List<DcnbBienBanTinhKhoHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBienBanTinhKhoHdr> optional = dcnbBienBanTinhKhoHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbBienBanTinhKhoHdr> allById = dcnbBienBanTinhKhoHdrRepository.findAllById(ids);
        allById.forEach(e -> {
            List<FileDinhKem> bienBanTinhKhoDaKy = fileDinhKemRepository.findByDataIdAndDataTypeIn(e.getId(), Collections.singleton(DcnbBienBanTinhKhoHdr.TABLE_NAME + "_BB_TINH_KHO_DA_KY"));
            e.setFileBbTinhKhoDaKy(bienBanTinhKhoDaKy);
        });
        return allById;
    }

    public DcnbBienBanTinhKhoHdr details(Long id) throws Exception {
        List<DcnbBienBanTinhKhoHdr> details = details(Arrays.asList(id));
        DcnbBienBanTinhKhoHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getDcnbBienBanTinhKhoDtl());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbBienBanTinhKhoHdr> optional = dcnbBienBanTinhKhoHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBienBanTinhKhoHdr data = optional.get();
        List<DcnbBienBanTinhKhoDtl> list = dcnbBienBanTinhKhoDtlRepository.findByHdrId(data.getId());
        dcnbBienBanTinhKhoDtlRepository.deleteAll(list);
        dcnbBienBanTinhKhoHdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<DcnbBienBanTinhKhoHdr> list = dcnbBienBanTinhKhoHdrRepository.findAllByIdIn(idSearchReq.getIdList());
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(DcnbBienBanTinhKhoHdr::getId).collect(Collectors.toList());
//        List<DcnbBienBanTinhKhoDtl> listBangKe = dcnbBienBanTinhKhoDtlRepository.findByHdrIdIn(listId);
//        dcnbBienBanTinhKhoDtlRepository.deleteAll(listBangKe);
//    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBienBanTinhKhoHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<DcnbBienBanTinhKhoHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBienBanTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBienBanTinhKhoHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                optional.get().setNgayPduyetKtvBQ(LocalDate.now());
                optional.get().setKtvBaoQuanId(currentUser.getUser().getId());
                optional.get().setKtvBaoQuan(currentUser.getUser().getFullName());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPduyetKt(LocalDate.now());
                optional.get().setKeToanId(currentUser.getUser().getId());
                optional.get().setKeToan(currentUser.getUser().getFullName());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPduyetLdcc(LocalDate.now());
                optional.get().setLanhDaoChiCucId(currentUser.getUser().getId());
                optional.get().setLanhDaoChiCuc(currentUser.getUser().getFullName());
                //                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                        optional.get().getQDinhDccId(),
//                        optional.get().getMaNganKho(),
//                        optional.get().getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBienBanTinhKhoHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                // biên bản lấy mẫu bàn giao
                // update biên bản lấy mẫu
                List<DcnbBienBanLayMauHdr> bienBanLayMauHdrList = new ArrayList<>();
                if (optional.get().getMaLoKho() == null) {
                    bienBanLayMauHdrList = dcnbBienBanLayMauHdrRepository.findByMaDviAndQdccIdAndMaNganKho(optional.get().getMaDvi(), optional.get().getQDinhDccId(), optional.get().getMaNganKho());
                } else {
                    bienBanLayMauHdrList = dcnbBienBanLayMauHdrRepository.findByMaDviAndQdccIdAndMaNganKhoAndMaLoKho(optional.get().getMaDvi(), optional.get().getQDinhDccId(), optional.get().getMaNganKho(), optional.get().getMaLoKho());
                }
                for (DcnbBienBanLayMauHdr hdrbq : bienBanLayMauHdrList) {
                    hdrbq.setBbTinhKhoId(optional.get().getId());
                    hdrbq.setSoBbTinhKho(optional.get().getSoBbTinhKho());
                    hdrbq.setNgayXuatDocKho(optional.get().getNgayKetThucXuat());
                    hdrbq.setNgayLapBbTinhKho(optional.get().getNgayLap());
                    dcnbBienBanLayMauHdrRepository.save(hdrbq);
                }
                // hiếu kiểm nghiệm chất lượng
                if (optional.get().getPhieuKnChatLuongHdrId() != null) {
                    Optional<DcnbPhieuKnChatLuongHdr> knChatLuongHdrOptional = dcnbPhieuKnChatLuongHdrRepository.findById(optional.get().getPhieuKnChatLuongHdrId());
                    if (knChatLuongHdrOptional.isPresent()) {
                        knChatLuongHdrOptional.get().setBbTinhKhoId(optional.get().getId());
                        knChatLuongHdrOptional.get().setSoBbTinhKho(optional.get().getSoBbTinhKho());
                        knChatLuongHdrOptional.get().setNgayXuatDocKho(optional.get().getNgayKetThucXuat());
                        knChatLuongHdrOptional.get().setNgayLapBbTinhKho(optional.get().getNgayLap());
                        dcnbPhieuKnChatLuongHdrRepository.save(knChatLuongHdrOptional.get());
                    }
                }

                break;
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                optional.get().setNgayPduyetKtvBQ(LocalDate.now());
                optional.get().setKtvBaoQuanId(currentUser.getUser().getId());
                optional.get().setKtvBaoQuan(currentUser.getUser().getFullName());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                optional.get().setNgayPduyetKt(LocalDate.now());
                optional.get().setKeToanId(currentUser.getUser().getId());
                optional.get().setKeToan(currentUser.getUser().getFullName());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPduyetLdcc(LocalDate.now());
                optional.get().setLanhDaoChiCucId(currentUser.getUser().getId());
                optional.get().setLanhDaoChiCuc(currentUser.getUser().getFullName());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBienBanTinhKhoHdr created = dcnbBienBanTinhKhoHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbBienBanTinhKho objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBienBanTinhKhoHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBienBanTinhKhoHdrDTO> data = page.getContent();

        String title = "Danh sách biên bản tịnh kho";
        String[] rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Thời hạn điều chuyển", "Điểm Kho", "Lô kho", "Số BB tịnh kho", "Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số bảng kê", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-tinh-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBienBanTinhKhoHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho(); //"Số BB tịnh kho", "Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số bảng kê", "Ngày xuất kho"
            objs[6] = dx.getSoBbTinhKho();
            objs[7] = dx.getNgayBatDauXuat();
            objs[8] = dx.getNgayKetThucXuat();
            objs[9] = dx.getSoPhieuXuatKho();
            objs[10] = dx.getSoBangKe();
            objs[11] = dx.getNgayXuatKho();
            objs[12] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbBienBanTinhKhoDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBienBanTinhKhoDtl> optional = dcnbBienBanTinhKhoDtlRepository.findByHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }

    public List<DcnbBienBanTinhKhoHdrDTO> searchList(CustomUserDetails currentUser, SearchDcnbBienBanTinhKho req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        List<DcnbBienBanTinhKhoHdrDTO> searchDto = dcnbBienBanTinhKhoHdrRepository.searchList(req);
        return searchDto;
    }

    public ReportTemplateResponse preview(DcnbBienBanTinhKhoHdrReq objReq) throws Exception {
        var DcnbBienBanTinhKhoHdr = dcnbBienBanTinhKhoHdrRepository.findById(objReq.getId());
        if (!DcnbBienBanTinhKhoHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBienBanTinhKhoHdrPreview = setDataToPreview(DcnbBienBanTinhKhoHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBienBanTinhKhoHdrPreview);
    }

    private DcnbBienBanTinhKhoHdrPreview setDataToPreview(Optional<DcnbBienBanTinhKhoHdr> dcnbBienBanTinhKhoHdr) {
        BigDecimal thua = BigDecimal.ZERO;
        BigDecimal thieu = BigDecimal.ZERO;
        if (dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai() != null) {
            if (dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai().compareTo(BigDecimal.ZERO) < 0) {
                thieu = thieu.add(dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai());
            } else if (dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai().compareTo(BigDecimal.ZERO) == 0) {
                thieu = thieu.add(dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai());
                thua = thua.add(dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai());
            } else {
                thua = thua.add(dcnbBienBanTinhKhoHdr.get().getChenhLechSlConLai());
            }
        }
        return DcnbBienBanTinhKhoHdrPreview.builder()
                .tenDvi(dcnbBienBanTinhKhoHdr.get().getTenDvi())
                .soBbTinhKho(dcnbBienBanTinhKhoHdr.get().getSoBbTinhKho())
                .tenCloaiVthh(dcnbBienBanTinhKhoHdr.get().getTenCloaiVthh())
                .tenLoaiVthh(dcnbBienBanTinhKhoHdr.get().getTenLoaiVthh())
                .tenNganKho(dcnbBienBanTinhKhoHdr.get().getTenNganKho())
                .tenLoKho(dcnbBienBanTinhKhoHdr.get().getTenLoKho())
                .maQhns(dcnbBienBanTinhKhoHdr.get().getMaQhns())
                .ngayLap(dcnbBienBanTinhKhoHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .chiCuc(dcnbBienBanTinhKhoHdr.get().getTenDvi())
                .lanhDaoChiCuc(dcnbBienBanTinhKhoHdr.get().getLanhDaoChiCuc())
                .keToan(dcnbBienBanTinhKhoHdr.get().getKeToan())
                .ktvBaoQuan(dcnbBienBanTinhKhoHdr.get().getKtvBaoQuan())
                .thuKho(dcnbBienBanTinhKhoHdr.get().getThuKho())
                .tongSlXuatTheoQd(dcnbBienBanTinhKhoHdr.get().getTongSlXuatTheoQd())
                .tongSlXuatTheoTt(dcnbBienBanTinhKhoHdr.get().getTongSlXuatTheoTt())
                .slConLaiTheoSs(dcnbBienBanTinhKhoHdr.get().getSlConLaiTheoSs())
                .slConLaiTheoTt(dcnbBienBanTinhKhoHdr.get().getSlConLaiTheoTt())
                .thua(thua)
                .thieu(thieu)
                .nguyeNhan(dcnbBienBanTinhKhoHdr.get().getNguyenNhan())
                .kienNghi(dcnbBienBanTinhKhoHdr.get().getKienNghi())
                .build();
    }
}
