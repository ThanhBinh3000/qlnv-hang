package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlXuatKhoHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlXuatKhoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlXuatKhoRepository extends JpaRepository<XhTlXuatKhoHdr, Long> {

    @Query("SELECT XK FROM XhTlXuatKhoHdr XK " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR XK.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR XK.nam = :#{#param.nam}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR LOWER(XK.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soPhieuXuatKho}),'%')) " +
            "AND (:#{#param.soBbQd} IS NULL OR LOWER(XK.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
            "AND ((:#{#param.ngayXuatKhoTu}  IS NULL OR XK.ngayXuatKho >= :#{#param.ngayXuatKhoTu})" +
            "AND (:#{#param.ngayXuatKhoDen}  IS NULL OR XK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}))" +
            "AND (:#{#param.loaiVthh} IS NULL OR LOWER(XK.loaiVthh) LIKE CONCAT('%',LOWER(:#{#param.loaiVthh}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR XK.trangThai = :#{#param.trangThai}) " +
            "ORDER BY XK.ngaySua desc , XK.ngayTao desc, XK.id desc"
    )
    Page<XhTlXuatKhoHdr> search(@Param("param") XhTlXuatKhoHdrReq param, Pageable pageable);

    List<XhTlXuatKhoHdr> findByIdIn(List<Long> ids);

    List<XhTlXuatKhoHdr> findAllByIdIn(List<Long> listId);
}