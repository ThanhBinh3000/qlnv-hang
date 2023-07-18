package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlQuyetDinhPdKqHdrReq extends BaseRequest {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private String trichYeu;
  private LocalDate ngayKy;
  private LocalDate ngayHieuLuc;
  private String loaiHinhNhapXuat;
  private String kieuNhapXuat;
  private Long idThongBao;
  private String maThongBao;
  private String loaiVthh;
  private String cloaiVthh;
  private String trangThai;
  private String lyDoTuChoi;
  private Long nguoiKyQdId;
  private LocalDate ngayKyQd;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private List<XhTlQuyetDinhPdKqDtl> quyetDinhDtl = new ArrayList<>();
  private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem;
  private List<FileDKemJoinHoSoKyThuatDtl> canCu;
  private String dvql;
}
