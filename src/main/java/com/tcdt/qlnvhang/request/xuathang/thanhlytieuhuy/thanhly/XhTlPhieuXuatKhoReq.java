package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class XhTlPhieuXuatKhoReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maQhns;
  private String soPhieuXuatKho;
  private LocalDate ngayXuatKho;
  private Integer soNo;
  private Integer soCo;
  private String soQdXh;
  private Long idQdXh;
  private LocalDate ngayQdXh;
  private Long idPhieuKtcl;
  private String soPhieuKtcl;
  private Long idDsHdr;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private Long idKtv;
  private String keToanTruong;
  private String nguoiGiaoHang;
  private String soCmt;
  private String dviNguoiGiaoHang;
  private String diaChi;
  private LocalDate thoiGianGiaoNhan;
  private String loaiHinhNx;
  private String kieuNx;
  private String soBangKeCanHang;
  private Long idBangKeCanHang;
  private BigDecimal tongSoLuong;
  private String ghiChu;
  private String trangThai;
  private String donViTinh;
  private List<XhTlPhieuXuatKhoDtl> children = new ArrayList<>();

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
