package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKhoRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBbKtNhapKhoRepository extends JpaRepository<XhXkVtBbKtNhapKho, Long> {

    @Query("SELECT c FROM XhXkVtBbKtNhapKho c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
            "AND ((:#{#param.ngayKetThucNhapKhoTu}  IS NULL OR c.ngayKetThucNhap >= :#{#param.ngayKetThucNhapKhoTu})" +
            "AND (:#{#param.ngayKetThucNhapKhoDen}  IS NULL OR c.ngayKetThucNhap <= :#{#param.ngayKetThucNhapKhoDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtBbKtNhapKho> search(@Param("param") XhXkVtBbKtNhapKhoRequest param, Pageable pageable);

    Optional<XhXkVtBbKtNhapKho> findBySoBienBan(String soBb);

    List<XhXkVtBbKtNhapKho> findByIdIn(List<Long> ids);

    List<XhXkVtBbKtNhapKho> findAllByIdIn(List<Long> idList);

}
