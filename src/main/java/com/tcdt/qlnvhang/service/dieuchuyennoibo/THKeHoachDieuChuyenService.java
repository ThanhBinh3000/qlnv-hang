package com.tcdt.qlnvhang.service.dieuchuyennoibo;


import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.*;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
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
import javax.xml.bind.ValidationException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class THKeHoachDieuChuyenService extends BaseServiceImpl {

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


    public Page<THKeHoachDieuChuyenCucHdr> searchPageCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        req.setMaDVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
            Page<THKeHoachDieuChuyenCucHdr> search = thKeHoachDieuChuyenHdrRepository.search(req, pageable);
            return search;
    }

    public Page<THKeHoachDieuChuyenTongCucHdr> searchPageTongCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception{
        req.setMaDVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<THKeHoachDieuChuyenTongCucHdr> search = tongCucHdrRepository.search(req, pageable);
        return search;
    }

    public THKeHoachDieuChuyenCucHdr yeuCauXacDinhDiemNhap(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception{
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if(!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
            throw  new Exception("Chức năng chỉ dành cho cấp cục");
        }
        if (objReq.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)){
            throw new Exception("Chức năng chỉ dành cho tổng hợp giữa 2 chi cục trong cùng 1 cục và tổng hợp tất cả");
        }

        return null;
    }
    @Transactional
    public THKeHoachDieuChuyenCucHdr save(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        if(!objReq.getDaXdinhDiemNhap()){
//            throw new Exception("Chưa hoàn tất yêu cầu xác định điểm nhập");
//        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(objReq.getMaTongHop());
        if (optional.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            throw new Exception("số đề xuất đã tồn tại");
        }
        THKeHoachDieuChuyenCucHdr data = new THKeHoachDieuChuyenCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.DUTHAO);
        data.setNgaytao(new Date());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNamKeHoach(objReq.getNamKeHoach());
        data.setLoaiDieuChuyen(objReq.getLoaiDieuChuyen());
        if(Objects.equals(data.getLoaiDieuChuyen(), Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
            data.setDaXdinhDiemNhap(false);
            List<THKeHoachDieuChuyenNoiBoCucDtl> chiTiet = new ArrayList<>();
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            objReq.setCtTongHopKeHoachDieuChuyen(createPlanChiCuc(currentUser, tongHopSearch));
            if (objReq.getCtTongHopKeHoachDieuChuyen() != null && !objReq.getCtTongHopKeHoachDieuChuyen().isEmpty()) {
                for (ThKeHoachDieuChuyenNoiBoCucDtlReq ct : objReq.getCtTongHopKeHoachDieuChuyen()) {
                    THKeHoachDieuChuyenNoiBoCucDtl ctTongHop = new THKeHoachDieuChuyenNoiBoCucDtl();
                    ObjectMapperUtils.map(ct, ctTongHop);
                    chiTiet.add(ctTongHop);
                }
            }
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            if (!chiTiet.isEmpty()) {
                for (THKeHoachDieuChuyenNoiBoCucDtl ct : chiTiet) {
                    ct.setHdrId(created.getId());
                    ct.setDaXdinhDiemNhap(false);
                }
            }
            thKeHoachDieuChuyenNoiBoCucDtlRepository.saveAll(chiTiet);
            return created;
        }
        if (Objects.equals(data.getLoaiDieuChuyen(), Contains.GIUA_2_CUC_DTNN_KV)) {
            List<THKeHoachDieuChuyenCucKhacCucDtl> chiTiet = new ArrayList<>();
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            objReq.setCtTongHopKeHoachDieuChuyenKhacCuc(createPlanCuc(currentUser, tongHopSearch));
            if (objReq.getCtTongHopKeHoachDieuChuyenKhacCuc() != null && !objReq.getCtTongHopKeHoachDieuChuyenKhacCuc().isEmpty()) {
                for (ThKeHoachDieuChuyenKhacCucDtlReq ct1 : objReq.getCtTongHopKeHoachDieuChuyenKhacCuc()) {
                    THKeHoachDieuChuyenCucKhacCucDtl ctTongHop1 = new THKeHoachDieuChuyenCucKhacCucDtl();
                    ObjectMapperUtils.map(ct1, ctTongHop1);
                    chiTiet.add(ctTongHop1);
                }
            }
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            if (!chiTiet.isEmpty()) {
                for (THKeHoachDieuChuyenCucKhacCucDtl ct : chiTiet) {
                    ct.setHdrId(created.getId());
                }
            }
            thKeHoachDieuChuyenCucKhacCucDtlRepository.saveAll(chiTiet);
            return created;
        }
        if (Objects.equals(data.getLoaiDieuChuyen(), Contains.TAT_CA)) {
            List<THKeHoachDieuChuyenNoiBoCucDtl> chiTiet = new ArrayList<>();
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            objReq.setCtTongHopKeHoachDieuChuyen(createPlanChiCuc(currentUser, tongHopSearch));
            if (objReq.getCtTongHopKeHoachDieuChuyen() != null && !objReq.getCtTongHopKeHoachDieuChuyen().isEmpty()) {
                for (ThKeHoachDieuChuyenNoiBoCucDtlReq ct : objReq.getCtTongHopKeHoachDieuChuyen()) {
                    THKeHoachDieuChuyenNoiBoCucDtl ctTongHop = new THKeHoachDieuChuyenNoiBoCucDtl();
                    ObjectMapperUtils.map(ct, ctTongHop);
                    chiTiet.add(ctTongHop);
                }
            }
            List<THKeHoachDieuChuyenCucKhacCucDtl> chiTiet1 = new ArrayList<>();
            TongHopKeHoachDieuChuyenSearch tongHopSearch1 = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            objReq.setCtTongHopKeHoachDieuChuyenKhacCuc(createPlanCuc(currentUser, tongHopSearch1));
            if (objReq.getCtTongHopKeHoachDieuChuyenKhacCuc() != null && !objReq.getCtTongHopKeHoachDieuChuyenKhacCuc().isEmpty()) {
                for (ThKeHoachDieuChuyenKhacCucDtlReq ct1 : objReq.getCtTongHopKeHoachDieuChuyenKhacCuc()) {
                    THKeHoachDieuChuyenCucKhacCucDtl ctTongHop1 = new THKeHoachDieuChuyenCucKhacCucDtl();
                    ObjectMapperUtils.map(ct1, ctTongHop1);
                    chiTiet1.add(ctTongHop1);
                }
            }
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            if (!chiTiet1.isEmpty()) {
                for (THKeHoachDieuChuyenCucKhacCucDtl ct1 : chiTiet1) {
                    ct1.setHdrId(created.getId());
                }
            }
            if (!chiTiet.isEmpty()) {
                for (THKeHoachDieuChuyenNoiBoCucDtl ct : chiTiet) {
                    ct.setHdrId(created.getId());
                }
            }
            thKeHoachDieuChuyenNoiBoCucDtlRepository.saveAll(chiTiet);
            thKeHoachDieuChuyenCucKhacCucDtlRepository.saveAll(chiTiet1);
            return created;
        }

        return null;
    }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr saveTongCuc(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findByMaTongHop(objReq.getMaTongHop());
        if (optional.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            throw new Exception("số đề xuất đã tồn tại");
        }
        THKeHoachDieuChuyenTongCucHdr data = new THKeHoachDieuChuyenTongCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDVi(currentUser.getUser().getDvql());
        data.setTenDVi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.CHUATAO_QD);
        data.setLoaiHangHoa(objReq.getLoaiHangHoa());
        data.setChungLoaiHangHoa(objReq.getChungLoaiHangHoa());
        data.setTenLoaiHangHoa(objReq.getTenLoaiHangHoa());
        data.setNgaytao(new Date());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNamKeHoach(objReq.getNamKeHoach());
        data.setLoaiDieuChuyen(objReq.getLoaiDieuChuyen());
            List<THKeHoachDieuChuyenTongCucDtl> chiTiet = new ArrayList<>();
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            objReq.setCtTongHopKeHoachDieuChuyen(createPlanTongCuc(currentUser, tongHopSearch));
            if (objReq.getCtTongHopKeHoachDieuChuyen() != null && !objReq.getCtTongHopKeHoachDieuChuyen().isEmpty()) {
                for (ThKeHoachDieuChuyenTongCucDtlReq ct : objReq.getCtTongHopKeHoachDieuChuyen()) {
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

    public List<THKeHoachDieuChuyenCucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenCucHdr> allById = thKeHoachDieuChuyenHdrRepository.findAllById(ids);
        allById.forEach(data -> {
                List<THKeHoachDieuChuyenCucKhacCucDtl> list = thKeHoachDieuChuyenCucKhacCucDtlRepository.findByHdrId(data.getId());
                data.setThKeHoachDieuChuyenCucKhacCucDtls(list);
                list.forEach(data1 -> {
                    List<DcnbKeHoachDcDtl> list1 = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data1.getDcnbKeHoachDcHdrId());
                    data1.setDcnbKeHoachDcDtlList(list1);
                });

                List<THKeHoachDieuChuyenNoiBoCucDtl> list1 = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
                data.setThKeHoachDieuChuyenNoiBoCucDtls(list1);
                list1.forEach(data1 -> {
                    List<DcnbKeHoachDcDtl> list2 = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndId(data1.getDcKeHoachDcHdrId(), data1.getDcKeHoachDcDtlId());
                    data1.setDcnbKeHoachDcDtlList(list2);
                });
        });
        return allById;
    }

    public List<THKeHoachDieuChuyenTongCucHdr> detailTongCuc(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenTongCucHdr> allById = tongCucHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<THKeHoachDieuChuyenTongCucDtl> list = thKeHoachDieuChuyenTongCucDtlRepository.findByHdrId(data.getId());
            data.setThKeHoachDieuChuyenTongCucDtls(list);
            list.forEach(data1 -> {
                List<DcnbKeHoachDcDtl> list1 = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data1.getKeHoachDcHdrId());
                data1.setDcnbKeHoachDcDtlList(list1);
            });
        });
        return allById;
    }


    @Transient
    public void delete(CustomUserDetails currentUser,IdSearchReq idSearchReq) throws Exception {
        if(currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
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
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(idSearchReq.getId());
            if (!optional.isPresent()) {
                throw new Exception("Bản ghi không tồn tại");
            }
            THKeHoachDieuChuyenTongCucHdr data = optional.get();
            List<THKeHoachDieuChuyenTongCucDtl> list = thKeHoachDieuChuyenTongCucDtlRepository.findByHdrId(data.getId());
            thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(list);
            tongCucHdrRepository.delete(data);
        }
    }

    @Transient
    public void deleteMulti(CustomUserDetails currentUser,IdSearchReq idSearchReq) throws Exception {
        if(currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            List<THKeHoachDieuChuyenCucHdr> list = thKeHoachDieuChuyenHdrRepository.findAllById(idSearchReq.getIds());
            if (list.isEmpty()) {
                throw new Exception("Bản ghi không tồn tại");
            }
            List<Long> listId = list.stream().map(THKeHoachDieuChuyenCucHdr::getId).collect(Collectors.toList());
            List<THKeHoachDieuChuyenNoiBoCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenNoiBoCucDtlRepository.findAllByHdrIdIn(listId);
            thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(listTongHopKeHoach);
            List<THKeHoachDieuChuyenCucKhacCucDtl> list1 = thKeHoachDieuChuyenCucKhacCucDtlRepository.findAllByHdrIdIn(listId);
            thKeHoachDieuChuyenCucKhacCucDtlRepository.deleteAll(list1);
            thKeHoachDieuChuyenHdrRepository.deleteAll(list);
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            List<THKeHoachDieuChuyenTongCucHdr> list = tongCucHdrRepository.findAllById(idSearchReq.getIds());
            if (list.isEmpty()) {
                throw new Exception("Bản ghi không tồn tại");
            }
            List<Long> listId = list.stream().map(THKeHoachDieuChuyenTongCucHdr::getId).collect(Collectors.toList());
            List<THKeHoachDieuChuyenTongCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenTongCucDtlRepository.findAllByHdrIdIn(listId);
            thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(listTongHopKeHoach);
            tongCucHdrRepository.deleteAll(list);
        }
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
        Optional<THKeHoachDieuChuyenCucHdr> maTongHop = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (maTongHop.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get().getId().equals(objReq.getId())) {
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        ObjectMapperUtils.map(objReq,data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(new Date());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
        thKeHoachDieuChuyenHdrRepository.save(created);
        return created;
    }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr updateTongCuc(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<THKeHoachDieuChuyenTongCucHdr> maTongHop = tongCucHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (maTongHop.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get().getId().equals(objReq.getId())) {
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        ObjectMapperUtils.map(objReq,data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(new Date());
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        tongCucHdrRepository.save(created);
        return created;
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
        Optional<THKeHoachDieuChuyenTongCucHdr> maTongHop = tongCucHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (maTongHop.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get().getId().equals(objReq.getId())) {
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        ObjectMapperUtils.map(objReq,data);
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(new Date());
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        tongCucHdrRepository.save(created);
        return created;
    }
    @Transactional
    public THKeHoachDieuChuyenCucHdr approveTongHop(CustomUserDetails currentUser, StatusReq statusReq, Optional<THKeHoachDieuChuyenCucHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai() ;
        switch (status) {
            case  Contains.DUTHAO + Contains.CHODUYET_TP:
                optional.get().setNguoiGDuyetId(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(new Date());
                break;
            case Contains.CHODUYET_TP + Contains.TU_CHOI_TP:
                optional.get().setTrangThai(Contains.DUTHAO);
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(new Date());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.TU_CHOI_LDC:
                optional.get().setTrangThai(Contains.DUTHAO);
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(new Date());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(new Date());
                break;
            case Contains.CHODUYET_LDC + Contains.DA_DUYET_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(new Date());
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
    public List<ThKeHoachDieuChuyenNoiBoCucDtlReq> createPlanChiCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception{
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(req.getMaTongHop());
        if (optional.isPresent()) {
            throw new Exception("Mã tổng hợp đã tồn tại");
        }
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(),"01");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<ThKeHoachDieuChuyenNoiBoCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
                req.setMaDVi(cqt.getMaDvi());
                List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenNoiBoCucDtls = dcnbKeHoachDcDtlRepository.findByDonViAndTrangThaiChiCuc(req.getMaDVi(), TrangThaiAllEnum.DA_DUYET_LDCC.getId(), formatter.format(req.getThoiGianTongHop()));
                for (DcnbKeHoachDcDtl entry : thKeHoachDieuChuyenNoiBoCucDtls) {
                    ThKeHoachDieuChuyenNoiBoCucDtlReq chiTiet = new ThKeHoachDieuChuyenNoiBoCucDtlReq();
                    chiTiet.setMaChiCucDxuat(req.getMaDVi());
                    chiTiet.setTenChiCucDxuat(cqt.getTenDvi());
                    chiTiet.setDcKeHoachDcDtlId(entry.getId());
                    chiTiet.setDcKeHoachDcHdrId(entry.getDcnbKeHoachDcHdr().getId());
                    chiTiet.setDcnbKeHoachDcDtls(thKeHoachDieuChuyenNoiBoCucDtls);
                    result.add(chiTiet);
                }
            }
        return result;
    }

    @Transactional
    public List<ThKeHoachDieuChuyenKhacCucDtlReq> createPlanCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(req.getMaTongHop());
        if (optional.isPresent()) {
            throw new Exception("Mã tổng hợp đã tồn tại");
        }
            List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            List<ThKeHoachDieuChuyenKhacCucDtlReq> result = new ArrayList<>();
            for (QlnvDmDonvi cqt : donvis) {
                req.setMaDVi(cqt.getMaDvi());
                List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = dcHdrRepository.findByDonViAndTrangThaiCuc(req.getMaDVi(), TrangThaiAllEnum.DA_DUYET_LDCC.getId(), Contains.GIUA_2_CUC_DTNN_KV, formatter.format(req.getThoiGianTongHop()));
                for (DcnbKeHoachDcHdr entry : dcnbKeHoachDcHdrs) {
                    ThKeHoachDieuChuyenKhacCucDtlReq chiTiet = new ThKeHoachDieuChuyenKhacCucDtlReq();
                    chiTiet.setDcnbKeHoachDcHdrId(entry.getId());
                    chiTiet.setMaCucNhan(entry.getMaCucNhan());
                    chiTiet.setTenCucNhan(entry.getTenCucNhan());
                    chiTiet.setSoDxuat(entry.getSoDxuat());
                    chiTiet.setNgayDxuat(entry.getNgayLapKh());
                    chiTiet.setMaChiCucDeXuat(cqt.getMaDvi());
                    chiTiet.setTenChiCucDxuat(entry.getTenDvi());
                    chiTiet.setNgayGduyetTc(null);
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDc(req.getMaDVi(),currentUser.getDvql(),entry.getMaCucNhan(),formatter.format(req.getThoiGianTongHop()));
                    chiTiet.setTongDuToanKp(tongDuToanKp);
                    chiTiet.setTrichYeu(entry.getTrichYeu());
                    List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(chiTiet.getDcnbKeHoachDcHdrId());
                    chiTiet.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                    result.add(chiTiet);
                }
            }
        return result;
        }
    @Transactional
    public List<ThKeHoachDieuChuyenTongCucDtlReq> createPlanTongCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception{
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findByMaTongHop(req.getMaTongHop());
        if (optional.isPresent()) {
            throw new Exception("Mã tổng hợp đã tồn tại");
        }
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(),"01");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<ThKeHoachDieuChuyenTongCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            if(req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)){
            List<THKeHoachDieuChuyenCucHdr> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenHdrRepository.findByDonViAndTrangThaiTongCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, req.getLoaiHangHoa(),req.getChungLoaiHangHoa(), formatter.format(req.getThoiGianTongHop()));
            for (THKeHoachDieuChuyenCucHdr entry : dcnbKeHoachDcHdrs) {
                ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ThKeHoachDieuChuyenTongCucDtlReq();
                chiTiet.setMaCucDxuatDc(entry.getMaDvi());
                chiTiet.setTenCucDxuatDc(entry.getTenDvi());
                chiTiet.setKeHoachDcHdrId(entry.getId());
                chiTiet.setSoDxuat(entry.getSoDeXuat());
                chiTiet.setTrichYeu(entry.getTrichYeu());
                Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(req.getMaDVi(),Contains.DIEU_CHUYEN,Contains.GIUA_2_CHI_CUC_TRONG_1_CUC,Contains.DADUYET_LDCC,req.getLoaiHangHoa(),req.getChungLoaiHangHoa(),formatter.format(req.getThoiGianTongHop()));
                chiTiet.setDuToanKp(tongDuToanKp);
                List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndLoaiHhAndCLoaiHh(chiTiet.getKeHoachDcHdrId(),req.getLoaiHangHoa(),req.getChungLoaiHangHoa());
                chiTiet.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                result.add(chiTiet);
            }
                return result;
        } else if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = dcHdrRepository.findByDonViAndTrangThaiTongCuc(req.getMaDVi(), TrangThaiAllEnum.DA_DUYET_LDCC.getId(), Contains.GIUA_2_CUC_DTNN_KV,req.getLoaiHangHoa(),req.getChungLoaiHangHoa(), formatter.format(req.getThoiGianTongHop()));
                for (DcnbKeHoachDcHdr entry : dcnbKeHoachDcHdrs) {
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ThKeHoachDieuChuyenTongCucDtlReq();
                    chiTiet.setMaCucDxuatDc(entry.getMaDviCuc());
                    chiTiet.setTenCucDxuatDc(entry.getTenDviCuc());
                    chiTiet.setMaCucNhanDc(entry.getMaCucNhan());
                    chiTiet.setTenCucNhanDc(entry.getTenCucNhan());
                    chiTiet.setKeHoachDcHdrId(entry.getId());
                    chiTiet.setSoDxuat(entry.getSoDxuat());
                    chiTiet.setTrichYeu(entry.getTrichYeu());
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucCuc(req.getMaDVi(),entry.getMaCucNhan(),Contains.DIEU_CHUYEN,Contains.GIUA_2_CHI_CUC_TRONG_1_CUC,Contains.DADUYET_LDCC,req.getLoaiHangHoa(),req.getChungLoaiHangHoa(),formatter.format(req.getThoiGianTongHop()));
                    chiTiet.setDuToanKp(tongDuToanKp);
                    List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndLoaiHhAndCLoaiHh(chiTiet.getKeHoachDcHdrId(),req.getLoaiHangHoa(),req.getChungLoaiHangHoa());
                    chiTiet.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                    result.add(chiTiet);
                }
                return result;
            }
        }
        return null;
    }
}



