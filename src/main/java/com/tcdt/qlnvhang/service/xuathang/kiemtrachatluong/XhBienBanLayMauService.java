package com.tcdt.qlnvhang.service.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong.XhBienBanLayMauRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchBienBanLayMauReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhBienBanLayMauReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat.NhHoSoKyThuatService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.BIEN_BAN_LAY_MAU.*;

@Service
public class XhBienBanLayMauService extends BaseServiceImpl {

  @Autowired
  private XhBienBanLayMauRepository xhBienBanLayMauRepository;
  @Autowired
  private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
  @Autowired
  private NhHoSoBienBanRepository nhHoSoBienBanRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private NhHoSoKyThuatService nhHoSoKyThuatService;
  @Autowired
  private BienBanLayMauRepository bienBanLayMauRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  public Page<XhBienBanLayMauHdr> searchPage(CustomUserDetails currentUser, SearchBienBanLayMauReq req) throws Exception {
    String dvql = currentUser.getDvql();
    req.setDvql(dvql);
  /*  if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
//      req.setTrangThai(Contains.BAN_HANH);
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }*/
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhBienBanLayMauHdr> search = xhBienBanLayMauRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapVthh(mapVthh);
      s.setMapDmucDvi(mapDmucDvi);
    });
    return search;
  }

  @Transactional
  public XhBienBanLayMauHdr create(CustomUserDetails currentUser, XhBienBanLayMauReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhBienBanLayMauHdr> optional = xhBienBanLayMauRepository.findFirstBySoBbQd(objReq.getSoBbQd());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }
    XhBienBanLayMauHdr data = new XhBienBanLayMauHdr();
    BeanUtils.copyProperties(objReq, data,"maDvi");
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    if(data.getNgayTao() == null){
      data.setNgayTao(LocalDateTime.now());
    }
    XhBienBanLayMauHdr created = xhBienBanLayMauRepository.save(data);
    String[] soBbQd = created.getSoBbQd().split("/");
    created.setSoBbQd(created.getId() + "/" + soBbQd[1]);
    XhBienBanLayMauHdr save = xhBienBanLayMauRepository.save(created);
    return save;
  }

  @Transactional
  public XhBienBanLayMauHdr update(CustomUserDetails currentUser, XhBienBanLayMauReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhBienBanLayMauHdr> optional = xhBienBanLayMauRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhBienBanLayMauHdr> soQd = xhBienBanLayMauRepository.findFirstBySoBbQd(objReq.getSoBbQd());
      if (soQd.isPresent()) {
        if (!soQd.get().getId().equals(objReq.getId())) {
          throw new Exception("Số quyết định đã tồn tại");
        }
      }
    }

    XhBienBanLayMauHdr data = optional.get();
    if(objReq.getNgayTao() == null){
      objReq.setNgayTao(data.getNgayTao());
    }
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    XhBienBanLayMauHdr updated = xhBienBanLayMauRepository.save(data);
    return updated;
  }


  public List<XhBienBanLayMauHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhBienBanLayMauHdr> optional = xhBienBanLayMauRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhBienBanLayMauHdr> allById = xhBienBanLayMauRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
      data.setMapVthh(mapVthh);
    });
    return allById;
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhBienBanLayMauHdr> list = xhBienBanLayMauRepository.findAllByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    xhBienBanLayMauRepository.deleteAll(list);

  }


  public XhBienBanLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhBienBanLayMauHdr> optional = xhBienBanLayMauRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    } else {
      XhBienBanLayMauHdr data = optional.get();
      String status = data.getTrangThai() + statusReq.getTrangThai();
      if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDCC.getId()) || status.equals(TrangThaiAllEnum.TU_CHOI_LDCC.getId() + TrangThaiAllEnum.CHO_DUYET_LDCC.getId())) {
        data.setNguoiGduyetId(currentUser.getUser().getId());
        data.setNgayGduyet(LocalDate.now());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_LDCC.getId() + TrangThaiAllEnum.TU_CHOI_LDCC.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
        data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
        data.setLanhDaoChiCuc(currentUser.getUser().getFullName());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_LDCC.getId() + TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
        data.setLanhDaoChiCuc(currentUser.getUser().getFullName());
      } else {
        throw new Exception("Phê duyệt không thành công");
      }
      data.setTrangThai(statusReq.getTrangThai());
      XhBienBanLayMauHdr created = xhBienBanLayMauRepository.save(data);
      return created;
    }
  }


  public void export(CustomUserDetails currentUser, SearchBienBanLayMauReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhBienBanLayMauHdr> page = this.searchPage(currentUser, objReq);
    List<XhBienBanLayMauHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký", "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-lay-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhBienBanLayMauHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoBbQd();
      /*objs[2] = qd.getTrichYeu();
      objs[3] = qd.getNgayKy();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();*/
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String path = baseReportFolder + "/chung/bienbanlaymau/Biên bản lấy mẫu bàn giao mẫu.docx";
      FileInputStream inputStream = new FileInputStream(path);
      List<XhBienBanLayMauHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      List<XhBienBanLayMauDtl> nlq = detail.get(0).getXhBienBanLayMauDtl().stream().filter(s -> s.getType().equals(NGUOI_LIEN_QUAN)).collect(Collectors.toList());
      List<XhBienBanLayMauDtl> pplm = detail.get(0).getXhBienBanLayMauDtl().stream().filter(s -> s.getType().equals(PHUONG_PHAP_LAY_MAU)).collect(Collectors.toList());
      List<XhBienBanLayMauDtl> ctlc = detail.get(0).getXhBienBanLayMauDtl().stream().filter(s -> s.getType().equals(CHI_TIEU_CHAT_LUONG)).collect(Collectors.toList());
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0), nlq, pplm, ctlc);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}
