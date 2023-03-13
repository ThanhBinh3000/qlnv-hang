package com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhDgBbHaoDoiDtl.TABLE_NAME)
@Data
public class XhDgBbHaoDoiDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DG_BB_HAO_DOI_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBbHaoDoiDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDgBbHaoDoiDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDgBbHaoDoiDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private Long idPhieuKnCl;
  private String soPhieuKnCl;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private Long idBkCanHang;
  private String soBkCanHang;
  private LocalDate ngayXuatKho;
  private BigDecimal slXuat;

}
