package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBienBanHaoDoiHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanHaoDoi;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBienBanHaoDoiHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiDtlDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiTtDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
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
import java.util.stream.Collectors;

@Service
public class DcnbBienBanHaoDoiServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbBienBanHaoDoiHdrRepository dcnbBienBanHaoDoiHdrRepository;

    @Autowired
    private DcnbBienBanHaoDoiDtlRepository dcnbBienBanHaoDoiDtlRepository;

    @Autowired
    private DcnbBienBanHaoDoiTtDtlRepository dcnbBienBanHaoDoiTtDtlRepository;

    @Autowired
    private DcnbQuyetDinhDcCHdrServiceImpl dcnbQuyetDinhDcCHdrServiceImpl;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;

    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;
    @Autowired
    private DcnbBienBanTinhKhoHdrRepository dcnbBienBanTinhKhoHdrRepository;


    public Page<DcnbBienBanHaoDoiHdrDTO> searchPage(CustomUserDetails currentUser, SearchDcnbBienBanHaoDoi req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBienBanHaoDoiHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.DIEU_CHUYEN);
        searchDto = dcnbBienBanHaoDoiHdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Transactional
    public DcnbBienBanHaoDoiHdr save(CustomUserDetails currentUser, DcnbBienBanHaoDoiHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBienBanHaoDoiHdr> optional = dcnbBienBanHaoDoiHdrRepository.findFirstBySoBienBan(objReq.getSoBienBan());
//        if (optional.isPresent() && objReq.getSoBienBan().split("/").length == 1) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }
        DcnbBienBanHaoDoiHdr data = new DcnbBienBanHaoDoiHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        objReq.getDanhSachBangKe().forEach(e -> {
            e.setDcnbBienBanHaoDoiHdr(data);
        });
        objReq.getThongTinHaoHut().forEach(e -> {
            e.setDcnbBienBanHaoDoiHdr(data);
        });
        DcnbBienBanHaoDoiHdr created = dcnbBienBanHaoDoiHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBHD-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBienBan(so);
        dcnbBienBanHaoDoiHdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBienBanHaoDoiHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Transactional
    public DcnbBienBanHaoDoiHdr update(CustomUserDetails currentUser, DcnbBienBanHaoDoiHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBienBanHaoDoiHdr> optional = dcnbBienBanHaoDoiHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBienBanHaoDoiHdr> soBienBan = dcnbBienBanHaoDoiHdrRepository.findFirstBySoBienBan(objReq.getSoBienBan());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBienBan())) {
//            if (soBienBan.isPresent() && objReq.getSoBienBan().split("/").length == 1) {
//                if (!soBienBan.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số biên bản đã tồn tại");
//                }
//            }
//        }

        DcnbBienBanHaoDoiHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        objReq.setTenDvi(data.getTenDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachBangKe(objReq.getDanhSachBangKe());
        data.setThongTinHaoHut(objReq.getThongTinHaoHut());
        DcnbBienBanHaoDoiHdr created = dcnbBienBanHaoDoiHdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBHD-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoBienBan(so);
        dcnbBienBanHaoDoiHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanHaoDoiHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBienBanHaoDoiHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }


    public List<DcnbBienBanHaoDoiHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBienBanHaoDoiHdr> optional = dcnbBienBanHaoDoiHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbBienBanHaoDoiHdr> allById = dcnbBienBanHaoDoiHdrRepository.findAllById(ids);
        optional.forEach(item -> {
            List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(item.getId(), Collections.singleton(DcnbBienBanHaoDoiHdr.TABLE_NAME));
            item.setFileDinhKems(canCu);
        });

        return allById;
    }

    public DcnbBienBanHaoDoiHdr details(Long id) throws Exception {
        List<DcnbBienBanHaoDoiHdr> details = details(Arrays.asList(id));
        DcnbBienBanHaoDoiHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getDanhSachBangKe());
            Hibernate.initialize(result.getThongTinHaoHut());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbBienBanHaoDoiHdr> optional = dcnbBienBanHaoDoiHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBienBanHaoDoiHdr data = optional.get();
        List<DcnbBienBanHaoDoiTtDtl> list = dcnbBienBanHaoDoiTtDtlRepository.findByHdrId(data.getId());
        List<DcnbBienBanHaoDoiDtl> list2 = dcnbBienBanHaoDoiDtlRepository.findByHdrId(data.getId());
        dcnbBienBanHaoDoiTtDtlRepository.deleteAll(list);
        dcnbBienBanHaoDoiDtlRepository.deleteAll(list2);
        dcnbBienBanHaoDoiHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbBienBanHaoDoiHdr> list = dcnbBienBanHaoDoiHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbBienBanHaoDoiHdr::getId).collect(Collectors.toList());
        List<DcnbBienBanHaoDoiTtDtl> listBangKe = dcnbBienBanHaoDoiTtDtlRepository.findByHdrIdIn(listId);
        listBangKe.forEach(e -> {
            List<DcnbBienBanHaoDoiDtl> listDtl = dcnbBienBanHaoDoiDtlRepository.findByHdrId(e.getId());
            dcnbBienBanHaoDoiDtlRepository.deleteAll(listDtl);
        });
        dcnbBienBanHaoDoiTtDtlRepository.deleteAll(listBangKe);
        dcnbBienBanHaoDoiHdrRepository.deleteAll(list);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBienBanHaoDoiHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<DcnbBienBanHaoDoiHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBienBanHaoDoiHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBienBanHaoDoiHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                optional.get().setNgayPduyetKtvBQ(LocalDate.now());
                optional.get().setKtvBaoQuanId(currentUser.getUser().getId());
                optional.get().setKtvBaoQuan(currentUser.getUser().getUsername());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPduyetKt(LocalDate.now());
                optional.get().setKeToanId(currentUser.getUser().getId());
                optional.get().setKeToan(currentUser.getUser().getUsername());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPduyetLdcc(LocalDate.now());
                optional.get().setLanhDaoChiCucId(currentUser.getUser().getId());
                optional.get().setLanhDaoChiCuc(currentUser.getUser().getUsername());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                optional.get().setNgayPduyetKtvBQ(LocalDate.now());
                optional.get().setKtvBaoQuanId(currentUser.getUser().getId());
                optional.get().setKtvBaoQuan(currentUser.getUser().getUsername());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                optional.get().setNgayPduyetKt(LocalDate.now());
                optional.get().setKeToanId(currentUser.getUser().getId());
                optional.get().setKeToan(currentUser.getUser().getUsername());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPduyetLdcc(LocalDate.now());
                optional.get().setLanhDaoChiCucId(currentUser.getUser().getId());
                optional.get().setLanhDaoChiCuc(currentUser.getUser().getUsername());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                        optional.get().getQDinhDccId(),
