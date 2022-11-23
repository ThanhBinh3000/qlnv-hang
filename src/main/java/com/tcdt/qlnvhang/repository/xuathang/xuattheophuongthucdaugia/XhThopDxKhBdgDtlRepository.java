package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface XhThopDxKhBdgDtlRepository extends JpaRepository<XhThopDxKhBdgDtl,Long> {


    List<XhThopDxKhBdgDtl> findByIdThopHdr(Long idThopHdr);
    @Transactional
    @Modifying
    void deleteAllByIdIn(List<HhDxKhMttThopDtl> ids);

}
