package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbHoSoKyThuat;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoKyThuatHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbHoSoKyThuatHdrRepository extends JpaRepository<DcnbHoSoKyThuatHdr, Long> {
    @Query(value = "SELECT distinct c FROM DcnbHoSoKyThuatHdr c  WHERE 1=1 " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<DcnbHoSoKyThuatHdr> search(@Param("param") SearchDcnbHoSoKyThuat param, Pageable pageable);

    Optional<DcnbHoSoKyThuatHdr> findFirstBySoHoSoKyThuat(String soHoSoKyThuat);

    List<DcnbHoSoKyThuatHdr> findByIdIn(List<Long> ids);

    List<DcnbHoSoKyThuatHdr> findAllByIdIn(List<Long> idList);
}
