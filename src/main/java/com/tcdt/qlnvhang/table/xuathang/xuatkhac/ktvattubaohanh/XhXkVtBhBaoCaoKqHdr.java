package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhXkVtBhBaoCaoKqHdr.TABLE_NAME)
public class XhXkVtBhBaoCaoKqHdr extends BaseEntity implements Serializable {

  public static final String TABLE_NAME = "XH_XK_VT_BH_BAO_CAO_KQ_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkVtBhBaoCaoKqHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkVtBhBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDviNhan;
  private String soBaoCao;
  private String tenBaoCao;
  private LocalDate ngayBaoCao;
  private String soCanCu;
  private String idCanCu;

  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;

  private Long idPhieuKtcl;
  private String spPhieuKtcl;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;


  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDviNhan;
  @Transient
  private String tenDvi;

  @Transient
  private List<FileDinhKem> fileDinhKems;
  @OneToMany(mappedBy = "bhBaoCaoKqHdr", cascade = CascadeType.ALL)
  private List<XhXkVtBhBaoCaoKqDtl> bhBaoCaoKqDtl = new ArrayList<>();


}
