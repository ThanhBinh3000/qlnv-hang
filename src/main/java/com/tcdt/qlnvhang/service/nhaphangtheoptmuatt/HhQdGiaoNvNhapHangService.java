package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhQdGiaoNvNhapHangService extends BaseServiceImpl {

    @Autowired
    private HhQdGiaoNvNhapHangRepository hhQdGiaoNvNhapHangRepository;

    @Autowired
    private HhQdGiaoNvNhangDtlRepository hhQdGiaoNvNhangDtlRepository;

    @Autowired
    private HhQdGiaoNvNhDdiemRepository hhQdGiaoNvNhDdiemRepository;

    @Autowired
    private HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;

    @Autowired
    private HhPhieuKiemTraChatLuongService hhPhieuKiemTraChatLuongService;

    @Autowired
    HhBienBanNghiemThuRepository hhBienBanNghiemThuRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    HhBbanNghiemThuDtlRepository hhBbanNghiemThuDtlRepository;

    public Page<HhQdGiaoNvNhapHang> searchPage(SearchHhQdGiaoNvNhReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable= PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdGiaoNvNhapHang> data = null;
        if(userInfo.getCapDvi().equalsIgnoreCase(Contains.CAP_CHI_CUC)){
            data =hhQdGiaoNvNhapHangRepository.searchPageChiCuc(
                    objReq.getNamNhap(),
                    objReq.getSoQd(),
                    objReq.getLoaiVthh(),
                    objReq.getCloaiVthh(),
                    objReq.getTrichyeu(),
                    Contains.convertDateToString(objReq.getNgayQdTu()),
                    Contains.convertDateToString(objReq.getNgayQdDen()),
                    objReq.getTrangThai(),
                    userInfo.getDvql(),
                    pageable);
        }else {
            data =hhQdGiaoNvNhapHangRepository.searchPageCuc(
                    objReq.getNamNhap(),
                    objReq.getSoQd(),
                    objReq.getLoaiVthh(),
                    objReq.getCloaiVthh(),
                    objReq.getTrichyeu(),
                    Contains.convertDateToString(objReq.getNgayQdTu()),
                    Contains.convertDateToString(objReq.getNgayQdDen()),
                    objReq.getTrangThai(),
                    userInfo.getDvql(),
                    pageable);
        }

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach( f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));

            // dtl
            List<HhQdGiaoNvNhangDtl> hhQdGiaoNvNhangDtl = hhQdGiaoNvNhangDtlRepository.findAllByIdQdHdr(f.getId());
            List<Long> listIdDtl= hhQdGiaoNvNhangDtl.stream().map(HhQdGiaoNvNhangDtl::getId).collect(Collectors.toList());
            List<HhQdGiaoNvNhDdiem> ddiemList= hhQdGiaoNvNhDdiemRepository.findAllByIdDtlIn(listIdDtl);
            // địa điểm
            for (HhQdGiaoNvNhDdiem dDiem: ddiemList){
                dDiem.setTenCuc(StringUtils.isEmpty(dDiem.getMaCuc())?null:hashMapDmdv.get(dDiem.getMaCuc()));
                dDiem.setTenChiCuc(StringUtils.isEmpty(dDiem.getMaChiCuc())?null:hashMapDmdv.get(dDiem.getMaChiCuc()));
                dDiem.setTenDiemKho(StringUtils.isEmpty(dDiem.getMaDiemKho())?null:hashMapDmdv.get(dDiem.getMaDiemKho()));
                dDiem.setTenNhaKho(StringUtils.isEmpty(dDiem.getMaNhaKho())?null:hashMapDmdv.get(dDiem.getMaNhaKho()));
                dDiem.setTenNganKho(StringUtils.isEmpty(dDiem.getMaNganKho())?null:hashMapDmdv.get(dDiem.getMaNganKho()));
                dDiem.setTenLoKho(StringUtils.isEmpty(dDiem.getMaLoKho())?null:hashMapDmdv.get(dDiem.getMaLoKho()));
                this.setDataPhieu(null,dDiem);
            }
            for (HhQdGiaoNvNhangDtl dtl : hhQdGiaoNvNhangDtl ){
                dtl.setHhQdGiaoNvNhDdiemList(ddiemList);
                // Set biên bản nghiệm thu bảo quản
                List<HhBienBanNghiemThu> bbNghiemThuBq = hhBienBanNghiemThuRepository.findByIdQdGiaoNvNhAndMaDvi(f.getId(), dtl.getMaDvi());
                bbNghiemThuBq.forEach( item ->  {
                    List<HhBbanNghiemThuDtl> listDtl = hhBbanNghiemThuDtlRepository.findAllByIdHdr(item.getId());
                    item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
                    List<HhBbanNghiemThuDtl> dtl1= listDtl.stream().filter(x -> x.getType().equals(Contains.CHU_DONG)).collect(Collectors.toList());
                    for (HhBbanNghiemThuDtl a: dtl1){
                        item.setKinhPhiThucTe(a.getTongGtri());
                    }
                    List<HhBbanNghiemThuDtl> dtl2=listDtl.stream().filter(x -> x.getType().equals(Contains.PHE_DUYET_TRUOC)).collect(Collectors.toList());
                    for (HhBbanNghiemThuDtl b: dtl2){
                        item.setKinhPhiTcPd(b.getTongGtri());
                    }


                });
                dtl.setListBienBanNghiemThuBq(bbNghiemThuBq);
            }
            f.setHhQdGiaoNvNhangDtlList(hhQdGiaoNvNhangDtl);
        });

    return data;
    }

    void setDataPhieu(HhQdGiaoNvNhangDtl dtl , HhQdGiaoNvNhDdiem ddNhap){
        if(dtl != null){

        }else{
            ddNhap.setListPhieuKtraCl(hhPhieuKiemTraChatLuongService.findAllByIdDdiemGiaoNvNh(ddNhap.getId()));
        }
    }

    @Transactional
    public HhQdGiaoNvNhapHang save(HhQdGiaoNvNhapHangReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        Optional<HhQdGiaoNvNhapHang> optional = hhQdGiaoNvNhapHangRepository.findAllBySoQd(objReq.getSoQd());
        if (optional.isPresent()){
            throw new Exception("Số quyết định đã tồn tại");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhQdGiaoNvNhapHang data= new ModelMapper().map(objReq,HhQdGiaoNvNhapHang.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        HhQdGiaoNvNhapHang created= hhQdGiaoNvNhapHangRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_QD_GIAO_NV_NHAP_HANG");
        created.setFileDinhKems(fileDinhKems);
        if (!DataUtils.isNullObject(data.getIdHd())){
//            HhHdongBkePmuahangHdr update = hhHdongBkePmuahangRepository.findAllById(data.getIdHdong());
//            update.setTrangThaiNh(data.getTenTrangThai());
//            hhHdongBkePmuahangRepository.save(update);
        }else if (!DataUtils.isNullObject(data.getIdQdPdKq())){
            HhQdPduyetKqcgHdr update= hhQdPduyetKqcgRepository.findAllById(data.getIdQdPdKq());
//            update.setTrangThaiNh(data.getTrangThai());
            hhQdPduyetKqcgRepository.save(update);
        }

        this.saveCtiet(data,objReq);
        return created;
    }

    @Transactional
    public HhQdGiaoNvNhapHang update(HhQdGiaoNvNhapHangReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if(userInfo== null){
            throw new Exception("Bad request.");
        }
        Optional<HhQdGiaoNvNhapHang> optional = hhQdGiaoNvNhapHangRepository.findById(objReq.getId());
        Optional<HhQdGiaoNvNhapHang> soQd = hhQdGiaoNvNhapHangRepository.findAllBySoQd(objReq.getSoQd());
        if (soQd.isPresent()){
            if (!soQd.get().getId().equals(objReq.getId())){
                throw new Exception("Số quyết định đã tồn tại");
            }
        }
        HhQdGiaoNvNhapHang data=optional.get();
        HhQdGiaoNvNhapHang dataMap = new ModelMapper().map(objReq,HhQdGiaoNvNhapHang.class);
        updateObjectToObject(data,dataMap);
        data.setNgaySua(new Date());
        data.setNguoiSua(userInfo.getUsername());
        HhQdGiaoNvNhapHang created= hhQdGiaoNvNhapHangRepository.save(data);
        List<HhQdGiaoNvNhangDtl> listDtl = hhQdGiaoNvNhangDtlRepository.findAllByIdQdHdr(data.getId());
        hhQdGiaoNvNhangDtlRepository.deleteAll(listDtl);
        List<Long> listId=listDtl.stream().map(HhQdGiaoNvNhangDtl::getId).collect(Collectors.toList());
        List<HhQdGiaoNvNhDdiem> listDd = hhQdGiaoNvNhDdiemRepository.findAllByIdDtlIn(listId);
        hhQdGiaoNvNhDdiemRepository.deleteAll(listDd);
        this.saveCtiet(data,objReq);
        return created;
    }
    public void saveCtiet(HhQdGiaoNvNhapHang data,HhQdGiaoNvNhapHangReq objReq){
        for(HhQdGiaoNvNhangDtlReq req : objReq.getHhQdGiaoNvNhangDtlList()){
            HhQdGiaoNvNhangDtl dtl= new ModelMapper().map(req,HhQdGiaoNvNhangDtl.class);
            dtl.setId(null);
            dtl.setIdQdHdr(data.getId());
            dtl.setTrangThai(Contains.CHUACAPNHAT);
            hhQdGiaoNvNhangDtlRepository.save(dtl);
            for (HhQdGiaoNvNhDdiemReq ddiemReq :req.getHhQdGiaoNvNhDdiemList()){
                HhQdGiaoNvNhDdiem ddiem= new ModelMapper().map(ddiemReq,HhQdGiaoNvNhDdiem.class);
                ddiem.setId(null);
                ddiem.setIdDtl(dtl.getId());
                hhQdGiaoNvNhDdiemRepository.save(ddiem);
            }

        }
    }

    public void updateDiem(HhQdGiaoNvNhapHangReq objReq)throws Exception{

        List<HhQdGiaoNvNhangDtlReq> listDtl = objReq.getHhQdGiaoNvNhangDtlList();
        for(HhQdGiaoNvNhangDtlReq dtlReq :listDtl){
            List<HhQdGiaoNvNhDdiem> list = hhQdGiaoNvNhDdiemRepository.findAllByIdDtl(dtlReq.getId());
            hhQdGiaoNvNhDdiemRepository.deleteAll(list);
            for (HhQdGiaoNvNhDdiemReq ddiemReq :dtlReq.getHhQdGiaoNvNhDdiemList()){
                HhQdGiaoNvNhDdiem ddiem= new ModelMapper().map(ddiemReq,HhQdGiaoNvNhDdiem.class);
                ddiem.setId(null);
                ddiem.setIdDtl(dtlReq.getId());
                hhQdGiaoNvNhDdiemRepository.save(ddiem);
            }
            hhQdGiaoNvNhangDtlRepository.updateTrangThai(dtlReq.getId(),dtlReq.getTrangThai());
        }
    }

    public HhQdGiaoNvNhapHang detail(String ids) throws Exception{
        Optional<HhQdGiaoNvNhapHang> optional = hhQdGiaoNvNhapHangRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        HhQdGiaoNvNhapHang data= optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenDvi(StringUtils.isEmpty(data.getTenDvi())?null:hashMapDmdv.get(data.getMaDvi()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<HhQdGiaoNvNhangDtl> listDtl = hhQdGiaoNvNhangDtlRepository.findAllByIdQdHdr(data.getId());
        List<Long> listId=listDtl.stream().map(HhQdGiaoNvNhangDtl::getId).collect(Collectors.toList());
        List<HhQdGiaoNvNhDdiem> listDd = hhQdGiaoNvNhDdiemRepository.findAllByIdDtlIn(listId);
        for (HhQdGiaoNvNhangDtl dtl : listDtl){
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())?null:hashMapDmdv.get(dtl.getMaDvi()));
            dtl.setTenDiemKho(StringUtils.isEmpty(dtl.getMaDiemKho())?null:hashMapDmdv.get(dtl.getMaDiemKho()));
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            for (HhQdGiaoNvNhDdiem dDiem : listDd){
                dDiem.setTenCuc(StringUtils.isEmpty(dDiem.getMaCuc())?null:hashMapDmdv.get(dDiem.getMaCuc()));
                dDiem.setTenChiCuc(StringUtils.isEmpty(dDiem.getMaChiCuc())?null:hashMapDmdv.get(dDiem.getMaChiCuc()));
                dDiem.setTenDiemKho(StringUtils.isEmpty(dDiem.getMaDiemKho())?null:hashMapDmdv.get(dDiem.getMaDiemKho()));
                dDiem.setTenNhaKho(StringUtils.isEmpty(dDiem.getMaNhaKho())?null:hashMapDmdv.get(dDiem.getMaNhaKho()));
                dDiem.setTenNganKho(StringUtils.isEmpty(dDiem.getMaNganKho())?null:hashMapDmdv.get(dDiem.getMaNganKho()));
                dDiem.setTenLoKho(StringUtils.isEmpty(dDiem.getMaLoKho())?null:hashMapDmdv.get(dDiem.getMaLoKho()));
                dDiem.setListPhieuKtraCl(hhPhieuKiemTraChatLuongService.findAllByIdDdiemGiaoNvNh(dDiem.getId()));
            }
            dtl.setHhQdGiaoNvNhDdiemList(listDd);
        }
        data.setHhQdGiaoNvNhangDtlList(listDtl);

        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhQdGiaoNvNhapHang> optional = hhQdGiaoNvNhapHangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhQdGiaoNvNhapHang data = optional.get();
        List<HhQdGiaoNvNhangDtl> listDtl = hhQdGiaoNvNhangDtlRepository.findAllByIdQdHdr(data.getId());
//        List<Long> listId=listDtl.stream().map(HhQdGiaoNvNhangDtl::getId).collect(Collectors.toList());
//        List<HhQdGiaoNvNhDdiem> listDd = hhQdGiaoNvNhDdiemRepository.findAllByIdDtlIn(listId);
        hhQdGiaoNvNhangDtlRepository.deleteAll(listDtl);
//        hhQdGiaoNvNhDdiemRepository.deleteAll(listDd);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_QD_GIAO_NV_NHAP_HANG"));
        hhQdGiaoNvNhapHangRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhQdGiaoNvNhapHang> list = hhQdGiaoNvNhapHangRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhQdGiaoNvNhapHang nvNhapHang : list){
            if (!nvNhapHang.getTrangThai().equals(Contains.DUTHAO)
                    && !nvNhapHang.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !nvNhapHang.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdHdr=list.stream().map(HhQdGiaoNvNhapHang::getId).collect(Collectors.toList());
        List<HhQdGiaoNvNhangDtl> listDtl = hhQdGiaoNvNhangDtlRepository.findAllByIdQdHdrIn(listIdHdr);
//        List<Long> listId=listDtl.stream().map(HhQdGiaoNvNhangDtl::getId).collect(Collectors.toList());
//        List<HhQdGiaoNvNhDdiem> listDd = hhQdGiaoNvNhDdiemRepository.findAllByIdDtlIn(listId);
        hhQdGiaoNvNhangDtlRepository.deleteAll(listDtl);
//        hhQdGiaoNvNhDdiemRepository.deleteAll(listDd);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_QD_GIAO_NV_NHAP_HANG"));
        hhQdGiaoNvNhapHangRepository.deleteAll(list);

    }

    public  void export(SearchHhQdGiaoNvNhReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdGiaoNvNhapHang> page=this.searchPage(objReq);
        List<HhQdGiaoNvNhapHang> data=page.getContent();

        String title="Danh sách đề xuất kế hoạch mua trực tiếp";
        String[] rowsName=new String[]{"STT","Số QD giao nhiệm vụ NH","Ngày quyết định","Số hơp đồng","Số QĐ phê duyệt KH","Năm nhập","Loại hàng hóa","Chủng loại hàng hóa","Trích yếu","Trạng Thái"};
        String fileName="danh-sach-dx-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdGiaoNvNhapHang dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoQd();
            objs[2]=dx.getNgayQd();
            objs[3]=dx.getTenHd();
            objs[4]=dx.getSoQdPdKh();
            objs[5]=dx.getNamNhap();
            objs[6]=dx.getTenLoaiVthh();
            objs[7]=dx.getTenCloaiVthh();
            objs[8]=dx.getTrichYeu();
            objs[9]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhQdGiaoNvNhapHang approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdGiaoNvNhapHang> optional =hhQdGiaoNvNhapHangRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHO_DUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
                optional.get().setNguoiGduyet(userInfo.getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuchoi(statusReq.getLyDo());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhQdGiaoNvNhapHang created = hhQdGiaoNvNhapHangRepository.save(optional.get());
        return created;
    }

}
