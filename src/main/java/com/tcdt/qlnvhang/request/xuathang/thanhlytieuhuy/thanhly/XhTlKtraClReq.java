package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class XhTlKtraClReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maQhns;
  private String soPhieuKtcl;
  private LocalDate ngayLap;
  private Long idQdXh;
  private String soQdXh;
  private Long idBbLayMau;
  private String soBbLayMau;
  private LocalDate ngayLayMau;
  private String maDiaDiem;
  private Long idDsHdr;
  private LocalDate ngayKnghiem;
  private Long idThuKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String dviTinh;
  private String hinhThucBaoQuan;
  private String ketQua;
  private String nhanXet;
  private List<XhTlKtraClDtl> children = new ArrayList<>();

  //
  private String maDviSr;
  private String phanLoai;
  private String typeLt;
  private String typeVt;

  public String getTypeLt() {
    if(Objects.equals(phanLoai, "LT")){
      return "1";
    }
    return typeLt;
  }

  public String getTypeVt() {
    if(Objects.equals(phanLoai, "VT")){
      return "1";
    }
    return typeLt;
  }
}
