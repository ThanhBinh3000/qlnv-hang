package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbKeHoachDcHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbKeHoachDc;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DcnbKeHoachDcDtlService extends BaseServiceImpl {


  @Autowired
  private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;

  @Autowired
  private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<DcnbKeHoachDcHdr> searchPage(CustomUserDetails currentUser, SearchDcnbKeHoachDc req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setMaDvi(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setMaDvi(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<DcnbKeHoachDcHdr> search = dcnbKeHoachDcHdrRepository.search(req, pageable);
    return search;
  }

  @Transactional
  public DcnbKeHoachDcHdr save(CustomUserDetails currentUser, DcnbKeHoachDcHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findFirstBySoDxuat(objReq.getSoDxuat());
    if (optional.isPresent() && objReq.getSoDxuat().split("/").length == 1) {
      throw new Exception("số đề xuất đã tồn tại");
    }
    DcnbKeHoachDcHdr data = new DcnbKeHoachDcHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getDvql());
    data.setType(Contains.DIEU_CHUYEN);
    data.setTrangThai(Contains.DUTHAO);
    objReq.getDanhSachHangHoa().forEach(e -> e.setDcnbKeHoachDcHdr(data));
    objReq.getPhuongAnDieuChuyen().forEach(e -> e.setDcnbKeHoachDcHdr(data));
    DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(data);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    return created;
  }

  @Transactional
  public DcnbKeHoachDcHdr update(CustomUserDetails currentUser, DcnbKeHoachDcHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<DcnbKeHoachDcHdr> soDxuat = dcnbKeHoachDcHdrRepository.findFirstBySoDxuat(objReq.getSoDxuat());
    if (soDxuat.isPresent() && objReq.getSoDxuat().split("/").length == 1) {
      if (!soDxuat.get().getId().equals(objReq.getId())) {
        throw new Exception("số đề xuất đã tồn tại");
      }
    }
    DcnbKeHoachDcHdr data = optional.get();
    objReq.getDanhSachHangHoa().forEach(e -> e.setDcnbKeHoachDcHdr(data));
    objReq.getPhuongAnDieuChuyen().forEach(e -> e.setDcnbKeHoachDcHdr(data));
    objReq.setType(data.getType());
    data.setDanhSachHangHoa(objReq.getDanhSachHangHoa());
    data.setPhuongAnDieuChuyen(objReq.getPhuongAnDieuChuyen());

    BeanUtils.copyProperties(objReq, data);
    DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    dcnbKeHoachDcHdrRepository.save(created);
    return created;
  }


  public List<DcnbKeHoachDcHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    List<DcnbKeHoachDcHdr> allById = dcnbKeHoachDcHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);
    });
    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    DcnbKeHoachDcHdr data = optional.get();
    List<DcnbKeHoachDcDtl> list = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data.getId());
    dcnbKeHoachDcDtlRepository.deleteAll(list);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
    dcnbKeHoachDcHdrRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<DcnbKeHoachDcHdr> list = dcnbKeHoachDcHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(DcnbKeHoachDcHdr::getId).collect(Collectors.toList());
    List<DcnbKeHoachDcDtl> listPhuongAn = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdIn(listId);
    dcnbKeHoachDcDtlRepository.deleteAll(listPhuongAn);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
    dcnbKeHoachDcHdrRepository.deleteAll(list);
  }

  public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    // check đang là điều chuyển hay là nhận điều chuyển
    if (Contains.DIEU_CHUYEN.equals(optional.get().getType())) {
      this.approveDieuChuyen(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }else if (Contains.NHAN_DIEU_CHUYEN.equals(optional.get().getType())){
      this.approveNhanDieuChuyen(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }
  }

  public DcnbKeHoachDcHdr approveDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbKeHoachDcHdr> optional) throws Exception {
    String status =  optional.get().getTrangThai() + statusReq.getTrangThai();
    switch (status) {
      case Contains.DUTHAO + Contains.CHODUYET_TBP_TVQT:
      case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
      case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
        optional.get().setNgayGduyet(LocalDate.now());
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        // check thông tin lô kho tồn kho với sl điều chuyển với  trong kế hoạch và các kế hoạch khác có đủ k, đã xuất bao nhiêu trong kế hoạch
        break;
      case Contains.CHODUYET_TBP_TVQT + Contains.TUCHOI_TBP_TVQT:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.CHODUYET_TBP_TVQT + Contains.CHODUYET_LDCC:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        break;
      case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
        optional.get().setNgayDuyetLdcc(LocalDate.now());
        optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
        optional.get().setNgayDuyetLdcc(LocalDate.now());
        optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());

        // xử lý clone tờ kế hoạch cho các chi cục với trạng thái YC_CHICUC_PHANBO_DC = 59
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(optional.get());
    return created;
  }

  public DcnbKeHoachDcHdr approveNhanDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbKeHoachDcHdr> optional) throws Exception {
    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
    switch (status) {
      case Contains.YC_CHICUC_PHANBO_DC + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
      case Contains.DA_PHANBO_DC_TUCHOI_TBP_TVQT + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
      case Contains.DA_PHANBO_DC_TUCHOI_LDCC + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
        optional.get().setNgayGduyet(LocalDate.now());
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        break;
      case Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT + Contains.DA_PHANBO_DC_TUCHOI_TBP_TVQT:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT + Contains.DA_PHANBO_DC_CHODUYET_LDCC:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        break;
      case Contains.DA_PHANBO_DC_CHODUYET_LDCC + Contains.DA_PHANBO_DC_TUCHOI_LDCC:
        optional.get().setNgayDuyetLdcc(LocalDate.now());
        optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DA_PHANBO_DC_CHODUYET_LDCC + Contains.DA_PHANBO_DC_DADUYET_LDCC:
        optional.get().setNgayDuyetLdcc(LocalDate.now());
        optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());

        // update lại các kho nhận điều chuyển trong danh sách hàng hóa cha.
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, SearchDcnbKeHoachDc objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<DcnbKeHoachDcHdr> page = this.searchPage(currentUser, objReq);
    List<DcnbKeHoachDcHdr> data = page.getContent();

    String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
    String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất","Ngày duyệt LĐ Cục","Loại điều chuyển","Đơn vị đề xuất", "Trạng thái",};
    String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      DcnbKeHoachDcHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getSoDxuat();
      objs[3] = dx.getNgayDuyetLdcc();
      objs[4] = dx.getLoaiDc();
      objs[5] = dx.getTenDvi();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
