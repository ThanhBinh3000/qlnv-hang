package com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BanDauGiaPhanLoTaiSanRepository extends BaseRepository<BanDauGiaPhanLoTaiSan, Long> {
	List<BanDauGiaPhanLoTaiSan> findByBhDgKehoachId(Long id);
	List<BanDauGiaPhanLoTaiSan> findByBhDgKehoachIdIn(List<Long> id);
	void deleteAllByBhDgKehoachIdIn(List<Long> ids);
	List<BanDauGiaPhanLoTaiSan> findByBbBanDauGiaIdIn(Collection<Long> ids);
}
