package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauNthauDuthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.service.HhDauThauService;
import com.tcdt.qlnvhang.service.HhQdKhlcntHdrService;
import com.tcdt.qlnvhang.table.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HhDauThauServiceImpl extends BaseServiceImpl implements HhDauThauService {

    @Autowired
    HhQdKhlcntDtlRepository dtlRepository;
    @Autowired
    HhQdKhlcntDsgthauRepository goiThauRepository;

    @Autowired
    HhDthauNthauDuthauRepository nhaThauDuthauRepository;

    @Autowired
    HhQdKhlcntHdrService hhQdKhlcntHdrService;

    @Autowired
    HhQdKhlcntHdrRepository hhQdKhlcntHdrRepository;

    @Autowired
    HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

    @Autowired
    HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;


    @Override
    @Transactional
    public List<HhDthauNthauDuthau> create(HhDthauReq objReq) throws Exception {
        if(objReq.getLoaiVthh().startsWith("02")){
            return createVatTu(objReq);
        }else{
            return createLuongThuc(objReq);
        }
    }

    List<HhDthauNthauDuthau> createVatTu(HhDthauReq objReq) throws Exception {
        Optional<HhQdKhlcntDtl> byId = dtlRepository.findById(objReq.getIdGoiThau());
        if(!byId.isPresent()){
            throw new Exception("Gói thầu không tồn tại");
        }

        Optional<HhQdKhlcntHdr> byId1 = hhQdKhlcntHdrRepository.findById(byId.get().getIdQdHdr());
        if(byId1.isPresent()){
            byId1.get().setTrangThaiDt(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            hhQdKhlcntHdrRepository.save(byId1.get());
        }

        HhQdKhlcntDtl hhQdKhlcntDsgthau = byId.get();
        nhaThauDuthauRepository.deleteAllByIdDtGt(objReq.getIdGoiThau());
        List<HhDthauNthauDuthau> listDuThau = new ArrayList<>();
        HhDthauNthauDuthau nhaThauTt = new HhDthauNthauDuthau();
        boolean isTrungThau = false;
        for (HhDthauNthauDuthauReq req : objReq.getNthauDuThauList()){
            HhDthauNthauDuthau nthauDthau = new HhDthauNthauDuthau();
            BeanUtils.copyProperties(req,nthauDthau,"id");
            nthauDthau.setIdDtGt(objReq.getIdGoiThau());
            HhDthauNthauDuthau save = nhaThauDuthauRepository.save(nthauDthau);
            if(!isTrungThau){
                isTrungThau = save.getTrangThai().equals(NhapXuatHangTrangThaiEnum.TRUNGTHAU.getId());
                if(isTrungThau){
                    nhaThauTt = save;
                }
            }
            listDuThau.add(nthauDthau);
        }
        if(isTrungThau){
            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
            hhQdKhlcntDsgthau.setIdNhaThau(nhaThauTt.getId());
            hhQdKhlcntDsgthau.setTenNhaThau(nhaThauTt.getTenNhaThau());
            hhQdKhlcntDsgthau.setDonGiaNhaThau(nhaThauTt.getDonGia());
        }else{
            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THAT_BAI.getId());
            hhQdKhlcntDsgthau.setIdNhaThau(null);
            hhQdKhlcntDsgthau.setTenNhaThau(null);
            hhQdKhlcntDsgthau.setDonGiaNhaThau(null);
        }
        dtlRepository.save(hhQdKhlcntDsgthau);
        return listDuThau;
    }

    List<HhDthauNthauDuthau> createLuongThuc(HhDthauReq objReq) throws Exception {
        Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(objReq.getIdGoiThau());
        if(!byId.isPresent()){
            throw new Exception("Gói thầu không tồn tại");
        }

        Optional<HhQdKhlcntDtl> byId1 = dtlRepository.findById(byId.get().getIdQdDtl());
        if(byId1.isPresent()){
            byId1.get().setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            dtlRepository.save(byId1.get());
        }

        HhQdKhlcntDsgthau hhQdKhlcntDsgthau = byId.get();
        nhaThauDuthauRepository.deleteAllByIdDtGt(objReq.getIdGoiThau());
        List<HhDthauNthauDuthau> listDuThau = new ArrayList<>();
        HhDthauNthauDuthau nhaThauTt = new HhDthauNthauDuthau();
        boolean isTrungThau = false;
        for (HhDthauNthauDuthauReq req : objReq.getNthauDuThauList()){
            HhDthauNthauDuthau nthauDthau = new HhDthauNthauDuthau();
            BeanUtils.copyProperties(req,nthauDthau,"id");
            nthauDthau.setIdDtGt(objReq.getIdGoiThau());
            HhDthauNthauDuthau save = nhaThauDuthauRepository.save(nthauDthau);
            if(!isTrungThau){
                isTrungThau = save.getTrangThai().equals(NhapXuatHangTrangThaiEnum.TRUNGTHAU.getId());
                if(isTrungThau){
                    nhaThauTt = save;
                }
            }
            listDuThau.add(nthauDthau);
        }
        if(isTrungThau){
            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THANH_CONG.getId());
            hhQdKhlcntDsgthau.setIdNhaThau(nhaThauTt.getId());
            hhQdKhlcntDsgthau.setTenNhaThau(nhaThauTt.getTenNhaThau());
            hhQdKhlcntDsgthau.setDonGiaNhaThau(nhaThauTt.getDonGia());
        }else{
            hhQdKhlcntDsgthau.setTrangThai(NhapXuatHangTrangThaiEnum.THAT_BAI.getId());
            hhQdKhlcntDsgthau.setIdNhaThau(null);
            hhQdKhlcntDsgthau.setTenNhaThau(null);
            hhQdKhlcntDsgthau.setDonGiaNhaThau(null);
        }
        goiThauRepository.save(hhQdKhlcntDsgthau);
        return listDuThau;

    }

    @Override
    public Page<HhQdKhlcntDtl> selectPage(HhQdKhlcntSearchReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdKhlcntDtl> hhQdKhlcntDtls = dtlRepository.selectPage(objReq.getNamKhoach(), objReq.getLoaiVthh(), objReq.getMaDvi(), NhapXuatHangTrangThaiEnum.BAN_HANH.getId(),objReq.getTrangThaiDtl(),objReq.getTrangThaiDt(),pageable);
        Map<String,String> hashMapPthucDthau = getListDanhMucChung("PT_DTHAU");
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

        hhQdKhlcntDtls.getContent().forEach(item ->{
            try {
                // Set Hdr
                HhQdKhlcntHdr hhQdKhlcntHdr = hhQdKhlcntHdrRepository.findById(item.getIdQdHdr()).get();
                hhQdKhlcntHdr.setTenPthucLcnt(hashMapPthucDthau.get(hhQdKhlcntHdr.getPthucLcnt()));
                hhQdKhlcntHdr.setTenCloaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getCloaiVthh()));
                hhQdKhlcntHdr.setTenLoaiVthh(hashMapDmHh.get(hhQdKhlcntHdr.getLoaiVthh()));
                item.setHhQdKhlcntHdr(hhQdKhlcntHdr);
                List<HhQdKhlcntDsgthau> byIdQdDtl = goiThauRepository.findByIdQdDtl(item.getId());
                long countThanhCong = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THANH_CONG.getId())).count();
                long countThatBai = byIdQdDtl.stream().filter(x -> x.getTrangThai().equals(NhapXuatHangTrangThaiEnum.THAT_BAI.getId())).count();
                item.setSoGthauTrung(countThanhCong);
                item.setSoGthauTruot(countThatBai);
                if(!StringUtils.isEmpty(item.getSoDxuat())){
                    Optional<HhDxuatKhLcntHdr> bySoDxuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(item.getSoDxuat());
                    bySoDxuat.ifPresent(item::setDxuatKhLcntHdr);
                }
                if(!StringUtils.isEmpty(item.getSoQdPdKqLcnt())){
                    Optional<HhQdPduyetKqlcntHdr> bySoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(item.getSoQdPdKqLcnt());
                    bySoQd.ifPresent(item::setHhQdPduyetKqlcntHdr);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        });
        return hhQdKhlcntDtls;
    }

    @Override
    public List<HhDthauNthauDuthau> detail(String ids,String loaiVthh) throws Exception {
        if(loaiVthh.startsWith("02")){
            Optional<HhQdKhlcntDtl> byId = dtlRepository.findById(Long.parseLong(ids));
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
        }else{
            Optional<HhQdKhlcntDsgthau> byId = goiThauRepository.findById(Long.parseLong(ids));
            if(!byId.isPresent()){
                throw new Exception("Gói thầu không tồn tại");
            }
        }

        List<HhDthauNthauDuthau> byIdDtGt = nhaThauDuthauRepository.findByIdDtGt(Long.parseLong(ids));
        byIdDtGt.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        return byIdDtGt;
    }

    @Override
    public void approve(HhDthauReq stReq) throws Exception {
        if(stReq.getLoaiVthh().startsWith("02")){
            approveVatTu(stReq);
        }else{
            approveLuongThuc(stReq);
        }

    }

    void approveLuongThuc(HhDthauReq stReq) throws Exception {
        Optional<HhQdKhlcntDtl> optional = dtlRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin đấu thầu không tồn tại");
        }
        List<HhQdKhlcntDsgthau> byIdQdDtl = goiThauRepository.findByIdQdDtl(stReq.getId());

        List<HhQdKhlcntDsgthau> collect = byIdQdDtl.stream().filter(item -> item.getTrangThai().equals(NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()){
            throw new Exception("Vui lòng cập nhật thông tin các gói thầu");
        }

        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThai(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        dtlRepository.save(optional.get());
    }

    void approveVatTu(HhDthauReq stReq) throws Exception {
        Optional<HhQdKhlcntHdr> optional = hhQdKhlcntHdrRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin đấu thầu không tồn tại");
        }
        List<HhQdKhlcntDtl> byIdQdDtl = dtlRepository.findAllByIdQdHdr(stReq.getId());

        List<HhQdKhlcntDtl> collect = byIdQdDtl.stream().filter(item -> item.getTrangThai().equals(NhapXuatHangTrangThaiEnum.CHUACAPNHAT.getId())).collect(Collectors.toList());
        if(!collect.isEmpty()){
            throw new Exception("Vui lòng cập nhật thông tin các gói thầu");
        }

        String status = stReq.getTrangThai() + optional.get().getTrangThaiDt();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThaiDt(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        hhQdKhlcntHdrRepository.save(optional.get());
    }


}
