package com.tcdt.qlnvhang.repository.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ThongBaoBanDauGiaRepository extends BaseRepository<ThongBaoBanDauGia, Long>, ThongBaoBanDauGiaRepositoryCustom {
	void deleteAllByIdIn(Collection<Long> ids);
}
