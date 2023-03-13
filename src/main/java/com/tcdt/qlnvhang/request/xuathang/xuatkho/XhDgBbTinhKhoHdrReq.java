package com.tcdt.qlnvhang.request.xuathang.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhDgBbTinhKhoDtl;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgBbTinhKhoHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soBbTinhKho;
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
  private LocalDate ngayBatDauXuat;
  private LocalDate ngayKetThucXuat;
  private BigDecimal tongSlNhap;
  private BigDecimal tongSlXuat;
  private BigDecimal slConLai;
  private BigDecimal slThucTeCon;
  private BigDecimal slThua;
  private BigDecimal slThieu;
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
  private List<XhDgBbTinhKhoDtl> listPhieuXuatKho= new ArrayList<>();
}
