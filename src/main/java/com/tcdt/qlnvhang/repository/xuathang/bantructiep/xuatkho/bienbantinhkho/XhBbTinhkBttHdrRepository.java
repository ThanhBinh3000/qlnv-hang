package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bienbantinhkho;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface XhBbTinhkBttHdrRepository extends JpaRepository<XhBbTinhkBttHdr, Long> {

    @Query("SELECT TK from XhBbTinhkBttHdr TK WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR TK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(TK.soBbTinhKho) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBbTinhKho}),'%' ) ) )" +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(TK.soQdNv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdNv}),'%' ) ) )" +
            "AND (:#{#param.trangThai} IS NULL OR TK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.ngayBdauXuatTu} IS NULL OR TK.ngayBdauXuat >= :#{#param.ngayBdauXuatTu}) " +
            "AND (:#{#param.ngayBdauXuatDen} IS NULL OR TK.ngayBdauXuat <= :#{#param.ngayBdauXuatDen}) " +
            "AND (:#{#param.ngayKthucXuatTu} IS NULL OR TK.ngayKthucXuat >= :#{#param.ngayKthucXuatTu}) " +
            "AND (:#{#param.ngayKthucXuatDen} IS NULL OR TK.ngayKthucXuat <= :#{#param.ngayKthucXuatDen}) " +
            "AND (:#{#param.ngayQdNvTu} IS NULL OR TK.ngayQdNv >= :#{#param.ngayQdNvTu}) " +
            "AND (:#{#param.ngayQdNvDen} IS NULL OR TK.ngayQdNv <= :#{#param.ngayQdNvDen}) " +
            "AND (:#{#param.maDvi} IS NULL OR TK.maDvi = :#{#param.maDvi})")
    Page<XhBbTinhkBttHdr> searchPage(@Param("param") XhBbTinhkBttHdrReq param, Pageable pageable);



}
