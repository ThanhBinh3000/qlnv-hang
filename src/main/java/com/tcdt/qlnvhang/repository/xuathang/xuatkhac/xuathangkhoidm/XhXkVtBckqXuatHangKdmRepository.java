package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMauRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkVtBckqXuatHangKdmRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMau;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkVtBckqXuatHangKdm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBckqXuatHangKdmRepository extends JpaRepository<XhXkVtBckqXuatHangKdm, Long> {

    @Query("SELECT c FROM XhXkVtBckqXuatHangKdm c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBaoCao} IS NULL OR LOWER(c.soBaoCao) LIKE CONCAT('%',LOWER(:#{#param.soBaoCao}),'%')) " +
            "AND (:#{#param.maDsTh} IS NULL OR LOWER(c.maDsTh) LIKE CONCAT('%',LOWER(:#{#param.maDsTh}),'%')) " +
            "AND ((:#{#param.ngayBaoCaoTu}  IS NULL OR c.ngayBaoCao >= :#{#param.ngayBaoCaoTu})" +
            "AND (:#{#param.ngayBaoCaoDen}  IS NULL OR c.ngayBaoCao <= :#{#param.ngayBaoCaoDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtBckqXuatHangKdm> search(@Param("param") XhXkVtBckqXuatHangKdmRequest param, Pageable pageable);

    Optional<XhXkVtBckqXuatHangKdm> findBySoBaoCao(String soBaoCao);

    List<XhXkVtBckqXuatHangKdm> findByIdIn(List<Long> ids);

    List<XhXkVtBckqXuatHangKdm> findAllByIdIn(List<Long> idList);

}
