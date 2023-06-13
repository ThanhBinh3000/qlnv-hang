package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbDataLinkHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhieuKtChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
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
public class DcnbPhieuKiemTraChatLuongServiceImpl extends BaseServiceImpl {

    @Autowired
    DcnbPhieuKtChatLuongHdrRepository dcnbPhieuKtChatLuongHdrRepository;
    @Autowired
    DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;

    @Autowired
    DcnbPhieuKtChatLuongDtlRepository dcnbPhieuKtChatLuongDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    public Page<DcnbPhieuKtChatLuongHdr> searchPage(CustomUserDetails currentUser, SearchPhieuKtChatLuong req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbPhieuKtChatLuongHdr> search = dcnbPhieuKtChatLuongHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public DcnbPhieuKtChatLuongHdr save(CustomUserDetails currentUser, DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
            throw new Exception("Chức năng thêm mới chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
        if (optional.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
            throw new Exception("Số phiếu đã tồn tại");
        }
        DcnbPhieuKtChatLuongHdr data = new DcnbPhieuKtChatLuongHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNgayTao(LocalDateTime.now());
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        objReq.getDcnbPhieuKtChatLuongDtl().forEach(e -> e.setDcnbPhieuKtChatLuongHdr(data));
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(data);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    @Transactional
    public DcnbPhieuKtChatLuongHdr update(CustomUserDetails currentUser, DcnbPhieuKtChatLuongHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
            throw new Exception("Chức năng cập nhật chỉ dành cho cấp cục");
        }
        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbPhieuKtChatLuongHdr> soPhieu = dcnbPhieuKtChatLuongHdrRepository.findFirstBySoPhieu(objReq.getSoPhieu());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoPhieu())) {
            if (soPhieu.isPresent() && objReq.getSoPhieu().split("/").length == 1) {
                if (!soPhieu.get().getId().equals(objReq.getId())) {
                    throw new Exception("Số phiếu đã tồn tại");
                }
            }
        }

        DcnbPhieuKtChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDateTime.now());
        data.setDcnbPhieuKtChatLuongDtl(objReq.getDcnbPhieuKtChatLuongDtl());
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU");
        data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    public List<DcnbPhieuKtChatLuongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbPhieuKtChatLuongHdr> allById = dcnbPhieuKtChatLuongHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(),Collections.singleton(DcnbPhieuKtChatLuongHdr.TABLE_NAME + "_CAN_CU"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
            List<DcnbPhieuKtChatLuongDtl> khs = dcnbPhieuKtChatLuongDtlRepository.findByHdrId(data.getId());
            data.setDcnbPhieuKtChatLuongDtl(khs);
        });
        return allById;
    }

    public DcnbPhieuKtChatLuongHdr detail(Long id) throws Exception {
        List<DcnbPhieuKtChatLuongHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbPhieuKtChatLuongHdr> optional = dcnbPhieuKtChatLuongHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbPhieuKtChatLuongHdr data = optional.get();
        List<DcnbPhieuKtChatLuongDtl> list = dcnbPhieuKtChatLuongDtlRepository.findByHdrId(data.getId());
        dcnbPhieuKtChatLuongDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKtChatLuongHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbPhieuKtChatLuongHdr> list = dcnbPhieuKtChatLuongHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbPhieuKtChatLuongHdr::getId).collect(Collectors.toList());
        List<DcnbPhieuKtChatLuongDtl> listDtl = dcnbPhieuKtChatLuongDtlRepository.findByHdrIdIn(listId);
        dcnbPhieuKtChatLuongDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        dcnbPhieuKtChatLuongHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbPhieuKtChatLuongHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbPhieuKtChatLuongHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbPhieuKtChatLuongHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbPhieuKtChatLuongHdr> optional) throws Exception {
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
                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
                        optional.get().getQdDcId(),
                        optional.get().getMaNganKho(),
                        optional.get().getMaLoKho());
                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
                dataLinkDtl.setLinkId(optional.get().getId());
                dataLinkDtl.setHdrId(dataLink.getId());
                dataLinkDtl.setType(DcnbPhieuKtChatLuongHdr.TABLE_NAME);
                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbPhieuKtChatLuongHdr created = dcnbPhieuKtChatLuongHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchPhieuKtChatLuong objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbPhieuKtChatLuongHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbPhieuKtChatLuongHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbPhieuKtChatLuongHdr dx = data.get(i);
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
