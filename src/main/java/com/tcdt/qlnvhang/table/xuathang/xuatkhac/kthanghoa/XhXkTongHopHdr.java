package com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXkTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class XhXkTongHopHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_Xk_TONG_HOP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDate ngayDeXuatTu;
  private LocalDate ngayDeXuatDen;
  private String trangThai;
  private String trangThaiKtCl;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSlHienTai;

  @Transient
  private String tenTrangThai;

  @Transient
  private String tenDvi;

  @Transient
  private String maDvql;
  @Transient
  private String tenDvql;

  @OneToMany(mappedBy = "tongHopHdr", cascade = CascadeType.ALL)
  private List<XhXkTongHopDtl> tongHopDtl = new ArrayList<>();
}
