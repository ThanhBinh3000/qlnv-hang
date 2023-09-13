package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
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

    List<BienBanLayMau> findByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    BienBanLayMau findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);
    @Query(
            value = " SELECT BB FROM BienBanLayMau BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.ngayLayMauTu} IS NULL OR BB.ngayLayMau >= TO_DATE(:#{#req.ngayLayMauTu}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.ngayLayMauDen} IS NULL OR BB.ngayLayMau <= TO_DATE(:#{#req.ngayLayMauDen}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idQdGiaoNvNh} IS NULL OR BB.idQdGiaoNvNh = :#{#req.idQdGiaoNvNh}) " +
                    " AND (:#{#req.maDviDtl} IS NULL OR BB.maDvi = :#{#req.maDviDtl}) " +
                    " AND (:#{#req.soBienBan} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBienBan}),'%'))) " +
                    " AND (:#{#req.dviKiemNghiem} IS NULL OR LOWER(BB.dviKiemNghiem) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.dviKiemNghiem}),'%'))) ",
            countQuery = " SELECT BB FROM BienBanLayMau BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.ngayLayMauTu} IS NULL OR BB.ngayLayMau >= TO_DATE(:#{#req.ngayLayMauTu}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.ngayLayMauDen} IS NULL OR BB.ngayLayMau <= TO_DATE(:#{#req.ngayLayMauDen}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idQdGiaoNvNh} IS NULL OR BB.idQdGiaoNvNh = :#{#req.idQdGiaoNvNh}) " +
                    " AND (:#{#req.maDviDtl} IS NULL OR BB.maDvi = :#{#req.maDviDtl}) " +
                    " AND (:#{#req.soBienBan} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBienBan}),'%'))) " +
                    " AND (:#{#req.dviKiemNghiem} IS NULL OR LOWER(BB.dviKiemNghiem) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.dviKiemNghiem}),'%'))) "
    )
    List<BienBanLayMau> timKiemList(HhQdNhapxuatSearchReq req);

    @Query(
            value = " SELECT BB FROM BienBanLayMau BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.ngayLayMauTuStr} IS NULL OR BB.ngayLayMau >= TO_DATE(:#{#req.ngayLayMauTuStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.ngayLayMauDenStr} IS NULL OR BB.ngayLayMau <= TO_DATE(:#{#req.ngayLayMauDenStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " +
                    " AND (:#{#req.soBienBan} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBienBan}),'%'))) " +
                    " AND (:#{#req.dviKiemNghiem} IS NULL OR LOWER(BB.dviKiemNghiem) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.dviKiemNghiem}),'%'))) ",
            countQuery = " SELECT COUNT * FROM ( SELECT BB FROM BienBanLayMau BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.ngayLayMauTuStr} IS NULL OR BB.ngayLayMau >= TO_DATE(:#{#req.ngayLayMauTuStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.ngayLayMauDenStr} IS NULL OR BB.ngayLayMau <= TO_DATE(:#{#req.ngayLayMauDenStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " +
                    " AND (:#{#req.soBienBan} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBienBan}),'%'))) " +
                    " AND (:#{#req.dviKiemNghiem} IS NULL OR LOWER(BB.dviKiemNghiem) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.dviKiemNghiem}),'%')))) "
    )
    BienBanLayMau timKiemByDiaDiemNh (HhQdNhapxuatSearchReq req);

    @Query(
            value = "SELECT * FROM NH_BB_LAY_MAU  BB ",
            countQuery = "SELECT COUNT(1) FROM NH_BB_LAY_MAU  BB",
            nativeQuery = true)
    Page<BienBanLayMau> selectPage( Pageable pageable);
}
