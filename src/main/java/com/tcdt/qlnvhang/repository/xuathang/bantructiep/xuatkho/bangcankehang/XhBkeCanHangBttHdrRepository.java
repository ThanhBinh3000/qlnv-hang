package com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XhBkeCanHangBttHdrRepository extends JpaRepository<XhBkeCanHangBttHdr, Long> {

    @Query("SELECT BK from XhBkeCanHangBttHdr BK WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR BK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(BK.soQdNv) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdNv}),'%' ) ) )" +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(BK.soBangKe) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBangKe}),'%' ) ) )" +
            "AND (:#{#param.loaiVthh} IS NULL OR BK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.ngayXuatKhoTu} IS NULL OR BK.ngayXuatKho <= :#{#param.ngayXuatKhoTu}) " +
            "AND (:#{#param.ngayXuatKhoDen} IS NULL OR BK.ngayXuatKho >= :#{#param.ngayXuatKhoDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR BK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR BK.maDvi = :#{#param.maDvi})")
    Page<XhBkeCanHangBttHdr> searchPage(@Param("param") XhBkeCanHangBttHdrReq param, Pageable pageable);

}
