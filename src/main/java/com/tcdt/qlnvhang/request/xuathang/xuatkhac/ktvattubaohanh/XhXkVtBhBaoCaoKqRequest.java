package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKqDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhBaoCaoKqRequest extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDviNhan;
  private String soBaoCao;
  private String tenBaoCao;
  private LocalDate ngayBaoCao;
  private String soCanCu;
  private String idCanCu;

  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;

  private Long idPhieuKtcl;
  private String spPhieuKtcl;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;

  private List<FileDinhKemReq> fileDinhKems;
  private List<XhXkVtBhBaoCaoKqDtl> bhBaoCaoKqDtl = new ArrayList<>();

  //search params
  private LocalDate ngayBaoCaoTu;
  private LocalDate ngayBaoCaoDen;
  private String dvql;
}
