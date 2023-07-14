package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScBangKeNhapVtDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScBangKeNhapVtHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeNhapVtService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScBangKeNhapVtServiceImpl extends BaseServiceImpl implements ScBangKeNhapVtService {
    @Autowired
    private ScBangKeNhapVtHdrRepository hdrRepository;

    @Autowired
    private ScBangKeNhapVtDtlRepository dtlRepository;

    @Override
    public Page<ScBangKeNhapVtHdr> searchPage(ScBangKeNhapVtReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeNhapVtHdr create(ScBangKeNhapVtReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeNhapVtHdr update(ScBangKeNhapVtReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeNhapVtHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public ScBangKeNhapVtHdr approve(ScBangKeNhapVtReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScBangKeNhapVtReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchBangKeCanHang(ScBangKeXuatVatTuReq req) {
        return null;
    }

//    public Page<ScBangKeNhapVtDTO> searchPage(CustomUserDetails currentUser, ScBangKeNhapVtReq req) throws Exception {
//        String dvql = currentUser.getDvql();
//        req.setMaDvi(dvql);
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        Page<ScBangKeNhapVtDTO> searchDto = hdrRepository.searchPage(req, pageable);
//
//        return searchDto;
//    }
//
//    public ScBangKeNhapVtHdr create(ScBangKeNhapVtReq objReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        String dvql = userInfo.getDvql();
//        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//            throw new Exception("Số bảng kê đã tồn tại");
//        }
//        ScBangKeNhapVtHdr data = new ScBangKeNhapVtHdr();
//        BeanUtils.copyProperties(objReq, data);
//        data.setMaDvi(dvql);
//        data.setTenDvi(userInfo.getTenDvi());
//        objReq.getScBangKeNhapVtDtls().forEach(e -> e.setScBangKeNhapVtHdr(data));
//        ScBangKeNhapVtHdr created = hdrRepository.save(data);
//        return created;
//    }
//
//    public ScBangKeNhapVtHdr update(ScBangKeNhapVtReq objReq) throws Exception {
//        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(objReq.getId());
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//        Optional<ScBangKeNhapVtHdr> soDxuat = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBangKe())) {
//            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }
//
//        ScBangKeNhapVtHdr data = optional.get();
//        objReq.setMaDvi(data.getMaDvi());
//        BeanUtils.copyProperties(objReq, data);
//        data.setScBangKeNhapVtDtls(objReq.getScBangKeNhapVtDtls());
//        ScBangKeNhapVtHdr created = hdrRepository.save(data);
//        return created;
//    }
//
//    public ScBangKeNhapVtHdr detail(Long id) throws Exception {
//        if (id == null)
//            throw new Exception("Tham số không hợp lệ.");
//        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        return optional.get();
//    }
//
//
//    @Transactional
//    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
//        if (StringUtils.isEmpty(statusReq.getId())) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScBangKeNhapVtHdr details = detail(statusReq.getId());
//        Optional<ScBangKeNhapVtHdr> optional = Optional.of(details);
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScBangKeNhapVtHdr data = optional.get();
//        data.setTrangThai(statusReq.getTrangThai());
//        hdrRepository.save(data);
//    }
//
//    public void delete(Long id) throws Exception {
//        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        ScBangKeNhapVtHdr data = optional.get();
//        List<ScBangKeNhapVtDtl> list = dtlRepository.findByHdrId(data.getId());
//        dtlRepository.deleteAll(list);
//        hdrRepository.delete(data);
//    }
//
//    public void deleteMulti(List<Long> listMulti) throws Exception {
//        List<ScBangKeNhapVtHdr> list = hdrRepository.findAllByIdIn(listMulti);
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(ScBangKeNhapVtHdr::getId).collect(Collectors.toList());
//        List<ScBangKeNhapVtDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
//        dtlRepository.deleteAll(listBangKe);
//    }
}
