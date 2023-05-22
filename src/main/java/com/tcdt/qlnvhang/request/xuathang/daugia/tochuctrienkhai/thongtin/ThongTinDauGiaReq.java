package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.math.BigDecimal;
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

  private String ghiChuBuocGia;

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

  private String ketQuaSl;

  private String ghiChu;

  private String maDvi;

  private String loaiVthh;

  private String cloaiVthh;

  private String moTaHangHoa;

  private BigDecimal khoanTienDatTruoc;

  private String thongBaoKhongThanh;

  private List<ThongTinDauGiaDtlReq> children = new ArrayList<>();

  private List<ThongTinDauGiaNtgReq> listNguoiTgia = new ArrayList<>();

  @Transient
  private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}
