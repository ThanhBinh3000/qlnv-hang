package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHaoDoiHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHaoDoiHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlHaoDoiHdrRepository extends JpaRepository<XhTlHaoDoiHdr, Long> {

//    @Query("SELECT HD FROM XhTlHaoDoiHdr HD " +
//            " WHERE 1=1 " +
//            "AND (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
//            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
//            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(HD.soBbHaoDoi) LIKE CONCAT('%',LOWER(:#{#param.soBbHaoDoi}),'%')) " +
//            "AND (:#{#param.soBbQd} IS NULL OR LOWER(HD.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
////            "AND ((:#{#param.ngayXuatKhoTu}  IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoTu})" +
////            "AND (:#{#param.ngayXuatKhoDen}  IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}))" +
//            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
//            "ORDER BY HD.ngaySua desc , HD.ngayTao desc, HD.id desc"
//    )
//    Page<XhTlHaoDoiHdr> search(@Param("param") XhTlHaoDoiHdrReq param, Pageable pageable);

    List<XhTlHaoDoiHdr> findByIdIn(List<Long> ids);

    List<XhTlHaoDoiHdr> findAllByIdIn(List<Long> listId);

    Optional<XhTlHaoDoiHdr> findByIdBbTinhKho(Long idBbTinhKho);
}