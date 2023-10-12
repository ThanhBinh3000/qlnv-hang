package com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauDtl;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XhBienBanLayMauReq {
  private LocalDateTime ngayTao;
  private Long nguoiTaoId;
  private LocalDateTime ngaySua;
  private Long nguoiSuaId;
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soBbQd;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private String lyDoTuChoi;
  private String trangThai;
  private Long nguoiKyQdId;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String maQhns;
  private Long idQdGnv;
  private String soQdGnv;
  private LocalDate ngayKyQdGnv;
  private Long idHopDong;
  private String soHopDong;
  private Long idBangKe;
  private String soBangKe;
  private Long idBbTinhKho;
  private String soBbTinhKho;
  private LocalDate ngayXuatDocKho;
  private Long idBbHaoDoi;
  private String soBbHaoDoi;
  private LocalDate ngayKy;
  private String ktvBaoQuan;
  private String dviKiemNghiem;
  private String diaDiemLayMau;
  private Long soLuongMau;
  private String niemPhong;
  private String loaiBb;
  private String type;
  private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();
  private List<FileDinhKemJoinTable> anhChupMauNiemPhong = new ArrayList<>();
  private List<XhBienBanLayMauDtl> xhBienBanLayMauDtl = new ArrayList<>();

}
