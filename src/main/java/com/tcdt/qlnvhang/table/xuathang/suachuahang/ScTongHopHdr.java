package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class ScTongHopHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_TONG_HOP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private String trangThai;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;
  @Transient
  private List<ScTongHopDtl> children = new ArrayList<>();
}
