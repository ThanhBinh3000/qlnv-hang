package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;


import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtDeXuatHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtTongHopHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class XhCtvtTongHopHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtTongHopHdrRepository xhCtvtTongHopHdrRepository;

  @Autowired
  private XhCtvtTongHopDtlRepository xhCtvtTongHopDtlRepository;

  @Autowired
  private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;


  public Page<XhCtvtTongHopHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr objReq) throws Exception {
    objReq.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
    Page<XhCtvtTongHopHdr> data = xhCtvtTongHopHdrRepository.search(objReq, pageable);
    Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    data.getContent().forEach(f -> {
      f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));

      List<XhCtvtTongHopDtl> listTh = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(f.getId());
      listTh.forEach(s -> {
        if (mapDmucDvi.containsKey((s.getMaDviDx()))) {
          Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDviDx());
          s.setTenDviDx(objDonVi.get("tenDvi").toString());
        }
      });
      f.setDeXuatCuuTro(listTh);
    });
    return data;
  }

  public XhCtvtTongHopHdr summaryData(CustomUserDetails currentUser, SearchXhCtvtDeXuatHdrReq objReq) throws Exception {
    List<XhCtvtDeXuatHdr> dxuatList = xhCtvtDeXuatHdrRepository.listTongHop(objReq);
    if (dxuatList.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
    }
    XhCtvtTongHopHdr thopHdr = new XhCtvtTongHopHdr();
    List<XhCtvtTongHopDtl> thopDtls = new ArrayList<>();
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    for (XhCtvtDeXuatHdr dxuat : dxuatList) {
      XhCtvtTongHopDtl thopDtl = new XhCtvtTongHopDtl();
      BeanUtils.copyProperties(dxuat, thopDtl, "id");
//      thopDtl.setIdHdr(objReq.getId());
      thopDtl.setMaDviDx(dxuat.getMaDvi());
      if (mapDmucDvi.containsKey((thopDtl.getMaDviDx()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(thopDtl.getMaDviDx());
        thopDtl.setTenDviDx(objDonVi.get("tenDvi").toString());
      }

      thopDtl.setIdDx(dxuat.getId());
      thopDtl.setNgayPduyetDx(dxuat.getNgayPduyet());
      thopDtl.setTrichYeuDx(dxuat.getTrichYeu());
      thopDtl.setTongSoLuongDx(dxuat.getTongSoLuong());
      thopDtl.setSoLuongXuatCap(dxuat.getSoLuongXuatCap());
      thopDtl.setSoLuongDeXuat(dxuat.getTongSoLuongDeXuat());
      thopDtl.setThanhTienDx(dxuat.getThanhTien());
      thopDtl.setNgayKetThucDx(dxuat.getNgayKetThuc());
      thopDtls.add(thopDtl);
    }
    thopHdr.setDeXuatCuuTro(thopDtls);
    return thopHdr;
  }

  @Transactional()
  public XhCtvtTongHopHdr save(CustomUserDetails currentUser, XhCtvtTongHopHdrReq objReq) throws Exception {
    XhCtvtTongHopHdr thopHdr = new XhCtvtTongHopHdr();
    DataUtils.copyProperties(objReq, thopHdr, "id");
    thopHdr.setTrangThai(Contains.DUTHAO);
    thopHdr.setMaDvi(currentUser.getUser().getDepartment());

    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(thopHdr);

    if (created.getDeXuatCuuTro().size() > 0) {
      List<Long> collectDxId = created.getDeXuatCuuTro().stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> newDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdIn(collectDxId);
      newDeXuatHdr.forEach(s -> {
        s.setIdThop(created.getId());
        s.setMaTongHop(created.getMaTongHop());
      });
      xhCtvtDeXuatHdrRepository.saveAll(newDeXuatHdr);
    }
    return created;
  }


  @Transactional()
  public XhCtvtTongHopHdr update(CustomUserDetails currentUser, XhCtvtTongHopHdrReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId()))
      throw new Exception(" Sửa thất bại, không tìm thấy dữ liệu");

    Optional<XhCtvtTongHopHdr> qOptional = xhCtvtTongHopHdrRepository.findById(Long.valueOf(objReq.getId()));
    if (!qOptional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần sửa");

    XhCtvtTongHopHdr data = qOptional.get();
    BeanUtils.copyProperties(objReq, data);
    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(data);

    //update dx
    //tim tat ca dx dang co ma th va update lại thanh null
    //update ma th cho dx xuat moi
    List<XhCtvtDeXuatHdr> oldDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdThop(created.getId());
    oldDeXuatHdr.forEach(s -> {
      s.setIdThop(null);
      s.setMaTongHop(null);
    });
    xhCtvtDeXuatHdrRepository.saveAll(oldDeXuatHdr);

    if (created.getDeXuatCuuTro().size() > 0) {
      List<Long> collectDxId = created.getDeXuatCuuTro().stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> newDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdIn(collectDxId);
      newDeXuatHdr.forEach(s -> {
        s.setIdThop(created.getId());
        s.setMaTongHop(created.getMaTongHop());
      });
      xhCtvtDeXuatHdrRepository.saveAll(newDeXuatHdr);
    }
    return created;
  }

  public List<XhCtvtTongHopHdr> detail(List<Long> ids) throws Exception {
    if (StringUtils.isEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional))
      throw new Exception("Không tìm thấy dữ liệu");

    Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
    List<XhCtvtTongHopHdr> allById = xhCtvtTongHopHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setTenLoaiVthh(hashMapDmHh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(hashMapDmHh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<XhCtvtTongHopDtl> listTh = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(data.getId());
      Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
      listTh.forEach(s -> {
        if (mapDmucDvi.containsKey((s.getMaDviDx()))) {
          Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDviDx());
          s.setTenDviDx(objDonVi.get("tenDvi").toString());
        }
      });
      data.setDeXuatCuuTro(listTh);
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getId()))
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

    Optional<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần xóa");

    XhCtvtTongHopHdr data = optional.get();
    List<XhCtvtTongHopDtl> listDls = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(data.getId());
    if (!CollectionUtils.isEmpty(listDls)) {
      List<Long> idDxList = listDls.stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> listDxHdr = xhCtvtDeXuatHdrRepository.findByIdIn(idDxList);
      if (!CollectionUtils.isEmpty(listDxHdr)) {
        listDxHdr.stream().map(item -> {
          item.setMaTongHop(null);
          item.setIdThop(null);
          return item;
        }).collect(Collectors.toList());
      }
      xhCtvtDeXuatHdrRepository.saveAll(listDxHdr);
    }
    xhCtvtTongHopDtlRepository.deleteAll(listDls);
    xhCtvtTongHopHdrRepository.delete(optional.get());

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIdList()))
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    List<XhCtvtTongHopHdr> listThop = xhCtvtTongHopHdrRepository.findAllByIdIn(idSearchReq.getIdList());
    for (XhCtvtTongHopHdr thopHdr : listThop) {
      List<XhCtvtTongHopDtl> listDls = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(thopHdr.getId());
      if (!CollectionUtils.isEmpty(listDls)) {
        List<Long> idDxList = listDls.stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
        List<XhCtvtDeXuatHdr> listDxHdr = xhCtvtDeXuatHdrRepository.findByIdIn(idDxList);
        if (!CollectionUtils.isEmpty(listDxHdr)) {
          listDxHdr.stream().map(item -> {
            item.setMaTongHop(null);
            item.setIdThop(null);
            return item;
          }).collect(Collectors.toList());
        }
        xhCtvtDeXuatHdrRepository.saveAll(listDxHdr);
      }
    }
    xhCtvtTongHopHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
  }


  public void export(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr searchReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    searchReq.setPaggingReq(paggingReq);
    Page<XhCtvtTongHopHdr> page = this.searchPage(currentUser, searchReq);
    List<XhCtvtTongHopHdr> data = page.getContent();

    String title = "Danh sách tổng hợp phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã Tổng hợp", "Ngày tổng hợp", "Số quyết định",
        "Ngày kí quyết định", "Loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "SL xuất CT,VT chuyển xuất cấp", "Nội dung tổng hợp", "Trạng thái"};
    String filename = "danh-sach-tong-hop-phuong-an-cuu-tro-vien-tro.xlsx";

    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtTongHopHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getMaTongHop();
      objs[3] = dx.getNgayThop();
      objs[4] = dx.getSoQdPd();
      objs[5] = dx.getNgayKyQd();
      objs[6] = dx.getTenLoaiVthh();
      for (XhCtvtTongHopDtl dtl : dx.getDeXuatCuuTro()) {
        objs[7] = dtl.getTongSoLuongDx();
        objs[8] = dtl.getSoLuongXuatCap();
      }
      objs[9] = dx.getNoiDungThop();
      objs[10] = dx.getTenTrangThai();
      dataList.add(objs);
    }

    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }


  public XhCtvtTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(optional.get());
    return created;
  }
}
