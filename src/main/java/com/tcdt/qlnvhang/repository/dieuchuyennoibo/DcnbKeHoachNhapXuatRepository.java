package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachNhapXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbKeHoachNhapXuatRepository extends JpaRepository<DcnbKeHoachNhapXuat, Long> {

    List<DcnbKeHoachNhapXuat> findByIdKhDcDtl(Long idKhDcDtl);

    Optional<DcnbKeHoachNhapXuat> findByIdKhDcDtlAndTableName(Long idKhDcDtl,String tableName);
}
