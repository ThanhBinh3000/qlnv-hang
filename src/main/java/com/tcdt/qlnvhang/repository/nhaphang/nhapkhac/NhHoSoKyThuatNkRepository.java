package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatNk;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepositoryCustom;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface NhHoSoKyThuatNkRepository extends BaseRepository<NhHoSoKyThuatNk, Long>, NhHoSoKyThuatNkRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    @Query(
            value = "SELECT * FROM NH_HO_SO_KY_THUAT_NK  HS " +
                    "WHERE 1=1 " +
                    "  AND (:#{#objReq.soBbKtraHskt} IS NULL OR LOWER(HS.SO_BB_KTRA_HSKT) LIKE LOWER(CONCAT(CONCAT('%', :#{#objReq.soBbKtraHskt}),'%'))) " +
                    "  AND (:#{#objReq.soBbKtraVanHanh} IS NULL OR LOWER(HS.SO_BB_KTRA_VAN_HANH) LIKE LOWER(CONCAT(CONCAT('%', :#{#objReq.soBbKtraVanHanh}),'%')))" +
                    "  AND (:#{#objReq.soBbKtraNgoaiQuan} IS NULL OR LOWER(HS.SO_BB_KTRA_NGOAI_QUAN) LIKE LOWER(CONCAT(CONCAT('%', :#{#objReq.soBbKtraNgoaiQuan}),'%')))" +
                    "  AND (:#{#objReq.soHoSoKyThuat} IS NULL OR LOWER(HS.SO_HO_SO_KY_THUAT) LIKE LOWER(CONCAT(CONCAT('%', :#{#objReq.soHoSoKyThuat}),'%')))",
            countQuery = "SELECT COUNT(1) FROM NH_HO_SO_KY_THUAT_NK  HS",
            nativeQuery = true)
    Page<NhHoSoKyThuatNk> selectPage(NhHoSoKyThuatReq objReq, Pageable pageable);

    NhHoSoKyThuatNk findBySoBbLayMau(String soBienBanLayMau);

    NhHoSoKyThuatNk findByIdBbLayMau(Long id);


//    Optional<NhHoSoKyThuat> findFirstBySoBienBan(String soBienBan);
}
