package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso.XhTlThongBaoKqHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso.XhTlThongBaoKqHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlThongBaoKqHdrRepository extends JpaRepository<XhTlThongBaoKqHdr, Long> {

    @Query("SELECT TB from XhTlThongBaoKqHdr TB WHERE 1 = 1 " +
            "AND (:#{#param.soThongBao} IS NULL OR LOWER(TB.soThongBao) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soThongBao}),'%' ) ) )" +
            "AND (:#{#param.soHoSo} IS NULL OR LOWER(TB.soHoSo) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHoSo}),'%' ) ) )" +
//            "AND (:#{#param.ngayKy} IS NULL OR TB.ngayKy >= :#{#param.ngayKyQdTu}) " +
//            "AND (:#{#param.ngayKyQdDen} IS NULL OR TB.ngayKy <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR TB.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR TB.maDvi = :#{#param.maDvi})")
    Page<XhTlThongBaoKqHdr> searchPage(@Param("param") XhTlThongBaoKqHdrReq param, Pageable pageable);

    Optional<XhTlThongBaoKqHdr> findBySoThongBao(String soThongBao);

    List<XhTlThongBaoKqHdr> findByIdIn(List<Long> idTbList);


}
