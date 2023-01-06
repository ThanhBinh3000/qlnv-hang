package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ThongTinDauGiaReq extends BaseRequest {
  private Long id;

  private String soQdPd;

  private Long idQdPdDtl;

  private Integer nam;

  private String maThongBao;

  private String trichYeuTbao;

  private String tenToChuc;

  private String sdtToChuc;

  private String diaChiToChuc;

  private String taiKhoanToChuc;

  private String soHd;

  private String ngayKyHd;

  private String hthucTchuc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianDkyTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianDkyDen;

  private String ghiChuTgianDky;

  private String diaDiemDky;

  private String dieuKienDky;

  private String tienMuaHoSo;

  private String buocGia;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianXemTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianXemDen;

  private String ghiChuTgianXem;

  private String diaDiemXem;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianNopTienTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianNopTienDen;

  private String ghiChuTgianNopTien;

  private String pthucTtoan;

  private String donViThuHuong;

  private String stkThuHuong;

  private String nganHangThuHuong;

  private String chiNhanhNganHang;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianDauGiaTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianDauGiaDen;

  private String diaDiemDauGia;

  private String hthucDgia;

  private String pthucDgia;

  private String dkienCthuc;

  private Integer ketQua; // 0 : Trượt 1 Trúng

  private String soBienBan;

  private String trichYeuBban;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayKyBban;

  // Transient

  private List<ThongTinDauGiaDtlReq> children = new ArrayList<>();

  private List<XhTcTtinBdgNlq> listNguoiLienQuan = new ArrayList<>();

}
