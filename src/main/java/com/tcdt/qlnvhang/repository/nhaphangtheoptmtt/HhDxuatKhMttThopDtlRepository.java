package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttThopDtlRepository extends JpaRepository<HhDxKhMttThopDtl,Long> {
    List<HhDxKhMttThopDtl> findByIdThopHdr(Long idThopHdr);
    List<HhDxKhMttThopDtl> findByIdThopHdrIn(List<Long> idThopHdr);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<HhDxKhMttThopDtl> ids);
}
