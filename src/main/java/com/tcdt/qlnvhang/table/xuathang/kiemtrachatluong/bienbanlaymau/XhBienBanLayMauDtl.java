package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatRow;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat.XhHoSoKyThuatHdr;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhBienBanLayMauDtl.TABLE_NAME)
@Getter
@Setter
public class XhBienBanLayMauDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_BIEN_BAN_LAY_MAU_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBienBanLayMauDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBienBanLayMauDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String ma;
  private String ten;
  private String loai;
  private Long soLuong;
  private String ghiChu;
  private String trangThai;
  //gia tri NLQ, PPLM phuong phap lay mau, CTCL chi tieu chat luong
  private String type;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhBienBanLayMauHdr xhBienBanLayMauHdr;

  //phan rieng
  private String chiTieuCl;
  private String chiSoCl;
  private String chiSoClToiThieu;
  private String chiSoClToiDa;
  private String toanTu;
  private String ketQua;
  private String phuongPhap;
  private String danhGia;
}
