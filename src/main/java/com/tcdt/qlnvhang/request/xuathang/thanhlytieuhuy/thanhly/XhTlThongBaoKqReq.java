package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XhTlThongBaoKqReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soThongBao;
  private LocalDate ngayThongBao;
  private Long idHoSo;
  private String soHoSo;
  private LocalDate ngayTrinhDuyet;
  private LocalDate ngayThamDinh;
  private String noiDung;
  private String lyDo;
  private String trangThai;
  private String trangThaiTb;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  private List<FileDinhKemReq> fileDinhKem;
}
