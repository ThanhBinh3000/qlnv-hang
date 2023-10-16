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
@Table(name = XhThQuyetDinhHdr.TABLE_NAME)
@Data
public class XhThQuyetDinhHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_QUYET_DINH_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThQuyetDinhHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThQuyetDinhHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhThQuyetDinhHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private String trichYeu;
  private LocalDate ngayKy;
  private Long idHoSo;
  private String soHoSo;
  private String trangThai;
  private String lyDoTuChoi;

  @Transient
  private XhThHoSoHdr xhThHoSoHdr;

  @Transient
  private List<FileDinhKem> fileDinhKem;
  @Transient
  private List<FileDinhKem> fileCanCu = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }

}
