package com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdNvXhBttDtlRepository extends JpaRepository<XhQdNvXhBttDtl, Long> {

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<XhQdNvXhBttDtl> findAllByIdQdHdr(Long idQdHdr);
}
