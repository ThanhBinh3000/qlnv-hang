package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvXhDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl implements XhQdGiaoNvXhService {

  @Autowired
  private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;

  @Autowired
  private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;

  @Autowired
  private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;

  @Autowired
  private XhHopDongHdrRepository xhHopDongHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Override
  public Page<XhQdGiaoNvXh> searchPage(XhQdGiaoNvuXuatReq objReq) throws Exception {
    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
        objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<XhQdGiaoNvXh> data = xhQdGiaoNvXhRepository.searchPage(objReq, pageable);
    Map<String, String> mapDmucHh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    data.getContent().forEach(item -> {
      item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
      item.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThaiXh()));
      item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
      item.setTenLoaiVthh(mapDmucHh.get(item.getLoaiVthh()));
      item.setTenCloaiVthh(mapDmucHh.get(item.getCloaiVthh()));
      item.setChildren(xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(item.getId()) != null
          ? xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(item.getId()) : null);
    });
    return data;
  }


  @Override
  @Transactional
  public XhQdGiaoNvXh create(XhQdGiaoNvuXuatReq req) throws Exception {

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) {
      throw new Exception("Bad request.");
    }


    Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findAllBySoQd(req.getSoQd());
    if (optional.isPresent()) {
      throw new Exception("Số quyết định đã tồn tại");
    }

    Map<String, String> mapDmucHh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    XhQdGiaoNvXh dataMap = new XhQdGiaoNvXh();
    BeanUtils.copyProperties(req, dataMap, "id");

    dataMap.setNguoiTaoId(userInfo.getId());
    dataMap.setNgayTao(new Date());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
    dataMap.setMaDvi(userInfo.getDvql());
    dataMap.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : mapDmucDvi.get(userInfo.getDvql()));
    XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(dataMap);

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
      created.setFileDinhKems(fileDinhKems);
    }

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
      created.setFileDinhKem(fileDinhKem);
    }

    this.saveDetail(req, dataMap.getId());
    return created;
  }

  public void saveDetail(XhQdGiaoNvuXuatReq req, Long idHdr) {
    xhQdGiaoNvXhDtlRepository.deleteAllByIdQdHdr(idHdr);
    for (XhQdGiaoNvuXuatCtReq dtlReq : req.getChildren()) {
      XhQdGiaoNvXhDtl dtl = new XhQdGiaoNvXhDtl();
      BeanUtils.copyProperties(dtlReq, dtl, "id");
      dtl.setIdQdHdr(idHdr);
      xhQdGiaoNvXhDtlRepository.save(dtl);
      xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(dtlReq.getId());
      for (XhQdGiaoNvXhDdiemReq ddiemReq : dtlReq.getChildren()) {
        XhQdGiaoNvXhDdiem ddiem = new XhQdGiaoNvXhDdiem();
        BeanUtils.copyProperties(ddiemReq, ddiem, "id");
        ddiem.setId(null);
        ddiem.setIdDtl(dtl.getId());
        xhQdGiaoNvXhDdiemRepository.save(ddiem);
      }
    }
  }

  @Override
  @Transactional
  public XhQdGiaoNvXh update(XhQdGiaoNvuXuatReq req) throws Exception {

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    if (StringUtils.isEmpty(req.getId()))
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
    Optional<XhQdGiaoNvXh> qOptional = xhQdGiaoNvXhRepository.findById(req.getId());

    if (!qOptional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }

    XhQdGiaoNvXh dataDB = qOptional.get();
    BeanUtils.copyProperties(req, dataDB, "id");
    dataDB.setNgaySua(getDateTimeNow());
    dataDB.setNguoiSuaId(getUser().getId());
    XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(dataDB);

    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
    dataDB.setFileDinhKems(fileDinhKems);

    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
    dataDB.setFileDinhKem(fileDinhKem);

    this.saveDetail(req, dataDB.getId());
    return created;
  }

  @Override
  public XhQdGiaoNvXh detail(Long id) throws Exception {
    Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhQdGiaoNvXh data = optional.get();
    Map<String, String> hashMapVthh = getListDanhMucHangHoa();
    Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
    data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
    data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
    data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdGiaoNvXh.TABLE_NAME));
    data.setFileDinhKems(fileDinhKem);
    data.setFileDinhKem(fileDinhKem);

    List<XhQdGiaoNvXhDtl> dtlList = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
    for (XhQdGiaoNvXhDtl dtl : dtlList) {
      List<XhQdGiaoNvXhDdiem> ddiemList = xhQdGiaoNvXhDdiemRepository.findAllByIdDtl(dtl.getId());
      for (XhQdGiaoNvXhDdiem ddiem : ddiemList) {
        ddiem.setTenDiemKho(hashMapDvi.get(ddiem.getMaDiemKho()));
        ddiem.setTenNhaKho(hashMapDvi.get(ddiem.getMaNhaKho()));
        ddiem.setTenNganKho(hashMapDvi.get(ddiem.getMaNganKho()));
        ddiem.setTenLoKho(hashMapDvi.get(ddiem.getMaLoKho()));
      }
      dtl.setTenDvi(hashMapDvi.get(dtl.getMaDvi()));
      dtl.setChildren(ddiemList);
    }
    data.setChildren(dtlList);
    return data;
  }

  @Override
  public XhQdGiaoNvXh approve(XhQdGiaoNvuXuatReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(Long.valueOf(req.getId()));
    if (!optional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu.");
    XhQdGiaoNvXh data = optional.get();
    String status = req.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
        data.setNguoiGuiDuyetId(userInfo.getId());
        data.setNgayGuiDuyet(getDateTimeNow());
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(getDateTimeNow());
        data.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.BAN_HANH + Contains.CHODUYET_LDC:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(getDateTimeNow());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(req.getTrangThai());

    if (req.getTrangThai().equals(Contains.BAN_HANH)) {
      Optional<XhHopDongHdr> hopDongHdr = xhHopDongHdrRepository.findById(data.getIdHd());
      if (hopDongHdr.isPresent()) {
        hopDongHdr.get().setSoQdGnv(data.getSoQd());
        xhHopDongHdrRepository.save(hopDongHdr.get());
      }
    }
    xhQdGiaoNvXhRepository.save(data);
    return data;
  }

  @Override
  public void delete(Long id) throws Exception {
    if (StringUtils.isEmpty(id)) {
      throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
    }

    Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }

    if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
      throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
    }

    List<XhQdGiaoNvXhDtl> dtlList = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(id);
    for (XhQdGiaoNvXhDtl dtl : dtlList) {
      xhQdGiaoNvXhDdiemRepository.deleteAllByIdDtl(dtl.getId());
    }
    xhQdGiaoNvXhDtlRepository.deleteAllByIdQdHdr(id);
    xhQdGiaoNvXhRepository.delete(optional.get());
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhQdGiaoNvXh.TABLE_NAME));

  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {
    if (StringUtils.isEmpty(listMulti)) {
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    }

    List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findByIdIn(listMulti);
    if (list.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }

    for (XhQdGiaoNvXh hdr : list) {
      this.delete(hdr.getId());
    }
  }

  @Override
  public void export(XhQdGiaoNvuXuatReq req, HttpServletResponse response) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    req.setMaDvi(userInfo.getDvql());
    Page<XhQdGiaoNvXh> page = this.searchPage(req);
    List<XhQdGiaoNvXh> data = page.getContent();

    String title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
    String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hàng hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
    String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_xuat_hang.xlsx";

    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhQdGiaoNvXh qdXh = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qdXh.getNam();
      objs[2] = qdXh.getSoQd();
      objs[3] = qdXh.getNgayTao();
      objs[4] = qdXh.getSoHd();
      objs[5] = qdXh.getTenLoaiVthh();
      objs[6] = qdXh.getTenCloaiVthh();
      objs[7] = qdXh.getTgianGnhan();
      objs[8] = qdXh.getTrichYeu();
      objs[9] = qdXh.getBbTinhKho();
      objs[10] = qdXh.getBbHaoDoi();
      objs[11] = qdXh.getTenTrangThai();
      objs[12] = qdXh.getTenTrangThaiXh();
      dataList.add(objs);

    }
    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }
  @Override
  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "bandaugia/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      XhQdGiaoNvXh detail = this.detail(DataUtils.safeToLong(body.get("id")));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }

//    @Override
//    @Transactional()
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findAllByIdIn(idSearchReq.getIdList());
//        if (list.isEmpty()) {
//            throw new Exception("Xóa thất bại bản ghi không tồn tại");
//        }
//
//        for (XhQdGiaoNvXh xhQdGiaoNvXh : list) {
//            if (!xhQdGiaoNvXh.getTrangThai().equals(Contains.DUTHAO)
//                    && !xhQdGiaoNvXh.getTrangThai().equals(Contains.TUCHOI_LDC)) {
//                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
//            }
//        }
//
//        List<Long> listIdHdr = list.stream().map(XhQdGiaoNvXh::getId).collect(Collectors.toList());
//        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdrIn(listIdHdr);
//        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
//        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhQdGiaoNvXh.TABLE_NAME));
//        xhQdGiaoNvXhRepository.deleteAll(list);
//    }

}
