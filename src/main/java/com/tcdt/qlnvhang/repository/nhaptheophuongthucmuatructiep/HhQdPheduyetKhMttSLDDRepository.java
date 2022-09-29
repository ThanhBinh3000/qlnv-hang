package com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep;


import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttSLDD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPheduyetKhMttSLDDRepository extends JpaRepository<HhQdPheduyetKhMttSLDD, Long> {


    List<HhQdPheduyetKhMttSLDD> findAllByIdDxKhmtt(Long idDxKhmtt);
    List<HhQdPheduyetKhMttSLDD> findAllByIdDxKhmttIn(List<Long> idDxKhmtt);


}
