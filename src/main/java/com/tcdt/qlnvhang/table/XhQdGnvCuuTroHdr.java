package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhQdGnvCuuTroHdr.TABLE_NAME)
@Data
public class XhQdGnvCuuTroHdr extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_GNV_CUU_TRO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGnvCuuTroHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdGnvCuuTroHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhQdGnvCuuTroHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiNhapXuat;
  private String soDxuat;
  private String maDvi;

  private int nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idQdPd;
  private String soQdPd;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long tongSoLuong;
  private LocalDate thoiGianGiaoNhan;
  private String trichYeu;
  private String trangThai;
  private String trangThaiXh;

  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();

  private String lyDoTuChoi;
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
  private List<XhQdGnvCuuTroDtl> noiDungCuuTro = new ArrayList<>();

  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi;
  @JsonIgnore
  @Transient
  private Map<String, String> mapVthh;

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    if (!DataUtils.isNullObject(getMaDvi())) {
      setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
    }
  }

  public void setMapVthh(Map<String, String> mapVthh) {
    this.mapVthh = mapVthh;
    if (!DataUtils.isNullObject(getLoaiVthh())) {
      setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
    }
    if (!DataUtils.isNullObject(getCloaiVthh())) {
      setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }

  public String getTrangThaiXh() {
    setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
    return trangThaiXh;
  }
}
