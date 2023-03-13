package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhDgBbHaoDoi extends BaseRequest {
  private String maDvi;
  private String dvql;
  private Long nam;
  private String soQdGiaoNvXh;
  private String soBbHaoDoi;
  private LocalDate ngayTaoBbTu;
  private LocalDate ngayTaoBbDen;
  private LocalDate ngayBatDauXuatTu;
  private LocalDate ngayBatDauXuatDen;
  private LocalDate ngayKetThucXuatTu;
  private LocalDate ngayKetThucXuatDen;
  private LocalDate ngayQdGiaoNvXhTu;
  private LocalDate ngayQdGiaoNvXhDen;
  private String type;
  private String trangThai;
  private String loaiVthh;
}
