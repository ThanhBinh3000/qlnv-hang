package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbLayMauRepository extends BaseRepository<XhBbLayMau, Long> {

    @Query("SELECT DISTINCT QD FROM XhBbLayMau QD " +
            "WHERE (:#{#param.dvql} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR QD.soBbLayMau = :#{#param.soBbLayMau}) " +
            "AND (:#{#param.soQdNv} IS NULL OR QD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.donViKnghiem} IS NULL OR LOWER(QD.donViKnghiem) LIKE LOWER(CONCAT('%', :#{#param.donViKnghiem}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR QD.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.ngayLayMauTu} IS NULL OR QD.ngayLayMau >= :#{#param.ngayLayMauTu}) " +
            "AND (:#{#param.ngayLayMauDen} IS NULL OR QD.ngayLayMau <= :#{#param.ngayLayMauDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR QD.trangThai = :#{#param.trangThai}) " +
            "ORDER BY QD.ngaySua DESC, QD.ngayTao DESC, QD.id DESC")
    Page<XhBbLayMau> searchPage(@Param("param") XhBbLayMauRequest param, Pageable pageable);

    boolean existsBySoBbLayMau(String soBbLayMau);

    boolean existsBySoBbLayMauAndIdNot(String soBbLayMau, Long id);

    List<XhBbLayMau> findByIdIn(List<Long> idList);

    List<XhBbLayMau> findAllByIdIn(List<Long> listId);
}
