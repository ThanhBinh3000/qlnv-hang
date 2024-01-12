package com.tcdt.qlnvhang.service.impl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKhoVt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.hopdong.HhHopDongPreview;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
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
  private HhHopDongDdiemNhapKhoVtRepository hhHopDongDdiemNhapKhoVtRepository;

  @Autowired
  private HhHopDongDtlRepository hhHopDongDtlRepository;

  @Autowired
  private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

  @Autowired
  private HttpServletRequest req;

  @Autowired
  private QlnvDmDonviRepository qlnvDmDonviRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Override
  @Transactional()
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




//    Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
//    if (!checkSoQd.isPresent())
//      throw new Exception(
//          "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);

//		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setNgayTao(getDateTimeNow());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setMaDvi(userInfo.getDvql());
    hhHopDongRepository.save(dataMap);
    if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataMap.getId(), HhHopDongHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getListFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListFileDinhKem(), dataMap.getId(), HhHopDongHdr.TABLE_NAME);
    }

    this.saveDataChildren(dataMap,objReq);
    return dataMap;
  }

  public HhHopDongHdr createLuongThuc(HhHopDongHdrReq objReq) throws Exception {

    if(!StringUtils.isEmpty(objReq.getSoHd())){
      Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
      if (qOpHdong.isPresent()){
        throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");
      }
    }

    Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getSoQdKqLcnt());
    if (!checkSoQd.isPresent()){
      throw new Exception(
              "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQdKqLcnt() + " không tồn tại");
    }

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null){
      throw new Exception("Bad request.");
    }

    HhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, HhHopDongHdr.class);
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setMaDvi(userInfo.getDvql());
    hhHopDongRepository.save(dataMap);
    if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataMap.getId(), HhHopDongHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getListFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListFileDinhKem(), dataMap.getId(), HhHopDongHdr.TABLE_NAME);
    }

    this.saveDataChildren(dataMap,objReq);

    Optional<HhQdPduyetKqlcntHdr> bySoQd = hhQdPduyetKqlcntHdrRepository.findById(dataMap.getIdQdKqLcnt());
    if(bySoQd.isPresent()){
      bySoQd.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
      hhQdPduyetKqlcntHdrRepository.save(bySoQd.get());
    }

    return dataMap;
  }

  @Transactional()
  void saveDataChildren(HhHopDongHdr dataMap,HhHopDongHdrReq objReq){
    hhHopDongDtlRepository.deleteAllByIdHdr(dataMap.getId());
    for (HhHopDongDtlReq ddNhapRq : objReq.getDetail()) {
      HhHopDongDtl ddNhap = new HhHopDongDtl();
      BeanUtils.copyProperties(ddNhapRq, ddNhap,"id");
      ddNhap.setIdHdr(dataMap.getId());
      ddNhap.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
      hhHopDongDtlRepository.save(ddNhap);
      hhHopDongDdiemNhapKhoRepository.deleteAllByIdHdongDtl(dataMap.getId());
      for (HhDdiemNhapKhoReq dd : ddNhapRq.getChildren()) {
        HhHopDongDdiemNhapKho ddSave = new HhHopDongDdiemNhapKho();
        BeanUtils.copyProperties(dd, ddSave,"id");
        ddSave.setIdHdongDtl(ddNhap.getId());
        ddSave.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
        hhHopDongDdiemNhapKhoRepository.save(ddSave);
        hhHopDongDdiemNhapKhoVtRepository.deleteAllByIdHdongDdiemNkho(dd.getId());
        if (dd.getChildren() != null) {
          for(HhDdiemNhapKhoVtReq vt : dd.getChildren()){
            HhHopDongDdiemNhapKhoVt ddSaveVt = new HhHopDongDdiemNhapKhoVt();
            BeanUtils.copyProperties(vt, ddSaveVt,"id");
            ddSaveVt.setIdHdongDdiemNkho(ddSave.getId());
            hhHopDongDdiemNhapKhoVtRepository.save(ddSaveVt);
          }
        }
      }
    }
  }

  @Override
  @Transactional
  public HhHopDongHdr update(HhHopDongHdrReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId()))
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

    Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(objReq.getId());
    if (!qOptional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần sửa");

    if(!StringUtils.isEmpty(objReq.getSoHd())){
      if (!objReq.getSoHd().equals(qOptional.get().getSoHd())) {
        Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findBySoHd(objReq.getSoHd());
        if (qOpHdong.isPresent())
          throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");
      }
    }

    if (!qOptional.get().getSoQdKqLcnt().equals(objReq.getSoQdKqLcnt())) {
      Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getSoQdKqLcnt());
      if (!checkSoQd.isPresent())

        throw new Exception(
            "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQdKqLcnt() + " không tồn tại");
    }

    HhHopDongHdr dataDB = qOptional.get();
    BeanUtils.copyProperties(objReq, dataDB);
    hhHopDongRepository.save(dataDB);
    if (!DataUtils.isNullOrEmpty(objReq.getListCcPhapLy())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListCcPhapLy(), dataDB.getId(), HhHopDongHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getListFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getListFileDinhKem(), dataDB.getId(), HhHopDongHdr.TABLE_NAME);
    }
    this.saveDataChildren(dataDB,objReq);
    return dataDB;
  }

  @Override
  public HhHopDongHdr detail(String ids) throws Exception {
    if (StringUtils.isEmpty(ids))
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    Optional<HhHopDongHdr> qOptional = hhHopDongRepository.findById(Long.parseLong(ids));

    if (!qOptional.isPresent())
      throw new UnsupportedOperationException("Không tồn tại bản ghi");


    Map<String, String> mapDmucDvi = getMapTenDvi();
//    Map<String, String> hashMapDviLquan = getListDanhMucDviLq("NT");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()));
    qOptional.get().setTenCloaiVthh(mapVthh.get(qOptional.get().getCloaiVthh()));
    qOptional.get().setTenDvi(qlnvDmDonviRepository.findByMaDvi(qOptional.get().getMaDvi()).getTenDvi());
    qOptional.get().setHhPhuLucHdongList(hhPhuLucRepository.findBySoHd(qOptional.get().getSoHd()));
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhHopDongHdr.TABLE_NAME));
    qOptional.get().setListFileDinhKem(fileDinhKem);
    List<FileDinhKem> canCu = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhHopDongHdr.TABLE_NAME + "_CAN_CU"));
    qOptional.get().setListCcPhapLy(canCu);

    List<HhHopDongDtl> allByIdHdr = hhHopDongDtlRepository.findAllByIdHdr(qOptional.get().getId());
    allByIdHdr.forEach(item -> {
      List<HhHopDongDdiemNhapKho> allByIdHdongDtl = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongDtl(item.getId());
      allByIdHdongDtl.forEach(s -> {
        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
//        QlnvDmDonvi dvi = qlnvDmDonviRepository.findByMaDvi(s.getMaDvi());
//        s.setTenDvi(dvi.getTenDvi());
//        if (qOptional.get().getLoaiVthh().startsWith("02")) {
//          dvi = qlnvDmDonviRepository.findByMaDvi(s.getMaDvi());
//          if (dvi != null) {
//            s.setTenDvi(dvi.getTenDvi());
//          }
//        } else {
//          dvi = qlnvDmDonviRepository.findByMaDvi(s.getMaDvi());
//          if (dvi != null) {
//            s.setTenDvi(dvi.getTenDvi());
//          }
//        }
          List<HhHopDongDdiemNhapKhoVt> allByIdHdongDdiemNkho = hhHopDongDdiemNhapKhoVtRepository.findAllByIdHdongDdiemNkho(s.getId());
          allByIdHdongDdiemNkho.forEach( x -> {
            QlnvDmDonvi dviCon = qlnvDmDonviRepository.findByMaDvi(x.getMaDvi());
            if (dviCon != null) {
              x.setTenDvi(dviCon.getTenDvi());
              x.setDiaDiemNhap(dviCon.getDiaChi());
            }
          });
          s.setChildren(allByIdHdongDdiemNkho);
      });
      item.setChildren(allByIdHdongDtl);
      if (item.getMaDvi() != null) {
        item.setTenDvi(qlnvDmDonviRepository.findByMaDvi(item.getMaDvi()).getTenDvi());
      }
    });


