package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkBbGiaoNhanDtlRepository extends JpaRepository<HhNkBbGiaoNhanDtl, Long> {
    List<HhNkBbGiaoNhanDtl> findByHdrId(Long id);

    List<HhNkBbGiaoNhanDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long hdrId);
}
