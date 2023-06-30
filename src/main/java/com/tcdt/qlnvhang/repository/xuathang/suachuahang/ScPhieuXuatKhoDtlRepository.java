package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScPhieuXuatKhoDtlRepository extends JpaRepository<ScPhieuXuatKhoDtl,Long> {
     List<ScPhieuXuatKhoDtl> findByHdrId(Long id) ;

}
