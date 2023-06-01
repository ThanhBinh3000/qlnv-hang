
package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgNlq.TABLE_NAME)
public class XhTcTtinBdgNlq implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_NLQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_NLQ_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_NLQ_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_NLQ_SEQ")
  private Long id;
  private Long idTtinHdr;
  private String hoVaTen;
  private String soCccd;
  private String chucVu;
  private String diaChi;
  private String giayTo;
  private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia
  private BigDecimal idVirtual;

}
