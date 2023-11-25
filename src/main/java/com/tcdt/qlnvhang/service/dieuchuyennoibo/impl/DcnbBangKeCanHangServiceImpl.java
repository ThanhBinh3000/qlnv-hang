package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeCanHangHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBangKeCanHangPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbBangKeCanHangServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbBangKeCanHangHdrRepository dcnbBangKeCanHangHdrRepository;

    @Autowired
    private DcnbBangKeCanHangDtlRepository dcnbBangKeCanHangDtlRepository;

    @Autowired
    private DcnbPhieuXuatKhoHdrRepository dcnbPhieuXuatKhoHdrRepository;

    @Autowired
    private DcnbPhieuNhapKhoHdrRepository dcnbPhieuNhapKhoHdrRepository;

    @Autowired
    private KtNganKhoRepository ktNganKhoRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Transactional
    public DcnbBangKeCanHangHdr save(CustomUserDetails currentUser, DcnbBangKeCanHangHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//            throw new Exception("Số bảng kê đã tồn tại");
//        }
        if("00".equals(objReq.getType())){
            Optional<DcnbPhieuXuatKhoHdr> phieuXuatKhoHdr = dcnbPhieuXuatKhoHdrRepository.findById(objReq.getPhieuXuatKhoId());
            if(phieuXuatKhoHdr.isPresent()){
                if(phieuXuatKhoHdr.get().getBangKeVtId()!= null){
                    throw new Exception("Phiếu xuất đã có bảng kê!");
                }
            }
        }else if("01".equals(objReq.getType())){
            Optional<DcnbPhieuNhapKhoHdr> phieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(objReq.getPhieuNhapKhoId());
            if(phieuNhapKhoHdr.isPresent()){
                if(phieuNhapKhoHdr.get().getBangKeVtId()!= null){
                    throw new Exception("Phiếu nhập đã có bảng kê!");
                }
            }
        }

        DcnbBangKeCanHangHdr data = new DcnbBangKeCanHangHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        if (objReq.getDcnbBangKeCanHangDtl() != null) {
            objReq.getDcnbBangKeCanHangDtl().forEach(e -> e.setDcnbBangKeCanHangHdr(data));
        }
        DcnbBangKeCanHangHdr created = dcnbBangKeCanHangHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKCH-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBangKe(so);
        dcnbBangKeCanHangHdrRepository.save(created);
        return created;
    }

    public Page<DcnbBangKeCanHangHdrDTO> searchPage(CustomUserDetails currentUser, SearchBangKeCanHang req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        Page<DcnbBangKeCanHangHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        if ("00".equals(req.getType())) { // kiểu xuất
            searchDto = dcnbBangKeCanHangHdrRepository.searchPageXuat(req, pageable);
        } else if ("01".equals(req.getType())) { // kiểu nhan
            searchDto = dcnbBangKeCanHangHdrRepository.searchPageNhan(req, pageable);
        }
        return searchDto;
    }

    @Transactional
    public DcnbBangKeCanHangHdr update(CustomUserDetails currentUser, DcnbBangKeCanHangHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBangKeCanHangHdr> soDxuat = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
//            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }
        if("00".equals(objReq.getType())){
            Optional<DcnbPhieuXuatKhoHdr> phieuXuatKhoHdr = dcnbPhieuXuatKhoHdrRepository.findById(objReq.getPhieuXuatKhoId());
            if(phieuXuatKhoHdr.isPresent()){
                if(!objReq.getPhieuXuatKhoId().equals(optional.get().getPhieuNhapKhoId())){
                    if(phieuXuatKhoHdr.get().getBangKeVtId()!= null){
                        throw new Exception("Phiếu nhập đã có bảng cân hàng!");
                    }
                }
            }
        }else if("01".equals(objReq.getType())){
            Optional<DcnbPhieuNhapKhoHdr> phieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(objReq.getPhieuNhapKhoId());
            if(phieuNhapKhoHdr.isPresent()){
                if(!objReq.getPhieuNhapKhoId().equals(optional.get().getPhieuNhapKhoId())){
                    if(phieuNhapKhoHdr.get().getBangKeVtId()!= null){
                        throw new Exception("Phiếu nhập đã có bảng cân hàng!");
                    }
                }
            }
        }
        DcnbBangKeCanHangHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        objReq.setType(optional.get().getType());
        objReq.setLoaiDc(optional.get().getLoaiDc());
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBangKeCanHangDtl(objReq.getDcnbBangKeCanHangDtl());
        DcnbBangKeCanHangHdr created = dcnbBangKeCanHangHdrRepository.save(data);
        String soBangKe = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKCH-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBangKe(soBangKe);
        dcnbBangKeCanHangHdrRepository.save(created);
        return created;
    }


    public List<DcnbBangKeCanHangHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbBangKeCanHangHdr> allById = dcnbBangKeCanHangHdrRepository.findAllById(ids);
        return allById;
    }

    public DcnbBangKeCanHangHdr details(Long id) throws Exception {
        List<DcnbBangKeCanHangHdr> details = details(Arrays.asList(id));
        DcnbBangKeCanHangHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getDcnbBangKeCanHangDtl());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBangKeCanHangHdr data = optional.get();
        List<DcnbBangKeCanHangDtl> list = dcnbBangKeCanHangDtlRepository.findByHdrId(data.getId());
        dcnbBangKeCanHangDtlRepository.deleteAll(list);
        dcnbBangKeCanHangHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbBangKeCanHangHdr> list = dcnbBangKeCanHangHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbBangKeCanHangHdr::getId).collect(Collectors.toList());
        List<DcnbBangKeCanHangDtl> listBangKe = dcnbBangKeCanHangDtlRepository.findByHdrIdIn(listId);
        dcnbBangKeCanHangDtlRepository.deleteAll(listBangKe);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBangKeCanHangHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<DcnbBangKeCanHangHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBangKeCanHangHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBangKeCanHangHdr> optional) throws Exception {
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
//                DcnbDataLinkHdr dataLink = null;
//                if ("00".equals(optional.get().getType())) { // xuất
//                    dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                            optional.get().getQDinhDccId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                    Optional<DcnbPhieuXuatKhoHdr> dcnbPhieuXuatKhoHdr = dcnbPhieuXuatKhoHdrRepository.findById(optional.get().getPhieuXuatKhoId());
//                    if(dcnbPhieuXuatKhoHdr.isPresent()){
//                        dcnbPhieuXuatKhoHdr.get().setBangKeChId(optional.get().getId());
//                        dcnbPhieuXuatKhoHdr.get().setSoBangKeCh(optional.get().getSoBangKe());
//                        dcnbPhieuXuatKhoHdrRepository.save(dcnbPhieuXuatKhoHdr.get());
//                    }
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCucNhan(optional.get().getMaDvi(),
//                            optional.get().getQDinhDccId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                    Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(optional.get().getPhieuXuatKhoId());
//                    if(dcnbPhieuNhapKhoHdr.isPresent()){
//                        dcnbPhieuNhapKhoHdr.get().setBangKeChId(optional.get().getId());
//                        dcnbPhieuNhapKhoHdr.get().setSoBangKeCh(optional.get().getSoBangKe());
//                        dcnbPhieuNhapKhoHdrRepository.save(dcnbPhieuNhapKhoHdr.get());
//                    }
//                } else {
//                    throw new Exception("Type phải là 00 hoặc 01!");
//                }
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                if ("00".equals(optional.get().getType())) { // xuất
//                    dataLinkDtl.setType("XDC" + DcnbBangKeCanHangHdr.TABLE_NAME);
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLinkDtl.setType("NDC" + DcnbBangKeCanHangHdr.TABLE_NAME);
//                } else {
//                    throw new Exception("Type phải là 00 hoặc 01!");
//                }
                if ("00".equals(optional.get().getType())) { // xuất
                    Optional<DcnbPhieuXuatKhoHdr> dcnbPhieuXuatKhoHdr = dcnbPhieuXuatKhoHdrRepository.findById(optional.get().getPhieuXuatKhoId());
                    if (dcnbPhieuXuatKhoHdr.isPresent()) {
                        dcnbPhieuXuatKhoHdr.get().setBangKeChId(optional.get().getId());
                        dcnbPhieuXuatKhoHdr.get().setSoBangKeCh(optional.get().getSoBangKe());
                        dcnbPhieuXuatKhoHdrRepository.save(dcnbPhieuXuatKhoHdr.get());
                    }
                } else if ("01".equals(optional.get().getType())) {
                    Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(optional.get().getPhieuNhapKhoId());
                    if (dcnbPhieuNhapKhoHdr.isPresent()) {
                        dcnbPhieuNhapKhoHdr.get().setBangKeChId(optional.get().getId());
                        dcnbPhieuNhapKhoHdr.get().setSoBangKeCh(optional.get().getSoBangKe());
                        dcnbPhieuNhapKhoHdrRepository.save(dcnbPhieuNhapKhoHdr.get());
                    }
                } else {
                    throw new Exception("Type phải là 00 hoặc 01!");
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBangKeCanHangHdr created = dcnbBangKeCanHangHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchBangKeCanHang objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBangKeCanHangHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBangKeCanHangHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = null;
        if ("00".equals(objReq.getType())) { // kiểu xuất
            rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Thời hạn điều chuyển", "Điểm kho", "Lô kho", "Số phiếu XK", "Số bảng kê xuất ĐC LT", "Ngày xuất kho", "Trạng thái"};
        } else if ("01".equals(objReq.getType())) { // kiểu nhan
            rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Thời hạn ĐC", "Điểm kho", "Lô kho", "Số bảng kê", "Số phiếu nhập kho", "Ngày nhập kho", "Trạng thái"};
        }
        String fileName = "danh-sach-bang-ke-can-hang.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBangKeCanHangHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieuXuatKho() == null ? dx.getSoPhieuNhapKho() : dx.getSoPhieuXuatKho();
            objs[7] = dx.getSoBangKe();
            objs[8] = dx.getNgayXuatKho() == null ? dx.getNgayNhapKho() : dx.getNgayXuatKho();
            objs[9] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbBangKeCanHangDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBangKeCanHangDtl> optional = dcnbBangKeCanHangDtlRepository.findByHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }

    public List<DcnbBangKeCanHangHdrDTO> list(CustomUserDetails currentUser, SearchBangKeCanHang req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        List<DcnbBangKeCanHangHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        if ("00".equals(req.getType())) { // kiểu xuất
            searchDto = dcnbBangKeCanHangHdrRepository.searchListXuat(req);
        }
        if ("01".equals(req.getType())) { // kiểu nhan
            searchDto = dcnbBangKeCanHangHdrRepository.searchListNhan(req);
        }
        return searchDto;
    }
    public ReportTemplateResponse preview(DcnbBangKeCanHangHdrReq objReq) throws Exception {
        var dcnbBangKeCanHangHdr = dcnbBangKeCanHangHdrRepository.findById(objReq.getId());
        if (!dcnbBangKeCanHangHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBangKeCanHangPreview = setDataToPreview(dcnbBangKeCanHangHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBangKeCanHangPreview);
    }

    private DcnbBangKeCanHangPreview setDataToPreview(Optional<DcnbBangKeCanHangHdr> dcnbBangKeCanHangHdr) {
        return DcnbBangKeCanHangPreview.builder()
                .tenBang(DieuChuyenNoiBo.checkType(dcnbBangKeCanHangHdr.get().getType()))
                .maDvi(dcnbBangKeCanHangHdr.get().getMaDvi())
                .maQhns(dcnbBangKeCanHangHdr.get().getMaQhns())
                .soBangKe(dcnbBangKeCanHangHdr.get().getSoBangKe())
                .tenThuKho(dcnbBangKeCanHangHdr.get().getTenThuKho())
                .lhKho(getDataKho(dcnbBangKeCanHangHdr.get().getMaDvi()))
                .tenNganKho(dcnbBangKeCanHangHdr.get().getTenNganKho())
                .tenLoKho(dcnbBangKeCanHangHdr.get().getTenLoKho())
                .tenDiemKho(dcnbBangKeCanHangHdr.get().getTenDiemKho())
                .tenDvi(dcnbBangKeCanHangHdr.get().getTenDvi())
                .chungLoaiHangHoa(dcnbBangKeCanHangHdr.get().getCloaiVthh())
                .tenDonViTinh(dcnbBangKeCanHangHdr.get().getDonViTinh())
                .tenNguoiGiaoHang(dcnbBangKeCanHangHdr.get().getTenNguoiGiaoHang())
                .thoiGianGiaoNhan(dcnbBangKeCanHangHdr.get().getThoiGianGiaoNhan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .nguoiGiamSat("")
                .tongTrongLuongCabaoBi(dcnbBangKeCanHangHdr.get().getTongTrongLuongCabaoBi())
                .tongTrongLuongBaoBi(dcnbBangKeCanHangHdr.get().getTongTrongLuongBaoBi())
                .tongTrongLuongTruBi(dcnbBangKeCanHangHdr.get().getTongTrongLuongTruBi())
                .tongTrongLuongTruBiText(dcnbBangKeCanHangHdr.get().getTongTrongLuongTruBiText())
                .ngayNhap(dcnbBangKeCanHangHdr.get().getNgayNhap().getDayOfMonth())
                .thangNhap(dcnbBangKeCanHangHdr.get().getNgayNhap().getMonth().getValue())
                .namNhap(dcnbBangKeCanHangHdr.get().getNgayNhap().getYear())
                .dcnbBangKeCanHangDtl(dcnbBangKeCanHangDtlToDto(dcnbBangKeCanHangHdr.get().getDcnbBangKeCanHangDtl()))
                .build();
    }

    private List<DcnbBangKeCanHangDtlDto> dcnbBangKeCanHangDtlToDto(List<DcnbBangKeCanHangDtl> dcnbBangKeCanHangDtl) {
        List<DcnbBangKeCanHangDtlDto> dcnbBangKeCanHangDtlDtoList = new ArrayList<>();
        int stt = 1;
        for (DcnbBangKeCanHangDtl res : dcnbBangKeCanHangDtl) {
            var dcnbBangKeCanHangDtlDto = DcnbBangKeCanHangDtlDto.builder()
                    .stt(stt++)
                    .maCan(res.getMaCan())
                    .soBaoBi(res.getMaCan())
                    .trongLuongCaBaoBi(res.getTrongLuongCaBaoBi())
                    .build();
            dcnbBangKeCanHangDtlDtoList.add(dcnbBangKeCanHangDtlDto);
        }
        return dcnbBangKeCanHangDtlDtoList;
    }

    public String getDataKho(String maDvi){
        try {
            if (!StringUtils.isEmpty(maDvi)) {
                Map<String, String> listLoaiKho = getListDanhMucChung("LOAI_KHO");
                if (maDvi.length() == 14) { //ma kho
                    KtNganKho ktNganKho = ktNganKhoRepository.findByMaNgankho(maDvi);
                    ktNganKho.setLhKho(listLoaiKho.get(ktNganKho.getLoaikhoId()));
                    return ktNganKho.getLhKho();
                } else {
                    KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(maDvi);
                    nganLo.setLhKho(listLoaiKho.get(nganLo.getLoaikhoId()));
                    return nganLo.getLhKho();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
