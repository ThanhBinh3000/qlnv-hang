package com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhHSoKthuatBttHdrRepository extends JpaRepository<XhHSoKthuatBttHdr, Long> {

    @Query("SELECT DX from XhHSoKthuatBttHdr DX WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.maDiemKho} IS NULL OR DX.maDiemKho = :#{#param.maDiemKho})" +
            "AND (:#{#param.maNhaKho} IS NULL OR DX.maNhaKho = :#{#param.maNhaKho})" +
            "AND (:#{#param.maNganKho} IS NULL OR DX.maNganKho = :#{#param.maNganKho})" +
            "AND (:#{#param.maLoKho} IS NULL OR DX.maLoKho = :#{#param.maLoKho})" +
            "AND (:#{#param.soHoSoKyThuat} IS NULL OR LOWER(DX.soHoSoKyThuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHoSoKyThuat}),'%' ) ) )" +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<XhHSoKthuatBttHdr> searchPage(@Param("param") XhHSoKthuatBttHdrReq param, Pageable pageable);

    List<XhHSoKthuatBttHdr> findAllByIdIn(List<Long> ids);

}
