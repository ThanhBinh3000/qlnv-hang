package com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class NhBbGiaoNhanVtCtReq {

    private Long id;

    private Long bbGiaoNhanVtId;

    private String loaiDaiDien;

    private String daiDien;

    private Integer stt;

    private String chucVu;
}
