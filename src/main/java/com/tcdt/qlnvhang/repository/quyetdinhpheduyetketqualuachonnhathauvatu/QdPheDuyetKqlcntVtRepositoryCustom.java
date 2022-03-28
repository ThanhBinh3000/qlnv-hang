package com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;
import com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtRes;
import org.springframework.data.domain.Page;

public interface QdPheDuyetKqlcntVtRepositoryCustom {
    Page<QdPheDuyetKqlcntVtRes> search(QdPheDuyetKqlcntVtSearchReq req);
}
