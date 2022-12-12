package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgNlq;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgTaiSan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThongTinDauGiaDtlReq extends BaseRequest {
  private Long id;
  private Long idTtinHdr;
  private String maDvi;
  //ket qua
  private String soBb;
  private LocalDate ngayKy;
  private String trichYeuKetQua;
  private String ketQua;

  //thong bao
  private String soTb;
  private String trichYeuThongBao;
  private String toChucTen;
  private String toChucDiaChi;
  private String toChucSdt;
  private String toChucStk;
  private String soHopDong;
  private LocalDate ngayKyHopDong;
  private String hinhThucToChuc;
  private LocalDate thoiHanDkTu;
  private LocalDate thoiHanDkDen;
  private String ghiChuThoiGianDk;
  private String ghiChuThoiGianXem;
  private String diaDiemNopHoSo;
  private String diaDiemXemTaiSan;
  private Long dieuKienDk;
  private Long buocGia;
  private LocalDate thoiHanXemTaiSanTu;
  private LocalDate thoiHanXemTaiSanDen;
  private LocalDate thoiHanNopTienTu;
  private LocalDate thoiHanNopTienDen;
  private String phuongThucThanhToan;
  private String huongThuTen;
  private String huongThuStk;
  private String huongThuNganHang;
  private String huongThuChiNhanh;
  private LocalDate thoiGianToChucTu;
  private LocalDate thoiGianToChucDen;
  private String diaDiemToChuc;
  private String hinhThucDauGia;
  private String phuongThucDauGia;
  private String ghiChu;
  private String tenDvi;
  private FileDinhKemReq fileDinhKem;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<XhTcTtinBdgNlq> listNguoiLienQuan = new ArrayList<>();
  private List<XhTcTtinBdgTaiSan> listTaiSan = new ArrayList<>();
}
