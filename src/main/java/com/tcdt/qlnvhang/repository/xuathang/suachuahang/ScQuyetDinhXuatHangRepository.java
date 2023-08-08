package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScQuyetDinhXuatHangRepository extends JpaRepository<ScQuyetDinhXuatHang,Long> {

    @Query(value = "SELECT a FROM ScQuyetDinhXuatHang a WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam} )" +
            "AND (:#{#param.soQd} IS NULL OR a.soQd LIKE CONCAT(:#{#param.soQd},'%'))" +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(a.trichYeu) LIKE LOWER(CONCAT('%',CONCAT(:#{#param.trichYeu},'%')))) " +
            "AND ((:#{#param.ngayTu}  IS NULL OR a.ngayKy >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR a.ngayKy <= :#{#param.ngayDen})) " +
            "AND (:#{#param.trangThai} IS NULL OR a.trangThai = :#{#param.trangThai} )" +
            " ORDER BY a.ngaySua desc , a.ngayTao desc, a.id desc "
    )
    Page<ScQuyetDinhXuatHang> searchPage(@Param("param") ScQuyetDinhXuatHangReq req, Pageable pageable);

    @Query(value = "SELECT a FROM ScQuyetDinhXuatHang a WHERE 1=1 " +
            " AND (:#{#param.trangThai} IS NULL OR a.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam} )" +
            "AND (:#{#param.maDviSr} IS NULL OR a.maDvi = :#{#param.maDviSr} )" +
            "AND (:#{#param.trangThai} IS NULL OR a.trangThai = :#{#param.trangThai} )" +
            "AND (:#{#param.soQd} IS NULL OR a.soQd LIKE CONCAT(:#{#param.soQd},'%'))" +
            "AND ((:#{#param.ngayTu}  IS NULL OR a.thoiHanXuat >= :#{#param.ngayTu}) AND (:#{#param.ngayDen}  IS NULL OR a.thoiHanXuat <= :#{#param.ngayDen})) " +
            " ORDER BY a.ngaySua desc , a.ngayTao desc, a.id desc "
    )
    Page<ScQuyetDinhXuatHang> searchPageViewFromPhieuXuatKho(@Param("param") ScQuyetDinhXuatHangReq req, Pageable pageable);

    @Query(value = "SELECT c FROM ScQuyetDinhXuatHang c " +
            " WHERE 1 = 1 " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi = :#{#param.maDviSr}) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
    )
    List<ScQuyetDinhXuatHang> listTaoPhieuXuatKho(@Param("param") ScQuyetDinhXuatHangReq param);

    @Query(value = "SELECT distinct xh FROM ScQuyetDinhXuatHang xh " +
            " LEFT JOIN ScQuyetDinhNhapHang nh on xh.id = nh.idQdXh " +
            " LEFT JOIN ScBaoCaoHdr bc on xh.id = bc.idQdXh " +
            " WHERE 1 = 1 " +
            " AND nh.id is not null " +
            " AND bc.id is null " +
            " AND (:#{#param.trangThai} IS NULL OR xh.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.trangThai} IS NULL OR nh.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.maDviSr} IS NULL OR xh.maDvi = :#{#param.maDviSr}) " +
            " AND (:#{#param.maDviSr} IS NULL OR nh.maDvi = :#{#param.maDviSr}) " +
            " ORDER BY xh.ngaySua desc , xh.ngayTao desc, xh.id desc "
    )
    List<ScQuyetDinhXuatHang> listTaoBaoCao(@Param("param") ScQuyetDinhXuatHangReq param);

    Optional<ScQuyetDinhXuatHang> findBySoQd(String soQd);

}
