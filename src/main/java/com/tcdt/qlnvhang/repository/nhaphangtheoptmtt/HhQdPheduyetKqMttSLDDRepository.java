package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;


import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttSLDD;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPheduyetKqMttSLDDRepository extends JpaRepository<HhQdPheduyetKqMttSLDD, Long> {



    void deleteByIdQdPdKq(Long idQdPdKq);

    List<HhQdPheduyetKqMttSLDD> findAllByIdQdPdKq(Long idQdPdKq);


}
