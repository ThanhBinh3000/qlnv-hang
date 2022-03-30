package com.tcdt.qlnvhang.service.dauthauvattu;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.dauthauvattu.ThongTinDauThauVTReq;
import com.tcdt.qlnvhang.request.search.ThongTinDauThauVTSearchReq;
import com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes;
import org.springframework.data.domain.Page;

public interface ThongTinDauThauVTService {
	ThongTinDauThauRes create(ThongTinDauThauVTReq req) throws Exception;
	ThongTinDauThauRes update(ThongTinDauThauVTReq req) throws Exception;
	ThongTinDauThauRes detail(Long id, Integer pageIndex, Integer pageSize) throws Exception;
	boolean delete(Long id) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
	Page<ThongTinDauThauRes> search(ThongTinDauThauVTSearchReq req);
}
