package com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXkThXuatHangKdmHdr.TABLE_NAME)
@Getter
@Setter
public class XhXkThXuatHangKdmHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_TH_DS_XH_KDM_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkThXuatHangKdmHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkThXuatHangKdmHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkThXuatHangKdmHdr.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  // update số QĐ xuất hàng khỏi danh mục
  private Long idQdXhKdm;
  private String soQdXhKdm;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDvi;
  @Transient
  private String maDvql;
  @Transient
  private String tenDvql;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();
  @OneToMany(mappedBy = "tongHopHdr", cascade = CascadeType.ALL)
  private List<XhXkThXuatHangKdmDtl> tongHopDtl = new ArrayList<>();
}
