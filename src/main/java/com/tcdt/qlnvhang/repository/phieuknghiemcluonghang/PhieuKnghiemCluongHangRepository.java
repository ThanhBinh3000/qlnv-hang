package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import org.springframework.data.repository.CrudRepository;

public interface PhieuKnghiemCluongHangRepository extends CrudRepository<PhieuKnghiemCluongHang, Long>, PhieuKnghiemCluongHangRepositoryCustom {
}
