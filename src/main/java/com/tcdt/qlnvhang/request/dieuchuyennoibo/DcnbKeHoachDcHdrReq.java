package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhuongAnDc;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DcnbKeHoachDcHdrReq {
  private Long id;
  private String loaiDc;
  private Integer nam;
  private String soDxuat;
  private LocalDate ngayLapKh;
  private LocalDate ngayDuyetLdc;
  private String trichYeu;
  private String lyDoDc;
  private String maDvi;
  private String maCucNhan;
  private String trachNhiemDviTh;
  private String trangThai;
  private String lyDoTuChoi;
  private String type;
  private Long idThop;
  private String maThop;
  private Long idQdDc;
  private String soQdDc;

  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<DcnbKeHoachDcDtl> dcNbKeHoachDcDtl = new ArrayList<>();
  private List<DcnbPhuongAnDc> dcnbPhuongAnDc = new ArrayList<>();
}
