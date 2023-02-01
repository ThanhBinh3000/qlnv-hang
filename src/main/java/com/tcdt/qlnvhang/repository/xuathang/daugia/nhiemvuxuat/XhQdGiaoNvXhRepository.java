package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhQdGiaoNvXhRepository extends BaseRepository<XhQdGiaoNvXh, Long> {
    @Query(value = "select * from XH_QD_GIAO_NV_XH HDR where " +
            "(:nam IS NULL OR HDR.NAM = TO_NUMBER(:nam)) " +
            "AND (:soQd IS NULL OR LOWER(HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%' ) ) )" +
            "AND (:loaiVthh IS NULL OR LOWER(HDR.LOAI_VTHH) LIKE LOWER(CONCAT(CONCAT('%',:loaiVthh),'%' ) ) )" +
            "AND (:trichYeu IS NULL OR LOWER(HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%' ) ) )" +
            "AND (:ngayTaoTu IS NULL OR HDR.NGAY_TAO >=  TO_DATE(:ngayTaoTu,'yyyy-MM-dd')) " +
            "AND (:ngayTaoDen IS NULL OR HDR.NGAY_TAO <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai)" +
            "AND (:maDvi IS NULL OR LOWER(HDR.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<XhQdGiaoNvXh> searchPage(Integer nam, String soQd, String loaiVthh, String trichYeu, String ngayTaoTu, String ngayTaoDen, String trangThai, String maDvi, Pageable pageable);

    Optional<XhQdGiaoNvXh> findAllBySoQd(String soQd);

    List<XhQdGiaoNvXh> findAllByIdIn(List<Long> ids);
}
