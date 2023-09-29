package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDgPhieuXuatKhoRepository extends JpaRepository<XhDgPhieuXuatKho, Long> {

    @Query("SELECT DISTINCT QD FROM XhDgPhieuXuatKho QD " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR QD.soPhieuXuatKho = :#{#param.soPhieuXuatKho}) " +
            "AND (:#{#param.soQdNv} IS NULL OR QD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.ngayLapPhieuTu} IS NULL OR QD.ngayLapPhieu >= :#{#param.ngayLapPhieuTu}) " +
            "AND (:#{#param.ngayLapPhieuDen} IS NULL OR QD.ngayLapPhieu <= :#{#param.ngayLapPhieuDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhDgPhieuXuatKho> searchPage(@Param("param") XhDgPhieuXuatKhoReq param, Pageable pageable);

    boolean existsBySoPhieuXuatKho(String soPhieuXuatKho);

    boolean existsBySoPhieuXuatKhoAndIdNot(String soPhieuXuatKho, Long id);

    List<XhDgPhieuXuatKho> findByIdIn(List<Long> idList);

    List<XhDgPhieuXuatKho> findAllByIdIn(List<Long> listId);

}
