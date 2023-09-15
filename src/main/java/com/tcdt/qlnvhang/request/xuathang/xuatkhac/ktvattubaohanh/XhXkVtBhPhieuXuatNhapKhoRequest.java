package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhPhieuXuatNhapKhoRequest extends BaseRequest {
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String soPhieu;
    private String loai;
    private String maQhns;
    private LocalDate ngayXuatNhap;
    private LocalDate thoiGianGiaoHang;
    private BigDecimal duNo;
    private BigDecimal duCo;
    private String soCanCu;
    private Long idCanCu;
    private LocalDate ngayKyCanCu;
    private Integer soLanLm;
    private String maDiaDiem; // mã địa điểm (mã lô/ngăn kho)
    private String soPhieuKdcl;
    private Long idPhieuKdcl;
    private LocalDate ngayKdcl;
    private String loaiVthh;
    private String cloaiVthh;
    private String canBoLapPhieu;
    private String ldChiCuc;
    private String ktvBaoQuan;
    private String keToanTruong;
    private String hoTenNgh;
    private String cccdNgh;
    private String donViNgh;
    private String diaChiNgh;
    private BigDecimal slThucTe;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String maSo;
    private BigDecimal slTonKho;
    private BigDecimal slLayMau;
    private String donViTinh;
    private Boolean mauBiHuy;
    private String loaiPhieu;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private Long idBbLayMau;
    private String soBbLayMau;
    private String soBcKqkdMau;
    private Long idBcKqkdMau;

    private Long idBienBanKt;
    private String soBienBanKt;

    //search params
    private LocalDate ngayXuatNhapTu;
    private LocalDate ngayXuatNhapDen;
    private String dvql;
    private List<Long> canCus = new ArrayList<>();
}
