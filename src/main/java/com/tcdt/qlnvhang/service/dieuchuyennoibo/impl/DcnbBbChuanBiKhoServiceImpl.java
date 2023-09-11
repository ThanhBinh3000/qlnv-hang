package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbChuanBiKhoDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbChuanBiKhoHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbChuanBiKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbChuanBiKhoService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkHdr;
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
public class DcnbBbChuanBiKhoServiceImpl implements DcnbBbChuanBiKhoService {

    @Autowired
    private DcnbBbChuanBiKhoHdrRepository hdrRepository;
    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;

    @Autowired
    private DcnbBbChuanBiKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<DcnbBbChuanBiKhoHdr> searchPage(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    public Page<DcnbBbChuanBiKhoHdrDTO> search(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBbChuanBiKhoHdrDTO> searchDto = null;
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

    public List<DcnbBbChuanBiKhoHdr> list(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        List<DcnbBbChuanBiKhoHdr> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.NHAN_DIEU_CHUYEN);
        searchDto = hdrRepository.searchList(req);
        return searchDto;
    }

    @Override
    public DcnbBbChuanBiKhoHdr create(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<DcnbBbChuanBiKhoHdr> optional = hdrRepository.findBySoBban(req.getSoBban());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        DcnbBbChuanBiKhoHdr data = new DcnbBbChuanBiKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(null);
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        DcnbBbChuanBiKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BBCBK-" + userInfo.getDvqlTenVietTat();
        created.setSoBban(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBbChuanBiKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBbChuanBiKhoHdr update(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbChuanBiKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbChuanBiKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        DcnbBbChuanBiKhoHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/BBCBK-" + userInfo.getDvqlTenVietTat();
        update.setSoBban(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBbChuanBiKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbBbChuanBiKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public DcnbBbChuanBiKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<DcnbBbChuanBiKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbChuanBiKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBbChuanBiKhoHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBbChuanBiKhoHdr approve(DcnbBbChuanBiKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbChuanBiKhoHdr approve(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        DcnbBbChuanBiKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_TK:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TK:
            case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
                hdr.setIdThuKho(userInfo.getId());
                hdr.setTenThuKho(userInfo.getFullName());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setNguoiPDuyetTk(userInfo.getId());
                hdr.setNgayPDuyetTk(LocalDate.now());
                break;
            case Contains.CHODUYET_TK + Contains.CHODUYET_LDCC:
                hdr.setIdThuKho(userInfo.getId());
                hdr.setTenThuKho(userInfo.getFullName());
                hdr.setNguoiPDuyetTk(userInfo.getId());
                hdr.setNgayPDuyetTk(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                hdr.setTenLanhDao(userInfo.getFullName());
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCucNhan(hdr.getMaDvi(),
//                        hdr.getQdDcCucId(),
//                        hdr.getMaNganKho(),
//                        hdr.getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(hdr.getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBbChuanBiKhoHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBbChuanBiKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBbChuanBiKhoHdr detail = detail(id);
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
    public void export(DcnbBbChuanBiKhoHdrReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBbChuanBiKhoHdrDTO> page = search(objReq);
        List<DcnbBbChuanBiKhoHdrDTO> data = page.getContent();

        String title = "Danh sách chuẩn bị kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ", "Năm KH", "Điểm kho", "Thời gian nhập kho muộn nhất", "Lô kho", "Số BB chuẩn bị kho", "Ngày lập biên bản", "Phiếu nhập kho", "BB kết thúc nhập kho", "BB giao nhận", "Ngày kết thúc nhập kho", "Trạng thái"};
        String fileName = "danh-sach-chuan-bi-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBbChuanBiKhoHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getTenDiemKho();
            objs[4] = dx.getThoiGianNhapKhoMuonNhat();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoBbChuanBiKho();
            objs[7] = dx.getNgayBbChuanBiKho();
            objs[8] = dx.getSoPhieuNhapKho();
            objs[9] = dx.getSoBbKetThucNK();
            objs[10] = dx.getSoBbGiaoNhan();
            objs[11] = dx.getNgayKtNhapKho();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}