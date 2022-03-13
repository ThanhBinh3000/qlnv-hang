package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.khlcnt.KhLuaChonNhaThauRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KhLuaChonNhaThauService {
	KhLuaChonNhaThauRes create(KhLuaChonNhaThauReq req);
	KhLuaChonNhaThauRes update(KhLuaChonNhaThauReq req);
	KhLuaChonNhaThauRes getDetail(Long id);
	KhLuaChonNhaThauRes getDetail(Long id, Pageable pageable);
	List<KhLuaChonNhaThauRes> search(KhLuaChonNhaThauSearchReq req);
	boolean delete(Long id);
}
