package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtPhieuXuatKhoRequest  extends BaseRequest {
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String soPhieu;
    private String loai;
    private String maQhns;
    private LocalDate ngayXuat;
    private LocalDate thoiGianGiaoHang;
    private BigDecimal duNo;
    private BigDecimal duCo;
    private String soCanCu;
    private Long idCanCu;
    private String maDiaDiem; // mã địa điểm (mã lô/ngăn kho)
    private String soPhieuKncl;
    private Long idPhieuKncl;
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
    private BigDecimal slThucXuat;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String maSo;
    private BigDecimal slTonKho;
    private BigDecimal slLayMau;
    private String donViTinh;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    //search params
    private LocalDate ngayXuatTu;
    private LocalDate ngayXuatDen;
    private String dvql;
}
