package com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhHopDongHdrRepository extends JpaRepository<XhHopDongHdr, Long> {

    @Query("SELECT HD FROM XhHopDongHdr HD " +
            "WHERE (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soHopDong} IS NULL OR LOWER(HD.soHopDong) LIKE LOWER(CONCAT('%', :#{#param.soHopDong}, '%'))) " +
            "AND (:#{#param.tenHopDong} IS NULL OR LOWER(HD.tenHopDong) LIKE LOWER(CONCAT('%', :#{#param.tenHopDong}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR HD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.soQdKq} IS NULL OR LOWER(HD.soQdKq) LIKE LOWER(CONCAT('%', :#{#param.soQdKq}, '%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY HD.nam DESC, HD.ngaySua DESC, HD.ngayTao DESC, HD.id DESC")
    Page<XhHopDongHdr> searchPage(@Param("param") XhHopDongHdrReq param, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM XH_HOP_DONG_HDR " +
            "WHERE TRANG_THAI = '30' " +
            "AND ID_QD_KQ = :idQdKq " +
            "AND LOAI_VTHH = :loaiVthh " +
            "AND MA_DVI = :maDvi",
            nativeQuery = true)
    Integer countSlHopDongDaKy(@Param("idQdKq") Long idQdKq,
                               @Param("loaiVthh") String loaiVthh,
                               @Param("maDvi") String maDvi);

    boolean existsBySoHopDong(String soHopDong);

    boolean existsBySoPhuLuc(String soPhuLuc);

    boolean existsBySoHopDongAndIdNot(String soHopDong, Long id);

    boolean existsBySoPhuLucAndIdNot(String soPhuLuc, Long id);

    List<XhHopDongHdr> findByIdIn(Collection<Long> ids);

    List<XhHopDongHdr> findAllByIdHopDong(Long idHopDong);

    List<XhHopDongHdr> findAllByIdQdKq(Long idQdKq);
}
