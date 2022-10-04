package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhThCuuTroHdr.TABLE_NAME)
@Data
public class XhThCuuTroHdr extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_CUU_TRO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThCuuTroHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThCuuTroHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhThCuuTroHdr.TABLE_NAME + "_SEQ")
  private Long id;
//  private String loaiNhapXuat;
//  private String soDxuat;
  private String maDvi;
  private String maTongHop;
  private int nam;
  private LocalDate ngayTongHop;
  private String loaiVthh;
  private String cloaiVthh;
//  private String tenVthh;
  private Long tongSoLuong;
//  private String trichYeu;
  private String trangThai;
//  private String trangThaiTh;
  private String loaiHinhNhapXuat;
//  private String kieuNhapXuat;
//  private LocalDate thoiGianThucHien;
  private String noiDung;
  private Long idQuyetDinh;


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
  private String tenLoaiHinhNhapXuat;
  @Transient
  private List<XhDxCuuTroHdr> thongTinDeXuat = new ArrayList<>();
  @Transient
  private List<XhThCuuTroDtl> thongTinTongHop = new ArrayList<>();

  public String getTenTrangThai() {
    return TrangThaiAllEnum.getLabelById(trangThai);
  }

}
