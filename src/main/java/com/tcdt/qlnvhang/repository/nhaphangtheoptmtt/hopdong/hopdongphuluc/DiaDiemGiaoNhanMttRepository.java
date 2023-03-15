package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DiaDiemGiaoNhanMttRepository extends JpaRepository<DiaDiemGiaoNhanMtt,Long> {
    @Transactional
    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    List<DiaDiemGiaoNhanMtt> findAllByIdHdr(Long idHdr);

    @Transactional
    List<DiaDiemGiaoNhanMtt>  findAllByIdHdDtl(Long idHdDtl);
}
