package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdCuuTro;
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
@Table(name = XhQdCuuTroHdr.TABLE_NAME)
@Data
public class XhQdCuuTroHdr extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_CUU_TRO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdCuuTroHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdCuuTroHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhQdCuuTroHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String soQd;
  private String maDvi;
  private String maDviDxuat;
  private Long idTongHop;
  private String maTongHop;
  private LocalDate ngayTongHop;
  private int nam;
  private LocalDate ngayKy;
  private String loaiVthh;
  private String cloaiVthh;
  private Long tongSoLuong;
  private String trangThai;
  private String trichYeu;
  private String lyDoTuChoi;


  @Transient
  private FileDinhKem fileDinhKem;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();
  @Transient
  private String tenDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<XhDxCuuTroDtl> thongTinDeXuat = new ArrayList<>();
  @Transient
  private List<XhThCuuTroDtl> thongTinTongHop = new ArrayList<>();
  @Transient
  private List<XhQdCuuTroDtl> thongTinQuyetDinh = new ArrayList<>();

  public String getTenTrangThai() {
    return TrangThaiAllEnum.getLabelById(trangThai);
  }

}
