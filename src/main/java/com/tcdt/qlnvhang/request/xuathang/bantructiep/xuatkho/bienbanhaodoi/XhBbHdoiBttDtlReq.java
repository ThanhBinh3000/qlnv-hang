package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbanhaodoi;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBbHdoiBttDtlReq {
    private Long id;
    private Long idHdr;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Long idBangKeHang;
    private String soBangKeHang;
    private BigDecimal soLuongXuat;
}