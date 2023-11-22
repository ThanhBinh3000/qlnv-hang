package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBienBanLayMauHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBienBanLayMauHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbLoKhoDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DieuChuyenNoiBo;
import lombok.var;
import com.tcdt.qlnvhang.util.ExportExcel;
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
public class DcnbBienBanLayMauServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;

    @Autowired
    private DcnbBienBanLayMauDtlRepository dcnbBienBanLayMauDtlRepository;

    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private DcnbQuyetDinhDcCHdrRepository dcnbQuyetDinhDcCHdrRepository;


    public Page<DcnbBienBanLayMauHdrDTO> searchPage(CustomUserDetails currentUser, SearchDcnbBienBanLayMau req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBienBanLayMauHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        if ("00".equals(req.getType())) { // kiểu xuất
            req.setTypeQd(Contains.DIEU_CHUYEN);
            searchDto = dcnbBienBanLayMauHdrRepository.searchPageXuat(req, pageable);
        }
        if ("01".equals(req.getType())) { // kiểu nhan
            req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
            searchDto = dcnbBienBanLayMauHdrRepository.searchPageNhan(req, pageable);
        }
        return searchDto;
    }

    @Transactional
    public DcnbBienBanLayMauHdr save(CustomUserDetails currentUser, DcnbBienBanLayMauHdrReq objReq) throws Exception {
        QlnvDmDonvi cqt = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findFirstBySoBbLayMau(objReq.getSoBbLayMau());
//        if (optional.isPresent() && objReq.getSoBbLayMau() != null && objReq.getSoBbLayMau().split("/").length == 1) {
//            throw new Exception("số biên bản lấy mẫu đã tồn tại");
//        }
        DcnbBienBanLayMauHdr data = new DcnbBienBanLayMauHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setKtvBaoQuanId(currentUser.getUser().getId());
        data.setKtvBaoQuan(currentUser.getUser().getUsername());
        data.setMaQhns(cqt.getMaQhns());
        data.setMaDvi(cqt.getMaDvi());
        data.setTenDvi(cqt.getTenDvi());
        data.setLoaiDc(objReq.getLoaiDc());
        data.setType(objReq.getType());
        // Biên bản lấy mẫu thì auto thay đổi thủ kho
        data.setThayDoiThuKho(true);
        data.setTrangThai(Contains.DUTHAO);
        data.setNgayTao(LocalDateTime.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        objReq.getDcnbBienBanLayMauDtl().forEach(e -> {
            e.setDcnbBienBanLayMauHdr(data);
        });
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBLM-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBbLayMau(so);
        dcnbBienBanLayMauHdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU");
        created.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByQdDcIdAndMaLoKho(created.getQdccId(), created.getMaLoKho());
        List<FileDinhKem> fileDinhKemMauNiemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemChupMauNiemPhong(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG");
        created.setFileDinhKemChupMauNiemPhong(fileDinhKemMauNiemPhong);
        return created;
    }

    @Transactional
    public DcnbBienBanLayMauHdr update(CustomUserDetails currentUser, DcnbBienBanLayMauHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBienBanLayMauHdr> soDxuat = dcnbBienBanLayMauHdrRepository.findFirstBySoBbLayMau(objReq.getSoBbLayMau());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBbLayMau())) {
//            if (soDxuat.isPresent() && objReq.getSoBbLayMau().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số biên bản lấy mẫu đã tồn tại");
//                }
//            }
//        }
        DcnbBienBanLayMauHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBienBanLayMauDtl(objReq.getDcnbBienBanLayMauDtl());
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBLM-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBbLayMau(so);
        dcnbBienBanLayMauHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
        List<FileDinhKem> fileDinhKemMauNiemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemChupMauNiemPhong(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG");
        created.setFileDinhKemChupMauNiemPhong(fileDinhKemMauNiemPhong);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU");
        created.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    public List<DcnbBienBanLayMauHdr> detail(List<Long> ids) throws Exception {
        List<DcnbBienBanLayMauHdr> allById = dcnbBienBanLayMauHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);

            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);

            List<FileDinhKem> fileDinhKemChupMauNiemPhong = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
            data.setFileDinhKemChupMauNiemPhong(fileDinhKemChupMauNiemPhong);

        });
        return allById;
    }

    public DcnbBienBanLayMauHdr detail(Long id) throws Exception {
        List<DcnbBienBanLayMauHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBienBanLayMauHdr data = optional.get();
        List<DcnbBienBanLayMauDtl> list = dcnbBienBanLayMauDtlRepository.findByHdrId(data.getId());
        dcnbBienBanLayMauDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
        dcnbBienBanLayMauHdrRepository.delete(data);
    }


    public DcnbBienBanLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBienBanLayMauHdr> optional) throws Exception {
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
//                            optional.get().getQdccId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCucNhan(optional.get().getMaDvi(),
//                            optional.get().getQdccId(),
//                            optional.get().getMaNganKho(),
//                            optional.get().getMaLoKho());
//                }
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                if ("00".equals(optional.get().getType())) { // xuất
//                    dataLinkDtl.setType("XDC" + DcnbBienBanLayMauHdr.TABLE_NAME);
//                } else if ("01".equals(optional.get().getType())) {
//                    dataLinkDtl.setType("NDC" + DcnbBienBanLayMauHdr.TABLE_NAME);
//                } else {
//                    throw new Exception("Type phải là 00 hoặc 01!");
//                }
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(optional.get());
        return created;
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBienBanLayMauHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBienBanLayMauHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public void export(CustomUserDetails currentUser, SearchDcnbBienBanLayMau objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbBienBanLayMauHdrDTO> page = this.searchPage(currentUser, objReq);
        List<DcnbBienBanLayMauHdrDTO> data = page.getContent();

        String title = "Danh sách Biên bản lấy mẫu ";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chuyển của Cục", "Năm KH", "Thời hạn điều chuyển", "Điểm kho", "Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho", "Ngày xuất dốc kho", "Số BB hao dôi", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBienBanLayMauHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoBBLayMau();
            objs[7] = dx.getNgaylayMau();
            objs[8] = dx.getSoBBTinhKho();
            objs[9] = dx.getNgayXuatDocKho();
            objs[10] = dx.getBbHaoDoi();
            objs[11] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbBienBanLayMauHdrDTO> danhSachBienBan(CustomUserDetails currentUser, SearchDcnbBienBanLayMau objReq) {
        // TO DO
//         Lấy ra các biên bản lấy mẫu của câấp dưới - trạng thái ban hành và có qDinhDccId được truyền lên (qDinhDccId truyền lên là qDinhDccId gốc)
//         DcnbBienBanLayMauHdrDTO JOIN  với QUYET định và wherer theo parentID  = qDinhDccId
        List<DcnbBienBanLayMauHdrDTO> dcnbBienBanLayMauHdrDTOList = dcnbBienBanLayMauHdrRepository.findByMaDviAndTrangThaiAndQdinhDcId(currentUser.getDvql(), Contains.BAN_HANH, objReq.getQdDcCucId());
        return dcnbBienBanLayMauHdrDTOList;
    }

    public List<DcnbLoKhoDTO> danhSachMaLokho(SearchDcnbBienBanLayMau objReq) {
        return dcnbBienBanLayMauHdrRepository.danhSachMaLokho(objReq);
    }

    public ReportTemplateResponse preview(DcnbBienBanLayMauHdrReq objReq) throws Exception {
        var dcnbBienBanLayMauHdr = dcnbBienBanLayMauHdrRepository.findById(objReq.getId());
        if (!dcnbBienBanLayMauHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        Optional<UserInfo> userInfo = null;
        if (dcnbBienBanLayMauHdr.get().getNguoiPDuyet() != null) {
            userInfo = userInfoRepository.findById(dcnbBienBanLayMauHdr.get().getNguoiPDuyet());
        }
        Optional<DcnbQuyetDinhDcCHdr> dcnbQuyetDinhDcCHdr = dcnbQuyetDinhDcCHdrRepository.findById(dcnbBienBanLayMauHdr.get().getQdccId());
        if (!dcnbQuyetDinhDcCHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBangKeCanHangPreview = setDataToPreview(dcnbBienBanLayMauHdr, userInfo, dcnbQuyetDinhDcCHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBangKeCanHangPreview);
    }

    private DcnbBienBanLayMauHdrPreview setDataToPreview(Optional<DcnbBienBanLayMauHdr> dcnbBienBanLayMauHdr,
                                                         Optional<UserInfo> userInfo,
                                                         Optional<DcnbQuyetDinhDcCHdr> dcnbQuyetDinhDcCHdr) {
//        2136+*Lấy mẫu ngẫu nhiên+*true
        String pplm = StringUtils.isEmpty(dcnbBienBanLayMauHdr.get().getPPLayMau()) ? "" : Lists.newArrayList(Splitter.on("+*").split(dcnbBienBanLayMauHdr.get().getPPLayMau())).get(1);
        return DcnbBienBanLayMauHdrPreview.builder()
                .donViCungCapHang(dcnbBienBanLayMauHdr.get().getTenDvi())
                .quyChuanTieuChuan("")
                .chungLoaiHangHoa(dcnbBienBanLayMauHdr.get().getTenCloaiVthh())
                .ngayLayMau(dcnbBienBanLayMauHdr.get().getNgayLayMau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tenDvi(dcnbBienBanLayMauHdr.get().getTenDvi())
                .tenDviCha(dcnbBienBanLayMauHdr.get().getTenDviCha())
                .soLuongMau(dcnbBienBanLayMauHdr.get().getSoLuongMau())
                .donViTinh(dcnbBienBanLayMauHdr.get().getDonViTinh())
                .pPLayMau(pplm)
                .chiTieuKiemTra(DieuChuyenNoiBo.getDataNew(dcnbBienBanLayMauHdr.get().getChiTieuKiemTra()))
                .ktvBaoQuan(dcnbBienBanLayMauHdr.get().getKtvBaoQuan())
                .truongBpKtbq(dcnbBienBanLayMauHdr.get().getKtvBaoQuan())
                .lanhDaoChiCuc(userInfo.isPresent() ? userInfo.get().getFullName() : "")
                .dcnbBienBanLayMauDtl(dcnbBienBanLayMauDtlToDto(dcnbBienBanLayMauHdr.get().getDcnbBienBanLayMauDtl()))
                .soQdinhDcc(dcnbBienBanLayMauHdr.get().getSoQdinhDcc())
                .ngayKyQDCC(dcnbQuyetDinhDcCHdr.get().getNgayKyQdinh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .soBbLayMau(dcnbBienBanLayMauHdr.get().getSoBbLayMau())
                .tenThuKho(dcnbBienBanLayMauHdr.get().getTenThuKho())
                .build();
    }

    private List<DcnbBienBanLayMauDtlDto> dcnbBienBanLayMauDtlToDto(List<DcnbBienBanLayMauDtl> dcnbBienBanLayMauDtl) {
        List<DcnbBienBanLayMauDtlDto> dcnbBienBanLayMauDtlDtos = new ArrayList<>();
        int stt = 1;
        for (var res : dcnbBienBanLayMauDtl) {
            var dcnbBienBanLayMauDtlDto = DcnbBienBanLayMauDtlDto.builder()
                    .stt(stt++)
                    .loaiDaiDien(res.getLoaiDaiDien())
                    .tenDaiDien(res.getTenDaiDien())
                    .build();
            dcnbBienBanLayMauDtlDtos.add(dcnbBienBanLayMauDtlDto);
        }
        return dcnbBienBanLayMauDtlDtos;
    }
}
