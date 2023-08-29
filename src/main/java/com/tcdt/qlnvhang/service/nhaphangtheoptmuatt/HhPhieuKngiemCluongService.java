package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuKnCluongDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuKngiemCluongRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhPhieuKngiemCluongPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuKnCluongDtlReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhieuKngiemCluongReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPhieuKnCluong;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKnCluongDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKngiemCluong;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhPhieuKngiemCluongService extends BaseServiceImpl {
    @Autowired
    HhPhieuKngiemCluongRepository hhPhieuKngiemCluongRepository;
    
    @Autowired
    HhPhieuKnCluongDtlRepository hhPhieuKnCluongDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhPhieuKngiemCluong> searchPage(SearchHhPhieuKnCluong objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhPhieuKngiemCluong> data = hhPhieuKngiemCluongRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQdNh(),
                objReq.getSoBb(),
                objReq.getSoPhieu(),
                Contains.convertDateToString(objReq.getNgayKnDen()),
                Contains.convertDateToString(objReq.getNgayKnDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");

        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDmdv.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDmdv.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDmdv.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDmdv.get(f.getMaLoKho()));

        });
        return data;
    }


    @Transactional
    public HhPhieuKngiemCluong save(HhPhieuKngiemCluongReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhPhieuKngiemCluong> optional = hhPhieuKngiemCluongRepository.findAllBySoBbLayMau(objReq.getSoBbLayMau());
        if(optional.isPresent()){
            throw new Exception("số phiếu đã tồn tại");
        }
        HhPhieuKngiemCluong data = new ModelMapper().map(objReq,HhPhieuKngiemCluong.class);
        data.setNgayTao(new Date());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DUTHAO);
         data.setMaDvi(userInfo.getDvql());
        HhPhieuKngiemCluong created=hhPhieuKngiemCluongRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_KNGHIEM_CLUONG");
        created.setFileDinhKems(fileDinhKems);
  
        for (HhPhieuKnCluongDtlReq list : objReq.getPhieuKnCluongDtlReqList()){
            HhPhieuKnCluongDtl dtl = ObjectMapperUtils.map(list,HhPhieuKnCluongDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhPhieuKnCluongDtlRepository.save(dtl);
        }

        return created;
    }

    @Transactional
    public HhPhieuKngiemCluong update(HhPhieuKngiemCluongReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhPhieuKngiemCluong> optional = hhPhieuKngiemCluongRepository.findById(objReq.getId());
        Optional<HhPhieuKngiemCluong> soPhieu = hhPhieuKngiemCluongRepository.findAllBySoBbLayMau(objReq.getSoBbLayMau());
        if(soPhieu.isPresent()){
            if (!soPhieu.get().getId().equals(objReq.getId())){
                throw new Exception("số phiếu đã tồn tại");
            }
        }
        HhPhieuKngiemCluong data = optional.get();
        HhPhieuKngiemCluong dataMap = new ModelMapper().map(objReq,HhPhieuKngiemCluong.class);
        updateObjectToObject(data,dataMap);
        data.setNgaySua(new Date());
        data.setNguoiSuaId(userInfo.getId());

        HhPhieuKngiemCluong created=hhPhieuKngiemCluongRepository.save(data);
        fileDinhKemService.delete(objReq.getId(),  Lists.newArrayList("HH_PHIEU_KNGHIEM_CLUONG"));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHIEU_KNGHIEM_CLUONG");
        created.setFileDinhKems(fileDinhKems);

        List<HhPhieuKnCluongDtl> listDtl=hhPhieuKnCluongDtlRepository.findAllByIdHdr(objReq.getId());
        hhPhieuKnCluongDtlRepository.deleteAll(listDtl);
        for (HhPhieuKnCluongDtlReq list : objReq.getPhieuKnCluongDtlReqList()){
            HhPhieuKnCluongDtl dtl = ObjectMapperUtils.map(list,HhPhieuKnCluongDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            hhPhieuKnCluongDtlRepository.save(dtl);
        }
        return created;
    }

    public HhPhieuKngiemCluong detail(String ids) throws Exception{
        Optional<HhPhieuKngiemCluong> optional=hhPhieuKngiemCluongRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhPhieuKngiemCluong data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenDiemKho(StringUtils.isEmpty(data.getMaDiemKho())?null:hashMapDmdv.get(data.getMaDiemKho()));
        data.setTenNhaKho(StringUtils.isEmpty(data.getMaNhaKho())?null:hashMapDmdv.get(data.getMaNhaKho()));
        data.setTenNganKho(StringUtils.isEmpty(data.getMaNganKho())?null:hashMapDmdv.get(data.getMaNganKho()));
        data.setTenLoKho(StringUtils.isEmpty(data.getMaLoKho())?null:hashMapDmdv.get(data.getMaLoKho()));
        List<HhPhieuKnCluongDtl> listDtl=hhPhieuKnCluongDtlRepository.findAllByIdHdr(data.getId());
        data.setHhPhieuKnCluongDtlList(listDtl);

        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList("HH_PHIEU_KNGHIEM_CLUONG"));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhPhieuKngiemCluong> optional= hhPhieuKngiemCluongRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhPhieuKngiemCluong data = optional.get();
        List<HhPhieuKnCluongDtl> listDtl=hhPhieuKnCluongDtlRepository.findAllByIdHdr(data.getId());
        hhPhieuKnCluongDtlRepository.deleteAll(listDtl);
        hhPhieuKngiemCluongRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhPhieuKngiemCluong> list= hhPhieuKngiemCluongRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhPhieuKngiemCluong dxuatKhMttHdr : list){
            if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDCC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdDtl= list.stream().map(HhPhieuKngiemCluong::getId).collect(Collectors.toList());
        List<HhPhieuKnCluongDtl> listDtl=hhPhieuKnCluongDtlRepository.findAllByIdHdrIn(listIdDtl);
        hhPhieuKnCluongDtlRepository.deleteAll(listDtl);
        hhPhieuKngiemCluongRepository.deleteAll(list);
    }

    public  void export(SearchHhPhieuKnCluong objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhPhieuKngiemCluong> page=this.searchPage(objReq);
        List<HhPhieuKngiemCluong> data=page.getContent();

        String title="Danh sách phiếu kiểm nghiệm lượng ";
        String[] rowsName=new String[]{"STT","Số quyết định nhập","Ngày kiểm nghiệm","Điểm kho","Nhà kho","Ngăn kho","Lô kho","Số BB LM/BGM","Ngày lấy mẫu","Số BB nhập đầy kho","Ngày nhập đầy kho","Trạng thái"};
        String fileName="danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhPhieuKngiemCluong dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoQdGiaoNvNh();
            objs[2]=dx.getNgayKnghiem();
            objs[3]=dx.getTenDiemKho();
            objs[4]=dx.getTenNhaKho();
            objs[5]=dx.getTenNganKho();
            objs[6]=dx.getTenLoKho();
            objs[7]=dx.getSoBbLayMau();
            objs[8]=dx.getNgayLayMau();
            objs[9]=dx.getSoBbNhapDayKho();
            objs[10]=dx.getNgayNhapDayKho();
            objs[11]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhPhieuKngiemCluong approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhPhieuKngiemCluong> optional =hhPhieuKngiemCluongRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                optional.get().setNguoiGuiDuyetId(userInfo.getId());
                optional.get().setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(userInfo.getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLyDoTuChoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(userInfo.getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhPhieuKngiemCluong created = hhPhieuKngiemCluongRepository.save(optional.get());
        return created;
    }

    public ReportTemplateResponse preview(HhPhieuKngiemCluongReq req) throws Exception {
        HhPhieuKngiemCluong nhBangKeVt = detail(req.getId().toString());
        if (nhBangKeVt == null) {
            throw new Exception("Phiếu kiểm nghiệm chất lượng không tồn tại.");
        }
        HhPhieuKngiemCluongPreview object = new HhPhieuKngiemCluongPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }
}
