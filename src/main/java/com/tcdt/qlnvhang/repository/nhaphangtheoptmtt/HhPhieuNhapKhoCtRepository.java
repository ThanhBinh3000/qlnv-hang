package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhPhieuNhapKhoCtRepository extends BaseRepository<HhPhieuNhapKhoCt, Long> {

    List<HhPhieuNhapKhoCt> findAllByIdHdr(Long idHdr);
}
