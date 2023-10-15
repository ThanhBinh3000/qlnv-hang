package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhThBaoCaoKqHdr.TABLE_NAME)
@Data
public class XhThBaoCaoKqHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_BAO_CAO_KQ_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThBaoCaoKqHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhThBaoCaoKqHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soBaoCao;
  private LocalDate ngayBaoCao;
  private Long idQd;
  private String soQd;
  private LocalDate ngayQd;
  private String noiDung;
  private String trangThai;
  private LocalDate ngayDuyetTp;
  private Long idTp;
  private LocalDate ngayDuyetLdc;
  private Long idLdc;
  private String lyDoTuChoi;

  @Transient
  private String tenDvi;
  @Transient
  private String tenTrangThai;

  @Transient
  private List<FileDinhKem> fileDinhKem;

  @Transient
  private List<XhThBaoCaoKqDtl> children = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }
}
