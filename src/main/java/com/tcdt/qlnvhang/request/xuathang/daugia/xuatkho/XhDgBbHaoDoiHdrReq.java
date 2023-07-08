package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgBbHaoDoiHdrReq {
  private Long id;
  private Integer nam;
  private String maQhns;
  private String soBbHaoDoi;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private Long idHdong;
  private String soHdong;
  private LocalDate ngayKyHd;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private Long idBbTinhKho;
  private String soBbTinhKho;
  private BigDecimal tongSlNhap;
  private LocalDate ngayKtNhap;
  private BigDecimal tongSlXuat;
  private LocalDate ngayKtXuat;
  private BigDecimal slHaoThucTe;
  private String tiLeHaoThucTe;
  private BigDecimal slHaoVuotDm;
  private String tiLeHaoVuotDm;
  private BigDecimal slHaoThanhLy;
  private String tiLeHaoThanhLy;
  private BigDecimal slHaoDuoiDm;
  private String tiLeHaoDuoiDm;
  private BigDecimal dinhMucHaoHut;
  private String nguyenNhan;
  private String kienNghi;
  private String ghiChu;
  private Long idKtvBaoQuan;
  private LocalDate ngayPduyetKtvBq;
  private Long idKeToan;
  private LocalDate ngayPduyetKt;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String donViTinh;
  private String trangThai;
  private String lyDoTuChoi;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  private List<XhDgBbHaoDoiDtlReq> listPhieuXuatKho= new ArrayList<>();
}
