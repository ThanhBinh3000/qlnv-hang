package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQdGiaoNvXh;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtlReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtvtQdGiaoNvXhHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtQdGiaoNvXhHdrRepository xhCtvtQdGiaoNvXhHdrRepository;

  @Autowired
  private XhCtvtQdGiaoNvXhDtlRepository xhCtvtQdGiaoNvXhDtlRepository;


  @Autowired
  private XhCtvtQdPdHdrRepository xhCtvtQdPdHdrRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtQdGiaoNvXhHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQdGiaoNvXh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
      req.setTrangThai(Contains.BAN_HANH);
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }

    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtQdGiaoNvXhHdr> search = xhCtvtQdGiaoNvXhHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
      if (s.getTrangThaiXh() != null) {
        s.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThaiXh()));
      }
      s.setNoiDungCuuTro(xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(s.getId()));
    });
    return search;
  }

  @Transactional
  public XhCtvtQdGiaoNvXhHdr save(CustomUserDetails currentUser, XhCtvtQdGiaoNvXhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())){
      Optional<XhCtvtQdGiaoNvXhHdr> optional = xhCtvtQdGiaoNvXhHdrRepository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định giao nhiệm vụ nhập hàng đã tồn tại");
      }
    }
    XhCtvtQdGiaoNvXhHdr data = new XhCtvtQdGiaoNvXhHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTongSoLuong(objReq.getTongSoLuong());
    data.setThanhTien(objReq.getThanhTien());
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtQdGiaoNvXhHdr created = xhCtvtQdGiaoNvXhHdrRepository.save(data);
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      xhCtvtQdPdHdrRepository.findById(objReq.getIdQdPd())
          .ifPresent(item -> {
            item.setIdQdGiaoNv(objReq.getId());
            item.setSoQdGiaoNv(objReq.getSoQd());
            xhCtvtQdPdHdrRepository.save(item);
          });
    }

    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM");
    created.setFileDinhKem(fileDinhKem);

    this.saveCtiet(created.getId(), objReq);
    return created;
  }

  @Transactional()
  public void saveCtiet(Long idHdr, XhCtvtQdGiaoNvXhHdrReq objReq) {
    for (XhCtvtQdGiaoNvXhDtlReq noiDungCuuTroReq : objReq.getNoiDungCuuTro()) {
      XhCtvtQdGiaoNvXhDtl noiDungCuuTro = new XhCtvtQdGiaoNvXhDtl();
      BeanUtils.copyProperties(noiDungCuuTroReq, noiDungCuuTro);
      noiDungCuuTro.setId(null);
      noiDungCuuTro.setIdHdr(idHdr);
      xhCtvtQdGiaoNvXhDtlRepository.save(noiDungCuuTro);
    }
  }

  @Transactional
  public XhCtvtQdGiaoNvXhHdr update(CustomUserDetails currentUser, XhCtvtQdGiaoNvXhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtQdGiaoNvXhHdr> optional = xhCtvtQdGiaoNvXhHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtQdGiaoNvXhHdr> soDx = xhCtvtQdGiaoNvXhHdrRepository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định giao nhiệm vụ nhập hàng đã tồn tại");
      }
    }
    XhCtvtQdGiaoNvXhHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    data.setTongSoLuong(objReq.getTongSoLuong());
    data.setThanhTien(objReq.getThanhTien());
    XhCtvtQdGiaoNvXhHdr created = xhCtvtQdGiaoNvXhHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM"));
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM");
    created.setFileDinhKem(fileDinhKem);

    List<XhCtvtQdGiaoNvXhDtl> listnoiDungCuuTro = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtQdGiaoNvXhDtlRepository.deleteAll(listnoiDungCuuTro);
    this.saveCtiet(created.getId(), objReq);
    return created;
  }

  public void updateDiem(XhCtvtQdGiaoNvXhHdrReq objReq) throws Exception {

    List<XhCtvtQdGiaoNvXhDtl> listnoiDungCuuTro = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtQdGiaoNvXhDtlRepository.deleteAll(listnoiDungCuuTro);
    for (XhCtvtQdGiaoNvXhDtlReq noiDungCuuTroReq : objReq.getNoiDungCuuTro()) {
      XhCtvtQdGiaoNvXhDtl noiDungCuuTro = new XhCtvtQdGiaoNvXhDtl();
      BeanUtils.copyProperties(noiDungCuuTroReq, noiDungCuuTro);
      noiDungCuuTro.setId(null);
      noiDungCuuTro.setIdHdr(objReq.getId());
      noiDungCuuTro.setTrangThai(objReq.getTrangThaiXh());
      xhCtvtQdGiaoNvXhDtlRepository.save(noiDungCuuTro);
    }
    Optional<XhCtvtQdGiaoNvXhHdr> optionalQdHdr = xhCtvtQdGiaoNvXhHdrRepository.findById(objReq.getId());
    if (optionalQdHdr.isPresent()) {
      XhCtvtQdGiaoNvXhHdr qdHdr = optionalQdHdr.get();
      qdHdr.setTrangThaiXh(objReq.getTrangThaiXh());
      xhCtvtQdGiaoNvXhHdrRepository.save(qdHdr);
    }
  }

  public List<XhCtvtQdGiaoNvXhHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtQdGiaoNvXhHdr> optional = xhCtvtQdGiaoNvXhHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtQdGiaoNvXhHdr> allById = xhCtvtQdGiaoNvXhHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);
      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM"));
      data.setFileDinhKem(fileDinhKem);

      List<XhCtvtQdGiaoNvXhDtl> list = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(data.getId());
      for (XhCtvtQdGiaoNvXhDtl noiDungCuuTro : list) {
        if (mapDmucDvi.containsKey(noiDungCuuTro.getMaDviChiCuc())) {
          noiDungCuuTro.setTenChiCuc(mapDmucDvi.get(noiDungCuuTro.getMaDviChiCuc()).get("tenDvi").toString());
        }
        if (mapDmucDvi.containsKey(noiDungCuuTro.getMaDiemKho())) {
          noiDungCuuTro.setTenDiemKho(mapDmucDvi.get(noiDungCuuTro.getMaDiemKho()).get("tenDvi").toString());
        }
        if (mapDmucDvi.containsKey(noiDungCuuTro.getMaNhaKho())) {
          noiDungCuuTro.setTenNhaKho(mapDmucDvi.get(noiDungCuuTro.getMaNhaKho()).get("tenDvi").toString());
        }
        if (mapDmucDvi.containsKey(noiDungCuuTro.getMaNganKho())) {
          noiDungCuuTro.setTenNganKho(mapDmucDvi.get(noiDungCuuTro.getMaNganKho()).get("tenDvi").toString());
        }
        if (mapDmucDvi.containsKey(noiDungCuuTro.getMaLoKho())) {
          noiDungCuuTro.setTenLoKho(mapDmucDvi.get(noiDungCuuTro.getMaLoKho()).get("tenDvi").toString());
        }
        if (noiDungCuuTro.getCloaiVthh() != null) {
          noiDungCuuTro.setTenCloaiVthh(mapVthh.get(noiDungCuuTro.getCloaiVthh()));
        }
        if (noiDungCuuTro.getLoaiVthh() != null) {
          noiDungCuuTro.setTenLoaiVthh(mapVthh.get(noiDungCuuTro.getLoaiVthh()));
        }
        if (noiDungCuuTro.getTrangThai() != null) {
          noiDungCuuTro.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(noiDungCuuTro.getTrangThai()));
        }
      }
      data.setNoiDungCuuTro(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhCtvtQdGiaoNvXhHdr> optional = xhCtvtQdGiaoNvXhHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtQdGiaoNvXhHdr data = optional.get();
    List<XhCtvtQdGiaoNvXhDtl> list = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(data.getId());
    xhCtvtQdGiaoNvXhDtlRepository.deleteAll(list);
    if (!DataUtils.isNullObject(data.getSoQd())) {
      xhCtvtQdPdHdrRepository.findById(data.getIdQdPd())
          .ifPresent(item -> {
            item.setIdQdGiaoNv(null);
            item.setSoQdGiaoNv(null);
            xhCtvtQdPdHdrRepository.save(item);
          });
    }
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM"));
    xhCtvtQdGiaoNvXhHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhCtvtQdGiaoNvXhHdr> list = xhCtvtQdGiaoNvXhHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhCtvtQdGiaoNvXhHdr::getId).collect(Collectors.toList());
    list.forEach(xhXcapQdGnvXhHdr -> {
      if (!DataUtils.isNullObject(xhXcapQdGnvXhHdr.getIdQdPd())) {
        xhCtvtQdPdHdrRepository.findById(xhXcapQdGnvXhHdr.getIdQdPd())
            .ifPresent(item -> {
              item.setIdQdGiaoNv(null);
              item.setSoQdGiaoNv(null);
              xhCtvtQdPdHdrRepository.save(item);
            });
      }
    });

    List<XhCtvtQdGiaoNvXhDtl> listNoiDung = xhCtvtQdGiaoNvXhDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtQdGiaoNvXhDtlRepository.deleteAll(listNoiDung);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_FILE_DINH_KEM"));
    xhCtvtQdGiaoNvXhHdrRepository.deleteAll(list);
  }


  public XhCtvtQdGiaoNvXhHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtQdGiaoNvXhHdr> optional = xhCtvtQdGiaoNvXhHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    if (status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.DU_THAO.getId())) {
      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
      optional.get().setNgayGduyet(LocalDate.now());
    } else if (
        status.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) ||
            status.equals(TrangThaiAllEnum.TU_CHOI_TP.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) ||
            status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_TP.getId()) ||
            status.equals(TrangThaiAllEnum.TU_CHOI_LDC.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId()) ||
            status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_LDC.getId()) ||
            status.equals(TrangThaiAllEnum.BAN_HANH.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId())) {
      optional.get().setNguoiPduyetId(currentUser.getUser().getId());
      optional.get().setNgayPduyet(LocalDate.now());
      optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    if (statusReq.equals(TrangThaiAllEnum.BAN_HANH)) {
      optional.get().setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
    }
    XhCtvtQdGiaoNvXhHdr created = xhCtvtQdGiaoNvXhHdrRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, SearchXhCtvtQdGiaoNvXh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtQdGiaoNvXhHdr> page = this.searchPage(currentUser, objReq);
    List<XhCtvtQdGiaoNvXhHdr> data = page.getContent();

    String title = "Danh sách quyết định giao nhiệm vụ xuất cứu trợ, viện trợ ";
    String[] rowsName = new String[]{"STT", "Năm xuất", "Số QĐ giao NV XH", "Ngày quyết định", "Số QĐ PD PA", "Loại hàng hóa",
        "Thời gian giao nhận", "Trích yếu", "Số BB tịnh kho", "Số BB hao dôi", "Trang thái QĐ", "Trạng thái NX",};
    String fileName = "danh-sach-quyet-dinh-giao-nhiem-vu-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtQdGiaoNvXhHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getSoQd();
      objs[3] = dx.getNgayKy();
      objs[4] = dx.getSoQd();
      objs[5] = dx.getTenLoaiVthh();
      objs[6] = dx.getThoiGianGiaoNhan();
      objs[7] = dx.getTrichYeu();
      objs[8] = dx.getSoBbTinhKho();
      objs[9] = dx.getSoBbHaoDoi();
      objs[10] = dx.getTenTrangThai();
      objs[11] = dx.getTenTrangThaiXh();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}