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

@Repository
public interface XhBangKeBttRepository extends JpaRepository<XhBangKeBtt, Long> {

    @Query("SELECT BK FROM XhBangKeBtt BK " +
            "WHERE (:#{#param.dvql} IS NULL OR BK.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR BK.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(BK.soBangKe) LIKE CONCAT('%', LOWER(:#{#param.soBangKe}),'%' ) ) " +
            "AND (:#{#param.soQdNv} IS NULL OR LOWER(BK.soQdNv) LIKE CONCAT('%', LOWER(:#{#param.soQdNv}),'%')) " +
            "AND (:#{#param.tenBenMua} IS NULL OR LOWER(BK.tenBenMua) LIKE CONCAT('%', LOWER(:#{#param.tenBenMua}),'%' ) ) " +
            "AND (:#{#param.ngayBanHangTu} IS NULL OR BK.ngayBanHang >= :#{#param.ngayBanHangTu}) " +
            "AND (:#{#param.ngayBanHangDen} IS NULL OR BK.ngayBanHang <= :#{#param.ngayBanHangDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR BK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "ORDER BY BK.namKh DESC, BK.ngayTao DESC, BK.id DESC")
    Page<XhBangKeBtt> searchPage(@Param("param") XhBangKeBttReq param, Pageable pageable);

    List<XhBangKeBtt> findByIdIn(List<Long> idBkList);

    List<XhBangKeBtt> findAllByIdIn(List<Long> listId);
}