package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhQdPheduyetKhMttDxRepository extends JpaRepository<HhQdPheduyetKhMttDx, Long> {

    List<HhQdPheduyetKhMttDx> findAllByIdPduyetHdr(Long idPduyetHdr);
    List<HhQdPheduyetKhMttDx> findAllByIdPduyetHdrIn(List<Long> idPduyetHdr);

}
