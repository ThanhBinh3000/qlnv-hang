package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;


import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttSLDD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPheduyetKhMttSLDDRepository extends JpaRepository<HhQdPheduyetKhMttSLDD, Long> {



    void deleteByIdQdDtl(Long idQdDtl);

    List<HhQdPheduyetKhMttSLDD> findAllByIdQdDtl(Long idQdDtl);


}
