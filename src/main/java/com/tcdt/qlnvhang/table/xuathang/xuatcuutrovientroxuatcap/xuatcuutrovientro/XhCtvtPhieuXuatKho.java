package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhCtvtPhieuXuatKho.TABLE_NAME)
@Data
public class XhCtvtPhieuXuatKho extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_PHIEU_XUAT_KHO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtPhieuXuatKho.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtPhieuXuatKho.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtPhieuXuatKho.TABLE_NAME + "_SEQ")
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
  private Long idDtlQdGiaoNvXh;
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
  private Long idBangKeCh;
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
  private String mucDichXuat;
  private String noiDungDx;
  private BigDecimal soLuong;
  private String soLuongBc;

  private String type;
  @Transient
  private String tenDvi;
  @Transient
  private String diaChiDvi;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenTrangThai;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private List<FileDinhKem> fileDinhKems =new ArrayList<>();

}
