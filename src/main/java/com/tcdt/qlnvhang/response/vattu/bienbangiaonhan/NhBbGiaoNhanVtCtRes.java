package com.tcdt.qlnvhang.response.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.vattu.bienbangiaonhan.NhBbGiaoNhanVtCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class NhBbGiaoNhanVtCtRes {

    private Long id;

    private Long bbGiaoNhanVtId;

    private String loaiDaiDien;

    private String daiDien;

    private Integer stt;

    private String chucVu;

    private String maDvi;

    private String tenDvi;
    public NhBbGiaoNhanVtCtRes(NhBbGiaoNhanVtCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
