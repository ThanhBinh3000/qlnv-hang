package com.tcdt.qlnvhang.request.nhaphang.nhaptructiep;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopDtl;
import lombok.Data;

import java.util.List;

@Data
public class HhDxKhMttThopPreview {
    private String tenCloaiVthh;
    private String namKhoach;
    private String tongSl;
    private String tongThanhTien;
    private List<HhDxKhMttThopDtl> details;
}
