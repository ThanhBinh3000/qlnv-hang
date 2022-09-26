package com.tcdt.qlnvhang.repository.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSldd;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttSlddRepository extends JpaRepository<HhDxuatKhMttSldd,Long> {
    void deleteAllByIdDxKhmtt(Long idDxKhmtt);

    List<HhDxuatKhMttSldd> findAllByIdDxKhmtt(Long idDxKhmtt);

    List<HhDxuatKhMttSldd> findAllByIdDxKhmttIn(List<Long> idDxKhmtt);

    @Transactional
    void deleteAllByIdDxKhmttIn(List<Long> ids);
}
