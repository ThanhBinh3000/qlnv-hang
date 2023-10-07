package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBbHaoDoi;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbTinhKhoHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDgBbHaoDoiHdrRepository extends JpaRepository<XhDgBbHaoDoiHdr, Long> {

    @Query("SELECT DISTINCT HD FROM XhDgBbHaoDoiHdr HD " +
            "LEFT JOIN XhQdGiaoNvXh QD ON QD.id = HD.idQdNv " +
            "WHERE (:#{#param.dvql} IS NULL OR HD.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR HD.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdNv} IS NULL OR HD.soQdNv = :#{#param.soQdNv}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR HD.soBbHaoDoi = :#{#param.soBbHaoDoi}) " +
            "AND (:#{#param.ngayLapBienBanTu} IS NULL OR HD.ngayLapBienBan >= :#{#param.ngayLapBienBanTu}) " +
            "AND (:#{#param.ngayLapBienBanDen} IS NULL OR HD.ngayLapBienBan <= :#{#param.ngayLapBienBanDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR HD.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR HD.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDviCha} IS NULL OR QD.maDvi LIKE CONCAT(:#{#param.maDviCha}, '%')) " +
            "ORDER BY HD.nam DESC, HD.ngaySua DESC, HD.ngayTao DESC, HD.id DESC")
    Page<XhDgBbHaoDoiHdr> searchPage(@Param("param") XhDgBbHaoDoiHdrReq param, Pageable pageable);

    boolean existsBySoBbHaoDoi(String soBbHaoDoi);

    boolean existsBySoBbHaoDoiAndIdNot(String soBbHaoDoi, Long id);

    List<XhDgBbHaoDoiHdr> findByIdIn(List<Long> idList);

    List<XhDgBbHaoDoiHdr> findAllByIdIn(List<Long> listId);
}
