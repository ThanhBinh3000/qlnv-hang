package com.tcdt.qlnvhang.service.dieuchuyennoibo;


import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class THKeHoachDieuChuyenCucService extends BaseServiceImpl {

    @Autowired
    private THKeHoachDieuChuyenCucHdrRepository thKeHoachDieuChuyenHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenNoiBoCucDtlRepository thKeHoachDieuChuyenNoiBoCucDtlRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    private THKeHoachDieuChuyenCucKhacCucDtlRepository thKeHoachDieuChuyenCucKhacCucDtlRepository;

    @Autowired
    private THKeHoachDieuChuyenTongCucHdrRepository tongCucHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenTongCucDtlRepository thKeHoachDieuChuyenTongCucDtlRepository;

    @Autowired
    private DcnbKeHoachDcHdrRepository dcHdrRepository;
    @Autowired
    private EntityManager entityManager;


    public Page<THKeHoachDieuChuyenCucHdr> searchPageCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        req.setMaDVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<THKeHoachDieuChuyenCucHdr> search = thKeHoachDieuChuyenHdrRepository.search(req, pageable);
        return search;
    }

    public List<THKeHoachDieuChuyenCucHdr> danhSachSoDeXuat(TongHopKeHoachDieuChuyenSearch req) throws Exception{
        List<THKeHoachDieuChuyenCucHdr> danhSachSoDeXuat = thKeHoachDieuChuyenHdrRepository.filterSoDeXuat(req);
        return danhSachSoDeXuat;
    }

    public THKeHoachDieuChuyenCucHdr yeuCauXacDinhDiemNhap(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp cục");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(statusReq.getId());
        if (optional.isPresent() && optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            THKeHoachDieuChuyenCucHdr data = optional.get();
            data.setTrangThai(Contains.YEU_CAU_XAC_DINH_DIEM_NHAP);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            for (THKeHoachDieuChuyenNoiBoCucDtl boCucDtl : created.getThKeHoachDieuChuyenNoiBoCucDtls()) {
                dcHdrRepository.updateTrangThaiNdc(boCucDtl.getDcKeHoachDcDtlId(), Contains.GIUA_2_CHI_CUC_TRONG_1_CUC);
            }
            return created;
        }
        return null;
    }


    @Transactional
    public THKeHoachDieuChuyenCucHdr save(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        THKeHoachDieuChuyenCucHdr data = new THKeHoachDieuChuyenCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.DUTHAO);
        data.setNgaytao(LocalDate.now());
        data.setNgayTongHop(objReq.getNgayTongHop());
        data.setThoiGianTongHop(objReq.getThoiGianTongHop());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNamKeHoach(objReq.getNamKeHoach());
        data.setLoaiDieuChuyen(objReq.getLoaiDieuChuyen());
        if (Objects.equals(data.getLoaiDieuChuyen(), Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            List<ThKeHoachDieuChuyenNoiBoCucDtlReq> planCuc = createPlanChiCuc(currentUser, tongHopSearch);
            List<THKeHoachDieuChuyenNoiBoCucDtl> dtls = new ArrayList<>();
            for (ThKeHoachDieuChuyenNoiBoCucDtlReq req : planCuc) {
                THKeHoachDieuChuyenNoiBoCucDtl dtl = new ModelMapper().map(req, THKeHoachDieuChuyenNoiBoCucDtl.class);
                dtls.add(dtl);
            }
            dtls.forEach(e -> e.setTHKeHoachDieuChuyenCucHdr(data));
            data.setThKeHoachDieuChuyenNoiBoCucDtls(dtls);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            return created;
        } else if (Objects.equals(data.getLoaiDieuChuyen(), Contains.GIUA_2_CUC_DTNN_KV)) {
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            List<THKeHoachDieuChuyenCucKhacCucDtlReq> planCuc = createPlanCuc(currentUser, tongHopSearch);
            List<THKeHoachDieuChuyenCucKhacCucDtl> dtls = new ArrayList<>();
            for (THKeHoachDieuChuyenCucKhacCucDtlReq req : planCuc) {
                THKeHoachDieuChuyenCucKhacCucDtl dtl = new ModelMapper().map(req, THKeHoachDieuChuyenCucKhacCucDtl.class);
                dtls.add(dtl);
            }
            dtls.forEach(e -> e.setTHKeHoachDieuChuyenCucHdr(data));
            data.setThKeHoachDieuChuyenCucKhacCucDtls(dtls);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            return created;
        }
        return null;
    }


    public List<THKeHoachDieuChuyenCucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenCucHdr> allById = thKeHoachDieuChuyenHdrRepository.findAllById(ids);
