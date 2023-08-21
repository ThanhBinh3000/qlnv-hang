package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDdiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhDxKhBanTrucTiepDdiemRepository extends JpaRepository<XhDxKhBanTrucTiepDdiem, Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhDxKhBanTrucTiepDdiem> findAllByIdDtl(Long idDtl);

    List<XhDxKhBanTrucTiepDdiem> findByIdDtlIn(List<Long> listId);
}
