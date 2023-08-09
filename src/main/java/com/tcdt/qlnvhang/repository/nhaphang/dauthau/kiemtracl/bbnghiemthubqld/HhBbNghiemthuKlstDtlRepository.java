package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bbnghiemthubqld;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhBbNghiemthuKlstDtlRepository extends JpaRepository<HhBbNghiemthuKlstDtl, Long> {
    void deleteAllByHdrId (Long hdrId);
    List<HhBbNghiemthuKlstDtl> findAllByHdrId (Long hdrId);
}
