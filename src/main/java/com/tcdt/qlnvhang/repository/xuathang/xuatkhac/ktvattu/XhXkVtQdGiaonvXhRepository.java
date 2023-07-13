package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkVtQdGiaonvXhRepository extends JpaRepository<XhXkVtQdGiaonvXhHdr, Long> {
    @Query("SELECT distinct c FROM XhXkVtQdGiaonvXhHdr c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.listTrangThaiXh} IS NULL OR c.trangThaiXh in :#{#param.listTrangThaiXh}) " +
            "AND ((:#{#param.ngayKyQdTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyQdTu})" +
            "AND  (:#{#param.ngayKyQdDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyQdDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtQdGiaonvXhHdr> searchPage(@Param("param") XhXkVtQdGiaonvXhRequest param, Pageable pageable);


    void deleteAllByIdIn(List<Long> listId);

    List<XhXkVtQdGiaonvXhHdr> findByIdIn(List<Long> ids);

    List<XhXkVtQdGiaonvXhHdr> findByIdCanCuIn(List<Long> ids);

    Optional<XhXkVtQdGiaonvXhHdr> findBySoQuyetDinh(String soQd);

}
