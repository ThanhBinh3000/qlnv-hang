package com.tcdt.qlnvhang.table.khotang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KT_NGAN_KHO")
@Data
public class KtNganKho implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_NGAN_KHO_SEQ")
  @SequenceGenerator(sequenceName = "KT_NGAN_KHO_SEQ", allocationSize = 1, name = "KT_NGAN_KHO_SEQ")
  Long id;
  String maNgankho;
  String tenNgankho;
  String diaChi;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate namSudung;
  String nhiemVu;
  String ghiChu;
  String loaikhoId;
  String chatluongId;
  String tinhtrangId;
  String ngankhoHientrangId;
  BigDecimal dienTichDat;
  BigDecimal tichLuongTkLt;
  BigDecimal tichLuongTkVt;
  BigDecimal tichLuongChua;
  //  Long nhakhoId;
  Long quyhoachDuyetId;
  BigDecimal tichLuongChuaLt;
  BigDecimal tichLuongChuaVt;
  BigDecimal theTichChuaLt;
  BigDecimal tichLuongKhaDung;
  BigDecimal tichLuongKdLt;
  BigDecimal tichLuongKdVt;
  String huongSuDung;
  BigDecimal tichLuongKdLtvt;
  Integer trangThaiTl;
  Integer namNhap;
  BigDecimal tichLuongChuaLtGao;
  BigDecimal tichLuongChuaLtThoc;
  BigDecimal theTichChuaVt;
  BigDecimal theTichTkLt;
  BigDecimal theTichTkVt;
  BigDecimal theTichKdLt;
  BigDecimal theTichKdVt;

  BigDecimal thanhTien;

  Long updateStatus;
  Date lastUpdate;
  String trangThai;
  Date ngayTao;
  String nguoiTao;
  Date ngaySua;
  String nguoiSua;
  String ldoTuchoi;
  Date ngayGuiDuyet;
  String nguoiGuiDuyet;
  Date ngayPduyet;
  String nguoiPduyet;
  Boolean coLoKho;

  String loaiVthh;

  String cloaiVthh;

  BigDecimal slTon;

  String dviTinh;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  Date ngayNhapDay;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  Date ngayXuatDoc;

  Long diemkhoId;

  Long tongkhoId;

  Long dtqgkvId;

  String loaiHangHoa;

  String kieuHang;
  String active;

  String maThuKho;

  Long idThuKho;

  @Transient
  UserInfo detailThuKho;


  @Transient
  String tenNhakho;

  @Transient
  String tenDiemkho;

  @Transient
  String tenLoaiVthh;

  @Transient
  String tenCloaiVthh;

  @Transient
  String tenTongKho;

  @Transient
  String tenDtkvqg;

  @Transient
  BigDecimal soLokho;

  @Transient
  Long idParent;
  @Transient
  Long idDmDonVi;


  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  List<KtNganLo> child;


  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
  @JoinColumn(name = "NHAKHO_ID", referencedColumnName = "ID")
  KtNhaKho parent;

  @Transient
  private List<FileDinhKem> fileDinhkems = new ArrayList<>();
  @Transient
  String lhKho;

  public BigDecimal getDienTichDat() {
    return ObjectUtils.isEmpty(dienTichDat) ? BigDecimal.ZERO : dienTichDat;
  }

  public void setDienTichDat(BigDecimal dienTichDat) {
    this.dienTichDat = ObjectUtils.isEmpty(dienTichDat) ? BigDecimal.ZERO : dienTichDat;
  }

  public BigDecimal getTichLuongTkLt() {
    return ObjectUtils.isEmpty(tichLuongTkLt) ? BigDecimal.ZERO : tichLuongTkLt;
  }

  public void setTichLuongTkLt(BigDecimal tichLuongTkLt) {
    this.tichLuongTkLt = ObjectUtils.isEmpty(tichLuongTkLt) ? BigDecimal.ZERO : tichLuongTkLt;
  }

  public BigDecimal getTichLuongTkVt() {
    return ObjectUtils.isEmpty(tichLuongTkVt) ? BigDecimal.ZERO : tichLuongTkVt;
  }

  public void setTichLuongTkVt(BigDecimal tichLuongTkVt) {
    this.tichLuongTkVt = ObjectUtils.isEmpty(tichLuongTkVt) ? BigDecimal.ZERO : tichLuongTkVt;
  }

  public BigDecimal getTichLuongKdLt() {
    return ObjectUtils.isEmpty(tichLuongKdLt) ? BigDecimal.ZERO : tichLuongKdLt;
  }

  public void setTichLuongKdLt(BigDecimal tichLuongKdLt) {
    this.tichLuongKdLt = ObjectUtils.isEmpty(tichLuongKdLt) ? BigDecimal.ZERO : tichLuongKdLt;
  }

  public BigDecimal getTichLuongKdVt() {
    return ObjectUtils.isEmpty(tichLuongKdVt) ? BigDecimal.ZERO : tichLuongKdVt;
  }

  public void setTichLuongKdVt(BigDecimal tichLuongKdVt) {
    this.tichLuongKdVt = ObjectUtils.isEmpty(tichLuongKdVt) ? BigDecimal.ZERO : tichLuongKdVt;
  }

  public BigDecimal getTheTichTkLt() {
    return ObjectUtils.isEmpty(theTichTkLt) ? BigDecimal.ZERO : theTichTkLt;
  }

  public void setTheTichTkLt(BigDecimal theTichTkLt) {
    this.theTichTkLt = ObjectUtils.isEmpty(theTichTkLt) ? BigDecimal.ZERO : theTichTkLt;
  }

  public BigDecimal getTheTichTkVt() {
    return ObjectUtils.isEmpty(theTichTkVt) ? BigDecimal.ZERO : theTichTkVt;
  }

  public void setTheTichTkVt(BigDecimal theTichTkVt) {
    this.theTichTkVt = ObjectUtils.isEmpty(theTichTkVt) ? BigDecimal.ZERO : theTichTkVt;
  }

  public BigDecimal getTheTichKdLt() {
    return ObjectUtils.isEmpty(theTichKdLt) ? BigDecimal.ZERO : theTichKdLt;
  }

  public void setTheTichKdLt(BigDecimal theTichKdLt) {
    this.theTichKdLt = ObjectUtils.isEmpty(theTichKdLt) ? BigDecimal.ZERO : theTichKdLt;
  }

  public BigDecimal getTheTichKdVt() {
    return ObjectUtils.isEmpty(theTichKdVt) ? BigDecimal.ZERO : theTichKdVt;
  }

  public void setTheTichKdVt(BigDecimal theTichKdVt) {
    this.theTichKdVt = ObjectUtils.isEmpty(theTichKdVt) ? BigDecimal.ZERO : theTichKdVt;
  }

  public BigDecimal getSlTon() {
    return ObjectUtils.isEmpty(slTon) ? BigDecimal.ZERO : slTon;
  }

  public void setSlTon(BigDecimal slTon) {
    this.slTon = ObjectUtils.isEmpty(slTon) ? BigDecimal.ZERO : slTon;
  }

  public BigDecimal getThanhTien() {
    return ObjectUtils.isEmpty(thanhTien) ? BigDecimal.ZERO : thanhTien;
  }

  public void setThanhTien(BigDecimal thanhTien) {
    this.thanhTien = ObjectUtils.isEmpty(thanhTien) ? BigDecimal.ZERO : thanhTien;
  }

}
