package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCDtl;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbQuyetDinhDcCHdrReq implements Serializable {
  private Long id;
  private String loaiDc;
  private String tenLoaiDc;
  private Integer nam;
  private String soQdinh;
  private LocalDate ngayKyQdinh;
  private LocalDate ngayDuyetTc;
  private Long nguoiDuyetTcId;
  private String trichYeu;
  private String maDvi;
  private String tenDvi;
  private String loaiQdinh;
  private String tenLoaiQdinh;
  private BigDecimal tongDuToanKp;
  private Long canCuQdTc;
  private String soCanCuQdTc;
  private Long dxuatId;
  private String soDxuat;
  private LocalDate ngayTrinhDuyetTc;
  private String trangThai;
  private String lyDoTuChoi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String tenTrangThai;
  private String type;
  @NotNull
  private LocalDate ngayHieuLuc;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<FileDinhKemReq> quyetDinh = new ArrayList<>();
  private List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = new ArrayList<>();
}
