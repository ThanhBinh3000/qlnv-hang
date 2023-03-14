package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSldd;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttSlddRepository extends JpaRepository<HhDxuatKhMttSldd,Long> {
    void deleteAllByIdHdr(Long idHdr);

    List<HhDxuatKhMttSldd> findAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);
}
