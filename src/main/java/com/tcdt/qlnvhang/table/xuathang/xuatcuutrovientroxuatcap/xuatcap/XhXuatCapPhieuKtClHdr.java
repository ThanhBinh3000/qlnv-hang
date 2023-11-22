package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;


import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhXuatCapPhieuKtClHdr.TABLE_NAME)
@Data
public class XhXuatCapPhieuKtClHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XC_PHIEU_KT_CL_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XC_PHIEU_KT_CL_HDR_SEQ")
  @SequenceGenerator(sequenceName = "XH_XC_PHIEU_KT_CL_HDR_SEQ", allocationSize = 1, name = "XH_XC_PHIEU_KT_CL_HDR_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieuKtCl;
  private LocalDate ngayLapPhieu;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate thoiHanXuatCtVt;
  private String nguoiKn;
  private String truongPhong;
  private String thuKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String hinhThucBq;
  private BigDecimal soLuongXuat;
  private BigDecimal soLuongNhan;
  private String ketQua;
  private String ketLuan;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String soBbTinhKho;

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
  private String tenCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @Transient
  private List<XhXuatCapPhieuKtClDtl> ketQuaPhanTich= new ArrayList<>();
  @Transient
  private Map<String, String> mapDmucDvi = new ArrayMap<>();
  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    if (!DataUtils.isNullObject(getMaNganKho())) {
      String maCuc = getMaNganKho().length() >= 6 ? getMaNganKho().substring(0, 6) : "";
      String maChiCuc = getMaNganKho().length() >= 8 ? getMaNganKho().substring(0, 8) : "";
      String maDiemKho = getMaNganKho().length() >= 10 ? getMaNganKho().substring(0, 10) : "";
      String maNhaKho = getMaNganKho().length() >= 12 ? getMaNganKho().substring(0, 12) : "";
      String maNganKho = getMaNganKho().length() >= 14 ? getMaNganKho().substring(0, 14) : "";
      String tenCuc = mapDmucDvi.containsKey(maCuc) ? mapDmucDvi.get(maCuc) : null;
      String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
      String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
      String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
      String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
      String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
      String tenDvi = mapDmucDvi.containsKey(maDvi) ? mapDmucDvi.get(maDvi) : null;
      setTenCuc(tenCuc);
      setTenChiCuc(tenChiCuc);
      setTenDiemKho(tenDiemKho);
      setTenNhaKho(tenNhaKho);
      setTenNganKho(tenNganKho);
      setTenLoKho(tenLoKho);
      setTenDvi(tenDvi);
    }
  }
}
