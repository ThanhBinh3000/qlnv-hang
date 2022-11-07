package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhCtietTtinCgiaRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPduyetKqcgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhChiTietTTinChaoGiaReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhQdPduyetKqcgHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhQdPduyetKqcg;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhQdPduyetKqcgService extends BaseServiceImpl {

    @Autowired
    HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    HhCtietTtinCgiaRepository hhCtietTtinCgiaRepository;

    @Autowired
    HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    public Page<HhQdPduyetKqcgHdr> searchPage(SearchHhQdPduyetKqcg objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable= PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPduyetKqcgHdr> data =hhQdPduyetKqcgRepository.searchPage(
                objReq.getNamKh(),
                Contains.convertDateToString(objReq.getNgayCgiaTu()),
                Contains.convertDateToString(objReq.getNgayCgiaDen()),
                objReq.getTrangThai(),
                objReq.getMaDvi(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null, null,"01");
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
        });

        return data;
    }

    @Transactional
    public HhQdPduyetKqcgHdr save(HhQdPduyetKqcgHdrReq objReq)throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhQdPduyetKqcgHdr> optional =hhQdPduyetKqcgRepository.findAllBySoQdPdCg(objReq.getSoQdPdCg());
        if(optional.isPresent()){
            throw new Exception("Số quyết định phê duyệt đã tồn tại");
        }
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        HhQdPduyetKqcgHdr data = new ModelMapper().map(objReq,HhQdPduyetKqcgHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        HhQdPduyetKqcgHdr created= hhQdPduyetKqcgRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_QD_PDUYET_KQCG_HDR");
        created.setFileDinhKems(fileDinhKems);
        List<HhChiTietTTinChaoGia> chiTietTTinChaoGia= hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(objReq.getIdPdKh());
        for (HhChiTietTTinChaoGia cTiet: chiTietTTinChaoGia){
            cTiet.setIdSoQdPduyetCgia(data.getId());
            cTiet.setLuaChonPduyet(cTiet.getLuaChonPduyet());
            hhCtietTtinCgiaRepository.save(cTiet);
        }
//        for (HhChiTietTTinChaoGiaReq chiTietTTinChaoGia:objReq.getHhChiTietTTinChaoGiaReqList()){
//            HhChiTietTTinChaoGia tTin = ObjectMapperUtils.map(chiTietTTinChaoGia,HhChiTietTTinChaoGia.class);
//            tTin.setId(null);
//            tTin.setIdSoQdPduyetCgia(data.getId());
//            hhCtietTtinCgiaRepository.updateLcPd(chiTietTTinChaoGia.getId(),chiTietTTinChaoGia.getLuaChonPduyet());
//        }
        return created;
    }

    @Transactional
    public HhQdPduyetKqcgHdr update(HhQdPduyetKqcgHdrReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhQdPduyetKqcgHdr> optional = hhQdPduyetKqcgRepository.findById(objReq.getId());

        Optional<HhQdPduyetKqcgHdr> soQdPdCg = hhQdPduyetKqcgRepository.findAllBySoQdPdCg(objReq.getSoQdPdCg());
        if(soQdPdCg.isPresent()){
            if (soQdPdCg.isPresent()){
                if (!soQdPdCg.get().getId().equals(objReq.getId())){
                    throw new Exception("số quyết định phê duyệt đã tồn tại");
                }
            }
        }
        HhQdPduyetKqcgHdr data= optional.get();
        HhQdPduyetKqcgHdr dataMap = new ModelMapper().map(objReq,HhQdPduyetKqcgHdr.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhQdPduyetKqcgHdr created= hhQdPduyetKqcgRepository.save(data);
//        List<FileDinhKem> listfileDinhKem=fileDinhKemService.search(data.getId(), Collections.singleton("HH_QD_PDUYET_KQCG_HDR"));
//        fileDinhKemService.deleteAll(listfileDinhKem);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_QD_PDUYET_KQCG_HDR");
        created.setFileDinhKems(fileDinhKems);
        List<HhChiTietTTinChaoGia> listTtin= hhCtietTtinCgiaRepository.findAllByIdSoQdPduyetCgia(data.getId());
        hhCtietTtinCgiaRepository.deleteAll(listTtin);
        for (HhChiTietTTinChaoGiaReq chiTietTTinChaoGia:objReq.getHhChiTietTTinChaoGiaReqList()){
            HhChiTietTTinChaoGia tTin = ObjectMapperUtils.map(chiTietTTinChaoGia,HhChiTietTTinChaoGia.class);
            tTin.setId(null);
            tTin.setIdSoQdPduyetCgia(data.getId());
            hhCtietTtinCgiaRepository.updateLcPd(chiTietTTinChaoGia.getId(),chiTietTTinChaoGia.getLuaChonPduyet());
        }
        return created;
    }

    public HhQdPduyetKqcgHdr detail(String ids) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhQdPduyetKqcgHdr> optional=hhQdPduyetKqcgRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhQdPduyetKqcgHdr data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(userInfo.getTenDvi()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setFileDinhKems(data.getFileDinhKems());
        List<HhChiTietTTinChaoGia> hhChiTietTTinChaoGias = hhCtietTtinCgiaRepository.findAllByIdSoQdPduyetCgia(data.getId());
        data.setHhChiTietTTinChaoGiaList(hhChiTietTTinChaoGias);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhQdPduyetKqcgHdr> optional= hhQdPduyetKqcgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhQdPduyetKqcgHdr data = optional.get();
        List<HhChiTietTTinChaoGia>  listSlDd = hhCtietTtinCgiaRepository.findAllByIdSoQdPduyetCgia(data.getIdQdPdKh());
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_QD_PDUYET_KQCG_HDR"));
        for (HhChiTietTTinChaoGia chiTietTTinChaoGia : listSlDd){
            hhCtietTtinCgiaRepository.updateLcPd(chiTietTTinChaoGia.getId(),chiTietTTinChaoGia.getLuaChon());
        }
        hhQdPduyetKqcgRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhQdPduyetKqcgHdr> list= hhQdPduyetKqcgRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhQdPduyetKqcgHdr hhQdPduyetKqcgHdr : list ){
            if (!hhQdPduyetKqcgHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hhQdPduyetKqcgHdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !hhQdPduyetKqcgHdr.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdPdKq=list.stream().map(HhQdPduyetKqcgHdr::getId).collect(Collectors.toList());
        List<HhChiTietTTinChaoGia> listTtCgia = hhCtietTtinCgiaRepository.findAllByIdSoQdPduyetCgiaIn(listIdPdKq);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_QD_PDUYET_KQCG_HDR"));
        for (HhChiTietTTinChaoGia chiTietTTinChaoGia : listTtCgia){
            hhCtietTtinCgiaRepository.updateLcPd(chiTietTTinChaoGia.getId(),chiTietTTinChaoGia.getLuaChon());
        }
        hhQdPduyetKqcgRepository.deleteAll(list);
    }

    public void export(SearchHhQdPduyetKqcg objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdPduyetKqcgHdr> page=this.searchPage(objReq);
        List<HhQdPduyetKqcgHdr> data=page.getContent();

        String title="Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName=new String[]{"STT","Số QĐ PDKQ chào giá","Ngày ký QĐ","Đơn vị","Số QĐ PDKH mua trực tiếp","Loại hàng hóa","Chủng loại hàng hóa","Loại hàng hóa","Trạng thái"};
        String fileName="danh-sach-quyet-dinh-phe-duyet-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPduyetKqcgHdr dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoQdPdCg();
            objs[2]=dx.getNgayKy();
            objs[3]=dx.getTenDvi();
            objs[4]=dx.getSoQdPdKh();
            objs[5]=dx.getTenLoaiVthh();
            objs[6]=dx.getTenCloaiVthh();
            objs[7]=dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhQdPduyetKqcgHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdPduyetKqcgHdr> optional =hhQdPduyetKqcgRepository.findById(Long.valueOf(statusReq.getId()));
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
                optional.get().setNgayPheDuyet(getDateTimeNow());
                optional.get().setLdoTuchoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPheDuyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhQdPduyetKqcgHdr created = hhQdPduyetKqcgRepository.save(optional.get());
        return created;
    }

}
