package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.request.object.khlcnt.DiaDiemNhapReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.khlcnt.DiaDiemNhapRes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DiaDiemNhapService {
	ListResponse<DiaDiemNhapRes> list(Long goiThauId, Pageable pageable);
	List<DiaDiemNhapRes> update(Long goiThauId, List<DiaDiemNhapReq> reqList);
	boolean deleteByGoiThauIds(Set<Long> goiThauId);
}
