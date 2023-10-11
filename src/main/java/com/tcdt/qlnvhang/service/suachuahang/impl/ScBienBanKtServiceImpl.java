package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.suachua.ScBienBanKtReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBienBanKtService;
import com.tcdt.qlnvhang.service.suachuahang.ScTongHopService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScBienBanKtServiceImpl extends BaseServiceImpl implements ScBienBanKtService {

  @Autowired
  private ScBienBanKtHdrRepository hdrRepository;

  @Autowired
  private ScBienBanKtDtlRepository dtlRepository;
  @Autowired
  private ScBienBanKtDdRepository ddRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;


  @Override
  public Page<ScBienBanKtHdr> searchPage(ScBienBanKtReq req) throws Exception {
    return null;
  }

  @Override
  public ScBienBanKtHdr create(ScBienBanKtReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      throw new Exception("Chức năng chỉ dành cho cấp chi cục");
    }
    ScBienBanKtHdr data = new ScBienBanKtHdr();
    BeanUtils.copyProperties(req, data);
    data.setMaDvi(currentUser.getDvql());
    data.setId(Long.parseLong(req.getSoBienBan().split("/")[0]));
    data.setIdThuKho(currentUser.getId());
    ScBienBanKtHdr created = hdrRepository.save(data);
    saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
    saveFileDinhKem(req.getFileCanCuReq(), created.getId(), ScPhieuNhapKhoHdr.TABLE_NAME+"_CC");
    saveDtl(req, data.getId());
    return created;
  }

  public void saveDtl(ScBienBanKtReq req, Long idHdr){
    dtlRepository.deleteAllByIdHdr(idHdr);
    req.getChildren().forEach(item -> {
      item.setId(null);
      item.setIdHdr(idHdr);
      dtlRepository.save(item);
    });
    ddRepository.deleteAllByIdHdr(idHdr);
    req.getDaiDienList().forEach(item -> {
      item.setId(null);
      item.setIdHdr(idHdr);
      ddRepository.save(item);
    });
  }

  @Override
  public ScBienBanKtHdr update(ScBienBanKtReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      throw new Exception("Chức năng chỉ dành cho cấp chi cục");
    }
    Optional<ScBienBanKtHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    ScBienBanKtHdr data = optional.get();
    BeanUtils.copyProperties(req, data);
    fileDinhKemService.delete(data.getId(), Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME));
    saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
    saveFileDinhKem(req.getFileCanCuReq(), data.getId(), ScPhieuNhapKhoHdr.TABLE_NAME+"_CC");
    saveDtl(req, data.getId());
    return data;
  }

  @Override
  public ScBienBanKtHdr detail(Long id) throws Exception {
    Optional<ScBienBanKtHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    ScBienBanKtHdr data = optional.get();
    data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME)));
    data.setFileCanCu(fileDinhKemService.search(id, Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME+"_CC")));
    data.setChildren(dtlRepository.findByIdHdr(id));
    data.setDaiDienList(ddRepository.findByIdHdr(id));
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    //set label
    data.setMapDmucDvi(mapDmucDvi);
    data.setMapVthh(mapVthh);
    data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
    if(!Objects.isNull(data.getIdThuKho())){
      data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
    }
    if(!Objects.isNull(data.getIdLanhDaoCc())){
      data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
    }
    return data;
  }

  @Override
  public ScBienBanKtHdr approve(ScBienBanKtReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    Optional<ScBienBanKtHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    ScBienBanKtHdr hdr = optional.get();

    String status = hdr.getTrangThai() + req.getTrangThai();
    switch (status) {
      // Re approve : gửi lại duyệt
      case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
      case Contains.TUCHOI_KT + Contains.CHODUYET_KTVBQ:
      case Contains.TUCHOI_LDCC + Contains.CHODUYET_KTVBQ:
        break;
      // Arena các cấp duuyệt
      case Contains.DUTHAO + Contains.CHODUYET_KTVBQ:
      case Contains.CHODUYET_KTVBQ + Contains.CHODUYET_KT:
      case Contains.CHODUYET_KT + Contains.CHODUYET_LDCC:
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
          throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
        }
        break;
      case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
          throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
        }
        hdr.setIdLanhDaoCc(userInfo.getId());
        break;
      // Arena từ chối
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
      case Contains.CHODUYET_KT + Contains.TUCHOI_KT:
      case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
        hdr.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    hdr.setTrangThai(req.getTrangThai());
    ScBienBanKtHdr save = hdrRepository.save(hdr);
    return save;
  }

  @Override
  public void delete(Long id) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
      throw new Exception("Chức năng chỉ dành cho cấp chi cục");
    }
    Optional<ScBienBanKtHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    hdrRepository.delete(optional.get());
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME+"_CC"));
    dtlRepository.deleteAllByIdHdr(optional.get().getId());
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {

  }

  @Override
  public void export(ScBienBanKtReq req, HttpServletResponse response) throws Exception {

  }

  @Override
  public Page<ScQuyetDinhNhapHang> searchBienBanKt(ScBienBanKtReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    ScQuyetDinhNhapHangReq reqQd = new ScQuyetDinhNhapHangReq();
    reqQd.setNam(req.getNam());
    reqQd.setSoQd(req.getSoQdNh());
    reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
    reqQd.setThoiHanNhapTu(req.getNgayNhapTu());
    reqQd.setThoiHanNhapDen(req.getNgayNhapDen());
    if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
      reqQd.setMaDviSr(userInfo.getDvql());
    }
    if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
      reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
    }
    Page<ScQuyetDinhNhapHang> search = scQuyetDinhNhapHangRepository.searchPageViewFromAnother(reqQd, pageable);
    search.getContent().forEach(item -> {
      try {
        req.setIdQdNh(item.getId());
        List<ScBienBanKtHdr> bbKtList = hdrRepository.searchList(req);
        List<ScBienBanKtHdr> bkList = new ArrayList<>();
        bbKtList.forEach(dtl -> {
          try {
            ScBienBanKtHdr detail = this.detail(dtl.getId());
            bkList.add(detail);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
        item.setScBienBanKtList(bkList);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return search;
  }

  @Override
  public ReportTemplateResponse preview(ScBienBanKtReq objReq) throws Exception {
    ScBienBanKtHdr optional = detail(objReq.getId());
    ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
    byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
    ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
    return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
  }
}
