package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeXuatVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBangKeXuatVTService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbBangKeXuatVTServiceImpl implements DcnbBangKeXuatVTService {
    @Autowired
    private DcnbBangKeXuatVTHdrRepository hdrRepository;
    @Autowired
    private DcnbBangKeXuatVTDtlRepository dtlRepository;
    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;
    @Autowired
    private DcnbPhieuXuatKhoHdrRepository dcnbPhieuXuatKhoHdrRepository;

    @Override
    public Page<DcnbBangKeXuatVTHdr> searchPage(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    public Page<DcnbBangKeXuatVTHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBangKeXuatVTReq req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBangKeXuatVTHdrDTO> searchDto = null;
        if (req.getIsVatTu() == null) {
            req.setIsVatTu(false);
        }
        if (req.getIsVatTu()) {
            req.setDsLoaiHang(Arrays.asList("VT"));
        } else {
            req.setDsLoaiHang(Arrays.asList("LT", "M"));
        }
        req.setType("00");
        req.setTypeQd(Contains.DIEU_CHUYEN);
        searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public DcnbBangKeXuatVTHdr create(DcnbBangKeXuatVTReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
//        Optional<DcnbBangKeXuatVTHdr> optional = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//            throw new Exception("Số bảng kê đã tồn tại");
//        }
        DcnbBangKeXuatVTHdr data = new DcnbBangKeXuatVTHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(dvql);
        data.setTenDvi(userInfo.getTenDvi());
        objReq.getDcnbBangKeXuatVTDtl().forEach(e -> e.setDcnbBangKeXuatVTHdr(data));
        DcnbBangKeXuatVTHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKXVT-" + userInfo.getDvqlTenVietTat();
        created.setSoBangKe(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbBangKeXuatVTHdr update(DcnbBangKeXuatVTReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<DcnbBangKeXuatVTHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBangKeXuatVTHdr> soDxuat = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
//            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }

        DcnbBangKeXuatVTHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBangKeXuatVTDtl(objReq.getDcnbBangKeXuatVTDtl());
        DcnbBangKeXuatVTHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/BKXVT-" + userInfo.getDvqlTenVietTat();
        created.setSoBangKe(so);
        hdrRepository.save(created);
        return created;
    }

    @Override
    public DcnbBangKeXuatVTHdr detail(Long id) throws Exception {
        if (id == null)
            throw new Exception("Tham số không hợp lệ.");
        Optional<DcnbBangKeXuatVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional.get();
    }

    @Override
    public DcnbBangKeXuatVTHdr approve(DcnbBangKeXuatVTReq req) throws Exception {
        return null;
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBangKeXuatVTHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBangKeXuatVTHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBangKeXuatVTHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBangKeXuatVTHdr> optional) throws Exception {
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
//                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
//                        optional.get().getQDinhDccId(),
//                        optional.get().getMaNganKho(),
//                        optional.get().getMaLoKho());
//                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
//                dataLinkDtl.setLinkId(optional.get().getId());
//                dataLinkDtl.setHdrId(dataLink.getId());
//                dataLinkDtl.setType(DcnbBangKeXuatVTHdr.TABLE_NAME);
//                dcnbDataLinkDtlRepository.save(dataLinkDtl);

                Optional<DcnbPhieuXuatKhoHdr> dcnbPhieuXuatKhoHdr = dcnbPhieuXuatKhoHdrRepository.findById(optional.get().getPhieuXuatKhoId());
                if (dcnbPhieuXuatKhoHdr.isPresent()) {
                    dcnbPhieuXuatKhoHdr.get().setBangKeVtId(optional.get().getId());
                    dcnbPhieuXuatKhoHdr.get().setSoBangKeVt(optional.get().getSoBangKe());
                    dcnbPhieuXuatKhoHdrRepository.save(dcnbPhieuXuatKhoHdr.get());
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBangKeXuatVTHdr created = hdrRepository.save(optional.get());
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbBangKeXuatVTHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBangKeXuatVTHdr data = optional.get();
        List<DcnbBangKeXuatVTDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        List<DcnbBangKeXuatVTHdr> list = hdrRepository.findAllByIdIn(listMulti);

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbBangKeXuatVTHdr::getId).collect(Collectors.toList());
        List<DcnbBangKeXuatVTDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
        dtlRepository.deleteAll(listBangKe);
    }

    @Override
    public void export(DcnbBangKeXuatVTReq objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Page<DcnbBangKeXuatVTHdrDTO> page = searchPage(currentUser, objReq);
        List<DcnbBangKeXuatVTHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê xuất vật tư";
        String[] rowsName = new String[]{"STT", "Số QĐ ĐC của Cục", "Năm KH", "Thời hạn điều chuyển", "Điểm kho", "Lô kho", "Số phiếu XK", "Số bảng kê xuất ĐC VT", "Ngày xuất kho", "Trạng thái"};
        String fileName = "danh-sach-bang-ke-xuat-vat-tu.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBangKeXuatVTHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQdinh();
            objs[2] = dx.getNamKh();
            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieuXuat();
            objs[7] = dx.getSoBangKe();
            objs[8] = dx.getNgayXuatKho();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
