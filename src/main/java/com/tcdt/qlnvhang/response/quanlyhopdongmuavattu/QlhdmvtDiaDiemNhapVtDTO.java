package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdmvtDiaDiemNhapVtDTO {
    private Long id;
    private Long stt;
    private Long donViId;
    private String maDonVi;
    private Long soLuongNhap;
    private Long qlhdmvtDsGoiThauId;
}
