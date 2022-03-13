package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.response.khlcnt.GoiThauRes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoiThauServiceImpl implements GoiThauService {
	@Override
	public List<GoiThauRes> list(Long khLcntId, Pageable pageable) {
		return null;
	}

	@Override
	public GoiThauRes getDetail(Long id, Pageable pageable) {
		return null;
	}

	@Override
	public GoiThauRes getDetail(Long id) {
		return null;
	}
}
