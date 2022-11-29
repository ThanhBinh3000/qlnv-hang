package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnTienDoThucHien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhCnTienDoThucHienRepository extends JpaRepository<KhCnTienDoThucHien,Long> {
    List<KhCnTienDoThucHien> findAllByIdHdr(Long ids);
    List<KhCnTienDoThucHien> findAllByIdHdrIn(List<Long> ids);

}
