package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlBbLayMauReq extends BaseRequest {
  private Long id;
  private String loaiBienBan;
  private Integer nam;
  private String maQhns;
  private String soBienBan;
  private String ngayLayMau;
  private Long idQdXh;
  private String soQdXh;
  private String dviKnghiem;
  private String diaDiemLayMau;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String truongBpKtbq;
  private BigDecimal soLuongMau;
  private Boolean ketQuaNiemPhong;
  private String ghiChu;
  private List<XhTlBbLayMauDtl> children = new ArrayList<>();

  //
  private String maDviSr;
  private String phanLoai;
  private String typeLt;
  private String typeVt;
}
