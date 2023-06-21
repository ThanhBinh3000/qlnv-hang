package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhCtietTtinCgiaRepository extends JpaRepository<HhChiTietTTinChaoGia,Long> {

    void deleteAllByIdQdPdSldd(Long idQdDtl);

    List<HhChiTietTTinChaoGia> findAllByIdQdPdSldd(Long idQdPdSldd);

}
