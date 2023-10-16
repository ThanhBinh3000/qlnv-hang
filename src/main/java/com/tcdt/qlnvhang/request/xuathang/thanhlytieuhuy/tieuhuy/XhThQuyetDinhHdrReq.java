package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhThQuyetDinhHdrReq {
  private Long id;
  private Integer nam;
  private String soQd;
  private String trichYeu;
  private LocalDate ngayKy;
  private Long idHoSo;
  private String soHoSo;
  private String trangThai;
  private String lyDoTuChoi;

  private List<FileDinhKemReq> fileDinhKemReq;
  private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();
}
