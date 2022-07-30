package com.tcdt.qlnvhang.response.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMauCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Data
public class BienBanLayMauCtRes {
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String daiDien;

    public BienBanLayMauCtRes(BienBanLayMauCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
