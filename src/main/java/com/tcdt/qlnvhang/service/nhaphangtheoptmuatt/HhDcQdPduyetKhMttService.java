package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.HhSlNhapHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrSearchReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.HhSlNhapHangReq;
import com.tcdt.qlnvhang.response.DcQdPduyetKhMttDTO;
import com.tcdt.qlnvhang.response.HopDongMttHdrDTO;
import com.tcdt.qlnvhang.service.HhSlNhapHangService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class HhDcQdPduyetKhMttService extends BaseServiceImpl {
    @Autowired
    private HhDcQdPduyetKhMttRepository hhDcQdPduyetKhMttRepository;

    @Autowired
    private HhDcQdPduyetKhMttDxRepository hhDcQdPduyetKhMttDxRepository;

    @Autowired
    private HhDcQdPduyetKhmttSlddRepository hhDcQdPduyetKhmttSlddRepository;

    @Autowired
    private HhDcQdPdKhmttSlddDtlRepository  hhDcQdPdKhmttSlddDtlRepository;

    @Autowired
    private HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    private HhQdPheduyetKhMttHdrService hhQdPheduyetKhMttHdrService;

    @Autowired
    private HhSlNhapHangService hhSlNhapHangService;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;

    @Autowired
    private HhQdPheduyetKhMttSLDDRepository hhQdPheduyetKhMttSLDDRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;



    public Page<HhDcQdPduyetKhmttHdr> searchPage(SearchHhDcQdPduyetKhMttReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDcQdPduyetKhmttHdr> data = hhDcQdPduyetKhMttRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQdDc(),
                Contains.convertDateToString(objReq.getNgayKyDcTu()),
                Contains.convertDateToString(objReq.getNgayKyDcDen()),
                objReq.getTrichYeu(),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable);
        Map<String, String> hashMapDmHh= getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Transactional
    public HhDcQdPduyetKhmttHdr save(HhDcQdPduyetKhmttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findBySoToTrinh(objReq.getSoToTrinh());
        if(optional.isPresent()){
            throw new Exception("số công văn/tờ trình đã tồn tại");
        }
        HhDcQdPduyetKhmttHdr data = new ModelMapper().map(objReq,HhDcQdPduyetKhmttHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DA_LAP);
        data.setMaDvi(userInfo.getDvql());
        HhDcQdPduyetKhmttHdr created=hhDcQdPduyetKhMttRepository.save(data);
        created.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(created.getTrangThai()));
//        objReq.setIsChange(true);
//        updateQdPduyet(created, objReq);
        if (!DataUtils.isNullOrEmpty(objReq.getCanCuPhapLy())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCanCuPhapLy(), created.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullOrEmpty(objReq.getCvanToTrinh())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCvanToTrinh(), created.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CONG_VAN");
        }
        if (!DataUtils.isNullObject(objReq.getFileDinhkems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(), created.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME);
        }
        for (HhDcQdPduyetKhmttDxReq listDx :objReq.getHhDcQdPduyetKhmttDxList()){
            HhDcQdPduyetKhmttDx dx = ObjectMapperUtils.map(listDx, HhDcQdPduyetKhmttDx.class);
            dx.setIdDxHdr(listDx.getIdDxHdr());
            dx.setIdQdHdr(listDx.getIdQdHdr());
            dx.setIdDcHdr(data.getId());
            HhDcQdPduyetKhmttDx save = hhDcQdPduyetKhMttDxRepository.save(dx);
            for (HhDcQdPduyetKhmttSlddReq listSLDD : listDx.getChildren()){
                HhDcQdPduyetKhmttSldd slDd =ObjectMapperUtils.map(listSLDD, HhDcQdPduyetKhmttSldd.class);
                slDd.setIdDxKhmtt(save.getIdDxHdr());
                slDd.setIdDcKhmtt(save.getId());
                slDd.setMaDiemKho(userInfo.getDvql());
                HhDcQdPduyetKhmttSldd saveSlDd = hhDcQdPduyetKhmttSlddRepository.save(slDd);
                for (HhDcQdPdKhmttSlddDtlReq slddDtlReq : listSLDD.getChildren()){
                    HhDcQdPdKhmttSlddDtl slddDtl = ObjectMapperUtils.map(slddDtlReq,HhDcQdPdKhmttSlddDtl.class);
                    slddDtl.setId(null);
                    slddDtl.setIdDiaDiem(saveSlDd.getId());
                    hhDcQdPdKhmttSlddDtlRepository.save(slddDtl);
                }
            }
        }
        return created;
    }


    private void updateQdPduyet(HhDcQdPduyetKhmttHdr dcHdr, HhDcQdPduyetKhmttHdrReq objReq){
        Optional<HhQdPheduyetKhMttHdr> qdHdr = hhQdPheduyetKhMttHdrRepository.findById(dcHdr.getIdQdGoc());
        if(qdHdr.isPresent()){
            qdHdr.get().setIsChange(objReq.getIsChange());
            hhQdPheduyetKhMttHdrRepository.save(qdHdr.get());
        }
    }

    @Transactional
    public HhDcQdPduyetKhmttHdr update(HhDcQdPduyetKhmttHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findById(objReq.getId());
        Optional<HhDcQdPduyetKhmttHdr> soQdDc = hhDcQdPduyetKhMttRepository.findBySoToTrinh(objReq.getSoToTrinh());
        if(soQdDc.isPresent()){
            if (optional.isPresent()){
                if (!soQdDc.get().getId().equals(objReq.getId())){
                    throw new Exception("số công văn/tờ trình đã tồn tại");
                }
            }
        }
        HhDcQdPduyetKhmttHdr data = optional.get();
        HhDcQdPduyetKhmttHdr dataMap= new ModelMapper().map(objReq,HhDcQdPduyetKhmttHdr.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhDcQdPduyetKhmttHdr cerated = hhDcQdPduyetKhMttRepository.save(data);
        cerated.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(cerated.getTrangThai()));
        if (!DataUtils.isNullOrEmpty(objReq.getCanCuPhapLy())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCanCuPhapLy(), cerated.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullOrEmpty(objReq.getCvanToTrinh())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCvanToTrinh(), cerated.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CONG_VAN");
        }
        if (!DataUtils.isNullObject(objReq.getFileDinhkems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(), cerated.getId(), HhDcQdPduyetKhmttHdr.TABLE_NAME);
        }
        List<HhDcQdPduyetKhmttDx> dcQdPduyetKhmttDxList=hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(data.getId());
        List<Long>  listIdDx=dcQdPduyetKhmttDxList.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttSldd> listSlDd=hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmttIn(listIdDx);
        for (HhDcQdPduyetKhmttSldd a : listSlDd){
            List<HhDcQdPdKhmttSlddDtl> listSlDdDtl =hhDcQdPdKhmttSlddDtlRepository.findAllByIdDiaDiem(a.getId());
            hhDcQdPdKhmttSlddDtlRepository.deleteAll(listSlDdDtl);
        }
        hhDcQdPduyetKhmttSlddRepository.deleteAll(listSlDd);
        hhDcQdPduyetKhMttDxRepository.deleteAll(dcQdPduyetKhmttDxList);
        for (HhDcQdPduyetKhmttDxReq listDx :objReq.getHhDcQdPduyetKhmttDxList()){
            HhDcQdPduyetKhmttDx dx = ObjectMapperUtils.map(listDx, HhDcQdPduyetKhmttDx.class);
            dx.setIdDxHdr(listDx.getIdDxHdr());
            dx.setIdDcHdr(data.getId());
            HhDcQdPduyetKhmttDx save = hhDcQdPduyetKhMttDxRepository.save(dx);
            for (HhDcQdPduyetKhmttSlddReq listSLDD : listDx.getChildren()){
                HhDcQdPduyetKhmttSldd slDd =ObjectMapperUtils.map(listSLDD, HhDcQdPduyetKhmttSldd.class);
                slDd.setIdDxKhmtt(save.getIdDxHdr());
                slDd.setIdDcKhmtt(save.getId());
                dx.setIdQdHdr(listDx.getIdQdHdr());
                slDd.setMaDiemKho(userInfo.getDvql());
                HhDcQdPduyetKhmttSldd saveSlDd = hhDcQdPduyetKhmttSlddRepository.save(slDd);
                for (HhDcQdPdKhmttSlddDtlReq slddDtlReq : listSLDD.getChildren()){
                    HhDcQdPdKhmttSlddDtl slddDtl = ObjectMapperUtils.map(slddDtlReq,HhDcQdPdKhmttSlddDtl.class);
                    slddDtl.setId(null);
                    slddDtl.setIdDiaDiem(saveSlDd.getId());
                    hhDcQdPdKhmttSlddDtlRepository.save(slddDtl);

                }
            }

        }

        return cerated;
    }
    public HhDcQdPduyetKhmttHdr detail(String ids) throws  Exception{
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        HhDcQdPduyetKhmttHdr data = optional.get();
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
        Map<String,String> hashMapNguonVon = getListDanhMucChung("NGUON_VON");
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
        List<HhDcQdPduyetKhmttDx> listdx=hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(data.getId());
        for (HhDcQdPduyetKhmttDx pduyetDx :listdx){
            pduyetDx.setTenLoaiVthh(StringUtils.isEmpty(pduyetDx.getLoaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getLoaiVthh()));
            pduyetDx.setTenCloaiVthh(StringUtils.isEmpty(pduyetDx.getCloaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getCloaiVthh()));
            pduyetDx.setTenChuDt(StringUtils.isEmpty(pduyetDx.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
            pduyetDx.setTenDvi(StringUtils.isEmpty(pduyetDx.getMaDvi()) ? null : hashMapDmdv.get(pduyetDx.getMaDvi()));
            List<Long> idDx=listdx.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
            List<HhDcQdPduyetKhmttSldd> listSlDd =hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmtt(pduyetDx.getId());
            pduyetDx.setTenNguonVon(hashMapNguonVon.get(pduyetDx.getNguonVon()));
            pduyetDx.setChildren(listSlDd);
            for (HhDcQdPduyetKhmttSldd sldd:listSlDd){
                sldd.setTenDvi(StringUtils.isEmpty(sldd.getMaDvi()) ? null : hashMapDmdv.get(sldd.getMaDvi()));
                List<HhDcQdPdKhmttSlddDtl> listSlddDtl =hhDcQdPdKhmttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
                for (HhDcQdPdKhmttSlddDtl slddDtl :listSlddDtl){
                    slddDtl.setTenDiemKho(StringUtils.isEmpty(slddDtl.getMaDiemKho())? null:hashMapDmdv.get(slddDtl.getMaDiemKho()));
                    sldd.setChildren(listSlddDtl);
                }
            }
        }
        data.setHhDcQdPduyetKhmttDxList(listdx.stream().sorted(Comparator.comparing(HhDcQdPduyetKhmttDx::getMaDvi)).collect(Collectors.toList()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Collections.singletonList(HhDcQdPduyetKhmttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKem);
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singletonList(HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CAN_CU"));
        data.setCanCuPhapLy(canCu);
        List<FileDinhKem> congVan = fileDinhKemService.search(data.getId(), Collections.singletonList(HhDcQdPduyetKhmttHdr.TABLE_NAME + "_CONG_VAN"));
        data.setCvanToTrinh(congVan);
        
        return data;
    }


    @Transactional(rollbackOn = Exception.class)
    public void delete (IdSearchReq idSearchReq) throws Exception{
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        if (!optional.get().getTrangThai().equals(Contains.DA_LAP)&& !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái bản nháp hoặc từ chối");
        }
        HhDcQdPduyetKhmttHdr data = optional.get();
        HhDcQdPduyetKhmttHdrReq hhDcQdPduyetKhmttHdrReq = new HhDcQdPduyetKhmttHdrReq();
        List<HhDcQdPduyetKhmttHdr> listCheckQd = hhDcQdPduyetKhMttRepository.findAllByIdQdGocOrderByIdDesc(data.getIdQdGoc());
        if(listCheckQd.size() == 1){
            hhDcQdPduyetKhmttHdrReq.setIsChange(null);
            updateQdPduyet(data, hhDcQdPduyetKhmttHdrReq);
        }


        List<HhDcQdPduyetKhmttDx> dcQdPduyetKhmttDxList =hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(data.getId());
        List<Long>  listIdDx=dcQdPduyetKhmttDxList.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttSldd> listSlDd=hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmttIn(listIdDx);
        for (HhDcQdPduyetKhmttSldd sldd : listSlDd){
            List<HhDcQdPdKhmttSlddDtl> listSlddDtl = hhDcQdPdKhmttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
            hhDcQdPdKhmttSlddDtlRepository.deleteAll(listSlddDtl);
        }
        hhDcQdPduyetKhMttDxRepository.deleteAll(dcQdPduyetKhmttDxList);
        hhDcQdPduyetKhmttSlddRepository.deleteAll(listSlDd);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_QD_PHE_DUYET_KHMTT_HDR"));
        hhDcQdPduyetKhMttRepository.delete(data);
    }

    @Transactional(rollbackOn = Exception.class)
    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhDcQdPduyetKhmttHdr> list= hhDcQdPduyetKhMttRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhDcQdPduyetKhmttHdr qdPheduyetKhMttHdr : list){
            if (!qdPheduyetKhMttHdr.getTrangThai().equals(Contains.DA_LAP)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listId=list.stream().map(HhDcQdPduyetKhmttHdr::getId).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttDx> listPduyet=hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdrIn(listId);
        List<Long> listIdDx=listPduyet.stream().map(HhDcQdPduyetKhmttDx::getId).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttSldd> listSlDd=hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmttIn(listIdDx);
        for (HhDcQdPduyetKhmttSldd sldd : listSlDd){
            List<HhDcQdPdKhmttSlddDtl> listSlddDtl = hhDcQdPdKhmttSlddDtlRepository.findAllByIdDiaDiem(sldd.getId());
            hhDcQdPdKhmttSlddDtlRepository.deleteAll(listSlddDtl);
        }
        hhDcQdPduyetKhMttDxRepository.deleteAll(listPduyet);
        hhDcQdPduyetKhmttSlddRepository.deleteAll(listSlDd);
        hhDcQdPduyetKhMttRepository.deleteAll(list);
    }


    public  void  export(SearchHhDcQdPduyetKhMttReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhDcQdPduyetKhmttHdr> page=this.searchPage(objReq);
        List<HhDcQdPduyetKhmttHdr> data=page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName =new String[]{"STT","Năm kế hoạch","Số QĐ điều chỉnh KH mua trực tiếp","Ngày ký QĐ điều chỉnh","Số quyết định gốc","Trích yếu","Loại hàng hóa","Chủng loại hàng hóa","Trạng Thái"};
        String fileName="danh-sach-dc-qd-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhDcQdPduyetKhmttHdr dchinh=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dchinh.getNamKh();
            objs[2]=dchinh.getSoQdDc();
            objs[3]=dchinh.getNgayKyDc();
            objs[4]=dchinh.getSoQdGoc();
            objs[5]=dchinh.getTrichYeu();
            objs[6]=dchinh.getTenLoaiVthh();
            objs[7]=dchinh.getTenCloaiVthh();
            objs[8]=dchinh.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }


    public HhDcQdPduyetKhmttHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhDcQdPduyetKhmttHdr> optional =hhDcQdPduyetKhMttRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        HhDcQdPduyetKhmttHdrReq objReq = new HhDcQdPduyetKhmttHdrReq();
        objReq.setIsChange(true);

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_LDV + Contains.DA_LAP:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
                optional.get().setNguoiGduyet(userInfo.getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.DADUYET_LDV:
                optional.get().setNguoiGduyet(getUser().getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                optional.get().setLdoTchoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.DADUYET_LDV:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhDcQdPduyetKhmttHdr created = hhDcQdPduyetKhMttRepository.save(optional.get());
        updateQdPduyet(created, objReq);
        if(created.getTrangThai().equals(Contains.BAN_HANH)){
            List<HhSlNhapHang> hhSlNhapHangs = hhSlNhapHangService.findAllByIdQd(created.getIdQdGoc());
            Optional<HhSlNhapHang> hhSlNhapHang = Optional.empty();
            HhSlNhapHangReq hhSlNhapHangReq = new HhSlNhapHangReq();
            List<HhDcQdPduyetKhmttDx> dxList = hhDcQdPduyetKhMttDxRepository.findAllByIdDcHdr(created.getId());
            for (HhDcQdPduyetKhmttDx hhDcQdPduyetKhmttDx : dxList) {
                List<HhDcQdPduyetKhmttSldd> slddList = hhDcQdPduyetKhmttSlddRepository.findAllByIdDcKhmtt(hhDcQdPduyetKhmttDx.getId());
                for (HhDcQdPduyetKhmttSldd hhDcQdPduyetKhmttSldd : slddList) {
                    hhSlNhapHang = hhSlNhapHangs.stream().filter(x -> x.getMaDvi().equals(hhDcQdPduyetKhmttSldd.getMaDvi())).findFirst();
                    hhSlNhapHang.get().setSoLuong(hhDcQdPduyetKhmttSldd.getTongSoLuong());
                    BeanUtils.copyProperties(hhSlNhapHang.get(), hhSlNhapHangReq);
                    hhSlNhapHangService.update(hhSlNhapHangReq);
                }
            }
        }

        return created;
    }

    public ReportTemplateResponse preview(HhDcQdPduyetKhmttHdrReq req) throws Exception {
        HhDcQdPduyetKhmttHdr hhDcQdPduyetKhmttHdr = detail(req.getId().toString());
        if (hhDcQdPduyetKhmttHdr == null) {
            throw new Exception("Biên bản không tồn tại.");
        }

        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        AtomicReference<BigDecimal> tongSoLuongGoc = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tongSoLuong = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tongThanhTien = new AtomicReference<>(BigDecimal.ZERO);
        if(hhDcQdPduyetKhmttHdr.getSoLanDieuChinh() > 0){
            Optional<HhDcQdPduyetKhmttHdr> hhDcQdPduyetKhmttHdrGoc = hhDcQdPduyetKhMttRepository.findById(hhDcQdPduyetKhmttHdr.getId());
            for (HhDcQdPduyetKhmttDx hhDcQdPduyetKhmttDx : hhDcQdPduyetKhmttHdr.getHhDcQdPduyetKhmttDxList()) {
                Optional<HhDcQdPduyetKhmttDx> listDxGoc = hhDcQdPduyetKhmttHdrGoc.get().getHhDcQdPduyetKhmttDxList().stream().filter(x -> x.getId().equals(hhDcQdPduyetKhmttDx.getId())).findFirst();
                hhDcQdPduyetKhmttDx.setTgianKthucGoc(Contains.convertDateToString(listDxGoc.get().getTgianKthuc()));
                hhDcQdPduyetKhmttDx.setTgianKthucStr(Contains.convertDateToString(hhDcQdPduyetKhmttDx.getTgianKthuc()));
                for (HhDcQdPduyetKhmttSldd child : hhDcQdPduyetKhmttDx.getChildren()) {
                    Optional<HhDcQdPduyetKhmttSldd> listSlddGoc = listDxGoc.get().getChildren().stream().filter(x -> x.getId().equals(child.getId())).findFirst();
                    child.setSoLuongGocStr(docxToPdfConverter.convertBigDecimalToStr(listSlddGoc.get().getTongSoLuong()));
                    child.setSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(child.getTongSoLuong()));
                    child.setTongThanhTienVatStr(docxToPdfConverter.convertBigDecimalToStr(child.getDonGiaVat().multiply(child.getTongSoLuong())));
                    tongSoLuongGoc.updateAndGet(v -> v.add(listSlddGoc.get().getTongSoLuong()));
                    tongSoLuong.updateAndGet(v -> v.add(child.getTongSoLuong()));
                    tongThanhTien.updateAndGet(v -> v.add(child.getDonGiaVat().multiply(child.getTongSoLuong())));
                }
            }
            hhDcQdPduyetKhmttHdr.setNgayPduyetGocStr(Contains.convertDateToString(hhDcQdPduyetKhmttHdrGoc.get().getNgayPduyet()));
            hhDcQdPduyetKhmttHdr.setNgayPduyetStr(Contains.convertDateToString(hhDcQdPduyetKhmttHdr.getNgayPduyet()));
            hhDcQdPduyetKhmttHdr.setTongSoLuongGocStr(docxToPdfConverter.convertBigDecimalToStr(tongSoLuongGoc.get()));
            hhDcQdPduyetKhmttHdr.setTongSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(tongSoLuong.get()));
            hhDcQdPduyetKhmttHdr.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien.get()));
        }else{
            HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr = hhQdPheduyetKhMttHdrService.detail(hhDcQdPduyetKhmttHdr.getIdQdGoc());
            for (HhDcQdPduyetKhmttDx hhDcQdPduyetKhmttDx : hhDcQdPduyetKhmttHdr.getHhDcQdPduyetKhmttDxList()) {
                Optional<HhQdPheduyetKhMttDx> listDxGoc = hhQdPheduyetKhMttHdr.getChildren().stream().filter(x -> x.getMaDvi().equals(hhDcQdPduyetKhmttDx.getMaDvi())).findFirst();
                if(listDxGoc.isPresent()){
                    hhDcQdPduyetKhmttDx.setTgianKthucGoc(Contains.convertDateToString(listDxGoc.get().getTgianKthuc()));
                    hhDcQdPduyetKhmttDx.setTgianKthucStr(Contains.convertDateToString(hhDcQdPduyetKhmttDx.getTgianKthuc()));
                    for (HhDcQdPduyetKhmttSldd child : hhDcQdPduyetKhmttDx.getChildren()) {
                        Optional<HhQdPheduyetKhMttSLDD> listSlddGoc = listDxGoc.get().getChildren().stream().filter(x -> x.getMaDvi().equals(child.getMaDvi())).findFirst();
                        if(listSlddGoc.isPresent()){
                            child.setSoLuongGocStr(docxToPdfConverter.convertBigDecimalToStr(listSlddGoc.get().getTongSoLuong()));
                            child.setSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(child.getTongSoLuong()));
                            child.setTongThanhTienVatStr(docxToPdfConverter.convertBigDecimalToStr(child.getDonGiaVat().multiply(child.getTongSoLuong())));
                            tongSoLuongGoc.updateAndGet(v -> v.add(listSlddGoc.get().getTongSoLuong()));
                            tongSoLuong.updateAndGet(v -> v.add(child.getTongSoLuong()));
                            tongThanhTien.updateAndGet(v -> v.add(child.getDonGiaVat().multiply(child.getTongSoLuong())));
                        }
                    }
                }
            }
            hhDcQdPduyetKhmttHdr.setNgayPduyetGocStr(Contains.convertDateToString(hhQdPheduyetKhMttHdr.getNgayPduyet()));
            hhDcQdPduyetKhmttHdr.setNgayPduyetStr(Contains.convertDateToString(hhDcQdPduyetKhmttHdr.getNgayPduyet()));
            hhDcQdPduyetKhmttHdr.setTongSoLuongGocStr(docxToPdfConverter.convertBigDecimalToStr(tongSoLuongGoc.get()));
            hhDcQdPduyetKhmttHdr.setTongSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(tongSoLuong.get()));
            hhDcQdPduyetKhmttHdr.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien.get()));
        }



        return docxToPdfConverter.convertDocxToPdf(inputStream, hhDcQdPduyetKhmttHdr);
    }


    public List<DcQdPduyetKhMttDTO> danhSachQdDc(SearchHhDcQdPduyetKhMttReqDTO req) throws Exception {
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        HhQdPheduyetKhMttHdrSearchReq objQd = new HhQdPheduyetKhMttHdrSearchReq();
        BeanUtils.copyProperties(req, objQd);
        List<HhQdPheduyetKhMttHdr> listQdPd = hhQdPheduyetKhMttHdrService.searchDsTaoQdDc(objQd);
        listQdPd = listQdPd.stream().filter(x -> x.getIsChange() == null).collect(Collectors.toList());
        List<HhDcQdPduyetKhmttHdr> listDcPd = hhDcQdPduyetKhMttRepository.searchDsLastest();
        for (HhDcQdPduyetKhmttHdr hhDcQdPduyetKhmttHdr : listDcPd) {
            hhDcQdPduyetKhmttHdr.setTenCloaiVthh(listDanhMucHangHoa.get(hhDcQdPduyetKhmttHdr.getCloaiVthh()));
            hhDcQdPduyetKhmttHdr.setTenLoaiVthh(listDanhMucHangHoa.get(hhDcQdPduyetKhmttHdr.getLoaiVthh()));
        }
        List<DcQdPduyetKhMttDTO> result = mergeLists(listQdPd, listDcPd);
        return result;
    }

    public static List<DcQdPduyetKhMttDTO> mergeLists(List<HhQdPheduyetKhMttHdr> list1, List<HhDcQdPduyetKhmttHdr> list2) {
        Map<Long, DcQdPduyetKhMttDTO> dtoMap = new HashMap<>();

        for (HhQdPheduyetKhMttHdr item : list1) {
            DcQdPduyetKhMttDTO dto = new DcQdPduyetKhMttDTO();
            BeanUtils.copyProperties(item, dto);
            dtoMap.put(item.getId(), dto);
        }

        for (HhDcQdPduyetKhmttHdr item : list2) {
            DcQdPduyetKhMttDTO dto = new DcQdPduyetKhMttDTO();
            BeanUtils.copyProperties(item, dto);
            dtoMap.put(item.getId(), dto);
        }
        List<DcQdPduyetKhMttDTO> result = new ArrayList<>(dtoMap.values());
        return result;
    }


}
