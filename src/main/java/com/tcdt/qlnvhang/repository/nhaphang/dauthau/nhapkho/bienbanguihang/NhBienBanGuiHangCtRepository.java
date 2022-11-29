package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBienBanGuiHangCtRepository extends BaseRepository<NhBienBanGuiHangCt, Long> {
    List<NhBienBanGuiHangCt> findByBienBanGuiHangIdIn(Collection<Long> bienBanGhIds);

    @Transactional
    @Modifying
    void deleteByBienBanGuiHangIdIn(Collection<Long> bienBanGhIds);

    @Transactional
    @Modifying
    void deleteByBienBanGuiHangId(Long bienBanGuiHangId);
}
