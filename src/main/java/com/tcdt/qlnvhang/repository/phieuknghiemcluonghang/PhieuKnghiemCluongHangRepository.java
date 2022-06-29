package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface PhieuKnghiemCluongHangRepository extends CrudRepository<PhieuKnghiemCluongHang, Long>, PhieuKnghiemCluongHangRepositoryCustom {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<PhieuKnghiemCluongHang> findFirstBySoPhieu(String soPhieu);
}
