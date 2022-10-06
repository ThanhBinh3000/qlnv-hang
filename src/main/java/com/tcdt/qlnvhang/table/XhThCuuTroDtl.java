package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhThCuuTroDtl.TABLE_NAME)
@Data
public class XhThCuuTroDtl extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_CUU_TRO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThCuuTroDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThCuuTroDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhThCuuTroDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idTongHop;
  private Long idDxuat;
  private String maDvi;
  private String noiDung;
  private Long soLuong;
  private Long donGia;
  private LocalDate ngayDxuat;
  private LocalDate thoiGianThucHien;

  @Transient
  private List<XhThCuuTroKho> phuongAnXuat = new ArrayList<>();
  @Transient
  private String tenDvi;
  @Transient
  private String soTongHop;

}
