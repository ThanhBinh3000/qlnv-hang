package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdDx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtQdPdDxRepository extends JpaRepository<XhCtvtQuyetDinhPdDx,Long> {
    List<XhCtvtQuyetDinhPdDx> findByXhCtvtQuyetDinhPdDtlIn(List<Long> xhCtvtQuyetDinhPdDx);
}