//    List<HhHopDongDdiemNhapKho> ddiemNhapKhos = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(Long.parseLong(ids));
//    ddiemNhapKhos.forEach(s -> {
//      s.setTenDvi((StringUtils.isEmpty(s.getMaDvi()) ? null : mapDmucDvi.get(s.getMaDvi())));
//      s.setTenDiemKho((StringUtils.isEmpty(s.getMaDiemKho()) ? null : mapDmucDvi.get(s.getMaDiemKho())));
//    });
    qOptional.get().setDetails(allByIdHdr);
    qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
//    qOptional.get().setTenNthau(hashMapDviLquan.get(String.valueOf(Double.parseDouble(qOptional.get().getIdNthau().toString()))));

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
      page = hhHopDongRepository.selectAll(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertFullDateToString(req.getTuNgayKy()), convertFullDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql, req.getNamHd(), pageable);
    } else {
      page = hhHopDongRepository.select(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), convertFullDateToString(req.getTuNgayKy()), convertFullDateToString(req.getDenNgayKy()), req.getTrangThai(), dvql, req.getNamHd(), pageable);
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();


    page.forEach(f -> {
      f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
      f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(mapVthh.get(f.getCloaiVthh()));
//      f.setTenNthau(hashMapDviLquan.get(DataUtils.safeToDouble(f.getIdNthau()).toString()));
//      List<HhHopDongDdiemNhapKho> diaDiemNhapKhos = diaDiemNhapKhoMap.get(f.getId()) != null ? diaDiemNhapKhoMap.get(f.getId()) : new ArrayList<>();
//      if (!CollectionUtils.isEmpty(diaDiemNhapKhos)) {
//        diaDiemNhapKhos.forEach(d -> {
//          d.setTenDvi(mapDmucDvi.get(d.getMaDvi()));
//        });
//        f.setHhDdiemNhapKhoList(diaDiemNhapKhos);
//      }
      f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));

    });
    return page;
  }

  @Override
  public Page<HhHopDongHdr> colection(HhHopDongSearchReq objReq, HttpServletRequest req) throws Exception {
    int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
    int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
    Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

    Page<HhHopDongHdr> dataPage = hhHopDongRepository.findAll(HhHopDongSpecification.buildSearchQuery(objReq),
        pageable);

    Set<Long> hopDongIds = dataPage.getContent().stream().map(HhHopDongHdr::getId).collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(hopDongIds))
      return dataPage;

