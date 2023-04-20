package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhuongAnDc;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbKeHoachDcHdrReq {
  private Long id;
  private Long parentId;
  private String loaiDc;
  private String tenLoaiDc;
  private String type;
  private Integer nam;
  private String soDxuat;
  private LocalDate ngayLapKh;
  private LocalDate ngayDuyetLdcc;
  private Long nguoiDuyetLdccId;
  private String trichYeu;
  private String lyDoDc;
  private String maDvi;
  private String maDviPq;
  private String tenDvi;
  private String maCucNhan;
  private String tenCucNhan;
  private String trachNhiemDviTh;
  private String trangThai;
  private String lyDoTuChoi;
  private Long idThop;
  private String maThop;
  private Long idQdDc;
  private String soQdDc;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private String maDviCuc;
  private String tenDviCuc;
  private String tenTrangThai;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<DcnbKeHoachDcDtl> danhSachHangHoa = new ArrayList<>();
  private List<DcnbPhuongAnDc> phuongAnDieuChuyen = new ArrayList<>();
}
