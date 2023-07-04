package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhac;
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
public interface BienBanLayMauKhacRepository extends BaseRepository<BienBanLayMauKhac, Long> {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BienBanLayMauKhac> findFirstBySoBienBan(String soBienBan);

    List<BienBanLayMauKhac> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);
    BienBanLayMauKhac findByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    BienBanLayMauKhac findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);


    @Query(
            value = "SELECT * FROM NH_BB_LAY_MAU_KHAC  BB ",
            countQuery = "SELECT COUNT(1) FROM NH_BB_LAY_MAU_KHAC  BB",
            nativeQuery = true)
    Page<BienBanLayMauKhac> selectPage( Pageable pageable);
}