//                        optional.get().getMaNganKho(),
//                        optional.get().getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBienBanHaoDoiHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBienBanHaoDoiHdr created = dcnbBienBanHaoDoiHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbBienBanHaoDoi objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBienBanHaoDoiHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBienBanHaoDoiHdrDTO> data = page.getContent();

        String title = "Danh sách biên bản hao dôi";
        String[] rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Ngày hiệu lực", "Điểm Kho", "Lô kho", "Số BB hao dôi", "Số BB tịnh kho", "Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số bảng kê", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-hao-doi.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBienBanHaoDoiHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayHieuLuc();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoBienBanHaoDoi();
            objs[7] = dx.getSoBbTinhKho();
            objs[8] = dx.getNgayBatDauXuat(); //"Ngày bắt đầu xuất", "Ngày kết thúc xuất", "Số phiếu XK", "Số bảng kê", "Ngày xuất kho"
            objs[9] = dx.getNgayKetThucXuat();
            objs[10] = dx.getSoPhieuXuatKho();
            objs[11] = dx.getSoBangKeXuatDcLt();
            objs[12] = dx.getNgayXuatKho();
            objs[13] = dx.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbBienBanHaoDoiTtDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbBienBanHaoDoiTtDtl> optional = dcnbBienBanHaoDoiTtDtlRepository.findByHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }

    public ReportTemplateResponse preview(DcnbBienBanHaoDoiHdrReq objReq) throws Exception {
        var dcnbBienBanHaoDoiHdr = dcnbBienBanHaoDoiHdrRepository.findById(objReq.getId());
        if (!dcnbBienBanHaoDoiHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        var dcnbBienBanTinhKhoHdr = dcnbBienBanTinhKhoHdrRepository.findFirstBySoBbTinhKho(dcnbBienBanHaoDoiHdr.get().getSoBbTinhKho());
        if (!dcnbBienBanTinhKhoHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBienBanHaoDoiHdrPreview = setDataToPreview(dcnbBienBanHaoDoiHdr, dcnbBienBanTinhKhoHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBienBanHaoDoiHdrPreview);
    }

    private DcnbBienBanHaoDoiHdrPreview setDataToPreview(Optional<DcnbBienBanHaoDoiHdr> dcnbBienBanHaoDoiHdr,
                                                         Optional<DcnbBienBanTinhKhoHdr> dcnbBienBanTinhKhoHdr) {
        return DcnbBienBanHaoDoiHdrPreview.builder()
                .tenDvi(dcnbBienBanHaoDoiHdr.get().getTenDvi())
                .maQhns(dcnbBienBanHaoDoiHdr.get().getMaQhns())
                .soBienBan(dcnbBienBanHaoDoiHdr.get().getSoBienBan())
                .tenCloaiVthh(dcnbBienBanHaoDoiHdr.get().getTenCloaiVthh())
                .tenLoaiVthh(dcnbBienBanHaoDoiHdr.get().getTenLoaiVthh())
                .tenNganKho(dcnbBienBanHaoDoiHdr.get().getTenNganKho())
                .tenLoKho(dcnbBienBanHaoDoiHdr.get().getTenLoKho())
                .soBbTinhKho(dcnbBienBanHaoDoiHdr.get().getSoBbTinhKho())
                .ngayLap(dcnbBienBanHaoDoiHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .ngayLapBienBanTinhKho(dcnbBienBanTinhKhoHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .chiCuc(dcnbBienBanHaoDoiHdr.get().getTenDvi())
                .lanhDaoChiCuc(dcnbBienBanHaoDoiHdr.get().getLanhDaoChiCuc())
                .keToan(dcnbBienBanHaoDoiHdr.get().getKeToan())
                .ktvBaoQuan(dcnbBienBanHaoDoiHdr.get().getKtvBaoQuan())
                .thuKho(dcnbBienBanHaoDoiHdr.get().getThuKho())
                .tongSlXuatTheoQd(dcnbBienBanHaoDoiHdr.get().getTongSlXuatTheoQd())
                .donViTinh(dcnbBienBanHaoDoiHdr.get().getDonViTinh())
                .tongSlXuatTheoTt(dcnbBienBanHaoDoiHdr.get().getTongSlXuatTheoTt())
                .ngayKetThucXuatQd(dcnbBienBanHaoDoiHdr.get().getNgayKetThucXuatQd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .slHaoTt(dcnbBienBanHaoDoiHdr.get().getSlHaoTt())
                .slHaoDuocThanhLy(dcnbBienBanHaoDoiHdr.get().getSlHaoTheoDinhMuc())
                .slHaoVuotDm(dcnbBienBanHaoDoiHdr.get().getSlHaoVuotDm())
                .tiLeHaoVuotDm(dcnbBienBanHaoDoiHdr.get().getTiLeHaoVuotDm())
                .slHaoDuoiDm(dcnbBienBanHaoDoiHdr.get().getSlHaoTheoDinhMuc()
                        .subtract(BigDecimal.valueOf(dcnbBienBanHaoDoiHdr.get().getSlHaoTt())))
                .tiLeHaoDuoiDm((dcnbBienBanHaoDoiHdr.get().getSlHaoTheoDinhMuc()
                        .divide(BigDecimal.valueOf(dcnbBienBanHaoDoiHdr.get().getTongSlXuatTheoQd())))
                        .multiply(new BigDecimal(100)))
                .nguyenNhan(dcnbBienBanHaoDoiHdr.get().getNguyenNhan())
                .kienNghi(dcnbBienBanHaoDoiHdr.get().getKienNghi())
                .dcnbBienBanHaoDoiDtlDto(dcnbBienBanHaoDoiDtlToDto(dcnbBienBanHaoDoiHdr))
                .build();
    }

    private List<DcnbBienBanHaoDoiDtlDto> dcnbBienBanHaoDoiDtlToDto(Optional<DcnbBienBanHaoDoiHdr> dcnbBienBanHaoDoiHdr) {
        List<DcnbBienBanHaoDoiDtlDto> dcnbBienBanHaoDoiDtlDtos = new ArrayList<>();
        int stt = 1;
        for (var resHaoHut : dcnbBienBanHaoDoiHdr.get().getThongTinHaoHut()) {
            for (var resBangKe : dcnbBienBanHaoDoiHdr.get().getDanhSachBangKe()) {
                var dcnbBienBanHaoDoiDtlDto = DcnbBienBanHaoDoiDtlDto.builder()
                        .stt(stt++)
                        .thoiGianBaoQuan(thoiGianBaoQuan(resHaoHut, resBangKe))
                        .slBaoQuan(resHaoHut.getSlBaoQuan())
                        .dinhMucHaoHut(resHaoHut.getDinhMucHaoHut())
                        .slHao(resHaoHut.getSlHao())
                        .soLuongHaoTheoDinhMuc(BigDecimal.valueOf(resHaoHut.getSlBaoQuan()).multiply(BigDecimal.valueOf(resHaoHut.getDinhMucHaoHut())))
                        .build();
                dcnbBienBanHaoDoiDtlDtos.add(dcnbBienBanHaoDoiDtlDto);
            }
        }
        return dcnbBienBanHaoDoiDtlDtos;
    }

    private String thoiGianBaoQuan(DcnbBienBanHaoDoiDtl thongTinHaoHut, DcnbBienBanHaoDoiTtDtl danhSachBangKe) {
        StringBuilder thoiGianBaoQuan = new StringBuilder();
        int thangKetThucNhap = thongTinHaoHut.getNgayKetThuc().getMonth().getValue();
        int namKetThucNhap = thongTinHaoHut.getNgayKetThuc().getYear();
        int thangKetThucXuat = danhSachBangKe.getNgayXuatKho().getMonth().getValue();
        int namKetThucXuat = danhSachBangKe.getNgayXuatKho().getYear();
        thoiGianBaoQuan.append(thangKetThucNhap).append("/").append(namKetThucNhap).append("-")
                .append(thangKetThucXuat).append("/").append(namKetThucXuat);
        return thoiGianBaoQuan.toString();
    }
}
