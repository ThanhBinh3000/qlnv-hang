package com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BhBbBanDauGiaCt1Res {
    private Long id;
    private Long bbBanDauGiaId;
    private Integer soLanTraGia;
    private BigDecimal donGiaCaoNhat;
    private BigDecimal thanhTien;
    private String traGiaCaoNhat;
}
