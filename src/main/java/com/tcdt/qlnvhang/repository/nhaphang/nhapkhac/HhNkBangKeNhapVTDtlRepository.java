package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkBangKeNhapVTDtlRepository extends JpaRepository<HhNkBangKeNhapVTDtl, Long> {
    List<HhNkBangKeNhapVTDtl> findByHdrId(Long id);

    List<HhNkBangKeNhapVTDtl> findByHdrIdIn(List<Long> listId);
}
