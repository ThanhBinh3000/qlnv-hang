package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtQdXuatCapHdr.TABLE_NAME)
@Data
public class XhCtvtQdXuatCapHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QD_XUAT_CAP_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtDeXuatHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;

  private LocalDate ngayHluc;
  private String trichYeu;
  private String trangThai;
  private String loaiVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;

  private LocalDate thoiHanXuat;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSoLuongThoc;
  private BigDecimal tongSoLuongGao;
  private BigDecimal thanhTien;


  private Long qdPaXuatCapId;

  @OneToMany(mappedBy = "xhCtvtQdXuatCapHdr", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<XhCtvtQdXuatCapDtl> xhCtvtQdXuatCapDtl = new ArrayList<>();

  @Transient
  private String tenTrangThai;
}
