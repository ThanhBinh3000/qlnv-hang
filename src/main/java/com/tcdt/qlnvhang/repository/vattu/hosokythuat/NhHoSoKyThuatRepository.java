package com.tcdt.qlnvhang.repository.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhHoSoKyThuatRepository extends BaseRepository<NhHoSoKyThuat, Long>, NhHoSoKyThuatRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    @Query(
            value = "SELECT * FROM NH_HO_SO_KY_THUAT  HS  WHERE :maDvi LIKE CONCAT(HS.MA_DVI, '%')",
            countQuery = "SELECT COUNT(1) FROM NH_HO_SO_KY_THUAT  HS WHERE :maDvi LIKE CONCAT(HS.MA_DVI, '%')",
            nativeQuery = true)
    Page<NhHoSoKyThuat> selectPage(String maDvi, Pageable pageable);

    NhHoSoKyThuat findBySoBbLayMau(String soBienBanLayMau);
    NhHoSoKyThuat findByIdBbLayMau(Long id);


//    Optional<NhHoSoKyThuat> findFirstBySoBienBan(String soBienBan);
}
