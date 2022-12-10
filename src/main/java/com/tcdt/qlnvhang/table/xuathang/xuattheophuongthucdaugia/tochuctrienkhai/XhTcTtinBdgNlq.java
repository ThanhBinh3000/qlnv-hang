
package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgNlq.TABLE_NAME)
public class XhTcTtinBdgNlq extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_NLQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_NLQ_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_NLQ_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_NLQ_SEQ")
  private Long id;
  private Long idTtinHdr;
  private Long idTtinDtl;
  private String maDvi;
  private String hoVaTen;
  private LocalDate chucVu;
  private String diaChi;
  private String giayTo;
  private String loai; //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia
  @Transient
  private String tenDvi;
}
