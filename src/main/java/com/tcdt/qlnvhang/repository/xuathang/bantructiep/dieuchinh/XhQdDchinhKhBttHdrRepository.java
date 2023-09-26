package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttHdrRepository extends JpaRepository<XhQdDchinhKhBttHdr, Long> {

    @Query("SELECT DISTINCT DX FROM XhQdDchinhKhBttHdr DX " +
            "WHERE (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdDc} IS NULL OR DX.soQdDc = :#{#param.soQdDc}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.ngayKyDcTu} IS NULL OR DX.ngayKyDc >= :#{#param.ngayKyDcTu}) " +
            "AND (:#{#param.ngayKyDcDen} IS NULL OR DX.ngayKyDc <= :#{#param.ngayKyDcDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "ORDER BY DX.ngaySua DESC, DX.ngayTao DESC, DX.id DESC")
    Page<XhQdDchinhKhBttHdr> searchPage(@Param("param") XhQdDchinhKhBttHdrReq param, Pageable pageable);

    boolean existsBySoQdDc(String soQdDc);

    boolean existsBySoQdDcAndIdNot(String soQdDc, Long id);

    List<XhQdDchinhKhBttHdr> findByIdIn(List<Long> idList);

    List<XhQdDchinhKhBttHdr> findAllByIdIn(List<Long> listId);

    long countByidQdPd(Long idQdPd);
}
