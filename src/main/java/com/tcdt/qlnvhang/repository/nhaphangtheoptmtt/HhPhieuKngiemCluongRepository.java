package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKngiemCluong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhPhieuKngiemCluongRepository extends JpaRepository<HhPhieuKngiemCluong,Long> {

    @Query(value = "select * from HH_PHIEU_KNGHIEM_CLUONG PKN where (:namKh IS NULL OR PKN.NAM_KH = TO_NUMBER(:namKh))" +
            " AND (:soQdNh IS NULL OR LOWER(PKN.SO_QD_NH) LIKE LOWER(CONCAT(CONCAT('%',:soQdNh),'%')))" +
            " AND (:soPhieu IS NULL OR LOWER(PKN.SO_PHIEU) LIKE LOWER(CONCAT(CONCAT('%',:soPhieu),'%')))" +
            " AND (:soBb IS NULL OR LOWER(PKN.SO_BIEN_BAN) LIKE LOWER(CONCAT(CONCAT('%',:soBb),'%')))" +
            " AND (:ngayKnTu IS NULL OR PKN.NGAY_KN_MAU >=  TO_DATE(:ngayKnTu,'yyyy-MM-dd')) " +
            " AND (:ngayKnDen IS NULL OR PKN.NGAY_KN_MAU <= TO_DATE(:ngayKnDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR PKN.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(PKN.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhPhieuKngiemCluong> searchPage(Integer namKh, String soQdNh, String soPhieu, String soBb, String ngayKnTu, String ngayKnDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhPhieuKngiemCluong> findAllBySoBbLayMau(String soBblayMau);

    List<HhPhieuKngiemCluong> findAllByIdIn(List<Long> ids);

    List<HhPhieuKngiemCluong> findBySoQdGiaoNvNhAndMaDvi(String idQdGiaoNvNh, String maDvi);
    List<HhPhieuKngiemCluong> findBySoQdGiaoNvNhAndMaLoKho(String idQdGiaoNvNh, String maLoKho);


}
