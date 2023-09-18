package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhGnv;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.CAP_CHI_CUC;

@Service
public class XhCtvtQuyetDinhGnvService extends BaseServiceImpl {


  @Autowired
  private XhCtvtQuyetDinhGnvHdrRepository xhCtvtQuyetDinhGnvHdrRepository;

  @Autowired
  private XhCtvtQdPdHdrRepository xhCtvtQdPdHdrRepository;

  @Autowired
  private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;


  public Page<XhCtvtQuyetDinhGnvHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhGnv objReq) throws Exception {
    if (!currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC)) {
      objReq.setDvql(currentUser.getDvql());
    } else {
      objReq.setMaDviGiao(currentUser.getDvql());
    }

    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
    Page<XhCtvtQuyetDinhGnvHdr> data = xhCtvtQuyetDinhGnvHdrRepository.search(objReq, pageable);
    return data;
  }

  @Transactional()
  public XhCtvtQuyetDinhGnvHdr save(CustomUserDetails currentUser, XhCtvtQuyetDinhGnvHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findBySoBbQd(objReq.getSoBbQd());
    if (optional.isPresent()) {
      throw new Exception("Số quyết định đã tồn tại");
    }

    XhCtvtQuyetDinhGnvHdr saveData = new XhCtvtQuyetDinhGnvHdr();
    DataUtils.copyProperties(objReq, saveData, "id");
    saveData.setMaDvi(currentUser.getUser().getDepartment());
    saveData.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(saveData);

    List<Long> listIdQdPdDtl = objReq.getDataDtl().stream().map(XhCtvtQuyetDinhGnvDtl::getIdQdPdDtl).collect(Collectors.toList());
    if (!DataUtils.isNullObject(created)) {
      Optional<XhCtvtQuyetDinhPdHdr> quyetDinhPd = xhCtvtQdPdHdrRepository.findById(created.getIdQdPd());
      quyetDinhPd.get().getQuyetDinhPdDtl().forEach(s -> {
        if (listIdQdPdDtl.contains(s.getId())) {
          s.setIdQdGnv(created.getId());
          s.setSoQdGnv(created.getSoBbQd());
        }
      });
      xhCtvtQdPdHdrRepository.save(quyetDinhPd.get());
    }
    return created;
  }


  @Transactional()
  public XhCtvtQuyetDinhGnvHdr update(CustomUserDetails currentUser, XhCtvtQuyetDinhGnvHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> soBbQd = xhCtvtQuyetDinhGnvHdrRepository.findBySoBbQd(objReq.getSoBbQd());
    if (soBbQd.isPresent()) {
      if (!soBbQd.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhCtvtQuyetDinhGnvHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(data);
    return created;
  }

  public List<XhCtvtQuyetDinhGnvHdr> detail(List<Long> ids) throws Exception {
    if (StringUtils.isEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) throw new Exception("Không tìm thấy dữ liệu");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtQuyetDinhGnvHdr> allById = xhCtvtQuyetDinhGnvHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      data.getDataDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
    });
    return allById;
  }


  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIdList())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    List<XhCtvtQuyetDinhGnvHdr> listDataHdr = xhCtvtQuyetDinhGnvHdrRepository.findAllByIdIn(idSearchReq.getIdList());
    List<Long> listQdPdId = listDataHdr.stream().map(XhCtvtQuyetDinhGnvHdr::getIdQdPd).collect(Collectors.toList());
    List<XhCtvtQuyetDinhPdHdr> listQdPd = xhCtvtQdPdHdrRepository.findAllByIdIn(listQdPdId);
    listQdPd.forEach(s -> {
      s.setIdQdGiaoNv(null);
      s.setSoQdGiaoNv(null);
    });
    xhCtvtQdPdHdrRepository.saveAll(listQdPd);
    xhCtvtQuyetDinhGnvHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
  }

  public void export(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhGnv searchReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    searchReq.setPaggingReq(paggingReq);
    Page<XhCtvtQuyetDinhGnvHdr> page = this.searchPage(currentUser, searchReq);
    List<XhCtvtQuyetDinhGnvHdr> data = page.getContent();

    String title = "Danh sách tổng hợp phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã Tổng hợp", "Ngày tổng hợp", "Số quyết định", "Ngày kí quyết định", "Loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "SL xuất CT,VT chuyển xuất cấp", "Nội dung tổng hợp", "Trạng thái"};
    String filename = "danh-sach-tong-hop-phuong-an-cuu-tro-vien-tro.xlsx";

    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtQuyetDinhGnvHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
     /* objs[1] = dx.getNam();
      objs[2] = dx.getMaTongHop();
      objs[3] = dx.getNgayThop();
      objs[4] = dx.getSoQdPd();
      objs[5] = dx.getNgayKyQd();
      objs[6] = dx.getTenLoaiVthh();

      objs[9] = dx.getNoiDungThop();
      objs[10] = dx.getTenTrangThai();*/
      dataList.add(objs);
    }

    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }


  public XhCtvtQuyetDinhGnvHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    } else {
      XhCtvtQuyetDinhGnvHdr data = optional.get();
      String status = data.getTrangThai() + statusReq.getTrangThai();
      if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) || status.equals(TrangThaiAllEnum.TU_CHOI_TP.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) || status.equals(TrangThaiAllEnum.TU_CHOI_LDC.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId())) {
        data.setNguoiGduyetId(currentUser.getUser().getId());
        data.setNgayGduyet(LocalDate.now());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_TP.getId()) || status.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
        data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId()) || status.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.BAN_HANH.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
      } else {
        throw new Exception("Phê duyệt không thành công");
      }
      data.setTrangThai(statusReq.getTrangThai());
      XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(data);
      return created;
    }
  }
}
