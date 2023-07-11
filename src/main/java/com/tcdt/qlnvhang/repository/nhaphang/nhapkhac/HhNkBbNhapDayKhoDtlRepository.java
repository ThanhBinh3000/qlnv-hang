package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkBbNhapDayKhoDtlRepository extends JpaRepository<HhNkBbNhapDayKhoDtl, Long> {
    List<HhNkBbNhapDayKhoDtl> findByHdrId(Long id);

    List<HhNkBbNhapDayKhoDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long id);
}
