package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiDtlDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbBienBanHaoDoiHdrPreview {
    private String tenDvi;
    private String maQhns;
    private String soBienBan;
    private String tenCloaiVthh;
    private String tenLoaiVthh;
    private String tenNganKho;
    private String tenLoKho;
    private String soBbTinhKho;
    private String ngayLap;
    private String ngayLapBienBanTinhKho;
    private String chiCuc;
    private String lanhDaoChiCuc;
    private String keToan;
    private String ktvBaoQuan;
    private String thuKho;
    private Double tongSlXuatTheoQd;
    private String donViTinh;
    private Double tongSlXuatTheoTt;
    private String ngayKetThucXuatQd;
    private Double slHaoTt;
    private BigDecimal slHaoDuocThanhLy;
    private Double slHaoVuotDm;
    private Double tiLeHaoVuotDm;
    private BigDecimal slHaoDuoiDm;
    private BigDecimal tiLeHaoDuoiDm;
    private String nguyenNhan;
    private String kienNghi;
    private List<DcnbBienBanHaoDoiDtlDto> dcnbBienBanHaoDoiDtlDto;

}
