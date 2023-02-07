package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhThopDxKhBttDtlRepository extends JpaRepository<XhThopDxKhBttDtl, Long> {

    List<XhThopDxKhBttDtl> findByIdThopHdr(Long idThopHdr);
}
