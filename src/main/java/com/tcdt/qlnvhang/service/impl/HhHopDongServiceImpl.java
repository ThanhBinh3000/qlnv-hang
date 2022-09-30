package com.tcdt.qlnvhang.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.HhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.HhPhuLucRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.HhQdPduyetKqlcntHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.object.HhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.secification.HhHopDongSpecification;
import com.tcdt.qlnvhang.service.HhHopDongService;

@Service
public class HhHopDongServiceImpl extends BaseServiceImpl implements HhHopDongService {
  @Autowired
  private HhHopDongRepository hhHopDongRepository;

  @Autowired
  private HhPhuLucRepository hhPhuLucRepository;

  @Autowired
  private HhHopDongDdiemNhapKhoRepository hhHopDongDdiemNhapKhoRepository;

  @Autowired
  private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

  @Autowired
  private HttpServletRequest req;

  @Override
  public HhHopDongHdr create(HhHopDongHdrReq objReq) throws Exception {
    if (objReq.getLoaiVthh().startsWith("02")) {
      return createVatTu(objReq);
    } else {
      return createLuongThuc(objReq);
    }
  }

  public HhHopDongHdr createVatTu(HhHopDongHdrReq objReq) throws Exception {

    Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
    if (qOpHdong.isPresent())
      throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");

    Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
    if (!checkSoQd.isPresent())
      throw new Exception(
          "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

//		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setNgayTao(getDateTimeNow());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setMaDvi(userInfo.getDvql());

    // File dinh kem cua goi thau
    List<FileDKemJoinHopDong> dtls2 = new ArrayList<FileDKemJoinHopDong>();
    if (objReq.getFileDinhKems() != null) {
      dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDong.class);
      dtls2.forEach(f -> {
        f.setDataType(HhHopDongHdr.TABLE_NAME);
        f.setCreateDate(new Date());
      });
    }
    dataMap.setFileDinhKems(dtls2);

    hhHopDongRepository.save(dataMap);

    for (HhDdiemNhapKhoReq ddNhapRq : objReq.getDiaDiemNhapKhoReq()) {
      HhHopDongDdiemNhapKho ddNhap = ObjectMapperUtils.map(ddNhapRq, HhHopDongDdiemNhapKho.class);
      ddNhap.setIdHdongHdr(dataMap.getId());
      ddNhap.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
      hhHopDongDdiemNhapKhoRepository.save(ddNhap);
    }
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    dataMap.setDonViTinh(StringUtils.isEmpty(dataMap.getLoaiVthh()) ? null : mapVthh.get(dataMap.getDonViTinh()));
    return dataMap;
  }

  public HhHopDongHdr createLuongThuc(HhHopDongHdrReq objReq) throws Exception {
    if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
      throw new Exception("Loại vật tư hàng hóa không phù hợp");

    Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
    if (qOpHdong.isPresent())
      throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");

    Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
    if (!checkSoQd.isPresent())
      throw new Exception(
          "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

//		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setNgayTao(getDateTimeNow());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setMaDvi(userInfo.getDvql());

    // File dinh kem cua goi thau
    List<FileDKemJoinHopDong> dtls2 = new ArrayList<FileDKemJoinHopDong>();
    if (objReq.getFileDinhKems() != null) {
      dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDong.class);
      dtls2.forEach(f -> {
        f.setDataType(HhHopDongHdr.TABLE_NAME);
        f.setCreateDate(new Date());
      });
    }
    dataMap.setFileDinhKems(dtls2);

    hhHopDongRepository.save(dataMap);

    for (HhDdiemNhapKhoReq ddNhapRq : objReq.getDiaDiemNhapKhoReq()) {
      HhHopDongDdiemNhapKho ddNhap = ObjectMapperUtils.map(ddNhapRq, HhHopDongDdiemNhapKho.class);
      ddNhap.setIdHdongHdr(dataMap.getId());
      ddNhap.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
      hhHopDongDdiemNhapKhoRepository.save(ddNhap);
    }

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    dataMap.setDonViTinh(StringUtils.isEmpty(dataMap.getLoaiVthh()) ? null : mapVthh.get(dataMap.getDonViTinh()));
    return dataMap;
  }


