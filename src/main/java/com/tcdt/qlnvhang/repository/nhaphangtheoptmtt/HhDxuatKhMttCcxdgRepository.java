package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttCcxdgRepository extends JpaRepository<HhDxuatKhMttCcxdg,Long> {
    List<HhDxuatKhMttCcxdg> findByIdDxKhmtt (Long idDxKhmtt);

    void deleteAllByIdDxKhmtt(Long idDxKhmtt);
    @Transactional
    void deleteAllByIdDxKhmttIn(List<Long> idDxKhmtt);

}
