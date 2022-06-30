package com.tcdt.qlnvhang.response.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.vattu.hosokythuat.NhHoSoKyThuatCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatCtRes {
    private Long id;
    private Long hoSoKyThuatId;
    private String loaiDaiDien;
    private String daiDien;
    private Integer stt;

    public NhHoSoKyThuatCtRes(NhHoSoKyThuatCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
