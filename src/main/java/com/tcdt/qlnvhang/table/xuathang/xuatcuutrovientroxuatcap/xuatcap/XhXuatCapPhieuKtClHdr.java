package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;


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
@Table(name = XhXuatCapPhieuKtClHdr.TABLE_NAME)
@Data
public class XhXuatCapPhieuKtClHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XC_PHIEU_KT_CL_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XC_PHIEU_KT_CL_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_XC_PHIEU_KT_CL_HDR_SEQ", allocationSize = 1, name = "XH_XC_PHIEU_KT_CL_HDR_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieuKtCl;
  private LocalDate ngayLapPhieu;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate thoiHanXuatCtVt;
  private String nguoiKn;
  private String truongPhong;
  private String thuKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String hinhThucBq;
  private BigDecimal soLuongXuat;
  private BigDecimal soLuongNhan;
  private String ketQua;
  private String ketLuan;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String soBbTinhKho;

  @Transient
  private String tenDvi;
  @Transient
  private String diaChiDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenChiCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @Transient
  private List<XhXuatCapPhieuKtClDtl> ketQuaPhanTich= new ArrayList<>();

}
