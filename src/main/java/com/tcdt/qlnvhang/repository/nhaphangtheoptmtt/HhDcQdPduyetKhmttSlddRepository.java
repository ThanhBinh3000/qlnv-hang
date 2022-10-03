package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPduyetKhmttSldd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDcQdPduyetKhmttSlddRepository extends JpaRepository<HhDcQdPduyetKhmttSldd,Long> {
    List<HhDcQdPduyetKhmttSldd> findAllByIdDxKhmttIn(List<Long> idDxKhmtt);
}
