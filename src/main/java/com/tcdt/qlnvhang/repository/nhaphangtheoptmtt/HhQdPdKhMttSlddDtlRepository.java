package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPdKhMttSlddDtlRepository extends JpaRepository<HhQdPdKhMttSlddDtl,Long> {
    List<HhQdPdKhMttSlddDtl> findByIdDiaDiem(Long idDiaDiem);

    void deleteAllByIdDiaDiem(Long idDiaDiem);

}
