package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScPhieuNhapKhoService extends BaseService<ScPhieuNhapKhoHdr, ScPhieuNhapKhoReq, Long> {

    Page<ScQuyetDinhNhapHang> searchPhieuNhapKho(ScPhieuNhapKhoReq req) throws Exception;

    List<ScPhieuNhapKhoHdr> searchDanhSachTaoBangKe(ScPhieuNhapKhoReq req);

    ReportTemplateResponse preview(ScPhieuNhapKhoReq objReq) throws Exception;
}
