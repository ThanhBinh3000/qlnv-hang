package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttHdrRepository extends JpaRepository<XhQdPdKhBttHdr, Long> {

    @Query("SELECT DISTINCT QD FROM XhQdPdKhBttHdr QD " +
            "LEFT JOIN XhQdPdKhBttDtl DTL ON QD.id = DTL.idHdr " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdPd} IS NULL OR LOWER(QD.soQdPd) LIKE LOWER(CONCAT('%', :#{#param.soQdPd}, '%'))) " +
            "AND (:#{#param.soQdDc} IS NULL OR LOWER(QD.soQdDc) LIKE LOWER(CONCAT('%', :#{#param.soQdDc}, '%'))) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.ngayKyQdTu} IS NULL OR QD.ngayKyQd >= :#{#param.ngayKyQdTu}) " +
            "AND (:#{#param.ngayKyQdDen} IS NULL OR QD.ngayKyQd <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.ngayKyDcTu} IS NULL OR QD.ngayKyDc >= :#{#param.ngayKyDcTu}) " +
            "AND (:#{#param.ngayKyDcDen} IS NULL OR QD.ngayKyDc <= :#{#param.ngayKyDcDen}) " +
            "AND (:#{#param.soTrHdr} IS NULL OR LOWER(QD.soTrHdr) LIKE LOWER(CONCAT('%', :#{#param.soTrHdr}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.type} IS NULL OR LOWER(QD.type) LIKE LOWER(CONCAT('%', :#{#param.type}, '%'))) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(QD.lastest) LIKE LOWER(CONCAT('%', :#{#param.lastest}, '%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maCuc} IS NULL OR DTL.maDvi LIKE CONCAT(:#{#param.maCuc}, '%')) " +
            "ORDER BY QD.namKh DESC, QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhQdPdKhBttHdr> searchPage(@Param("param") XhQdPdKhBttHdrReq param, Pageable pageable);

    boolean existsBySoQdPd(String soQdPd);

    boolean existsBySoQdPdAndIdNot(String soQdPd, Long id);

    List<XhQdPdKhBttHdr> findByIdIn(List<Long> idQdList);

    List<XhQdPdKhBttHdr> findAllByIdIn(List<Long> listId);

    boolean existsBySoQdDc(String soQdDc);

    boolean existsBySoQdDcAndIdNot(String soQdDc, Long id);

    long countBySoQdPdAndType(String soQdPd, String type);
}