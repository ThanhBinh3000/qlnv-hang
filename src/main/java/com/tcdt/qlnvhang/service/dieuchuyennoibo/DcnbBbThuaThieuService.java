package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbThuaThieuHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuDtl;

import java.util.List;

public interface DcnbBbThuaThieuService extends BaseService<DcnbBbThuaThieuHdr, DcnbBbThuaThieuHdrReq, Long> {
    List<DcnbBbThuaThieuDtl> thongTinNhapXuatHang(DcnbBbKqDcSearch objReq) throws Exception;
}
