package com.tcdt.qlnvhang.table.xuathang.hosokythuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhHoSoKyThuatRow.TABLE_NAME)
@Data
public class XhHoSoKyThuatRow extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_HO_SO_KY_THUAT_ROW";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHoSoKyThuatRow.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhHoSoKyThuatRow.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhHoSoKyThuatRow.TABLE_NAME + "_SEQ")
  private Long id;
  private String ten;
  private String loai;
  private Long soLuong;
  private String ghiChu;
  private String trangThai;

  //gia tri NLQ, HS
  private String type;
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idDtl")
  @JsonIgnore
  private XhHoSoKyThuatDtl xhHoSoKyThuatDtl;
}
