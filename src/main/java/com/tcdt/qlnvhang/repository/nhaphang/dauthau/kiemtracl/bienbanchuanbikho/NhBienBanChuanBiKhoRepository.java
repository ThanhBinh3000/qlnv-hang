package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
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
public interface NhBienBanChuanBiKhoRepository extends BaseRepository<NhBienBanChuanBiKho, Long> {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    List<NhBienBanChuanBiKho> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh,String maDvi);

    Optional<NhBienBanChuanBiKho> findFirstBySoBienBan(String soBienBan);

    NhBienBanChuanBiKho findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    @Query(
            value = " SELECT BB FROM NhBienBanChuanBiKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.tuNgayTaoStr} IS NULL OR BB.ngayTao >= TO_DATE(:#{#req.tuNgayTaoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayTaoStr} IS NULL OR BB.ngayTao <= TO_DATE(:#{#req.denNgayTaoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " +
                    " AND (:#{#req.soBbCbk} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBbCbk}),'%'))) ",
            countQuery = " SELECT COUNT * FROM ( SELECT BB FROM NhBienBanChuanBiKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.tuNgayTaoStr} IS NULL OR BB.ngayTao >= TO_DATE(:#{#req.tuNgayTaoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayTaoStr} IS NULL OR BB.ngayTao <= TO_DATE(:#{#req.denNgayTaoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                   " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " +
                    " AND (:#{#req.soBbCbk} IS NULL OR LOWER(BB.soBienBan) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBbCbk}),'%')))) "
    )
    NhBienBanChuanBiKho timKiemByDiaDiemNh (HhQdNhapxuatSearchReq req);

}
