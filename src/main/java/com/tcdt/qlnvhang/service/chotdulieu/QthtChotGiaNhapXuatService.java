package com.tcdt.qlnvhang.service.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaInfoReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.response.chotdulieu.QthtChotGiaInfoRes;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;


public interface QthtChotGiaNhapXuatService extends BaseService<QthtChotGiaNhapXuat, QthtChotGiaNhapXuatReq, Long> {

    QthtChotGiaInfoRes thongTinChotDieuChinhGia(QthtChotGiaInfoReq objReq) throws Exception;

    boolean checkKhoaChotDieuChinhGia() throws Exception;

    boolean checkKhoaChotNhapXuat() throws Exception;
}
