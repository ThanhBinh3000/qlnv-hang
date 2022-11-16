package com.tcdt.qlnvhang.response.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanBanGiaoMauCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Data
public class BienBanBanGiaoMauCtRes {
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String daiDien;

    public BienBanBanGiaoMauCtRes(BienBanBanGiaoMauCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
