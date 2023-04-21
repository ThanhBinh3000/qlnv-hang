package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBdgHdr.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgHdr extends TrangThaiBaseEntity implements Serializable {
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKyDc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHlucDc;

  private String soQdGoc;

  private Long idQdGoc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKyQdGoc;

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

  @Transient
  List<XhQdDchinhKhBdgDtl> children = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();



}
