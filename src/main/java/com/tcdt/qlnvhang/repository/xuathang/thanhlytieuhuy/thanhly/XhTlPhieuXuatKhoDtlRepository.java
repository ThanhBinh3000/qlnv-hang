package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlPhieuXuatKhoDtlRepository extends JpaRepository<XhTlPhieuXuatKhoDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhTlPhieuXuatKhoDtl> findByIdHdr(Long idHdr);
}