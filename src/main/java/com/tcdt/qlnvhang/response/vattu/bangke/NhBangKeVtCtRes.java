package com.tcdt.qlnvhang.response.vattu.bangke;

import com.tcdt.qlnvhang.entities.vattu.bangke.NhBangKeVtCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBangKeVtCtRes {
    private Long id;
    private Long bangKeVtId;
    private BigDecimal soLuong;
    private String soSerial;
    private Integer stt;

    public NhBangKeVtCtRes(NhBangKeVtCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
