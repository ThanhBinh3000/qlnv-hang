package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcTcTTDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbQuyetDinhDcTcTTDtlRepository extends JpaRepository<DcnbQuyetDinhDcTcTTDtl, Long> {

    List<DcnbQuyetDinhDcTcTTDtl> findByHdrId(Long idHdr);

    List<DcnbQuyetDinhDcTcTTDtl> findByHdrIdIn(List<Long> ids);
}