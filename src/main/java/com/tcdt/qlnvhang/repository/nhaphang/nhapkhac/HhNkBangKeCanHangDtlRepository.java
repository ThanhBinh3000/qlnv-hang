package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkBangKeCanHangDtlRepository extends JpaRepository<HhNkBangKeCanHangDtl, Long> {
    List<HhNkBangKeCanHangDtl> findByHdrId(Long id);

    List<HhNkBangKeCanHangDtl> findByHdrIdIn(List<Long> listId);
}
