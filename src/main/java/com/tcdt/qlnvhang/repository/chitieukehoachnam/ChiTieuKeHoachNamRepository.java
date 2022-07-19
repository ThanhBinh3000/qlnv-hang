package com.tcdt.qlnvhang.repository.chitieukehoachnam;


import com.tcdt.qlnvhang.entities.chitieukehoachnam.ChiTieuKeHoachNam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChiTieuKeHoachNamRepository extends JpaRepository<ChiTieuKeHoachNam, Long> {
	List<ChiTieuKeHoachNam> findByIdIn(Collection<Long> ids);
}
