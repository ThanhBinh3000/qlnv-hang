package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhPhieuKnghiemCluongRepository extends BaseRepository<XhPhieuKnghiemCluong, Long> {


    @Query("SELECT c FROM XhPhieuKnghiemCluong c " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(c.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) "
    )
    Page<XhPhieuKnghiemCluong> searchPage(@Param("param") XhPhieuKnghiemCluongReq param, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<XhPhieuKnghiemCluong> findFirstBySoPhieu(String soPhieu);

    List<XhPhieuKnghiemCluong> findAllByIdQdGiaoNvXh(Long idQdGiaoNvXh);
}
