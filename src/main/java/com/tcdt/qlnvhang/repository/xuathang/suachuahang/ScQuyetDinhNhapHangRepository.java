package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhNhapHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScQuyetDinhNhapHangRepository extends JpaRepository<ScQuyetDinhNhapHang, Long> {

    @Query(value = "SELECT c FROM ScQuyetDinhNhapHang c WHERE 1=1 " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            " AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            " AND (:#{#param.trichYeu} IS NULL OR c.trichYeu LIKE CONCAT(:#{#param.trichYeu},'%')) ")
    Page<ScQuyetDinhNhapHang> searchPage(@Param("param") ScQuyetDinhNhapHangReq req, Pageable pageable);

    @Query(value = "SELECT a FROM ScQuyetDinhNhapHang a WHERE 1=1 " +
            " AND (:#{#param.trangThai} IS NULL OR a.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.nam} IS NULL OR a.nam = :#{#param.nam} )" +
            "AND (:#{#param.soQd} IS NULL OR a.soQd LIKE CONCAT(:#{#param.soQd},'%'))")
    Page<ScQuyetDinhNhapHang> searchPageViewFromAnother(@Param("param") ScQuyetDinhNhapHangReq req, Pageable pageable);

    @Query(value = "SELECT c FROM ScQuyetDinhNhapHang c " +
            " WHERE 1 = 1 " +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi = :#{#param.maDviSr}) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
    )
    List<ScQuyetDinhNhapHang> listTaoPhieuNhapKho(@Param("param") ScQuyetDinhNhapHangReq param);


    List<ScQuyetDinhNhapHang> findAllByIdQdXh(Long idQdXh);


}
