package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.*;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPdKhMttSlddDtlReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgSearchReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdg;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhQdPheduyetKhMttHdrService extends BaseServiceImpl {

    @Autowired
    private HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;

    @Autowired
    private HhQdPheduyetKhMttSLDDRepository hhQdPheduyetKhMttSLDDRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    HhDxuatKhMttThopRepository hhDxuatKhMttThopRepository;

    @Autowired
    HhQdPdKhMttSlddDtlRepository hhQdPdKhMttSlddDtlRepositoryl;

    @Autowired
    private KeHoachService keHoachService;


    public Page<HhQdPheduyetKhMttHdr> searchPage(HhQdPheduyetKhMttHdrSearchReq objReq)throws Exception{

        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQd(),
                objReq.getTrichYeu(),
                Contains.convertDateToString(objReq.getNgayQdTu()),
                Contains.convertDateToString(objReq.getNgayQdDen()),
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                objReq.getLastest(),
                objReq.getMaDvi(),
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public HhQdPheduyetKhMttHdr create (HhQdPheduyetKhMttHdrReq objReq) throws Exception{

        if(!StringUtils.isEmpty(objReq.getSoQd())){
            List<HhQdPheduyetKhMttHdr> checkSoQd = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
            }
        }

        if(objReq.getPhanLoai().equals("TH")){
            Optional<HhDxKhMttThopHdr> qOptionalTh = hhDxuatKhMttThopRepository.findById(objReq.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch mua trực tiếp");
            }
        }else{
            Optional<HhDxuatKhMttHdr> byId = hhDxuatKhMttRepository.findById(objReq.getIdTrHdr());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch mua trực tiếp");
            }
        }

        HhQdPheduyetKhMttHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPheduyetKhMttHdr.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiTkhai(Contains.CHUACAPNHAT);
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setLastest(objReq.getLastest());
        HhQdPheduyetKhMttHdr created=hhQdPheduyetKhMttHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"HH_QD_PHE_DUYET_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);

        // Update trạng thái tổng hợp dxkhclnt
        if(objReq.getPhanLoai().equals("TH")){
            hhDxuatKhMttThopRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
        }else{
            hhDxuatKhMttRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
        }

        saveDetail(objReq,dataMap);
        return created;
    }


    @Transactional
    void saveDetail(HhQdPheduyetKhMttHdrReq  objReq, HhQdPheduyetKhMttHdr dataMap){
        hhQdPheduyetKhMttDxRepository.deleteAllByIdQdHdr(dataMap.getId());
        for (HhQdPheduyetKhMttDxReq dx : objReq.getDsDiaDiem()){
            HhQdPheduyetKhMttDx qd = ObjectMapperUtils.map(dx, HhQdPheduyetKhMttDx.class);
            hhQdPheduyetKhMttSLDDRepository.deleteByIdQdDtl(qd.getId());
            qd.setId(null);
            qd.setIdQdHdr(dataMap.getId());
            qd.setTrangThai(Contains.CHUACAPNHAT);
            hhQdPheduyetKhMttDxRepository.save(qd);
            for (HhQdPheduyetKhMttSLDDReq gtList : ObjectUtils.isEmpty(dx.getDsGoiThau()) ? dx.getChildren() : dx.getDsGoiThau()){
                HhQdPheduyetKhMttSLDD gt = ObjectMapperUtils.map(gtList, HhQdPheduyetKhMttSLDD.class);
                hhQdPdKhMttSlddDtlRepositoryl.deleteAllByIdDiaDiem(gt.getId());
                gt.setId(null);
                gt.setIdQdDtl(qd.getId());
                gt.setTrangThai(Contains.CHUACAPNHAT);
                hhQdPheduyetKhMttSLDDRepository.save(gt);
                for (HhQdPdKhMttSlddDtlReq ddNhap : gtList.getChildren()){
                    HhQdPdKhMttSlddDtl dataDdNhap = new ModelMapper().map(ddNhap, HhQdPdKhMttSlddDtl.class);
                    dataDdNhap.setId(null);
                    dataDdNhap.setIdDiaDiem(gt.getId());
                    hhQdPdKhMttSlddDtlRepositoryl.save(dataDdNhap);

                }
            }
        }
    }


    @Transactional
    public HhQdPheduyetKhMttHdr update(HhQdPheduyetKhMttHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<HhQdPheduyetKhMttHdr> qOptional = hhQdPheduyetKhMttHdrRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if(!StringUtils.isEmpty(objReq.getSoQd())){
            if (!objReq.getSoQd().equals(qOptional.get().getSoQd())) {
                List<HhQdPheduyetKhMttHdr> checkSoQd = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
                }
            }
        }
        if(objReq.getPhanLoai().equals("TH")){
            Optional<HhDxKhMttThopHdr> qOptionalTh = hhDxuatKhMttThopRepository.findById(objReq.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch mua trực tiếp");
            }
        }else{
            Optional<HhDxuatKhMttHdr> byId = hhDxuatKhMttRepository.findById(objReq.getIdTrHdr());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch mua trực tiếp");
            }
        }

        HhQdPheduyetKhMttHdr dataDB = qOptional.get();
        HhQdPheduyetKhMttHdr dataMap = ObjectMapperUtils.map(objReq, HhQdPheduyetKhMttHdr.class);

        updateObjectToObject(dataDB, dataMap);

        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSua(getUser().getUsername());


        hhQdPheduyetKhMttHdrRepository.save(dataDB);

        this.saveDetail(objReq,dataDB);

        return dataDB;
    }


    public HhQdPheduyetKhMttHdr detail(String ids) throws Exception{
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        Optional<HhQdPheduyetKhMttHdr> qOptional = hhQdPheduyetKhMttHdrRepository.findById(Long.parseLong(ids));


        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");


        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));

        List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxList = new ArrayList<>();
        for (HhQdPheduyetKhMttDx dtl : hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(Long.parseLong(ids))){
            List<HhQdPheduyetKhMttSLDD> hhQdPheduyetKhMttSLDDList = new ArrayList<>();
            for(HhQdPheduyetKhMttSLDD dsg : hhQdPheduyetKhMttSLDDRepository.findByIdQdDtl(dtl.getId())){
                List<HhQdPdKhMttSlddDtl> listGtCtiet = hhQdPdKhMttSlddDtlRepositoryl.findByIdDiaDiem(dsg.getId());
                listGtCtiet.forEach(f -> {
                    f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
                    f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));

                });
                dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
                dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
                dsg.setChildren(listGtCtiet);
                hhQdPheduyetKhMttSLDDList.add(dsg);
            };
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setChildren(hhQdPheduyetKhMttSLDDList);
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));

            hhQdPheduyetKhMttDxList.add(dtl);

        }

        qOptional.get().setChildren(hhQdPheduyetKhMttDxList);
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

        return qOptional.get();
    }


    @Transactional()
    public void delete(IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");

        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }

        List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDx = hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(hhQdPheduyetKhMttDx)){
            for (HhQdPheduyetKhMttDx dtl: hhQdPheduyetKhMttDx){
                List<HhQdPheduyetKhMttSLDD> byIdQdDtl = hhQdPheduyetKhMttSLDDRepository.findByIdQdDtl(dtl.getId());
                for (HhQdPheduyetKhMttSLDD sldd : byIdQdDtl){
                    hhQdPdKhMttSlddDtlRepositoryl.deleteAllByIdDiaDiem(sldd.getId());
                }
                hhQdPheduyetKhMttSLDDRepository.deleteByIdQdDtl(dtl.getId());
            }
            hhQdPheduyetKhMttDxRepository.deleteAll(hhQdPheduyetKhMttDx);
        }
        hhQdPheduyetKhMttHdrRepository.delete(optional.get());

        if (optional.get().getPhanLoai().equals("TH")){
            hhDxuatKhMttThopRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        }else {
            hhDxuatKhMttRepository.updateStatusInList(Arrays.asList(optional.get().getSoTrHdr()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
        }
    }



    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<HhQdPheduyetKhMttHdr> listHdr = hhQdPheduyetKhMttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (listHdr.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (HhQdPheduyetKhMttHdr mtt : listHdr){
            if (!mtt.getTrangThai().equals(Contains.DUTHAO))
                throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
        }

        List<HhQdPheduyetKhMttHdr> list = hhQdPheduyetKhMttHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        List<Long> idList = listHdr.stream().map(HhQdPheduyetKhMttHdr::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxs = hhQdPheduyetKhMttDxRepository.findAllByIdQdHdrIn(idList);
        List<Long> listIdDx = hhQdPheduyetKhMttDxs.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttSLDD> hhQdPheduyetKhMttSLDDS = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdDtlIn(listIdDx);
        for (HhQdPheduyetKhMttSLDD mttSLDD :  hhQdPheduyetKhMttSLDDS){
            List<HhQdPdKhMttSlddDtl> slddDtls = hhQdPdKhMttSlddDtlRepositoryl.findByIdDiaDiem(mttSLDD.getId());
            hhQdPdKhMttSlddDtlRepositoryl.deleteAll(slddDtls);
        }
        hhQdPheduyetKhMttSLDDRepository.deleteAll(hhQdPheduyetKhMttSLDDS);
        hhQdPheduyetKhMttDxRepository.deleteAll(hhQdPheduyetKhMttDxs);
        hhQdPheduyetKhMttHdrRepository.deleteAll(list);
    }

    public HhQdPheduyetKhMttHdr approve(StatusReq stReq) throws Exception{
        if (StringUtils.isEmpty(stReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        HhQdPheduyetKhMttHdr detail = detail(String.valueOf(stReq.getId()));
       return this.approveLT(stReq,detail);
    }

    @Transactional(rollbackOn = Exception.class)
    HhQdPheduyetKhMttHdr approveLT(StatusReq stReq, HhQdPheduyetKhMttHdr dataDB) throws Exception{
        String status = stReq.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                dataDB.setNguoiPduyet(getUser().getUsername());
                dataDB.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(stReq.getTrangThai());
        if (stReq.getTrangThai().equals(Contains.BAN_HANH)){
            if (dataDB.getPhanLoai().equals("TH")){
                Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(dataDB.getIdThHdr());
                if (qOptional.isPresent()){
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    hhDxuatKhMttThopRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
                }else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }
            }else {
                Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(dataDB.getIdTrHdr());
                if (qOptional.isPresent()){
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                    hhDxuatKhMttRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
                }else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }
            this.cloneProject(dataDB.getId());
        }
        HhQdPheduyetKhMttHdr createCheck = hhQdPheduyetKhMttHdrRepository.save(dataDB);
        return createCheck;
    }


    public void validateData(HhQdPheduyetKhMttHdr objHdr) throws Exception {
        for(HhQdPheduyetKhMttDx dtl : objHdr.getChildren()){
            for(HhQdPheduyetKhMttSLDD dsgthau : dtl.getChildren()){
                BigDecimal aLong = hhDxuatKhMttRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP");
                if(soLuongTotal.compareTo(nhap) > 0){
                    throw new Exception(dsgthau.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
                }
            }
        }
    }

    private void cloneProject(Long idClone) throws Exception {
        HhQdPheduyetKhMttHdr hdr = this.detail(idClone.toString());
            HhQdPheduyetKhMttHdr hdrClone = new HhQdPheduyetKhMttHdr();
            BeanUtils.copyProperties(hdr, hdrClone);
            hdrClone.setId(null);
            hdrClone.setLastest(true);
            hdrClone.setIdGoc(hdr.getId());
            hhQdPheduyetKhMttHdrRepository.save(hdrClone);
            for (HhQdPheduyetKhMttDx dx: hdr.getChildren()){
                HhQdPheduyetKhMttDx dxClone = new HhQdPheduyetKhMttDx();
                BeanUtils.copyProperties(dx, dxClone);
                dxClone.setId(null);
                dxClone.setIdQdHdr(hdrClone.getId());
                hhQdPheduyetKhMttDxRepository.save(dxClone);
                for (HhQdPheduyetKhMttSLDD mttSLDD : dx.getChildren()){
                    HhQdPheduyetKhMttSLDD slddClone = new HhQdPheduyetKhMttSLDD();
                    BeanUtils.copyProperties(mttSLDD, slddClone);
                    slddClone.setId(null);
                    slddClone.setIdQdDtl(dxClone.getId());
                    hhQdPheduyetKhMttSLDDRepository.save(slddClone);
                    for (HhQdPdKhMttSlddDtl dsDdnhap : slddClone.getChildren()){
                        HhQdPdKhMttSlddDtl dsDdNhapClone = new HhQdPdKhMttSlddDtl();
                        BeanUtils.copyProperties(dsDdnhap, dsDdNhapClone);
                        dsDdNhapClone.setId(null);
                        dsDdNhapClone.setIdDiaDiem(slddClone.getId());
                        hhQdPdKhMttSlddDtlRepositoryl.save(dsDdNhapClone);
                    }
                }
            }
        }

    public  void  export(HhQdPheduyetKhMttHdrSearchReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdPheduyetKhMttHdr> page=this.searchPage(objReq);
        List<HhQdPheduyetKhMttHdr> data=page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName =new String[]{"STT","Số quyết định","Ngày quyết định","Trích yếu","Số đề xuất/ tờ trình","Mã tổng hợp","Năm kế hoạch","Phương thức mua trực tiếp","Trạng Thái"};
        String fileName="danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPheduyetKhMttHdr pduyet=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=pduyet.getSoQd();
            objs[2]=pduyet.getNgayTao();
            objs[3]=pduyet.getTrichYeu();
            objs[4]=pduyet.getSoTrHdr();
            objs[5]=pduyet.getMaThop();
            objs[6]=pduyet.getNamKh();
            objs[7]=pduyet.getPthucMuatt();
            objs[8]=pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }






}
