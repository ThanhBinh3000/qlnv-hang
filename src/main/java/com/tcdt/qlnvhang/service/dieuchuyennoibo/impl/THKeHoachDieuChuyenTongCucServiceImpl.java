package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucDtlReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucHdrReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class THKeHoachDieuChuyenTongCucServiceImpl extends BaseServiceImpl {
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
    @Autowired
    private FileDinhKemService fileDinhKemService;

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
        data.setThKeHoachDieuChuyenTongCucDtls(chiTiet);
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        if (created.getId() > 0) {
            List<Long> danhSachKeHoach = chiTiet.stream().map(THKeHoachDieuChuyenTongCucDtl::getThKhDcHdrId)
                    .collect(Collectors.toList());
            thKeHoachDieuChuyenCucHdrRepository.updateIdTongHop(created.getId(),danhSachKeHoach);
        }
        if (chiTiet.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }else {
                for (THKeHoachDieuChuyenTongCucDtl ct : chiTiet) {
                    ct.setHdrId(created.getId());
                }
        }
        created.setMaTongHop(created.getId());
        THKeHoachDieuChuyenTongCucHdr createdSave = tongCucHdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), createdSave.getId(), THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return createdSave;
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
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            data.getThKeHoachDieuChuyenTongCucDtls().forEach(data1 -> {
                if(data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)){
                    Hibernate.initialize(data1.getThKeHoachDieuChuyenCucHdr());
                }
                if(data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    List<Long> listId = Arrays.stream(data1.getThKeHoachDieuChuyenCucKhacCucDtl().getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                    data1.getThKeHoachDieuChuyenCucHdr().getThKeHoachDieuChuyenCucKhacCucDtls().clear();
                    data1.getThKeHoachDieuChuyenCucKhacCucDtl().setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                }
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
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<THKeHoachDieuChuyenTongCucDtl> list = thKeHoachDieuChuyenTongCucDtlRepository.findByHdrId(data.getId());
        list.forEach(e ->{
            e.getThKeHoachDieuChuyenCucHdr().setIdThTongCuc(null);
            thKeHoachDieuChuyenCucHdrRepository.save(e.getThKeHoachDieuChuyenCucHdr());
        });
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
        fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<THKeHoachDieuChuyenTongCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenTongCucDtlRepository.findAllByHdrIdIn(listId);
        listTongHopKeHoach.forEach(e ->{
            e.getThKeHoachDieuChuyenCucHdr().setIdThTongCuc(null);
            thKeHoachDieuChuyenCucHdrRepository.save(e.getThKeHoachDieuChuyenCucHdr());
        });
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
//        List<THKeHoachDieuChuyenTongCucHdr> maTongHop = tongCucHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
//        if (!maTongHop.isEmpty() && objReq.getMaTongHop().split("/").length == 1) {
//            if (!maTongHop.get(0).getId().equals(objReq.getId())) {
//                throw new Exception("Mã tổng hợp đã tồn tại");
//            }
//        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        THKeHoachDieuChuyenTongCucHdr dataMap = new ModelMapper().map(objReq, THKeHoachDieuChuyenTongCucHdr.class);
        BeanUtils.copyProperties(data,dataMap);
        dataMap.setTrichYeu(objReq.getTrichYeu());
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(dataMap);
        data.setNguoiSuaId(currentUser.getUser().getId());
        created = tongCucHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }

    @Transactional
    public List<ThKeHoachDieuChuyenTongCucDtlReq> createPlan(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        List<ThKeHoachDieuChuyenTongCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                List<THKeHoachDieuChuyenCucHdr> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucHdrRepository.findByDonViAndTrangThaiTongCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, req.getThoiGianTongHop());
                for (THKeHoachDieuChuyenCucHdr entry : dcnbKeHoachDcHdrs) {
                    Hibernate.initialize(entry.getThKeHoachDieuChuyenCucKhacCucDtls());
                    THKeHoachDieuChuyenCucHdr khhc = SerializationUtils.clone(entry);
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ModelMapper().map(khhc, ThKeHoachDieuChuyenTongCucDtlReq.class);
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    chiTiet.setMaCucDxuat(req.getMaDVi());
                    chiTiet.setTenCucDxuat(cqt.getTenDvi());
                    chiTiet.setSoDxuat(khhc.getSoDeXuat());
                    chiTiet.setThKhDcHdrId(khhc.getId());
                    chiTiet.setNgayTrinhDuyetTc(entry.getNgayDuyetLdc());
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(req.getMaDVi(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, Contains.DADUYET_LDCC, req.getThoiGianTongHop(),khhc.getId());
                    chiTiet.setTongDuToanKp(tongDuToanKp == null ? 0 : tongDuToanKp);
                    result.add(chiTiet);
                }
            } else if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                List<THKeHoachDieuChuyenCucKhacCucDtl> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucKhacCucDtlRepository.findByDonViAndTrangThaiAndLoaiDcCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CUC_DTNN_KV, req.getThoiGianTongHop());
                Map<String, List<THKeHoachDieuChuyenCucKhacCucDtl>> postsPerType = dcnbKeHoachDcHdrs.stream()
                        .collect(groupingBy(THKeHoachDieuChuyenCucKhacCucDtl::getMaCucNhan));
                for (Map.Entry<String, List<THKeHoachDieuChuyenCucKhacCucDtl>> entry : postsPerType.entrySet()) {
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ThKeHoachDieuChuyenTongCucDtlReq();
                    List<THKeHoachDieuChuyenCucKhacCucDtl> thkh = entry.getValue();
                    chiTiet.setMaCucNhan(entry.getKey());
                    chiTiet.setTenCucNhan(thkh.get(0).getTenCucNhan());
                    chiTiet.setMaCucDxuat(cqt.getMaDvi());
                    chiTiet.setTenCucDxuat(cqt.getTenDvi());
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    thkh.forEach(data ->{
                        chiTiet.setThKhDcDtlId(data.getId());
                        chiTiet.setSoDxuat(data.getTHKeHoachDieuChuyenCucHdr().getSoDeXuat());
                        chiTiet.setNgayTrinhDuyetTc(data.getTHKeHoachDieuChuyenCucHdr().getNgayDuyetLdc());
                        chiTiet.setTrichYeu(data.getTHKeHoachDieuChuyenCucHdr().getTrichYeu());
                        chiTiet.setThKhDcHdrId(data.getHdrId());
                        List<THKeHoachDieuChuyenCucKhacCucDtl> chiTietKh = thKeHoachDieuChuyenCucKhacCucDtlRepository.findAllByHdrIdAndId(chiTiet.getThKhDcHdrId(), chiTiet.getThKhDcDtlId());
                        chiTiet.setThKeHoachDieuChuyenCucKhacCucDtls(chiTietKh);
                        chiTietKh.forEach(data2 ->{
                            List<Long> listId = Arrays.asList(data2.getDcnbKeHoachDcHdrId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                            data2.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                        });
                    });
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucCuc(req.getMaDVi(), chiTiet.getMaCucNhan(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CUC_DTNN_KV, Contains.DADUYET_LDCC, req.getThoiGianTongHop(), chiTiet.getThKhDcHdrId());
                    chiTiet.setTongDuToanKp(tongDuToanKp == null ? 0 : tongDuToanKp);
                    result.add(chiTiet);
                }
            }
        }
        if(result.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }
        return result;
    }

    @Transactional
    public void export(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<THKeHoachDieuChuyenTongCucHdr> page = this.searchPage(currentUser, objReq);
        List<THKeHoachDieuChuyenTongCucHdr> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch điều chuyển";
        String[] rowsName = new String[]{"STT", "Năm KH", "Mã tổng hợp", "Loại điều chuyển", "Ngày tổng hợp", "Nội dung tổng hợp","Số QĐ PD KHĐC", "Trạng thái",};
        String fileName = "danh-sach-tong-hop-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            THKeHoachDieuChuyenTongCucHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getMaTongHop();
            objs[3] = dx.getLoaiDieuChuyen();
            objs[4] = dx.getNgayTongHop();
            objs[5] = dx.getTrichYeu();
            objs[6] = dx.getSoQddc();
            objs[7] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}

