package com.tcdt.qlnvhang.request.xuathang;

import com.tcdt.qlnvhang.table.xuathang.hosokythuat.XhHoSoKyThuatDtl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class XhHoSoKyThuatReq {
  private Long id;
  private String soHskt;
  private Long idHsktNh;
  private Long soHsktNh;
  private Long idBbLayMau;
  private String soBbLayMau;
  private String soBbLayMauNh;
  private String soQdGiaoNvNh;
  private LocalDate ngayTaoHskt;
  private LocalDate ngayDuyetHskt;
  private String maDvi;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String lyDo;
  private String trangThai;
  private String type;
  private List<XhHoSoKyThuatDtl> xhHoSoKyThuatDtl = new ArrayList<>();
  private Map<String, String> mapVthh;
  private String tenLoaiVthh;
  private String tenCloaiVthh;
  private String tenTrangThai;
  private Map<String, String> mapDmucDvi;
  private String tenDvi;
  private String tenCuc;
  private String tenChiCuc;
  private String tenDiemKho;
  private String tenNhaKho;
  private String tenNganKho;
  private String tenLoKho;
}
