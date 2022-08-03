package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDtl;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HhDchinhDxKhLcntDtlRepository extends CrudRepository<HhDchinhDxKhLcntDtl, Long> {

    List<HhDchinhDxKhLcntDtl> findAllByIdDxDcHdr (Long idDxDcHdr);

}