//        allById.forEach(data -> {
//            List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
//            data.setThKeHoachDieuChuyenNoiBoCucDtls(thKeHoachDieuChuyenNoiBoCucDtls);
//            thKeHoachDieuChuyenNoiBoCucDtls.forEach();
//        });
        return allById;
    }


    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        List<THKeHoachDieuChuyenNoiBoCucDtl> list = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(list);
        List<THKeHoachDieuChuyenCucKhacCucDtl> list1 = thKeHoachDieuChuyenCucKhacCucDtlRepository.findByHdrId(data.getId());
        thKeHoachDieuChuyenCucKhacCucDtlRepository.deleteAll(list1);
        thKeHoachDieuChuyenHdrRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<THKeHoachDieuChuyenCucHdr> list = thKeHoachDieuChuyenHdrRepository.findAllById(idSearchReq.getIds());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(THKeHoachDieuChuyenCucHdr::getId).collect(Collectors.toList());
        List<THKeHoachDieuChuyenNoiBoCucDtl> listTongHopKeHoachChiCuc = thKeHoachDieuChuyenNoiBoCucDtlRepository.findAllByHdrIdIn(listId);
        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(listTongHopKeHoachChiCuc);
        List<THKeHoachDieuChuyenCucKhacCucDtl> listTongHopKeHoachKhacCuc = thKeHoachDieuChuyenCucKhacCucDtlRepository.findAllByHdrIdIn(listId);
        thKeHoachDieuChuyenCucKhacCucDtlRepository.deleteAll(listTongHopKeHoachKhacCuc);
        thKeHoachDieuChuyenHdrRepository.deleteAll(list);
    }


    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(statusReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approveTongHop(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    @Transactional
    public THKeHoachDieuChuyenCucHdr update(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        List<THKeHoachDieuChuyenCucHdr> maTongHop = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (!maTongHop.isEmpty() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get(0).getId().equals(objReq.getId())) {
                throw new Exception("Mã tổng hợp đã tồn tại");
            }
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDate.now());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
        thKeHoachDieuChuyenHdrRepository.save(created);
        return created;
    }

    @Transactional
    public THKeHoachDieuChuyenCucHdr approveTongHop(CustomUserDetails currentUser, StatusReq statusReq, Optional<THKeHoachDieuChuyenCucHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.YEU_CAU_XAC_DINH_DIEM_NHAP + Contains.CHODUYET_TP:
                optional.get().setNguoiGDuyetId(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_TP + Contains.TU_CHOI_TP:
                optional.get().setTrangThai(Contains.YEU_CAU_XAC_DINH_DIEM_NHAP);
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.TU_CHOI_LDC:
                optional.get().setTrangThai(Contains.YEU_CAU_XAC_DINH_DIEM_NHAP);
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                break;
            case Contains.CHODUYET_LDC + Contains.DA_DUYET_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(optional.get());
        return created;
    }

    @Transactional
    public void export(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<THKeHoachDieuChuyenCucHdr> page = this.searchPageCuc(currentUser, objReq);
        List<THKeHoachDieuChuyenCucHdr> data = page.getContent();
        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            THKeHoachDieuChuyenCucHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getMaTongHop();
            objs[3] = dx.getNgayDuyetLdc();
            objs[4] = dx.getLoaiDieuChuyen();
            objs[5] = dx.getTenDvi();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Transactional
    public List<ThKeHoachDieuChuyenNoiBoCucDtlReq> createPlanChiCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        List<ThKeHoachDieuChuyenNoiBoCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenNoiBoCucDtls = dcnbKeHoachDcDtlRepository.findByDonViAndTrangThaiChiCuc(req.getMaDVi(), Contains.DADUYET_LDCC, Contains.DIEU_CHUYEN, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, req.getThoiGianTongHop());
            for (DcnbKeHoachDcDtl entry : thKeHoachDieuChuyenNoiBoCucDtls) {
                Hibernate.initialize(entry.getDcnbKeHoachDcHdr());
                DcnbKeHoachDcHdr entryClone = SerializationUtils.clone(entry.getDcnbKeHoachDcHdr());
                ThKeHoachDieuChuyenNoiBoCucDtlReq dtl = new ModelMapper().map(entryClone, ThKeHoachDieuChuyenNoiBoCucDtlReq.class);
                dtl.setId(null);
                dtl.setHdrId(null);
                dtl.setDcKeHoachDcHdrId(entryClone.getId());
                dtl.setDcKeHoachDcDtlId(entry.getId());
                List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndId(entryClone.getId(),entry.getId());
                dtl.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                result.add(dtl);
            }
        }
        return result;
    }

    public List<THKeHoachDieuChuyenCucKhacCucDtlReq> createPlanCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        List<THKeHoachDieuChuyenCucKhacCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = dcHdrRepository.findByDonViAndTrangThaiCuc(req.getMaDVi(), TrangThaiAllEnum.DA_DUYET_LDCC.getId(), Contains.GIUA_2_CUC_DTNN_KV, Contains.DIEU_CHUYEN, req.getThoiGianTongHop());
            for (DcnbKeHoachDcHdr khh : dcnbKeHoachDcHdrs) {
                Hibernate.initialize(khh.getDanhSachHangHoa());
                DcnbKeHoachDcHdr khhc = SerializationUtils.clone(khh);
                THKeHoachDieuChuyenCucKhacCucDtlReq dtl = new ModelMapper().map(khhc, THKeHoachDieuChuyenCucKhacCucDtlReq.class);
                dtl.setId(null);
                dtl.setDcnbKeHoachDcHdrId(khh.getId());
                dtl.setHdrId(null);
                dtl.setNgayGduyetTc(null);
                List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(dtl.getDcnbKeHoachDcHdrId());
                dtl.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                result.add(dtl);
            }
        }
        return result;
    }

}



