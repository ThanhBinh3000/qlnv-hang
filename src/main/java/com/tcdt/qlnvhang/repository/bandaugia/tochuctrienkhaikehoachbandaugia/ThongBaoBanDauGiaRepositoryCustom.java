package com.tcdt.qlnvhang.repository.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThongBaoBanDauGiaRepositoryCustom {
	Page<ThongBaoBanDauGiaSearchResponse> search(ThongBaoBanDauGiaSearchRequest req, Pageable pageable);
}
