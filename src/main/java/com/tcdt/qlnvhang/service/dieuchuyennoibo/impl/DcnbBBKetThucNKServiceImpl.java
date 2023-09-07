package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBKetThucNKDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBBKetThucNKHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBKetThucNKReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrListDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBBKetThucNKService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
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
public class DcnbBBKetThucNKServiceImpl implements DcnbBBKetThucNKService {
    @Autowired
    private DcnbBBKetThucNKHdrRepository hdrRepository;
    @Autowired
    private DcnbBBKetThucNKDtlRepository dtlRepository;
    @Autowired
    private DcnbPhieuNhapKhoHdrRepository dcnbPhieuNhapKhoHdrRepository;

    @Override
    public Page<DcnbBBKetThucNKHdr> searchPage(DcnbBBKetThucNKReq req) throws Exception {
        return null;
    }

    public Page<DcnbBBKetThucNKHdrDTO> search(CustomUserDetails currentUser, DcnbBBKetThucNKReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBBKetThucNKHdrDTO> searchDto = null;
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
    public List<DcnbBBKetThucNKHdrListDTO> searchList(CustomUserDetails currentUser, DcnbBBKetThucNKReq req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        return hdrRepository.searchList(req);
    }

    @Override
    public DcnbBBKetThucNKHdr create(DcnbBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbBBKetThucNKHdr> optional = hdrRepository.findFirstBySoBb(req.getSoBb());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBBKetThucNKHdr data = new DcnbBBKetThucNKHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getDcnbBBKetThucNKDtl().forEach(e -> {
            e.setDcnbBBKetThucNKHdr(data);
        });
        DcnbBBKetThucNKHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBKT-" + userInfo.getDvqlTenVietTat();
        created.setSoBb(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbBBKetThucNKHdr update(DcnbBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBBKetThucNKHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBKetThucNKHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setDcnbBBKetThucNKDtl(req.getDcnbBBKetThucNKDtl());
        DcnbBBKetThucNKHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBKT-" + userInfo.getDvqlTenVietTat();
        update.setSoBb(so);
        hdrRepository.save(update);
        return update;
    }

    @Override
    public DcnbBBKetThucNKHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBBKetThucNKHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBBKetThucNKHdr data = optional.get();
        return data;
    }

    @Override
    public DcnbBBKetThucNKHdr approve(DcnbBBKetThucNKReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBBKetThucNKHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
                hdr.setNguoiPDuyetTvqt(userInfo.getId());
                hdr.setNgayPDuyetTvqt(LocalDate.now());
                break;
            case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
                hdr.setNguoiPDuyetTvqt(userInfo.getId());
                hdr.setNgayPDuyetTvqt(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
                hdr.setNguoiPDuyetKt(userInfo.getId());
                hdr.setNgayPDuyetKt(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
                hdr.setNguoiPDuyetKt(userInfo.getId());
                hdr.setNgayPDuyetKt(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                List<DcnbBBKetThucNKDtl> bbKetThucNKDtl = dtlRepository.findByHdrId(hdr.getId());
                for (DcnbBBKetThucNKDtl kt : bbKetThucNKDtl) {
                    Optional<DcnbPhieuNhapKhoHdr> dcnbPhieuNhapKhoHdr = dcnbPhieuNhapKhoHdrRepository.findById(kt.getPhieuNhapKhoId());
                    if (dcnbPhieuNhapKhoHdr.isPresent()) {
                        dcnbPhieuNhapKhoHdr.get().setSoBbKetThucNk(hdr.getSoBb());
                        dcnbPhieuNhapKhoHdr.get().setBbKetThucNkId(hdr.getId());
                        dcnbPhieuNhapKhoHdrRepository.save(dcnbPhieuNhapKhoHdr.get());
                    }
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBBKetThucNKHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBBKetThucNKHdr detail = detail(id);
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
    public void export(DcnbBBKetThucNKReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBBKetThucNKHdrDTO> page = search(currentUser, objReq);
        List<DcnbBBKetThucNKHdrDTO> data = page.getContent();

        String title = "Danh sách biên bản kết thúc nhập kho";
        String[] rowsName = new String[]{"STT", "Số QĐ điều chuyển", "Năm KH", "Thời hạn ĐC", "Điểm kho", "Lô kho", "Số BB kết thúc NH", "Ngày kết thúc NK", "Số HSKT", "Số phiếu nhập kho", "Ngày nhập kho", "Số BB lấy mẫu/BG mẫu", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-ket-thuc-nhap-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBBKetThucNKHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoBBKtNH();
            objs[7] = dx.getNgayKetThucNhapHang();
            objs[8] = dx.getSoHoSoKyThuat();
            objs[9] = dx.getSoPhieuNhapKho();
            objs[10] = dx.getNgayNhapKho();
            objs[11] = dx.getSoBbLayMau();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
