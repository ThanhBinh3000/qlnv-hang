package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HhHopDongDdiemNhapKhoRepository extends BaseRepository<HhHopDongDdiemNhapKho, Long> {

	List<HhHopDongDdiemNhapKho> findAllByIdHdongHdr(Long idHdongHdr);


}
