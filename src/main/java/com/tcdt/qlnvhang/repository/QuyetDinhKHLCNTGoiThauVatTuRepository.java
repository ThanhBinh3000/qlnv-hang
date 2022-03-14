package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.entities.QuyetDinhKHLCNTGoiThauVatTu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuyetDinhKHLCNTGoiThauVatTuRepository extends JpaRepository<QuyetDinhKHLCNTGoiThauVatTu, Long> {
	List<QuyetDinhKHLCNTGoiThauVatTu> findByIdIn(Collection<Long> ids);

	List<QuyetDinhKHLCNTGoiThauVatTu> findByQuyetDinhId(Long quetDinhId);
}
