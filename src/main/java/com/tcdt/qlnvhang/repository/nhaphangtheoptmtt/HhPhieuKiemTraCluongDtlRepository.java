package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKiemTraChatLuongDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhPhieuKiemTraCluongDtlRepository extends JpaRepository<HhPhieuKiemTraChatLuongDtl,Long> {
    List<HhPhieuKiemTraChatLuongDtl> findAllByIdHdr(Long ids);

    List<HhPhieuKiemTraChatLuongDtl> findAllByIdHdrIn(List<Long> ids);
}
