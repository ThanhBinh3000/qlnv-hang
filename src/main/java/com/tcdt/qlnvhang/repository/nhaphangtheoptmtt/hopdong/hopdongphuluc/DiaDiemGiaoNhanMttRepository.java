package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaDiemGiaoNhanMttRepository extends JpaRepository<DiaDiemGiaoNhanMtt,Long> {
    List<DiaDiemGiaoNhanMtt> findAllByIdHdr(Long ids);

    List<DiaDiemGiaoNhanMtt> findAllByIdHdrIn(List<Long> ids);
}
