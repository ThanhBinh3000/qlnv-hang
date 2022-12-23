package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDcDiaDiemGiaoNhanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhHdongBkePmuahangRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhuLucHopDongMttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDcDiaDiemGiaoNhanHangReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhPhuLucHopDongMttReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HhPhuLucHopDongMttService extends BaseServiceImpl {
    @Autowired
    HhPhuLucHopDongMttRepository hhPhuLucHopDongMttRepository;

    @Autowired
    HhDcDiaDiemGiaoNhanHangRepository hhDcDiaDiemGiaoNhanHangRepository;

    @Autowired
    HhHdongBkePmuahangRepository hhHdongBkePmuahangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    public List<HhPhuLucHopDongMtt> findAll(HhPhuLucHopDongMttReq objReq){
        List<HhPhuLucHopDongMtt> phuLucHopDongMtt =  hhPhuLucHopDongMttRepository.findAll();
        for (HhPhuLucHopDongMtt pluc:phuLucHopDongMtt){
            pluc.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(pluc.getTrangThai()));
        }
       return phuLucHopDongMtt;
    }

    @Transactional
    public HhPhuLucHopDongMtt save(HhPhuLucHopDongMttReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhPhuLucHopDongMtt> optional=hhPhuLucHopDongMttRepository.findAllByPhuLucSo(objReq.getPhuLucSo());
        if (optional.isPresent()){
            throw new Exception("Phụ lục số "+objReq.getPhuLucSo()+"đã tồn tại");
        }
        HhPhuLucHopDongMtt data= new ModelMapper().map(objReq,HhPhuLucHopDongMtt.class);
        Optional<HhHdongBkePmuahangHdr> hd =hhHdongBkePmuahangRepository.findById(objReq.getIdHdHdr());

//        data.setTenHdong(hd.get().getTenHdong());
//        data.setSoHdong(hd.get().getSoHdong());
        data.setNgayTao(new Date());
        data.setNguoiTao(userInfo.getUsername());
        data.setTrangThai(Contains.DUTHAO);
        HhPhuLucHopDongMtt created=hhPhuLucHopDongMttRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHU_LUC_HOP_DONG_MTT");
        created.setFileDinhKems(fileDinhKems);
        for (HhDcDiaDiemGiaoNhanHangReq req : objReq.getDcDiaDiemGiaoNhanHangList()){
            HhDCDiaDiemGiaoNhanHang dcDdNh=new ModelMapper().map(req,HhDCDiaDiemGiaoNhanHang.class);
            dcDdNh.setId(null);
            dcDdNh.setIdHdPluc(data.getId());
            BigDecimal thanhTien =dcDdNh.getDonGiaVat().multiply(dcDdNh.getSoLuong());
            dcDdNh.setThanhTien(thanhTien);
            hhDcDiaDiemGiaoNhanHangRepository.save(dcDdNh);
        }
        return created;
    }

    @Transactional
    public HhPhuLucHopDongMtt update(HhPhuLucHopDongMttReq objReq)throws Exception{
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhPhuLucHopDongMtt> optional=hhPhuLucHopDongMttRepository.findById(objReq.getId());
        Optional<HhPhuLucHopDongMtt> soPluc=hhPhuLucHopDongMttRepository.findAllByPhuLucSo(objReq.getPhuLucSo());
        if (soPluc.isPresent()){
            if (soPluc.get().getId().equals(objReq.getId())){
                throw new Exception("Phụ lục số "+objReq.getPhuLucSo()+"đã tồn tại");
            }
        }
        HhPhuLucHopDongMtt data = optional.get();
        HhPhuLucHopDongMtt dataMap= new ModelMapper().map(objReq,HhPhuLucHopDongMtt.class);
        updateObjectToObject(data,dataMap);
        data.setNguoiSua(userInfo.getUsername());
        data.setNgaySua(new Date());
        HhPhuLucHopDongMtt created=hhPhuLucHopDongMttRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_PHU_LUC_HOP_DONG_MTT");
        created.setFileDinhKems(fileDinhKems);
        List<HhDCDiaDiemGiaoNhanHang> listDdNh=hhDcDiaDiemGiaoNhanHangRepository.findAllByIdHdPluc(objReq.getId());
        hhDcDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);

        for (HhDcDiaDiemGiaoNhanHangReq req : objReq.getDcDiaDiemGiaoNhanHangList()){
            HhDCDiaDiemGiaoNhanHang dcDdNh=new ModelMapper().map(req,HhDCDiaDiemGiaoNhanHang.class);
            dcDdNh.setId(null);
            dcDdNh.setIdHdPluc(data.getId());
            BigDecimal thanhTien =dcDdNh.getDonGiaVat().multiply(dcDdNh.getSoLuong());
            dcDdNh.setThanhTien(thanhTien);
            hhDcDiaDiemGiaoNhanHangRepository.save(dcDdNh);
        }
        return created;
    }

    public HhPhuLucHopDongMtt detail(String ids) throws Exception{
        Optional<HhPhuLucHopDongMtt> optional = hhPhuLucHopDongMttRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        HhPhuLucHopDongMtt data= optional.get();
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<HhDCDiaDiemGiaoNhanHang> listDdNh=hhDcDiaDiemGiaoNhanHangRepository.findAllByIdHdPluc(data.getId());
        data.setDcDiaDiemGiaoNhanHangList(listDdNh);

        return data;
    }


    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception{
        Optional<HhPhuLucHopDongMtt> optional= hhPhuLucHopDongMttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        HhPhuLucHopDongMtt data = optional.get();
        List<HhDCDiaDiemGiaoNhanHang> listDdNh=hhDcDiaDiemGiaoNhanHangRepository.findAllByIdHdPluc(data.getId());
        hhDcDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
        hhPhuLucHopDongMttRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
        List<HhPhuLucHopDongMtt> list= hhPhuLucHopDongMttRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Bản ghi không tồn tại");
        }
        for (HhPhuLucHopDongMtt hhPhuLucHopDongMtt : list){
            if (!hhPhuLucHopDongMtt.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }
        List<Long> listIdPluc = list.stream().map(HhPhuLucHopDongMtt::getId).collect(Collectors.toList());
        List<HhDCDiaDiemGiaoNhanHang> listDdNh=hhDcDiaDiemGiaoNhanHangRepository.findAllByIdHdPlucIn(listIdPluc);
        hhDcDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
        hhPhuLucHopDongMttRepository.deleteAll(list);

    }
}
