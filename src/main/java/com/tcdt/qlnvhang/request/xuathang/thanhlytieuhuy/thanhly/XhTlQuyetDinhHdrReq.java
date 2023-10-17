package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlQuyetDinhHdrReq {
  private Long id;
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

  private String lyDoTuChoi;

  private List<FileDinhKemReq> fileDinhKemReq =new ArrayList<>();

  private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();
}
