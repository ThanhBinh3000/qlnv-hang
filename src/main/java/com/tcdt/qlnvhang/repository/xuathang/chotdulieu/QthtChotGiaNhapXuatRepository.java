package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QthtChotGiaNhapXuatRepository extends JpaRepository<QthtChotGiaNhapXuat, Long> {


    @Query(value = "SELECT q FROM QthtChotGiaNhapXuat q " +
            " LEFT JOIN UserInfo u on u.id = q.nguoiTaoId" +
            " WHERE 1 = 1 " +
            " AND (:#{#param.type} IS NULL OR q.type = :#{#param.type}) " +
            " AND (:#{#param.ngayChotSr} IS NULL OR q.ngayChot >= :#{#param.ngayChotSr}) " +
            " AND (:#{#param.ngayHlucSr} IS NULL OR q.ngayHluc >= :#{#param.ngayHlucSr}) " +
            " AND (:#{#param.ngayHuySr} IS NULL OR q.ngayHuy >= :#{#param.ngayHuySr}) " +
            " AND (:#{#param.taiKhoanSr} IS NULL OR lower(u.fullName) LIKE lower(concat('%',CONCAT(:#{#param.taiKhoanSr},'%'))))" +
            " ORDER BY q.ngaySua desc , q.ngayTao desc, q.id desc "
    )
    Page<QthtChotGiaNhapXuat> searchPage(@Param("param") QthtChotGiaNhapXuatReq req, Pageable pageable);

    @Query(value = "SELECT q FROM QthtChotGiaNhapXuat q " +
            " WHERE 1 = 1 " +
            " AND (:#{#param.type} IS NULL OR q.type = :#{#param.type}) " +
            " AND (TRUNC(:#{#param.ngayChotSr}) BETWEEN TRUNC(q.ngayChot) AND TRUNC(q.ngayHluc)) " +
            " AND (:#{#param.maDvi} IS NULL OR q.maDonVi LIKE LOWER(CONCAT(CONCAT('%', :#{#param.maDvi}),'%')) OR q.maDonVi LIKE LOWER(CONCAT(:#{#param.maDvi},'%'))  OR q.maDonVi LIKE LOWER(CONCAT('%', :#{#param.maDvi})))" +
            " ORDER BY q.ngaySua desc , q.ngayTao desc, q.id desc "
    )
    List<QthtChotGiaNhapXuat> findBetweenNgayChotAndHieuLuc(@Param("param") QthtChotGiaNhapXuatReq req);

}
