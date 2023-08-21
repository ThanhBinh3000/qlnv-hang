package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuXuatKhoDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuXuatKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuXuatKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class DcnbPhieuXuatKhoServiceImpl extends BaseServiceImpl {

    @Autowired
    private DcnbPhieuXuatKhoHdrRepository hdrRepository;

    @Autowired
    private DcnbPhieuXuatKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;

    public Page<DcnbPhieuXuatKhoHdrDTO> searchPage(CustomUserDetails currentUser, SearchPhieuXuatKho req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbPhieuXuatKhoHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setTypeQd(Contains.DIEU_CHUYEN);
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Transactional
    public DcnbPhieuXuatKhoHdr save(CustomUserDetails currentUser, DcnbPhieuXuatKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<DcnbPhieuXuatKhoHdr> soDxuat = hdrRepository.findBySoPhieuXuatKho(objReq.getSoPhieuXuatKho());
        if (soDxuat.isPresent()) {
            throw new Exception("Số đề xuất đã tồn tại");
        }
        DcnbPhieuXuatKhoHdr data = new DcnbPhieuXuatKhoHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        if (objReq.getDcnbPhieuXuatKhoDtl() != null) {
            objReq.getDcnbPhieuXuatKhoDtl().forEach(e -> {
                e.setDcnbPhieuXuatKhoHdr(data);
            });
        }
        DcnbPhieuXuatKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PXK-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieuXuatKho(so);
        hdrRepository.save(created);
        saveFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbPhieuXuatKhoHdr.TABLE_NAME);
        return created;
    }

    @Transactional
    public DcnbPhieuXuatKhoHdr update(CustomUserDetails currentUser, DcnbPhieuXuatKhoHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbPhieuXuatKhoHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }

        DcnbPhieuXuatKhoHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbPhieuXuatKhoDtl(objReq.getDcnbPhieuXuatKhoDtl());
        DcnbPhieuXuatKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PXK-" + currentUser.getUser().getDvqlTenVietTat();
        created.setSoPhieuXuatKho(so);
        hdrRepository.save(created);
        saveFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbPhieuXuatKhoHdr.TABLE_NAME);
        return created;
    }

    public DcnbPhieuXuatKhoHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<DcnbPhieuXuatKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuXuatKhoHdr data = optional.get();
        data.setDcnbPhieuXuatKhoDtl(dtlRepository.findAllByHdrId(id));
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singletonList(DcnbPhieuXuatKhoHdr.TABLE_NAME)));
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbPhieuXuatKhoHdr> optional = hdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuXuatKhoHdr data = optional.get();
        List<DcnbPhieuXuatKhoDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<DcnbPhieuXuatKhoHdr> list = hdrRepository.findAllByIdIn(idSearchReq.getIdList());
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(DcnbPhieuXuatKhoHdr::getId).collect(Collectors.toList());
//        List<DcnbBangKeCanHangDtl> listBangKe = dcnbBangKeCanHangDtlRepository.findByHdrIdIn(listId);
//        dcnbBangKeCanHangDtlRepository.deleteAll(listBangKe);
//    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuXuatKhoHdr details = detail(statusReq.getId());
        Optional<DcnbPhieuXuatKhoHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbPhieuXuatKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbPhieuXuatKhoHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                        optional.get().getQddcId(),
//                        optional.get().getMaNganKho(),
//                        optional.get().getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbPhieuXuatKhoHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbPhieuXuatKhoHdr created = hdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchPhieuXuatKho objReq, HttpServletResponse response) throws Exception {
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        objReq.setPaggingReq(paggingReq);
//        Page<DcnbPhieuXuatKhoHdr> page = this.searchPage(currentUser, objReq);
//        List<DcnbPhieuXuatKhoHdr> data = page.getContent();
//
//        String title = "Danh sách bảng kê cân hàng ";
//        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
//        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//        for (int i = 0; i < data.size(); i++) {
//            DcnbPhieuXuatKhoHdr dx = data.get(i);
//            objs = new Object[rowsName.length];
//            objs[0] = i + 1;
//            objs[1] = dx.getNam();
//            objs[2] = dx.getSoQdinhDcc();
//            objs[3] = dx.getNam();
//            objs[4] = dx.getThoiHanDieuChuyen();
//            objs[5] = dx.getTenDiemKho();
//            objs[6] = dx.getTenLoKho();
//            objs[7] = dx.getSoPhieuXuatKho();
//            objs[8] = dx.getSoBangKe();
//            objs[9] = dx.getNgayXuatKho();
//            objs[10] = dx.getTrangThai();
//            dataList.add(objs);
//        }
//        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//        ex.export();
    }

    public List<DcnbPhieuXuatKhoHdrListDTO> searchList(CustomUserDetails currentUser, SearchPhieuXuatKho req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        List<DcnbPhieuXuatKhoHdrListDTO> searchDto = null;
        req.setTypeQd(Contains.DIEU_CHUYEN);
        if(req.getIsVatTu() == null){
            req.setIsVatTu(false);
        }
        if(req.getIsVatTu()){
            req.setDsLoaiHang(Arrays.asList("VT"));
        }else {
            req.setDsLoaiHang(Arrays.asList("LT","M"));
        }
        searchDto = hdrRepository.searchList(req);
        return searchDto;
    }

    public List<DcnbPhieuXuatKhoHdrListDTO> searchListChung(CustomUserDetails currentUser, SearchPhieuXuatKho req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        List<DcnbPhieuXuatKhoHdrListDTO> searchDto = null;
        req.setTypeQd(Contains.DIEU_CHUYEN);
        if(req.getIsVatTu() == null){
            req.setIsVatTu(false);
        }
        if(req.getIsVatTu()){
            req.setDsLoaiHang(Arrays.asList("VT"));
        }else {
            req.setDsLoaiHang(Arrays.asList("LT","M"));
        }
        searchDto = hdrRepository.searchListChung(req);
        return searchDto;
    }
}
