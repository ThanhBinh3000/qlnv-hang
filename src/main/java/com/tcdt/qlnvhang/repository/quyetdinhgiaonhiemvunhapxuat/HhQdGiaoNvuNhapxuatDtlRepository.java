package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatDtl1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface HhQdGiaoNvuNhapxuatDtlRepository extends BaseRepository<HhQdGiaoNvuNhapxuatDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);
    List<HhQdGiaoNvuNhapxuatDtl> findAllByIdHdr(Long idHdr);
}
