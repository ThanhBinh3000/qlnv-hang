package com.tcdt.qlnvhang.request.object.xuatcuutrovientro;

import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtDeXuatHdrDto;
import lombok.Data;

import java.util.List;

@Data
public class XhCtvtDeXuatHdrPreview {
    private String chungLoaiHangHoa;
    private Integer namKeHoach;
    private String tongSoLuongXuatCap;
    private List<XhCtvtDeXuatHdrDto> xhCtvtDeXuatHdrDto;
}