  @Override
  public HhHopDongHdr update(HhHopDongHdrReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId()))
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

    Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(objReq.getId());
    if (!qOptional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần sửa");

    if (!qOptional.get().getSoHd().equals(objReq.getSoHd())) {
      Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
      if (qOpHdong.isPresent())
        throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");
    }

    if (!qOptional.get().getCanCu().equals(objReq.getCanCu())) {
      Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
      if (!checkSoQd.isPresent())

        throw new Exception(
            "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
    }

    HhHopDongHdr dataDB = qOptional.get();
    HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

    updateObjectToObject(dataDB, dataMap);

//		dataDB.setNgaySua(getDateTimeNow());
//		dataDB.setNguoiSua(getUser().getUsername());

//		// add thong tin detail
//		List<HhHopDongDtlReq> dtlReqList = objReq.getDetail();
//		List<HhHopDongDtl> details = new ArrayList<>();
//		if (dtlReqList != null) {
//			List<HhHopDongDdiemNhapKho> detailChild;
//			for (HhHopDongDtlReq dtlReq : dtlReqList) {
//				List<HhDdiemNhapKhoReq> cTietReq = dtlReq.getDetail();
//				HhHopDongDtl detail = ObjectMapperUtils.map(dtlReq, HhHopDongDtl.class);
//				detail.setType(Contains.HOP_DONG);
//				detailChild = new ArrayList<HhHopDongDdiemNhapKho>();
//				if (cTietReq != null)
//					detailChild = ObjectMapperUtils.mapAll(cTietReq, HhHopDongDdiemNhapKho.class);
//				detail.setChildren(detailChild);
//				details.add(detail);
//			}
//			dataDB.setChildren(details);
//		}
//
//		// add thong tin don vi lien quan
//		List<HhDviLquan> dtls1 = ObjectMapperUtils.mapAll(dtlReqList, HhDviLquan.class);
//		dataDB.setChildren1(dtls1);

    // File dinh kem cua goi thau
    List<FileDKemJoinHopDong> dtls2 = new ArrayList<FileDKemJoinHopDong>();
    if (objReq.getFileDinhKems() != null) {
      dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDong.class);
      dtls2.forEach(f -> {
        f.setDataType(HhHopDongHdr.TABLE_NAME);
        f.setCreateDate(new Date());
      });
    }
    dataDB.setFileDinhKems(dtls2);

//		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);

    hhHopDongRepository.save(dataDB);
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    dataDB.setDonViTinh(StringUtils.isEmpty(dataDB.getLoaiVthh()) ? null : mapVthh.get(dataDB.getDonViTinh()));
    return dataDB;
  }

  @Override
  public HhHopDongHdr detail(String ids) throws Exception {
    if (StringUtils.isEmpty(ids))
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(Long.parseLong(ids));

    if (!qOptional.isPresent())
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    // Quy doi don vi kg = tan
//		List<HhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhHopDongDtl.class);
//		for (HhHopDongDtl dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}
    Map<String, String> mapDmucDvi = getMapTenDvi();
    Map<String, String> hashMapDviLquan = getListDanhMucDviLq("NT");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
    qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : mapVthh.get(qOptional.get().getCloaiVthh()));
    qOptional.get().setTenDvi(StringUtils.isEmpty(qOptional.get().getMaDvi()) ? null : mapDmucDvi.get(qOptional.get().getMaDvi()));
    qOptional.get().setDonViTinh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getDonViTinh()));
    qOptional.get().setHhPhuLucHdongList(hhPhuLucRepository.findBySoHd(qOptional.get().getSoHd()));
    List<HhHopDongDdiemNhapKho> ddiemNhapKhos = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(Long.parseLong(ids));
    ddiemNhapKhos.forEach(s->{
      s.setTenDvi((StringUtils.isEmpty(s.getMaDvi()) ? null : mapDmucDvi.get(s.getMaDvi())));
      s.setTenDiemKho((StringUtils.isEmpty(s.getMaDiemKho()) ? null : mapDmucDvi.get(s.getMaDiemKho())));
    });
    qOptional.get().setHhDdiemNhapKhoList(hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(Long.parseLong(ids)));
    qOptional.get().setTenNthau(hashMapDviLquan.get(String.valueOf(Double.parseDouble(qOptional.get().getIdNthau().toString()))));

    return qOptional.get();
  }

  @Override
  public HhHopDongHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
    if (StringUtils.isEmpty(strSearchReq.getStr()))
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findBySoHd(strSearchReq.getStr());

    if (!qOptional.isPresent())
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    // Quy doi don vi kg = tan
//		List<HhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), HhHopDongDtl.class);
//		for (HhHopDongDtl dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}

    HhHopDongHdr dataDB = qOptional.get();
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    dataDB.setDonViTinh(StringUtils.isEmpty(dataDB.getLoaiVthh()) ? null : mapVthh.get(dataDB.getDonViTinh()));
    return this.detail(qOptional.get().getId().toString());
  }

  @Override
  public Page<HhHopDongHdr> selectPage(HhHopDongSearchReq req, HttpServletResponse response) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("ngay_sua", "ngay_tao").descending());
    UserInfo userInfo = UserUtils.getUserInfo();
    String dvql = userInfo.getDvql();
    Page<HhHopDongHdr> page;
    if (req.getTrangThai() != null && req.getTrangThai().equals(TrangThaiAllEnum.DA_KY.getId())) {
      dvql = userInfo.getDvql().substring(0, 4);
      page = hhHopDongRepository.selectAll(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertDateToString(req.getTuNgayKy()), convertDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql, req.getNamHd(), pageable);
    } else {
      page = hhHopDongRepository.select(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertDateToString(req.getTuNgayKy()), convertDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql, req.getNamHd(), pageable);
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> hashMapDviLquan = getListDanhMucDviLq("NT");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    Set<Long> hopDongIds = page.getContent().stream().map(HhHopDongHdr::getId).collect(Collectors.toSet());
    Map<Long, List<HhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
        .stream().collect(Collectors.groupingBy(HhHopDongDdiemNhapKho::getIdHdongHdr));

    page.forEach(f -> {
      f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
      f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(mapVthh.get(f.getCloaiVthh()));
      f.setTenNthau(hashMapDviLquan.get(DataUtils.safeToDouble(f.getIdNthau()).toString()));
      List<HhHopDongDdiemNhapKho> diaDiemNhapKhos = diaDiemNhapKhoMap.get(f.getId()) != null ? diaDiemNhapKhoMap.get(f.getId()) : new ArrayList<>();
      if (!CollectionUtils.isEmpty(diaDiemNhapKhos)) {
        diaDiemNhapKhos.forEach(d -> {
          d.setTenDvi(mapDmucDvi.get(d.getMaDvi()));
        });
        f.setHhDdiemNhapKhoList(diaDiemNhapKhos);
      }
      f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
    });
    return page;
  }

  @Override
  public Page<HhHopDongHdr> colection(HhHopDongSearchReq objReq, HttpServletRequest req) throws Exception {
    int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
    int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
    Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

    Page<HhHopDongHdr> dataPage = hhHopDongRepository.findAll(HhHopDongSpecification.buildSearchQuery(objReq),
        pageable);

    Set<Long> hopDongIds = dataPage.getContent().stream().map(HhHopDongHdr::getId).collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(hopDongIds))
      return dataPage;

    Map<Long, List<HhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
        .stream().collect(Collectors.groupingBy(HhHopDongDdiemNhapKho::getIdHdongHdr));

    // Lay danh muc dung chung
    Map<String, String> mapDmucDvi = getMapTenDvi();
    for (HhHopDongHdr hdr : dataPage.getContent()) {
      hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
      hdr.setHhDdiemNhapKhoList(diaDiemNhapKhoMap.get(hdr.getId()));
    }
    return dataPage;
  }

  @Override
  public HhHopDongHdr approve(StatusReq stReq) throws Exception {
    if (StringUtils.isEmpty(stReq.getId()))
      throw new Exception("Không tìm thấy dữ liệu");

    Optional<HhHopDongHdr> optional = hhHopDongRepository.findById(Long.valueOf(stReq.getId()));
    if (!optional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu");

    String status = stReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
//			case Contains.DUYET + Contains.MOI_TAO:
//				optional.get().setNguoiGuiDuyet(getUser().getUsername());
//				optional.get().setNgayGuiDuyet(getDateTimeNow());
//				break;
//			case Contains.TU_CHOI + Contains.CHO_DUYET:
//				optional.get().setNguoiPduyet(getUser().getUsername());
//				optional.get().setNgayPduyet(getDateTimeNow());
//				optional.get().setLdoTuchoi(stReq.getLyDo());
//				break;
      case Contains.DAKY + Contains.DUTHAO:
        optional.get().setNguoiPduyet(getUser().getUsername());
        optional.get().setNgayPduyet(getDateTimeNow());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(stReq.getTrangThai());

    // TODO: Cap nhat lai tinh trang hien thoi cua kho sau khi phe duyet quyet dinh
    // giao nhiem vu nhap hang
    return hhHopDongRepository.save(optional.get());
  }

  @Override
  public void delete(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getId()))
      throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

    Optional<HhHopDongHdr> optional = hhHopDongRepository.findById(idSearchReq.getId());
    if (!optional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần xoá");

    if (!optional.get().getTrangThai().equals(Contains.DUTHAO))
      throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

    hhHopDongRepository.delete(optional.get());

  }

  @Override
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIds()))
      throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

    List<HhHopDongHdr> optional = hhHopDongRepository.findByIdIn(idSearchReq.getIds());
    if (optional.isEmpty())
      throw new Exception("Không tìm thấy dữ liệu cần xoá");
    for (HhHopDongHdr hd : optional) {
      if (!hd.getTrangThai().equals(Contains.DUTHAO))
        throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");
    }
    hhHopDongRepository.deleteAll(optional);
  }

  @Override
  public void exportList(HhHopDongSearchReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<HhHopDongHdr> page = this.selectPage(objReq, response);
    List<HhHopDongHdr> data = page.getContent();

    String title = "Danh sách hợp đồng mua";
    String[] rowsName = new String[]{"STT", "Số HĐ", "Tên hợp đồng", "Ngày ký", "Loại hàng hóa", "Chủng loại hàng hóa", "Chủ đầu tư", "Nhà cung cấp", "Giá trị hợp đồng", "Trạng thái"};
    String fileName = "danh-sach-hop-dong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      HhHopDongHdr hd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = hd.getSoHd();
      objs[2] = hd.getTenHd();
      objs[3] = hd.getNgayKy();
      objs[4] = hd.getLoaiVthh();
      objs[5] = hd.getCloaiVthh();
      objs[6] = hd.getMaDvi();
      objs[7] = hd.getIdNthau();
      objs[8] = hd.getGtriHdSauVat();
      objs[9] = hd.getTrangThai();
      dataList.add(objs);

    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }


  public List<HhHopDongHdr> listHopDong(String maDvi, String loaiVthh) throws Exception {
    return hhHopDongRepository.ListHdTheoDk(maDvi, loaiVthh);
  }

  @Override
  public Page<HhHopDongHdr> lookupData(HhHopDongSearchReq req, HttpServletResponse response) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("ngay_sua", "ngay_tao").descending());
    UserInfo userInfo = UserUtils.getUserInfo();
    String dvql = userInfo.getDvql();
    Page<HhHopDongHdr> page;
    if (req.getTrangThai() != null && req.getTrangThai().equals(TrangThaiAllEnum.DA_KY.getId())) {
      dvql = userInfo.getDvql().substring(0, 4);
      page = hhHopDongRepository.selectAll(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertDateToString(req.getTuNgayKy()), convertDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql, req.getNamHd(), pageable);
    } else {
      page = hhHopDongRepository.lookupData(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertDateToString(req.getTuNgayKy()), convertDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql,userInfo.getDvql(), req.getNamHd(), pageable);
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> hashMapDviLquan = getListDanhMucDviLq("NT");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    Set<Long> hopDongIds = page.getContent().stream().map(HhHopDongHdr::getId).collect(Collectors.toSet());
    Map<Long, List<HhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
        .stream().collect(Collectors.groupingBy(HhHopDongDdiemNhapKho::getIdHdongHdr));

    page.forEach(f -> {
      f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
      f.setTenVthh(mapVthh.get(f.getLoaiVthh()));
      f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(mapVthh.get(f.getCloaiVthh()));
      f.setTenNthau(hashMapDviLquan.get(DataUtils.safeToDouble(f.getIdNthau()).toString()));
      List<HhHopDongDdiemNhapKho> diaDiemNhapKhos = diaDiemNhapKhoMap.get(f.getId()) != null ? diaDiemNhapKhoMap.get(f.getId()) : new ArrayList<>();
      if (!CollectionUtils.isEmpty(diaDiemNhapKhos)) {
        diaDiemNhapKhos.forEach(d -> {
          d.setTenDvi(mapDmucDvi.get(d.getMaDvi()));
        });
        f.setHhDdiemNhapKhoList(diaDiemNhapKhos);
      }
      f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
    });
    return page;
  }
}
