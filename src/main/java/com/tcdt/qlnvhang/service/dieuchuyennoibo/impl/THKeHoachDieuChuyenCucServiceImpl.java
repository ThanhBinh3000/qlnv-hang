package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;


import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtlReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenCucHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenNoiBoCucDtlReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class THKeHoachDieuChuyenCucServiceImpl extends BaseServiceImpl {

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
    private DcnbKeHoachDcHdrRepository dcHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;



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

    @Transactional
    public THKeHoachDieuChuyenCucHdr yeuCauXacDinhDiemNhap(CustomUserDetails currentUser, IdSearchReq idSearchReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp cục");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(idSearchReq.getId());
        if (optional.isPresent() && (optional.get().getTrangThai().equals(Contains.DUTHAO)|| optional.get().getTrangThai().equals(Contains.YC_CHICUC_PHANBO_DC))){
            THKeHoachDieuChuyenCucHdr data = optional.get();
            data.setTrangThai(Contains.YC_CHICUC_PHANBO_DC);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            for (THKeHoachDieuChuyenNoiBoCucDtl boCucDtl : created.getThKeHoachDieuChuyenNoiBoCucDtls()) {
                for(DcnbKeHoachDcDtl dcnbKeHoachDcDtl : boCucDtl.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                    dcHdrRepository.updateTrangThaiNdc(dcnbKeHoachDcDtl.getId(), Contains.NHAN_DIEU_CHUYEN, Contains.YC_CHICUC_PHANBO_DC);
                }
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
                dtl.setHdrId(data.getId());
                dtls.add(dtl);
            }
            if(dtls.isEmpty()){
                throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
            }
            dtls.forEach(e -> e.setTHKeHoachDieuChuyenCucHdr(data));
            data.setThKeHoachDieuChuyenNoiBoCucDtls(dtls);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            // Update Id tổng hợp, mã tổng hợp kế hoạch điều chuyển
            if (created.getId() > 0 && created.getThKeHoachDieuChuyenNoiBoCucDtls().size() > 0) {
                List<Long> danhSachKeHoach = created.getThKeHoachDieuChuyenNoiBoCucDtls().stream().map(THKeHoachDieuChuyenNoiBoCucDtl::getDcKeHoachDcHdrId)
                        .collect(Collectors.toList());
                dcHdrRepository.updateIdTongHop(created.getId(),danhSachKeHoach);
            }
            created.setMaTongHop(created.getId());
            THKeHoachDieuChuyenCucHdr createdSave = thKeHoachDieuChuyenHdrRepository.save(created);
            List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU");
            createdSave.setCanCu(canCu);
            return createdSave;
        } else if (Objects.equals(data.getLoaiDieuChuyen(), Contains.GIUA_2_CUC_DTNN_KV)) {
            TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
            List<THKeHoachDieuChuyenCucKhacCucDtlReq> planCuc = createPlanCuc(currentUser, tongHopSearch);
            List<THKeHoachDieuChuyenCucKhacCucDtl> dtls = new ArrayList<>();
            for (THKeHoachDieuChuyenCucKhacCucDtlReq req : planCuc) {
                THKeHoachDieuChuyenCucKhacCucDtl dtl = new THKeHoachDieuChuyenCucKhacCucDtl();
                BeanUtils.copyProperties(req,dtl);
                dtl.setHdrId(data.getId());
                dtls.add(dtl);
            }
            if(dtls.isEmpty()){
                throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
            }
            dtls.forEach(e -> e.setTHKeHoachDieuChuyenCucHdr(data));
            data.setThKeHoachDieuChuyenCucKhacCucDtls(dtls);
            THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
            // Update Id tổng hợp, mã tổng hợp kế hoạch điều chuyển
            if (created.getId() > 0 && created.getThKeHoachDieuChuyenCucKhacCucDtls().size() > 0) {
                created.getThKeHoachDieuChuyenCucKhacCucDtls().forEach(dataHdrId -> {
                    List<Long> danhSachKeHoach = Arrays.stream(dataHdrId.getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    dcHdrRepository.updateIdTongHop(created.getId(),danhSachKeHoach);
                });
            }
            created.setMaTongHop(created.getId());
            THKeHoachDieuChuyenCucHdr createdSave = thKeHoachDieuChuyenHdrRepository.save(created);
            List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU");
            createdSave.setCanCu(canCu);
            return createdSave;
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
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            data.getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data1 -> {
                Hibernate.initialize(data1.getDcnbKeHoachDcHdr());
            });
            data.getThKeHoachDieuChuyenCucKhacCucDtls().forEach(data2 ->{
                List<Long> listId = Arrays.stream(data2.getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                data2.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
            });
        });
        return allById;
    }

    public List<String> danhSachChiCucNhanDc(TongHopKeHoachDieuChuyenSearch req) throws Exception{
        List<String> ltStr = new ArrayList<>();
        List<Object[]> danhSachChiCucNhanDc = dcnbKeHoachDcDtlRepository.findByDcnbKhHdrId(req.getId());
        danhSachChiCucNhanDc.forEach(item ->{
            ltStr.add(item[0].toString());
        });
        return ltStr;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        //Xóa tổng hợp kế hoạch nội bộ cục
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU"));
        List<THKeHoachDieuChuyenNoiBoCucDtl> list = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
        if(data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
            list.forEach(e ->{
                e.getDcnbKeHoachDcHdr().setIdThop(null);
                e.getDcnbKeHoachDcHdr().setMaThop(null);
                dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
            });
            thKeHoachDieuChuyenNoiBoCucDtlRepository.saveAll(list);
        }
        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(list);
        //Xóa tổng hợp kế hoạch khác cục
        List<THKeHoachDieuChuyenCucKhacCucDtl> list1 = thKeHoachDieuChuyenCucKhacCucDtlRepository.findByHdrId(data.getId());
        if(data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
            list1.forEach(dataHdrId -> {
                List<Long> danhSachKeHoach = Arrays.stream(dataHdrId.getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(danhSachKeHoach);
                dcnbKeHoachDcHdr.forEach(e ->{
                    e.setMaThop(null);
                    e.setIdThop(null);
                    dcHdrRepository.save(e);
                });
            });
            thKeHoachDieuChuyenCucKhacCucDtlRepository.saveAll(list1);
        }
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
        fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU"));
        //Xóa tổng hợp kế hoạch nội bộ cục
        List<THKeHoachDieuChuyenNoiBoCucDtl> listTongHopKeHoachChiCuc = thKeHoachDieuChuyenNoiBoCucDtlRepository.findAllByHdrIdIn(listId);

        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(listTongHopKeHoachChiCuc);
        listTongHopKeHoachChiCuc.forEach(e->{
             e.getDcnbKeHoachDcHdr().setIdThop(null);
             e.getDcnbKeHoachDcHdr().setMaThop(null);
             dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
        });
        thKeHoachDieuChuyenNoiBoCucDtlRepository.saveAll(listTongHopKeHoachChiCuc);
        //Xóa tổng hợp kế hoạch khác cục
        List<THKeHoachDieuChuyenCucKhacCucDtl> listTongHopKeHoachKhacCuc = thKeHoachDieuChuyenCucKhacCucDtlRepository.findAllByHdrIdIn(listId);
        listTongHopKeHoachKhacCuc.forEach(dataHdrId -> {
            List<Long> danhSachKeHoach = Arrays.stream(dataHdrId.getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(danhSachKeHoach);
            dcnbKeHoachDcHdr.forEach(e ->{
                e.setMaThop(null);
                e.setIdThop(null);
                dcHdrRepository.save(e);
            });
        });
        thKeHoachDieuChuyenCucKhacCucDtlRepository.saveAll(listTongHopKeHoachKhacCuc);
        thKeHoachDieuChuyenCucKhacCucDtlRepository.deleteAll(listTongHopKeHoachKhacCuc);
        thKeHoachDieuChuyenHdrRepository.deleteAll(list);
    }


    public void approve(CustomUserDetails currentUser,TongHopKeHoachDieuChuyenSearch statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(statusReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenCucHdr> soDeXuat = thKeHoachDieuChuyenHdrRepository.findBySoDeXuat(optional.get().getSoDeXuat());
        if(soDeXuat.get(0).getSoDeXuat() == null){
            throw new Exception("Bản tổng hợp chưa có số đề xuất");
        }
        if (!soDeXuat.isEmpty() && optional.get().getSoDeXuat().split("/").length == 1) {
           if (soDeXuat.get(0).getId().equals(optional.get().getId())) {
             throw new Exception("Số đề xuất đã tồn tại");
          }
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
        if(!optional.get().getTrangThai().equals(Contains.DUTHAO) && !optional.get().getTrangThai().equals(Contains.YC_CHICUC_PHANBO_DC)){
            throw new Exception("Không được phép chỉnh sửa dữ liệu");
        }
//        List<THKeHoachDieuChuyenCucHdr> soDeXuat = thKeHoachDieuChuyenHdrRepository.findBySoDeXuat(optional.get().getSoDeXuat());
//        if (!soDeXuat.isEmpty() && objReq.getSoDeXuat().split("/").length == 1) {
//            if (soDeXuat.get(0).getId().equals(objReq.getId())) {
//                throw new Exception("Số đề xuất đã tồn tại");
//            }
//        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        THKeHoachDieuChuyenCucHdr dataMap = new ModelMapper().map(objReq, THKeHoachDieuChuyenCucHdr.class);
        BeanUtils.copyProperties(data,dataMap);
        dataMap.setSoDeXuat(objReq.getSoDeXuat());
        dataMap.setTrichYeu(objReq.getTrichYeu());
        data.setNguoiSuaId(currentUser.getUser().getId());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(dataMap);
        created = thKeHoachDieuChuyenHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), THKeHoachDieuChuyenCucHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }

    @Transactional
    public THKeHoachDieuChuyenCucHdr approveTongHop(CustomUserDetails currentUser,TongHopKeHoachDieuChuyenSearch statusReq, Optional<THKeHoachDieuChuyenCucHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.YC_CHICUC_PHANBO_DC + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_TP + Contains.CHODUYET_TP:
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    if(Contains.TU_CHOI_TP.equals(optional.get().getTrangThai())) {
                        throw new Exception("Luồng giữa 2 cục dự trữ nhà nước khu vực đã hết luồng!");
                    }
                }
                optional.get().setNguoiGDuyetId(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(LocalDate.now());
                // CHECK TRẠNG THÁI BẢN KẾ HOẠCH ĐÃ XACS ĐỊNH ĐIỂM NHẬP HỢP LỆ CHƯA. (LDCC DA DUYET, DA_PHANBO_DC_CHODUYET_TP, DA_PHANBO_DC_CHODUYET_LDC)
                // Update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái DA_PHANBO_DC_CHODUYET_TP
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data -> {
                        if(!data.getDcnbKeHoachDcHdr().getTrangThai().equals(Contains.DADUYET_LDCC) && !data.getDcnbKeHoachDcHdr().getTrangThai().equals(Contains.DA_PHANBO_DC_CHODUYET_TP) && !data.getDcnbKeHoachDcHdr().getTrangThai().equals(Contains.DA_PHANBO_DC_CHODUYET_LDC)){
                            try {
                                throw new Exception("Trạng thái kế hoạch không hợp lệ");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndType(data.getDcKeHoachDcHdrId(),Contains.NHAN_DIEU_CHUYEN);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(false);
                            e.setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_CHODUYET_TP);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                break;
            case Contains.CHODUYET_TP + Contains.TU_CHOI_TP:
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                // update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái  DA_PHANBO_DC_TUCHOI_TP
                // LẤY RA THẰNG NÀO BỊ TỪ CHỐI? vd CHI CỤC a
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data ->{
                        List<String> danhSachKeHoach = Arrays.asList(statusReq.getMaChiCucNhan().split(","));
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByChiCucNhan(danhSachKeHoach,data.getDcKeHoachDcHdrId());
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(true);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_TUCHOI_TP);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(true);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    optional.get().getThKeHoachDieuChuyenCucKhacCucDtls().forEach(data ->{
                        String[] keHoachIdArrs = data.getDcnbKeHoachDcHdrId().split(",");
                        List<String> keHoachIds = Arrays.asList(keHoachIdArrs);
                        ArrayList<Long> ids = new ArrayList<>();
                        for (String str : keHoachIds) {
                            long value = Long.parseLong(str);
                            ids.add(value);
                        }
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByIdIn(ids);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(true);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.TUCHOI_TP);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(true);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                break;
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_TP:
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    if(Contains.TU_CHOI_LDC.equals(optional.get().getTrangThai())) {
                        throw new Exception("Luồng giữa 2 cục dự trữ nhà nước khu vực đã hết luồng!");
                    }
                }
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                // update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái DA_PHANBO_DC_CHODUYET_TP
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data -> {
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndType(data.getDcKeHoachDcHdrId(),Contains.NHAN_DIEU_CHUYEN);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_CHODUYET_TP);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
            case Contains.CHODUYET_LDC + Contains.TU_CHOI_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                // update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái DA_PHANBO_DC_TUCHOI_LDC
                // LẤY RA THẰNG NÀO BỊ TỪ CHỐI? vd CHI CỤC a
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data ->{
                        List<String> danhSachKeHoach = Arrays.asList(statusReq.getMaChiCucNhan().split(","));
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByChiCucNhan(danhSachKeHoach,data.getDcKeHoachDcHdrId());
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(true);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(true);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_TUCHOI_LDC);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    optional.get().getThKeHoachDieuChuyenCucKhacCucDtls().forEach(data ->{
                        String[] keHoachIdArrs = data.getDcnbKeHoachDcHdrId().split(",");
                        List<String> keHoachIds = Arrays.asList(keHoachIdArrs);
                        ArrayList<Long> ids = new ArrayList<>();
                        for (String str : keHoachIds) {
                            long value = Long.parseLong(str);
                            ids.add(value);
                        }
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByIdIn(ids);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(true);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.TUCHOI_TP);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(true);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetTpId(currentUser.getUser().getId());
                optional.get().setNgayDuyetTp(LocalDate.now());
                // update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái DA_PHANBO_DC_CHODUYET_LDC
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data -> {
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndType(data.getDcKeHoachDcHdrId(),Contains.NHAN_DIEU_CHUYEN);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_CHODUYET_LDC);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
                break;
            case Contains.CHODUYET_LDC + Contains.DA_DUYET_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(LocalDate.now());
                // update trạng thái của kế hoạch hdr của bản tổng hợp này về trạng thái DA_PHANBO_DC_DADUYET_LDC
                if(optional.get().getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    optional.get().getThKeHoachDieuChuyenNoiBoCucDtls().forEach(data -> {
                        List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdAndType(data.getDcKeHoachDcHdrId(),Contains.NHAN_DIEU_CHUYEN);
                        dcnbKeHoachDcDtl.forEach(e -> {
                            e.setXdLaiDiemNhap(false);
                            e.setXdLaiDiemNhap(false);
                            e.getDcnbKeHoachDcHdr().setTrangThai(Contains.DA_PHANBO_DC_DADUYET_LDC);
                            dcHdrRepository.save(e.getDcnbKeHoachDcHdr());
                        });
                    });
                }
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
        String title = "Danh sách tổng hợp kế hoạch điều chuyển";
        String[] rowsName = new String[]{"STT", "Năm KH", "Mã tổng hợp", "Loại điều chuyển", "Ngày tổng hợp", "Nội dung tổng hợp", "Trạng thái",};
        String fileName = "danh-sach-tong-hop-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            THKeHoachDieuChuyenCucHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getMaTongHop();
            objs[3] = dx.getLoaiDieuChuyen();
            objs[4] = dx.getNgayTongHop();
            objs[5] = dx.getTrichYeu();
            objs[6] = dx.getTrangThai();
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
            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = dcHdrRepository.findByDonViAndTrangThaiCucChiCuc(req.getMaDVi(), Contains.DADUYET_LDCC, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, Contains.DIEU_CHUYEN,req.getThoiGianTongHop());
            for(DcnbKeHoachDcHdr khh : dcnbKeHoachDcHdrs){
                Hibernate.initialize(khh.getDanhSachHangHoa());
                DcnbKeHoachDcHdr khhc = SerializationUtils.clone(khh);
                ThKeHoachDieuChuyenNoiBoCucDtlReq dtl = new ModelMapper().map(khhc, ThKeHoachDieuChuyenNoiBoCucDtlReq.class);
                dtl.setId(null);
                dtl.setDcKeHoachDcHdrId(khhc.getId());
                dtl.setHdrId(null);
                List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(dtl.getDcKeHoachDcHdrId());
                dtl.setDcnbKeHoachDcDtls(dcnbKeHoachDcDtls);
                result.add(dtl);
            }
        }
        if(result.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }
        return result;
    }

    public List<THKeHoachDieuChuyenCucKhacCucDtlReq> createPlanCuc(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        List<THKeHoachDieuChuyenCucKhacCucDtlReq> result = new ArrayList<>();
            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = dcHdrRepository.findByDonViAndTrangThaiCucCuc(currentUser.getDvql(), Contains.DADUYET_LDCC, Contains.GIUA_2_CUC_DTNN_KV, Contains.DIEU_CHUYEN,req.getThoiGianTongHop());
            Map<String, List<DcnbKeHoachDcHdr>> postsPerType = dcnbKeHoachDcHdrs.stream()
                    .collect(groupingBy(DcnbKeHoachDcHdr::getMaCucNhan));
            for (Map.Entry<String, List<DcnbKeHoachDcHdr>> entry : postsPerType.entrySet()) {
                THKeHoachDieuChuyenCucKhacCucDtlReq dtl = new THKeHoachDieuChuyenCucKhacCucDtlReq();
                dtl.setMaCucNhan(entry.getKey());
                List<DcnbKeHoachDcHdr> khhc = entry.getValue();
                dtl.setTenCucNhan(khhc.get(0).getTenCucNhan());
                List<Long> listId = khhc.stream().map(DcnbKeHoachDcHdr::getId).collect(Collectors.toList());
                String idString = listId.stream().map(Objects::toString).collect(Collectors.joining(","));
                dtl.setNgayGduyetTc(LocalDate.now());
                dtl.setDcnbKeHoachDcHdrId(idString);
                dtl.setDcnbKeHoachDcHdrs(khhc);
                dtl.setId(null);
                dtl.setHdrId(null);
//            for (DcnbKeHoachDcHdr khh : dcnbKeHoachDcHdrs) {
//                Hibernate.initialize(khh.getDanhSachHangHoa());
//                DcnbKeHoachDcHdr khhc = SerializationUtils.clone(khh);
//                THKeHoachDieuChuyenCucKhacCucDtlReq dtl = new ModelMapper().map(khhc, THKeHoachDieuChuyenCucKhacCucDtlReq.class);
//                dtl.setId(null);
//                dtl.setDcnbKeHoachDcHdrId(khh.getId());
//                dtl.setHdrId(null);
                Long tongDuToanKinhPhi = dcnbKeHoachDcDtlRepository.findByMaDviCucAndCucNhan(currentUser.getDvql(),dtl.getMaCucNhan(),Contains.DADUYET_LDCC,Contains.GIUA_2_CUC_DTNN_KV,Contains.DIEU_CHUYEN,req.getThoiGianTongHop(),listId);
                dtl.setTongDuToanKinhPhi((tongDuToanKinhPhi == null) ? 0 : tongDuToanKinhPhi);
                result.add(dtl);
            }
        if(result.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }
        return result;
    }
}



