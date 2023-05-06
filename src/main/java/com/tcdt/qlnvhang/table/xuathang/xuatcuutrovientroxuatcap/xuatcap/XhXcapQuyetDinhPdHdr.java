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
@Table(name = XhXcapQuyetDinhPdHdr.TABLE_NAME)
@Data
public class XhXcapQuyetDinhPdHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XCAP_QUYET_DINH_PD_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXcapQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXcapQuyetDinhPdHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXcapQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idQdPd;
  private String soQdPd;
  private Long idQdGiaoNv;
  private String soQdGiaoNv;
  private LocalDate ngayHlucQdPd;
  private String trichYeu;
  private String trangThai;

  private String loaiVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiHanXuat;
  private BigDecimal tongSoLuongThoc;
  private BigDecimal tongSoLuongGao;
  private BigDecimal thanhTien;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> fileDinhKem;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();

  @OneToMany(mappedBy = "quyetDinhPdHdr", cascade = CascadeType.ALL)
  private List<XhXcapQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();
}
