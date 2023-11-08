package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchXhCtvtDeXuatHdrReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maDviDx;
  private String dvql;
  private String soDx;
  private LocalDate ngayDxTu;
  private LocalDate ngayDxDen;
  private LocalDate ngayKetThucTu;
  private LocalDate ngayKetThucDen;
  private String trangThai;
  private String type;
  private String mucDichXuat;

  private String tenVthh;
  private String loaiVthh;
  private String loaiNhapXuat;
  private List<String> trangThaiList = new ArrayList<>();
  private List<XhCtvtTongHopDtl> deXuatCuuTro = new ArrayList<>();
  private String maTongHop;

  //dung cho chuc nang list cua man hình Qdpd
  private List<Long> idQdPdList = new ArrayList<>();
  private Boolean idThopNull = false;
  private Boolean idQdPdNull = false;

}
