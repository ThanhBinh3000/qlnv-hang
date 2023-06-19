package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBienBanLayMauHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanLayMauHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbLoKhoDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
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

@Service
public class DcnbBienBanLayMauServiceImpl extends BaseServiceImpl {

    @Autowired
    DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;

    @Autowired
    DcnbBienBanLayMauDtlRepository dcnbBienBanLayMauDtlRepository;

    @Autowired
    QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;
    @Autowired
    private DcnbDataLinkDtlRepository dcnbDataLinkDtlRepository;

    @Autowired
    HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;

    @Autowired
    DcnbPhieuKtChatLuongHdrRepository dcnbPhieuKtChatLuongHdrRepository;

    @Autowired
    DcnbQuyetDinhDcCHdrServiceImpl dcnbQuyetDinhDcCHdrServiceImpl;

    @Autowired
    DcnbQuyetDinhDcCHdrRepository dcnbQuyetDinhDcCHdrRepository;

    @Autowired
    DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;


    public Page<DcnbBienBanLayMauHdrDTO> searchPage(CustomUserDetails currentUser, SearchDcnbBienBanLayMau req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBienBanLayMauHdrDTO> searchDto = null;
        if(req.getIsVatTu() == null){
            req.setIsVatTu(false);
        }
        if(req.getIsVatTu()){
            req.setDsLoaiHang(Arrays.asList("VT"));
        }else {
            req.setDsLoaiHang(Arrays.asList("LT","M"));
        }
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            searchDto = dcnbBienBanLayMauHdrRepository.searchPageChiCuc(req, pageable);
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setTypeDataLink(Contains.DIEU_CHUYEN);
            searchDto = dcnbBienBanLayMauHdrRepository.searchPageCuc(req, pageable);
        }
        return searchDto;
    }

    @Transactional
    public DcnbBienBanLayMauHdr save(CustomUserDetails currentUser, DcnbBienBanLayMauHdrReq objReq) throws Exception {
        QlnvDmDonvi cqt = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findFirstBySoBbLayMau(objReq.getSoBbLayMau());
        if (optional.isPresent() && objReq.getSoBbLayMau() != null && objReq.getSoBbLayMau().split("/").length == 1) {
            throw new Exception("số biên bản lấy mẫu đã tồn tại");
        }
        DcnbBienBanLayMauHdr data = new DcnbBienBanLayMauHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setKtvBaoQuanId(currentUser.getUser().getId());
        data.setKtvBaoQuan(currentUser.getUser().getUsername());
        data.setMaQhns(cqt.getMaQhns());
        data.setMaDvi(cqt.getMaDvi());
        data.setTenDvi(cqt.getTenDvi());
        data.setLoaiDc(objReq.getLoaiDc());
        data.setType(objReq.getType());
        // Biên bản lấy mẫu thì auto thay đổi thủ kho
        data.setThayDoiThuKho(true);
        data.setTrangThai(Contains.DUTHAO);
        data.setNgayTao(LocalDateTime.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        objReq.getDcnbBienBanLayMauDtl().forEach(e -> {
            e.setDcnbBienBanLayMauHdr(data);
        });
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU");
        created.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByQdDcIdAndMaLoKho(created.getQdDcCucId(), created.getMaLoKho());
        List<FileDinhKem> fileDinhKemMauNiemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemChupMauNiemPhong(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG");
        created.setFileDinhKemChupMauNiemPhong(fileDinhKemMauNiemPhong);
        return created;
    }

    @Transactional
    public DcnbBienBanLayMauHdr update(CustomUserDetails currentUser, DcnbBienBanLayMauHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbBienBanLayMauHdr> soDxuat = dcnbBienBanLayMauHdrRepository.findFirstBySoBbLayMau(objReq.getSoBbLayMau());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBbLayMau())) {
            if (soDxuat.isPresent() && objReq.getSoBbLayMau().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số biên bản lấy mẫu đã tồn tại");
                }
            }
        }
        DcnbBienBanLayMauHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setDcnbBienBanLayMauDtl(objReq.getDcnbBienBanLayMauDtl());
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
        List<FileDinhKem> fileDinhKemMauNiemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemChupMauNiemPhong(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG");
        created.setFileDinhKemChupMauNiemPhong(fileDinhKemMauNiemPhong);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getBienBanLayMauDinhKem(), created.getId(), DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU");
        created.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);
        return created;
    }

    public List<DcnbBienBanLayMauHdr> detail(List<Long> ids) throws Exception {
        List<DcnbBienBanLayMauHdr> allById = dcnbBienBanLayMauHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);

            List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
            data.setBienBanLayMauDinhKem(bienBanLayMauDinhKem);

            List<FileDinhKem> fileDinhKemChupMauNiemPhong = fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(), Collections.singleton(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
            data.setFileDinhKemChupMauNiemPhong(fileDinhKemChupMauNiemPhong);

        });
        return allById;
    }

    public DcnbBienBanLayMauHdr detail(Long id) throws Exception {
        List<DcnbBienBanLayMauHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbBienBanLayMauHdr> optional = dcnbBienBanLayMauHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBienBanLayMauHdr data = optional.get();
        List<DcnbBienBanLayMauDtl> list = dcnbBienBanLayMauDtlRepository.findByHdrId(data.getId());
        dcnbBienBanLayMauDtlRepository.deleteAll(list);
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_BIEN_BAN_LAY_MAU"));
        fileDinhKemService.delete(idSearchReq.getId(), Lists.newArrayList(DcnbBienBanLayMauHdr.TABLE_NAME + "_MAU_DA_NIEM_PHONG"));
        dcnbBienBanLayMauHdrRepository.delete(data);
    }


    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBienBanLayMauHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBienBanLayMauHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBienBanLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBienBanLayMauHdr> optional) throws Exception {
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

                DcnbDataLinkHdr dataLink = dcnbDataLinkHdrRepository.findDataLinkChiCuc(optional.get().getMaDvi(),
                        optional.get().getQdDcCucId(),
                        optional.get().getMaNganKho(),
                        optional.get().getMaLoKho());
                DcnbDataLinkDtl dataLinkDtl = new DcnbDataLinkDtl();
                dataLinkDtl.setLinkId(optional.get().getId());
                dataLinkDtl.setHdrId(dataLink.getId());
                if ("00".equals(optional.get().getType())) { // xuất
                    dataLinkDtl.setType("XDC" + DcnbBienBanLayMauHdr.TABLE_NAME);
                } else if ("01".equals(optional.get().getType())) {
                    dataLinkDtl.setType("NDC" + DcnbBienBanLayMauHdr.TABLE_NAME);
                } else {
                    throw new Exception("Type phải là 00 hoặc 01!");
                }
                dcnbDataLinkDtlRepository.save(dataLinkDtl);
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBienBanLayMauHdr created = dcnbBienBanLayMauHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbBienBanLayMau objReq, HttpServletResponse response) throws Exception {
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        objReq.setPaggingReq(paggingReq);
//        Page<DcnbBienBanLayMauHdr> page = this.searchPage(currentUser, objReq);
//        List<DcnbBienBanLayMauHdr> data = page.getContent();
//
//        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
//        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
//        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//        for (int i = 0; i < data.size(); i++) {
//            DcnbBienBanLayMauHdr dx = data.get(i);
//            objs = new Object[rowsName.length];
//            objs[0] = i;
//            objs[1] = dx.getSoQdinhDcc();
//            objs[2] = dx.getNam();
//            objs[3] = dx.getNgayTao();
//            objs[4] = dx.getTenDiemKho();
//            objs[5] = dx.getTenLoKho();
//            objs[6] = dx.getThayDoiThuKho();
//            objs[7] = dx.getSoBbLayMau();
//            objs[8] = dx.getNgayLayMau();
//            dataList.add(objs);
//        }
//        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//        ex.export();
    }

    public List<DcnbBienBanLayMauHdrDTO> danhSachBienBan(CustomUserDetails currentUser, SearchDcnbBienBanLayMau objReq) {
        // TO DO
//         Lấy ra các biên bản lấy mẫu của câấp dưới - trạng thái ban hành và có qDinhDccId được truyền lên (qDinhDccId truyền lên là qDinhDccId gốc)
//         DcnbBienBanLayMauHdrDTO JOIN  với QUYET định và wherer theo parentID  = qDinhDccId
        List<DcnbBienBanLayMauHdrDTO> dcnbBienBanLayMauHdrDTOList = dcnbBienBanLayMauHdrRepository.findByMaDviAndTrangThaiAndQdinhDcId(currentUser.getDvql(), Contains.BAN_HANH, objReq.getQDinhDccId());
        return dcnbBienBanLayMauHdrDTOList;
    }

    public List<DcnbLoKhoDTO> danhSachMaLokho(SearchDcnbBienBanLayMau objReq) {
        return dcnbBienBanLayMauHdrRepository.danhSachMaLokho(objReq);
    }
}
