package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBienBanGuiHangRepository extends BaseRepository<NhBienBanGuiHang, Long> {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    NhBienBanGuiHang findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

//    Optional<NhBienBanGuiHang> findFirstBySoBienBan(String soBienBan);
}
