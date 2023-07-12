package com.tcdt.qlnvhang.request.xuathang.xuatkhac;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkTongHopRequest extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDanhSach;
  private String tenDanhSach;
  private LocalDateTime ngayDeXuat;
  private String trangThai;
  private String trangThaiKtCl;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private BigDecimal tongSlHienTai;
  private Integer capTh;
  private String dvql;
  private String maCuc;
  private String maChiCuc;
  private String loai;
  private LocalDateTime ngayTaoTu;
  private LocalDateTime ngayTaoDen;
  private List<XhXkTongHopDtl> tongHopDtl = new ArrayList<>();
  private List<Long> listId;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
}
