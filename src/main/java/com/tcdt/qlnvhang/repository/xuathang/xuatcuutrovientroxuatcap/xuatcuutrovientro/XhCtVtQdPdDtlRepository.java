package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtVtQdPdDtlRepository extends JpaRepository<XhCtVtQuyetDinhPdDtl,Long> {
    List<XhCtVtQuyetDinhPdDtl> findByXhCtVtQuyetDinhPdHdrId(Long id);

    List<XhCtVtQuyetDinhPdDtl> findByXhCtVtQuyetDinhPdHdrIdIn(List<Long> listId);
}
