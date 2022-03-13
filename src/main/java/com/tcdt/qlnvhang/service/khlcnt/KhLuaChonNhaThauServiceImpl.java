package com.tcdt.qlnvhang.service.khlcnt;

import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauReq;
import com.tcdt.qlnvhang.request.object.khlcnt.KhLuaChonNhaThauSearchReq;
import com.tcdt.qlnvhang.response.khlcnt.KhLuaChonNhaThauRes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhLuaChonNhaThauServiceImpl implements KhLuaChonNhaThauService{
	@Override
	public KhLuaChonNhaThauRes create(KhLuaChonNhaThauReq req) {
		return null;
	}

	@Override
	public KhLuaChonNhaThauRes update(KhLuaChonNhaThauReq req) {
		return null;
	}

	@Override
	public KhLuaChonNhaThauRes getDetail(Long id) {
		return null;
	}

	@Override
	public KhLuaChonNhaThauRes getDetail(Long id, Pageable pageable) {
		return null;
	}

	@Override
	public List<KhLuaChonNhaThauRes> search(KhLuaChonNhaThauSearchReq req) {
		return null;
	}

	@Override
	public boolean delete(Long id) {
		return false;
	}
}
