package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import lombok.Data;

import java.util.List;

@Data
public class HhQdPdNhapKhacDTO {
    private HhQdPdNhapKhacHdr hdr;
    private List<HhQdPdNhapKhacDtl> dtl;
}
