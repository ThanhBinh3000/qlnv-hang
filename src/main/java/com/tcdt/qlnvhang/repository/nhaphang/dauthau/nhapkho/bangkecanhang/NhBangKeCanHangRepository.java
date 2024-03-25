package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhBangKeCanHangRepository extends BaseRepository<NhBangKeCanHang, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBangKeCanHang> findFirstBySoBangKe(String soBangKe);

    List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhBangKeCanHang> findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    NhBangKeCanHang findBySoPhieuNhapKho(String soPhieuNhapKho);

    @Query(
            value = " SELECT BB FROM NhBangKeCanHang BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.soBkch} IS NULL OR LOWER(BB.soBangKe) LIKE LOWER(CONCAT(CONCAT('%',:#{#req.soBkch}),'%'))) " +
                    " AND (:#{#req.tuNgayTaoBkchStr} IS NULL OR BB.ngayTao >= TO_DATE(:#{#req.tuNgayTaoBkchStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayTaoBkchStr} IS NULL OR BB.ngayTao <= TO_DATE(:#{#req.denNgayTaoBkchStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " ,
            countQuery = " SELECT COUNT * FROM ( SELECT BB FROM NhBbNhapDayKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.soBkch} IS NULL OR LOWER(BB.soBangKe) LIKE LOWER(CONCAT(CONCAT('%',:#{#req.soBkch}),'%'))) " +
                    " AND (:#{#req.tuNgayTaoBkchStr} IS NULL OR BB.ngayTao >= TO_DATE(:#{#req.tuNgayTaoBkchStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayTaoBkchStr} IS NULL OR BB.ngayTao <= TO_DATE(:#{#req.denNgayTaoBkchStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) "
    )
    List<NhBangKeCanHang> timKiemByDiaDiemNh (HhQdNhapxuatSearchReq req);
}
