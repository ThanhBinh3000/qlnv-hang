package com.tcdt.qlnvhang.request.xuatcuutro;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.XhDxCuuTroDtl;
import com.tcdt.qlnvhang.table.XhDxCuuTroKho;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxCuuTroHdrSearchReq extends BaseRequest {
  private Long id;
  private String loaiNhapXuat;
  private String soDxuat;
  private String maDvi;
  private LocalDate ngayDxuat;
  private LocalDate tuNgayDxuat;
  private LocalDate denNgayDxuat;
  private String loaiVthh;
  private String cloaiVthh;
  private String tenVthh;
  private Long tongSoLuong;
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
  private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
  private List<XhDxCuuTroDtl> thongTinChiTiet = new ArrayList<>();
  private List<XhDxCuuTroKho> phuongAnXuat = new ArrayList<>();
  private List<Long> ids;
}
