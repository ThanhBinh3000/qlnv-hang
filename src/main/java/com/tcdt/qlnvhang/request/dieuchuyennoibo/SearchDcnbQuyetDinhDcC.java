package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchDcnbQuyetDinhDcC extends BaseRequest {
  private Integer nam;
  private String maDvi;
  private String loaiDc;
  private LocalDate ngayHieuLucTu;
  private LocalDate ngayHieuLucDen;
  private LocalDate ngayDuyetTcTu;
  private LocalDate ngayDuyetTcDen;
  private String soQdinh;
  private String trichYeu;
  private String trangThai;
  private String type;
  private List<String> loaiVthh;
}
