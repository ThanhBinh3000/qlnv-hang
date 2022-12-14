package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.HhQdPheduyetKhMttHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.HhDthauNthauDuthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
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
import java.util.stream.Collectors;

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

    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;


    public Page<HhQdPheduyetKhMttDx> selectPage(SearchHhPthucTkhaiReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPheduyetKhMttDx> hhQdPheduyetKhMttDxes = hhQdPheduyetKhMttDxRepository.selectPage(
                objReq.getNamKh(),
                convertDateToString(objReq.getNgayCgiaTu()),
                convertDateToString(objReq.getNgayCgiadDen()),
                objReq.getLoaiVthh(),
                objReq.getMaDvi(),
                NhapXuatHangTrangThaiEnum.BAN_HANH.getId(),
                objReq.getTrangThaiTk(),
                objReq.getCtyCgia(),
                objReq.getPthucMuatt(),
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String,String> hashMapDmDv= getListDanhMucDvi(null, null, "01");


        hhQdPheduyetKhMttDxes.getContent().forEach(item ->{
            try {
                // Set Hdr
                HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr = hhQdPheduyetKhMttHdrRepository.findById(item.getIdQdHdr()).get();

                hhQdPheduyetKhMttHdr.setTenCloaiVthh(hashMapDmHh.get(hhQdPheduyetKhMttHdr.getCloaiVthh()));
                hhQdPheduyetKhMttHdr.setTenLoaiVthh(hashMapDmHh.get(hhQdPheduyetKhMttHdr.getLoaiVthh()));
                item.setHhQdPheduyetKhMttHdr(hhQdPheduyetKhMttHdr);

                if(!StringUtils.isEmpty(item.getSoDxuat())){
                    Optional<HhDxuatKhMttHdr> bySoDxuat = hhDxuatKhMttRepository.findBySoDxuat(item.getSoDxuat());
                    bySoDxuat.ifPresent(item::setDxuatKhMttHdr);
                }
                if(!StringUtils.isEmpty(item.getSoQdPdKqMtt())){
                    Optional<HhQdPduyetKqcgHdr> bySoQd = hhQdPduyetKqcgRepository.findAllBySoQdPdCg(item.getSoQdPdKqMtt());
                    bySoQd.ifPresent(item::setHhQdPduyetKqcgHdr);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            item.setTenLoaiVthh(hashMapDmHh.get(item.getLoaiVthh()));
            item.setTenCloaiVthh(hashMapDmHh.get(item.getCloaiVthh()));
            item.setTenDvi(hashMapDmDv.get(item.getMaDvi()));
        });
        return hhQdPheduyetKhMttDxes;
    }

    @Transactional
    public List<HhChiTietTTinChaoGia> create(HhCgiaReq objReq) throws Exception {
        Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(objReq.getIdChaoGia());
        if (!byId.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }

        Optional<HhQdPheduyetKhMttHdr> byId1 = hhQdPheduyetKhMttHdrRepository.findById(byId.get().getIdQdHdr());
        if (byId1.isPresent()){
            byId1.get().setTrangThaiTkhai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            hhQdPheduyetKhMttHdrRepository.save(byId1.get());
        }

        HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
        hhCtietTtinCgiaRepository.deleteAllByIdTkhaiKh(objReq.getIdChaoGia());
        List<HhChiTietTTinChaoGia> listDuThau = new ArrayList<>();
        for (HhChiTietTTinChaoGiaReq req : objReq.getHhChiTietTTinChaoGiaReqs()){
            HhChiTietTTinChaoGia nthauDthau = new HhChiTietTTinChaoGia();
            BeanUtils.copyProperties(req,nthauDthau,"id");
            nthauDthau.setIdTkhaiKh(objReq.getIdChaoGia());
            HhChiTietTTinChaoGia save = hhCtietTtinCgiaRepository.save(nthauDthau);
            listDuThau.add(nthauDthau);
        }
        if(hhQdPheduyetKhMttDx.getPthucMuatt().equals(Contains.UY_QUYEN)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),hhQdPheduyetKhMttDx.getId(),"HH_QD_PHE_DUYET_KHMTT_DX");
            hhQdPheduyetKhMttDx.setFileDinhKemUyQuyen(fileDinhKems);
        }
        if(hhQdPheduyetKhMttDx.getPthucMuatt().equals(Contains.MUA_LE)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),hhQdPheduyetKhMttDx.getId(),"HH_QD_PHE_DUYET_KHMTT_DX");
            hhQdPheduyetKhMttDx.setFileDinhKemMuaLe(fileDinhKems);
        }

        hhQdPheduyetKhMttDxRepository.save(hhQdPheduyetKhMttDx);
        return listDuThau;
    }


    public List<HhChiTietTTinChaoGia> detail(String ids) throws Exception {

            Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(Long.parseLong(ids));
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }


        List<HhChiTietTTinChaoGia> byIdDtGt = hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(Long.parseLong(ids));
        byIdDtGt.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return byIdDtGt;
    }







    public void approve(HhCgiaReq stReq) throws Exception {

            approveLuongThuc(stReq);


    }

    void approveLuongThuc(HhCgiaReq stReq) throws Exception {
        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin đấu thầu không tồn tại");
        }
        List<HhQdPheduyetKhMttDx> byIdQdDtl = hhQdPheduyetKhMttDxRepository.findAllByIdQdHdr(stReq.getId());

        List<HhQdPheduyetKhMttDx> collect = byIdQdDtl.stream().filter(item -> item.getTrangThai().equals(NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()){
            throw new Exception("Vui lòng cập nhật thông tin các gói thầu");
        }

        String status = stReq.getTrangThai() + optional.get().getTrangThaiTkhai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTenTrangThai(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        hhQdPheduyetKhMttHdrRepository.save(optional.get());
    }




















//    public Page<HhQdPheduyetKhMttHdr> searchPageTkhai(SearchHhPthucTkhaiReq objReq) throws Exception{
//        UserInfo userInfo= SecurityContextService.getUser();
//        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
//                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
//        Page<HhQdPheduyetKhMttHdr> data = hhQdPheduyetKhMttHdrRepository.searchPageTkhai(
//                objReq.getNamKh(),
//                Contains.convertDateToString(objReq.getNgayCgiaTu()),
//                Contains.convertDateToString(objReq.getNgayCgiadDen()),
//                objReq.getTrangThai(),
//                objReq.getTrangThaiTk(),
//                userInfo.getDvql(),
//                objReq.getCtyCgia(),
//                objReq.getPthucMuatt(),
//                pageable);
//        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
//        Map<String, String> hashMapDmDv = getListDanhMucDvi(null, null, "01");
//        data.getContent().forEach(f->{
//            f.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTkhai()));
//            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
//            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
//            f.setTenDvi(StringUtils.isEmpty(f.getTenDvi())? null : hashMapDmDv.get(f.getMaDvi()));
//        });
//        return data;
//    }
//    @Transactional
//    public HhQdPheduyetKhMttHdr save(HhQdPheduyetKhMttHdrReq objReq) throws Exception{
//        UserInfo userInfo= SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        HhQdPheduyetKhMttHdr dataMap = new ModelMapper().map(objReq,HhQdPheduyetKhMttHdr.class);
////        List<HhQdPheduyetKhMttDx> listDx=hhQdPheduyetKhMttDxRepository.findAllByIdPduyetHdr(objReq.getId());
////        for (HhQdPheduyetKhMttDx dx :listDx){
////            dataMap.setLoaiVthh(dx.getLoaiVthh());
////            dataMap.setCloaiVthh(dx.getCloaiVthh());
////            dataMap.setMoTaHangHoa(dx.getMoTaHangHoa());
////        }
//        if(dataMap.getPthucMuatt().equals(Contains.UY_QUYEN)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"HH_DX_KHMTT_HDR");
//            dataMap.setFileDinhKemUyQuyen(fileDinhKems);
//        }
//        if(dataMap.getPthucMuatt().equals(Contains.MUA_LE)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhkems(),dataMap.getId(),"HH_DX_KHMTT_HDR");
//            dataMap.setFileDinhKemMuaLe(fileDinhKems);
//        }
//        hhQdPheduyetKhMttHdrRepository.save(dataMap);
//        if(dataMap.getPthucMuatt().equals(Contains.CHAO_GIA)){
//            for (HhChiTietTTinChaoGiaReq chiTietTTinChaoGia:objReq.getHhChiTietTTinChaoGiaReqList()){
//                HhChiTietTTinChaoGia cTietCgia =new ModelMapper().map(chiTietTTinChaoGia,HhChiTietTTinChaoGia.class);
//                cTietCgia.setIdTkhaiKh(dataMap.getId());
//                cTietCgia.setLuaChonPduyet(chiTietTTinChaoGia.getLuaChon());
//                BigDecimal thanhTien= cTietCgia.getSoLuong().multiply(cTietCgia.getDonGia());
//                cTietCgia.setThanhTien(thanhTien);
//                hhCtietTtinCgiaRepository.save(cTietCgia);
//                List<FileDinhKemReq> listFile = new ArrayList<>();
//                listFile.add(chiTietTTinChaoGia.getFileDinhKems());
//                FileDinhKem fileDinhKems = fileDinhKemService.saveListFileDinhKem(listFile, cTietCgia.getId(), "HH_CTIET_TTIN_CHAO_GIA").get(0);
//                cTietCgia.setFileDinhKems(fileDinhKems);
//            }
//        }
//        hhQdPheduyetKhMttHdrRepository.updateTrangThaiTkhai(dataMap.getId(),objReq.getTrangThaiTkhai());
//        return dataMap;
//    }
//
//    public HhQdPheduyetKhMttHdr detail(String id){
//        Optional<HhQdPheduyetKhMttHdr> optional = hhQdPheduyetKhMttHdrRepository.findById(Long.parseLong(id));
//        HhQdPheduyetKhMttHdr data=optional.get();
//        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
//        Map<String, String> hashMapDmDvi = getListDanhMucDvi(null, null, "01");
//        List<HhChiTietTTinChaoGia> cTietCgia =hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(Long.parseLong(id));
//        for (HhChiTietTTinChaoGia dtl : cTietCgia){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Collections.singleton("HH_CTIET_TTIN_CHAO_GIA"));
//            dtl.setFileDinhKems(fileDinhKems.get(0));
//        }
//
//        data.setHhChiTietTTinChaoGiaList(cTietCgia);
//        data.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiTkhai()));
//        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
//        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
//        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())? null: hashMapDmDvi.get(data.getMaDvi()));
//        return data;
//    }
}
