package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhCtvtDeXuatHdr.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class XhCtvtDeXuatHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_DE_XUAT_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String soDx;
  private String trichYeu;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private LocalDate ngayDx;
  private LocalDate ngayKetThuc;
  private String noiDung;
  private String trangThai;
  private Long idThop;
  private String maTongHop;
  private Long idQdPd;
  private String soQdPd;
  private LocalDate ngayKyQd;
  private BigDecimal tongSoLuong;
  private BigDecimal tongSoLuongDeXuat;
  private BigDecimal tongSoLuongXuatCap;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private BigDecimal tonKhoDvi;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi = new ArrayMap<>();
  @Transient
  private String tenDvi;
  @Transient
  private String tenDviDx;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiTh;
  @Transient
  private String tenTrangThaiQd;

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    String tenDvi = mapDmucDvi.containsKey(maDvi) ? mapDmucDvi.get(maDvi) : null;
    setTenDvi(tenDvi);
    String maDviDx = maDvi.substring(0, maDvi.length() - 2);
    String tenDviDx = mapDmucDvi.containsKey(maDviDx) ? mapDmucDvi.get(maDviDx) : null;
    setTenDviDx(tenDviDx);
  }

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhCtvtDeXuatHdr.TABLE_NAME + "_CAN_CU'")
  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

  public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
    this.canCu.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhCtvtDeXuatHdr.TABLE_NAME + "_CAN_CU");
        s.setXhCtvtDeXuatHdr(this);
      });
      this.canCu.addAll(fileDinhKem);
    }
  }

  @OneToMany(mappedBy = "xhCtvtDeXuatHdr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<XhCtvtDeXuatPa> deXuatPhuongAn = new ArrayList<>();

  public void setDeXuatPhuongAn(List<XhCtvtDeXuatPa> data) {
    deXuatPhuongAn.clear();
    if (!DataUtils.isNullOrEmpty(data)) {
      data.forEach(s -> {
        s.setXhCtvtDeXuatHdr(this);
      });
      deXuatPhuongAn.addAll(data);
    }
  }

  public String getTrangThai() {
    setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
    return trangThai;
  }
}
