package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.khlcnt.KhLuaChonNhaThauRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhLuaChonNhaThauService {
	KhLuaChonNhaThauRes create(KhLuaChonNhaThauReq req) throws Exception;
	KhLuaChonNhaThauRes update(KhLuaChonNhaThauReq req) throws Exception;
	KhLuaChonNhaThauRes getDetail(Long id) throws Exception;
	KhLuaChonNhaThauRes getDetail(Long id, Pageable pageable) throws Exception;
	List<KhLuaChonNhaThauRes> search(KhLuaChonNhaThauSearchReq req);
	boolean delete(Long id) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
}
