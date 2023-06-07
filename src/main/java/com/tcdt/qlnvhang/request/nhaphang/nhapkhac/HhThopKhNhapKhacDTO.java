package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import lombok.Data;

import java.util.List;

@Data
public class HhThopKhNhapKhacDTO {
    private HhThopKhNhapKhac hdr;
    private List<HhDxuatKhNhapKhacHdr> dtl;
}
