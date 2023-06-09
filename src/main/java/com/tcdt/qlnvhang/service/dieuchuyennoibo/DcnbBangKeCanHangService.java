package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeCanHangDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBangKeCanHangHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
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
public class DcnbBangKeCanHangService extends BaseServiceImpl {

    @Autowired
    private DcnbBangKeCanHangHdrRepository dcnbBangKeCanHangHdrRepository;

    @Autowired
    private DcnbBangKeCanHangDtlRepository dcnbBangKeCanHangDtlRepository;

    @Autowired
    private DcnbQuyetDinhDcCHdrService dcnbQuyetDinhDcCHdrService;

    @Autowired
    private DcnbKeHoachNhapXuatService dcnbKeHoachNhapXuatService;


    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, SearchBangKeCanHang req) throws Exception {

        // Get Tree quyết định
        SearchDcnbQuyetDinhDcC reqQd = new SearchDcnbQuyetDinhDcC();
        reqQd.setPaggingReq(req.getPaggingReq());
        reqQd.setNam(req.getNam());
        reqQd.setSoQdinh(req.getSoQdinhDcc());
        reqQd.setLoaiDc(req.getLoaiDc());
        reqQd.setTrangThai(NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
        Page<DcnbQuyetDinhDcCHdr> dcnbQuyetDinhDcCHdrs = dcnbQuyetDinhDcCHdrService.searchPage(currentUser, reqQd);

        // Gắn data vào biên bản lấy mẫu vào tree
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        dcnbQuyetDinhDcCHdrs.forEach( hdr -> {
            hdr.getDanhSachQuyetDinh().forEach( dtl -> {
                DcnbKeHoachDcHdr keHoachHdr = dtl.getDcnbKeHoachDcHdr();
                keHoachHdr.getDanhSachHangHoa().forEach( keHoachDtl -> {
                    try {
                        DcnbKeHoachDcDtlTT keHoachNhapXuat = dcnbKeHoachNhapXuatService.detailKhDtl(keHoachDtl.getId());
                        keHoachDtl.setDcnbKeHoachDcDtlTT(keHoachNhapXuat);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        });

        return dcnbQuyetDinhDcCHdrs;
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
        Page<DcnbBangKeCanHangHdr> page = dcnbBangKeCanHangHdrRepository.search(objReq,pageable);
        List<DcnbBangKeCanHangHdr> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBangKeCanHangHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdinhDcc();
            objs[3] = dx.getNam();
            objs[4] = dx.getThoiHanDieuChuyen();
            objs[5] = dx.getTenDiemKho();
            objs[6] = dx.getTenLoKho();
            objs[7] = dx.getSoPhieuXuatKho();
            objs[8] = dx.getSoBangKe();
            objs[9] = dx.getNgayXuatKho();
            objs[10] = dx.getTrangThai();
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
