package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanNghiemThu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhBienBanNghiemThuRepository extends JpaRepository<HhBienBanNghiemThu,Long> {

    @Query(value = "select *from HH_BIEN_BAN_NGHIEM_THU BB where (:namKh IS NULL OR BB.NAM_KH = TO_NUMBER(:namKh))" +
            "AND (:soQd IS NULL OR LOWER(BB.SO_QD_NH) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%' ) ) )"+
            "AND (:soBb IS NULL OR LOWER(BB.SO_BB) LIKE LOWER(CONCAT(CONCAT('%',:soBb),'%' ) ) )"+
            "AND (:ngayKnTu IS NULL OR BB.NGAY_PDUYET >=  TO_DATE(:ngayKnTu,'yyyy-MM-dd')) " +
            "AND (:ngayKnDen IS NULL OR BB.NGAY_PDUYET <= TO_DATE(:ngayKnDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR BB.TRANG_THAI = :trangThai)" +
            "AND (:maDvi IS NULL OR LOWER(BB.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhBienBanNghiemThu> searchPage(Integer namKh, String soQd, String soBb, String ngayKnTu, String ngayKnDen, String trangThai, String maDvi, Pageable pageable);

    List<HhBienBanNghiemThu> findAllByIdIn(List<Long> ids);

    Optional<HhBienBanNghiemThu> findAllBySoBb(String soBb);

    List<HhBienBanNghiemThu> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);


}
