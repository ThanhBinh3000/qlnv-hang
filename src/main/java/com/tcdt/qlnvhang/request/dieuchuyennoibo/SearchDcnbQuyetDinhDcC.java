package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDcnbQuyetDinhDcC extends BaseRequest {
  private Integer nam;
  private String maDvi;
  private String loaiDc;
  private LocalDate ngayHieuLucTu;
  private LocalDate ngayHieuLucDen;
  private LocalDate ngayDuyetTcTu;
  private LocalDate ngayDuyetTcDen;
  private LocalDate ngayKyQdinhTu;
  private LocalDate ngayKyQdinhDen;
  private String soQdinh;
  private String trichYeu;
  private String trangThai;
  private String type;
  private List<String> types;
  private List<String> loaiVthh;
  private String loaiQdinh;
  private Boolean isVatTu;
  private Boolean thayDoiThuKho;
  private List<String> dsLoaiHang = new ArrayList<>();
}
