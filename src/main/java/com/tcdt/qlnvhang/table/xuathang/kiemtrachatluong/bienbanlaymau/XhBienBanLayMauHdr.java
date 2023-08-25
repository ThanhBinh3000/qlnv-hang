package com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhBienBanLayMauHdr.TABLE_NAME)
@Getter
@Setter
public class XhBienBanLayMauHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_BIEN_BAN_LAY_MAU_HDR";

  //phan chung
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBienBanLayMauHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhBienBanLayMauHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBienBanLayMauHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soBbQd;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private String lyDoTuChoi;
  private String trangThai;
  private Long nguoiKyQdId;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  @JsonIgnore
  @Transient
  private Map<String, String> mapVthh;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @JsonIgnore
  @Transient
  private Map<String, String> mapDmucDvi = new ArrayMap<>();
  @Transient
  private String tenDvi;
  @Transient
  private String tenCuc;
  @Transient
  private String tenChiCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;

  public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
    this.mapDmucDvi = mapDmucDvi;
    if (!DataUtils.isNullObject(getMaDiaDiem())) {
      String maCuc = getMaDiaDiem().length() >= 6 ? getMaDiaDiem().substring(0, 6) : "";
      String maChiCuc = getMaDiaDiem().length() >= 8 ? getMaDiaDiem().substring(0, 8) : "";
      String maDiemKho = getMaDiaDiem().length() >= 10 ? getMaDiaDiem().substring(0, 10) : "";
      String maNhaKho = getMaDiaDiem().length() >= 12 ? getMaDiaDiem().substring(0, 12) : "";
      String maNganKho = getMaDiaDiem().length() >= 14 ? getMaDiaDiem().substring(0, 14) : "";
      String maLoKho = getMaDiaDiem().length() >= 16 ? getMaDiaDiem().substring(0, 16) : "";
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


  //phan rieng
  private String maQhns;
  private Long idQdGnv;
  private String soQdGnv;
  private LocalDate ngayKyQdGnv;
  private Long idHopDong;
  private String soHopDong;
  private Long idBangKe;
  private String soBangKe;
  private Long idBbTinhKho;
  private String soBbTinhKho;
  private LocalDate ngayXuatDocKho;
  private Long idBbHaoDoi;
  private String soBbHaoDoi;
  private LocalDate ngayKy;
  private String ktvBaoQuan;
  private String dviKiemNghiem;
  private String diaDiemLayMau;
  private Long soLuongMau;
  private String niemPhong;
  private String loaiBb;
  private String type;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhBienBanLayMauHdr.TABLE_NAME + "_DINH_KEM'")
  private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

  public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
    this.fileDinhKem.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhBienBanLayMauHdr.TABLE_NAME + "_DINH_KEM");
        s.setXhBienBanLayMauHdr(this);
      });
      this.fileDinhKem.addAll(fileDinhKem);
    }
  }


  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhBienBanLayMauHdr.TABLE_NAME + "_CAN_CU'")
  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

  public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
    this.canCu.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhBienBanLayMauHdr.TABLE_NAME + "_CAN_CU");
        s.setXhBienBanLayMauHdr(this);
      });
      this.canCu.addAll(fileDinhKem);
    }
  }

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @Where(clause = "data_type='" + XhBienBanLayMauHdr.TABLE_NAME + "_ANH_CHUP'")
  private List<FileDinhKemJoinTable> anhChupMauNiemPhong = new ArrayList<>();

  public void setAnhChupMauNiemPhong(List<FileDinhKemJoinTable> fileDinhKem) {
    this.anhChupMauNiemPhong.clear();
    if (!DataUtils.isNullObject(fileDinhKem)) {
      fileDinhKem.forEach(s -> {
        s.setDataType(XhBienBanLayMauHdr.TABLE_NAME + "_ANH_CHUP");
        s.setXhBienBanLayMauHdr(this);
      });
      this.anhChupMauNiemPhong.addAll(fileDinhKem);
    }
  }

  @OneToMany(mappedBy = "xhBienBanLayMauHdr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<XhBienBanLayMauDtl> xhBienBanLayMauDtl = new ArrayList<>();

  public void setXhBienBanLayMauDtl(List<XhBienBanLayMauDtl> xhBienBanLayMauDtl) {
    this.getXhBienBanLayMauDtl().clear();
    if (!DataUtils.isNullOrEmpty(xhBienBanLayMauDtl)) {
      xhBienBanLayMauDtl.forEach(s -> {
        s.setXhBienBanLayMauHdr(this);
      });
      this.xhBienBanLayMauDtl.addAll(xhBienBanLayMauDtl);
    }
  }
}
