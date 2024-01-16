package com.tcdt.qlnvhang.request.chotdulieu;

import lombok.Data;

import java.util.List;

@Data
public class QthtChotGiaInfoReq {
    private Long idQuyetDinhCanDieuChinh;
    private List<String> maCucs;
    private Integer nam;
    private String loaiVthh;
    private String cloaiVthh;
    private String loaiGia;
}
