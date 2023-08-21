package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhThopDxKhBdgRepository extends JpaRepository<XhThopDxKhBdg, Long> {

    @Query("SELECT distinct c FROM XhThopDxKhBdg c left join c.children h WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR c.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR c.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(c.noiDungThop) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.noiDungThop}),'%'))) " +
            "AND ((:#{#param.ngayThopTu}  IS NULL OR c.ngayThop >= :#{#param.ngayThopTu})" +
            "AND  (:#{#param.ngayThopDen}  IS NULL OR c.ngayThop <= :#{#param.ngayThopDen})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhThopDxKhBdg> searchPage(@feign.Param("param") SearchXhThopDxKhBdg param, Pageable pageable);

    List<XhThopDxKhBdg> findAllByIdIn(List<Long> ids);

    List<XhThopDxKhBdg> findByIdIn(List<Long> ids);
}
