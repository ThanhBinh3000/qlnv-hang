package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuKnghiemCluongHangRepository extends CrudRepository<PhieuKnghiemCluongHang, Long>, PhieuKnghiemCluongHangRepositoryCustom {
}
