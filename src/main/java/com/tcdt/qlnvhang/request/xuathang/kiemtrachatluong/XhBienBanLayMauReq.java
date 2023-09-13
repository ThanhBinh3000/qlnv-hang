package com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.bienbanlaymau.XhBienBanLayMauDtl;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XhBienBanLayMauReq {
  private Long id;
  private Integer nam;
  private String maDvi;
  private String soBbQd;
  private String maDiaDiem;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private String lyDo;
  private String trangThai;
  private String tenLoaiVthh;
  private String tenCloaiVthh;
  private String tenTrangThai;
  private String tenDvi;
  private String tenCuc;
  private String tenChiCuc;
  private String tenDiemKho;
  private String tenNhaKho;
  private String tenNganKho;
  private String tenLoKho;
  private String maQhns;
  private Long idQdGnv;
  private String soQdGnv;
  private Long idHopDong;
  private String soHopDong;
  private Long idBangKe;
  private String soBangKe;
  private LocalDate ngayKy;
  private LocalDate ngayKyQdGnv;
  private String ktvBaoQuan;
  private String dviKiemNghiem;
  private String diaDiemLayMau;
  private Long soLuongMau;
  private String niemPhong;
  private String loaiBb;
  private String type;
  private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
  private List<FileDinhKemJoinTable> canCu = new ArrayList<>();
  private List<XhBienBanLayMauDtl> xhBienBanLayMauDtl = new ArrayList<>();

}
