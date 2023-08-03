package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhPhieuKnclDtl.TABLE_NAME)
@Getter
@Setter
public class XhPhieuKnclDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_PHIEU_KNCL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhPhieuKnclDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhPhieuKnclDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhPhieuKnclDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String ten;
  private String loai;
  private Long soLuong;
  private String ghiChu;
  private String trangThai;
  //gia tri HTBQ hinh thuc bao quan, KQPT ket qua phan tich
  private String type;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhPhieuKnclHdr xhPhieuKnclHdr;

  //phan rieng
  private String chiSoCl;
  private String ketQua;
  private String phuongPhap;
  private String danhGia;
}
