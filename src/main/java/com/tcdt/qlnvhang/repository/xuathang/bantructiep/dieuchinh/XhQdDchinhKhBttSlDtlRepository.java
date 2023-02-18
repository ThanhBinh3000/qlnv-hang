package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttSlDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttSlDtlRepository extends JpaRepository<XhQdDchinhKhBttSlDtl, Long> {

    void deleteAllByIdSl(Long idSl);

    List<XhQdDchinhKhBttSlDtl> findByIdSl(Long idSl);


}
