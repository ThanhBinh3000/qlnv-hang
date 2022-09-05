package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReqExt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface BhQdPheDuyetKqbdgRepository extends BaseRepository<BhQdPheDuyetKqbdg, Long>, BhQdPheDuyetKqbdgRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BhQdPheDuyetKqbdg> findFirstBySoQuyetDinh(String soQuyetDinh);

    @Query("SELECT c FROM BhQdPheDuyetKqbdg c WHERE 1=1 " +
        "AND (:#{#param.pagTypeLT} IS NULL OR c.loaiVthh NOT LIKE CONCAT('02','%')) " +
        "AND (:#{#param.pagTypeVT} IS NULL OR c.loaiVthh LIKE CONCAT('02','%')) " +
        "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
        "AND (:#{#param.dsTrangThai == null} = true OR c.trangThai in :#{#param.dsTrangThai}) " +
        "AND (:#{#param.soQuyetDinh}  IS NULL OR LOWER(c.soQuyetDinh) LIKE CONCAT('%',LOWER(:#{#param.soQuyetDinh}),'%')) " +
        "AND (:#{#param.trichYeu}  IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
        "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu}) AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
        "ORDER BY c.ngaySua desc , c.ngayTao desc")
    Page<BhQdPheDuyetKqbdg> search(
        @Param("param") BhQdPheDuyetKqbdgSearchReqExt param, Pageable pageable);
}
