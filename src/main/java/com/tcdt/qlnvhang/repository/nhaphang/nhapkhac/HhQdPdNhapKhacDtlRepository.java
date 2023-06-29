package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhQdPdNhapKhacDtlRepository extends JpaRepository<HhQdPdNhapKhacDtl, Long> {
    void deleteAllByIdHdr (Long hdrId);
    List<HhQdPdNhapKhacDtl> findAllByIdHdr (Long hdrId);
    List<HhQdPdNhapKhacDtl> findAllByIdDxHdr (Long hdrId);
}
