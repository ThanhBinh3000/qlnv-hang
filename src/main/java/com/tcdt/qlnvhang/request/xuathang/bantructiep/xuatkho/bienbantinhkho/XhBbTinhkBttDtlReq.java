package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBbTinhkBttDtlReq {

    private Long id;

    private Long idHdr;

    private Long idPhieu;

    private String soPhieu;

    private Long idPhieuXuat;

    private String soPhieuXuat;

    private Long idBangKe;

    private String soBangKe;

    private LocalDate ngayXuatKho;

    private BigDecimal soLuongThucXuat;
}
