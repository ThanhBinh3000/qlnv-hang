package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKqbdgCtRes extends BhQdPheDuyetKhBdgThongTinTaiSanResponse {
    private Long id;
    private BigDecimal donGiaTrungDauGia;
    private String trungDauGia;

    public BhQdPheDuyetKqbdgCtRes(BhQdPheDuyetKhBdgThongTinTaiSan item) {
        BeanUtils.copyProperties(item, this);
    }
}
