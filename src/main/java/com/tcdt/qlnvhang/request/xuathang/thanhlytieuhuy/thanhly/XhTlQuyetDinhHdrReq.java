package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlQuyetDinhHdrReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private Long idHoSo;
  private String soHoSo;
  private Long idKq;
  private String soKq;
  private LocalDate thoiGianTlTu;
  private LocalDate thoiGianTlDen;
  private String trichYeu;
  private String trangThai;
  private BigDecimal tongSoLuongTl;
  private BigDecimal tongSoLuongCon;
  private BigDecimal tongThanhTien;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;

  private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem;
  private List<FileDKemJoinHoSoKyThuatDtl> canCu = new ArrayList<>();
  private List<XhTlQuyetDinhDtl> quyetDinhDtl = new ArrayList<>();
}
