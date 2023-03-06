package com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.bangkebanle;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.bangkebanle.XhBangKeBttReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhBangKeBttRepository extends JpaRepository<XhBangKeBtt, Long> {

    @Query("SELECT BK from XhBangKeBtt BK WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR BK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(BK.soBangKe) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soBangKe}),'%' ) ) )" +
            "AND (:#{#param.soQd} IS NULL OR LOWER(BK.soQd) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQd}),'%'))) " +
            "AND (:#{#param.tenNguoiMua} IS NULL OR LOWER(BK.tenNguoiMua) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tenNguoiMua}),'%' ) ) )" +
            "AND (:#{#param.ngayBanHangTu} IS NULL OR BK.ngayBanHang >= :#{#param.ngayBanHangTu}) " +
            "AND (:#{#param.ngayBanHangDen} IS NULL OR BK.ngayBanHang <= :#{#param.ngayBanHangDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR BK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR BK.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR BK.maDvi = :#{#param.maDvi})")
    Page<XhBangKeBtt> searchPage(@Param("param") XhBangKeBttReq param, Pageable pageable);

    Optional<XhBangKeBtt> findBySoBangKe(String soBangKe);

    List<XhBangKeBtt> findByIdIn(List<Long> idDxList);
}
