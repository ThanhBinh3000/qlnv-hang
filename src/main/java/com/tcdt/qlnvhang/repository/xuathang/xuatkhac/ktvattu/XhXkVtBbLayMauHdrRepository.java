package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBbLayMau;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBbLayMauHdrRepository extends JpaRepository<XhXkVtBbLayMauHdr, Long> {

    @Query("SELECT c FROM XhXkVtBbLayMauHdr c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
            "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
            "AND (:#{#param.dviKiemNghiem} IS NULL OR LOWER(c.dviKiemNghiem) LIKE CONCAT('%',LOWER(:#{#param.dviKiemNghiem}),'%')) " +
            "AND ((:#{#param.ngayLayMauTu}  IS NULL OR c.ngayLayMau >= :#{#param.ngayLayMauTu})" +
            "AND (:#{#param.ngayLayMauDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayLayMauDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtBbLayMauHdr> search(@Param("param") XhXkVtBbLayMauRequest param, Pageable pageable);

    Optional<XhXkVtBbLayMauHdr> findBySoBienBan(String soBienBan);

    List<XhXkVtBbLayMauHdr> findByIdIn(List<Long> ids);

    List<XhXkVtBbLayMauHdr> findAllByIdIn(List<Long> idList);

    List<XhXkVtBbLayMauHdr> findByIdQdGiaoNvXh(Long ids);

}
