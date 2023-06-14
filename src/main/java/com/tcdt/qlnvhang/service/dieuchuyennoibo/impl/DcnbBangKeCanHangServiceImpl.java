package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DcnbBangKeCanHangServiceImpl extends BaseServiceImpl{

    @Autowired
    private DcnbBangKeCanHangHdrRepository dcnbBangKeCanHangHdrRepository;

    @Autowired
    private DcnbBangKeCanHangDtlRepository dcnbBangKeCanHangDtlRepository;

    @Autowired
    private DcnbQuyetDinhDcCHdrServiceImpl dcnbQuyetDinhDcCHdrServiceImpl;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;


    public Page<DcnbBangKeCanHangHdrDTO> searchPage(CustomUserDetails currentUser, SearchBangKeCanHang req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        Page<DcnbBangKeCanHangHdrDTO> searchDto = null;
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            searchDto = dcnbBangKeCanHangHdrRepository.searchPage(req, pageable);
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            if("00".equals(req.getType())){
                req.setTypeDataLink(Contains.DIEU_CHUYEN);
            }else if("01".equals(req.getType())){
                req.setTypeDataLink(Contains.NHAN_DIEU_CHUYEN);
            }
            searchDto = dcnbBangKeCanHangHdrRepository.searchPageCuc(req, pageable);
        }
        return searchDto;
    }

    @Transactional
    public DcnbBangKeCanHangHdr save(CustomUserDetails currentUser, DcnbBangKeCanHangHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBangKeCanHangHdr> optional = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
            throw new Exception("Số bảng kê đã tồn tại");
        }
        DcnbBangKeCanHangHdr data = new DcnbBangKeCanHangHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        objReq.getDcnbBangKeCanHangDtl().forEach(e->e.setDcnbBangKeCanHangHdr(data));
        DcnbBangKeCanHangHdr created = dcnbBangKeCanHangHdrRepository.save(data);
        return created;
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
        Optional<DcnbBangKeCanHangHdr> soDxuat = dcnbBangKeCanHangHdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số bảng kê đã tồn tại");
                }
            }
        }

        DcnbBangKeCanHangHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBangKeCanHangDtl(objReq.getDcnbBangKeCanHangDtl());
        if (objReq.getDcnbBangKeCanHangDtl() != null) {
        }
        DcnbBangKeCanHangHdr created = dcnbBangKeCanHangHdrRepository.save(data);
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
                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
                        optional.get().getQDinhDccId(),
                        optional.get().getMaNganKho(),
                        optional.get().getMaLoKho());
                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
                dataLinkDtl.setLinkId(optional.get().getId());
                dataLinkDtl.setHdrId(dataLink.getId());
                if ("00".equals(optional.get().getType())) { // xuất
                    dataLinkDtl.setType("XDC" + DcnbBangKeCanHangHdr.TABLE_NAME);
                } else if ("01".equals(optional.get().getType())) {
                    dataLinkDtl.setType("NDC" + DcnbBangKeCanHangHdr.TABLE_NAME);
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
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
        Page<DcnbBangKeCanHangHdrDTO> page = dcnbBangKeCanHangHdrRepository.searchPage(objReq,pageable);
        List<DcnbBangKeCanHangHdrDTO> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
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
            objs[6] = dx.getSoPhieuXuatKho();
            objs[7] = dx.getSoBangKeXuatDcLt();
            objs[8] = dx.getNgayXuatKho();
            objs[9] = dx.getTrangThai();
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
}
