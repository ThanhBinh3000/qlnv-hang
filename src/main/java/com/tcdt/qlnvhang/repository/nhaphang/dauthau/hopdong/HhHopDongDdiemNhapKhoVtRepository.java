package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKhoVt;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.List;

public interface HhHopDongDdiemNhapKhoVtRepository extends BaseRepository<HhHopDongDdiemNhapKhoVt, Long> {

	List<HhHopDongDdiemNhapKhoVt> findAllByIdHdongDdiemNkho(Long idHdongDdiemNkho);

	void deleteAllByIdHdongDdiemNkho(Long idHdongDdiemNkho);

}
