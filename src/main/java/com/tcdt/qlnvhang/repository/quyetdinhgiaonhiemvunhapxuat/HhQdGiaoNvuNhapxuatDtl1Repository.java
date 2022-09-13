package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HhQdGiaoNvuNhapxuatDtl1Repository extends BaseRepository<HhQdGiaoNvuNhapxuatDtl1, Long> {

//    @Query("SELECT dtl1 FROM HhQdGiaoNvuNhapxuatDtl1 dtl1 WHERE dtl1.parent.id IN ?1")
    List<HhQdGiaoNvuNhapxuatDtl1> findByIdHdrIn(Collection<Long> idHdr);

    List<HhQdGiaoNvuNhapxuatDtl1> findAllByIdHdr(Long idHdr);
}
