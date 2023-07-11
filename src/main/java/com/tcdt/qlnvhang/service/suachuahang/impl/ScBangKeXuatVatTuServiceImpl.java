package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeXuatVatTuService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScBangKeXuatVatTuServiceImpl extends BaseServiceImpl implements ScBangKeXuatVatTuService {
    @Autowired
    private ScBangKeXuatVatTuHdrRepository hdrRepository;

    @Autowired
    private ScBangKeXuatVatTuDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhScService scQuyetDinhScService;

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Override
    public Page<ScBangKeXuatVatTuHdr> searchPage(ScBangKeXuatVatTuReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeXuatVatTuHdr create(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        ScBangKeXuatVatTuHdr data = new ScBangKeXuatVatTuHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(dvql);
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.parseLong(req.getSoBangKe().split("/")[0]));
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        this.updatePhieuNhapKho(data,data.getIdPhieuXuatKho(),null);
        return created;
    }

    private void updatePhieuNhapKho(ScBangKeXuatVatTuHdr data,Long idPhieuNhapKho,Long idPhieuNhapKhoOld){
        if(!Objects.isNull(idPhieuNhapKhoOld) && !idPhieuNhapKho.equals(idPhieuNhapKhoOld)){
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuNhapKhoOld);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(null);
                byId.get().setSoBangKeCanHang(null);
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }else{
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuNhapKho);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(data.getId());
                byId.get().setSoBangKeCanHang(data.getSoBangKe());
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }
    }

    public List<ScBangKeXuatVatTuDtl> saveDtl(ScBangKeXuatVatTuReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }



    @Override
    public ScBangKeXuatVatTuHdr update(ScBangKeXuatVatTuReq req) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        this.updatePhieuNhapKho(data,req.getIdPhieuXuatKho(),data.getIdPhieuXuatKho());
        BeanUtils.copyProperties(req, data);
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        return created;
    }

    @Override
    public ScBangKeXuatVatTuHdr detail(Long id) throws Exception {
        return null;
    }

    @Override
    public ScBangKeXuatVatTuHdr approve(ScBangKeXuatVatTuReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
        Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(optional.get().getIdPhieuXuatKho());
        if(byId.isPresent()){
            byId.get().setIdBangKeCanHang(null);
            byId.get().setSoBangKeCanHang(null);
            scPhieuXuatKhoHdrRepository.save(byId.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScBangKeXuatVatTuReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchBangKeCanHang(ScBangKeXuatVatTuReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        ScQuyetDinhXuatHangReq reqQd = new ScQuyetDinhXuatHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdXh());
        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPageViewFromPhieuXuatKho(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                List<ScDanhSachHdr> scDanhSachHdrList = new ArrayList<>();
                ScQuyetDinhSc scQuyetDinhSc = scQuyetDinhScService.detail(item.getIdQdSc());
                // lấy Danh sách sửa chữa hdr
                scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().forEach(( dsHdr)->{
                    ScDanhSachHdr scDanhSachHdr = dsHdr.getScDanhSachHdr();
                    req.setIdScDanhSachHdr(scDanhSachHdr.getId());
                    scDanhSachHdr.setScBangKeXuatVatTuList(hdrRepository.searchList(req));
                    scDanhSachHdrList.add(scDanhSachHdr);
                });
                item.setScQuyetDinhSc(scQuyetDinhScService.detail(item.getIdQdSc()));
                item.setScDanhSachHdrList(scDanhSachHdrList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

//    public Page<ScBangKeXuatVtDTO> searchPage(CustomUserDetails currentUser, ScBangKeXuatVatTuReq req) throws Exception {
//        String dvql = currentUser.getDvql();
//        req.setMaDvi(dvql);
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        Page<ScBangKeXuatVtDTO> searchDto = hdrRepository.searchPage(req, pageable);
//        return searchDto;
//    }
//
//    @Transactional
//    public ScBangKeXuatVatTuHdr create(ScBangKeXuatVatTuReq objReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        String dvql = userInfo.getDvql();
//        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (optional.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//            throw new Exception("Số bảng kê đã tồn tại");
//        }
//        ScBangKeXuatVatTuHdr data = new ScBangKeXuatVatTuHdr();
//        BeanUtils.copyProperties(objReq, data);
//        data.setMaDvi(dvql);
//        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
//        return created;
//    }
//
//    @Transactional
//    public ScBangKeXuatVatTuHdr update(ScBangKeXuatVatTuReq objReq) throws Exception {
//        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(objReq.getId());
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//        Optional<ScBangKeXuatVatTuHdr> soDxuat = hdrRepository.findFirstBySoBangKe(objReq.getSoBangKe());
//        if (StringUtils.isNotEmpty(objReq.getSoBangKe())) {
//            if (soDxuat.isPresent() && objReq.getSoBangKe().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số bảng kê đã tồn tại");
//                }
//            }
//        }
//
//        ScBangKeXuatVatTuHdr data = optional.get();
//        objReq.setMaDvi(data.getMaDvi());
//        BeanUtils.copyProperties(objReq, data);
//        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
//        return created;
//    }
//
//
//    public ScBangKeXuatVatTuHdr detail(Long id) throws Exception {
//        if (id == null)
//            throw new Exception("Tham số không hợp lệ.");
//        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScBangKeXuatVatTuHdr data = optional.get();
//        List<ScBangKeXuatVatTuDtl> list = dtlRepository.findByHdrId(id);
//        return data;
//    }
//
//
//    @Transactional
//    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
//        if (ObjectUtils.isEmpty(statusReq.getId())) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScBangKeXuatVatTuHdr details = detail(Long.valueOf(statusReq.getId()));
//        Optional<ScBangKeXuatVatTuHdr> optional = Optional.of(details);
//        if (!optional.isPresent()) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        ScBangKeXuatVatTuHdr data = optional.get();
//        hdrRepository.save(data);
//    }
//
//    public void delete(Long id) throws Exception {
//        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        ScBangKeXuatVatTuHdr data = optional.get();
//        List<ScBangKeXuatVatTuDtl> list = dtlRepository.findByHdrId(data.getId());
//        dtlRepository.deleteAll(list);
//        hdrRepository.delete(data);
//    }
//
//
//    public void deleteMulti(List<Long> listMulti) throws Exception {
//        List<ScBangKeXuatVatTuHdr> list = hdrRepository.findAllByIdIn(listMulti);
//
//        if (list.isEmpty()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        List<Long> listId = list.stream().map(ScBangKeXuatVatTuHdr::getId).collect(Collectors.toList());
//        List<ScBangKeXuatVatTuDtl> listBangKe = dtlRepository.findByHdrIdIn(listId);
//        dtlRepository.deleteAll(listBangKe);
//    }
//
//    public void export(ScBangKeXuatVatTuReq objReq, HttpServletResponse response) throws Exception {
//
//    }
}
