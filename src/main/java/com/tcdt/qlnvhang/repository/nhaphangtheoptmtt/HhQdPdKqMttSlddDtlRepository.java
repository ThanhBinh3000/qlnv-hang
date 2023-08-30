package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKQMttSlddDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HhQdPdKqMttSlddDtlRepository extends JpaRepository<HhQdPdKQMttSlddDtl,Long> {
    List<HhQdPdKQMttSlddDtl> findAllByIdDiaDiem(Long idDiaDiem);
    Optional<HhQdPdKQMttSlddDtl> findAllByIdDiaDiemAndMaDiemKho(Long idDiaDiem, String maDiemKho);

    void deleteAllByIdDiaDiem(Long idDiaDiem);

}
