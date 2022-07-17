package com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.vattu.bienbangiaonhan.NhBbGiaoNhanVtCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBbGiaoNhanVtCtRepository extends BaseRepository<NhBbGiaoNhanVtCt, Long> {
    List<NhBbGiaoNhanVtCt> findByBbGiaoNhanVtIdIn(Collection<Long> bbGnVtIds);

    @Transactional
    @Modifying
    void deleteByBbGiaoNhanVtIdIn(Collection<Long> bbGnVtIds);

    List<NhBbGiaoNhanVtCt> findByBbGiaoNhanVtIdInAndLoaiDaiDien(Collection<Long> bbGnVtIds, String loaiDaiDien);
}
