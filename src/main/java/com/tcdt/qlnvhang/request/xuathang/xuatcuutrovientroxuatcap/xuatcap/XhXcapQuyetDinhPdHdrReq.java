package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXcapQuyetDinhPdHdrReq {
  private Long id;
  private String maDvi;
  private Integer nam;
  private String soQd;
  private LocalDate ngayKy;
  private LocalDate ngayHluc;
  private Long idQdPd;
  private String soQdPd ;
  private Long idQdGiaoNv;
  private String soQdGiaoNv;
  private LocalDate ngayHlucQdPd;
  private String trichYeu;
  private String trangThai;

  private String loaiVthh;
  private String loaiNhapXuat;
  private String kieuNhapXuat;
  private LocalDate thoiHanXuat;
  private BigDecimal tongSoLuongThoc;
  private BigDecimal tongSoLuongGao;
  private BigDecimal thanhTien;

  private LocalDate ngayPduyet;
  private Long nguoiPduyetId;
  private LocalDate ngayGduyet;
  private Long nguoiGduyetId;
  private String lyDoTuChoi;


  private List<FileDinhKemReq> fileDinhKem;
  private List<FileDinhKemReq> canCu = new ArrayList<>();
  private List<XhXcapQuyetDinhPdDtl> quyetDinhPdDtl = new ArrayList<>();

}
