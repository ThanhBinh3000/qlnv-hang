package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhThTongHopRequest extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDate thoiGianThTu;
  private LocalDate thoiGianThDen;
  private String trangThai;

  private LocalDateTime ngayTaoTu;
  private LocalDateTime ngayTaoDen;

  private String maDviSr;
}
