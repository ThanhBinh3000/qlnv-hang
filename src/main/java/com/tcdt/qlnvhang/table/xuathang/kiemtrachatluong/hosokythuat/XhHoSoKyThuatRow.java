package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.hosokythuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatRow;
import lombok.Data;
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
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @JsonManagedReference
  @Where(clause = "data_type='" + XhHoSoKyThuatRow.TABLE_NAME + "_DINH_KEM'")
  private List<FileDKemJoinHoSoKyThuatRow> fileDinhKem = new ArrayList<>();

  public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatRow> children) {
    this.fileDinhKem.clear();
    for (FileDKemJoinHoSoKyThuatRow child : children) {
      child.setDataType(XhHoSoKyThuatRow.TABLE_NAME + "_DINH_KEM");
      child.setParent(this);
    }
    this.fileDinhKem.addAll(children);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idDtl")
  @JsonIgnore
  private XhHoSoKyThuatDtl xhHoSoKyThuatDtl;
}
