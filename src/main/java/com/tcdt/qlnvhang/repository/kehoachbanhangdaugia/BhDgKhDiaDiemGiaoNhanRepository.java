package com.tcdt.qlnvhang.repository.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BhDgKhDiaDiemGiaoNhanRepository extends BaseRepository<BanDauGiaDiaDiemGiaoNhan, Long> {
	List<BanDauGiaDiaDiemGiaoNhan> findByBhDgKehoachId(Long id);
	List<BanDauGiaDiaDiemGiaoNhan> findByBhDgKehoachIdIn(List<Long> id);

	void deleteAllByBhDgKehoachIdIn(List<Long> ids);
}
