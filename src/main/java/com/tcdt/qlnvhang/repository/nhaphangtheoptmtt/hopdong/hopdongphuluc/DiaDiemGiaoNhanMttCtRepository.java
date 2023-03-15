package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttCt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DiaDiemGiaoNhanMttCtRepository extends JpaRepository<DiaDiemGiaoNhanMttCt, Long> {

    @Transactional
    void deleteAllByIdDiaDiem(Long idDiaDiem);

    List<DiaDiemGiaoNhanMttCt> findAllByIdDiaDiem (Long idDiaDiem);
}
