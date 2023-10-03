package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlHopDongHdrRepository extends JpaRepository<XhTlHopDongHdr, Long> {

    @Query("SELECT DISTINCT  HD FROM XhTlHopDongHdr HD " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdKqTl} IS NULL OR LOWER(HD.soQdKqTl) LIKE CONCAT('%',LOWER(:#{#param.soQdKqTl}),'%')) " +
            "AND (:#{#param.soQdTl} IS NULL OR LOWER(HD.soQdTl) LIKE CONCAT('%',LOWER(:#{#param.soQdTl}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY HD.ngaySua desc , HD.ngayTao desc, HD.id desc"
    )
    Page<XhTlHopDongHdr> search(@Param("param") XhTlHopDongHdrReq param, Pageable pageable);

    @Query("SELECT DISTINCT  HD FROM XhTlHopDongHdr HD " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
            "AND (:#{#param.idQdKqTl} IS NULL OR HD.idQdKqTl = :#{#param.idQdKqTl}) " +
            "AND (:#{#param.soQdKqTl} IS NULL OR LOWER(HD.soQdKqTl) LIKE CONCAT('%',LOWER(:#{#param.soQdKqTl}),'%')) " +
            "AND (:#{#param.soQdTl} IS NULL OR LOWER(HD.soQdTl) LIKE CONCAT('%',LOWER(:#{#param.soQdTl}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY HD.ngaySua desc , HD.ngayTao desc, HD.id desc"
    )
    List<XhTlHopDongHdr> searchAll(@Param("param") XhTlHopDongHdrReq param);

    @Query("SELECT DISTINCT  HD FROM XhTlHopDongHdr HD " +
            " LEFT JOIN XhTlQdGiaoNvHdr qd on HD.id = qd.idHopDong " +
            " WHERE 1=1 " +
            " AND qd.id is null " +
            "AND (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY HD.ngaySua desc , HD.ngayTao desc, HD.id desc"
    )
    List<XhTlHopDongHdr> dsTaoQdGiaoNvXh(@Param("param") XhTlHopDongHdrReq param);

    Optional<XhTlHopDongHdr> findBySoHd(String soHd);

    List<XhTlHopDongHdr> findByIdIn(List<Long> ids);

    List<XhTlHopDongHdr> findAllByIdIn(List<Long> listId);

    List<XhTlHopDongHdr> findAllByIdQdKqTl(Long idQdKqTl);
}
