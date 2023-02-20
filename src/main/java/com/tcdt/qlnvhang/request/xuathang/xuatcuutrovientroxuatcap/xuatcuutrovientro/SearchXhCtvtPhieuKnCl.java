package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhCtvtPhieuKnCl extends BaseRequest {
  private String maDvi;
  private String dvql;
  private Integer nam;
  private String soQdGiaoNvXh;
  private String soPhieu;
  private LocalDate ngayKnTu;
  private LocalDate ngayKnDen;
  private String soBienBan;
  private String soBbXuatDocKho;
  private String type;
  private String trangThai;
}