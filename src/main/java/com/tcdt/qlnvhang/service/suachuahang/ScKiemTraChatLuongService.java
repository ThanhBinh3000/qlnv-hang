package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScKiemTraChatLuongService extends BaseService<ScKiemTraChatLuongHdr, ScKiemTraChatLuongReq, Long> {

    Page<ScQuyetDinhXuatHang> searchKiemTraChatLuong(ScKiemTraChatLuongReq req) throws Exception;

    List<ScKiemTraChatLuongHdr> searchDanhSachTaoQuyetDinhNhapHang(ScKiemTraChatLuongReq req) throws Exception;

}
