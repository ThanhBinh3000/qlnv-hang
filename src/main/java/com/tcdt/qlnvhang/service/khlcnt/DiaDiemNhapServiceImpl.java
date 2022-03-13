package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.response.khlcnt.DiaDiemNhapRes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaDiemNhapServiceImpl implements DiaDiemNhapService {
	@Override
	public List<DiaDiemNhapRes> list(Long goiThauId, Pageable pageable) {
		return null;
	}
}
