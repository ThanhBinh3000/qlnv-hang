package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlBaoCaoKqRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBaoCaoKqHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBaoCaoKqHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhTlBaoCaoKqService extends BaseServiceImpl {


  @Autowired
  private XhTlBaoCaoKqRepository xhTlBaoCaoKqRepository;

  @Autowired
  private XhTlQuyetDinhHdrRepository xhTlQuyetDinhHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlBaoCaoKqHdr> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlBaoCaoKqHdr> search = xhTlBaoCaoKqRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhTlBaoCaoKqHdr save( XhTlBaoCaoKqHdrReq req) throws Exception {
//    XhTlBaoCaoKqHdr hdr = new XhTlBaoCaoKqHdr();
//    validateData(req);
//    BeanUtils.copyProperties(req, hdr);
//    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//    XhTlBaoCaoKqHdr created = scQuyetDinhScRepository.save(hdr);
//    this.updateScTongHopHdr(created,false);
//    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
//    created.setFileCanCu(canCu);
//    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
//    created.setFileDinhKem(fileDinhKem);
    return null;
  }

//  void validateData(XhTlBaoCaoKqHdrReq req) throws Exception {
//    Optional<ScQuyetDinhSc> bySoQd = hdr.findBySoQd(req.getSoQd());
//    if(bySoQd.isPresent()){
//      if(ObjectUtils.isEmpty(req.getId())){
//        throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
//      }else{
//        if(!req.getId().equals(bySoQd.get().getId())){
//          throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
//        }
//      }
//    }
//  }

  void updateScTongHopHdr(ScQuyetDinhSc sc,boolean isDelete) throws Exception {
//    Optional<ScTrinhThamDinhHdr> byId = scTrinhThamDinhRepository.findById(sc.getIdTtr());
//    if(byId.isPresent()){
//      ScTrinhThamDinhHdr data = byId.get();
//      if(isDelete){
//        data.setIdQdSc(null);
//        data.setSoQdSc(null);
//      }else{
//        data.setIdQdSc(sc.getId());
//        data.setSoQdSc(sc.getSoQd());
//      }
//      scTrinhThamDinhRepository.save(data);
//    }else{
//      throw new Exception("Không tìm thấy số tờ trình cần sửa chữa");
//    }
  }

  @Transactional
  public XhTlBaoCaoKqHdr update(CustomUserDetails currentUser, XhTlBaoCaoKqHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhTlBaoCaoKqHdr> optional = xhTlBaoCaoKqRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhTlBaoCaoKqHdr> soDx = xhTlBaoCaoKqRepository.findBySoBaoCao(objReq.getSoBaoCao());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhTlBaoCaoKqHdr data = optional.get();
    objReq.getBaoCaoKqDtl().forEach(s -> {
      s.setBaoCaoKqHdr(null);
    });
    BeanUtils.copyProperties(objReq, data, "id","maDvi");
    data.getBaoCaoKqDtl().forEach(s -> {
      s.setBaoCaoKqHdr(data);
    });
    XhTlBaoCaoKqHdr created = xhTlBaoCaoKqRepository.save(data);

//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      xhTlQuyetDinhHdrRepository.findById(data.getIdQd())
//          .ifPresent(item -> {
//            item.setIdKq(data.getId());
//            item.setSoKq(data.getSoBaoCao());
//            xhTlQuyetDinhHdrRepository.save(item);
//          });
//    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlBaoCaoKqHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlBaoCaoKqHdr.TABLE_NAME));

    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlBaoCaoKqHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhTlBaoCaoKqHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhTlBaoCaoKqHdr> optional = xhTlBaoCaoKqRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhTlBaoCaoKqHdr> allById = xhTlBaoCaoKqRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.get((data.getMaDvi())) != null) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      }
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlBaoCaoKqHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      data.getBaoCaoKqDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setTenLoaiVthh(StringUtils.isEmpty(s.getLoaiVthh()) ? null : mapVthh.get(s.getLoaiVthh()));
        s.setTenCloaiVthh(StringUtils.isEmpty(s.getCloaiVthh()) ? null : mapVthh.get(s.getCloaiVthh()));
      });

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlBaoCaoKqHdr> optional = xhTlBaoCaoKqRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlBaoCaoKqHdr data = optional.get();

//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      xhTlQuyetDinhHdrRepository.findById(data.getIdQd())
//          .ifPresent(item -> {
//            item.setIdKq(null);
//            item.setSoKq(null);
//            xhTlQuyetDinhHdrRepository.save(item);
//          });
//    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlBaoCaoKqHdr.TABLE_NAME));
    xhTlBaoCaoKqRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlBaoCaoKqHdr> list = xhTlBaoCaoKqRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

//    list.forEach(f -> {
//      if (!DataUtils.isNullObject(f.getIdQd())) {
//        xhTlQuyetDinhHdrRepository.findById(f.getIdQd())
//            .ifPresent(item -> {
//              item.setIdKq(null);
//              item.setSoKq(null);
//              xhTlQuyetDinhHdrRepository.save(item);
//            });
//      }
//    });

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlBaoCaoKqHdr.TABLE_NAME));
    xhTlBaoCaoKqRepository.deleteAll(list);

  }

  public XhTlBaoCaoKqHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlBaoCaoKqHdr> optional = xhTlBaoCaoKqRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
//        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
//        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
//        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
//        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhTlBaoCaoKqHdr created = xhTlBaoCaoKqRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlBaoCaoKqHdr> page = this.searchPage(currentUser, objReq);
    List<XhTlBaoCaoKqHdr> data = page.getContent();

    String title = "Danh sách báo cáo kết quả bán thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số báo cáo", "Nội dung báo cáo", "Ngày báo cáo",
        "Số quyết định thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-ban-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlBaoCaoKqHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoBaoCao();
      objs[2] = qd.getNoiDung();
      objs[3] = qd.getNgayBaoCao();
      objs[4] = qd.getSoQd();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
