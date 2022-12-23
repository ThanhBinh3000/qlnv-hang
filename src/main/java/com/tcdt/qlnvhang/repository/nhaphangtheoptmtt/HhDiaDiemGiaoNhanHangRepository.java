package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDiaDiemGiaoNhanHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HhDiaDiemGiaoNhanHangRepository extends JpaRepository<HhDiaDiemGiaoNhanHang,Long> {

    List<HhDiaDiemGiaoNhanHang> findAllByIdHdongDtl(Long idHdongDtl);
    void deleteAllByIdHdongDtl(Long idHdongDtl);

    @Transactional
    void deleteAllByIdHdongDtlIn(List<Long> idHdongDtl);
}
