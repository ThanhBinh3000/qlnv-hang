package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.entities.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKt;
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
import com.tcdt.qlnvhang.util.DataUtils;
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
                objReq.getTrangThaiTkhai(),
                objReq.getCtyCgia(),
                objReq.getPtMua(),
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
                    Optional<HhQdPduyetKqcgHdr> bySoQd = hhQdPduyetKqcgRepository.findBySoQd(item.getSoQdPdKqMtt());
                    bySoQd.ifPresent(item::setHhQdPduyetKqcgHdr);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            item.setTenTrangThaiTkhai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThaiTkhai()));
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
        }else {
            byId.get().setTrangThaiTkhai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            hhQdPheduyetKhMttDxRepository.save(byId.get());
        }
        byId.get().setPtMuaTrucTiep(objReq.getPtMuaTrucTiep());
        HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
        hhCtietTtinCgiaRepository.deleteAllByIdTkhaiKh(objReq.getIdChaoGia());
        List<HhChiTietTTinChaoGia> listDuThau = new ArrayList<>();
        for (HhChiTietTTinChaoGiaReq req : objReq.getHhChiTietTTinChaoGiaReqs()){
            HhChiTietTTinChaoGia nthauDthau = new HhChiTietTTinChaoGia();
            BeanUtils.copyProperties(req,nthauDthau,"id");
            nthauDthau.setIdTkhaiKh(objReq.getIdChaoGia());
            HhChiTietTTinChaoGia save = hhCtietTtinCgiaRepository.save(nthauDthau);
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKems()), nthauDthau.getId(), "HH_CTIET_TTIN_CHAO_GIA");
            nthauDthau.setFileDinhKems(fileDinhKems.get(0));
            listDuThau.add(nthauDthau);
        }
        if(hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.UY_QUYEN)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),hhQdPheduyetKhMttDx.getId(),"HH_QD_PHE_DUYET_KHMTT_DX");
            hhQdPheduyetKhMttDx.setFileDinhKemUyQuyen(fileDinhKems);
        }
        if(hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.MUA_LE)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),hhQdPheduyetKhMttDx.getId(),"HH_QD_PHE_DUYET_KHMTT_DX");
            hhQdPheduyetKhMttDx.setFileDinhKemMuaLe(fileDinhKems);
        }
        hhQdPheduyetKhMttDxRepository.save(hhQdPheduyetKhMttDx);
        return listDuThau;
    }

    public List<HhChiTietTTinChaoGia> detail(String ids) throws Exception {
            Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(Long.parseLong(ids));
            if(!byId.isPresent()){
                throw new Exception("Bản ghi không tồn tại");
            }
        HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
        List<HhChiTietTTinChaoGia> byIdDtGt = hhCtietTtinCgiaRepository.findAllByIdTkhaiKh(Long.parseLong(ids));
        byIdDtGt.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });
        for (HhChiTietTTinChaoGia chaoGia : byIdDtGt){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(chaoGia.getId(), Arrays.asList(HhChiTietTTinChaoGia.TABLE_NAME));
            chaoGia.setFileDinhKems(fileDinhKems.get(0));
        }
        if(hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.UY_QUYEN)){
            hhQdPheduyetKhMttDx.setFileDinhKemUyQuyen(fileDinhKemService.search(hhQdPheduyetKhMttDx.getId(), Collections.singleton(HhQdPheduyetKhMttDx.TABLE_NAME)));
        }
        if (hhQdPheduyetKhMttDx.getPtMuaTrucTiep().equals(Contains.MUA_LE)){
            hhQdPheduyetKhMttDx.setFileDinhKemMuaLe(fileDinhKemService.search(hhQdPheduyetKhMttDx.getId(), Collections.singleton(HhQdPheduyetKhMttDx.TABLE_NAME)));
        }
        return byIdDtGt;
    }

  public  void approve(HhCgiaReq stReq) throws Exception {
        Optional<HhQdPheduyetKhMttDx> optional = hhQdPheduyetKhMttDxRepository.findById(stReq.getId());
        if(!optional.isPresent()){
            throw new Exception("Bản nghi không tồn tại");
        }
        String status = stReq.getTrangThaiTkhai() + optional.get().getTrangThaiTkhai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThaiTkhai(stReq.getTrangThaiTkhai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
      hhQdPheduyetKhMttDxRepository.save(optional.get());
    }
}
