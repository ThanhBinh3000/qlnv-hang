package com.tcdt.qlnvhang.service.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtReq;
import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;

import javax.transaction.Transactional;

public interface QdPheDuyetKqlcntVtService {
    Object create(QdPheDuyetKqlcntVtReq req) throws Exception;

    Object update(QdPheDuyetKqlcntVtReq req) throws Exception;

    boolean delete(Long id) throws Exception;

    Object search(QdPheDuyetKqlcntVtSearchReq req) throws Exception;

    Object detail(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatusQd(StatusReq req) throws Exception;

    Object export();
}
