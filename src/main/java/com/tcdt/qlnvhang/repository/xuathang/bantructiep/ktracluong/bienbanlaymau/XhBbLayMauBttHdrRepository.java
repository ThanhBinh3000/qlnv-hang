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

    @Query("SELECT LM from XhBbLayMauBttHdr LM WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR LM.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(LM.soBienBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBienBan}),'%' ) ) )" +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(LM.soQdNv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdNv}),'%' ) ) )" +
            "AND (:#{#param.dviKnghiem} IS NULL OR LOWER(LM.dviKnghiem) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.dviKnghiem}),'%'))) " +
            "AND (:#{#param.ngayLayMauTu} IS NULL OR LM.ngayLayMau >= :#{#param.ngayLayMauTu}) " +
            "AND (:#{#param.ngayLayMauDen} IS NULL OR LM.ngayLayMau <= :#{#param.ngayLayMauDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR LM.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR LM.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR LM.maDvi = :#{#param.maDvi})")
    Page<XhBbLayMauBttHdr> searchPage(@Param("param") XhBbLayMauBttHdrReq param, Pageable pageable);

    List<XhBbLayMauBttHdr> findByIdIn(List<Long> idList);

    Optional<XhBbLayMauBttHdr> findBySoBienBan(String soBienBan);
}
