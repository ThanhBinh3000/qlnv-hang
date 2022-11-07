package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDiaDiemGiaoNhanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhHdongBkePmuahangRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhuLucHopDongMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhThongTinDviDtuCcapRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhHdongBkePmuahangService extends BaseServiceImpl {

    @Autowired
    HhHdongBkePmuahangRepository hhHdongBkePmuahangRepository;

    @Autowired
    HhThongTinDviDtuCcapRepository hhThongTinDviDtuCcapRepository;

    @Autowired
    HhDiaDiemGiaoNhanHangRepository hhDiaDiemGiaoNhanHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    HhPhuLucHopDongMttRepository hhPhuLucHopDongMttRepository;

    public Page<HhHdongBkePmuahangHdr> searchPage(SearchHhHdongBkePmh objReq) throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable= PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhHdongBkePmuahangHdr> data= hhHdongBkePmuahangRepository.searchPage(
                objReq.getNamHd(),
                objReq.getSoHdong(),
                objReq.getDviMua(),
                objReq.getTenHdong(),
                Contains.convertDateToString(objReq.getNgayKyHdTu()),
                Contains.convertDateToString(objReq.getNgayKyHdDen()),
                objReq.getTrangThaiHd(),
                objReq.getTrangThaiNh(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        List<HhHdongBkePmuahangHdr> hhHdr=data.getContent();
        hhHdr.forEach(f -> {
            List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(f.getId(),Contains.CUNG_CAP);
            List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(f.getId(), Contains.DAU_TU);
            List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(f.getId());
            f.setThongTinDviCungCap(listTtCc);
            f.setThongTinChuDauTu(listTtDtu);
            f.setDiaDiemGiaoNhanHangList(listDdNh);
            f.setTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
            f.setTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiNh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDmdv.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
            for (HhThongTinDviDtuCcap dviDtu :listTtDtu){
                f.setBenMua(dviDtu.getTenDvi());
                f.setDDiemBmua(dviDtu.getDiaChi());
            }
            for (HhThongTinDviDtuCcap dviCcap :listTtCc){
                f.setBenBan(dviCcap.getTenDvi());
            }
            for (HhDiaDiemGiaoNhanHang giaTri :listDdNh){
                f.setGiaTriHd(giaTri.getThanhTien());
            }

        });
        return data;
    }

    @Transactional
    public HhHdongBkePmuahangHdr save(HhHdongBkePmuahangReq objReq) throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhHdongBkePmuahangHdr> optional =hhHdongBkePmuahangRepository.findAllBySoHdong(objReq.getSoHdong());
        if(optional.isPresent()){
            throw new Exception("Số hợp đồng đã tồn tại");
        }
        HhHdongBkePmuahangHdr data = new ModelMapper().map(objReq,HhHdongBkePmuahangHdr.class);
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThaiHd(Contains.DUTHAO);
        data.setTrangThaiNh(null);
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        data.setMaDvi(userInfo.getDvql());
        HhHdongBkePmuahangHdr created= hhHdongBkePmuahangRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_HDONG_BKE_PMUAHANG_HDR");
        created.setFileDinhKems(fileDinhKems);
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(objReq.getCanCuPhapLy(),data.getId(),"HH_HDONG_BKE_PMUAHANG_HDR");
        created.setFileDinhKems(canCuPhapLy);
        this.saveCtiet(objReq,data);
        return data;
    }

    public void saveCtiet(HhHdongBkePmuahangReq objReq, HhHdongBkePmuahangHdr data){
        for (HhThongTinDviDtuCcapReq thongTinDviDtuCcap :objReq.getThongTinChuDauTu()){
            HhThongTinDviDtuCcap thongTin=new ModelMapper().map(thongTinDviDtuCcap,HhThongTinDviDtuCcap.class);
            thongTin.setId(null);
            thongTin.setIdHdr(data.getId());
            thongTin.setType(Contains.DAU_TU);
            hhThongTinDviDtuCcapRepository.save(thongTin);
        }
        for (HhThongTinDviDtuCcapReq thongTinDviDtuCcap :objReq.getThongTinDviCungCap()){
            HhThongTinDviDtuCcap thongTin=new ModelMapper().map(thongTinDviDtuCcap,HhThongTinDviDtuCcap.class);
            thongTin.setId(null);
            thongTin.setIdHdr(data.getId());
            thongTin.setType(Contains.CUNG_CAP);
            hhThongTinDviDtuCcapRepository.save(thongTin);
        }
        for (HhDiaDiemGiaoNhanHangReq diaDiemGiaoNhanHangList: objReq.getDiaDiemGiaoNhanHangList()){
            HhDiaDiemGiaoNhanHang diaDiemGiaoNhanHang =new ModelMapper().map(diaDiemGiaoNhanHangList,HhDiaDiemGiaoNhanHang.class);
            diaDiemGiaoNhanHang.setId(null);
            diaDiemGiaoNhanHang.setIdHdr(data.getId());
            BigDecimal thanhTien =diaDiemGiaoNhanHang.getDonGiaVat().multiply(diaDiemGiaoNhanHang.getSoLuong());
            diaDiemGiaoNhanHang.setThanhTien(thanhTien);
            hhDiaDiemGiaoNhanHangRepository.save(diaDiemGiaoNhanHang);
        }
    }

    @Transactional
    public HhHdongBkePmuahangHdr update(HhHdongBkePmuahangReq objReq) throws Exception{
        UserInfo userInfo =SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhHdongBkePmuahangHdr>optional = hhHdongBkePmuahangRepository.findById(objReq.getId());
        if (!optional.isPresent()){
            throw new Exception("id không tồn tại");
        }
        Optional<HhHdongBkePmuahangHdr> soHdong =hhHdongBkePmuahangRepository.findAllBySoHdong(objReq.getSoHdong());
        if(soHdong.isPresent()){
            if(!soHdong.get().getId().equals(objReq.getId())) {
                throw new Exception("Số hợp đồng đã tồn tại");
            }
        }
        HhHdongBkePmuahangHdr data=optional.get();
        HhHdongBkePmuahangHdr dataMap = new ModelMapper().map(objReq,HhHdongBkePmuahangHdr.class);
        updateObjectToObject(data,dataMap);
        data.setNgaySua(new Date());
        data.setNguoiSua(userInfo.getUsername());
        HhHdongBkePmuahangHdr created= hhHdongBkePmuahangRepository.save(data);
        List<HhThongTinDviDtuCcap> thongTinDviDtuCcap = hhThongTinDviDtuCcapRepository.findAllByIdHdr(objReq.getId());
        List<HhDiaDiemGiaoNhanHang> diemGiaoNhanHang = hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(objReq.getId());
        hhThongTinDviDtuCcapRepository.deleteAll(thongTinDviDtuCcap);
        hhDiaDiemGiaoNhanHangRepository.deleteAll(diemGiaoNhanHang);
        this.saveCtiet(objReq,data);
        return created;
    }

    public HhHdongBkePmuahangHdr detail(String ids) throws Exception{
        Optional<HhHdongBkePmuahangHdr> optional = hhHdongBkePmuahangRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        HhHdongBkePmuahangHdr data= optional.get();
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
        data.setTentrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiNh()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(data.getMaDvi()));
        List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(data.getId(),Contains.CUNG_CAP);
        List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(data.getId(), Contains.DAU_TU);
        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(data.getId());
        data.setThongTinDviCungCap(listTtCc);
        data.setThongTinChuDauTu(listTtDtu);
        data.setDiaDiemGiaoNhanHangList(listDdNh);

        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhHdongBkePmuahangHdr> optional= hhHdongBkePmuahangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThaiHd().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        if (!optional.get().getTrangThaiNh().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhHdongBkePmuahangHdr data = optional.get();

        List<HhThongTinDviDtuCcap> listTt=hhThongTinDviDtuCcapRepository.findAllByIdHdr(data.getId());
        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(data.getId());
        List<HhPhuLucHopDongMtt> hhPhuLuc = hhPhuLucHopDongMttRepository.findAllByIdHdHdr(data.getId());
        hhPhuLucHopDongMttRepository.deleteAll(hhPhuLuc);
        hhDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
        hhThongTinDviDtuCcapRepository.deleteAll(listTt);
        hhHdongBkePmuahangRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhHdongBkePmuahangHdr> list= hhHdongBkePmuahangRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhHdongBkePmuahangHdr bkePmuahangHdr:list){
            if (!bkePmuahangHdr.getTrangThaiHd().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        for (HhHdongBkePmuahangHdr bkePmuahangHdr  : list){
            if (!bkePmuahangHdr.getTrangThaiNh().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiên xóa với quyết định trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdHdr=list.stream().map(HhHdongBkePmuahangHdr::getId).collect(Collectors.toList());
        List<HhThongTinDviDtuCcap> listTt=hhThongTinDviDtuCcapRepository.findAllByIdHdrIn(listIdHdr);
        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdrIn(listIdHdr);
        List<HhPhuLucHopDongMtt> hhPhuLuc = hhPhuLucHopDongMttRepository.findAllByIdHdHdrIn(listIdHdr);
        hhPhuLucHopDongMttRepository.deleteAll(hhPhuLuc);
        hhDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
        hhThongTinDviDtuCcapRepository.deleteAll(listTt);
        hhHdongBkePmuahangRepository.deleteAll(list);

    }

    public  void export(SearchHhHdongBkePmh objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhHdongBkePmuahangHdr> page=this.searchPage(objReq);
        List<HhHdongBkePmuahangHdr> data=page.getContent();

        String title="Danh sách hợp đồng mua trực tiếp";
        String[] rowsName=new String[]{"STT","Năm kế hoạch","Số hợp đồng","Ngày ký","Loại hang hóa","Chủng loại hàng hóa","Giá trị hợp đồng","Chủ đầu tư","Nhà thầu bên mua","Địa chỉ bên mua","Trạng thái"};
        String fileName="danh-sach-hop-dong-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhHdongBkePmuahangHdr dx=data.get(i);
            List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(dx.getId(),Contains.CUNG_CAP);
            List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(dx.getId(), Contains.DAU_TU);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getNamHd();
            objs[2]=dx.getSoHdong();
            objs[3]=dx.getNgayHluc();
            objs[4]=dx.getTenLoaiVthh();
            objs[5]=dx.getTenCloaiVthh();
            objs[6]=dx.getThanhTien();
            for (HhThongTinDviDtuCcap dt:listTtDtu){
                objs[7]=dt.getTenDvi();
            }
            for (HhThongTinDviDtuCcap cc:listTtCc){
                objs[8]=cc.getTenDvi();
                objs[9]=cc.getDiaChi();
            }
            objs[10]=dx.getTenTrangThaiHd();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public HhHdongBkePmuahangHdr approve(StatusReq statusReq) throws Exception{
        UserInfo userInfo=SecurityContextService.getUser();
        if(StringUtils.isEmpty(statusReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhHdongBkePmuahangHdr> optional =hhHdongBkePmuahangRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status= statusReq.getTrangThai()+optional.get().getTenTrangThaiHd();
        switch (status){
            case Contains.DAKY + Contains.DUTHAO:
                optional.get().setNguoiKy(getUser().getUsername());
                optional.get().setNgayKy(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThaiHd(statusReq.getTrangThai());
        HhHdongBkePmuahangHdr created = hhHdongBkePmuahangRepository.save(optional.get());
        return created;
    }

}
