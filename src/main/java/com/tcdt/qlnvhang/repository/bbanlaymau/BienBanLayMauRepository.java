package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BienBanLayMauRepository extends BaseRepository<BienBanLayMau, Long> {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BienBanLayMau> findFirstBySoBienBan(String soBienBan);

    List<BienBanLayMau> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);

    BienBanLayMau findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);


    @Query(
            value = "SELECT * FROM NH_BB_LAY_MAU  BB ",
            countQuery = "SELECT COUNT(1) FROM NH_BB_LAY_MAU  BB",
            nativeQuery = true)
    Page<BienBanLayMau> selectPage( Pageable pageable);
}
