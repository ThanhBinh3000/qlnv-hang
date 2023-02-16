package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhCtvtBbLayMauHdrReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String loaiBienBan;
  private String maQhNs;
  private Long idQdGiaoNvXh;
  private String soQdGiaoNvXh;
  private LocalDate ngayQdGiaoNvXh;
  private String ktvBaoQuan;
  private String soBienBan;
  private Date ngayLayMau;
  private String dviKiemNghiem;
  private String diaDiemLayMau;
  private String loaiVthh;
  private String cloaiVthh;
  private String moTaHangHoa;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Integer soLuongMau;
  private String ppLayMau;
  private String chiTieuKiemTra;
  private Boolean ketQuaNiemPhong;
  private String trangThai;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String lyDoTuChoi;
  private String soBbHaoDoi;
  private String soBbTinhKho;
  private LocalDate ngayXuatDocKho;
  private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();

  private List<FileDinhKemReq> fileDinhKemNiemPhong =new ArrayList<>();

  private List<XhCtvtBbLayMauDtlReq> nguoiLienQuan = new ArrayList<>();
}
