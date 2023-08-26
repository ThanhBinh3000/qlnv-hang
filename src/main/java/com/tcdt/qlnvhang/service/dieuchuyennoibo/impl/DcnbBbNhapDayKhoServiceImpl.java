package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBNTBQHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbNhapDayKhoDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbNhapDayKhoHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbNhapDayKhoService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
public class DcnbBbNhapDayKhoServiceImpl implements DcnbBbNhapDayKhoService {

    @Autowired
    private DcnbBbNhapDayKhoHdrRepository hdrRepository;

    @Autowired
    private DcnbBbNhapDayKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbBBNTBQHdrRepository dcnbBBNTBQHdrRepository;

    @Override
    public Page<DcnbBbNhapDayKhoHdr> searchPage(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        return null;
    }

    public Page<DcnbBbNhapDayKhoHdrDTO> search(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBbNhapDayKhoHdrDTO> searchDto = null;
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

        return searchDto;
    }

    @Override
    public DcnbBbNhapDayKhoHdr create(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findBySoBb(req.getSoBb());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBbNhapDayKhoHdr data = new DcnbBbNhapDayKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        DcnbBbNhapDayKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBNDK-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBbNhapDayKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
//        DcnbKeHoachDcDtlTT kh = new DcnbKeHoachDcDtlTT();
//        kh.setIdHdr(created.getId());
//        kh.setTableName(DcnbBbNhapDayKhoHdr.TABLE_NAME);
//        kh.setIdKhDcDtl(data.getIdKeHoachDtl());
//        dcnbKeHoachNhapXuatService.saveOrUpdate(kh);
        return created;
    }

    @Override
    public DcnbBbNhapDayKhoHdr update(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbNhapDayKhoHdr data = optional.get();
        req.setMaDvi(userInfo.getDvql());
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        DcnbBbNhapDayKhoHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBNDK-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBbNhapDayKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbBbNhapDayKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public DcnbBbNhapDayKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBbNhapDayKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbNhapDayKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBbNhapDayKhoHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBbNhapDayKhoHdr approve(DcnbBbNhapDayKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBbNhapDayKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các cấp duuyệt
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                hdr.setNgayPDuyetKtvbq(LocalDate.now());
                hdr.setNguoiPDuyeKtvbq(userInfo.getId());
                hdr.setIdKyThuatVien(userInfo.getId());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setIdKeToan(userInfo.getId());
                hdr.setTenKeToan(userInfo.getFullName());
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setIdKeToan(userInfo.getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setIdLanhDao(userInfo.getId());

                List<DcnbBBNTBQHdr> bbntbqHdrList = new ArrayList<>();
                if (hdr.getMaLoKho() == null) {
                    bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKho(hdr.getQdDcCucId(), hdr.getMaNganKho());
                } else {
                    bbntbqHdrList = dcnbBBNTBQHdrRepository.findByQdDcCucIdAndMaNganKhoAndMaLoKho(hdr.getQdDcCucId(), hdr.getMaNganKho(), hdr.getMaLoKho());
                }

                // lưu biên bản nghiệp thu bảo quản lần đầu
                for (DcnbBBNTBQHdr hdrbq : bbntbqHdrList) {
                    hdrbq.setBbNhapDayKhoId(hdr.getId());
                    hdrbq.setSoBbNhapDayKho(hdr.getSoBb());
                    dcnbBBNTBQHdrRepository.save(hdrbq);
                }

                break;
            // Arena từ chối
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setNguoiPDuyeKt(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBbNhapDayKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBbNhapDayKhoHdr detail = detail(id);
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
    public void export(DcnbBbNhapDayKhoHdrReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<DcnbBbNhapDayKhoHdr> searchList(CustomUserDetails currentUser, DcnbBbNhapDayKhoHdrReq param) {
        param.setMaDvi(currentUser.getDvql());
        return hdrRepository.searchList(param);
    }
}