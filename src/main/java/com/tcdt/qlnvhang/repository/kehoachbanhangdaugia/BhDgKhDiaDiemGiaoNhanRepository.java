package com.tcdt.qlnvhang.repository.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.kehoachbanhangdaugia.BhDgKhDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BhDgKhDiaDiemGiaoNhanRepository extends BaseRepository<BhDgKhDiaDiemGiaoNhan, Long> {
	List<BhDgKhDiaDiemGiaoNhan> findByBhDgKehoachId(Long id);
}
