package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhBbLayMauBttHdrRepository extends JpaRepository<XhBbLayMauBttHdr, Long> {

    @Query("SELECT DISTINCT LM FROM XhBbLayMauBttHdr LM " +
            "LEFT JOIN XhQdNvXhBttHdr QD ON QD.id = LM.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR LM.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR LM.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LM.soBbLayMau = :#{#param.soBbLayMau}) " +
            "AND (:#{#param.soQdNv} IS NULL OR LM.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.donViKnghiem} IS NULL OR LOWER(LM.donViKnghiem) LIKE LOWER(CONCAT('%', :#{#param.donViKnghiem}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR LM.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.ngayLayMauTu} IS NULL OR LM.ngayLayMau >= :#{#param.ngayLayMauTu}) " +
            "AND (:#{#param.ngayLayMauDen} IS NULL OR LM.ngayLayMau <= :#{#param.ngayLayMauDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR LM.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY LM.namKh DESC, LM.ngaySua DESC, LM.ngayTao DESC, LM.id DESC")
    Page<XhBbLayMauBttHdr> searchPage(@Param("param") XhBbLayMauBttHdrReq param, Pageable pageable);

    boolean existsBySoBbLayMau(String soBbLayMau);

    boolean existsBySoBbLayMauAndIdNot(String soBbLayMau, Long id);

    List<XhBbLayMauBttHdr> findByIdIn(List<Long> idList);

    List<XhBbLayMauBttHdr> findAllByIdIn(List<Long> listId);
}