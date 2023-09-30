package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBienBanTkDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlBienBanTkReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhns;
  private String soBienBan;
  private LocalDate ngayLap;
  private String soQdNh;
  private Long idQdNh;
  private Long idScDanhSachHdr;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private Long idThuKho;
  private Long idLanhDaoCc;
  private String lyDoTuChoi;
  private String trangThai;
  private String ghiChu;
  private LocalDate ngayBatDau;
  private LocalDate ngayKetThuc;
  private BigDecimal tongSoLuong;
  private List<XhTlBienBanTkDtl> children = new ArrayList<>();
}
