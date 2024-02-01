package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKhoRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdr;
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
public interface XhXkVtPhieuXuatNhapKhoRepository extends JpaRepository<XhXkVtPhieuXuatNhapKho, Long> {

    @Query("SELECT c FROM XhXkVtPhieuXuatNhapKho c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
            "AND (:#{#param.loaiPhieu} IS NULL OR c.loaiPhieu = :#{#param.loaiPhieu}) " +
            "AND (:#{#param.maDiaDiem} IS NULL OR c.maDiaDiem = :#{#param.maDiaDiem}) " +
            "AND (:#{#param.idCanCu} IS NULL OR c.idCanCu = :#{#param.idCanCu}) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.soBcKqkdMau} IS NULL OR c.soBcKqkdMau = :#{#param.soBcKqkdMau}) " +
            "AND (:#{#param.idBcKqkdMau} IS NULL OR c.idBcKqkdMau = :#{#param.idBcKqkdMau}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.canCus.size() }  = 0 OR c.idCanCu IN (:#{#param.canCus})) " +
            "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND ((:#{#param.ngayXuatNhapTu}  IS NULL OR c.ngayXuatNhap >= :#{#param.ngayXuatNhapTu})" +
            "AND (:#{#param.ngayXuatNhapDen}  IS NULL OR c.ngayXuatNhap <= :#{#param.ngayXuatNhapDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtPhieuXuatNhapKho> search(@Param("param") XhXkVtPhieuXuatNhapKhoRequest param, Pageable pageable);

    Optional<XhXkVtPhieuXuatNhapKho> findBySoPhieu(String soPhieu);

    List<XhXkVtPhieuXuatNhapKho> findByIdIn(List<Long> ids);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdIn(List<Long> idList);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdCanCuIn(List<Long> idList);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdBbKtNhapKho(Long idBbNhapKho);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdBcKqkdMau(Long idBcKqKdMau);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdBcKqkdMauIn(List<Long> idList);

    List<XhXkVtPhieuXuatNhapKho> findAllByIdQdXuatGiamVt(Long idQdXuatGiamVt);

    List<XhXkVtPhieuXuatNhapKho> findByIdCanCuIn(List<Long> ids);

    @Query("SELECT c FROM XhXkVtPhieuXuatNhapKho c, XhXkVtBbKtNhapKho b WHERE 1=1 " +
            "AND (c.idBbKtNhapKho = b.id) " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (c.loai = 'NHAP_MAU') " +
            "AND (c.loaiPhieu ='NHAP') " +
            "AND (c.soBcKqkdMau is not null) " +
            "AND (c.soBbKtNhapKho is not null) " +
            "AND (:#{#param.namKeHoach} IS NULL OR c.namKeHoach = :#{#param.namKeHoach}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.soBcKqKdMau} IS NULL OR LOWER(c.soBcKqkdMau) LIKE CONCAT('%',LOWER(:#{#param.soBcKqKdMau}),'%')) " +
            "AND (:#{#param.soBienBan}  IS NULL OR b.soBienBan >= :#{#param.soBienBan})" +
            "AND ((:#{#param.ngayKetThucNhapKhoTu}  IS NULL OR b.ngayKetThucNhap >= :#{#param.ngayKetThucNhapKhoTu})" +
            "AND (:#{#param.ngayKetThucNhapKhoDen}  IS NULL OR b.ngayKetThucNhap  <= :#{#param.ngayKetThucNhapKhoDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR b.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtPhieuXuatNhapKho> searchPageBbKetThucNhapKho(@Param("param") XhXkVtBbKtNhapKhoRequest param, Pageable pageable);
}
