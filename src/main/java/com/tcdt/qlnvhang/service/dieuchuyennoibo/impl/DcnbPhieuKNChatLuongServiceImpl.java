package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKnChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKnChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuKnChatLuongHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKnChatLuong;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbPhieuKnChatLuongHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.DieuChuyenNoiBo;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DcnbPhieuKNChatLuongServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbPhieuKnChatLuongHdrRepository dcnbPhieuKnChatLuongHdrRepository;

    @Autowired
    private DcnbPhieuKnChatLuongDtlRepository dcnbPhieuKnChatLuongDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    public Page<DcnbPhieuKnChatLuongHdrDTO> searchPage(CustomUserDetails currentUser, SearchPhieuKnChatLuong req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setMaDvi(dvql.substring(0, 6));
            req.setTrangThai(Contains.DADUYET_LDC);
        } else {
            req.setMaDvi(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        Page<DcnbPhieuKnChatLuongHdrDTO> searchDto = null;

        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        if ("00".equals(req.getType())) { // kiểu xuất
            searchDto = dcnbPhieuKnChatLuongHdrRepository.searchPageXuat(req, pageable);
        }
        if ("01".equals(req.getType())) { // kiểu nhan
            searchDto = dcnbPhieuKnChatLuongHdrRepository.searchPageNhan(req, pageable);
        }
        return searchDto;
    }

    @Transactional
    public DcnbPhieuKnChatLuongHdr save(CustomUserDetails currentUser, DcnbPhieuKnChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng thêm mới chỉ dành cho cấp cục");
        }
//        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
//        if (optional.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
//            throw new Exception("Số phiếu đã tồn tại");
//        }
        List<DcnbPhieuKnChatLuongHdr> lists = new ArrayList<>();
        if(StringUtils.isEmpty(objReq.getMaLoKho())){
            lists = dcnbPhieuKnChatLuongHdrRepository.findByMaDviAndSoQdinhDcAndMaNganKho(currentUser.getDvql(), objReq.getSoQdinhDc(), objReq.getMaNganKho());
        }else {
            lists = dcnbPhieuKnChatLuongHdrRepository.findByMaDviAndSoQdinhDcAndMaLoKho(currentUser.getDvql(), objReq.getSoQdinhDc(), objReq.getMaLoKho());
        }
        if(!lists.isEmpty()){
            throw new Exception("Ngăn Lô kho đã được khởi tạo!");
        }
        DcnbPhieuKnChatLuongHdr data = new DcnbPhieuKnChatLuongHdr();
        BeanUtils.copyProperties(objReq, data);
        Optional<DcnbKeHoachDcDtl> keHoachDcDtl = dcnbKeHoachDcDtlRepository.findById(objReq.getKeHoachDcDtlId());
        if (keHoachDcDtl.isPresent()) {
            if (keHoachDcDtl.get().getParentId() != null) {
                data.setKeHoachDcDtlId(keHoachDcDtl.get().getParentId());
            } else {
                data.setKeHoachDcDtlId(objReq.getKeHoachDcDtlId());
            }
        }
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNgayTao(LocalDateTime.now());
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.DUTHAO);
        data.setType(objReq.getType());
        data.setLoaiDc(objReq.getLoaiDc());
        objReq.getDcnbPhieuKnChatLuongDtl().forEach(e -> e.setDcnbPhieuKnChatLuongHdr(data));
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PKNCL-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKnChatLuongHdrRepository.save(created);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getDinhKems(), created.getId(), DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setDinhKems(bienBanLayMauDinhKem);
        return created;
    }

    @Transactional
    public DcnbPhieuKnChatLuongHdr update(CustomUserDetails currentUser, DcnbPhieuKnChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng cập nhật chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbPhieuKnChatLuongHdr> soPhieu = dcnbPhieuKnChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoPhieu())) {
//            if (soPhieu.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
//                if (!soPhieu.get().getId().equals(objReq.getId())) {
//                    throw new Exception("Số phiếu đã tồn tại");
//                }
//            }
//        }

        DcnbPhieuKnChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        Optional<DcnbKeHoachDcDtl> keHoachDcDtl = dcnbKeHoachDcDtlRepository.findById(objReq.getKeHoachDcDtlId());
        if(!data.getKeHoachDcDtlId().equals(objReq.getKeHoachDcDtlId())){
            if (keHoachDcDtl.isPresent()) {
                if (keHoachDcDtl.get().getParentId() != null) {
                    data.setKeHoachDcDtlId(keHoachDcDtl.get().getParentId());
                } else {
                    data.setKeHoachDcDtlId(objReq.getKeHoachDcDtlId());
                }
            }
        }
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDateTime.now());
        data.setDcnbPhieuKnChatLuongDtl(objReq.getDcnbPhieuKnChatLuongDtl());
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PKNCL-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieu(so);
        dcnbPhieuKnChatLuongHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getDinhKems(), created.getId(), DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setDinhKems(bienBanLayMauDinhKem);
        return created;
    }

    public List<DcnbPhieuKnChatLuongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbPhieuKnChatLuongHdr> allById = dcnbPhieuKnChatLuongHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU"));
            data.setDinhKems(bienBanLayMauDinhKem);
            List<DcnbPhieuKnChatLuongDtl> khs = dcnbPhieuKnChatLuongDtlRepository.findByHdrId(data.getId());
            data.setDcnbPhieuKnChatLuongDtl(khs);
        });
        return allById;
    }

    public DcnbPhieuKnChatLuongHdr detail(Long id) throws Exception {
        List<DcnbPhieuKnChatLuongHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuKnChatLuongHdr data = optional.get();
        List<DcnbPhieuKnChatLuongDtl> list = dcnbPhieuKnChatLuongDtlRepository.findByHdrId(data.getId());
        dcnbPhieuKnChatLuongDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKnChatLuongHdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<DcnbPhieuKnChatLuongHdr> list = dcnbPhieuKnChatLuongHdrRepository.findAllByIdIn(idSearchReq.getIdList());
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(DcnbPhieuKnChatLuongHdr::getId).collect(Collectors.toList());
//        List<DcnbPhieuKnChatLuongDtl> listDtl = dcnbPhieuKnChatLuongDtlRepository.findByHdrIdIn(listId);
//        dcnbPhieuKnChatLuongDtlRepository.deleteAll(listDtl);
//        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
//        dcnbPhieuKnChatLuongHdrRepository.deleteAll(list);
//    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuKnChatLuongHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbPhieuKnChatLuongHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbPhieuKnChatLuongHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbPhieuKnChatLuongHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_TP + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_TP:
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TP + Contains.TU_CHOI_TP:
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setTpNguoiKt(currentUser.getUser().getFullName());
                optional.get().setTpNguoiKtId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.TU_CHOI_LDC:
                optional.get().setNguoiDuyetLdCuc(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdCuc(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setTpNguoiKt(currentUser.getUser().getFullName());
                optional.get().setTpNguoiKtId(currentUser.getUser().getId());
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.DA_DUYET_LDC:
                optional.get().setNguoiDuyetLdCuc(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdCuc(LocalDate.now());
//                DcnbDataLinkHdr dataLink = null;
//                if ("00".equals(optional.get().getType())) { // xuất
//                    dataLink = dcnbDataLinkHdrRepository.findDataLinkCuc(optional.get().getMaDvi(),
//                            optional.get().getQdDcId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLink = dcnbDataLinkHdrRepository.findDataLinkCucNhan(optional.get().getMaDvi(),
//                            optional.get().getQdDcId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                }
//
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                if ("00".equals(optional.get().getType())) { // xuất
//                    dataLinkDtl.setType("XDC" + DcnbPhieuKnChatLuongHdr.TABLE_NAME);
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLinkDtl.setType("NDC" + DcnbPhieuKnChatLuongHdr.TABLE_NAME);
//                } else {
//                    throw new Exception("Type phải là 00 hoặc 01!");
//                }
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchPhieuKnChatLuong objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbPhieuKnChatLuongHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbPhieuKnChatLuongHdrDTO> data = page.getContent();

        String title = "Danh sách phiếu kiểm nghiệm chất lượng ";
        String[] rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Thời hạn điều chuyển", "Điểm kho", "Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho", "Trạng thái"};
        String fileName = "danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbPhieuKnChatLuongHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieuKnChatLuong();
            objs[7] = dx.getNgayKiemNghiem();
            objs[8] = dx.getSoBBLayMau();
            objs[9] = dx.getNgaylayMau();
            objs[10] = dx.getSoBBTinhKho();
            objs[11] = dx.getNgayXuatDocKho();
            objs[12] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbPhieuKnChatLuongHdrDTO> danhSach(CustomUserDetails currentUser, SearchPhieuKnChatLuong req) {
        List<DcnbPhieuKnChatLuongHdrDTO> searchDto = null;

        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        if ("00".equals(req.getType())) { // kiểu xuất
        }
        if ("01".equals(req.getType())) { // kiểu nhan
        }
        searchDto = dcnbPhieuKnChatLuongHdrRepository.searchList(req);

        return searchDto;
    }

    public ReportTemplateResponse preview(DcnbPhieuKnChatLuongHdrReq objReq) throws Exception {
        var dcnbPhieuKnChatLuongHdr = dcnbPhieuKnChatLuongHdrRepository.findById(objReq.getId());
        if (!dcnbPhieuKnChatLuongHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        Optional<UserInfo> userInfo = null;
        if (dcnbPhieuKnChatLuongHdr.get().getNguoiDuyetLdCuc() != null) {
            userInfo = userInfoRepository.findById(dcnbPhieuKnChatLuongHdr.get().getNguoiDuyetLdCuc());
        }
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbPhieuKnChatLuongHdrPreview = setDataToPreview(dcnbPhieuKnChatLuongHdr, userInfo);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbPhieuKnChatLuongHdrPreview);
    }

    private DcnbPhieuKnChatLuongHdrPreview setDataToPreview(Optional<DcnbPhieuKnChatLuongHdr> dcnbPhieuKnChatLuongHdr,
                                                            Optional<UserInfo> userInfo) {
        return DcnbPhieuKnChatLuongHdrPreview.builder()
                .maDvi(dcnbPhieuKnChatLuongHdr.get().getMaDvi())
                .tenDvi(dcnbPhieuKnChatLuongHdr.get().getTenDvi())
                .soPhieu(dcnbPhieuKnChatLuongHdr.get().getSoPhieu())
                .tenNganKho(dcnbPhieuKnChatLuongHdr.get().getTenNganKho())
                .tenLoKho(dcnbPhieuKnChatLuongHdr.get().getTenLoKho())
                .tenNhaKho(dcnbPhieuKnChatLuongHdr.get().getTenNhaKho())
                .tenDiemKho(dcnbPhieuKnChatLuongHdr.get().getTenDiemKho())
                .soLuongHangBaoQuan("")
                .hinhThucBq(DieuChuyenNoiBo.getData(dcnbPhieuKnChatLuongHdr.get().getHinhThucBq()))
                .tenThuKho(dcnbPhieuKnChatLuongHdr.get().getTenThuKho())
                .ngayNhapDayKho(dcnbPhieuKnChatLuongHdr.get().getNgayNhapDayKho() ==null ?"":dcnbPhieuKnChatLuongHdr.get().getNgayNhapDayKho().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .ngayLayMau(dcnbPhieuKnChatLuongHdr.get().getNgayLayMau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .ngayKiem(dcnbPhieuKnChatLuongHdr.get().getNgayKiem().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .danhGiaCamQuan(dcnbPhieuKnChatLuongHdr.get().getDanhGiaCamQuan())
                .nhanXetKetLuan(dcnbPhieuKnChatLuongHdr.get().getNhanXetKetLuan())
                .ngayNhap(dcnbPhieuKnChatLuongHdr.get().getNgayLapPhieu().getDayOfMonth())
                .thangNhap(dcnbPhieuKnChatLuongHdr.get().getNgayLapPhieu().getMonth().getValue())
                .namNhap(dcnbPhieuKnChatLuongHdr.get().getNgayLapPhieu().getYear())
                .nguoiKt(dcnbPhieuKnChatLuongHdr.get().getNguoiKt())
                .truongBpKtbq(dcnbPhieuKnChatLuongHdr.get().getTpNguoiKt())
                .lanhDaoCuc(userInfo.isPresent() ? userInfo.get().getFullName() : "")
                .maQhns(dcnbPhieuKnChatLuongHdr.get().getMaQhns())
                .loaiHangHoa(dcnbPhieuKnChatLuongHdr.get().getTenCloaiVthh())
                .hinhThucKeLot(DieuChuyenNoiBo.getData(dcnbPhieuKnChatLuongHdr.get().getHinhThucBq()))
                .dcnbPhieuKnChatLuongDtls(DcnbPhieuKnChatLuongDtlToDto(dcnbPhieuKnChatLuongHdr.get().getDcnbPhieuKnChatLuongDtl()))
                .build();
    }

    private List<DcnbPhieuKnChatLuongDtlDto> DcnbPhieuKnChatLuongDtlToDto(List<DcnbPhieuKnChatLuongDtl> dcnbPhieuKnChatLuongDtl) {
        List<DcnbPhieuKnChatLuongDtlDto> dcnbPhieuKnChatLuongDtlDtos = new ArrayList<>();
        int stt = 1;
        for (var res : dcnbPhieuKnChatLuongDtl) {
            var dcnbPhieuKnChatLuongDtlDto = DcnbPhieuKnChatLuongDtlDto.builder()
                    .stt(stt++)
                    .chiTieuCl(res.getChiTieuCl())
                    .chiSoCl(res.getChiSoCl())
                    .ketQuaPt(res.getKetQuaPt())
                    .phuongPhap(res.getPhuongPhap())
                    .danhGia(DieuChuyenNoiBo.checkDanhGia(res.getDanhGia()))
                    .build();
            dcnbPhieuKnChatLuongDtlDtos.add(dcnbPhieuKnChatLuongDtlDto);
        }
        return dcnbPhieuKnChatLuongDtlDtos;
    }
}
