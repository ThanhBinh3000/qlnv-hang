package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhCtietTtinCgiaRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttDxRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKhMttHdrRepository;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.util.Contains;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class HhPthucTkhaiMuaTtService extends BaseServiceImpl {
    @Autowired
    HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    HhCtietTtinCgiaRepository hhCtietTtinCgiaRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;


    public Page<HhQdPheduyetKhMttHdr> searchPageTkhai(SearchHhPthucTkhaiReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchPageTkhai(
                objReq.getNamKh(),
                Contains.convertDateToString(objReq.getNgayCgiaTu()),
                Contains.convertDateToString(objReq.getNgayCgiadDen()),
                objReq.getTrangThai(),
                objReq.getTrangThaiTk(),
                userInfo.getDvql(),
                objReq.getCtyCgia(),
                objReq.getPthucMuatt(),
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmDv = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f->{
            f.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTkhai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
            f.setTenDvi(StringUtils.isEmpty(f.getTenDvi())? null : hashMapDmDv.get(f.getMaDvi()));
        });
        return data;
    }
    @Transactional
    public HhQdPheduyetKhMttHdr save(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        HhQdPheduyetKhMttHdr dataMap = new ModelMapper().map(objReq,HhQdPheduyetKhMttHdr.class);
        List<HhQdPheduyetKhMttDx> listDx=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdr(objReq.getId());
        for (HhQdPheduyetKhMttDx dx :listDx){
            dataMap.setLoaiVthh(dx.getLoaiVthh());
            dataMap.setCloaiVthh(dx.getCloaiVthh());
            dataMap.setMoTaHangHoa(dx.getMoTaHangHoa());
        }
        if(dataMap.getPthucMuatt().equals(Contains.UY_QUYEN)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"HH_DX_KHMTT_HDR");
            dataMap.setFileDinhKemUyQuyen(fileDinhKems);
        }
        if(dataMap.getPthucMuatt().equals(Contains.MUA_LE)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"HH_DX_KHMTT_HDR");
            dataMap.setFileDinhKemMuaLe(fileDinhKems);
        }
        hhQdPheduyetKhMttHdrRepository.save(dataMap);
        if(dataMap.getPthucMuatt().equals(Contains.CHAO_GIA)){
            for (HhChiTietTTinChaoGiaReq chiTietTTinChaoGia:objReq.getHhChiTietTTinChaoGiaReqList()){
                HhChiTietTTinChaoGia cTietCgia =new ModelMapper().map(chiTietTTinChaoGia,HhChiTietTTinChaoGia.class);
                cTietCgia.setIdTkhaiKh(dataMap.getId());
                cTietCgia.setLuaChonPduyet(chiTietTTinChaoGia.getLuaChon());
                BigDecimal thanhTien= cTietCgia.getSoLuong().multiply(cTietCgia.getDgiaChuaThue());
                cTietCgia.setThanhTien(thanhTien);
                hhCtietTtinCgiaRepository.save(cTietCgia);
                List<FileDinhKemReq> listFile = new ArrayList<>();
                listFile.add(chiTietTTinChaoGia.getFileDinhKems());
                FileDinhKem fileDinhKems = fileDinhKemService.saveListFileDinhKem(listFile, cTietCgia.getId(), "HH_CTIET_TTIN_CHAO_GIA").get(0);
                cTietCgia.setFileDinhKems(fileDinhKems);
            }
        }
        hhQdPheduyetKhMttHdrRepository.updateTrangThaiTkhai(dataMap.getId(),objReq.getTrangThaiTkhai());
        return dataMap;
    }

    public HhQdPheduyetKhMttHdr detail(String id){
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(Long.parseLong(id));
        HhQdPheduyetKhMttHdr data=optional.get();
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDmDvi = getListDanhMucDvi(null, null, "01");
        List<HhChiTietTTinChaoGia> cTietCgia =hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(Long.parseLong(id));
        for (HhChiTietTTinChaoGia dtl : cTietCgia){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Collections.singleton("HH_CTIET_TTIN_CHAO_GIA"));
            dtl.setFileDinhKems(fileDinhKems.get(0));
        }

        data.setHhChiTietTTinChaoGiaList(cTietCgia);
        data.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTkhai()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())? null: hashMapDmDvi.get(data.getMaDvi()));
        return data;
    }
}
