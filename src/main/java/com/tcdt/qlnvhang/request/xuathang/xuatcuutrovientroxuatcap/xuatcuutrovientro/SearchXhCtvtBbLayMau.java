package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhCtvtBbLayMau extends BaseRequest {
  private String maDvi;
  private String dvql;
  private String soBienBan;
  private String soQdGiaoNvXh;
  private String dviKiemNghiem;
  private LocalDate ngayLayMauTu;
  private LocalDate ngayLayMauDen;
  private String type;
  private String trangThai;
}
