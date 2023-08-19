package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdHdrReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idTongHop;
  private String maTongHop;
  private LocalDate ngayThop;
  private Long idDx;
  private String soDx;
  private LocalDate ngayDx;
  private Long tongSoLuongDx;
  private Long tongSoLuong;
  private Long thanhTien;
  private Long soLuongXuaCap;
  private String loaiVthh;
  private String cloaiVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private String mucDichXuat;
  private String trichYeu;
  private String trangThai;
  private String lyDoTuChoi;
  private String type;
  private Boolean xuatCap;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String donViTinh;

  private Long idQdGiaoNv;
  private String soQdGiaoNv;

  private List<FileDinhKemReq> fileDinhKem;

  private List<FileDinhKemReq> canCu = new ArrayList<>();

  private List<XhCtvtQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();
}

