package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBBNTBQDtlRepository extends JpaRepository<DcnbBBNTBQDtl, Long> {

//    List<DcnbBBNTBQDtl> findAllByHdrId(Long hdrId);
//
    void deleteAllByHdrId(Long hdrId);
    List<DcnbBBNTBQDtl> findByHdrId(Long id);


}
