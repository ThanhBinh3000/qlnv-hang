package com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgPlRepository extends JpaRepository<XhQdDchinhKhBdgPl, Long> {

    void deleteByIdDcDtl(Long idDcDtl);

    List<XhQdDchinhKhBdgPl> findByIdDcDtl(Long idDcDtl);
}