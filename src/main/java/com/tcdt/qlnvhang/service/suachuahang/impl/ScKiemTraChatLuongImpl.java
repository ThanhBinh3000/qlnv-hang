package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScKiemTraChatLuongDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScKiemTraChatLuongHdrRepository;
import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScKiemTraChatLuongService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class ScKiemTraChatLuongImpl extends BaseServiceImpl implements ScKiemTraChatLuongService {
    @Autowired
    private ScKiemTraChatLuongHdrRepository hdrRepository;
    @Autowired
    private ScKiemTraChatLuongDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<ScKiemTraChatLuongHdr> searchPage(ScKiemTraChatLuongReq req) throws Exception {
        return null;
    }

    @Override
    public ScKiemTraChatLuongHdr create(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        ScKiemTraChatLuongHdr data = new ScKiemTraChatLuongHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoPhieuKdcl().split("/")[0]));
        ScKiemTraChatLuongHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScKiemTraChatLuongDtl> dtls = saveDtl(req, data.getId());
        created.setChildren(dtls);
        return created;
    }

    public List<ScKiemTraChatLuongDtl> saveDtl(ScKiemTraChatLuongReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScKiemTraChatLuongHdr update(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScKiemTraChatLuongDtl> dtls = saveDtl(req, data.getId());
        data.setChildren(dtls);
        return data;
    }

    @Override
    public ScKiemTraChatLuongHdr detail(Long id) throws Exception {
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME)));
        data.setChildren(dtlRepository.findAllByIdHdr(id));

        if(!Objects.isNull(data.getNguoiTaoId())){
            data.setTenNguoiTao(userInfoRepository.findById(data.getNguoiTaoId()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdTruongPhongKtvq())){
            data.setTenTruongPhongKtbq(userInfoRepository.findById(data.getIdTruongPhongKtvq()).get().getFullName());
        }
        return data;
    }

    @Override
    public ScKiemTraChatLuongHdr approve(ScKiemTraChatLuongReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScKiemTraChatLuongReq req, HttpServletResponse response) throws Exception {

    }

//    public Page<ScKiemTraChatLuongDTO> searchPage(CustomUserDetails currentUser, ScKiemTraChatLuongReq req) throws Exception {
//        String dvql = currentUser.getDvql();
//        req.setMaDvi(dvql);
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
////        Page<ScKiemTraChatLuongDTO> searchDto = hdrRepository.searchPage(req, pageable);
//
//        return searchDto;
//    }
//
//    @Transactional
//    public ScKiemTraChatLuongHdr save(CustomUserDetails currentUser, ScKiemTraChatLuongReq objReq) throws Exception {
//        if (currentUser == null) {
//            throw new Exception("Bad request.");
//        }
//        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
//        }
//        Optional<ScKiemTraChatLuongHdr> soDxuat = hdrRepository.findBySoPhieuKdcl(objReq.getSoPhieuKdcl());
//        if (soDxuat.isPresent()) {
//            throw new Exception("Số phiếu đã tồn tại");
//        }
//        ScKiemTraChatLuongHdr data = new ScKiemTraChatLuongHdr();
//        BeanUtils.copyProperties(objReq, data);
//        data.setMaDvi(currentUser.getDvql());
//        objReq.getScKiemTraChatLuongDtls().forEach(e -> {
//            e.setScKiemTraChatLuongHdr(data);
//        });
//        ScKiemTraChatLuongHdr created = hdrRepository.save(data);
//        saveFileDinhKem(objReq.getFileDinhKemReqs(), created.getId(), ScKiemTraChatLuongHdr.TABLE_NAME);
//        return created;
//    }
//
//    @Transactional
//    public ScKiemTraChatLuongHdr update(ScKiemTraChatLuongReq objReq) throws Exception {
//        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(objReq.getId());
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//
//        ScKiemTraChatLuongHdr data = optional.get();
//        objReq.setMaDvi(data.getMaDvi());
//        BeanUtils.copyProperties(objReq, data);
//        data.setScKiemTraChatLuongDtls(objReq.getScKiemTraChatLuongDtls());
//        ScKiemTraChatLuongHdr update = hdrRepository.save(data);
//        fileDinhKemService.delete(update.getId(), Lists.newArrayList(ScKiemTraChatLuongHdr.TABLE_NAME));
//        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), update.getId(), ScKiemTraChatLuongHdr.TABLE_NAME);
//        update.setFileDinhKems(canCu);
//        return update;
//    }
//
//    public ScKiemTraChatLuongHdr detail(Long id) throws Exception {
//        if (ObjectUtils.isEmpty(id)) {
//            throw new Exception("Tham số không hợp lệ.");
//        }
//        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScKiemTraChatLuongHdr data = optional.get();
//        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singletonList(ScKiemTraChatLuongHdr.TABLE_NAME)));
//        return data;
//    }
//
//    @Transient
//    public void delete(Long id) throws Exception {
//        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        ScKiemTraChatLuongHdr data = optional.get();
//        List<ScKiemTraChatLuongDtl> list = dtlRepository.findAllByHdrId(id);
//        dtlRepository.deleteAll(list);
//        hdrRepository.delete(data);
//    }
//
//    @Transactional
//    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
//        if (ObjectUtils.isEmpty(statusReq.getId())) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScKiemTraChatLuongHdr details = detail(statusReq.getId());
//        Optional<ScKiemTraChatLuongHdr> optional = Optional.of(details);
//        ScKiemTraChatLuongHdr data = optional.get();
//        data.setTrangThai(statusReq.getTrangThai());
//        hdrRepository.save(data);
//    }
}
