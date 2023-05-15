package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBdgHdr.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgHdr implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_DC_KH_BDG_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_QD_DC_KH_BDG_HDR_SEQ", allocationSize = 1, name = "XH_QD_DC_KH_BDG_HDR_SEQ")
  private Long id;

  private Integer nam;

  private String maDvi;
  @Transient
  private String tenDvi;

  private String soQdDc;

  private LocalDate ngayKyDc;

  private LocalDate ngayHlucDc;

  private String soQdGoc;

  private Long idQdGoc;

  private LocalDate ngayKyQdGoc;

  private String trichYeu;

  private String soQdCc;

  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;

  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;

  private String moTaHangHoa;

  private String loaiHinhNx;

  private String kieuNx;

  private String tchuanCluong;

  private Integer slDviTsan;

  private String trangThai;
  @Transient
  private String tenTrangThai;

  private LocalDate ngayTao;

  private Long nguoiTaoId;

  private LocalDate ngaySua;

  private Long nguoiSuaId;

  private LocalDate ngayGuiDuyet;

  private Long nguoiGuiDuyetId;

  private LocalDate ngayPduyet;

  private Long nguoiPduyetId;

  private String lyDoTuChoi;

  @Transient
  private List<XhQdDchinhKhBdgDtl> children = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();
}
