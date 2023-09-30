package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoHdr;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XhTlPhieuXuatKhoService extends BaseService<XhTlPhieuXuatKhoHdr, XhTlPhieuXuatKhoReq, Long> {

    List<XhTlPhieuXuatKhoHdr> searchDanhSachTaoBangKe(XhTlPhieuXuatKhoReq req) throws Exception;

}