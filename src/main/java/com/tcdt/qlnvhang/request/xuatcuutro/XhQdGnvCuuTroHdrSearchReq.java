package com.tcdt.qlnvhang.request.xuatcuutro;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import com.tcdt.qlnvhang.table.XhDxCuuTroKho;
import com.tcdt.qlnvhang.table.XhQdGnvCuuTroDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdGnvCuuTroHdrSearchReq extends BaseRequest {
  private Long id;
  private Long idQdPd;
  private String loaiNhapXuat;
  private String soDxuat;
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
  private Long soLuong;
  private String trichYeu;
  private String trangThai;
  private List<String> listTrangThai;
  private String trangThaiTh;
  private List<String> listTrangThaiTh;
  private String loaiHinhNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiGianThucHien;
  private LocalDate tuThoiGianThucHien;
  private LocalDate denThoiGianThucHien;
  private String noiDung;
  private int nam;
  private String dvql;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
  private List<XhQdGnvCuuTroDtl> noiDungCuuTro = new ArrayList<>();
  private List<Long> ids;
}
