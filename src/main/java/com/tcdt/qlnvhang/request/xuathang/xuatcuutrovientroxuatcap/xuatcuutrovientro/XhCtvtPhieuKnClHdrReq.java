package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtPhieuKnClHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String maQhNs;
  private String soPhieu;
  private LocalDate ngayLapPhieu;
  private LocalDate ngayKnMau;
  private Long idBienBan;
  private String soBienBan;
  private LocalDate ngayLayMau;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String nguoiKn;
  private String truongPhong;
  private String thuKho;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private String hinhThucBq;
  private String noiDung;
  private String ketLuan;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String type;
  private String soBbTinhKho;
  private String soBbXuatDocKho;
  private LocalDate ngayXuatDocKho;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
  private List<XhCtvtPhieuKnClDtlReq> ketQuaPhanTich= new ArrayList<>();
}
