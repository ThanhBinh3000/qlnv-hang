package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhieuKnChatLuongDtlRepository extends JpaRepository<DcnbPhieuKnChatLuongDtl, Long> {
    List<DcnbPhieuKnChatLuongDtl> findByHdrId(Long id);

    List<DcnbPhieuKnChatLuongDtl> findByHdrIdIn(List<Long> listId);
}
