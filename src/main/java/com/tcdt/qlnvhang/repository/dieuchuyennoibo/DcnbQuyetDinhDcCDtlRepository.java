package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbQuyetDinhDcCDtlRepository extends JpaRepository<DcnbQuyetDinhDcCDtl, Long> {

    List<DcnbQuyetDinhDcCDtl> findByHdrId(Long idHdr);

    List<DcnbQuyetDinhDcCDtl> findByHdrIdIn(List<Long> ids);
}