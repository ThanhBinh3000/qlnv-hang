package com.tcdt.qlnvhang.request.object.xuatcuutrovientro;

import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtTongHopDto;
import lombok.Data;

import java.util.List;

@Data
public class XhCtvtTongHopHdrPreview {
    private Integer namKeHoach;
    private String loaiVthh;
    private String tongSoLuong;
    List<XhCtvtTongHopDto> xhCtvtTongHopDto;
}
