package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.request.*;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
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
public class XhQdPdKhBdgService extends BaseServiceImpl {


    @Autowired
    private XhQdPdKhBdgRepository xhQdPdKhBdgRepository;

    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

    @Autowired
    private XhQdPdKhBdgPlRepository xhQdPdKhBdgPlRepository;

    @Autowired
    private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;
    
    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;
    



    public Page<XhQdPdKhBdg> searchPage(XhQdPdKhBdgSearchReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBdg> data = xhQdPdKhBdgRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQdPd(),
                objReq.getTrichYeu(),
                Contains.convertDateToString(objReq.getNgayKyQdTu()),
                Contains.convertDateToString(objReq.getNgayKyQdDen()),
                objReq.getSoDxuat(),
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                userInfo.getDvql(),
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
    public XhQdPdKhBdg create(XhQdPdKhBdgReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
        if(optional.isPresent()){
            throw new Exception("số quyết định đã tồn tại");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        XhQdPdKhBdg data = new ModelMapper().map(objReq,XhQdPdKhBdg.class);
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        XhQdPdKhBdg created=xhQdPdKhBdgRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_QD_PHE_DUYET_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);
        if (created.getIdThop() != null){
            Optional<XhThopDxKhBdg> tHop= xhThopDxKhBdgRepository.findAllBySoQdPd(data.getSoQdPd());
            tHop.get().setSoQdPd(data.getSoQdPd());
            tHop.get().setTrangThai(Contains.DADUTHAO_QD);
            xhThopDxKhBdgRepository.save(tHop.get());
        }else if (!DataUtils.isNullObject(created.getSoDxuat())){
            Optional<XhDxKhBanDauGia> soDxuat= xhDxKhBanDauGiaRepository.findBySoDxuat(created.getSoDxuat());
            soDxuat.get().setSoQdPd(created.getSoQdPd());
            xhDxKhBanDauGiaRepository.save(soDxuat.get());
        }
        for (XhQdPdKhBdgDtlReq listDx :objReq.getQdPdKhBdgDtlReq()){
            XhQdPdKhBdgDtl dx = ObjectMapperUtils.map(listDx, XhQdPdKhBdgDtl.class);
            dx.setId(null);
            dx.setIdDxuat(data.getIdDxuat());
            dx.setSoDxuat(data.getSoDxuat());
            dx.setIdQdPd(data.getId());
            xhQdPdKhBdgDtlRepository.save(dx);
            for (XhQdPdKhBdgPlReq plReq : listDx.getXhQdPdKhBdgPlReq()){
                XhQdPdKhBdgPl pl =ObjectMapperUtils.map(plReq, XhQdPdKhBdgPl.class);
                pl.setId(null);
                pl.setIdQdPdDtl(dx.getId());
                pl.setMaDiemKho(userInfo.getDvql());
                xhQdPdKhBdgPlRepository.save(pl);
                for (XhQdPdKhBdgPlDtlReq plDtlReq : plReq.getQdPdKhBdgPlDtlReq()){
                    XhQdPdKhBdgPlDtl slddDtl = ObjectMapperUtils.map(plDtlReq,XhQdPdKhBdgPlDtl.class);
                    slddDtl.setId(null);
                    slddDtl.setIdPl(pl.getId());
                    slddDtl.setGiaKhoiDiem(slddDtl.getGiaKhongVat().multiply(slddDtl.getSoLuong()));
                    slddDtl.setTienDatTruoc(slddDtl.getGiaKhoiDiem().multiply(dx.getKhoanTienDatTruoc().divide(new BigDecimal(100))));
                    xhQdPdKhBdgPlDtlRepository.save(slddDtl);
                }
            }
        }
        return created;
    }

    @Transactional
    public XhQdPdKhBdg update(XhQdPdKhBdgReq objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(objReq.getId());
        Optional<XhQdPdKhBdg> soQdPd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
            if (soQdPd.isPresent()){
                if (!soQdPd.get().getId().equals(objReq.getId())){
                    throw new Exception("Số quyết định đã tồn tại");
                }
        }
        XhQdPdKhBdg data = optional.get();
        XhQdPdKhBdg dataMap = new ModelMapper().map(objReq, XhQdPdKhBdg.class);
        updateObjectToObject(data,dataMap);
        XhQdPdKhBdg cerated = xhQdPdKhBdgRepository.save(data);
        List<XhQdPdKhBdgDtl> qdPdKhBdgDtlList=xhQdPdKhBdgDtlRepository.findAllByIdQdPd(data.getId());
        List<Long> listIdDtl =qdPdKhBdgDtlList.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> qdPdKhBdgPlList=xhQdPdKhBdgPlRepository.findAllByIdQdPdDtlIn(listIdDtl);
        List<Long> listIdPl =qdPdKhBdgPlList.stream().map(XhQdPdKhBdgPl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPlDtl> qdPdKhBdgPlDtlList=xhQdPdKhBdgPlDtlRepository.findAllByIdPlIn(listIdPl);
        xhQdPdKhBdgDtlRepository.deleteAll(qdPdKhBdgDtlList);
        xhQdPdKhBdgPlRepository.deleteAll(qdPdKhBdgPlList);
        xhQdPdKhBdgPlDtlRepository.deleteAll(qdPdKhBdgPlDtlList);
        for (XhQdPdKhBdgDtlReq listDx :objReq.getQdPdKhBdgDtlReq()){
            XhQdPdKhBdgDtl dx = ObjectMapperUtils.map(listDx, XhQdPdKhBdgDtl.class);
            dx.setId(null);
            dx.setIdDxuat(data.getIdDxuat());
            dx.setSoDxuat(data.getSoDxuat());
            dx.setIdQdPd(data.getId());
            xhQdPdKhBdgDtlRepository.save(dx);
            for (XhQdPdKhBdgPlReq plReq : listDx.getXhQdPdKhBdgPlReq()){
                XhQdPdKhBdgPl pl =ObjectMapperUtils.map(plReq, XhQdPdKhBdgPl.class);
                pl.setId(null);
                pl.setIdQdPdDtl(dx.getId());
                pl.setMaDiemKho(userInfo.getDvql());
                xhQdPdKhBdgPlRepository.save(pl);
                for (XhQdPdKhBdgPlDtlReq plDtlReq : plReq.getQdPdKhBdgPlDtlReq()){
                    XhQdPdKhBdgPlDtl slddDtl = ObjectMapperUtils.map(plDtlReq,XhQdPdKhBdgPlDtl.class);
                    slddDtl.setId(null);
                    slddDtl.setIdPl(pl.getId());
                    slddDtl.setGiaKhoiDiem(slddDtl.getGiaKhongVat().multiply(slddDtl.getSoLuong()));
                    slddDtl.setTienDatTruoc(slddDtl.getGiaKhoiDiem().multiply(dx.getKhoanTienDatTruoc().divide(new BigDecimal(100))));
                    xhQdPdKhBdgPlDtlRepository.save(slddDtl);
                }
            }
        }

        return cerated;
    }


    public XhQdPdKhBdg detail(Long ids) throws  Exception{
        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        XhQdPdKhBdg data = optional.get();
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())? null : hashMapDmdv.get(data.getMaDvi()));
        List<XhQdPdKhBdgDtl> qdPdKhBdgDtlList=xhQdPdKhBdgDtlRepository.findAllByIdQdPd(data.getId());
        List<Long> listIdDtl =qdPdKhBdgDtlList.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> qdPdKhBdgPlList=xhQdPdKhBdgPlRepository.findAllByIdQdPdDtlIn(listIdDtl);
        List<Long> listIdPl =qdPdKhBdgPlList.stream().map(XhQdPdKhBdgPl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPlDtl> qdPdKhBdgPlDtlList=xhQdPdKhBdgPlDtlRepository.findAllByIdPlIn(listIdPl);
        for (XhQdPdKhBdgDtl pduyetDx :qdPdKhBdgDtlList){
            pduyetDx.setTenLoaiVthh(StringUtils.isEmpty(pduyetDx.getLoaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getLoaiVthh()));
            pduyetDx.setTenCloaiVthh(StringUtils.isEmpty(pduyetDx.getCloaiVthh()) ? null : hashMapDmHh.get(pduyetDx.getCloaiVthh()));
            pduyetDx.setTenDvi(StringUtils.isEmpty(pduyetDx.getMaDvi()) ? null : hashMapDmdv.get(pduyetDx.getMaDvi()));
            for (XhQdPdKhBdgPl pl :qdPdKhBdgPlList){
                pl.setTenDvi(StringUtils.isEmpty(pl.getMaDvi()) ? null : hashMapDmdv.get(pl.getMaDvi()));
                pl.setTenDiemKho(StringUtils.isEmpty(pl.getMaDiemKho()) ? null : hashMapDmdv.get(pl.getMaDiemKho()));
                for (XhQdPdKhBdgPlDtl plDtl :qdPdKhBdgPlDtlList){
                    plDtl.setTenDvi(StringUtils.isEmpty(plDtl.getMaDvi())? null:hashMapDmdv.get(plDtl.getMaDvi()));
                    plDtl.setTenDiemKho(StringUtils.isEmpty(plDtl.getMaDiemKho()) ? null : hashMapDmdv.get(plDtl.getMaDiemKho()));
                    plDtl.setTenNganKho(StringUtils.isEmpty(plDtl.getTenNganKho()) ? null : hashMapDmdv.get(plDtl.getTenNganKho()));
                    plDtl.setTenLoKho(StringUtils.isEmpty(plDtl.getTenLoKho()) ? null : hashMapDmdv.get(plDtl.getTenLoKho()));
                }
                pl.setQdPdKhBdgPlDtlList(qdPdKhBdgPlDtlList);
            }
            pduyetDx.setXhQdPdKhBdgPL(qdPdKhBdgPlList);
        }
        data.setQdPdKhBdgDtlList(qdPdKhBdgDtlList);

        return data;
    }
    @Transactional(rollbackOn = Exception.class)
    public void delete (IdSearchReq idSearchReq) throws Exception{
        Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)&& !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hieenh xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
        }
        XhQdPdKhBdg data = optional.get();
        if (data.getIdThop() != null){
            Optional<XhThopDxKhBdg> tHop= xhThopDxKhBdgRepository.findAllBySoQdPd(data.getSoQdPd());
            tHop.get().setSoQdPd(null);
            tHop.get().setTrangThai(Contains.DADUTHAO_QD);
            xhThopDxKhBdgRepository.save(tHop.get());

        }else if (data.getSoDxuat() !=null){
            Optional<XhDxKhBanDauGia> soDxuat= xhDxKhBanDauGiaRepository.findBySoDxuat(data.getSoDxuat());
            soDxuat.get().setSoQdPd(null);
            xhDxKhBanDauGiaRepository.save(soDxuat.get());
        }
        List<XhQdPdKhBdgDtl> XhQdPdKhBdgDtlList=xhQdPdKhBdgDtlRepository.findAllByIdQdPd(data.getId());
        List<Long>  listIdDx=XhQdPdKhBdgDtlList.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> listSlDd=xhQdPdKhBdgPlRepository.findAllByIdQdPdDtlIn(listIdDx);
        for (XhQdPdKhBdgPl sldd : listSlDd){
            List<XhQdPdKhBdgPlDtl> listSlddDtl = xhQdPdKhBdgPlDtlRepository.findAllByIdPl(sldd.getId());
            xhQdPdKhBdgPlDtlRepository.deleteAll(listSlddDtl);
        }
        xhQdPdKhBdgPlRepository.deleteAll(listSlDd);
        xhQdPdKhBdgDtlRepository.deleteAll(XhQdPdKhBdgDtlList);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("XH_QD_PD_KH_BDG_DTL"));
        xhQdPdKhBdgRepository.delete(data);
    }

    @Transactional(rollbackOn = Exception.class)
    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<XhQdPdKhBdg> list= xhQdPdKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (XhQdPdKhBdg qdPdKhBdg : list){
            if (!qdPdKhBdg.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
            if (!DataUtils.isNullObject(qdPdKhBdg.getIdThop())){
                Optional<XhThopDxKhBdg> tHop= xhThopDxKhBdgRepository.findAllBySoQdPd(qdPdKhBdg.getSoQdPd());
                tHop.get().setSoQdPd(null);
                tHop.get().setTrangThai(Contains.DADUTHAO_QD);
                xhThopDxKhBdgRepository.save(tHop.get());
            }else if (!DataUtils.isNullObject(qdPdKhBdg.getSoDxuat())){
                Optional<XhDxKhBanDauGia> soDxuat= xhDxKhBanDauGiaRepository.findBySoDxuat(qdPdKhBdg.getSoDxuat());
                soDxuat.get().setSoQdPd(null);
                xhDxKhBanDauGiaRepository.save(soDxuat.get());
            }
        }
        List<Long> listId=list.stream().map(XhQdPdKhBdg::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgDtl> listPduyet=xhQdPdKhBdgDtlRepository.findAllByIdQdPdIn(listId);
        List<Long> listIdDx=listPduyet.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdPdKhBdgPl> listSlDd=xhQdPdKhBdgPlRepository.findAllByIdQdPdDtlIn(listIdDx);
        for (XhQdPdKhBdgPl sldd : listSlDd){
            List<XhQdPdKhBdgPlDtl> listSlddDtl = xhQdPdKhBdgPlDtlRepository.findAllByIdPl(sldd.getId());
            xhQdPdKhBdgPlDtlRepository.deleteAll(listSlddDtl);
        }
        xhQdPdKhBdgDtlRepository.deleteAll(listPduyet);
        xhQdPdKhBdgPlRepository.deleteAll(listSlDd);
        xhQdPdKhBdgRepository.deleteAll(list);
    }

    public  void  export(XhQdPdKhBdgSearchReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhQdPdKhBdg> page=this.searchPage(objReq);
        List<XhQdPdKhBdg> data=page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName =new String[]{"STT","Năm kế hoạch","Số QĐ PD KH BĐG","ngày ký QĐ","Trích yếu","Số KH/ Tờ trình","Mã tổng hợp","Loại hàng hóa","Chủng loại hàng hóa","Số ĐV tài sản","SL HĐ đã ký","Trạng Thái"};
        String fileName="danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            XhQdPdKhBdg pduyet=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=pduyet.getNamKh();
            objs[2]=pduyet.getSoQdPd();
            objs[3]=pduyet.getNgayKyQd();
            objs[4]=pduyet.getTrichYeu();
            objs[5]=pduyet.getSoDxuat();
            objs[6]=pduyet.getMaThop();
            objs[7]=pduyet.getTenLoaiVthh();
            objs[8]=pduyet.getTenCloaiVthh();
            objs[9]=pduyet.getSoDviTsan();
            objs[10]=pduyet.getSlHdDaKy();
            objs[11]=pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public XhQdPdKhBdg approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception(" Bar request.");
        }
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhQdPdKhBdg> optional =xhQdPdKhBdgRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.BAN_HANH + Contains.DUTHAO:
                optional.get().setNguoiPduyetId(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(optional.get());
        if (created.getTrangThai().equals(Contains.BAN_HANH)){
            if (created.getIdDxuat()!=null){
                Optional<XhDxKhBanDauGia> soDxuat= xhDxKhBanDauGiaRepository.findBySoDxuat(created.getSoDxuat());
                soDxuat.get().setSoQdPd(created.getSoQdPd());
                xhDxKhBanDauGiaRepository.save(soDxuat.get());
            }
            if (created.getIdThop()!=null){
                Optional<XhThopDxKhBdg> tHop= xhThopDxKhBdgRepository.findAllBySoQdPd(created.getSoQdPd());
                tHop.get().setSoQdPd(null);
                tHop.get().setTrangThai(Contains.DABANHANH_QD);
                xhThopDxKhBdgRepository.save(tHop.get());
            }
        }
        return created;
    }

}
