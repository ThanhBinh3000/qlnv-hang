package com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgPlDtlRepository extends JpaRepository<XhQdDchinhKhBdgPlDtl, Long> {

    void deleteAllByIdPhanLo(Long idPhanLo);

    List<XhQdDchinhKhBdgPlDtl> findAllByIdPhanLo(Long idPhanLo);
}