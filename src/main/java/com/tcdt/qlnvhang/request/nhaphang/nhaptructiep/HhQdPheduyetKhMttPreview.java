package com.tcdt.qlnvhang.request.nhaphang.nhaptructiep;

import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import lombok.Data;

import java.util.List;

@Data
public class HhQdPheduyetKhMttPreview {
    private String tenCloaiVthh;
    private String namKhoach;
    private String tongSl;
    private String tongThanhTien;
    private List<HhQdPheduyetKhMttDx> details;
}
