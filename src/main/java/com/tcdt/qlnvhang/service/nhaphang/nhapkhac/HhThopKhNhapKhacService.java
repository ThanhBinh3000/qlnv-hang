package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HhThopKhNhapKhacService {
    Page<HhThopKhNhapKhac> timKiem(HhThopKhNhapKhacSearch req);
    List<HhDxuatKhNhapKhacHdr> layDsDxuatChuaTongHop (HhThopKhNhapKhacSearch req);
    HhThopKhNhapKhac themMoi (HhThopKhNhapKhacReq req) throws Exception;
}
