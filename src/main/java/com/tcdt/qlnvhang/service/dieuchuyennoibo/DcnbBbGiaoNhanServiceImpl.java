package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbChuanBiKhoDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbChuanBiKhoHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbGiaoNhanDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbGiaoNhanHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachNhapXuat;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DcnbBbGiaoNhanServiceImpl implements DcnbBbGiaoNhanService {

    @Autowired
    private DcnbBbGiaoNhanHdrRepository hdrRepository;

    @Autowired
    private DcnbBbGiaoNhanDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private DcnbKeHoachNhapXuatService dcnbKeHoachNhapXuatService;

    @Override
    public Page<DcnbBbGiaoNhanHdr> searchPage(DcnbBbGiaoNhanHdrReq req) throws Exception {
        return null;
    }

    @Override
    public DcnbBbGiaoNhanHdr create(DcnbBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findFirstBySoBb(req.getSoBb());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }

        DcnbBbGiaoNhanHdr data = new DcnbBbGiaoNhanHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(req.getSoBb().split("/")[0]));
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        DcnbBbGiaoNhanHdr created = hdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        DcnbKeHoachNhapXuat kh = new DcnbKeHoachNhapXuat();
        kh.setIdHdr(created.getId());
        kh.setTableName(DcnbBbGiaoNhanHdr.TABLE_NAME);
        kh.setIdKhDcDtl(data.getIdKeHoachDtl());
        dcnbKeHoachNhapXuatService.saveOrUpdate(kh);
        return created;
    }

    @Override
    public DcnbBbGiaoNhanHdr update(DcnbBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbGiaoNhanHdr data = optional.get();
        BeanUtils.copyProperties(req,data);
        data.setChildren(req.getChildren());
        DcnbBbGiaoNhanHdr update = hdrRepository.save(data);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(DcnbBBNTBQHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), DcnbBBNTBQHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public DcnbBbGiaoNhanHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        if(Objects.isNull(id)){
            throw new Exception("Id is null");
        }
        Optional<DcnbBbGiaoNhanHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        DcnbBbGiaoNhanHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(DcnbBBNTBQHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public DcnbBbGiaoNhanHdr approve(DcnbBbGiaoNhanHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Access denied.");
        }
        DcnbBbGiaoNhanHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các roll back approve
            case Contains.TUCHOI_LDC + Contains.DUTHAO:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDC:
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDao(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        DcnbBbGiaoNhanHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        DcnbBbGiaoNhanHdr detail = detail(id);
        hdrRepository.delete(detail);
        dtlRepository.deleteAllByHdrId(id);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if(listMulti != null && !listMulti.isEmpty()){
            listMulti.forEach( i -> {
                try {
                    delete(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }else{
            throw new Exception("List id is null");
        }
    }

    @Override
    public void export(DcnbBbGiaoNhanHdrReq req, HttpServletResponse response) throws Exception {

    }
}