package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhTlQuyetDinh extends BaseRequest {
  private String dvql;
  private Integer nam;
  private String soQd;
  private String soHoSo;
  private LocalDate ngayKyTu;
  private LocalDate ngayKyDen;
}
