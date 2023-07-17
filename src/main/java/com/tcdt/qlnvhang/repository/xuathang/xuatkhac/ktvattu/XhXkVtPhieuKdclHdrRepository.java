package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtPhieuKdclHdrRepository extends JpaRepository<XhXkVtPhieuKdclHdr, Long> {

    @Query("SELECT c FROM XhXkVtPhieuKdclHdr c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
            "AND (:#{#param.dviKiemNghiem} IS NULL OR LOWER(c.dviKiemNghiem) LIKE CONCAT('%',LOWER(:#{#param.dviKiemNghiem}),'%')) " +
            "AND ((:#{#param.ngayKiemDinhTu}  IS NULL OR c.ngayLapPhieu >= :#{#param.ngayKiemDinhTu})" +
            "AND (:#{#param.ngayKiemDinhDen}  IS NULL OR c.ngayLapPhieu <= :#{#param.ngayKiemDinhDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtPhieuKdclHdr> search(@Param("param") XhXkVtPhieuKdclRequest param, Pageable pageable);

    Optional<XhXkVtPhieuKdclHdr> findBySoPhieu(String soPhieu);

    List<XhXkVtPhieuKdclHdr> findByIdIn(List<Long> ids);

    List<XhXkVtPhieuKdclHdr> findAllByIdIn(List<Long> idList);

    List<XhXkVtPhieuKdclHdr> findByIdQdGiaoNvXh(Long ids);

}
