package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgKetQua.TABLE_NAME)
public class XhTcTtinBdgKetQua extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_KET_QUA";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_KET_QUA_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_KET_QUA_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_KET_QUA_SEQ")
  private Long id;
  private Long idTtin;
  private String maDvi;
  private String soBb;
  private LocalDate ngayKy;
  private String trichYeu;
  private String trangThai;

  @Transient
  private String tenDvi;
  @Transient
  private FileDinhKem fileDinhKem;
  @Transient
  List<XhTcTtinBdgNlq> listNguoiLienQuan = new ArrayList<>();
  @Transient
  List<XhTcTtinBdgTaiSan> listTaiSan = new ArrayList<>();
}
