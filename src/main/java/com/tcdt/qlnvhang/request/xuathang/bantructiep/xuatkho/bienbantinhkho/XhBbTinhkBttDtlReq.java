package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhBbTinhkBttDtlReq {

    private Long id;

    private Long idHdr;

    private String soPhieu;

    private String soPhieuXuat;

    private String soBangKe;

    private LocalDate ngayXuatKho;

    private BigDecimal soLuongThucXuat;
}
