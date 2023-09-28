package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhThopDxKhBdgRepository extends JpaRepository<XhThopDxKhBdg, Long> {

    @Query("SELECT DISTINCT TH FROM XhThopDxKhBdg TH WHERE " +
            "(:#{#param.dvql} IS NULL OR TH.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh}, '%')) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(TH.noiDungThop) LIKE LOWER(CONCAT('%', :#{#param.noiDungThop}, '%'))) " +
            "AND (:#{#param.ngayThopTu} IS NULL OR TH.ngayThop >= :#{#param.ngayThopTu}) " +
            "AND (:#{#param.ngayThopDen} IS NULL OR TH.ngayThop <= :#{#param.ngayThopDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR TH.trangThai = :#{#param.trangThai}) " +
            "ORDER BY TH.namKh DESC, TH.ngaySua DESC, TH.ngayTao DESC, TH.id DESC")
    Page<XhThopDxKhBdg> searchPage(@Param("param") SearchXhThopDxKhBdg param, Pageable pageable);

    List<XhThopDxKhBdg> findAllByIdIn(List<Long> ids);

    List<XhThopDxKhBdg> findByIdIn(List<Long> ids);
}
