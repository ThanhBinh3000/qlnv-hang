package com.tcdt.qlnvhang.repository.xuathang.chotdulieu;

import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.request.chotdulieu.QthtKetChuyenReq;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenDtl;
import com.tcdt.qlnvhang.table.chotdulieu.QthtKetChuyenHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QthtKetChuyenDtlRepository extends JpaRepository<QthtKetChuyenDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<QthtKetChuyenDtl> findAllByIdHdr(Long idHdr);

    @Query("SELECT distinct c FROM QthtKetChuyenDtl c " +
            " left join QthtKetChuyenHdr hdr on c.idHdr = hdr.id " +
            " WHERE 1=1 " +
            " AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDonVi LIKE CONCAT(:#{#param.maDviSr},'%')) " +
            " AND (:#{#param.maDvi} IS NULL OR c.maDonVi LIKE CONCAT(:#{#param.maDvi},'%')) "
    )
    Page<QthtKetChuyenDtl> searchPage(@Param("param") QthtKetChuyenReq param, Pageable pageable);

}
