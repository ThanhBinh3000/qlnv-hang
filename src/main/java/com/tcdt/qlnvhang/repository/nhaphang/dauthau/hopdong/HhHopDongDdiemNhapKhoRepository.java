package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.repository.BaseRepository;

import java.util.List;

public interface HhHopDongDdiemNhapKhoRepository extends BaseRepository<HhHopDongDdiemNhapKho, Long> {

	List<HhHopDongDdiemNhapKho> findAllByIdHdongDtl(Long idHdongDtl);

//	List<HhHopDongDdiemNhapKho> findAllByIdHdongHdrIn(Collection<Long> idHdongHdrs);

	void deleteAllByIdHdongDtl(Long idHdongDtl);


}
