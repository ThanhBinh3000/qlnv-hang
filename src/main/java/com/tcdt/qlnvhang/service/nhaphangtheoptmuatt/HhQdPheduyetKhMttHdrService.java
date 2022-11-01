package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttThopRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttDxRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttSLDDRepository;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
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




    public Page<HhQdPheduyetKhMttHdr> searchPage(HhQdPheduyetKhMttHdrSearchReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").ascending());
        Page<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQd(),
                objReq.getTrichYeu(),
                Contains.convertDateToString(objReq.getNgayKyQdTu()),
                Contains.convertDateToString(objReq.getNgayKyQdDen()),
                userInfo.getDvql(),
                objReq.getTrangThai(),
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }


    @Transactional
    public HhQdPheduyetKhMttHdr save(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findBySoQdPduyet(objReq.getSoQdPduyet());
        if(optional.isPresent()){
            throw new Exception("số quyết định đã tồn tại");
        }
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");

        HhQdPheduyetKhMttHdr data = new ModelMapper().map(objReq,HhQdPheduyetKhMttHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        HhQdPheduyetKhMttHdr created=hhQdPheduyetKhMttHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_QD_PHE_DUYET_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);
        if (created.getIdThop() != null){
            hhDxuatKhMttThopRepository.updateTrangThai(created.getIdThop(),Contains.DADUTHAO_QD);
        }else if (created.getSoDxuat() !=null){
            Optional<HhDxuatKhMttHdr> soDxuat= hhDxuatKhMttRepository.findBySoDxuat(created.getSoDxuat());
            soDxuat.get().setSoQdPduyet(created.getSoQdPduyet());
            hhDxuatKhMttRepository.save(soDxuat.get());
        }
        for (HhQdPheduyetKhMttDxReq listDx :objReq.getHhQdPheduyetKhMttDxList()){
            HhQdPheduyetKhMttDx dx =ObjectMapperUtils.map(listDx, HhQdPheduyetKhMttDx.class);
            dx.setId(null);
            dx.setIdDxuat(listDx.getIdDxuat());
            dx.setSoDxuat(listDx.getSoDxuat());
            dx.setIdPduyetHdr(data.getId());
            dx.setTenLoaiVthh(StringUtils.isEmpty(dx.getLoaiVthh()) ? null : hashMapDmHh.get(dx.getLoaiVthh()));
            dx.setTenCloaiVthh(StringUtils.isEmpty(dx.getCloaiVthh()) ? null : hashMapDmHh.get(dx.getCloaiVthh()));
            hhQdPheduyetKhMttDxRepository.save(dx);
            for (HhQdPheduyetKhMttSLDDReq listSLDD : listDx.getSoLuongDiaDiemList()){
                HhQdPheduyetKhMttSLDD slDd =ObjectMapperUtils.map(listSLDD, HhQdPheduyetKhMttSLDD.class);
                slDd.setId(null);
                slDd.setIdDxKhmtt(dx.getId());
                slDd.setMaDiemKho(userInfo.getDvql());
                slDd.setDonGiaVat(dx.getGiaCoThue());
                slDd.setThanhTien(slDd.getDonGiaVat().multiply(slDd.getSoLuongDxmtt()));
                hhQdPheduyetKhMttSLDDRepository.save(slDd);
            }
        }
        return created;
    }

   @Transactional
    public HhQdPheduyetKhMttHdr update(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(objReq.getId());
        Optional<HhQdPheduyetKhMttHdr> soQdPduyet = hhQdPheduyetKhMttHdrRepository.findBySoQdPduyet(objReq.getSoQdPduyet());
         if (soQdPduyet.isPresent()){
             if (soQdPduyet.isPresent()){
                 if (!soQdPduyet.get().getId().equals(objReq.getId())){
                     throw new Exception("Số quyết định đã tồn tại");
                 }
             }
         }
         HhQdPheduyetKhMttHdr data = optional.get();
         HhQdPheduyetKhMttHdr dataMap = new ModelMapper().map(objReq, HhQdPheduyetKhMttHdr.class);
         updateObjectToObject(data,dataMap);
         data.setNguoiSua(userInfo.getUsername());
         data.setNgaySua(new Date());
         HhQdPheduyetKhMttHdr cerated = hhQdPheduyetKhMttHdrRepository.save(data);

       List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxList=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdr(data.getId());
       List<Long>  listIdDx=hhQdPheduyetKhMttDxList.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
       List<HhQdPheduyetKhMttSLDD> listSlDd=hhQdPheduyetKhMttSLDDRepository.findAllByIdDxKhmttIn(listIdDx);
       hhQdPheduyetKhMttDxRepository.deleteAll(hhQdPheduyetKhMttDxList);
       for (HhQdPheduyetKhMttDxReq listDx :objReq.getHhQdPheduyetKhMttDxList()){
           HhQdPheduyetKhMttDx dx =ObjectMapperUtils.map(listDx, HhQdPheduyetKhMttDx.class);
           dx.setId(null);
           dx.setIdDxuat(listDx.getIdDxuat());
           dx.setIdPduyetHdr(data.getId());
           hhQdPheduyetKhMttDxRepository.save(dx);

           hhQdPheduyetKhMttSLDDRepository.deleteAll(listSlDd);
           for (HhQdPheduyetKhMttSLDDReq hhQdPheduyetKhMttSLDDReq : listDx.getSoLuongDiaDiemList()){
               HhQdPheduyetKhMttSLDD slDd =ObjectMapperUtils.map(hhQdPheduyetKhMttSLDDReq, HhQdPheduyetKhMttSLDD.class);
               slDd.setId(null);
               slDd.setIdDxKhmtt(dx.getId());
               slDd.setMaDiemKho(userInfo.getDvql());
               slDd.setDonGiaVat(dx.getGiaCoThue());
               slDd.setThanhTien(slDd.getDonGiaVat().multiply(slDd.getSoLuongDxmtt()));
               hhQdPheduyetKhMttSLDDRepository.save(slDd);
           }

       }

       return cerated;
   }


   public HhQdPheduyetKhMttHdr datail(String ids) throws  Exception{
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
       UserInfo userInfo = SecurityContextService.getUser();
       if (userInfo == null){
           throw new Exception(" Bar request.");
       }
        HhQdPheduyetKhMttHdr data = optional.get();
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
       data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
       data.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTkhai()));
       data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
       data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
        List<HhQdPheduyetKhMttDx> listdx=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdr(data.getId());
        for (HhQdPheduyetKhMttDx pduyetDx :listdx){
            pduyetDx.setTenLoaiVthh(StringUtils.isEmpty(pduyetDx.getLoaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getLoaiVthh()));
            pduyetDx.setTenCloaiVthh(StringUtils.isEmpty(pduyetDx.getCloaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getCloaiVthh()));
            pduyetDx.setTenChuDt(StringUtils.isEmpty(pduyetDx.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
            pduyetDx.setTenDvi(StringUtils.isEmpty(pduyetDx.getMaDvi()) ? null : hashMapDmdv.get(pduyetDx.getMaDvi()));
            List<Long> idDx=listdx.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
            List<HhQdPheduyetKhMttSLDD> listSlDd =hhQdPheduyetKhMttSLDDRepository.findAllByIdDxKhmttIn(idDx);
            for (HhQdPheduyetKhMttSLDD sldd:listSlDd){
                sldd.setTenDvi(StringUtils.isEmpty(sldd.getMaDvi()) ? null : hashMapDmdv.get(sldd.getMaDvi()));
            }
            pduyetDx.setSoLuongDiaDiemList(listSlDd);
        }
        data.setHhQdPheduyetKhMttDxList(listdx);



        return data;
       }
   @Transactional(rollbackOn = Exception.class)
   public void delete (IdSearchReq idSearchReq) throws Exception{
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)&& !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hieenh xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
        }
        HhQdPheduyetKhMttHdr data = optional.get();
       if (data.getIdThop() != null){
           hhDxuatKhMttThopRepository.updateTrangThai(data.getIdThop(),Contains.DADUTHAO_QD);
       }else if (data.getSoDxuat() !=null){
           Optional<HhDxuatKhMttHdr> soDxuat= hhDxuatKhMttRepository.findBySoDxuat(data.getSoDxuat());
           soDxuat.get().setSoQdPduyet(data.getSoQdPduyet());
           hhDxuatKhMttRepository.save(soDxuat.get());
       }
        List<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxList=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdr(data.getId());
        List<Long>  listIdDx=hhQdPheduyetKhMttDxList.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttSLDD> listSlDd=hhQdPheduyetKhMttSLDDRepository.findAllByIdDxKhmttIn(listIdDx);
        hhQdPheduyetKhMttDxRepository.deleteAll(hhQdPheduyetKhMttDxList);
        hhQdPheduyetKhMttSLDDRepository.deleteAll(listSlDd);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_QD_PHE_DUYET_KHMTT_HDR"));
        hhQdPheduyetKhMttHdrRepository.delete(data);
   }

    @Transactional(rollbackOn = Exception.class)
    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhQdPheduyetKhMttHdr> list= hhQdPheduyetKhMttHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhQdPheduyetKhMttHdr qdPheduyetKhMttHdr : list){
            if (!qdPheduyetKhMttHdr.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
            if (qdPheduyetKhMttHdr.getIdThop() != null){
                hhDxuatKhMttThopRepository.updateTrangThai(qdPheduyetKhMttHdr.getIdThop(),Contains.DADUTHAO_QD);
            }else if (qdPheduyetKhMttHdr.getSoDxuat() !=null){
                Optional<HhDxuatKhMttHdr> soDxuat= hhDxuatKhMttRepository.findBySoDxuat(qdPheduyetKhMttHdr.getSoDxuat());
                soDxuat.get().setSoQdPduyet(qdPheduyetKhMttHdr.getSoQdPduyet());
                hhDxuatKhMttRepository.save(soDxuat.get());
            }
        }
        List<Long> listId=list.stream().map(HhQdPheduyetKhMttHdr::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttDx> listPduyet=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdrIn(listId);
        List<Long> listIdDx=listPduyet.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttSLDD> listSlDd=hhQdPheduyetKhMttSLDDRepository.findAllByIdDxKhmttIn(listIdDx);

        hhQdPheduyetKhMttDxRepository.deleteAll(listPduyet);
        hhQdPheduyetKhMttSLDDRepository.deleteAll(listSlDd);
        hhQdPheduyetKhMttHdrRepository.deleteAll(list);
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
            objs[1]=pduyet.getSoQdPduyet();
            objs[2]=pduyet.getNgayKy();
            objs[3]=pduyet.getTrichYeu();
            objs[4]=pduyet.getSoDxuat();
            objs[5]=pduyet.getMaThop();
            objs[6]=pduyet.getNamKh();
            objs[7]=pduyet.getPthucMuatt();
            objs[8]=pduyet.getTenTrangThai();
            dataList.add(objs);
     }
     ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
     ex.export();
 }

    public HhQdPheduyetKhMttHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdPheduyetKhMttHdr> optional =hhQdPheduyetKhMttHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.BAN_HANH + Contains.DUTHAO:
                optional.get().setNguoiDuyet(getUser().getUsername());
                optional.get().setNgayDuyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        optional.get().setTrangThaiTkhai(Contains.CHUACAPNHAT);
        HhQdPheduyetKhMttHdr created = hhQdPheduyetKhMttHdrRepository.save(optional.get());
        if (created.getTrangThai().equals(Contains.BAN_HANH)){
            if (created.getIdDxuat()!=null){
                hhDxuatKhMttRepository.updateSoQdPduyet(created.getIdDxuat(),created.getSoQdPduyet());
            }
            if (created.getId()!=null){
                hhDxuatKhMttThopRepository.updateTrangThai(created.getIdThop(),Contains.DABANHANH_QD);
            }
        }
        return created;
    }


}
