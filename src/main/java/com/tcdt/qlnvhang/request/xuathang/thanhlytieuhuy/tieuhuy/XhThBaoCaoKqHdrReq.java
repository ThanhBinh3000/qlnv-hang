package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThBaoCaoKqDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhThBaoCaoKqHdrReq {
  private Long id;
  private Integer nam;
  private String soBaoCao;
  private LocalDate ngayBaoCao;
  private Long idQd;
  private String soQd;
  private LocalDate ngayQd;
  private String noiDung;
  private String trangThai;

  private String lyDoTuChoi;

  private List<FileDinhKemReq> fileDinhKemReq;
  private List<XhThBaoCaoKqDtl> children = new ArrayList<>();
}
