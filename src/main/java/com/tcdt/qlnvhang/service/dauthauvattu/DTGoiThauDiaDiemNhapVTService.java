package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.request.object.dauthauvattu.DTGoiThauDiaDiemNhapVTReq;
import com.tcdt.qlnvhang.response.dauthauvattu.DTGoiThauDiaDiemNhapVTRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DTGoiThauDiaDiemNhapVTService {
	Page<DTGoiThauDiaDiemNhapVTRes> findByGoiThauId(Long dtvtGoiThauId, Pageable pageable);
	List<DTGoiThauDiaDiemNhapVTRes> update(List<DTGoiThauDiaDiemNhapVTReq> list, Long dtvtGoiThauId);
	boolean deleteByGoiThauIds(Set<Long> dtvtGoiThauId);
}
