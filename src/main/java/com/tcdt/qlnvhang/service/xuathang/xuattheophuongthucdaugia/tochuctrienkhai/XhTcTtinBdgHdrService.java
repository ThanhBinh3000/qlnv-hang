package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.*;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.XhQdCuuTroHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPlDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.*;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class XhTcTtinBdgHdrService extends BaseServiceImpl {
  @Autowired
  private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;
  @Autowired
  private XhTcTtinBdgThongBaoRepository xhTcTtinBdgThongBaoRepository;
  @Autowired
  private XhTcTtinBdgKetQuaRepository xhTcTtinBdgKetQuaRepository;
  @Autowired
  private XhTcTtinBdgNlqRepository xhTcTtinBdgNlqRepository;
  @Autowired
  private XhTcTtinBdgTaiSanRepository xhTcTtinBdgTaiSanRepository;
  @Autowired
  private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;
  @Autowired
  private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTcTtinBdgHdr> searchPage(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

    req.setDvql(currentUser.getUser().getDvql());
    Page<XhTcTtinBdgHdr> search = xhTcTtinBdgHdrRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.get((s.getMaDvi())) != null) {
        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
    });
    return search;
  }

  public List<XhTcTtinBdgHdr> detail(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhTcTtinBdgHdr> allById = xhTcTtinBdgHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      List<XhTcTtinBdgDtl> listDetail = xhTcTtinBdgDtlRepository.findByIdTtinHdr(data.getId());
      listDetail.forEach(s -> {
        List<FileDinhKem> canCu = fileDinhKemService.search(s.getId(), Arrays.asList(XhTcTtinBdgDtl.TABLE_NAME + "_CAN_CU"));
        s.setCanCu(canCu);

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(s.getId(), Arrays.asList(XhTcTtinBdgDtl.TABLE_NAME + "_DINH_KEM"));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
          s.setFileDinhKem(fileDinhKem.get(0));
        }

        //tai san
        List<XhTcTtinBdgTaiSan> listTaiSan = xhTcTtinBdgTaiSanRepository.findByIdTtinDtl(s.getId());
        setTenTaiSan(listTaiSan, mapDmucDvi, mapVthh);
        s.setListTaiSan(listTaiSan);

        //nguoi lien quan
        List<XhTcTtinBdgNlq> listNlq = xhTcTtinBdgNlqRepository.findByIdTtinDtl(s.getId());
        s.setListNguoiLienQuan(listNlq);
      });

      //tai san tu quyet dinh de lam template
      List<XhTcTtinBdgTaiSan> listTaiSanTmp = new ArrayList<>();
      List<XhQdPdKhBdgPlDtl> listPhanLo = xhQdPdKhBdgPlDtlRepository.findByIdQdHdr(data.getIdQdPdKh());

      listPhanLo.forEach(s -> {
        XhTcTtinBdgTaiSan row = new XhTcTtinBdgTaiSan();
        row.setIdTtinHdr(data.getId());
        row.setIdTtinDtl(null);
        row.setMaDvi(s.getMaDvi());
        row.setMaDiaDiem(DataUtils.isNullOrEmpty(s.getMaLoKho()) ? s.getMaNganKho() : s.getMaLoKho());
        row.setSoLuong(DataUtils.safeToLong(s.getSoLuong()));
        row.setDonGia(DataUtils.safeToLong(s.getDonGiaVat()));
        row.setDonGiaCaoNhat(0L);
        row.setCloaiVthh(s.getCloaiVthh());
        row.setMaDvTaiSan(s.getMaDviTsan());
        row.setTonKho(0L);
        row.setDonViTinh(s.getDviTinh());
        row.setGiaKhoiDiem(DataUtils.safeToLong(s.getGiaKhoiDiem()));
        row.setSoTienDatTruoc(DataUtils.safeToLong(s.getTienDatTruoc()));
        row.setSoLanTraGia(0);
        row.setNguoiTraGiaCaoNhat("");
        listTaiSanTmp.add(row);
      });
      setTenTaiSan(listTaiSanTmp, mapDmucDvi, mapVthh);

      data.setDetail(listDetail);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setListTaiSanQd(listTaiSanTmp);
    });
    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
    return allById;
  }

  @Transactional(rollbackFor = Exception.class)
  public XhTcTtinBdgHdr update(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
    if (DataUtils.isNullObject(req.getId()))
      throw new Exception("Tham số không hợp lệ.");
    XhTcTtinBdgHdr currentRow = xhTcTtinBdgHdrRepository.findById(req.getId()).orElse(null);
    if (DataUtils.isNullObject(currentRow))
      throw new Exception("Không tìm thấy dữ liệu.");
    BeanUtils.copyProperties(req, currentRow, "id");

    req.getDetail().forEach(s -> {

      if (!DataUtils.isNullOrEmpty(s.getCanCu())) {
        fileDinhKemService.saveListFileDinhKem(s.getCanCu(), s.getId(), XhTcTtinBdgDtl.TABLE_NAME + "_CAN_CU");
      }
      if (!DataUtils.isNullObject(s.getFileDinhKem())) {
        fileDinhKemService.saveListFileDinhKem(Arrays.asList(s.getFileDinhKem()), s.getId(), XhTcTtinBdgDtl.TABLE_NAME + "_DINH_KEM");
      }
    });
    xhTcTtinBdgTaiSanRepository.deleteByIdTtinHdr(req.getId());
    xhTcTtinBdgNlqRepository.deleteByIdTtinHdr(req.getId());
    return currentRow;
  }

  private void setTenTaiSan(List<XhTcTtinBdgTaiSan> listTaiSan, Map<String, String> mapDmucDvi, Map<String, String> mapVthh) {
    listTaiSan.forEach(taiSan -> {
      String maChiCuc = taiSan.getMaDvTaiSan().length() >= 8 ? taiSan.getMaDvTaiSan().substring(0, 8) : "";
      String maDiemKho = taiSan.getMaDvTaiSan().length() >= 10 ? taiSan.getMaDvTaiSan().substring(0, 10) : "";
      String maNhaKho = taiSan.getMaDvTaiSan().length() >= 12 ? taiSan.getMaDvTaiSan().substring(0, 12) : "";
      String maNganKho = taiSan.getMaDvTaiSan().length() >= 14 ? taiSan.getMaDvTaiSan().substring(0, 14) : "";
      String maLoKho = taiSan.getMaDvTaiSan().length() >= 16 ? taiSan.getMaDvTaiSan().substring(0, 16) : "";
      String tenDvi = mapDmucDvi.containsKey(taiSan.getMaDvi()) ? mapDmucDvi.get(taiSan.getMaDvi()) : null;
      String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
      String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
      String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
      String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
      String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
      taiSan.setTenDiemKho(tenChiCuc);
      taiSan.setTenDiemKho(tenDiemKho);
      taiSan.setTenNhaKho(tenNhaKho);
      taiSan.setTenNganKho(tenNganKho);
      taiSan.setTenLoKho(tenLoKho);
      taiSan.setTenDvi(tenDvi);

      String loaiVthh = taiSan.getCloaiVthh().length() >= 4 ? taiSan.getCloaiVthh() : "";
      String tenLoaiVthh = mapVthh.containsKey(loaiVthh) ? mapVthh.get(loaiVthh) : null;
      String tenCloaiVthh = mapVthh.containsKey(taiSan.getCloaiVthh()) ? mapVthh.get(taiSan.getCloaiVthh()) : null;
      taiSan.setTenLoaiVthh(tenLoaiVthh);
      taiSan.setTenCloaiVthh(tenCloaiVthh);
    });
  }
}

