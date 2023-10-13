package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDtl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface HhDchinhDxKhLcntDtlRepository extends CrudRepository<HhDchinhDxKhLcntDtl, Long> {

    List<HhDchinhDxKhLcntDtl> findAllByIdDxDcHdr (Long idDxDcHdr);
    List<HhDchinhDxKhLcntDtl> findAllByIdDxDcHdrOrderByMaDvi (Long idDxDcHdr);

    void deleteAllByIdDxDcHdr(Long IdDxDcHdr);



    @Query(value = "SELECT ID_DX_DC_HDR,count(*) as c  from HH_DC_DX_LCNT_DTL where ID_DX_DC_HDR in (:dcIds) group by ID_DX_DC_HDR",
            nativeQuery = true)
    List<Object[]> countAllByDcHdr(Collection<Long> dcIds);

}
