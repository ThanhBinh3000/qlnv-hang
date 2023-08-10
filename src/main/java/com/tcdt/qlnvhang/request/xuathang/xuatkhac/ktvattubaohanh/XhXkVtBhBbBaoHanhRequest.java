package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhBbBaoHanhRequest extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soBienBan;
  private LocalDate ngayLapBb;
  private String soCanCu;
  private Long idCanCu;
  private Integer soLanLm;
  private String soPhieuKdcl;
  private Long idPhieuKdcl;
  private LocalDate ngayKdcl;
  private String maDiaDiem; // mã địa điểm (mã lô/ngăn kho)
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal slTonKho;
  private BigDecimal slBaoHanh;
  private LocalDate thoiGianBh;
  private String canBoLapBb;
  private String noiDung;
  private String lyDo;
  private String trangThai;
  private String lyDoTuChoi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String trangThaiBh;

  private Long idQdGnvNh;
  private String soQdGnvNh;
  private Long idPhieuKtcl;
  private String soPhieuKtcl;
  private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

  private Long idBbLayMauL1;
  private LocalDate ngayLayMauL1;
  private String soBbLayMauL1;
  private Long idBbLayMauL2;
  private LocalDate ngayLayMauL2;
  private String soBbLayMauL2;
  //search params
  private LocalDate ngayKdclTu;
  private LocalDate ngayKdclDen;
  private String dvql;
}
