package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhQdGiaoNvXhRepository extends BaseRepository<XhQdGiaoNvXh, Long> {
    @Query("SELECT c FROM XhQdGiaoNvXh c " +
            " LEFT JOIN XhQdGiaoNvXhDtl dtl on c.id = dtl.idQdHdr " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maChiCuc} IS NULL OR dtl.maDvi = :#{#param.maChiCuc}) "
    )
    Page<XhQdGiaoNvXh> searchPage(@Param("param") XhQdGiaoNvuXuatReq param, Pageable pageable);

    Optional<XhQdGiaoNvXh> findAllBySoQd(String soQd);

    List<XhQdGiaoNvXh> findAllByIdIn(List<Long> ids);
}
