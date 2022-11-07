package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttCcxdgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSldd;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhDxuatKhMttService extends BaseServiceImpl {
    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhDxuatKhMttSlddRepository hhDxuatKhMttSlddRepository;

    @Autowired
    private HhDxuatKhMttCcxdgRepository hhDxuatKhMttCcxdgRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    HhDxuatKhMttSlddDtlRepository hhDxuatKhMttSlddDtlRepository;



    public Page<HhDxuatKhMttHdr> searchPage(SearchHhDxKhMttHdrReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxuatKhMttHdr> data = hhDxuatKhMttRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoDxuat(),
                Contains.convertDateToString(objReq.getNgayTaoTu()),
                Contains.convertDateToString(objReq.getNgayTaoDen()),
                Contains.convertDateToString(objReq.getNgayDuyetTu()),
                Contains.convertDateToString(objReq.getNgayDuyetDen()),
                objReq.getTrichYeu(),
                objReq.getNoiDungTh(),
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
    public HhDxuatKhMttHdr save(HhDxuatKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhDxuatKhMttHdr> optional = hhDxuatKhMttRepository.findBySoDxuat(objReq.getSoDxuat());
        if(optional.isPresent()){
            throw new Exception("số đề xuất đã tồn tại");
        }
        HhDxuatKhMttHdr data = new ModelMapper().map(objReq,HhDxuatKhMttHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiTh(Contains.CHUATONGHOP);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        this.validateData(data, data.getTrangThai());
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        HhDxuatKhMttHdr created=hhDxuatKhMttRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),data.getId(),"HH_DX_KHMTT_HDR");
        created.setFileDinhKems(fileDinhKems);
        for (HhDxuatKhMttCcxdgReq listCc : objReq.getCcXdgList()){
            HhDxuatKhMttCcxdg ccxdg = new HhDxuatKhMttCcxdg();
            ccxdg =  ObjectMapperUtils.map(listCc, HhDxuatKhMttCcxdg.class);
            ccxdg.setIdDxKhmtt(data.getId());
            ccxdg =  hhDxuatKhMttCcxdgRepository.save(ccxdg);
            List<FileDinhKem> ccFildeDinhKems = fileDinhKemService.saveListFileDinhKem(listCc.getCcFileDinhkems(),ccxdg.getId(),"HH_DX_KHMTT_CCXDG");
            ccxdg.setCcFileDinhKems(ccFildeDinhKems);
            }
        for (HhDxuatKhMttSlddReq listSlDd : objReq.getSoLuongDiaDiemList()){
            HhDxuatKhMttSldd sldd =ObjectMapperUtils.map(listSlDd,HhDxuatKhMttSldd.class);
                sldd.setIdDxKhmtt(data.getId());
                sldd.setMaDiemKho(userInfo.getDvql());
                sldd.setDonGiaVat(data.getGiaCoThue());
                BigDecimal thanhTien = sldd.getDonGiaVat().multiply(sldd.getSoLuongDxmtt());
                sldd.setThanhTien(thanhTien);
                hhDxuatKhMttSlddRepository.save(sldd);
                for (HhDxuatKhMttSlddDtlReq listSlddDtl : listSlDd.getListSlddDtl()){
                    HhDxuatKhMttSlddDtl slddDtl =new ModelMapper().map(listSlddDtl,HhDxuatKhMttSlddDtl.class);
                    slddDtl.setIdSldd(sldd.getId());
                    hhDxuatKhMttSlddDtlRepository.save(slddDtl);
                }
        }
        return created;
    }

    @Transactional
    public HhDxuatKhMttHdr update(HhDxuatKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<HhDxuatKhMttHdr> optional = hhDxuatKhMttRepository.findById(objReq.getId());

        Optional<HhDxuatKhMttHdr> soDxuat = hhDxuatKhMttRepository.findBySoDxuat(objReq.getSoDxuat());
            if (soDxuat.isPresent()){
                if (!soDxuat.get().getId().equals(objReq.getId())){
                    throw new Exception("số đề xuất đã tồn tại");
                }
        }
        HhDxuatKhMttHdr data = optional.get();
        HhDxuatKhMttHdr dataMap = new ModelMapper().map(objReq,HhDxuatKhMttHdr.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhDxuatKhMttHdr created=hhDxuatKhMttRepository.save(data);

        List<HhDxuatKhMttCcxdg> hhDxuatKhMttCcxdgs = hhDxuatKhMttCcxdgRepository.findAllByIdDxKhmtt(objReq.getId());
        hhDxuatKhMttCcxdgRepository.deleteAll(hhDxuatKhMttCcxdgs);
        for (HhDxuatKhMttCcxdgReq listCc : objReq.getCcXdgList()){
            HhDxuatKhMttCcxdg ccxdg = ObjectMapperUtils.map(listCc, HhDxuatKhMttCcxdg.class);
            ccxdg.setId(null);
            ccxdg.setIdDxKhmtt(data.getId());
            hhDxuatKhMttCcxdgRepository.save(ccxdg);
            List<FileDinhKem> ccFildeDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),ccxdg.getId(),"HH_DX_KHMTT_CCXDG");
            ccxdg.setCcFileDinhKems(ccFildeDinhKems);
        }
        List<HhDxuatKhMttSldd> hhDxuatKhMttSldds= hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(objReq.getId());
        hhDxuatKhMttSlddRepository.deleteAll(hhDxuatKhMttSldds);
        for (HhDxuatKhMttSlddReq listSlDd : objReq.getSoLuongDiaDiemList()){
            HhDxuatKhMttSldd sldd =ObjectMapperUtils.map(listSlDd,HhDxuatKhMttSldd.class);
            sldd.setId(null);
            sldd.setIdDxKhmtt(data.getId());
            BigDecimal thanhTien = sldd.getDonGiaVat().multiply(sldd.getSoLuongDxmtt());
            sldd.setThanhTien(thanhTien);
            hhDxuatKhMttSlddRepository.save(sldd);
            for (HhDxuatKhMttSlddDtlReq listSlddDtl : listSlDd.getListSlddDtl()){
                HhDxuatKhMttSlddDtl slddDtl =new ModelMapper().map(listSlddDtl,HhDxuatKhMttSlddDtl.class);
                slddDtl.setId(null);
                slddDtl.setIdSldd(sldd.getId());
                hhDxuatKhMttSlddDtlRepository.save(slddDtl);
            }
        }
        return created;
    }

    public HhDxuatKhMttHdr detail(String ids) throws Exception{
        Optional<HhDxuatKhMttHdr> optional=hhDxuatKhMttRepository.findById(Long.valueOf(ids));

        if (!optional.isPresent()){
            throw new Exception("Không tồn tại bản ghi");
        }
        HhDxuatKhMttHdr data = optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");


        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTh()));
        List<FileDinhKem> fdk=fileDinhKemRepository.findByDataIdAndDataTypeIn(data.getId(),Collections.singleton("HH_DX_KHMTT_HDR"));
        data.setFileDinhKems(fdk);

        List<HhDxuatKhMttCcxdg> listCcXdg = hhDxuatKhMttCcxdgRepository.findAllByIdDxKhmtt(data.getId());
        List<Long> listIdCcXd=listCcXdg.stream().map(HhDxuatKhMttCcxdg::getId).collect(Collectors.toList());
        List<FileDinhKem> listFdk= fileDinhKemRepository.findByDataIdInAndDataTypeIn(listIdCcXd,Collections.singleton("HH_DX_KHMTT_CCXDG"));
        for (HhDxuatKhMttCcxdg  hhDxuatKhMttCcxdg :listCcXdg){
            hhDxuatKhMttCcxdg.setCcFileDinhKems(listFdk);
        }
        data.setCcXdgList(listCcXdg);

        List<HhDxuatKhMttSldd> listSlDd = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(data.getId());
        for (HhDxuatKhMttSldd sldd : listSlDd){
            sldd.setTenDvi(StringUtils.isEmpty(sldd.getMaDvi())? null:hashMapDmdv.get(sldd.getMaDvi()));
            sldd.setDiaDiemKho(StringUtils.isEmpty(sldd.getMaDiemKho()) ? null : hashMapDmdv.get(sldd.getMaDiemKho()));
            List<HhDxuatKhMttSlddDtl> listSlddDtl= hhDxuatKhMttSlddDtlRepository.findAllByIdSldd(sldd.getId());
            for (HhDxuatKhMttSlddDtl slddDtl :listSlddDtl){
                slddDtl.setTenDvi(StringUtils.isEmpty(slddDtl.getMaDvi())? null:hashMapDmdv.get(slddDtl.getMaDvi()));
                slddDtl.setDiaDiemKho(StringUtils.isEmpty(slddDtl.getMaDiemKho()) ? null : hashMapDmdv.get(slddDtl.getMaDiemKho()));
            }
        }
        data.setSoLuongDiaDiemList(listSlDd);

        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhDxuatKhMttHdr> optional= hhDxuatKhMttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhDxuatKhMttHdr data = optional.get();
        List<HhDxuatKhMttCcxdg> listCcXdg=hhDxuatKhMttCcxdgRepository.findAllByIdDxKhmtt(data.getId());
        List<Long> ccXdg=listCcXdg.stream().map(HhDxuatKhMttCcxdg::getId).collect(Collectors.toList());
        List<HhDxuatKhMttSldd>  listSlDd = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(data.getId());
        List<Long> idSlddDtl=listSlDd.stream().map(HhDxuatKhMttSldd::getId).collect(Collectors.toList());
        List<HhDxuatKhMttSlddDtl>  listSlDdDtl = hhDxuatKhMttSlddDtlRepository.findAllByIdSlddIn(idSlddDtl);
        hhDxuatKhMttCcxdgRepository.deleteAll(listCcXdg);
        hhDxuatKhMttSlddDtlRepository.deleteAll(listSlDdDtl);
        fileDinhKemRepository.deleteByDataIdInAndDataTypeIn(ccXdg,Collections.singleton("HH_DX_KHMTT_CCXDG"));
        hhDxuatKhMttSlddRepository.deleteAll(listSlDd);
        fileDinhKemService.delete(data.getId(),  Lists.newArrayList("HH_DX_KHMTT_HDR"));
        hhDxuatKhMttRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhDxuatKhMttHdr> list= hhDxuatKhMttRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhDxuatKhMttHdr dxuatKhMttHdr : list){
            if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listId=list.stream().map(HhDxuatKhMttHdr::getId).collect(Collectors.toList());
        List<HhDxuatKhMttCcxdg> listCcXdg=hhDxuatKhMttCcxdgRepository.findAllByIdDxKhmttIn(listId);
        List<Long> ccXdg=listCcXdg.stream().map(HhDxuatKhMttCcxdg::getId).collect(Collectors.toList());
        List<HhDxuatKhMttSldd>  listSlDd = hhDxuatKhMttSlddRepository.findAllByIdDxKhmttIn(listId);
        List<Long> idSlddDtl=listSlDd.stream().map(HhDxuatKhMttSldd::getId).collect(Collectors.toList());
        List<HhDxuatKhMttSlddDtl>  listSlDdDtl = hhDxuatKhMttSlddDtlRepository.findAllByIdSlddIn(idSlddDtl);
        hhDxuatKhMttSlddDtlRepository.deleteAll(listSlDdDtl);
        hhDxuatKhMttCcxdgRepository.deleteAll(listCcXdg);
        fileDinhKemRepository.deleteByDataIdInAndDataTypeIn(ccXdg,Collections.singleton("HH_DX_KHMTT_CCXDG"));
        hhDxuatKhMttSlddRepository.deleteAll(listSlDd);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),  Lists.newArrayList("HH_DX_KHMTT_HDR"));
        hhDxuatKhMttRepository.deleteAll(list);
    }

    public  void export(SearchHhDxKhMttHdrReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhDxuatKhMttHdr> page=this.searchPage(objReq);
        List<HhDxuatKhMttHdr> data=page.getContent();

        String title="Danh sách đề xuất kế hoạch mua trực tiếp";
        String[] rowsName=new String[]{"STT","Số kế hoạch/đề xuất","Năm tạo","Ngày tạo","Ngày duyệt","Trích yếu","Số QĐ giao chỉ tiêu","Loại hàng hóa","Chủng loại hàng hóa","Số lượng(tấn)","Trạng thái đề xuất","Mã tổng hợp"};
        String fileName="danh-sach-dx-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhDxuatKhMttHdr dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoDxuat();
            objs[2]=dx.getNamKh();
            objs[3]=dx.getNgayTao();
            objs[4]=dx.getNgayPduyet();
            objs[5]=dx.getTrichYeu();
            objs[6]=dx.getSoQd();
            objs[7]=dx.getTenLoaiVthh();
            objs[8]=dx.getTenCloaiVthh();
            objs[9]=dx.getTongSoLuong();
            objs[10]=dx.getTenTrangThai();
            objs[11]=dx.getTenTrangThaiTh();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhDxuatKhMttHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhDxuatKhMttHdr> optional =hhDxuatKhMttRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTrangThai();
        switch (status){
            case Contains.CHO_DUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
                this.validateData(optional.get(),Contains.CHODUYET_TP);
                optional.get().setNguoiGuiDuyet(userInfo.getUsername());
                optional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuchoi(statusReq.getLyDo());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                this.validateData(optional.get(),statusReq.getTrangThai());
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        HhDxuatKhMttHdr created = hhDxuatKhMttRepository.save(optional.get());
        return created;
    }

    public void validateData(HhDxuatKhMttHdr objHdr,String trangThai) throws Exception {
        if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            HhDxuatKhMttHdr dXuat = hhDxuatKhMttRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
            if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
                throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
            }
        }
    }
}
