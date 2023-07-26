package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HhQdPduyetKqlcntDtlRepository extends JpaRepository<HhQdPduyetKqlcntDtl, Long> {
    HhQdPduyetKqlcntDtl findByIdGoiThauAndType (Long id, String type);
    void deleteAllByIdQdPdHdr (Long id);
}
