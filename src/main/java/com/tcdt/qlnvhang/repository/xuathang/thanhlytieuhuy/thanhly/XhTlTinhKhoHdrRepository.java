package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlTinhKhoHdrRepository extends JpaRepository<XhTlTinhKhoHdr, Long> {

//    @Query("SELECT TK FROM XhTlTinhKhoHdr TK " +
//            " WHERE 1=1 " +
//            "AND (:#{#param.dvql} IS NULL OR TK.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
//            "AND (:#{#param.nam} IS NULL OR TK.nam = :#{#param.nam}) " +
//            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(TK.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
//            "AND (:#{#param.soBbQd} IS NULL OR LOWER(TK.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
////            "AND ((:#{#param.ngayXuatKhoTu}  IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoTu})" +
////            "AND (:#{#param.ngayXuatKhoDen}  IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoDen}))" +
//            "AND (:#{#param.trangThai} IS NULL OR TK.trangThai = :#{#param.trangThai}) " +
//            "ORDER BY TK.ngaySua desc , TK.ngayTao desc, TK.id desc"
//    )
//    Page<XhTlTinhKhoHdr> search(@Param("param") XhTlTinhKhoReq param, Pageable pageable);

    List<XhTlTinhKhoHdr> findByIdIn(List<Long> ids);

    List<XhTlTinhKhoHdr> findAllByIdIn(List<Long> listId);
    Optional<XhTlTinhKhoHdr> findByIdDsHdr(Long idDsHdr);

    @Query("SELECT c FROM XhTlTinhKhoHdr c " +
            " left join XhTlHaoDoiHdr hdoi on hdoi.idBbTinhKho = c.id " +
            " WHERE hdoi.id is null "+
            " AND (:#{#param.typeLt} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
            " AND (:#{#param.typeVt} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
            " AND (:#{#param.maDviSr} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDviSr},'%'))" +
            " AND (:#{#param.trangThai} IS NULL OR c.trangThai =:#{#param.trangThai})" +
            " AND (:#{#param.idQdXh} IS NULL OR c.idQdXh =:#{#param.idQdXh})" +
            " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<XhTlTinhKhoHdr> searchDsTaoBbHaoDoi(@Param("param") XhTlTinhKhoReq param);
}