package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

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
@Table(name = XhCtvtQdGiaoNvXhHdr.TABLE_NAME)
@Data
public class XhCtvtQdGiaoNvXhHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_Qd_Giao_Nv_Xh_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtQdGiaoNvXhHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtQdGiaoNvXhHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idQdPd;
  private String soQdPd;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal soLuong;
  private BigDecimal donViTinh;
  private LocalDate thoiGianGiaoNhan;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private String trangThaiXh;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private BigDecimal tongSoLuong;
  private BigDecimal thanhTien;
  private String type;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;

  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();


  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiXh;
  @Transient
  private List<XhCtvtQdGiaoNvXhDtl> noiDungCuuTro = new ArrayList<>();
}
