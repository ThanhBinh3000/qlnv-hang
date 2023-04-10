package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
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

    @Query("SELECT c FROM XhBbLayMauBttHdr c " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBienBan}),'%' ) ) )" +
            "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%' ) ) )" +
            "AND (:#{#param.dviKnghiem} IS NULL OR LOWER(c.dviKnghiem) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.dviKnghiem}),'%'))) " +
            "AND (:#{#param.ngayLayMauTu} IS NULL OR c.ngayLayMau >= :#{#param.ngayLayMauTu}) " +
            "AND (:#{#param.ngayLayMauDen} IS NULL OR c.ngayLayMau <= :#{#param.ngayLayMauDen}) " +
            "AND (:#{#param.namKh} IS NULL OR c.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    Page<XhBbLayMauBttHdr> searchPage(@Param("param") XhBbLayMauBttHdrReq param, Pageable pageable);

    List<XhBbLayMauBttHdr> findAllByIdQd(Long idQd);

    List<XhBbLayMauBttHdr> findByIdIn(List<Long> idList);

    Optional<XhBbLayMauBttHdr> findBySoBienBan(String soBienBan);
}
