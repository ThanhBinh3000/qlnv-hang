package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.DmVattuBqRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang.DcnbBBNTBQHdrPreview;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBBNTBQHdrService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattuBq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DieuChuyenNoiBo;
import lombok.var;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoHdr;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbBBNTBQHdrServiceImpl extends BaseServiceImpl implements DcnbBBNTBQHdrService {

    @Autowired
    private DcnbBBNTBQHdrRepository hdrRepository;

    @Autowired
    private DcnbBBNTBQDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbBbGiaoNhanHdrRepository dcnbBbGiaoNhanHdrRepository;

    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;
    @Autowired
    public DocxToPdfConverter docxToPdfConverter;
    @Autowired
    private DcnbBbNhapDayKhoHdrRepository dcnbBbNhapDayKhoHdrRepository;

    @Autowired
    private DcnbBienBanHaoDoiHdrRepository dcnbBienBanHaoDoiHdrRepository;
    @Autowired
    private DcnbBienBanTinhKhoHdrRepository dcnbBienBanTinhKhoHdrRepository;
    @Autowired
    private DmVattuBqRepository dmVattuBqRepository;

    @Override
    public Page<DcnbBBNTBQHdr> searchPage(DcnbBBNTBQHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdr> search = hdrRepository.search(req, pageable);
        return search;
    }

    @Override
    public Page<DcnbBBNTBQHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBNTBQHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        searchDto = hdrRepository.searchPage(req, pageable);

        for (DcnbBBNTBQHdrDTO dto : searchDto.getContent()) {
            if (dcnbBbGiaoNhanHdrRepository.findByKeHoachDcDtlId(dto.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                dto.setTrangThaiNhan(Contains.DA_HOAN_THANH);
                dto.setTenTrangThaiNhan(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            } else if (dcnbBbNhapDayKhoHdrRepository.findByKeHoachDcDtlId(dto.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                QlnvDmVattu byMa = qlnvDmVattuRepository.findByMa(dto.getCloaiVthh());
                if ("VT".equals(byMa.getLoaiHang())) { // là vật từ thì đang thực hiện
                    dto.setTrangThaiNhan(Contains.DANG_THUC_HIEN);
                    dto.setTenTrangThaiNhan(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
                } else {
                    dto.setTrangThaiNhan(Contains.DA_HOAN_THANH);
                    dto.setTenTrangThaiNhan(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
                }
            } else if (!dcnbBienBanLayMauHdrRepository.findByKeHoachDcDtlIdAndType(dto.getKeHoachDcDtlId(), "01").isEmpty()) { // có biên bản lấy mẫu
                dto.setTrangThaiNhan(Contains.DANG_THUC_HIEN);
                dto.setTenTrangThaiNhan(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            } else {
                dto.setTrangThaiNhan(Contains.CHUA_THUC_HIEN);
                dto.setTenTrangThaiNhan(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            }

            if (dcnbBienBanHaoDoiHdrRepository.findByKeHoachDcDtlId(dto.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                dto.setTrangThaiXuat(Contains.DA_HOAN_THANH);
                dto.setTenTrangThaiXuat(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            } else if (dcnbBienBanTinhKhoHdrRepository.findByKeHoachDcDtlId(dto.getKeHoachDcDtlId()).isEmpty()) { // có biên bản nhập đầy kho đối với lương thực || có biên bản giao nhận đối với vt
                QlnvDmVattu byMa = qlnvDmVattuRepository.findByMa(dto.getCloaiVthh());
                if ("VT".equals(byMa.getLoaiHang())) { // là vật từ thì đã hoàn thành
                    dto.setTrangThaiXuat(Contains.DA_HOAN_THANH);
                    dto.setTenTrangThaiXuat(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
                } else {
                    dto.setTrangThaiXuat(Contains.DANG_THUC_HIEN);
                    dto.setTenTrangThaiXuat(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
                }
            } else if (!dcnbBienBanLayMauHdrRepository.findByKeHoachDcDtlIdAndType(dto.getKeHoachDcDtlId(), "00").isEmpty()) { // có biên bản lấy mẫu
                dto.setTrangThaiXuat(Contains.DANG_THUC_HIEN);
                dto.setTenTrangThaiXuat(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            } else {
                dto.setTrangThaiXuat(Contains.CHUA_THUC_HIEN);
                dto.setTenTrangThaiXuat(TrangThaiAllEnum.getLabelById(dto.getTrangThaiNhan()));
            }
        }

        return searchDto;
    }

    @Override
    public List<DcnbBBNTBQHdr> searchList(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) {
        req.setTrangThai("17");
        List<DcnbBBNTBQHdr> search = hdrRepository.list(req);
        return search;
    }

    @Override
    public DcnbBBNTBQHdr create(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findBySoBban(req.getSoBban());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBBNTBQHdr data = new DcnbBBNTBQHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.getDcnbBBNTBQDtl().forEach(e -> {
            e.setDcnbBBNTBQHdr(data);
        });
        // check biên bản bảo quản lần mấy (nếu đã bảo biên bản nhập đầy kho thì lần 2, nếu không thì lần 1)
        Long lan = 1l;
        DcnbBbNhapDayKhoHdr dcnbBbNhapDayKhoHdr = null;
        if (req.getMaLoKho() != null) {
            dcnbBbNhapDayKhoHdr = dcnbBbNhapDayKhoHdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndTrangThai(userInfo.getDvql(),
                    req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), Contains.DADUYET_LDCC);
        } else {
            dcnbBbNhapDayKhoHdr = dcnbBbNhapDayKhoHdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndTrangThai(userInfo.getDvql(),
                    req.getQdDcCucId(), req.getMaNganKho(), Contains.DADUYET_LDCC);
        }

        if (dcnbBbNhapDayKhoHdr == null) {
            lan = 1l;
            if (req.getMaLoKho() != null) {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), lan);
                if (bbntbqHdr != null) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 1.");
                }
            } else {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), lan);
                if (bbntbqHdr != null) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 1.");
                }
            }
        } else {
            lan = 2l;
            if (req.getMaLoKho() != null) {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), lan);
                if (bbntbqHdr != null) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 2.");
                }
            } else {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), lan);
                if (bbntbqHdr != null) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 2.");
                }
            }
        }
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBBQLD-" + userInfo.getDvqlTenVietTat();
        created.setSoBban(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        List<DcnbBBNTBQHdr> lists = new ArrayList<>();
        if (created.getMaLoKho() == null) {
            lists = hdrRepository.findByMaDviAndMaNganKhoXuat(data.getMaDvi(), data.getMaNganKhoXuat());
        } else {
            lists = hdrRepository.findByMaDviAndMaNganKhoXuatAndMaLoKhoXuat(data.getMaDvi(), data.getMaNganKhoXuat(), data.getMaLoKhoXuat());
        }
        created.setBbNtBqLdKx(lists.stream().map(DcnbBBNTBQHdr::getSoBban).collect(Collectors.joining()));
        hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr update(DcnbBBNTBQHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setDcnbBBNTBQDtl(req.getDcnbBBNTBQDtl());
        DcnbBBNTBQHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBBQLD-" + userInfo.getDvqlTenVietTat();
        created.setSoBban(so);

        // check biên bản bảo quản lần mấy (nếu đã bảo biên bản nhập đầy kho thì lần 2, nếu không thì lần 1)
        Long lan = 1l;
        DcnbBbNhapDayKhoHdr dcnbBbNhapDayKhoHdr = dcnbBbNhapDayKhoHdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndTrangThai(userInfo.getDvql(),
                req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), Contains.DADUYET_LDCC);
        if (dcnbBbNhapDayKhoHdr == null) {
            lan = 1l;
            if (req.getMaLoKho() != null) {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), lan);
                if (bbntbqHdr != null && !Objects.equals(bbntbqHdr.getId(), req.getId())) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 1.");
                }
            } else {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), lan);
                if (bbntbqHdr != null && !Objects.equals(bbntbqHdr.getId(), req.getId())) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 1.");
                }
            }
        } else {
            lan = 2l;
            if (req.getMaLoKho() != null) {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndMaLoKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), req.getMaLoKho(), lan);
                if (bbntbqHdr != null && !Objects.equals(bbntbqHdr.getId(), req.getId())) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 2.");
                }
            } else {
                DcnbBBNTBQHdr bbntbqHdr = hdrRepository.findByMaDviAndQdDcCucIdAndMaNganKhoAndLan(userInfo.getDvql(),
                        req.getQdDcCucId(), req.getMaNganKho(), lan);
                if (bbntbqHdr != null && !Objects.equals(bbntbqHdr.getId(), req.getId())) {
                    throw new Exception("Đã tạo biên bản nghiệm thu bảo quản lần 2.");
                }
            }
        }


        hdrRepository.save(created);
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(DcnbBBNTBQHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        List<DcnbBBNTBQHdr> lists = new ArrayList<>();
        if (created.getMaLoKho() == null) {
            lists = hdrRepository.findByMaDviAndMaNganKhoXuat(data.getMaDvi(), data.getMaNganKhoXuat());
        } else {
            lists = hdrRepository.findByMaDviAndMaNganKhoXuatAndMaLoKhoXuat(data.getMaDvi(), data.getMaNganKhoXuat(), data.getMaLoKhoXuat());
        }
        created.setBbNtBqLdKx(lists.stream().map(DcnbBBNTBQHdr::getSoBban).collect(Collectors.joining()));
        hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbBBNTBQHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBBNTBQHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBNTBQHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBBNTBQHdr approve(DcnbBBNTBQHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBBNTBQHdr approve(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBBNTBQHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
            case Contains.TUCHOI_KT + Contains.CHODUYET_TK:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TK:
            case Contains.DUTHAO + Contains.CHODUYET_TK:
                hdr.setNguoiGduyetId(userInfo.getId());
                hdr.setNgayGduyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TK + Contains.CHODUYET_KT:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setThuKho(userInfo.getFullName());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setKeToan(userInfo.getFullName());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setNgayPduyet(LocalDate.now());
                hdr.setNguoiPduyetId(userInfo.getId());
                hdr.setLdChiCuc(userInfo.getFullName());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(hdr.getMaDvi(),
//                        hdr.getQdDcCucId(),
//                        hdr.getMaNganKhoXuat(),
//                        hdr.getMaLoKhoXuat());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(hdr.getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBBNTBQHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setNgayPduyet(LocalDate.now());
                hdr.setNguoiPduyetId(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        hdr.setTenTrangThai(TrangThaiAllEnum.getLabelById(req.getTrangThai()));
        DcnbBBNTBQHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBBNTBQHdr detail = detail(id);
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
    public void export(DcnbBBNTBQHdrReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBBNTBQHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBBNTBQHdrDTO> data = page.getContent();

        String title = "Danh sách biên bản nghiệm thu bảo quản lần đầu";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chuyển", "Năm KH", "Thời hạn ĐC", "Lô kho xuất ĐC", "Trạng thái xuất ĐC", "Điểm kho nhập ĐC", "Lô kho nhập ĐC", "Trạng thái nhập ĐC", "Số BB NT kê lót BQLĐ", "Ngày lập biên bản", "Ngày kết thúc NT kê lót BQLĐ", "Tổng kinh phí thực tế (đ)", "Tổng kinh phí TC PD (đ)", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-nghiem-thu-bao-quan-lan-dau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBBNTBQHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenLoKhoXuat();
            objs[5] = dx.getTrangThaiXuat();
            objs[6] = dx.getTenDiemKhoNhan();
            objs[7] = dx.getTenLoKhoNhan();
            objs[8] = dx.getTenTrangThaiNhan();
            objs[9] = dx.getSoBBKLot();
            objs[10] = dx.getNgayLapBBKLot();
            objs[11] = dx.getNgayKetThucNtKeLot();
            objs[12] = dx.getTongKinhPhiTT();
            objs[13] = dx.getTongKinhPhiPd();
            objs[14] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(DcnbBBNTBQHdrReq objReq) throws Exception {
        var dcnbBBNTBQHdr = hdrRepository.findById(objReq.getId());
        if (!dcnbBBNTBQHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        var dcnbBBNTBQHdrPreview = setDataToPreview(dcnbBBNTBQHdr);
        return docxToPdfConverter.convertDocxToPdf(inputStream, dcnbBBNTBQHdrPreview);
    }

    private DcnbBBNTBQHdrPreview setDataToPreview(Optional<DcnbBBNTBQHdr> dcnbBBNTBQHdr) {
        var dcnbBBNTBQDtlPheDuyetDtos = dcnbBBNTBQDtlPheDuyetDto(dcnbBBNTBQHdr.get().getDcnbBBNTBQDtl());
        var dcnbBBNTBQDtlThucHienDtos = dcnbBBNTBQDtlThucHienDto(dcnbBBNTBQHdr.get().getDcnbBBNTBQDtl());
        return DcnbBBNTBQHdrPreview.builder()
                .maDvi(dcnbBBNTBQHdr.get().getMaDvi())
                .tenDvi(dcnbBBNTBQHdr.get().getTenDvi())
                .maQhns(dcnbBBNTBQHdr.get().getMaQhns())
                .soBban(dcnbBBNTBQHdr.get().getSoBban())
                .ngayLap(dcnbBBNTBQHdr.get().getNgayLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .tenChiCuc(dcnbBBNTBQHdr.get().getTenDvi())
                .ldChiCuc(dcnbBBNTBQHdr.get().getLdChiCuc())
                .keToan(dcnbBBNTBQHdr.get().getKeToan())
                .ktvBaoQuan(dcnbBBNTBQHdr.get().getKthuatVien())
                .thuKho(dcnbBBNTBQHdr.get().getThuKho())
                .chungLoaiHangHoa(dcnbBBNTBQHdr.get().getTenCloaiVthh())
                .tenNganKho(dcnbBBNTBQHdr.get().getTenNganKho())
                .tenLoKho(dcnbBBNTBQHdr.get().getTenLoKho())
                .loaiHinhKho(dcnbBBNTBQHdr.get().getLoaiHinhKho())
                .tichLuongKhaDung(dcnbBBNTBQHdr.get().getTichLuongKhaDung())
                .slThucNhapDc(dcnbBBNTBQHdr.get().getSlThucNhapDc())
                .phuongThucBaoQuan(phuongThucBaoQuanList(dcnbBBNTBQHdr.get().getPhuongThucBaoQuan()))
                .hinhThucKeLot(hinhThucBaoQuanKlot(dcnbBBNTBQHdr.get().getHinhThucBaoQuan()))
                .hinhThucBaoQuan(phuongThucBaoQuanList(dcnbBBNTBQHdr.get().getHinhThucBaoQuan()))
                .dinhMucDuocGiao(dcnbBBNTBQHdr.get().getDinhMucDuocGiao())
                .dinhMucTT(dcnbBBNTBQHdr.get().getDinhMucTT())
                .tongKinhPhiDaTh(dcnbBBNTBQHdr.get().getTongKinhPhiDaTh())
                .tongKinhPhiDaThBc(dcnbBBNTBQHdr.get().getTongKinhPhiDaThBc())
                .nhanXet(dcnbBBNTBQHdr.get().getNhanXet())
                .dcnbBBNTBQDtlPheDuyetDto(dcnbBBNTBQDtlPheDuyetDtos)
                .dcnbBBNTBQDtlThucHienDto(dcnbBBNTBQDtlThucHienDtos)
                .build();
    }

    private String hinhThucBaoQuanKlot (String maHinhThucBaoQuan) {
        var qlnvDmVattuBq = dmVattuBqRepository.findAllByMaAndType(maHinhThucBaoQuan, "htbq");
        return qlnvDmVattuBq.stream().findFirst().map(QlnvDmVattuBq::getGiaTri).get();
    }
    private String phuongThucBaoQuanList (String maPhuongThucBaoQuan) {
        var qlnvDmVattuBq = dmVattuBqRepository.findAllByMaAndType(maPhuongThucBaoQuan, "ppbq");
        return qlnvDmVattuBq.stream().findFirst().map(QlnvDmVattuBq::getGiaTri).get();
    }


    private List<DcnbBBNTBQDtlPheDuyetDto> dcnbBBNTBQDtlPheDuyetDto(List<DcnbBBNTBQDtl> dcnbBBNTBQDtl) {
        List<DcnbBBNTBQDtlPheDuyetDto> dcnbBBNTBQDtlPheDuyetDtos = new ArrayList<>();
        int stt = 1;
        for (var res : dcnbBBNTBQDtl) {
            if (res.getType().equals("PD")) {
                var dcnbBBNTBQDtlPheDuyetDto = DcnbBBNTBQDtlPheDuyetDto.builder()
                        .stt(stt++)
                        .noiDung(res.getDanhMuc())
                        .dviTinh(res.getDonViTinh())
                        .soLuongTrongNam(BigDecimal.valueOf(res.getSoLuongTrongNam()))
                        .donGiaTrongNam(BigDecimal.valueOf(res.getDonGia()))
                        .thanhTienTrongNam(BigDecimal.valueOf(res.getThanhTienTrongNam()))
                        .soLuongNamTruoc(res.getSoLuongNamTruoc())
                        .thanhTienNamTruoc(BigDecimal.valueOf(res.getThanhTienNamTruoc() == null ? 0.0 : res.getThanhTienNamTruoc()))
                        .tongGiaTri(BigDecimal.valueOf(res.getTongGiaTri()))
                        .build();
                dcnbBBNTBQDtlPheDuyetDtos.add(dcnbBBNTBQDtlPheDuyetDto);
            }
        }
        return dcnbBBNTBQDtlPheDuyetDtos;
    }

    private List<DcnbBBNTBQDtlThucHienDto> dcnbBBNTBQDtlThucHienDto(List<DcnbBBNTBQDtl> dcnbBBNTBQDtl) {
        List<DcnbBBNTBQDtlThucHienDto> DcnbBBNTBQDtlThucHienDtos = new ArrayList<>();
        int stt = 1;
        for (var res : dcnbBBNTBQDtl) {
            if (res.getType().equals("TH")) {
                var dcnbBBNTBQDtlThucHienDto = DcnbBBNTBQDtlThucHienDto.builder()
                        .stt(stt++)
                        .noiDung(res.getDanhMuc())
                        .dviTinh(res.getDonViTinh())
                        .soLuongTrongNam(BigDecimal.valueOf(res.getSoLuongTrongNam()))
                        .donGiaTrongNam(BigDecimal.valueOf(res.getDonGia()))
                        .thanhTienTrongNam(BigDecimal.valueOf(res.getThanhTienTrongNam()))
                        .soLuongNamTruoc(res.getSoLuongNamTruoc())
                        .thanhTienNamTruoc(BigDecimal.valueOf(res.getThanhTienNamTruoc() == null ? 0.0 : res.getThanhTienNamTruoc()))
                        .tongGiaTri(BigDecimal.valueOf(res.getTongGiaTri()))
                        .build();
                DcnbBBNTBQDtlThucHienDtos.add(dcnbBBNTBQDtlThucHienDto);
            }
        }
        return DcnbBBNTBQDtlThucHienDtos;
    }
}
