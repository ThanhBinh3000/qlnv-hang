package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = HopDongMttHdr.TABLE_NAME)
public class HopDongMttHdr extends TrangThaiBaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "HH_HD_MTT_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;

  private Integer namHd;

  private Long idQdKq;
  private Long idQdPdSldd;

  private String soQdKq;
  private Long idQdGiaoNvNh;
  private String soQdGiaoNvNh;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKyQdKq;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayMkho;

  private String soQd;

  private String soHd;

  private String tenHd;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHluc;

  private String ghiChuNgayHluc;

  private String loaiHdong;
  @Transient
  private String tenLoaiHdong;

  private String ghiChuLoaiHdong;

  private Integer tgianThienHd;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date tgianGnhanTu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date tgianGnhanDen;

  private String ghiChuTgianGnhan;

  private String noiDungHdong;

  private String dkienHanTtoan;

  private String maDvi;
  @Transient
  private String tenDvi;

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
  @Transient
  private String tenLoaiVthh;

  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;

  private String moTaHangHoa;

  private String dviTinh;

  private BigDecimal soLuong;

  private BigDecimal donGiaGomThue;

  private BigDecimal thanhTien;

  private String ghiChu;

  private BigDecimal tongSoLuongQdKhChuakyHd;

  private String trangThaiNh;
  @Transient
  private String tenTrangThaiNh;

  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  @Transient
  private FileDinhKem fileDinhKem;

  @Transient
  private List<HhQdPheduyetKqMttSLDD> children = new ArrayList<>();


//  Phụ lục

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

  private String dviCungCap;

  private Long idKqCgia;

  @Transient
  private List<HopDongMttHdr> phuLuc = new ArrayList<>();

  @Transient
  private List<DiaDiemGiaoNhanMtt> phuLucDtl = new ArrayList<>();

  @Transient
  private List<HhQdGiaoNvNhangDtl> qdGiaoNvuDtlList = new ArrayList<>();

  @Transient
  private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

}
