package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class XhKqBdgHdrReq extends BaseRequest {

  private Long id;

  private String soQdKq;

  private String trichYeu;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayHluc;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  @Column(columnDefinition = "Date")
  private Date ngayKy;

  private String maThongBao;

  private String soBienBan;

  private String pthucGnhan;

  private Integer tgianGnhan;

  private String tgianGnhanGhiChu;

  private String ghiChu;

  private Integer nam;

  private String maDvi;

  private String loaiVthh;

  // Transient
  @Transient
  private List<FileDinhKem> fileDinhKems = new ArrayList<>();

}
