package com.tcdt.qlnvhang.request.object;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import lombok.Data;

import java.util.List;

@Data
public class HhDxuatKhLcntThopPreview {
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String namKhoach;
    private String tongSl;
    private String tongThanhTien;
    private String tongSoGthau;
    private String soTtr;
    private String ngayTrinh;
    private List<HhDxKhLcntThopDtl> details;
}
