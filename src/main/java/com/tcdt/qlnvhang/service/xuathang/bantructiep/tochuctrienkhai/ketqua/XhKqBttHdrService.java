package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttTchuc;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface XhKqBttHdrService extends BaseService<XhKqBttHdr, XhKqBttHdrReq, Long> {

    XhKqBttTchuc detailToChuc (Long id) throws Exception;
}
