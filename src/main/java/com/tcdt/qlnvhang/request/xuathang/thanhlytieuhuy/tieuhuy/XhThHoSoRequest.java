package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoDtl;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhThHoSoRequest extends BaseRequest {
  private Long id;
  private Integer nam;
  private String soHoSo;
  private LocalDate ngayTaoHs;
  private Long idDanhSach;
  private String maDanhSach;
  private LocalDate thoiGianThTu;
  private LocalDate thoiGianThDen;
  private LocalDate thoiGianPdTu;
  private LocalDate thoiGianPdDen;
  private String trangThai;
  private String trangThaiTc;

  private String trichYeu;
  private List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();
  private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();

  private List<XhThHoSoDtl> children = new ArrayList<>();

  private String ketQua;

  private String maDviSr;

  private String soQdSr;

}
