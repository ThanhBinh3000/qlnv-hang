package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhuongAnDc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhuongAnDcRepository extends JpaRepository<DcnbPhuongAnDc, Long> {
    List<DcnbPhuongAnDc> findByKeHoachDcHdrId(Long id);
}
