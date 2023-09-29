package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuKiemTraChatLuongRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuKiemTraCluongDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhPhieuKiemTraChatLuongPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuKiemTraChatLuongReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKiemTraChatLuong;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhPhieuKiemTraChatLuongService extends BaseServiceImpl {

    @Autowired
    private HhPhieuKiemTraChatLuongRepository hhPhieuKiemTraChatLuongRepository;

    @Autowired
    private HhPhieuKiemTraCluongDtlRepository hhPhieuKiemTraCluongDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private HhPhieuNhapKhoHdrRepository hhPhieuNhapKhoHdrRepository;

    public Page<HhPhieuKiemTraChatLuong> searchPage(SearchHhPhieuKiemTraChatLuong objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhPhieuKiemTraChatLuong> data = hhPhieuKiemTraChatLuongRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQd(),
                objReq.getSoPhieu(),
                Contains.convertDateToString(objReq.getNgayLphieuTu()),
                Contains.convertDateToString(objReq.getNgayLphieuDen()),
                Contains.convertDateToString(objReq.getNgayGdinhTu()),
                Contains.convertDateToString(objReq.getNgayGdinhDen()),
                objReq.getKetQua(),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDmdv.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDmdv.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDmdv.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDmdv.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDmdv.get(f.getMaLoKho()));
        });
        return data;
    }
    
    
    @Transactional
    public HhPhieuKiemTraChatLuong save(HhPhieuKiemTraChatLuongReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhPhieuKiemTraChatLuong> optional = hhPhieuKiemTraChatLuongRepository.findAllBySoPhieu(objReq.getSoPhieu());
        if(optional.isPresent()){
            throw new Exception("số phiếu đã tồn tại");
        }
        HhPhieuKiemTraChatLuong data = new ModelMapper().map(objReq,HhPhieuKiemTraChatLuong.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        HhPhieuKiemTraChatLuong created=hhPhieuKiemTraChatLuongRepository.save(data);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem= fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_PHIEU_KT_CHAT_LUONG");
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_KT_CHAT_LUONG");
        created.setFileDinhKems(fileDinhKems);

        for (HhPhieuKiemTraChatLuongDtlReq list : objReq.getPhieuKiemTraChatLuongDtlList()){
            HhPhieuKiemTraChatLuongDtl dtl = ObjectMapperUtils.map(list,HhPhieuKiemTraChatLuongDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhPhieuKiemTraCluongDtlRepository.save(dtl);
        }

        return created;
    }

    @Transactional
    public HhPhieuKiemTraChatLuong update(HhPhieuKiemTraChatLuongReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhPhieuKiemTraChatLuong> optional = hhPhieuKiemTraChatLuongRepository.findById(objReq.getId());
        Optional<HhPhieuKiemTraChatLuong> soPhieu = hhPhieuKiemTraChatLuongRepository.findAllBySoPhieu(objReq.getSoPhieu());
        if(soPhieu.isPresent()){
            if (!soPhieu.get().getId().equals(objReq.getId())){
                throw new Exception("số phiếu đã tồn tại");
            }
        }
        HhPhieuKiemTraChatLuong data = optional.get();
        HhPhieuKiemTraChatLuong dataMap = new ModelMapper().map(objReq,HhPhieuKiemTraChatLuong.class);
        updateObjectToObject(data,dataMap);
        data.setNgaySua(new Date());
        data.setNguoiSua(userInfo.getUsername());

        HhPhieuKiemTraChatLuong created=hhPhieuKiemTraChatLuongRepository.save(data);
        fileDinhKemService.delete(objReq.getId(),  Lists.newArrayList("HH_PHIEU_KT_CHAT_LUONG"));
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem=fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),"HH_PHIEU_KT_CHAT_LUONG");
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_KT_CHAT_LUONG");
        created.setFileDinhKems(fileDinhKems);

        List<HhPhieuKiemTraChatLuongDtl> listDtl=hhPhieuKiemTraCluongDtlRepository.findAllByIdHdr(objReq.getId());
        hhPhieuKiemTraCluongDtlRepository.deleteAll(listDtl);
        for (HhPhieuKiemTraChatLuongDtlReq list : objReq.getPhieuKiemTraChatLuongDtlList()){
            HhPhieuKiemTraChatLuongDtl dtl = ObjectMapperUtils.map(list,HhPhieuKiemTraChatLuongDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhPhieuKiemTraCluongDtlRepository.save(dtl);
        }

        return created;
    }

    public HhPhieuKiemTraChatLuong detail(String ids) throws Exception{
        Optional<HhPhieuKiemTraChatLuong> optional=hhPhieuKiemTraChatLuongRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhPhieuKiemTraChatLuong data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDmdv.get(data.getMaDvi()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho())?null:hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho())?null:hashMapDmdv.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho())?null:hashMapDmdv.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho())?null:hashMapDmdv.get(data.getMaLoKho()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList("HH_PHIEU_KT_CHAT_LUONG"));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList("HH_PHIEU_KT_CHAT_LUONG"));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }
        List<HhPhieuKiemTraChatLuongDtl> listDtl=hhPhieuKiemTraCluongDtlRepository.findAllByIdHdr(data.getId());
        data.setPhieuKiemTraChatLuongDtlList(listDtl);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhPhieuKiemTraChatLuong> optional= hhPhieuKiemTraChatLuongRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhPhieuKiemTraChatLuong data = optional.get();
        List<HhPhieuKiemTraChatLuongDtl> listDtl=hhPhieuKiemTraCluongDtlRepository.findAllByIdHdr(data.getId());
        hhPhieuKiemTraCluongDtlRepository.deleteAll(listDtl);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_PHIEU_KT_CHAT_LUONG"));
        hhPhieuKiemTraChatLuongRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhPhieuKiemTraChatLuong> list= hhPhieuKiemTraChatLuongRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhPhieuKiemTraChatLuong dxuatKhMttHdr : list){
            if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdDtl= list.stream().map(HhPhieuKiemTraChatLuong::getId).collect(Collectors.toList());
        List<HhPhieuKiemTraChatLuongDtl> listDtl=hhPhieuKiemTraCluongDtlRepository.findAllByIdHdrIn(listIdDtl);
        hhPhieuKiemTraCluongDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_PHIEU_KT_CHAT_LUONG"));
        hhPhieuKiemTraChatLuongRepository.deleteAll(list);
    }

    public  void export(SearchHhPhieuKiemTraChatLuong objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhPhieuKiemTraChatLuong> page=this.searchPage(objReq);
        List<HhPhieuKiemTraChatLuong> data=page.getContent();

        String title="Danh sách phiếu kiểm tra chất lượng ";
        String[] rowsName=new String[]{"STT","Số phiếu KTCL","Số QĐ giao nhiệm vụ NH","Điểm kho","Nhà kho","Ngăn Kho","Lô kho","Ngày giám định","Kết quả đánh giá","Trạng thái"};
        String fileName="danh-sach-phieu-kiem-tra-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhPhieuKiemTraChatLuong dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoPhieu();
            objs[2]=dx.getSoQdGiaoNvNh();
            objs[3]=dx.getTenDiemKho();
            objs[4]=dx.getTenNhaKho();
            objs[5]=dx.getTenNganKho();
            objs[6]=dx.getTenLoKho();
            objs[7]=dx.getNgayGdinh();
            objs[8]=dx.getKqDanhGia();
            objs[9]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhPhieuKiemTraChatLuong approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhPhieuKiemTraChatLuong> optional =hhPhieuKiemTraChatLuongRepository.findById(Long.valueOf(statusReq.getId()));
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
        HhPhieuKiemTraChatLuong created = hhPhieuKiemTraChatLuongRepository.save(optional.get());
        return created;
    }

    public List<HhPhieuKiemTraChatLuong> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
        List<HhPhieuKiemTraChatLuong> list = hhPhieuKiemTraChatLuongRepository.findByIdDdiemGiaoNvNhOrderById(idDdiemGiaoNvNh);
        for(HhPhieuKiemTraChatLuong pkt : list){
            pkt.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(pkt.getTrangThai()));
        }
        return setDetailList(list);
    }

    public BigDecimal getSoLuongNhapKho(Long idDdiemGiaoNvNh) {
        BigDecimal bigDecimal = hhPhieuKiemTraChatLuongRepository.soLuongNhapKho(idDdiemGiaoNvNh,NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
        return bigDecimal;
    }
    List<HhPhieuKiemTraChatLuong> setDetailList(List<HhPhieuKiemTraChatLuong> list){
        list.forEach( item -> {
            item.setPhieuNhapKhoHdr(hhPhieuNhapKhoHdrRepository.findAllBySoPhieuKtraCluong(item.getSoPhieu()));
        });
        return list;
    }

    public ReportTemplateResponse preview(HhPhieuKiemTraChatLuongReq req) throws Exception {
        HhPhieuKiemTraChatLuong hhPhieuKiemTraChatLuong = detail(req.getId().toString());
        if (hhPhieuKiemTraChatLuong == null) {
            throw new Exception("Bản kê nhập vật tư không tồn tại.");
        }
        HhPhieuKiemTraChatLuongPreview object = new HhPhieuKiemTraChatLuongPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
