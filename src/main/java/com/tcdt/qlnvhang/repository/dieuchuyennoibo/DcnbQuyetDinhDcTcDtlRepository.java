package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbQuyetDinhDcTcDtlRepository extends JpaRepository<DcnbQuyetDinhDcTcDtl, Long> {

    List<DcnbQuyetDinhDcTcDtl> findByHdrId(Long idHdr);

    List<DcnbQuyetDinhDcTcDtl> findByHdrIdIn(List<Long> ids);
}