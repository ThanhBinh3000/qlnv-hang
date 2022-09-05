package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthau;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HhDchinhDxKhLcntDsgthauRepository extends CrudRepository<HhDchinhDxKhLcntDsgthau, Long> {

    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtl(Long idDcDxDtl);

    void deleteAllByIdDcDxDtl(Long idDcDxDtl);

    long countByIdDcDxDtl(Long idDcDxDtl);

}
