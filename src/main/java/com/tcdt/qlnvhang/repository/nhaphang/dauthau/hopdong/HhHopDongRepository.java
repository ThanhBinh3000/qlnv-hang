package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import java.beans.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HhHopDongRepository extends BaseRepository<HhHopDongHdr, Long> {

  Optional<HhHopDongHdr> findBySoHd(String soHd);
  Optional<HhHopDongHdr> findBySoQdKqLcntAndIdGoiThau(String soQd, Long idGthau);

  List<HhHopDongHdr> findAllByIdQdKqLcnt(Long idQdKqLcnt);


  @Query(
      value = "SELECT * " +
          "FROM HH_HOP_DONG_HDR  HDR" +
          " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
          "  AND (:soHd IS NULL OR HDR.SO_HD LIKE CONCAT(CONCAT('%',:soHd),'%')) " +
          "  AND (:tenHd IS NULL OR LOWER(HDR.TEN_HD) LIKE LOWER(CONCAT(CONCAT('%',:tenHd),'%'))) " +
          "  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
          "  AND (:namHd IS NULL OR HDR.NAM_HD = :namHd) " +
          "  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'YYYY-MM-DD HH24:MI:SS')) " +
          "  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'YYYY-MM-DD HH24:MI:SS')) " +
          "  AND (:maDvi IS NULL OR HDR.MA_DVI = :maDvi) " +
          "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
      nativeQuery = true)
  Page<HhHopDongHdr> select(String loaiVthh, String soHd, String tenHd, String nhaCcap, String tuNgayKy, String denNgayKy, String trangThai, String maDvi, String namHd, Pageable pageable);

  @Query(
      value = "SELECT * " +
          "FROM HH_HOP_DONG_HDR  HDR" +
          " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
          "  AND (:soHd IS NULL OR HDR.SO_HD LIKE CONCAT(CONCAT('%',:soHd),'%')) " +
          "  AND (:tenHd IS NULL OR LOWER(HDR.TEN_HD) LIKE LOWER(CONCAT(CONCAT('%',:tenHd),'%'))) " +
          "  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
          "  AND (:namHd IS NULL OR HDR.NAM_HD = :namHd) " +
          "  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'yyyy-MM-dd')) " +
          "  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'yyyy-MM-dd')) " +
          "  AND (:maDvi IS NULL OR HDR.MA_DVI LIKE CONCAT(:maDvi,'%')) " +
          "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
      nativeQuery = true)
  Page<HhHopDongHdr> selectAll(String loaiVthh, String soHd, String tenHd, String nhaCcap, String tuNgayKy, String denNgayKy, String trangThai, String maDvi, String namHd, Pageable pageable);

  List<HhHopDongHdr> findByIdIn(Collection<Long> ids);

  @Query(
      value = " SELECT DISTINCT * " +
          " FROM HH_HOP_DONG_HDR HD " +
          " WHERE HD.TRANG_THAI = " + Contains.DAKY + " " +
          " AND NOT EXISTS(SELECT NX.ID +" +
          " FROM NH_QD_GIAO_NVU_NHAPXUAT NX" +
          " LEFT JOIN NH_QD_GIAO_NVU_NHAPXUAT_CT1 CT ON CT.ID_HDR = NX.ID" +
          " WHERE HD.ID = CT.HOP_DONG_ID" +
          " AND NX.TRANG_THAI = " + Contains.DABANHANH_QD + " )" +
          "  AND (:maDvi IS NULL OR HD.MA_DVI = :maDvi) " +
          "  AND (:loaiVthh IS NULL OR HD.LOAI_VTHH = :loaiVthh) ",
      nativeQuery = true)
  List<HhHopDongHdr> ListHdTheoDk(String maDvi, String loaiVthh);

  List<HhHopDongHdr> findByIdIn(List<Long> ids);

  @Transient
  void deleteByIdIn(List<Long> ids);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_HOP_DONG_HDR HD SET HD.TRANG_THAI=:trangThai WHERE HD.ID = :idHopDong ", nativeQuery = true)
	void updateHopDong(Long idHopDong, String trangThai);


  @Query(
      value = "SELECT HDR.* " +
          "FROM HH_HOP_DONG_HDR HDR JOIN HH_HOP_DONG_DTL DTL ON HDR.ID = DTL.ID_HDR" +
          " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
          "  AND (:namHd IS NULL OR HDR.NAM_HD = :namHd) " +
          "  AND (:maDvi IS NULL OR ( HDR.MA_DVI = :maDvi OR DTL.MA_DVI = :maDvi ))" +
          "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
      nativeQuery = true)
  Page<HhHopDongHdr> lookupData(String loaiVthh,String namHd,  String maDvi, String trangThai, Pageable pageable);
}
