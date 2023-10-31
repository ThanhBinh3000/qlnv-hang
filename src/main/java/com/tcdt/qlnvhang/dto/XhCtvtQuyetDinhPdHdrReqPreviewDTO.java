package com.tcdt.qlnvhang.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XhCtvtQuyetDinhPdHdrReqPreviewDTO {
    private String tenBaoCao;
    private Long id;
    private List<XhCtvtQuyetDinhPdHdrDTO> children;
}
