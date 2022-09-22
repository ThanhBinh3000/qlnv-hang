package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;

@Entity
@Table(name = XhDxCuuTroHdr.TABLE_NAME)
@Data
public class XhDxCuuTroHdr extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DX_CUU_TRO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxCuuTroHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDxCuuTroHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDxCuuTroHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String loaiNhapXuat;
  private String soDxuat;
  private String maDvi;
  private LocalDate ngayDxuat;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long tongSoLuong;
  private String trichYeu;
  private String trangThai;
  private String trangThaiTh;
  private String loaiHinhNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiGianThucHien;
  private String noiDung;
  private int nam;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "dataId")
  @JsonManagedReference
  @Where(clause = "data_type='" + XhDxCuuTroHdr.TABLE_NAME + "'")
  private List<FileDKemJoinDxuatCuuTro> fileDinhKem = new ArrayList<>();

  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenTrangThaiTh;
  @Transient
  private List<XhDxCuuTroDtl> thongTinChiTiet = new ArrayList<>();
  @Transient
  private List<XhDxCuuTroKho> phuongAnXuat = new ArrayList<>();

  public String getTenTrangThai() {
    return TrangThaiAllEnum.getLabelById(trangThai);
  }

  public String getTenTrangThaiTh() {
    return TrangThaiAllEnum.getLabelById(trangThaiTh);
  }

  public void setFileDinhKem(List<FileDKemJoinDxuatCuuTro> children2) {
    this.fileDinhKem.clear();
    for (FileDKemJoinDxuatCuuTro child2 : children2) {
      child2.setParent(this);
    }
    this.fileDinhKem.addAll(children2);
  }
}
