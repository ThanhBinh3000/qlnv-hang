package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhieuKtChatLuongDtlRepository extends JpaRepository<DcnbPhieuKtChatLuongDtl, Long> {
    List<DcnbPhieuKtChatLuongDtl> findByHdrId(Long id);

    List<DcnbPhieuKtChatLuongDtl> findByHdrIdIn(List<Long> listId);
}
