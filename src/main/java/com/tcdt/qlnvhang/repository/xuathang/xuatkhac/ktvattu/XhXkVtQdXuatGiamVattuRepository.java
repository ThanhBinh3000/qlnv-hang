package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattuRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattu;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkVtQdXuatGiamVattuRepository extends JpaRepository<XhXkVtQdXuatGiamVattu, Long> {
    @Query("SELECT distinct c FROM XhXkVtQdXuatGiamVattu c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.ngayQuyetDinhTu}  IS NULL OR c.ngayKy >= :#{#param.ngayQuyetDinhTu})" +
            "AND  (:#{#param.ngayQuyetDinhDen}  IS NULL OR c.ngayKy <= :#{#param.ngayQuyetDinhDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtQdXuatGiamVattu> searchPage(@Param("param") XhXkVtQdXuatGiamVattuRequest param, Pageable pageable);


    void deleteAllByIdIn(List<Long> listId);

    List<XhXkVtQdXuatGiamVattu> findByIdIn(List<Long> ids);

    List<XhXkVtQdXuatGiamVattu> findByIdCanCuIn(List<Long> ids);

    Optional<XhXkVtQdXuatGiamVattu> findBySoQuyetDinh(String soQd);

}
