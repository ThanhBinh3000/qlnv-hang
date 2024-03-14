package com.tcdt.qlnvhang.repository.vattu.bangke;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
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
public interface NhBangKeVtRepository extends BaseRepository<NhBangKeVt, Long>, NhBangKeVtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBangKeVt> findFirstBySoBangKe(String soBienBan);
    NhBangKeVt findBySoPhieuNhapKho(String soPhieuNhapKho);

    List<NhBangKeVt> findAllByIdDdiemGiaoNvNh (Long idDdiemGiaoNvNh);

    @Query(
            value = " SELECT BB FROM NhBangKeVt BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.soBangKe} IS NULL OR LOWER(BB.soBangKe) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBangKe}),'%')))  " +
                    " AND (:#{#req.tuNgayNkStr} IS NULL OR BB.ngayNhapKho >= TO_DATE(:#{#req.tuNgayNkStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayNkStr} IS NULL OR BB.ngayNhapKho <= TO_DATE(:#{#req.denNgayNkStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " ,
            countQuery = " SELECT COUNT * FROM ( SELECT BB FROM NhBbNhapDayKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.soBangKe} IS NULL OR LOWER(BB.soBangKe) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBangKe}),'%')))  " +
                    " AND (:#{#req.tuNgayNkStr} IS NULL OR BB.ngayNhapKho >= TO_DATE(:#{#req.tuNgayNkStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayNkStr} IS NULL OR BB.ngayNhapKho <= TO_DATE(:#{#req.denNgayNkStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) "
    )
    List<NhBangKeVt> timKiemByDiaDiemNh (HhQdNhapxuatSearchReq req);
}
