package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import lombok.Data;

import java.util.List;

@Data
public class HhDxuatKhNhapKhacDTO {
    private HhDxuatKhNhapKhacHdr hdr;
    private List<HhDxuatKhNhapKhacDtl> dtl;
}
