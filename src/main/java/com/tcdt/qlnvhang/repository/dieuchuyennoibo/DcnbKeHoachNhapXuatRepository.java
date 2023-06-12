package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DcnbKeHoachNhapXuatRepository extends JpaRepository<DcnbDataLink, Long> {

//    List<DcnbKeHoachDcDtlTT> findByIdKhDcDtl(Long idKhDcDtl);
//
//    Optional<DcnbKeHoachDcDtlTT> findByIdKhDcDtlAndTableName(Long idKhDcDtl, String tableName);
}
