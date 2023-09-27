package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacSearch;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HhBbNghiemThuNhapKhacService {
    Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhBbNghiemThuNhapKhacSearch req) throws Exception;
    HhBbNghiemThuNhapKhac themMoi (HhBbNghiemThuNhapKhacReq objReq) throws Exception;
    HhBbNghiemThuNhapKhac capNhat (HhBbNghiemThuNhapKhacReq objReq) throws Exception;
    HhBbNghiemThuNhapKhac chiTiet (Long id) throws Exception;
    HhBbNghiemThuNhapKhac pheDuyet(StatusReq stReq) throws Exception;
    Object getDataKho(String maDvi) throws Exception;
    void xoa (IdSearchReq idSearchReq) throws Exception;
    void exportBbNtBq (HhBbNghiemThuNhapKhacSearch req, HttpServletResponse response) throws Exception;
    List<HhBbNghiemThuNhapKhac> timKiemBbtheoMaNganLo (HhBbNghiemThuNhapKhacSearch objReq) throws Exception;
    List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBbNtBqLd (HhBbNghiemThuNhapKhacSearch objReq) throws Exception;
    ReportTemplateResponse preview(HhBbNghiemThuNhapKhacReq objReq) throws Exception;
}
