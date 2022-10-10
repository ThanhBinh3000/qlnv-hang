package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdCuuTroDtl.TABLE_NAME)
@Data
public class XhQdCuuTroDtl extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_CUU_TRO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdCuuTroDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdCuuTroDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhQdCuuTroDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idQd;
  private Long idTongHop;
  private String maDvi;
  private String noiDung;
  private Long soLuong;
  private Long donGia;
  private LocalDate ngayDxuat;
  private LocalDate thoiGianThucHien;
  private Long thanhTien;
  @Transient
  private List<XhQdCuuTroKho> phuongAnXuat = new ArrayList<>();
  @Transient
  private String tenDvi;
  @Transient
  private String soTongHop;

}
