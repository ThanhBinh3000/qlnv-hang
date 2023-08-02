package com.tcdt.qlnvhang.request.object;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import lombok.Data;

import java.util.List;

@Data
public class HhDxuatKhLcntThopPreview {
    private String tenLoaiVthh;
    private String namKhoach;
    private String tongSl;
    private String tongSoGthau;
    private List<HhDxKhLcntThopDtl> details;
}
