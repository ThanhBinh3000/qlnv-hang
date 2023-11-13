package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XhThThongBaoKqReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soThongBao;
  private LocalDate ngayThongBao;
  private Long idQd;
  private String soQd;
  private Long idHoSo;
  private String soHoSo;
  private String trichYeu;
  private LocalDate ngayKy;
  private String lyDo;
  private String trangThai;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  private List<FileDinhKemReq> fileDinhKemReq;
}
