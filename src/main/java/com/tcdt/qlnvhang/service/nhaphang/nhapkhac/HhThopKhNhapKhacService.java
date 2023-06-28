package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacDTO;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HhThopKhNhapKhacService {
    Page<HhThopKhNhapKhac> timKiem(HhThopKhNhapKhacSearch req);
    List<HhDxuatKhNhapKhacHdr> layDsDxuatChuaTongHop (HhThopKhNhapKhacSearch req);
    HhThopKhNhapKhac themMoi (HhThopKhNhapKhacReq req) throws Exception;
    HhThopKhNhapKhac capNhat (HhThopKhNhapKhacReq req) throws Exception;
    HhThopKhNhapKhacDTO chiTiet (Long id) throws Exception;
    void delete(IdSearchReq idSearchReq) throws Exception;
    void deleteMulti(IdSearchReq idSearchReq) throws Exception;
    void exportList(HhThopKhNhapKhacSearch objReq, HttpServletResponse response) throws Exception;
}
