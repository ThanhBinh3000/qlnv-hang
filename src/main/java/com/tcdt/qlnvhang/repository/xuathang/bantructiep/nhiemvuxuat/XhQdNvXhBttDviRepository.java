package com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XhQdNvXhBttDviRepository extends JpaRepository<XhQdNvXhBttDvi, Long> {

    void deleteAllByIdDtl(Long idDtl);
}
