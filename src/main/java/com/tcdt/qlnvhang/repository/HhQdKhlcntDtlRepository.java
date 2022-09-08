package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDtl;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface HhQdKhlcntDtlRepository extends BaseRepository<HhQdKhlcntDtl, Long> {

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<HhQdKhlcntDtl> findAllByIdQdHdr (Long idQdHdr);

    @Query(value = "SELECT ID_QD_HDR,count(*) as c  from HH_QD_KHLCNT_DTL where ID_QD_HDR in (:qdIds) group by ID_QD_HDR",
            nativeQuery = true)
    List<Object[]> countAllBySoGthau(Collection<Long> qdIds);

}
