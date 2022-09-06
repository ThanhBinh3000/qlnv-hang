package com.tcdt.qlnvhang.repository.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface XhBbLayMauRepositoryCustom {
	Page<XhBbLayMauSearchResponse> search(XhBbLayMauSearchRequest req, Pageable pageable);
}
