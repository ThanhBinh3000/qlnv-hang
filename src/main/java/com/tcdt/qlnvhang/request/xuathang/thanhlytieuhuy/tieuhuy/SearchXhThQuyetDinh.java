package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhThQuyetDinh extends BaseRequest {
  private String dvql;
  private Integer nam;
  private String soQd;
  private String soHoSo;
  private LocalDate ngayKyTu;
  private LocalDate ngayKyDen;

  private String soBaoCao;
  private LocalDate ngayBaoCaoTu;
  private LocalDate ngayBaoCaoDen;

  private String soThongBao;
  private LocalDate ngayPduyetTu;
  private LocalDate ngayPduyetDen;
}
