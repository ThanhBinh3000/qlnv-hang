package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia;

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
public class BhQdPheDuyetKqbdgCtRes extends BanDauGiaPhanLoTaiSanResponse {
    private Long id;
    private BigDecimal donGiaTrungDauGia;
    private String trungDauGia;

    public BhQdPheDuyetKqbdgCtRes(BanDauGiaPhanLoTaiSan item) {
        BeanUtils.copyProperties(item, this);
    }
}
