package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScPhieuXuatKhoService extends BaseService<ScPhieuXuatKhoHdr, ScPhieuXuatKhoReq, Long> {

    Page<ScQuyetDinhXuatHang> searchPhieuXuatKho(ScPhieuXuatKhoReq req);

    List<ScPhieuXuatKhoHdr> searchDanhSachTaoBangKe(ScPhieuXuatKhoReq req);

}
