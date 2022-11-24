package com.tcdt.qlnvhang.repository.banhang;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.BhHopDongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BhHopDongRepository extends BaseRepository<BhHopDongHdr,Long> {
    Optional<BhHopDongHdr> findBySoHd(String soHd);

    @Query(
            value = "SELECT * " +
                    "FROM BH_HOP_DONG_HDR  HDR" +
                    " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH = :loaiVthh) " +
                    "  AND (:soHd IS NULL OR HDR.SO_HD = :soHd) " +
                    "  AND (:tenHd IS NULL OR HDR.TEN_HD = :tenHd) " +
                    "  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
                    "  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'yyyy-MM-dd')) " +
                    "  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'yyyy-MM-dd')) " +
                    "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
            nativeQuery = true)
    Page<BhHopDongHdr> select(String loaiVthh, String soHd, String tenHd, String nhaCcap, String tuNgayKy, String denNgayKy, String trangThai, Pageable pageable);

    List<BhHopDongHdr> findByIdIn(Collection<Long> ids);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);


}
