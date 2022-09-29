package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttCcxdgRepository extends JpaRepository<HhDxuatKhMttCcxdg,Long> {
    void deleteAllByIdDxKhmtt(Long idDxmtt);

    List<HhDxuatKhMttCcxdg> findAllByIdDxKhmtt(Long idDxmtt);

    List<HhDxuatKhMttCcxdg> findAllByIdDxKhmttIn(List<Long> ids);

    @Transactional(rollbackOn = Exception.class)
    Void deleteAllByIdDxKhmttIn(List<Long> ids);
}
