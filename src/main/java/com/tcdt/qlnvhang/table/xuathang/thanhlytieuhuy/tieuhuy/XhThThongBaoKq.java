package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = XhThThongBaoKq.TABLE_NAME)
@Data
public class XhThThongBaoKq extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_THONG_BAO_KQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThThongBaoKq.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThThongBaoKq.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhThThongBaoKq.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soThongBao;
  private LocalDate ngayThongBao;
  private Long idQd;
  private String soQd;
  private Long idHoSo;
  private String soHoSo;
  private String trichYeu;
  private LocalDate ngayKy;
  private String lyDo;
  private String trangThai;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  @Transient
  private String tenDvi;
  @Transient
  private String tenTrangThai;

  @Transient
  private List<FileDinhKem> fileDinhKem;

}
