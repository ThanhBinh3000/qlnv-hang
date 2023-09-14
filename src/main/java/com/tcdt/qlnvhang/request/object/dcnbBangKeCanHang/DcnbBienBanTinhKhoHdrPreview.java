package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbBienBanTinhKhoHdrPreview {
    private String tenDvi;
    private String maQhns;
    private String soBbTinhKho;
    private String tenCloaiVthh;
    private String tenLoaiVthh;
    private String tenNganKho;
    private String tenLoKho;
    private String ngayLap;
    private String chiCuc;
    private String lanhDaoChiCuc;
    private String keToan;
    private String ktvBaoQuan;
    private String thuKho;
    private BigDecimal tongSlXuatTheoQd;
    private BigDecimal tongSlXuatTheoTt;
    private BigDecimal slConLaiTheoSs;
    private BigDecimal slConLaiTheoTt;
    private BigDecimal thua;
    private BigDecimal thieu;
    private String nguyeNhan;
    private String kienNghi;
}
