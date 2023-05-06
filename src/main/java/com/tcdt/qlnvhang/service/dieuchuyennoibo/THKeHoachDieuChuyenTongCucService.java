package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenNoiBoCucDtlReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucDtlReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucHdrReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
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

import javax.persistence.Transient;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class THKeHoachDieuChuyenTongCucService extends BaseServiceImpl {
    @Autowired
    private DcnbKeHoachDcHdrRepository dcHdrRepository;
    @Autowired
    private THKeHoachDieuChuyenTongCucHdrRepository tongCucHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenTongCucDtlRepository thKeHoachDieuChuyenTongCucDtlRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private THKeHoachDieuChuyenCucKhacCucDtlRepository thKeHoachDieuChuyenCucKhacCucDtlRepository;

    @Autowired
    private THKeHoachDieuChuyenCucHdrRepository thKeHoachDieuChuyenCucHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenNoiBoCucDtlRepository thKeHoachDieuChuyenNoiBoCucDtlRepository;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    public Page<THKeHoachDieuChuyenTongCucHdr> searchPage(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        req.setMaDVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<THKeHoachDieuChuyenTongCucHdr> search = tongCucHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr save(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        THKeHoachDieuChuyenTongCucHdr data = new THKeHoachDieuChuyenTongCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDVi(currentUser.getUser().getDvql());
        data.setTenDVi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.CHUATAO_QD);
        data.setNgayTongHop(objReq.getNgayTongHop());
        data.setThoiGianTongHop(objReq.getThoiGianTongHop());
        data.setLoaiHangHoa(objReq.getLoaiHangHoa());
        data.setChungLoaiHangHoa(objReq.getChungLoaiHangHoa());
        data.setTenLoaiHangHoa(objReq.getTenLoaiHangHoa());
        data.setNgaytao(LocalDate.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNamKeHoach(objReq.getNamKeHoach());
        data.setLoaiDieuChuyen(objReq.getLoaiDieuChuyen());
        List<THKeHoachDieuChuyenTongCucDtl> chiTiet = new ArrayList<>();
        TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
        objReq.setThKeHoachDieuChuyenTongCucDtls(createPlan(currentUser, tongHopSearch));
        if (objReq.getThKeHoachDieuChuyenTongCucDtls() != null && !objReq.getThKeHoachDieuChuyenTongCucDtls().isEmpty()) {
            for (ThKeHoachDieuChuyenTongCucDtlReq ct : objReq.getThKeHoachDieuChuyenTongCucDtls()) {
                THKeHoachDieuChuyenTongCucDtl ctTongHop = new THKeHoachDieuChuyenTongCucDtl();
                ObjectMapperUtils.map(ct, ctTongHop);
                chiTiet.add(ctTongHop);
            }
        }
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        if (!chiTiet.isEmpty()) {
            for (THKeHoachDieuChuyenTongCucDtl ct : chiTiet) {
                ct.setHdrId(created.getId());
            }
        }
        thKeHoachDieuChuyenTongCucDtlRepository.saveAll(chiTiet);

        return created;
    }

    public List<THKeHoachDieuChuyenTongCucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenTongCucHdr> allById = tongCucHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            data.getThKeHoachDieuChuyenTongCucDtls().forEach(data1 -> {
                Hibernate.initialize(data1.getThKeHoachDieuChuyenCucHdr());
                data1.getThKeHoachDieuChuyenCucHdr().getThKeHoachDieuChuyenCucKhacCucDtls().forEach(data2 ->{
                    List<Long> listId = Arrays.asList(data2.getDcnbKeHoachDcHdrId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                    data2.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                });
            });
        });
        return allById;
    }


    public List<THKeHoachDieuChuyenTongCucHdr> danhSachMaTongHop(TongHopKeHoachDieuChuyenSearch req) throws Exception{
        List<THKeHoachDieuChuyenTongCucHdr> danhSachMaTongHop = tongCucHdrRepository.filterMaTongHop(req);
        return danhSachMaTongHop;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
            Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(idSearchReq.getId());
            if (!optional.isPresent()) {
                throw new Exception("Bản ghi không tồn tại");
            }
            THKeHoachDieuChuyenTongCucHdr data = optional.get();
            List<THKeHoachDieuChuyenTongCucDtl> list = thKeHoachDieuChuyenTongCucDtlRepository.findByHdrId(data.getId());
            thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(list);
            tongCucHdrRepository.delete(data);
        }


    @Transient
    public void deleteMulti( IdSearchReq idSearchReq) throws Exception {
            List<THKeHoachDieuChuyenTongCucHdr> list = tongCucHdrRepository.findAllById(idSearchReq.getIds());
            if (list.isEmpty()) {
                throw new Exception("Bản ghi không tồn tại");
            }
            List<Long> listId = list.stream().map(THKeHoachDieuChuyenTongCucHdr::getId).collect(Collectors.toList());
            List<THKeHoachDieuChuyenTongCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenTongCucDtlRepository.findAllByHdrIdIn(listId);
            thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(listTongHopKeHoach);
            tongCucHdrRepository.deleteAll(list);
        }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr update(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        List<THKeHoachDieuChuyenTongCucHdr> maTongHop = tongCucHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (!maTongHop.isEmpty() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get(0).getId().equals(objReq.getId())) {
                throw new Exception("Mã tổng hợp đã tồn tại");
            }
        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        ObjectMapperUtils.map(objReq, data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(LocalDate.now());
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        tongCucHdrRepository.save(created);
        return created;
    }

    @Transactional
    public List<ThKeHoachDieuChuyenTongCucDtlReq> createPlan(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        LocalDateTime thoiGianTongHop = req.getThoiGianTongHop();
        List<ThKeHoachDieuChuyenTongCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                List<THKeHoachDieuChuyenCucHdr> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucHdrRepository.findByDonViAndTrangThaiTongCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, req.getLoaiHangHoa(), req.getChungLoaiHangHoa(), thoiGianTongHop.toLocalDate());
                for (THKeHoachDieuChuyenCucHdr entry : dcnbKeHoachDcHdrs) {
//                    Hibernate.initialize(entry.getThKeHoachDieuChuyenNoiBoCucDtls());
                    THKeHoachDieuChuyenCucHdr khhc = SerializationUtils.clone(entry);
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ModelMapper().map(khhc, ThKeHoachDieuChuyenTongCucDtlReq.class);
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    chiTiet.setThKhDcHdrId(khhc.getId());
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(req.getMaDVi(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, Contains.DADUYET_LDCC, req.getLoaiHangHoa(), req.getChungLoaiHangHoa(), req.getThoiGianTongHop());
                    chiTiet.setTongDuToanKp(tongDuToanKp);
                    result.add(chiTiet);
                }
            } else if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                List<THKeHoachDieuChuyenCucHdr> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucHdrRepository.findByDonViAndTrangThaiTongCucKhacCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CUC_DTNN_KV, req.getLoaiHangHoa(), req.getChungLoaiHangHoa(), thoiGianTongHop.toLocalDate());
                for (THKeHoachDieuChuyenCucHdr entry : dcnbKeHoachDcHdrs) {
                    Hibernate.initialize(entry.getThKeHoachDieuChuyenCucKhacCucDtls());
                    THKeHoachDieuChuyenCucHdr khhc = SerializationUtils.clone(entry);
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ModelMapper().map(khhc, ThKeHoachDieuChuyenTongCucDtlReq.class);
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    chiTiet.setThKhDcHdrId(khhc.getId());
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucCuc(req.getMaDVi(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CUC_DTNN_KV, Contains.DADUYET_LDCC, req.getLoaiHangHoa(), req.getChungLoaiHangHoa(), req.getThoiGianTongHop());
                    chiTiet.setTongDuToanKp(tongDuToanKp);
                    result.add(chiTiet);
                }
            }
        }
        return result;
    }
}

