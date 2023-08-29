package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtBbLayMauHdr.TABLE_NAME)
@Data
public class XhCtvtBbLayMauHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BB_LAY_MAU_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBbLayMauHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBbLayMauHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBbLayMauHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String loaiBienBan;
  private String maQhNs;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String ktvBaoQuan;
  private String soBienBan;
  private LocalDate ngayLayMau;
  private String dviKiemNghiem;
  private String diaDiemLayMau;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Integer soLuongMau;
  private String ppLayMau;
  private String chiTieuKiemTra;
  private Boolean ketQuaNiemPhong;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private LocalDate ngayXuatDocKho;
  private String type;
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
  private String tenDviCha;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private String tenThuKho;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @Transient
  private List<FileDinhKem> canCu =new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKemNiemPhong =new ArrayList<>();
  @Transient
  private List<XhCtvtBbLayMauDtl> nguoiLienQuan = new ArrayList<>();
}
