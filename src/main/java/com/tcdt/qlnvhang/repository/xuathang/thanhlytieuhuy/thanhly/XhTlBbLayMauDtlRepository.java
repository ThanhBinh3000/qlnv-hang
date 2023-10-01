package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlBbLayMauDtlRepository extends JpaRepository<XhTlBbLayMauDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTlBbLayMauDtl> findAllByIdHdr(Long idHdr);
}