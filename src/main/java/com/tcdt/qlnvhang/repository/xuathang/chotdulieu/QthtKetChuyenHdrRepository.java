package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtKetChuyenReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QthtKetChuyenHdrRepository extends JpaRepository<QthtKetChuyenHdr, Long> {

    @Query("SELECT c FROM QthtKetChuyenHdr c " +
            " WHERE 1=1 " +
            "AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<QthtKetChuyenHdr> searchPage(@Param("param") QthtKetChuyenReq param, Pageable pageable);

    @Query("SELECT c FROM QthtKetChuyenHdr c " +
            " WHERE 1=1 " +
            " AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<QthtKetChuyenHdr> searchList(@Param("param") QthtKetChuyenReq param);

    Optional<QthtKetChuyenHdr> findByNamAndMaDvi(Integer nam,String maDvi);

}
