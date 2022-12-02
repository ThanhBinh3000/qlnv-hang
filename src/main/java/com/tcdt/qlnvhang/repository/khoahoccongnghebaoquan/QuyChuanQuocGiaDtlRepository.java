package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuyChuanQuocGiaDtlRepository extends JpaRepository<QuyChuanQuocGiaDtl,Long> {

    List<QuyChuanQuocGiaDtl> findAllByIdHdr(Long ids);

    List<QuyChuanQuocGiaDtl> findAllByIdHdrIn(List<Long> ids);
}
