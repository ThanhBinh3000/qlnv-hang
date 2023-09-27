package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdGiaoNvXhRepository extends BaseRepository<XhQdGiaoNvXh, Long> {

    @Query("SELECT DISTINCT QD FROM XhQdGiaoNvXh QD " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNv} IS NULL OR QD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(QD.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.ngayKyTu} IS NULL OR QD.ngayKy >= :#{#param.ngayKyTu}) " +
            "AND (:#{#param.ngayKyDen} IS NULL OR QD.ngayKy <= :#{#param.ngayKyDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.nam DESC, QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhQdGiaoNvXh> searchPage(@Param("param") XhQdGiaoNvuXuatReq param, Pageable pageable);

    boolean existsBySoQdNv(String soQdNv);

    boolean existsBySoQdNvAndIdNot(String soQdNv, Long id);

    List<XhQdGiaoNvXh> findByIdIn(List<Long> idList);

    List<XhQdGiaoNvXh> findAllByIdIn(List<Long> listId);
}
