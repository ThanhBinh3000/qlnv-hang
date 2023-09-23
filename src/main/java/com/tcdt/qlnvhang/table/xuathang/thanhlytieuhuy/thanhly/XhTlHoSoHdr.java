package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTlHoSoHdr.TABLE_NAME)
@Getter
@Setter
public class XhTlHoSoHdr extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_HO_SO_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHoSoHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlHoSoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlHoSoHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soHoSo;
  private Long idDanhSach;
  private String maDanhSach;
  private Long idQd;
  private String soQd;
  private Long idTb;
  private String soTb;
  private LocalDate ngayTaoHs;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trangThai;
  private LocalDate ngayDuyetLan1;
  private LocalDate ngayDuyetLan2;
  private LocalDate ngayDuyetLan3;
  private LocalDate ketQua;
  private String trichYeu;
  @Transient
  private List<FileDinhKem> fileDinhKem =new ArrayList<>();;
  @Transient
  private List<FileDinhKem> fileCanCu = new ArrayList<>();

  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;

  private Long idLdc;

  private LocalDate ngayDuyetLdc;

  private Long idLdv;

  private LocalDate ngayDuyetLdv;

  private Long idLdtc;

  private LocalDate ngayDuyetLdtc;

  @Transient
  private String tenTrangThai;

  @Transient
  private String tenDvi;

  @Transient
  private String maDvql;
  @Transient
  private String tenDvql;

  @Transient
  private List<XhTlHoSoDtl> children = new ArrayList<>();

  public String getTenTrangThai(){
    return TrangThaiAllEnum.getLabelById(getTrangThai());
  }

}
