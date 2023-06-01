package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanTinhKho;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBienBanTinhKhoHdrRepository extends JpaRepository<DcnbBienBanTinhKhoHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbBienBanTinhKhoHdr c left join c.dcnbBienBanTinhKhoDtl h left join QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(c.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBatDauXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBatDauXuat <= :#{#param.denNgay}) ) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayKeThucXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayKeThucXuat <= :#{#param.denNgay}) ) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.thoiHanXuatHang >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.thoiHanXuatHang <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc")
    Page<DcnbBienBanTinhKhoHdr> search(@Param("param") SearchDcnbBienBanTinhKho req, Pageable pageable);

    Optional<DcnbBienBanTinhKhoHdr> findFirstBySoBbTinhKho(String soBbTinhKho);

    List<DcnbBienBanTinhKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanTinhKhoHdr> findAllByIdIn(List<Long> idList);
}
