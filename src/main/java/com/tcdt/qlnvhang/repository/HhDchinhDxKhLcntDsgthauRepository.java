package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthau;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HhDchinhDxKhLcntDsgthauRepository extends CrudRepository<HhDchinhDxKhLcntDsgthau, Long> {

    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtl(Long idDcDxDtl);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlOrderByGoiThau(Long idDcDxDtl);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdr(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdrOrderByGoiThau(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlIn(List<Long> ids);

    void deleteAllByIdDcDxDtl(Long idDcDxDtl);
    void deleteAllByIdDcDxHdr(Long idDcDxHdr);

    long countByIdDcDxDtl(Long idDcDxDtl);

}
