package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKtChatLuong;
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
public interface DcnbPhieuKtChatLuongHdrRepository extends JpaRepository<DcnbPhieuKtChatLuongHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbPhieuKtChatLuongHdr c WHERE 1=1 " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayKiem <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soBbLayMau} IS NULL OR LOWER(c.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "GROUP BY c.soQdinhDc,c.nam"
    )
    Page<DcnbPhieuKtChatLuongHdr> search(@Param("param")SearchPhieuKtChatLuong req, Pageable pageable);

    Optional<DcnbPhieuKtChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKtChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKtChatLuongHdr> findAllByIdIn(List<Long> idList);
}
