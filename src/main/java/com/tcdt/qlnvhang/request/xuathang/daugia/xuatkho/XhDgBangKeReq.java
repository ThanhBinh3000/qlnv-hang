package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgBangKeReq extends BaseRequest {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soBangKe;
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
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private LocalDate ngayXuat;
  private String diaDiemKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String nlqHoTen;
  private String nlqCmnd;
  private String nlqDonVi;
  private String nlqDiaChi;
  private LocalDate thoiGianGiaoNhan;
  private Long tongTrongLuong;
  private Long tongTrongLuongBaoBi;
  private Long tongTrongLuongHang;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String trangThai;
  private String type;
  private List<XhDgBangKeDtl> bangKeDtl= new ArrayList<>();
}
