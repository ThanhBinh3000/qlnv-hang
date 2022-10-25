package com.tcdt.qlnvhang.request.xuatcuutro;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import com.tcdt.qlnvhang.table.XhQdCuuTroDtl;
import com.tcdt.qlnvhang.table.XhThCuuTroDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdCuuTroHdrSearchReq extends BaseRequest {
  private Long id;
  private String loaiNhapXuat;
  private Long idDxuat;
  private String soDxuat;
  private Long idTongHop;
  private String maTongHop;
  private String soQd;
  private String maDvi;
  private String maDviDxuat;
  private LocalDate ngayDxuat;
  private LocalDate tuNgayDxuat;
  private LocalDate denNgayDxuat;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long tongSoLuong;
  private String trichYeu;
  private String trangThai;
  private String trangThaiTh;
  private String loaiHinhNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiGianThucHien;
  private LocalDate tuThoiGianThucHien;
  private LocalDate denThoiGianThucHien;
  private LocalDate ngayTongHop;
  private LocalDate tuNgayTongHop;
  private LocalDate denNgayTongHop;
  private LocalDate ngayKy;
  private LocalDate tuNgayKy;
  private LocalDate denNgayKy;
  private String noiDung;
  private int nam;
  private String dvql;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private FileDinhKemReq fileDinhKem;
  private List<XhDxCuuTroHdr> thongTinDeXuat = new ArrayList<>();
  private List<XhThCuuTroDtl> thongTinTongHop = new ArrayList<>();
  private List<XhQdCuuTroDtl> thongTinQuyetDinh = new ArrayList<>();
}
