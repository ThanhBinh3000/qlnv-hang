package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HopDongMttHdrReq extends BaseRequest {

  private Long id;

  private Long idQdKq;

  private Integer namHd;

  private String soQdKq;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayKyQdKq;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayMkho;

  private String soQd;

  private String soHd;

  private String tenHd;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayHluc;

  private String ghiChuNgayHluc;

  private String loaiHdong;

  private String ghiChuLoaiHdong;

  private Integer tgianThienHd;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianGnhanTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date tgianGnhanDen;

  private String ghiChuTgianGnhan;

  private String noiDungHdong;

  private String dkienHanTtoan;

  private String maDvi;

  private String diaChi;

  private String mst;

  private String tenNguoiDdien;

  private String chucVu;

  private String sdt;

  private String fax;

  private String stk;

  private String moLai;

  private String ttinGiayUyQuyen;

  private Long idDviBan;
  private Long idKqCgia;

  private String tenDviBan;

  private String diaChiDviBan;

  private String mstDviBan;

  private String tenNguoiDdienDviBan;

  private String chucVuDviBan;

  private String sdtDviBan;

  private String faxDviBan;

  private String stkDviBan;

  private String moLaiDviBan;

  private String loaiVthh;

  private String cloaiVthh;

  private String moTaHangHoa;

  private String dviTinh;

  private BigDecimal soLuong;

  private BigDecimal donGiaGomThue;

  private BigDecimal thanhTien;

  private String ghiChu;

  private BigDecimal tongSoLuongQdKhChuakyHd;

  private Long idQdGiaoNvNh;
  private String soQdGiaoNvNh;

  @Transient
  private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

  @Transient
  private FileDinhKemReq fileDinhKem;

  private List<DiaDiemGiaoNhanMttReq> children = new ArrayList<>();


  //    Phụ lục

  private Long idHd;

  private String soPhuLuc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHlucPhuLuc;

  private String noiDungPhuLuc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHlucSauDcTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHlucSauDcDen;

  private Integer tgianThienHdSauDc;

  private String noiDungDcKhac;

  private String ghiChuPhuLuc;

  private String trangThaiPhuLuc;
  @Transient
  private String tenTrangThaiPhuLuc;

  @Transient
  private List<HopDongMttHdrReq> phuLuc = new ArrayList<>();

  @Transient
  private List<DiaDiemGiaoNhanMttReq> phuLucDtl = new ArrayList<>();

}
