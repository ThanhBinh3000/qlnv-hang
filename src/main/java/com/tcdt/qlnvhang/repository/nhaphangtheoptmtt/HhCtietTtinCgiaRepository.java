package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhCtietTtinCgiaRepository extends JpaRepository<HhChiTietTTinChaoGia,Long> {

    void deleteAllByIdQdDtl(Long idQdDtl);

    List<HhChiTietTTinChaoGia> findAllByIdQdDtl(Long idQdDtl);

}
