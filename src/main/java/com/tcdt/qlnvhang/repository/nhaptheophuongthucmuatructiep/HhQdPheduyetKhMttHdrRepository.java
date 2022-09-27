package com.tcdt.qlnvhang.repository.nhaptheophuongthucmuatructiep;

import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HhQdPheduyetKhMttHdrRepository extends JpaRepository<HhQdPheduyetKhMttHdr, Long> {

    @Query(value = "select * from HH_QD_PHE_DUYET_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soQd IS NULL OR LOWER(MTT.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%' ) ) )" +
            "AND (:trichYeu IS NULL OR LOWER(MTT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%')))" +
            "AND (:ngayKyQdTu IS NULL OR MTT.NGAY_KY_QD >=  TO_DATE(:ngayKyQdTu,'yyyy-MM-dd')) " +
            "AND (:ngayKyQdDen IS NULL OR MTT.NGAY_KY_QD <= TO_DATE(:ngayKyQdDen,'yyyy-MM-dd'))" +
            "AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            "AND (:trangThai IS NULL OR MTT.TRANG_THAI = :trangThai)" +
            "AND (:trangThaiTh IS NULL OR MTT.TRANG_THAI_TH = :trangThaiTh) " +
            "AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhQdPheduyetKhMttHdr> searchPage(Integer namKh, String soQd, String trichYeu, String ngayKyQdTu, String ngayKyQdDen, String loaiVthh, String maDvi, String trangThai, String trangThaiTh,  Pageable pageable);


    Optional<HhQdPheduyetKhMttHdr> findBySoQd(String soQd);

    List <HhQdPheduyetKhMttHdr>findAllByIdIn(List<Long> listId);
}
