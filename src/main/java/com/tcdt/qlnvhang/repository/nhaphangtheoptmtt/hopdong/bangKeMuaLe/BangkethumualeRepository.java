package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.bangKeMuaLe;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.bangkethumuale.BangKeMuaLeReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.bangkethumuale.BangKeMuaLe;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BangkethumualeRepository extends JpaRepository<BangKeMuaLe,Long> {
    @Query("SELECT c FROM BangKeMuaLe c WHERE 1=1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.namQd} IS NULL OR c.namQd = :#{#param.namQd}) " +
             "AND ((:#{#param.ngayMuaTu}  IS NULL OR c.ngayMua >= :#{#param.ngayMuaTu})" +
            "AND (:#{#param.ngayMuaDen}  IS NULL OR c.ngayMua <= :#{#param.ngayMuaDen}) ) " +
            "AND (:#{#param.nguoiBan}  IS NULL OR LOWER(c.nguoiBan) LIKE CONCAT('%',LOWER(:#{#param.nguoiBan}),'%')) " +
            "AND (:#{#param.soBangKe}  IS NULL OR LOWER(c.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
            "AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
            "AND (:#{#param.soQdGiaoNvNh == null} = true OR c.soQdGiaoNvNh in :#{#param.soQdGiaoNvNh}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<BangKeMuaLe> search(@Param("param") BangKeMuaLeReq param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<BangKeMuaLe> findByIdIn(List<Long> ids);

    Optional<BangKeMuaLe> findBySoBangKe(String soBk);

    List<BangKeMuaLe> findAllByIdIn(List<Long> listId);
}
