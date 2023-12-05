package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlQuyetDinhPdKqHdrRepository extends JpaRepository<XhTlQuyetDinhPdKqHdr, Long> {

    @Query("SELECT c FROM XhTlQuyetDinhPdKqHdr c " +
            " WHERE 1=1 " +
            "AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
            "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhTlQuyetDinhPdKqHdr> search(@Param("param") XhTlQuyetDinhPdKqHdrReq param, Pageable pageable);

    @Query("SELECT c FROM XhTlQuyetDinhPdKqHdr c " +
            " WHERE 1=1 " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    List<XhTlQuyetDinhPdKqHdr> searchAll(@Param("param") XhTlQuyetDinhPdKqHdrReq param);

    @Query("SELECT c FROM XhTlQuyetDinhPdKqHdr c " +
            " WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    List<XhTlQuyetDinhPdKqHdr> searchListTaoHopDong(@Param("param") XhTlQuyetDinhPdKqHdrReq param);


    void deleteAllByIdIn(List<Long> listId);

    List<XhTlQuyetDinhPdKqHdr> findByIdIn(List<Long> ids);

    List<XhTlQuyetDinhPdKqHdr> findAllByIdIn(List<Long> listId);

    Optional<XhTlQuyetDinhPdKqHdr> findBySoQd(String soQd);

    Optional<XhTlQuyetDinhPdKqHdr> findFirstBySoQd(String soQd);
}
