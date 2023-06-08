package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKnChatLuong;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKtChatLuong;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuKnChatLuongHdrRepository extends JpaRepository<DcnbPhieuKnChatLuongHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbPhieuKnChatLuongHdr c  LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayKiem <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(c.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "ORDER BY c.soQdinhDc desc,c.nam desc "
    )
    Page<DcnbPhieuKnChatLuongHdr> search(@Param("param") SearchPhieuKnChatLuong req, Pageable pageable);

    Optional<DcnbPhieuKnChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKnChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKnChatLuongHdr> findAllByIdIn(List<Long> idList);
}
