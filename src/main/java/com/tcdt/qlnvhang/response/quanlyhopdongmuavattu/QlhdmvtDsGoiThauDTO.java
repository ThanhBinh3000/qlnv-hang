package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdmvtDsGoiThauDTO {
    private Long id;
    private Long stt;
    private String tenGoiThau;
    private String soHieuGoiThau;
    private Long soLuong;
    private Integer thueVat;
    private BigDecimal donGiaTruocThue;
    private String thue;
    private BigDecimal giaTru0CThue;
    private BigDecimal giaSauThue;
    private String ghiChu;
}
