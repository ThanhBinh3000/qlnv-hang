package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhTlTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class XhTlTongHopHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_TONG_HOP_HDR";

  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlTongHopHdr.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhTlTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;

  @Transient
  private List<XhTlTongHopDtl> children = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }
}
