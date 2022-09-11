package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHang;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhBangKeCanHangRepository extends BaseRepository<XhBangKeCanHang, Long>, XhBangKeCanHangRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(List<Long> ids);

    Optional<XhBangKeCanHang> findFirstBySoBangKe(String so);

    @Query(value = "select max(id) from XhBangKeCanHang")
    Long getMaxId();
}
