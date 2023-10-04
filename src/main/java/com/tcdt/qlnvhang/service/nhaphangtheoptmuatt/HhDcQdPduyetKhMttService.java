package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
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
import java.util.*;
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
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findBySoQdDc(objReq.getSoQdDc());
        if(optional.isPresent()){
            throw new Exception("số quyết định đã tồn tại");
        }
        HhDcQdPduyetKhmttHdr data = new ModelMapper().map(objReq,HhDcQdPduyetKhmttHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        HhDcQdPduyetKhmttHdr created=hhDcQdPduyetKhMttRepository.save(data);
//        objReq.setIsChange(true);
//        updateQdPduyet(created, objReq);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_DC_QD_PDUYET_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);
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
        qdHdr.get().setIsChange(objReq.getIsChange());
        hhQdPheduyetKhMttHdrRepository.save(qdHdr.get());
    }

    @Transactional
    public HhDcQdPduyetKhmttHdr update(HhDcQdPduyetKhmttHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findById(objReq.getId());
        Optional<HhDcQdPduyetKhmttHdr> soQdDc = hhDcQdPduyetKhMttRepository.findBySoQdDc(objReq.getSoQdDc());
        if (soQdDc.isPresent()){
            if (soQdDc.isPresent()){
                if (!soQdDc.get().getId().equals(objReq.getId())){
                    throw new Exception("Số quyết định đã tồn tại");
                }
            }
        }
        HhDcQdPduyetKhmttHdr data = optional.get();
        HhDcQdPduyetKhmttHdr dataMap= new ModelMapper().map(objReq,HhDcQdPduyetKhmttHdr.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhDcQdPduyetKhmttHdr cerated = hhDcQdPduyetKhMttRepository.save(data);

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
        data.setHhDcQdPduyetKhmttDxList(listdx);
        
        return data;
    }


    @Transactional(rollbackOn = Exception.class)
    public void delete (IdSearchReq idSearchReq) throws Exception{
        Optional<HhDcQdPduyetKhmttHdr> optional = hhDcQdPduyetKhMttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)&& !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
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
            if (!qdPheduyetKhMttHdr.getTrangThai().equals(Contains.DUTHAO)){
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
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                optional.get().setNguoiGduyet(userInfo.getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
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

        return created;
    }


}
