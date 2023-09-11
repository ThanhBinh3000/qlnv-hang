package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhHopDongBttHdrRepository extends JpaRepository<XhHopDongBttHdr, Long> {

    @Query("SELECT HD FROM XhHopDongBttHdr HD " +
            "WHERE (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namHd} IS NULL OR HD.namHd = :#{#param.namHd}) " +
            "AND (:#{#param.soHd} IS NULL OR LOWER(HD.soHd) LIKE LOWER(CONCAT('%', :#{#param.soHd}, '%'))) " +
            "AND (:#{#param.tenHd} IS NULL OR LOWER(HD.tenHd) LIKE LOWER(CONCAT('%', :#{#param.tenHd}, '%'))) " +
            "AND (:#{#param.tenDviMua} IS NULL OR LOWER(HD.tenDviMua) LIKE LOWER(CONCAT('%', :#{#param.tenDviMua}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR HD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(HD.soQdKq) LIKE LOWER(CONCAT('%', :#{#param.soQdKq}, '%'))) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(HD.soQdNv) LIKE LOWER(CONCAT('%', :#{#param.soQdNv}, '%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY HD.ngaySua DESC, HD.ngayTao DESC, HD.id DESC")
    Page<XhHopDongBttHdr> searchPage(@Param("param") XhHopDongBttHdrReq param, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '00' AND ID_QD_KQ = :idQdKq", nativeQuery = true)
    Integer countSlHopDongChuaKyCuc(@Param("idQdKq") Long idQdKq);

    @Query(value = "SELECT COUNT(*) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '00' AND ID_CHAO_GIA = :idChaoGia", nativeQuery = true)
    Integer countSlHopDongChuaKyChiCuc(@Param("idChaoGia") Long idChaoGia);

    @Query(value = "SELECT SUM(SO_LUONG_BAN_TRUC_TIEP) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '30' AND ID_QD_KQ = :idQdKq", nativeQuery = true)
    BigDecimal countSlXuatBanKyHdongCuc(@Param("idQdKq") Long idQdKq);

    @Query(value = "SELECT SUM(SO_LUONG_BAN_TRUC_TIEP) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '30' AND ID_CHAO_GIA = :idChaoGia", nativeQuery = true)
    BigDecimal countSlXuatBanKyHdongChiCuc(@Param("idChaoGia") Long idChaoGia);

    @Query(value = "SELECT COUNT(*) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '30' AND ID_QD_KQ = :idQdKq", nativeQuery = true)
    Integer countSlHopDongDaKyCuc(@Param("idQdKq") Long idQdKq);

    @Query(value = "SELECT COUNT(*) FROM XH_HOP_DONG_BTT_HDR WHERE TRANG_THAI = '30' AND ID_CHAO_GIA = :idChaoGia", nativeQuery = true)
    Integer countSlHopDongDaKyChiCuc(@Param("idChaoGia") Long idChaoGia);

    boolean existsBySoHd(String soHd);

    boolean existsBySoPhuLuc(String soPhuLuc);

    boolean existsBySoHdAndIdNot(String soHd, Long id);

    boolean existsBySoPhuLucAndIdNot(String soPhuLuc, Long id);

    List<XhHopDongBttHdr> findByIdIn(List<Long> idHdList);

    List<XhHopDongBttHdr> findAllByIdHd(Long idHd);

    List<XhHopDongBttHdr> findAllByIdChaoGia(Long idChaoGia);

    List<XhHopDongBttHdr> findAllByIdQdKq(Long idQdKq);

    List<XhHopDongBttHdr> findAllByIdQdNv(Long idQdNv);
}
