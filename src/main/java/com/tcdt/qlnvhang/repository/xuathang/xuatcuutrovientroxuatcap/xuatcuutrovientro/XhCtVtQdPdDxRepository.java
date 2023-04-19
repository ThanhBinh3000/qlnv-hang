package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtVtQdPdDxRepository extends JpaRepository<XhCtVtQuyetDinhPdDx,Long> {
    List<XhCtVtQuyetDinhPdDx> findByXhCtVtQuyetDinhPdDtlIn(List<Long> xhCtVtQuyetDinhPdDx);
}
