package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhXcapQuyetDinhPdDtl.TABLE_NAME)
@Data
public class XhXcapQuyetDinhPdDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XCAP_QUYET_DINH_PD_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXcapQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXcapQuyetDinhPdDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXcapQuyetDinhPdDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDx;
  private String soDx;
  private String maDviDx;
  private LocalDate ngayPduyetDx;
  private String trichYeuDx;
  private BigDecimal tongSoLuongDx;
  private BigDecimal soLuongXuatCap;

  @Transient
  private String tenDviDx;

  @OneToMany(mappedBy = "quyetDinhPdDtl", cascade = CascadeType.ALL)
  private List<XhXcapQuyetDinhPdDx> quyetDinhPdDx = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhXcapQuyetDinhPdHdr quyetDinhPdHdr;
}
