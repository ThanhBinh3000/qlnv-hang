package com.tcdt.qlnvhang.repository.dauthauvattu;

import com.tcdt.qlnvhang.entities.dauthauvattu.ThongTinDauThauVT;
import com.tcdt.qlnvhang.request.search.ThongTinDauThauVTSearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ThongTinDauThauVTRepositoryCustom {
	Page<ThongTinDauThauVT> search(ThongTinDauThauVTSearchReq req, Pageable pageable);
}
