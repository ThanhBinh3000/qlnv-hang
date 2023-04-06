package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbKeHoachDcDtlRepository extends JpaRepository<DcnbKeHoachDcDtl,Long> {

    List<DcnbKeHoachDcDtl> findByIdHdr(Long idHdr);

    List<DcnbKeHoachDcDtl> findAllByIdHdrIn(List<Long> ids);
    }