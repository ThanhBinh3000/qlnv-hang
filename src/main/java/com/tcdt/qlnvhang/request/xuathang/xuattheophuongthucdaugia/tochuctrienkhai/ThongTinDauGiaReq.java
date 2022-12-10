package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import com.tcdt.qlnvhang.table.XhDxCuuTroKho;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgKetQua;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgThongBao;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ThongTinDauGiaReq extends BaseRequest {
  private Long id;
  private int nam;
  private String maDvi;
  private String dvql;
  private String loaiVthh;
  private String cloaiVthh;
  private Long idQdPdKh;
  private String soQdPdKh;
  private Long idQdDcKh;
  private String soQdDcKh;
  private Long idQdPdKq;
  private String soQdPdKq;
  private Long idKhDx;
  private String soKhDx;
  private LocalDate ngayQdPdKqBdg;
  private int thoiHanGiaoNhan;
  private int thoiHanThanhToan;
  private String phuongThucThanhToan;
  private String phuongThucGiaoNhan;
  private String trangThai;
  private String maDviThucHien;
  private Long tongTienGiaKhoiDiem;
  private Long tongTienDatTruoc;
  private Long tongTienDatTruocDuocDuyet;
  private Long tongSoLuong;
  private int phanTramDatTruoc;
  private LocalDate thoiGianToChucTu;
  private LocalDate thoiGianToChucDen;
  private List<ThongTinDauGiaDtlReq> detail = new ArrayList<>();
  private List<Long> ids;
}
