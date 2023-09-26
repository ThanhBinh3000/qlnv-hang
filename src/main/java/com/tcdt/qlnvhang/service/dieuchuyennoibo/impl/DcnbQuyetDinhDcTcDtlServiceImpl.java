package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcTcHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcTc;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DcnbQuyetDinhDcTcDtlServiceImpl extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbQuyetDinhDcTcDtlServiceImpl.class);

    @Autowired
    private DcnbQuyetDinhDcTcHdrRepository dcnbQuyetDinhDcTcHdrRepository;

    @Autowired
    private DcnbQuyetDinhDcTcDtlRepository dcnbQuyetDinhDcTcDtlRepository;
    @Autowired
    private DcnbQuyetDinhDcTcTTDtlRepository dcnbQuyetDinhDcTcTTDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;
    @Autowired
    private THKeHoachDieuChuyenTongCucHdrRepository thKeHoachDCTCHdrRepository;
    @Autowired
    private THKeHoachDieuChuyenCucHdrRepository tHKeHoachDieuChuyenCucHdrRepository;
    @Autowired
    private LuuKhoClient luuKhoClient;

    public Page<DcnbQuyetDinhDcTcHdr> searchPage(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcTc req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setMaDvi(currentUser.getDvql());
        Page<DcnbQuyetDinhDcTcHdr> search = dcnbQuyetDinhDcTcHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public DcnbQuyetDinhDcTcHdr save(CustomUserDetails currentUser, DcnbQuyetDinhDcTcHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcTcHdr> optional = dcnbQuyetDinhDcTcHdrRepository.findFirstBySoQdinh(objReq.getSoQdinh());
        if (optional.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
            throw new Exception("số quyết định đã tồn tại");
        }
        List<DcnbQuyetDinhDcTcHdr> quyetDinhDcTcHdrs = dcnbQuyetDinhDcTcHdrRepository.findByIdIn(Arrays.asList(objReq.getIdThop()));
        if (!quyetDinhDcTcHdrs.isEmpty()) {
            throw new Exception("Mã tổng hợp đã được tạo Quyết định!");
        }
        DcnbQuyetDinhDcTcHdr data = new DcnbQuyetDinhDcTcHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setType(Contains.DIEU_CHUYEN);
        data.setTrangThai(Contains.DUTHAO);
        data.setDanhSachQuyetDinh(getDanhSachQuyetDinh(objReq, false));
        DcnbQuyetDinhDcTcHdr created = dcnbQuyetDinhDcTcHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        if (created.getIdThop() != null) {
            Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoach = thKeHoachDCTCHdrRepository.findById(created.getIdThop());
            if (thKeHoach.isPresent()) {
                thKeHoach.get().setTrangThai(Contains.DADUTHAO_QD);
                thKeHoach.get().setQdDcId(created.getId());
                thKeHoach.get().setSoQddc(created.getSoQdinh());
                thKeHoachDCTCHdrRepository.save(thKeHoach.get());
            }
        }
        return created;
    }

    @Transactional
    public DcnbQuyetDinhDcTcHdr update(CustomUserDetails currentUser, DcnbQuyetDinhDcTcHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcTcHdr> optional = dcnbQuyetDinhDcTcHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        // chuyển cái cũ về chưa tạo quyết định
        if (optional.get().getIdThop() != null) {
            Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoach = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
            if (thKeHoach.isPresent()) {
                thKeHoach.get().setTrangThai(Contains.CHUATAO_QD);
                thKeHoach.get().setQdDcId(null);
                thKeHoach.get().setSoQddc(null);
                thKeHoachDCTCHdrRepository.save(thKeHoach.get());
            }
        }
        Optional<DcnbQuyetDinhDcTcHdr> soDxuat = dcnbQuyetDinhDcTcHdrRepository.findFirstBySoQdinh(objReq.getSoQdinh());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoQdinh())) {
            if (soDxuat.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
        }

        DcnbQuyetDinhDcTcHdr data = optional.get();
        objReq.setType(data.getType());
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
//        deleteDetail(optional.get());
//        dcnbQuyetDinhDcTcTTDtlRepository.flush();
//        dcnbQuyetDinhDcTcDtlRepository.flush();
        data.setDanhSachQuyetDinh(getDanhSachQuyetDinh(objReq, true));
        DcnbQuyetDinhDcTcHdr created = dcnbQuyetDinhDcTcHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU");

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH"));
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        if (created.getIdThop() != null) {
            Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoach = thKeHoachDCTCHdrRepository.findById(created.getIdThop());
            if (thKeHoach.isPresent()) {
                thKeHoach.get().setTrangThai(Contains.DADUTHAO_QD);
                thKeHoach.get().setQdDcId(created.getId());
                thKeHoach.get().setSoQddc(created.getSoQdinh());
                thKeHoachDCTCHdrRepository.save(thKeHoach.get());
            }
        }
        return created;
    }

    private List<DcnbQuyetDinhDcTcDtl> getDanhSachQuyetDinh(DcnbQuyetDinhDcTcHdrReq objReq, boolean isUpdate) {
        List<DcnbQuyetDinhDcTcDtl> result = new ArrayList<>();
        List<THKeHoachDieuChuyenTongCucDtl> quyetDinhPdDtl = objReq.getQuyetDinhPdDtl();
        for (THKeHoachDieuChuyenTongCucDtl qd : quyetDinhPdDtl) {
            DcnbQuyetDinhDcTcDtl qdtc = new DcnbQuyetDinhDcTcDtl();
            qdtc.setId(isUpdate ? qd.getId() :null);
            qdtc.setHdrId(isUpdate ? qd.getHdrId() :null);
            qdtc.setMaCucXuat(qd.getMaCucDxuat() == null ? qd.getMaCucXuat() : qd.getMaCucDxuat());
            qdtc.setTenCucXuat(qd.getTenCucDxuat() == null ? qd.getTenCucXuat() : qd.getTenCucDxuat());
            qdtc.setMaCucNhan(qd.getMaCucNhan());
            qdtc.setTenCucNhan(qd.getTenCucNhan());
            qdtc.setSoDxuat(qd.getSoDxuat());
            qdtc.setNgayTrinhTc(qd.getThKeHoachDieuChuyenCucHdr() == null ? qd.getNgayTrinhTc() : qd.getThKeHoachDieuChuyenCucHdr().getNgayTrinhDuyetTc());
            qdtc.setTongDuToanKp(qd.getTongDuToanKp());
            qdtc.setTrichYeu(qd.getThKeHoachDieuChuyenCucHdr() == null ? qd.getTrichYeu() : qd.getThKeHoachDieuChuyenCucHdr().getTrichYeu());
            List<DcnbQuyetDinhDcTcTTDtl> danhSachQuyetDinhChiTiet = new ArrayList<>();
            if (qd.getThKeHoachDieuChuyenCucKhacCucDtl() != null && !qd.getThKeHoachDieuChuyenCucKhacCucDtl().getDcnbKeHoachDcHdr().isEmpty()) {
                List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = qd.getThKeHoachDieuChuyenCucKhacCucDtl().getDcnbKeHoachDcHdr();
                for (DcnbKeHoachDcHdr dc : dcnbKeHoachDcHdr) {
                    DcnbQuyetDinhDcTcTTDtl dcnbQuyetDinhDcTcTTDtl = new DcnbQuyetDinhDcTcTTDtl();
                    dcnbQuyetDinhDcTcTTDtl.setKeHoachDcHdrId(dc.getId());
                    danhSachQuyetDinhChiTiet.add(dcnbQuyetDinhDcTcTTDtl);
                }
            } else if (qd.getThKeHoachDieuChuyenCucHdr() != null && !qd.getThKeHoachDieuChuyenCucHdr().getThKeHoachDieuChuyenNoiBoCucDtls().isEmpty()) {
                List<THKeHoachDieuChuyenNoiBoCucDtl> thKeHoachDieuChuyenNoiBoCucDtls = qd.getThKeHoachDieuChuyenCucHdr().getThKeHoachDieuChuyenNoiBoCucDtls();
                Set<Long> ids = new HashSet<>();
                for (THKeHoachDieuChuyenNoiBoCucDtl dc : thKeHoachDieuChuyenNoiBoCucDtls) {
                    ids.add(dc.getDcKeHoachDcHdrId());
                }
                for (Long id : ids) {
                    DcnbQuyetDinhDcTcTTDtl dcnbQuyetDinhDcTcTTDtl = new DcnbQuyetDinhDcTcTTDtl();
                    dcnbQuyetDinhDcTcTTDtl.setKeHoachDcHdrId(id);
                    danhSachQuyetDinhChiTiet.add(dcnbQuyetDinhDcTcTTDtl);
                }
            }else if(qd.getDanhSachQuyetDinhChiTiet() != null && !qd.getDanhSachQuyetDinhChiTiet().isEmpty()){
                for (DcnbQuyetDinhDcTcTTDtl dc : qd.getDanhSachQuyetDinhChiTiet()) {
                    danhSachQuyetDinhChiTiet.add(dc);
                }
            }else if(qd.getDanhSachHangHoa() != null && !qd.getDanhSachHangHoa().isEmpty()){
                Set<Long> ids = new HashSet<>();
                for (DcnbKeHoachDcDtl khct : qd.getDanhSachHangHoa()) {
                    ids.add(khct.getHdrId());
                }
                for (Long id : ids) {
                    DcnbQuyetDinhDcTcTTDtl dcnbQuyetDinhDcTcTTDtl = new DcnbQuyetDinhDcTcTTDtl();
                    dcnbQuyetDinhDcTcTTDtl.setKeHoachDcHdrId(id);
                    danhSachQuyetDinhChiTiet.add(dcnbQuyetDinhDcTcTTDtl);
                }
            }

            qdtc.setDanhSachQuyetDinhChiTiet(danhSachQuyetDinhChiTiet);
            result.add(qdtc);
        }
        return result;
    }


    public List<DcnbQuyetDinhDcTcHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbQuyetDinhDcTcHdr> optional = dcnbQuyetDinhDcTcHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbQuyetDinhDcTcHdr> allById = dcnbQuyetDinhDcTcHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            List<FileDinhKem> quyetDinh = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH"));
            data.setQuyetDinh(quyetDinh);

            List<DcnbQuyetDinhDcTcDtl> sachQuyetDinh = data.getDanhSachQuyetDinh();
            sachQuyetDinh.forEach(data1 -> {
                data1.getDanhSachQuyetDinhChiTiet().forEach(data2 -> {
                    Optional<DcnbKeHoachDcHdr> hoachDcHdr = dcnbKeHoachDcHdrRepository.findById(data2.getKeHoachDcHdrId());
                    if (hoachDcHdr.isPresent()) {
                        data2.setDanhSachKeHoach(hoachDcHdr.get().getDanhSachHangHoa());
                        data2.setDcnbKeHoachDcHdr(hoachDcHdr.get());
                    }
                });
            });
        });
        return allById;
    }

    public DcnbQuyetDinhDcTcHdr detail(Long id) throws Exception {
        List<DcnbQuyetDinhDcTcHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbQuyetDinhDcTcHdr> optional = dcnbQuyetDinhDcTcHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbQuyetDinhDcTcHdr data = optional.get();
        deleteDetail(data);
        dcnbQuyetDinhDcTcHdrRepository.delete(data);
        if (data.getIdThop() != null) {
            Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoach = thKeHoachDCTCHdrRepository.findById(data.getIdThop());
            if (thKeHoach.isPresent()) {
                thKeHoach.get().setTrangThai(Contains.CHUATAO_QD);
                thKeHoach.get().setQdDcId(null);
                thKeHoach.get().setSoQddc(null);
                thKeHoachDCTCHdrRepository.save(thKeHoach.get());
            }
        }
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH"));
    }

    private void deleteDetail(DcnbQuyetDinhDcTcHdr data) {
        List<DcnbQuyetDinhDcTcDtl> listDtls = dcnbQuyetDinhDcTcDtlRepository.findByHdrId(data.getId());
        List<Long> listDtlIds = listDtls.stream().map(DcnbQuyetDinhDcTcDtl::getId).collect(Collectors.toList());
        List<DcnbQuyetDinhDcTcTTDtl> listTTDtls = dcnbQuyetDinhDcTcTTDtlRepository.findByHdrIdIn(listDtlIds);
        dcnbQuyetDinhDcTcTTDtlRepository.deleteAll(listTTDtls);
        dcnbQuyetDinhDcTcDtlRepository.deleteAll(listDtls);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbQuyetDinhDcTcHdr> list = dcnbQuyetDinhDcTcHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listIdDetail = list.stream().map(DcnbQuyetDinhDcTcHdr::getId).collect(Collectors.toList());
        List<DcnbQuyetDinhDcTcDtl> listDtl = dcnbQuyetDinhDcTcDtlRepository.findByHdrIdIn(listIdDetail);
        List<Long> listIdDetailTT = listDtl.stream().map(DcnbQuyetDinhDcTcDtl::getId).collect(Collectors.toList());
        List<DcnbQuyetDinhDcTcTTDtl> listDtlTT = dcnbQuyetDinhDcTcTTDtlRepository.findByHdrIdIn(listIdDetailTT);
        dcnbQuyetDinhDcTcDtlRepository.deleteAll(listDtl);
        dcnbQuyetDinhDcTcTTDtlRepository.deleteAll(listDtlTT);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH"));
        dcnbQuyetDinhDcTcHdrRepository.deleteAll(list);
        for (DcnbQuyetDinhDcTcHdr qd : list) {
            if (qd.getIdThop() != null) {
                Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoach = thKeHoachDCTCHdrRepository.findById(qd.getIdThop());
                if (thKeHoach.isPresent()) {
                    thKeHoach.get().setTrangThai(Contains.CHUATAO_QD);
                    thKeHoach.get().setQdDcId(null);
                    thKeHoach.get().setSoQddc(null);
                    thKeHoachDCTCHdrRepository.save(thKeHoach.get());
                }
            }
        }
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbQuyetDinhDcTcHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbQuyetDinhDcTcHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbQuyetDinhDcTcHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcTcHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDV:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.DADUTHAO_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.TU_CHOI_BAN_HANH_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.DADUTHAO_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.TU_CHOI_BAN_HANH_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            case Contains.CHODUYET_LDTC + Contains.DADUYET_LDTC:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.DADUTHAO_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            case Contains.CHODUYET_LDTC + Contains.BAN_HANH:
            case Contains.DADUYET_LDTC + Contains.BAN_HANH:
                optional.get().setNgayBanHanhTc(LocalDate.now());
                optional.get().setNguoiBanHanhTcId(currentUser.getUser().getId());
                if (optional.get().getIdThop() != null) {
                    Optional<THKeHoachDieuChuyenTongCucHdr> thKeHoachDieu = thKeHoachDCTCHdrRepository.findById(optional.get().getIdThop());
                    thKeHoachDieu.get().setTrangThai(Contains.DABANHANH_QD);
                    thKeHoachDCTCHdrRepository.save(thKeHoachDieu.get());
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcTcHdr created = dcnbQuyetDinhDcTcHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcTc objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbQuyetDinhDcTcHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbQuyetDinhDcTcHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbQuyetDinhDcTcHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdinh();
            objs[3] = dx.getNgayDuyetTc();
            objs[4] = dx.getLoaiDc();
            objs[5] = dx.getTenDvi();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbQuyetDinhDcTcHdr> danhSachQuyetDinh(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcTc objReq) {
        String dvql = currentUser.getDvql();
        objReq.setMaDvi(dvql);
        List<DcnbQuyetDinhDcTcHdr> danhSachs = new ArrayList<>();
//        if (Contains.QD_NHAP.equals(objReq.getLoaiQdinh())) {
//            danhSachs = dcnbQuyetDinhDcTcHdrRepository.findDanhSachQuyetDinhNhan(objReq);
//        }
//        if (Contains.QD_XUAT.equals(objReq.getLoaiQdinh())) {
//            danhSachs = dcnbQuyetDinhDcTcHdrRepository.findDanhSachQuyetDinhXuat(objReq);
//        }
        if (objReq.getQDinhCucId() == null) {
            objReq.setQDinhCucId(-1l);
        }
        danhSachs = dcnbQuyetDinhDcTcHdrRepository.findDanhSachQuyetDinh(objReq);
        return danhSachs;
    }

    public List<DcnbQuyetDinhDcTcHdr> detailTheoTongHop(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbQuyetDinhDcTcHdr> optional = dcnbQuyetDinhDcTcHdrRepository.findByIdThopIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbQuyetDinhDcTcHdr> allById = dcnbQuyetDinhDcTcHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            List<FileDinhKem> quyetDinh = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_QUYET_DINH"));
            data.setQuyetDinh(quyetDinh);

            List<DcnbQuyetDinhDcTcDtl> sachQuyetDinh = data.getDanhSachQuyetDinh();
            sachQuyetDinh.forEach(data1 -> {
                data1.getDanhSachQuyetDinhChiTiet().forEach(data2 -> {
                    Optional<DcnbKeHoachDcHdr> hoachDcHdr = dcnbKeHoachDcHdrRepository.findById(data2.getKeHoachDcHdrId());
                    if (hoachDcHdr.isPresent()) {
                        data2.setDanhSachKeHoach(hoachDcHdr.get().getDanhSachHangHoa());
                        data2.setDcnbKeHoachDcHdr(hoachDcHdr.get());
                    }
                });
            });
        });
        return allById;
    }
}
