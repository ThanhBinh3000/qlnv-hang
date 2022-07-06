package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntDtl;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HhQdPduyetKqlcntDtlRepository extends BaseRepository<HhQdPduyetKqlcntDtl, Long> {

    List<HhQdPduyetKqlcntDtl> findByIdQdPdHdr(Long idQdPdHdr);

}
