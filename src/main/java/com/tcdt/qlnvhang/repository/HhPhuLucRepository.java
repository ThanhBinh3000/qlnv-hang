package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhPhuLucHd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HhPhuLucRepository extends BaseRepository<HhPhuLucHd, Long> {

	List<HhPhuLucHd> findBySoHd(String soHd);

}
