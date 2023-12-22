package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbQuyetDinhDcTcHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbQuyetDinhDcTcHdr extends BaseEntity implements Serializable, Cloneable{

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_TC_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = DcnbQuyetDinhDcTcHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcTcHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiDc;
  private String tenLoaiDc;
  private Integer nam;
  private String soQdinh;
  private LocalDate ngayKyQdinh;
  private LocalDate ngayDuyetTc;
  private Long nguoiDuyetTcId;
  private LocalDate ngayBanHanhTc;
  private Long nguoiBanHanhTcId;
  private String trichYeu;
  private String maDvi;
  private String tenDvi;
  private String maThop;
  private Long idThop;
  private String maDxuat;
  private Long idDxuat;
  private String type; // loại TH (tổng hợp), DX (đề xuất)
  @Access(value=AccessType.PROPERTY)
  private String trangThai;
  private String lyDoTuChoi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String qdinhNhapCucId;
  private String soQdinhNhapCuc;
  private String qdinhXuatCucId;
  private String soQdinhXuatCuc;
  private String tenLoaiHinhNhapXuat;
  private String tenKieuNhapXuat;
  private String yKienVuKhac;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> quyetDinh = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "HDR_ID")
  private List<DcnbQuyetDinhDcTcDtl> danhSachQuyetDinh = new ArrayList<>();

  public void setTrangThai(String trangThai) {
    this.trangThai = trangThai;
    this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
  }
}
