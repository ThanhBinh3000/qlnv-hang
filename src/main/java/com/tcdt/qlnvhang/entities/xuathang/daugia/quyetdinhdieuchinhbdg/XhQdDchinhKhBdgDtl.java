package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBdgDtl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;

  private Long idHdr;

  private String maDvi;
  @Transient
  private String tenDvi;

  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;

  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;

  private String soDxuat;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayTao;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayPduyet;

  @Temporal(TemporalType.DATE)
  private Date tgianDkienTu;

  @Temporal(TemporalType.DATE)
  private Date tgianDkienDen;

  private String trichYeu;

  private BigDecimal tongSoLuong;

  private Integer slDviTsan;

  private BigDecimal tongTienGiaKhoiDiemDx;

  private BigDecimal tongKhoanTienDatTruocDx;

  private String moTaHangHoa;

  private String diaChi;

  private Integer tgianTtoan;

  private String tgianTtoanGhiChu;

  private String pthucTtoan;

  private Integer tgianGnhan;

  private String tgianGnhanGhiChu;

  private String pthucGnhan;

  private String thongBaoKh;

  private BigDecimal khoanTienDatTruoc;

  @Transient
  private XhQdDchinhKhBdgHdr xhQdPdKhBdg;

  @Transient
  private XhDxKhBanDauGia xhDxKhBanDauGia;

  @Transient
  private List<XhQdDchinhKhBdgPl> children = new ArrayList<>();

}
