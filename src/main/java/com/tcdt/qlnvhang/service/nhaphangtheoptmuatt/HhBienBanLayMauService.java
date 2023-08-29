package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.QuyChuanQuocGiaDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBbanLayMauDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBienBanDayKhoHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhBienBanLayMauRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBangKeVtPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhBienBanLayMauPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanLayMau;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhBienBanLayMauService extends BaseServiceImpl {
    @Autowired
    private HhBienBanLayMauRepository hhBienBanLayMauRepository;
    
    @Autowired
    private HhBbanLayMauDtlRepository hhBbanLayMauDtlRepository;
    
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private HhBienBanDayKhoHdrRepository hhBienBanDayKhoHdrRepository;

    @Autowired
    private QuyChuanQuocGiaDtlRepository quyChuanQuocGiaDtlRepository;


    public Page<HhBienBanLayMau> searchPage(SearchHhBbanLayMau objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhBienBanLayMau> data = hhBienBanLayMauRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoBb(),
                objReq.getSoQdNh(),
                objReq.getDviKn(),
                Contains.convertDateToString(objReq.getNgayLayMauTu()),
                Contains.convertDateToString(objReq.getNgayLayMauDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDmdv.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDmdv.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDmdv.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDmdv.get(f.getMaLoKho()));

        });
        return data;
    }

    @Transient
    public HhBienBanLayMau save(HhBienBanLayMauReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhBienBanLayMau> optional = hhBienBanLayMauRepository.findBySoBienBan(objReq.getSoBienBan());
        if(optional.isPresent()){
            throw new Exception("số biên bản đã tồn tại");
        }
        HhBienBanLayMau data = new ModelMapper().map(objReq,HhBienBanLayMau.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        HhBienBanLayMau created=hhBienBanLayMauRepository.save(data);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem= fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_BIEN_BAN_LAY_MAU");
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_BIEN_BAN_LAY_MAU");
        created.setFileDinhKems(fileDinhKems);

        for (HhBbanLayMauDtlReq listDtl : objReq.getBbanLayMauDtlList()){
            HhBbanLayMauDtl dtl = ObjectMapperUtils.map(listDtl, HhBbanLayMauDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhBbanLayMauDtlRepository.save(dtl);
        }
        return created;
    }

    @Transactional
    public HhBienBanLayMau update(HhBienBanLayMauReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhBienBanLayMau> optional = hhBienBanLayMauRepository.findById(objReq.getId());

        Optional<HhBienBanLayMau> soBienBan = hhBienBanLayMauRepository.findBySoBienBan(objReq.getSoBienBan());
        if (soBienBan.isPresent()){
            if (!soBienBan.get().getId().equals(objReq.getId())){
                throw new Exception("số biên bản đã tồn tại");
            }
        }
        HhBienBanLayMau data = optional.get();
        HhBienBanLayMau dataMap = new ModelMapper().map(objReq,HhBienBanLayMau.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhBienBanLayMau created=hhBienBanLayMauRepository.save(data);
        fileDinhKemService.delete(objReq.getId(),  Lists.newArrayList("HH_BIEN_BAN_LAY_MAU"));
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem= fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_BIEN_BAN_LAY_MAU");
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_BIEN_BAN_LAY_MAU");
        created.setFileDinhKems(fileDinhKems);

        List<HhBbanLayMauDtl> hhBbanLayMauDtls = hhBbanLayMauDtlRepository.findAllByIdHdr(objReq.getId());
        hhBbanLayMauDtlRepository.deleteAll(hhBbanLayMauDtls);
        for (HhBbanLayMauDtlReq listDtl : objReq.getBbanLayMauDtlList()){
            HhBbanLayMauDtl dtl = ObjectMapperUtils.map(listDtl, HhBbanLayMauDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhBbanLayMauDtlRepository.save(dtl);
        }
        return created;
    }

    public HhBienBanLayMau detaiBySoQd(HhBienBanLayMauReq obj) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Optional<HhBienBanLayMau> optional=hhBienBanLayMauRepository.findBySoBienBan(obj.getSoBbLayMau() != null ? obj.getSoBbLayMau() : obj.getSoBienBan());

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhBienBanLayMau data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");

        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho()) ? null : hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho()) ? null : hashMapDmdv.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho()) ? null : hashMapDmdv.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho()) ? null : hashMapDmdv.get(data.getMaLoKho()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_LAY_MAU"));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_LAY_MAU"));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }
        if(!ObjectUtils.isEmpty(data.getIdBbNhapDayKho())){
            Optional<HhBienBanDayKhoHdr> byId = hhBienBanDayKhoHdrRepository.findById(data.getIdBbNhapDayKho());
            byId.ifPresent(data::setBbNhapDayKho);
        }
        List<HhBbanLayMauDtl> listDtl = hhBbanLayMauDtlRepository.findAllByIdHdr(data.getId());
        data.setBbanLayMauDtlList(listDtl);
        return data;
    }

    public HhBienBanLayMau detail(String ids) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Optional<HhBienBanLayMau> optional=hhBienBanLayMauRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhBienBanLayMau data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");

        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho()) ? null : hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho()) ? null : hashMapDmdv.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho()) ? null : hashMapDmdv.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho()) ? null : hashMapDmdv.get(data.getMaLoKho()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_LAY_MAU"));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList("HH_BIEN_BAN_LAY_MAU"));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }
        if(!ObjectUtils.isEmpty(data.getIdBbNhapDayKho())){
            Optional<HhBienBanDayKhoHdr> byId = hhBienBanDayKhoHdrRepository.findById(data.getIdBbNhapDayKho());
            byId.ifPresent(data::setBbNhapDayKho);
        }
        List<HhBbanLayMauDtl> listDtl = hhBbanLayMauDtlRepository.findAllByIdHdr(data.getId());
        data.setBbanLayMauDtlList(listDtl);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhBienBanLayMau> optional= hhBienBanLayMauRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhBienBanLayMau data = optional.get();
        List<HhBbanLayMauDtl> listDtl=hhBbanLayMauDtlRepository.findAllByIdHdr(data.getId());
        hhBbanLayMauDtlRepository.deleteAll(listDtl);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_BIEN_BAN_LAY_MAU"));
        hhBienBanLayMauRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhBienBanLayMau> list= hhBienBanLayMauRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhBienBanLayMau dxuatKhMttHdr : list){
            if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listId=list.stream().map(HhBienBanLayMau::getId).collect(Collectors.toList());
        List<HhBbanLayMauDtl> listDtl=hhBbanLayMauDtlRepository.findAllByIdHdrIn(listId);
        hhBbanLayMauDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_BIEN_BAN_LAY_MAU"));
        hhBienBanLayMauRepository.deleteAll(list);
    }

    public  void export(SearchHhBbanLayMau objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhBienBanLayMau> page=this.searchPage(objReq);
        List<HhBienBanLayMau> data=page.getContent();

        String title="Danh sách biên bản lấy mẫu bàn giao mẫu";
        String[] rowsName=new String[]{"STT","Số biên bản","Số quyết định nhập hàng","Ngày lấy mẫu","Số hợp đồng","Điểm kho","Nhà kho","Ngăn kho","Lô kho","Trạng thái",};
        String fileName="danh-sach-bien-ban-lay-mau-ban-giao-mau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhBienBanLayMau dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoBienBan();
            objs[2]=dx.getSoQdGiaoNvNh();
            objs[3]=dx.getNgayLayMau();
            objs[4]=dx.getSoHd();
            objs[5]=dx.getTenDiemKho();
            objs[6]=dx.getTenNhaKho();
            objs[7]=dx.getTenNganKho();
            objs[8]=dx.getTenLoKho();
            objs[9]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhBienBanLayMau approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhBienBanLayMau> optional =hhBienBanLayMauRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiGduyet(userInfo.getUsername());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuChoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhBienBanLayMau created = hhBienBanLayMauRepository.save(optional.get());
        return created;
    }

    public List<QuyChuanQuocGiaDtl> getAllQuyChuanByCloaiVthh(String loaiVthh) throws Exception {
        return quyChuanQuocGiaDtlRepository.getAllQuyChuanByCloaiVthh(loaiVthh).isEmpty() ? new ArrayList<>() : quyChuanQuocGiaDtlRepository.getAllQuyChuanByCloaiVthh(loaiVthh);
    }

    public ReportTemplateResponse preview(HhBienBanLayMauReq req) throws Exception {
        HhBienBanLayMau nhBangKeVt = detail(req.getId().toString());
        if (nhBangKeVt == null) {
            throw new Exception("Bản kê nhập vật tư không tồn tại.");
        }
        HhBienBanLayMauPreview object = new HhBienBanLayMauPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
