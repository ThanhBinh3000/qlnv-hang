package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlhdmvtDsGoiThauResponseDTO {
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
    private List<QlhdmvtDiaDiemNhapVtResponseDTO> diaDiemNhapVtList;
}
