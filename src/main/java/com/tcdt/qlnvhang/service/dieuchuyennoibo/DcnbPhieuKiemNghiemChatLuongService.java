package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKnChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKnChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuKnChatLuongHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcC;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKnChatLuong;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DcnbPhieuKiemNghiemChatLuongService extends BaseServiceImpl {

    @Autowired
    DcnbPhieuKnChatLuongHdrRepository dcnbPhieuKnChatLuongHdrRepository;

    @Autowired
    DcnbPhieuKnChatLuongDtlRepository dcnbPhieuKnChatLuongDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Autowired
    DcnbQuyetDinhDcCHdrService dcnbQuyetDinhDcCHdrService;

    @Autowired
    DcnbKeHoachNhapXuatService dcnbKeHoachNhapXuatService;

    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, SearchPhieuKnChatLuong req) throws Exception {

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
                        DcnbKeHoachNhapXuat keHoachNhapXuat = dcnbKeHoachNhapXuatService.detailKhDtl(keHoachDtl.getId());
                        keHoachDtl.setDcnbKeHoachNhapXuat(keHoachNhapXuat);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        });

        return dcnbQuyetDinhDcCHdrs;
    }
    @Transactional
    public DcnbPhieuKnChatLuongHdr save(CustomUserDetails currentUser, DcnbPhieuKnChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
            throw new Exception("Chức năng thêm mới chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
        if (optional.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
            throw new Exception("Số phiếu đã tồn tại");
        }
        DcnbPhieuKnChatLuongHdr data = new DcnbPhieuKnChatLuongHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNgayTao(LocalDateTime.now());
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        objReq.getDcnbPhieuKnChatLuongDtl().forEach(e -> e.setDcnbPhieuKnChatLuongHdr(data));
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(data);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByQdDcIdAndMaLoKho(created.getQdDcId(),created.getMaLoKho());
        dcnbKeHoachDcDtls.forEach(e-> {
            DcnbKeHoachNhapXuat keHoachNhapXuat = new DcnbKeHoachNhapXuat();
            keHoachNhapXuat.setIdKhDcDtl(e.getId());
            keHoachNhapXuat.setIdHdr(created.getId());
            keHoachNhapXuat.setTableName(DcnbPhieuKnChatLuongHdr.TABLE_NAME);
            keHoachNhapXuat.setType(Contains.QD_XUAT);
            try {
                dcnbKeHoachNhapXuatService.saveOrUpdate(keHoachNhapXuat);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return created;
    }

    @Transactional
    public DcnbPhieuKnChatLuongHdr update(CustomUserDetails currentUser, DcnbPhieuKnChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
            throw new Exception("Chức năng cập nhật chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbPhieuKnChatLuongHdr> soPhieu = dcnbPhieuKnChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoPhieu())) {
            if (soPhieu.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
                if (!soPhieu.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số phiếu đã tồn tại");
                }
            }
        }

        DcnbPhieuKnChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDateTime.now());
        data.setDcnbPhieuKnChatLuongDtl(objReq.getDcnbPhieuKnChatLuongDtl());
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByQdDcIdAndMaLoKho(created.getQdDcId(),created.getMaLoKho());
        dcnbKeHoachDcDtls.forEach(e-> {
            DcnbKeHoachNhapXuat keHoachNhapXuat = new DcnbKeHoachNhapXuat();
            keHoachNhapXuat.setIdKhDcDtl(e.getId());
            keHoachNhapXuat.setIdHdr(created.getId());
            keHoachNhapXuat.setTableName(DcnbPhieuKnChatLuongHdr.TABLE_NAME);
            keHoachNhapXuat.setType(Contains.QD_XUAT);
            try {
                dcnbKeHoachNhapXuatService.saveOrUpdate(keHoachNhapXuat);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return created;
    }

    public List<DcnbPhieuKnChatLuongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbPhieuKnChatLuongHdr> allById = dcnbPhieuKnChatLuongHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(),Collections.singleton(DcnbPhieuKnChatLuongHdr.TABLE_NAME + "_CAN_CU"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
            List<DcnbPhieuKnChatLuongDtl> khs = dcnbPhieuKnChatLuongDtlRepository.findByHdrId(data.getId());
            data.setDcnbPhieuKnChatLuongDtl(khs);
        });
        return allById;
    }

    public DcnbPhieuKnChatLuongHdr detail(Long id) throws Exception {
        List<DcnbPhieuKnChatLuongHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbPhieuKnChatLuongHdr> optional = dcnbPhieuKnChatLuongHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuKnChatLuongHdr data = optional.get();
        List<DcnbPhieuKnChatLuongDtl> list = dcnbPhieuKnChatLuongDtlRepository.findByHdrId(data.getId());
        dcnbPhieuKnChatLuongDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKnChatLuongHdrRepository.delete(data);
    }

//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<DcnbPhieuKnChatLuongHdr> list = dcnbPhieuKnChatLuongHdrRepository.findAllByIdIn(idSearchReq.getIdList());
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(DcnbPhieuKnChatLuongHdr::getId).collect(Collectors.toList());
//        List<DcnbPhieuKnChatLuongDtl> listDtl = dcnbPhieuKnChatLuongDtlRepository.findByHdrIdIn(listId);
//        dcnbPhieuKnChatLuongDtlRepository.deleteAll(listDtl);
//        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
//        dcnbPhieuKnChatLuongHdrRepository.deleteAll(list);
//    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuKnChatLuongHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbPhieuKnChatLuongHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbPhieuKnChatLuongHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbPhieuKnChatLuongHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_TP + Contains.CHODUYET_TP:
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TP + Contains.TU_CHOI_TP:
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_TP:
            case Contains.CHODUYET_LDC + Contains.TU_CHOI_LDC:
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.DA_DUYET_LDC:
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setNgayPDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbPhieuKnChatLuongHdr created = dcnbPhieuKnChatLuongHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchPhieuKnChatLuong objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        objReq.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
        Page<DcnbPhieuKnChatLuongHdr> page = dcnbPhieuKnChatLuongHdrRepository.search(objReq,pageable);
        List<DcnbPhieuKnChatLuongHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbPhieuKnChatLuongHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdinhDc();
            objs[2] = dx.getNam();
            objs[3] = dx.getNgayTao();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getThayDoiThuKho();
            objs[7] = dx.getSoBbLayMau();
            objs[8] = dx.getNgayLayMau();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
