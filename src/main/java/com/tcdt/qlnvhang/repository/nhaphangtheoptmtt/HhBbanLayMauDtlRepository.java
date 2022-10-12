package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBbanLayMauDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhBbanLayMauDtlRepository extends JpaRepository<HhBbanLayMauDtl,Long> {
    List<HhBbanLayMauDtl> findAllByIdHdr(Long ids);
    List<HhBbanLayMauDtl> findAllByIdHdrIn(List<Long> ids);
}
