package com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhQdDchinhKhBdgDtlRepository extends JpaRepository<XhQdDchinhKhBdgDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdDchinhKhBdgDtl> findAllByIdHdr(Long idHdr);

    List<XhQdDchinhKhBdgDtl> findByIdHdrIn(List<Long> listId);
}