package com.tcdt.qlnvhang.service.chotdulieu;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.chotdulieu.QthtChotGiaNhapXuatRepository;
import com.tcdt.qlnvhang.repository.xuathang.chotdulieu.QthtKetChuyenDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.chotdulieu.QthtKetChuyenHdrRepository;
import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtKetChuyenReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenDtl;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QthtKetChuyenServiceImpl extends BaseServiceImpl implements QthtKetChuyenService {

  @Autowired
  private QthtKetChuyenHdrRepository hdrRepository;

  @Autowired
  private QthtKetChuyenDtlRepository dtlRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Override
  public Page<QthtKetChuyenHdr> searchPage(QthtKetChuyenReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    UserInfo userInfo = UserUtils.getUserInfo();
    req.setMaDviSr(userInfo.getDvql());
//    if (userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//    }
//    if (userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//      req.setMaDviSr(userInfo.getDvql().substring(0, 6));
//    }
    Page<QthtKetChuyenHdr> search = hdrRepository.searchPage(req, pageable);
    search.getContent().forEach( item -> {
      item.setTenNguoiTao(userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
    });
    return null;
  }

  @Override
  public QthtKetChuyenHdr create(QthtKetChuyenReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    QthtKetChuyenHdr hdr = new QthtKetChuyenHdr();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNguoiTaoId(userInfo.getId());
    hdr.setNgayTao(LocalDateTime.now());
    QthtKetChuyenHdr save = hdrRepository.save(hdr);
    return save;
  }

  @Override
  public QthtKetChuyenHdr update(QthtKetChuyenReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();

    Optional<QthtKetChuyenHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    QthtKetChuyenHdr data = optional.get();
    BeanUtils.copyProperties(req, data);
    return data;
  }

  @Override
  public QthtKetChuyenHdr detail(Long id) throws Exception {
    Optional<QthtKetChuyenHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    return optional.get();
  }

  @Override
  public QthtKetChuyenHdr approve(QthtKetChuyenReq req) throws Exception {
    return null;
  }

  @Override
  public void delete(Long id) throws Exception {
    Optional<QthtKetChuyenHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    QthtKetChuyenHdr data = optional.get();

    hdrRepository.save(data);
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {

  }

  @Override
  public void export(QthtKetChuyenReq req, HttpServletResponse response) throws Exception {

  }

  @Override
  public List<QthtKetChuyenHdr> createList(List<QthtKetChuyenReq> req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if(req == null || req.isEmpty() ){
      throw new Exception("KHông tìm thấy dữ liệu để thêm");
    }
    List<QthtKetChuyenHdr> dataSaved = new ArrayList<>();
    for (QthtKetChuyenReq dataReq : req) {
      try {
        QthtKetChuyenHdr hdr = new QthtKetChuyenHdr();
        BeanUtils.copyProperties(dataReq,hdr);
        hdr.setNguoiTaoId(userInfo.getId());
        hdr.setNgayTao(LocalDateTime.now());
        this.validateData(hdr);
        QthtKetChuyenHdr save = hdrRepository.save(hdr);
        save.setTenChiCuc(dataReq.getTenChiCuc());
        save.setChildren(this.saveDetail(dataReq,save.getId()));
        dataSaved.add(save);
      }catch (Exception e){
        throw new Exception(e.getMessage());
      }
    }
    return dataSaved;
  }


  List<QthtKetChuyenDtl> saveDetail(QthtKetChuyenReq req,Long idHdr){
    dtlRepository.deleteAllByIdHdr(idHdr);
    List<QthtKetChuyenDtl> dtlList = new ArrayList<>();
    for (QthtKetChuyenDtl dtl : req.getChildren()) {
      dtl.setId(null);
      dtl.setIdHdr(idHdr);
      dtlRepository.save(dtl);
      dtlList.add(dtl);
    }
    return dtlList;
  }

  void validateData(QthtKetChuyenHdr hdr) throws Exception {
    Optional<QthtKetChuyenHdr> byNamAndMaDvi = hdrRepository.findByNamAndMaDvi(hdr.getNam(), hdr.getMaDvi());
    if(byNamAndMaDvi.isPresent()){
      throw new Exception(hdr.getTenChiCuc()+" đã chốt điều chuyển");
    }
  }
}
