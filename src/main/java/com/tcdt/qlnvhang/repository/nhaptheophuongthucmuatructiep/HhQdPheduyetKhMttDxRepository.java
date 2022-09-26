package com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPheduyetKhMttDxRepository extends JpaRepository<HhQdPheduyetKhMttDx, Long> {
    List<HhQdPheduyetKhMttDx> findAllByIdQdKhmtt(Long idQdKhmtt);

    List<HhQdPheduyetKhMttDx> findAllByIdQdKhmttIn(List<Long> ids);
}
