package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbDataLinkRepository extends JpaRepository<DcnbDataLink, Long> {

    DcnbDataLink findByKeHoachDcDtlId(Long idKhDcDtl);
//
    List<DcnbDataLink> findByKeHoachDcHdrId(Long idKhDcHdr);
}
