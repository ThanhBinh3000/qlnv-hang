package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietKqTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhCtietKqTtinCgiaRepository extends JpaRepository<HhChiTietKqTTinChaoGia,Long> {

    void deleteAllByIdQdPdKqSldd(Long idQdDtl);

    List<HhChiTietKqTTinChaoGia> findAllByIdQdPdKqSldd(Long idQdPdSldd);

}
