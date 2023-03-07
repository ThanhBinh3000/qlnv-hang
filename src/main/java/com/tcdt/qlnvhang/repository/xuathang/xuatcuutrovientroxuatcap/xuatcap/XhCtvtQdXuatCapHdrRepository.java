package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhCtvtQdXuatCap;
import com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.xuatcap.XhCtvtQdXuatCap;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtQdXuatCapHdrRepository extends JpaRepository<XhCtvtQdXuatCapHdr, Long> {

    @Query("SELECT DISTINCT  c FROM XhCtvtQdXuatCapHdr c  left join c.xhCtVtQuyetDinhPdHdr e " +
            " WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdXc} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdXc}),'%')) " +
            "AND ((:#{#param.ngayXuatCtvtTu}  IS NULL OR c.thoiHanXuat >= :#{#param.ngayXuatCtvtTu})" +
            "AND (:#{#param.ngayXuatCtvtDen}  IS NULL OR c.thoiHanXuat <= :#{#param.ngayXuatCtvtDen}) ) " +
            "AND ((:#{#param.ngayHieuLucTu}  IS NULL OR c.ngayHluc >= :#{#param.ngayHieuLucTu})" +
            "AND (:#{#param.ngayHieuLucDen}  IS NULL OR c.ngayHluc <= :#{#param.ngayHieuLucDen}) ) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhCtvtQdXuatCapHdr> search (@Param("param")SearchXhCtvtQdXuatCap param, Pageable pageable);

    Optional<XhCtvtQdXuatCapHdr> findBySoQd (String soQd);

    List<XhCtvtQdXuatCapHdr> findAllByIdIn (List<Long> listId);
}