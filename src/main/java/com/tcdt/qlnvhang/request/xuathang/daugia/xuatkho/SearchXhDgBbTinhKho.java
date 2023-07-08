package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhDgBbTinhKho extends BaseRequest {
  private String maDvi;
  private String dvql;
  private Integer nam;
  private String soQdGiaoNvXh;
  private String soBbTinhKho;
  private LocalDate ngayBatDauXuatTu;
  private LocalDate ngayBatDauXuatDen;
  private LocalDate ngayKetThucXuatTu;
  private LocalDate ngayKetThucXuatDen;
  private String type;
  private String trangThai;
  private String loaiVthh;
}