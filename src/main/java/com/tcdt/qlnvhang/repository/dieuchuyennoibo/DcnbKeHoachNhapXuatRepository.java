package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtlTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbKeHoachNhapXuatRepository extends JpaRepository<DcnbKeHoachDcDtlTT, Long> {

//    List<DcnbKeHoachDcDtlTT> findByIdKhDcDtl(Long idKhDcDtl);
//
//    Optional<DcnbKeHoachDcDtlTT> findByIdKhDcDtlAndTableName(Long idKhDcDtl, String tableName);
}
