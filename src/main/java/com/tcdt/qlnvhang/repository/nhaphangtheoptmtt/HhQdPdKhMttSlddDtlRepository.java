package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPdKhMttSlddDtlRepository extends JpaRepository<HhQdPdKhMttSlddDtl,Long> {
    List<HhQdPdKhMttSlddDtl> findAllByIdDiaDiem(Long idDiaDiem);

    void deleteByIdDiaDiem(Long idDiaDiem);

}
