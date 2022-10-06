package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDCDiaDiemGiaoNhanHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDcDiaDiemGiaoNhanHangRepository extends JpaRepository<HhDCDiaDiemGiaoNhanHang,Long> {
    List<HhDCDiaDiemGiaoNhanHang> findAllByIdHdPluc(Long ids);

    List<HhDCDiaDiemGiaoNhanHang> findAllByIdHdPlucIn(List<Long> ids);
}
