package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtBbHaoDoiHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soBbHaoDoi;
  private LocalDate ngayTaoBb;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private Long idBbTinhKho;
  private String soBbTinhKho;
  private LocalDate ngayBatDauXuat;
  private LocalDate ngayKetThucXuat;
  private BigDecimal tongSlNhap;
  private LocalDate ngayKtNhap;
  private BigDecimal tongSlXuat;
  private BigDecimal slHaoThucTe;
  private String tiLeHaoThucTe;
  private BigDecimal slHaoThanhLy;
  private String tiLeHaoThanhLy;
  private BigDecimal slHaoVuotDm;
  private String tiLeHaoVuotDm;
  private BigDecimal slHaoDuoiDm;
  private String tiLeHaoDuoiDm;
  private BigDecimal dinhMucHaoHut;
  private BigDecimal sLHaoHutTheoDm;
  private String nguyenNhan;
  private String kienNghi;
  private String ghiChu;
  private String thuKho;
  private String ktvBaoQuan;
  private String keToan;
  private String ldChiCuc;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  private List<XhCtvtBbHaoDoiDtlReq> listPhieuXuatKho= new ArrayList<>();
}
