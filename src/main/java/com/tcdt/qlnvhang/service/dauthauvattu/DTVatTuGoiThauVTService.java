package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.request.object.dauthauvattu.DTVatTuGoiThauVTReq;
import com.tcdt.qlnvhang.response.ListResponse;
import com.tcdt.qlnvhang.response.dauthauvattu.DTVatTuGoiThauVTRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DTVatTuGoiThauVTService {
	ListResponse<DTVatTuGoiThauVTRes> list(Long ttdtVtId, Pageable pageable);
	List<DTVatTuGoiThauVTRes> update(List<DTVatTuGoiThauVTReq> reqList, Long ttdtVtId);
	boolean deleteByTtdtVtId(Long ttdtVtId);
	DTVatTuGoiThauVTRes detail(Long goiThauId, Integer pageSize, Integer pageIndex) throws Exception;
}
