package com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.response.banhangdaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhBbBanDauGiaCt1Res extends BanDauGiaPhanLoTaiSanResponse {
    private Long bbBanDauGiaId;
    private Integer soLanTraGia;
    private BigDecimal donGiaCaoNhat;
    private BigDecimal thanhTien;
    private String traGiaCaoNhat;

    public BhBbBanDauGiaCt1Res(BanDauGiaPhanLoTaiSan item) {
        BeanUtils.copyProperties(item, this);
    }
}