//    Map<Long, List<HhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = hhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
//        .stream().collect(Collectors.groupingBy(HhHopDongDdiemNhapKho::getIdHdongHdr));

    // Lay danh muc dung chung
    Map<String, String> mapDmucDvi = getMapTenDvi();
    for (HhHopDongHdr hdr : dataPage.getContent()) {
      hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
//      hdr.setHhDdiemNhapKhoList(diaDiemNhapKhoMap.get(hdr.getId()));
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
    if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
      optional.get().setNguoiPduyet(getUser().getUsername());
      optional.get().setNgayPduyet(getDateTimeNow());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setNgayKy(getDateTimeNow());
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
//      objs[7] = hd.getIdNthau();
//      objs[8] = hd.getGtriHdSauVat();
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
  public Page<HhHopDongHdr> lookupData(HhHopDongSearchReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("ngay_sua", "ngay_tao").descending());
    UserInfo userInfo = UserUtils.getUserInfo();
    Page<HhHopDongHdr> page = hhHopDongRepository.lookupData(req.getLoaiVthh(), req.getNamHd(), userInfo.getDvql(), req.getTrangThai(), pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    page.forEach(f -> {
      f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
      f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(mapVthh.get(f.getCloaiVthh()));
      f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
    });
    return page;
  }

  @Override
  public ReportTemplateResponse preview(HhHopDongHdrReq req) throws Exception {
    try {
      HhHopDongHdr hhHopDongHdr = detail(req.getId().toString());
      if (!hhHopDongHdr.getLoaiVthh().startsWith("02")) {

        for (HhHopDongDtl detail : hhHopDongHdr.getDetails()) {
          BigDecimal sumTtChildChild = BigDecimal.ZERO;
          BigDecimal sumSlChildChild = BigDecimal.ZERO;
          for (HhHopDongDdiemNhapKho child : detail.getChildren()) {
            for (HhHopDongDdiemNhapKhoVt childChild : child.getChildren()) {
              childChild.setTongThanhTien(childChild.getSoLuong().multiply(BigDecimal.valueOf(hhHopDongHdr.getDonGia())));
              childChild.setDonGia(hhHopDongHdr.getDonGia());
              sumTtChildChild = sumTtChildChild.add(childChild.getTongThanhTien());
              sumSlChildChild = sumSlChildChild.add(childChild.getSoLuong());
            }
            child.setTongThanhTien(sumTtChildChild);
            child.setSoLuong(sumSlChildChild);
          }
          hhHopDongHdr.setTongThanhTien(sumTtChildChild);
          hhHopDongHdr.setTongSoLuong(sumSlChildChild);
        }
      } else {
        BigDecimal tongSoLuong = BigDecimal.ZERO;
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        for (HhHopDongDtl detail : hhHopDongHdr.getDetails()) {
          detail.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(detail.getDonGiaVat().multiply(detail.getSoLuong())));
          tongThanhTien = tongThanhTien.add(detail.getDonGiaVat().multiply(detail.getSoLuong()));
          tongSoLuong = tongSoLuong.add(detail.getSoLuong());
        }
        hhHopDongHdr.setTongSoLuongStr(docxToPdfConverter.convertBigDecimalToStr(tongSoLuong));
        hhHopDongHdr.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien));
        hhHopDongHdr.setTenLoaiVthh(hhHopDongHdr.getTenCloaiVthh().toUpperCase());
      }
      ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
      byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
      ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
      return docxToPdfConverter.convertDocxToPdf(inputStream, hhHopDongHdr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Transactional()
  @Override
  public void saveSoTienTinhPhat(HhHopDongDtlReq dataMap) throws Exception {
    Optional<HhHopDongDtl> hhHopDongDtl = hhHopDongDtlRepository.findById(dataMap.getId());
    if (!hhHopDongDtl.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    HhHopDongDtl hopDongDtl = hhHopDongDtl.get();
    hopDongDtl.setTgianGiaoThucTe(dataMap.getTgianGiaoThucTe());
    hopDongDtl.setSoNgayGiaoCham(dataMap.getSoNgayGiaoCham());
    hopDongDtl.setSlGiaoCham(dataMap.getSlGiaoCham());
    hopDongDtl.setMucPhat(dataMap.getMucPhat());
    hopDongDtl.setThanhTienTinhPhat(dataMap.getThanhTienTinhPhat());
    hhHopDongDtlRepository.save(hopDongDtl);
  }
}
