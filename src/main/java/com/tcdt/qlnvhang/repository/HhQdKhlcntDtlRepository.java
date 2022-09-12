package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface HhQdKhlcntDtlRepository extends JpaRepository<HhQdKhlcntDtl, Long> {

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<HhQdKhlcntDtl> findAllByIdQdHdr (Long idQdHdr);

    HhQdKhlcntDtl findByIdQdHdr(Long idQdHdr);

    @Query(value = "SELECT ID_QD_HDR,count(*) as c  from HH_QD_KHLCNT_DTL where ID_QD_HDR in (:qdIds) group by ID_QD_HDR",
            nativeQuery = true)
    List<Object[]> countAllBySoGthau(Collection<Long> qdIds);

    @Query(value = "SELECT ID_QD_HDR,NVL(SUM(DTL.TONG_TIEN),0) FROM HH_QD_KHLCNT_DTL DTL WHERE ID_QD_HDR in (:qdIds) group by ID_QD_HDR ",
            nativeQuery = true)
    List<Object[]> sumTongTienByIdHdr(Collection<Long> qdIds);


}
