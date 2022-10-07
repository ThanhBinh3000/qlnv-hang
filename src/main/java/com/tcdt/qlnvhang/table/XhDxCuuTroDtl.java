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
@Table(name = XhDxCuuTroDtl.TABLE_NAME)
@Data
public class XhDxCuuTroDtl extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DX_CUU_TRO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxCuuTroDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDxCuuTroDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDxCuuTroDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDxuat;
  private String soDxuat;
  private String noiDung;
  private Long soLuong;
  private Long thanhTien;
  @Transient
  private List<XhDxCuuTroKho> phuongAnXuat = new ArrayList<>();
}
