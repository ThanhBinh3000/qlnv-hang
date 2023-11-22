package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtPhieuXuatKhoReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieuXuatKho;
  private LocalDate ngayTaoPhieu;
  private LocalDate ngayXuatKho;
  private BigDecimal taiKhoanNo;
  private BigDecimal taiKhoanCo;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Long idPhieuKnCl;
  private String soPhieuKnCl;
  private LocalDate ngayKn;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String canBoLapPhieu;
  private String ldChiCuc;
  private String ktvBaoQuan;
  private String keToanTruong;
  private String nguoiGiaoHang;
  private String soCmt;
  private String ctyNguoiGh;
  private String diaChi;
  private LocalDate thoiGianGiaoNhan;
  private String soBangKeCh;
  private String maSo;
  private String donViTinh;
  private String theoChungTu;
  private BigDecimal thucXuat;
  private BigDecimal donGia;
  private BigDecimal thanhTien;
  private String thanhTienBc;
  private String ghiChu;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String mucDichXuat;
  private String noiDungDx;
  private BigDecimal soLuong;
  private String soLuongBc;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
}
