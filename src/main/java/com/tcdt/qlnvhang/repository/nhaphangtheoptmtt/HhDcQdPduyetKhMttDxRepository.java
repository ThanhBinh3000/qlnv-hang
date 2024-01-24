package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPduyetKhmttDx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDcQdPduyetKhMttDxRepository extends JpaRepository<HhDcQdPduyetKhmttDx,Long> {

    List<HhDcQdPduyetKhmttDx> findAllByIdDcHdr(Long idDcHdr);

    List<HhDcQdPduyetKhmttDx> findAllByIdDcHdrIn(List<Long> ids);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
