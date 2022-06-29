package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDsgthau;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface HhQdKhlcntDsgthauRepository extends BaseRepository<HhQdKhlcntDsgthau, Long> {

    List<HhQdKhlcntDsgthau> findByIdQdHdr(Long idQdKhlcntHdr);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_KHLCNT_DSGTHAU SET TRANG_THAI =:trangThai , LY_DO_HUY =:lyDoHuy WHERE ID = :idGt ", nativeQuery = true)
    void updateGoiThau(Long idGt, String trangThai, String lyDoHuy);

}
