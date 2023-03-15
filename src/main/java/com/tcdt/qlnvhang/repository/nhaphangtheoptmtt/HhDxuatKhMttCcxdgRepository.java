package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttCcxdgRepository extends JpaRepository<HhDxuatKhMttCcxdg,Long> {

    List<HhDxuatKhMttCcxdg> findAllByIdHdr(Long idHdr);

    void deleteAllByIdHdr(Long idHdr);

    @Transactional
    void deleteAllByIdHdrIn(List<Long> idHdr);

}
