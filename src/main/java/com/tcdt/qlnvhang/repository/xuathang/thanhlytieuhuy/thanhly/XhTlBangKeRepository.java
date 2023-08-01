package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlBangKeRepository extends JpaRepository<XhTlBangKeHdr, Long> {

    @Query("SELECT BK FROM XhTlBangKeHdr BK " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR BK.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR BK.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(BK.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soBbQd} IS NULL OR LOWER(BK.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
            "AND ((:#{#param.ngayXuatKhoTu}  IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoTu})" +
            "AND (:#{#param.ngayXuatKhoDen}  IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}))" +
            "AND (:#{#param.loaiVthh} IS NULL OR LOWER(BK.loaiVthh) LIKE CONCAT('%',LOWER(:#{#param.loaiVthh}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR BK.trangThai = :#{#param.trangThai}) " +
            "ORDER BY BK.ngaySua desc , BK.ngayTao desc, BK.id desc"
    )
    Page<XhTlBangKeHdr> search(@Param("param") XhTlBangKeHdrReq param, Pageable pageable);

    List<XhTlBangKeHdr> findByIdIn(List<Long> ids);

    List<XhTlBangKeHdr> findAllByIdIn(List<Long> listId);
}