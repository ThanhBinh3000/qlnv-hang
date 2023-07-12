package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HhNkBBKetThucNKDtlRepository extends JpaRepository<HhNkBBKetThucNKDtl, Long> {
    void deleteAllByHdrId(Long id);
}
