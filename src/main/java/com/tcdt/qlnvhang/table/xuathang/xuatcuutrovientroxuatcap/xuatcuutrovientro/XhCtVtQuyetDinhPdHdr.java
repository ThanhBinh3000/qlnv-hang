package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtVtQuyetDinhPdHdr.TABLE_NAME)
@Data
public class XhCtVtQuyetDinhPdHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_QUYET_DINH_PD_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtVtQuyetDinhPdHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idTongHop;
  private String maTongHop;
  private LocalDate ngayThop;
  private Long idDx;
  private String soDx;
  private LocalDate ngayDx;
  private Long tongSoLuongDx;
  private Long tongSoLuong;
  private Long soLuongXuaCap;
  private String loaiVthh;
  private String cloaiVthh;
  private String loaiNhapXuat;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private boolean xuatCap;
  private String type;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;

  @Transient
  private List<FileDinhKem> fileDinhKem;
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

  @OneToMany(mappedBy = "xhCtVtQuyetDinhPdHdr", cascade = CascadeType.MERGE)
  private List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();
}
