package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanLayMau;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanNghiemThu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhBienBanLayMauRepository extends JpaRepository<HhBienBanLayMau,Long> {
    @Query(value = "select * from HH_BIEN_BAN_LAY_MAU BB where (:namKh IS NULL OR BB.NAM_KH = TO_NUMBER(:namKh))" +
            "AND (:soBb IS NULL OR LOWER(BB.SO_BIEN_BAN) LIKE LOWER(CONCAT(CONCAT('%',:soBb),'%' ) ) )" +
            "AND (:soQdNh IS NULL OR LOWER(BB.SO_QD_GIAO_NV_NH) LIKE LOWER(CONCAT(CONCAT('%',:soQdNh),'%' ) ) )" +
            "AND (:dviKn IS NULL OR LOWER(BB.DVI_KIEM_NGHIEM) LIKE LOWER(CONCAT(CONCAT('%',:dviKn),'%' ) ) )" +
            "AND (:ngayLayMauTu IS NULL OR BB.NGAY_LAY_MAU >=  TO_DATE(:ngayLayMauTu,'yyyy-MM-dd')) " +
            "AND (:ngayLayMauDen IS NULL OR BB.NGAY_LAY_MAU <= TO_DATE(:ngayLayMauDen,'yyyy-MM-dd'))"+
            "AND (:trangThai IS NULL OR BB.TRANG_THAI = :trangThai) " +
            "AND (:maDvi IS NULL OR LOWER(BB.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhBienBanLayMau> searchPage(Integer namKh, String soBb, String soQdNh, String dviKn, String ngayLayMauTu, String ngayLayMauDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhBienBanLayMau> findBySoBienBan(String soBb);

    List<HhBienBanLayMau> findAllByIdIn(List<Long> listId);

    List<HhBienBanLayMau> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);
    List<HhBienBanLayMau> findByIdQdGiaoNvNhAndMaLoKho(Long idQdGiaoNvNh, String maLoKho);
}
