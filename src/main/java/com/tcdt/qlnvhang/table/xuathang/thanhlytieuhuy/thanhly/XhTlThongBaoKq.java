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
@Table(name = XhTlThongBaoKq.TABLE_NAME)
@Data
public class XhTlThongBaoKq extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_THONG_BAO_KQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlThongBaoKq.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlThongBaoKq.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhTlThongBaoKq.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soThongBao;
  private LocalDate ngayThongBao;
  private Long idHoSo;
  private String soHoSo;
  private LocalDate ngayTrinhDuyet;
  private LocalDate ngayThamDinh;
  private String noiDung;
  private String lyDo;
  private String trangThai;
  private String trangThaiTb;

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
