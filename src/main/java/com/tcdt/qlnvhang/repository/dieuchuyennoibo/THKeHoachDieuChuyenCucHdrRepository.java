package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbKeHoachDc;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenCucHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface THKeHoachDieuChuyenCucHdrRepository extends JpaRepository<THKeHoachDieuChuyenCucHdr, Long> {
    @Query(value = "SELECT distinct hdr FROM THKeHoachDieuChuyenCucHdr hdr WHERE 1=1 " +
            "AND (:#{#param.maDVi} IS NULL OR hdr.maDvi LIKE CONCAT(:#{#param.maDVi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR hdr.namKeHoach = :#{#param.nam}) " +
            "AND (:#{#param.maTongHop} IS NULL OR LOWER(hdr.maTongHop) LIKE CONCAT('%',LOWER(:#{#param.maTongHop}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayTongHop >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayTongHop <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(hdr.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "ORDER BY hdr.namKeHoach desc"
    )
    Page<THKeHoachDieuChuyenCucHdr> search(@Param("param") TongHopKeHoachDieuChuyenSearch param, Pageable pageable);

    List<THKeHoachDieuChuyenCucHdr> findAllByIdIn(List<Long> id);

    List<THKeHoachDieuChuyenCucHdr> findByIdIn(List<Long> id);

    Optional<THKeHoachDieuChuyenCucHdr> findByMaTongHop(String maTongHop);
}
