package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhQdPdKhBdgRepository extends JpaRepository<XhQdPdKhBdg,Long> {

    @Query(value = "select * from XH_QD_PD_KH_BDG BDG " +
            " where (:namKh IS NULL OR BDG.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soQdPd IS NULL OR LOWER(BDG.SO_QD_PD) LIKE LOWER(CONCAT(CONCAT('%',:soQdPd),'%' ) ) )" +
            "AND (:trichYeu IS NULL OR LOWER(BDG.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%')))" +
            "AND (:ngayKyQdTu IS NULL OR BDG.NGAY_KY_QD >=  TO_DATE(:ngayKyQdTu,'yyyy-MM-dd')) " +
            "AND (:ngayKyQdDen IS NULL OR BDG.NGAY_KY_QD <= TO_DATE(:ngayKyQdDen,'yyyy-MM-dd'))" +
            "AND (:soTrHdr IS NULL OR LOWER(BDG.SO_TR_HDR) LIKE LOWER(CONCAT(CONCAT('%',:soTrHdr),'%' ) ) )" +
            "AND (:loaiVthh IS NULL OR LOWER(BDG.LOAI_VTHH) LIKE LOWER(CONCAT(:loaiVthh,'%' ) ) )" +
            "AND (:trangThai IS NULL OR BDG.TRANG_THAI = :trangThai)" +
            "AND (:maDvi IS NULL OR LOWER(BDG.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<XhQdPdKhBdg> searchPage(Integer namKh, String soQdPd, String trichYeu, String ngayKyQdTu, String ngayKyQdDen, String soTrHdr, String loaiVthh, String trangThai, String maDvi,  Pageable pageable);


    List<XhQdPdKhBdg> findBySoQdPd(String soQdPd);
    List <XhQdPdKhBdg> findAllByIdIn(List<Long> ids);
}
