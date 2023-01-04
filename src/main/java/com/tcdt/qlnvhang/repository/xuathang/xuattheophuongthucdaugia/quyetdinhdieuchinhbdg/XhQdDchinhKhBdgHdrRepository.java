package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XhQdDchinhKhBdgHdrRepository extends JpaRepository<XhQdDchinhKhBdgHdr, Long> {
  @Query(value = "select * from XH_QD_DC_KH_BDG_HDR BDG " +
      " where (:namKh IS NULL OR BDG.NAM_KH = TO_NUMBER(:namKh)) " +
      "AND (:soQdDc IS NULL OR LOWER(BDG.SO_QD_DC) LIKE LOWER(CONCAT(CONCAT('%',:soQdDc),'%' ) ) )" +
      "AND (:trichYeu IS NULL OR LOWER(BDG.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%')))" +
      "AND (:ngayKyQdTu IS NULL OR BDG.NGAY_KY_QD >=  TO_DATE(:ngayKyQdTu,'yyyy-MM-dd')) " +
      "AND (:ngayKyQdDen IS NULL OR BDG.NGAY_KY_QD <= TO_DATE(:ngayKyQdDen,'yyyy-MM-dd'))" +
      "AND (:soTrHdr IS NULL OR LOWER(BDG.SO_TR_HDR) LIKE LOWER(CONCAT(CONCAT('%',:soTrHdr),'%' ) ) )" +
      "AND (:loaiVthh IS NULL OR LOWER(BDG.LOAI_VTHH) LIKE LOWER(CONCAT(:loaiVthh,'%' ) ) )" +
      "AND (:trangThai IS NULL OR BDG.TRANG_THAI = :trangThai)" +
      " AND (:lastest IS NULL OR BDG.LASTEST = :lastest) " +
      "AND (:maDvi IS NULL OR LOWER(BDG.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
      ,nativeQuery = true)
  Page<XhQdDchinhKhBdgHdr> searchPage(Integer namKh, String soQdDc, String trichYeu, String ngayKyQdTu, String ngayKyQdDen, String soTrHdr, String loaiVthh, String trangThai, Integer lastest, String maDvi, Pageable pageable);


  List<XhQdDchinhKhBdgHdr> findBySoQdDc(String soQdDc);
  List <XhQdDchinhKhBdgHdr> findAllByIdIn(List<Long> ids);
}