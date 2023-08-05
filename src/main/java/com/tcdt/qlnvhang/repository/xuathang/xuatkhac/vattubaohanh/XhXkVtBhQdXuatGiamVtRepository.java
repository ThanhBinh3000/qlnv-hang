package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdXuatGiamVtRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdXuatGiamVt;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkVtBhQdXuatGiamVtRepository extends JpaRepository<XhXkVtBhQdXuatGiamVt, Long> {
    @Query("SELECT distinct c FROM XhXkVtBhQdXuatGiamVt c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDviNhan LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.ngayQuyetDinhTu}  IS NULL OR c.ngayKy >= :#{#param.ngayQuyetDinhTu})" +
            "AND  (:#{#param.ngayQuyetDinhDen}  IS NULL OR c.ngayKy <= :#{#param.ngayQuyetDinhDen})) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtBhQdXuatGiamVt> searchPage(@Param("param") XhXkVtBhQdXuatGiamVtRequest param, Pageable pageable);


    void deleteAllByIdIn(List<Long> listId);

    List<XhXkVtBhQdXuatGiamVt> findByIdIn(List<Long> ids);

    List<XhXkVtBhQdXuatGiamVt> findByIdCanCuIn(List<Long> ids);

    Optional<XhXkVtBhQdXuatGiamVt> findBySoQuyetDinh(String soQd);

}
