package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.response.khlcnt.GoiThauRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoiThauService {
	List<GoiThauRes> list(Long khLcntId, Pageable pageable);
	GoiThauRes getDetail(Long id, Pageable pageable);
	GoiThauRes getDetail(Long id);
}
