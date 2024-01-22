package com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXkTongHopHdr.TABLE_NAME)
@Getter
@Setter
public class XhXkTongHopHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_TONG_HOP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkTongHopHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkTongHopHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkTongHopHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
//  private LocalDateTime ngayDeXuat;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSlHienTai;
  private Integer capTh;
  private String loai;
  //Lưu id và mã danh sách tổng hợp của tổng cục, bản ghi Cục (capth = 2) sẽ có giá trị 2 cột này.
  private Long idThTc;
  private String maDanhSachTc;

  private Long idBaoCao;
  private String soBaoCao;
  // Ktra cl vt-tb trongh thời gian bảo hành theo hđ
  private Integer soLanLm;
  // update số QĐ giao NvXh
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  @Transient
  private String tenTrangThai;

  @Transient
  private String tenDvi;

  @Transient
  private String maDvql;

  @Transient
  private String tenDvql;

  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();

  @OneToMany(mappedBy = "tongHopHdr", cascade = CascadeType.ALL)
  private List<XhXkTongHopDtl> tongHopDtl = new ArrayList<>();
}
