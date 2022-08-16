package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BhQdPheDuyetKhbdgRepositoryCustom {
	Page<BhQdPheDuyetKhbdgSearchResponse> search(BhQdPheDuyetKhbdgSearchRequest req, Pageable pageable);
}
