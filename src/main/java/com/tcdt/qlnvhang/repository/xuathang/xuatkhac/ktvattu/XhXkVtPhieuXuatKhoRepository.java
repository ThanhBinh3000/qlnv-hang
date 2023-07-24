package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatKhoRequest;
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
public interface XhXkVtPhieuXuatKhoRepository extends JpaRepository<XhXkVtPhieuXuatNhapKho, Long> {

    @Query("SELECT c FROM XhXkVtPhieuXuatNhapKho c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.loaiPhieu} IS NULL OR c.loaiPhieu = :#{#param.loaiPhieu}) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.canCus.size() }  = 0 OR c.idCanCu IN (:#{#param.canCus})) " +
            "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND ((:#{#param.ngayXuatNhapTu}  IS NULL OR c.ngayXuatNhap >= :#{#param.ngayXuatNhapTu})" +
            "AND (:#{#param.ngayXuatNhapDen}  IS NULL OR c.ngayXuatNhap <= :#{#param.ngayXuatNhapDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtPhieuXuatNhapKho> search(@Param("param") XhXkVtPhieuXuatKhoRequest param, Pageable pageable);

    Optional<XhXkVtPhieuXuatNhapKho> findBySoPhieu(String soPhieu);

    List<XhXkVtPhieuXuatNhapKho> findByIdIn(List<Long> ids);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdIn(List<Long> idList);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdCanCuIn(List<Long> idList);
}
