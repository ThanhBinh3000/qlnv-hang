package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttDtlRepository extends JpaRepository<XhQdDchinhKhBttDtl, Long> {

    void deleteAllByIdDcHdr(Long idDcHdr);

    List<XhQdDchinhKhBttDtl> findAllByIdDcHdr(Long idDcHdr);

}
