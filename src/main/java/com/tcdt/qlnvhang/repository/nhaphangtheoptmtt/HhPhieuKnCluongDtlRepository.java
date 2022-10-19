package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKnCluongDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhPhieuKnCluongDtlRepository extends JpaRepository<HhPhieuKnCluongDtl,Long> {

    List<HhPhieuKnCluongDtl> findAllByIdHdr(Long ids);

    List<HhPhieuKnCluongDtl> findAllByIdHdrIn(List<Long> ids);
}
