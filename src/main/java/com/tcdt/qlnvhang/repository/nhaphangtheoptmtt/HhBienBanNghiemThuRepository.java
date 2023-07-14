package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.SearchQuyChuanQgReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhBbNghiemThu;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanNghiemThu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhBienBanNghiemThuRepository extends JpaRepository<HhBienBanNghiemThu,Long> {

    @Query(value = "select c from HhBienBanNghiemThu c  WHERE 1=1 " +
            " AND (:#{#param.namKh} IS NULL OR  c.namKh = :#{#param.namKh}) " +
            " AND (:#{#param.soBb} IS NULL OR LOWER(c.soBb) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBb}),'%' ) ) )" +
            " AND (:#{#param.soQd}  IS NULL OR LOWER(c.soQdGiaoNvNh) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%' ) ) )" +
            " AND ((:#{#param.ngayKnTu}  IS NULL OR c.ngayPduyet >= :#{#param.ngayKnTu})" +
            " AND (:#{#param.ngayKnDen}  IS NULL OR c.ngayPduyet <= :#{#param.ngayKnDen}) ) " +
            " AND (:#{#param.trangThai} IS NULL OR LOWER(c.trangThai)=:#{#param.trangThai} )"
//            " AND (:#{#param.maDvi} IS NULL OR LOWER(c.maDvi) LIKE LOWER(CONCAT(:#{#param.maDvi},'%')))  "
    )
            Page<HhBienBanNghiemThu> searchPage (@Param("param") SearchHhBbNghiemThu param, Pageable pageable);

    List<HhBienBanNghiemThu> findAllByIdIn(List<Long> ids);

    Optional<HhBienBanNghiemThu> findAllBySoBb(String soBb);

    List<HhBienBanNghiemThu> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);
    List<HhBienBanNghiemThu> findByIdDdiemGiaoNvNh(Long idDdiemGnvNh);


}
