package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = XhXkLtBaoCaoKq.TABLE_NAME)
public class XhXkLtBaoCaoKq extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_LT_BAO_CAO_KQ";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkLtBaoCaoKq.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkLtBaoCaoKq.TABLE_NAME + "_SEQ",
      allocationSize = 1, name = XhXkLtBaoCaoKq.TABLE_NAME + "_SEQ")
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDviNhan;
  private String soBaoCao;
  private String tenBaoCao;
  private LocalDate ngayBaoCao;
  private String idTongHop;
  private String maDanhSach;
  private String tenDanhSach;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  @Transient
  private String tenDvi;
  @Transient
  private String tenDviNhan;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<Long> listIdTongHop = new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @Transient
  private List<FileDinhKem> fileDinhKemsBc =new ArrayList<>();
}
