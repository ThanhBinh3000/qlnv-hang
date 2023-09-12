package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DcnbPhieuKnChatLuongDtlDto {
    private int stt;
    private String chiTieuCl;
    private String chiSoCl;
    private String ketQuaPt;
    private String phuongPhap;
    private Long danhGia;
}
