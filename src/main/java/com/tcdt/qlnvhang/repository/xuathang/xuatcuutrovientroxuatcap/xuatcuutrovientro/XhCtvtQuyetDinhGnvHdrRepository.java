package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhGnv;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtQuyetDinhGnvHdrRepository extends JpaRepository<XhCtvtQuyetDinhGnvHdr, Long> {

    @Query("SELECT DISTINCT c FROM XhCtvtQuyetDinhGnvHdr c left join c.dataDtl e WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.maDviGiao} IS NULL OR e.maDvi LIKE CONCAT(:#{#param.maDviGiao},'%')) " +
            "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.tenVthh} IS NULL OR c.tenVthh = :#{#param.tenVthh}) " +
            "AND (:#{#param.soBbQd} IS NULL OR LOWER(c.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
            "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.listTrangThaiXh.size() }  = 0 OR c.trangThaiXh in :#{#param.listTrangThaiXh}) " +
            "AND (:#{#param.types.size() } = 0 OR c.type in :#{#param.types}) " +
            "AND (:#{#param.idQdPd} IS NULL OR c.idQdPd = :#{#param.idQdPd}) " +
//            "AND (:#{#param.listTrangThai.size() } = 0 OR c.trangThai in :#{#param.listTrangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhCtvtQuyetDinhGnvHdr> search(@Param("param") SearchXhCtvtQuyetDinhGnv param, Pageable pageable);

    void deleteAllByIdIn(List<Long> listId);

    List<XhCtvtQuyetDinhGnvHdr> findByIdIn(List<Long> ids);

    List<XhCtvtQuyetDinhGnvHdr> findAllByIdIn(List<Long> listId);

    List<XhCtvtQuyetDinhGnvHdr> findByIdQdPdIn(List<Long> listId);

    Optional<XhCtvtQuyetDinhGnvHdr> findBySoBbQd(String soQd);

    @Query("SELECT DISTINCT c FROM XhCtvtQuyetDinhGnvHdr c left join c.dataDtl e WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.maDviGiao} IS NULL OR e.maDvi LIKE CONCAT(:#{#param.maDviGiao},'%')) " +
            "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.tenVthh} IS NULL OR c.tenVthh = :#{#param.tenVthh}) " +
            "AND (:#{#param.soBbQd} IS NULL OR LOWER(c.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
            "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.listTrangThaiXh.size() }  = 0 OR e.trangThai in :#{#param.listTrangThaiXh}) " +
            "AND (:#{#param.types.size() } = 0 OR c.type in :#{#param.types}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    List<XhCtvtQuyetDinhGnvHdr> searchList(@Param("param") SearchXhCtvtQuyetDinhGnv param);
}
