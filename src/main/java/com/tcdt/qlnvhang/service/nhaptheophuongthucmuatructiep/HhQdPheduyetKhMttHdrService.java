package com.tcdt.qlnvhang.service.nhaptheophuongthucmuatructiep;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep.HhQdPheduyetKhMttDxRepository;
import com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep.HhQdPheduyetKhMttHdrRepository;
import com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep.HhQdPheduyetKhMttSLDDRepository;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
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
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                objReq.getTrangThaiTh(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();

        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }


    @Transactional
    public HhQdPheduyetKhMttHdr save(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQd());
        if(optional.isPresent()){
            throw new Exception("số quyết định đã tồn tại");
        }
        HhQdPheduyetKhMttHdr data = new ModelMapper().map(objReq,HhQdPheduyetKhMttHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        data.setMaDvi(userInfo.getDvql());
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        HhQdPheduyetKhMttHdr created=hhQdPheduyetKhMttHdrRepository.save(data);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_QD_PHE_DUYET_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);

        for (HhQdPheduyetKhMttSLDDReq listSLDD : objReq.getHhQdPheduyetKhMttSLDDList()){
            HhQdPheduyetKhMttSLDD mttSLDD =ObjectMapperUtils.map(listSLDD, HhQdPheduyetKhMttSLDD.class);
            mttSLDD.setIdQdKhmtt(data.getId());
            mttSLDD.setMaDiemKho(userInfo.getDvql());
//            BigDecimal thanhTien = mttSLDD.getSlChitieuKh().multiply(mttSLDD.getSlDxMuatt());
//            mttSLDD.setThanhTien(thanhTien);
            hhQdPheduyetKhMttSLDDRepository.save(mttSLDD);
        }
        for (HhQdPheduyetKhMttDxReq listDx : objReq.getHhQdPheduyetKhMttDxList()){
            HhQdPheduyetKhMttDx mttDx =ObjectMapperUtils.map(listDx, HhQdPheduyetKhMttDx.class);
            mttDx.setIdQdKhmtt(data.getId());
            hhQdPheduyetKhMttDxRepository.save(mttDx);
        }
        return created;
    }

   @Transactional
    public HhQdPheduyetKhMttHdr updata(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(objReq.getId());
         Optional<HhQdPheduyetKhMttHdr> optional1 = hhQdPheduyetKhMttHdrRepository.findBySoQd(objReq.getSoQd());
         if (optional1.isPresent()){
             if (optional1.isPresent()){
                 if (!optional1.get().getId().equals(objReq.getId())){
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

         List<HhQdPheduyetKhMttSLDD> hhQdPheduyetKhMttSLDDS = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdKhmtt(objReq.getId());
         hhQdPheduyetKhMttSLDDRepository.deleteAll(hhQdPheduyetKhMttSLDDS);

         for (HhQdPheduyetKhMttSLDDReq listSLDD : objReq.getHhQdPheduyetKhMttSLDDList()){
           HhQdPheduyetKhMttSLDD mttSLDD =ObjectMapperUtils.map(listSLDD,HhQdPheduyetKhMttSLDD.class);
           mttSLDD.setIdQdKhmtt(data.getId());
           mttSLDD.setMaDiemKho(userInfo.getDvql());
//           BigDecimal thanhTien = mttSLDD.getSlChitieuKh().multiply(mttSLDD.getSlDxMuatt());
//           mttSLDD.setThanhTien(thanhTien);
           hhQdPheduyetKhMttSLDDRepository.save(mttSLDD);
       }
       for (HhQdPheduyetKhMttDxReq listDx : objReq.getHhQdPheduyetKhMttDxList()){
           HhQdPheduyetKhMttDx mttDx =ObjectMapperUtils.map(listDx, HhQdPheduyetKhMttDx.class);
           mttDx.setIdQdKhmtt(data.getId());
           hhQdPheduyetKhMttDxRepository.save(mttDx);
       }
       return cerated;
   }


   public HhQdPheduyetKhMttHdr datail(String ids) throws  Exception{
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bane ghi không tồn tại");
        }
        HhQdPheduyetKhMttHdr data = optional.get();
        Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");

        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTh()));

        List<HhQdPheduyetKhMttSLDD> listSLDD = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdKhmtt(data.getId());
       for (HhQdPheduyetKhMttSLDD SLDD: listSLDD) {
           SLDD.setTenDvi(StringUtils.isEmpty(SLDD.getMaDvi())? null:hashMapDmdv.get(data.getMaDvi()));
           SLDD.setDiaDiemKho(StringUtils.isEmpty(SLDD.getMaDiemKho()) ? null : hashMapDmdv.get(SLDD.getMaDiemKho()));

       }
       data.setHhQdPheduyetKhMttSLDDList(listSLDD);

       List<HhQdPheduyetKhMttDx> listDx = hhQdPheduyetKhMttDxRepository.findAllByIdQdKhmtt(data.getId());
       List<Long> listIdDx = listDx.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
       data.setHhQdPheduyetKhMttDxList(listDx);

       return data;
   }

   public void delete (IdSearchReq idSearchReq) throws Exception{
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)&& !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hieenh xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
        }
        HhQdPheduyetKhMttHdr data = optional.get();
        List<HhQdPheduyetKhMttDx> ListDx = hhQdPheduyetKhMttDxRepository.findAllByIdQdKhmtt(data.getId());
        List<Long> mttDx = ListDx.stream().map(HhQdPheduyetKhMttDx::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttSLDD> listSLDD = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdKhmtt(data.getId());
        hhQdPheduyetKhMttDxRepository.deleteAll(ListDx);
        hhQdPheduyetKhMttSLDDRepository.deleteAll(listSLDD);
        hhQdPheduyetKhMttHdrRepository.delete(data);
   }


    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhQdPheduyetKhMttHdr> list= hhQdPheduyetKhMttHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhQdPheduyetKhMttHdr qdPheduyetKhMttHdr : list){
            if (qdPheduyetKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && qdPheduyetKhMttHdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && qdPheduyetKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listId=list.stream().map(HhQdPheduyetKhMttHdr::getId).collect(Collectors.toList());
        List<HhQdPheduyetKhMttDx> listMttDx=hhQdPheduyetKhMttDxRepository.findAllByIdQdKhmttIn(listId);
        List<HhQdPheduyetKhMttSLDD>  listSLDD = hhQdPheduyetKhMttSLDDRepository.findAllByIdQdKhmttIn(listId);
        hhQdPheduyetKhMttDxRepository.deleteAll(listMttDx);
        hhQdPheduyetKhMttSLDDRepository.deleteAll(listSLDD);
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
     String[] rowsName =new String[]{"STT","Số quyết định","Ngày tạo","Trích yếu","Số đề xuất/ tờ trình","Mã tổng hợp","Năm kế hoạch","Phương thức mua trực tiếp","Trạng Thái"};
     String fileName="danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPheduyetKhMttHdr MTT=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=MTT.getSoQd();
            objs[2]=MTT.getNamKh();
            objs[3]=MTT.getNgayTao();
            objs[4]=MTT.getTrichYeu();
            objs[5]=MTT.getSoDxuat();
            objs[6]=MTT.getMaTongHop();
            objs[7]=MTT.getPhuongThucMua();
            objs[8]=MTT.getTrangThai();
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
            case Contains.CHO_DUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
                optional.get().setNguoiGuiDuyet(userInfo.getUsername());
                optional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLyDoTuChoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhQdPheduyetKhMttHdr created = hhQdPheduyetKhMttHdrRepository.save(optional.get());
        return created;
    }


}
