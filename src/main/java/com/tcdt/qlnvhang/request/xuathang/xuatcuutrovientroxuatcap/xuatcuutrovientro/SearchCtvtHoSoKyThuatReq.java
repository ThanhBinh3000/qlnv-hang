package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

@Data
public class SearchCtvtHoSoKyThuatReq extends BaseRequest {

  private Long id;

  private String dvql;

  private Integer nam;

  private Long idQdGiaoNvNh;

  private String soQdGiaoNvNh;

  private String soBbLayMau;

  private String soHd;

  private String maDvi;

  private String tenDvi;

  private String soHoSoKyThuat;

  private Integer idBbLayMauXuat;

  private Boolean kqKiemTra;

}
