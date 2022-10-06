package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDiaDiemGiaoNhanHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDiaDiemGiaoNhanHangRepository extends JpaRepository<HhDiaDiemGiaoNhanHang,Long> {

    List<HhDiaDiemGiaoNhanHang> findAllByIdHdr(Long idHdr);
    List<HhDiaDiemGiaoNhanHang> findAllByIdHdrIn(List<Long> idHdr);
}
