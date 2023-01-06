package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HhBcanKeHangDtlRepository extends JpaRepository<HhBcanKeHangDtl, Long> {

    List<HhBcanKeHangDtl> findAllByIdHdr(Long idHdr);

    void deleteByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);

}
