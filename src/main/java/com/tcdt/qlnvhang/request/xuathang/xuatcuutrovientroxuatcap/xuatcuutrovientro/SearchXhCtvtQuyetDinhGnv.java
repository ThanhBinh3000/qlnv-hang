package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchXhCtvtQuyetDinhGnv extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String dvql;
  private String soQd;
  private String loaiVthh;
  private String trichYeu;
  private LocalDate ngayKyTu;
  private LocalDate ngayKyDen;
  private String trangThai;
  private String trangThaiXh;
  private List<String> listTrangThaiXh = new ArrayList<>();
  private String type;
}
