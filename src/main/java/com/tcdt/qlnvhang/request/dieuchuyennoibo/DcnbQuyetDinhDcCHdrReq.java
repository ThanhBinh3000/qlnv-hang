package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCDtl;
import lombok.Data;

import java.io.Serializable;
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
  private String maThop;
  private Long idThop;
  private String maDxuat;
  private Long idDxuat;
  private String type; // loại TH (tổng hợp), DX (đề xuất)
  private String trangThai;
  private String lyDoTuChoi;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String tenTrangThai;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<FileDinhKemReq> quyetDinh = new ArrayList<>();
  private List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = new ArrayList<>();
}
