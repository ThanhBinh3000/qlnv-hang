package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.request.object.khlcnt.GoiThauReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.GoiThauRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoiThauService {
	ListResponse<GoiThauRes> list(Long khLcntId, Pageable pageable);
	GoiThauRes getDetail(Long id, Pageable pageable) throws Exception;
	GoiThauRes getDetail(Long id) throws Exception;
	boolean deleteByKhLcntId(Long khLcntId);
	List<GoiThauRes> update(Long khLcntId, List<GoiThauReq> reqList);
}
