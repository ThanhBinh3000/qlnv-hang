package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhKqBdgHdr.TABLE_NAME)
public class XhKqBdgHdr extends TrangThaiBaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_KQ_BDG_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BDG_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_KQ_BDG_HDR_SEQ", allocationSize = 1, name = "XH_KQ_BDG_HDR_SEQ")
  private Long id;

  private String soQdKq;

  private String trichYeu;

  private Integer nam;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHluc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKy;

  private String maThongBao;

  private String soBienBan;

  private String pthucGnhan;

  private Integer tgianGnhan;

  private String tgianGnhanGhiChu;

  private String ghiChu;

  private String maDvi;

  private String loaiVthh;

  // Transient
  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();
  @Transient
  private String tenLoaiVthh;

}
