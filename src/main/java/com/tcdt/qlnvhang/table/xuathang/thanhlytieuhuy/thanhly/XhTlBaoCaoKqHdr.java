package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTlBaoCaoKqHdr.TABLE_NAME)
@Data
public class XhTlBaoCaoKqHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_BAO_CAO_KQ_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlBaoCaoKqHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhTlBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soBaoCao;
  private LocalDate ngayBaoCao;
  private Long idQd;
  private String soQd;
  private String noiDung;
  private String trangThai;
  private String lyDoTuChoi;
  @Transient
  private String tenDvi;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> fileDinhKem;

  @OneToMany(mappedBy = "baoCaoKqHdr", cascade = CascadeType.ALL)
  private List<XhTlBaoCaoKqDtl> baoCaoKqDtl = new ArrayList<>();
}
