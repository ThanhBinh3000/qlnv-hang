package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.response.khlcnt.DiaDiemNhapRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiaDiemNhapService {
	List<DiaDiemNhapRes> list(Long goiThauId, Pageable pageable);
}
