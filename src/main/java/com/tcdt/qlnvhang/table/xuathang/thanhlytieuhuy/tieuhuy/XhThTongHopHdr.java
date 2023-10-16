package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhThTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class XhThTongHopHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_TONG_HOP_HDR";

  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThTongHopHdr.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhThTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDate thoiGianThTu;
  private LocalDate thoiGianThDen;
  private String trangThai;
  private String trangThaiTh;

  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiTl;

  @Transient
  private String tenDvi;

  @Transient
  private List<XhThTongHopDtl> children = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }
  public String getTenTrangThaiTh(){
    return TrangThaiAllEnum.getLabelById(getTrangThaiTh());
  }
}
