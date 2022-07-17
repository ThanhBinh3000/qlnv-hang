package com.tcdt.qlnvhang.repository.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhPhanLoTaiSan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BhDgKhPhanLoTaiSanRepository extends BaseRepository<BhDgKhPhanLoTaiSan, Long> {
	List<BhDgKhPhanLoTaiSan> findByBhDgKehoachId(Long id);
}
