package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HhDxuatKhMttSlddDtlRepository extends JpaRepository<HhDxuatKhMttSlddDtl,Long> {
List<HhDxuatKhMttSlddDtl> findAllByIdSldd(Long ids);

List<HhDxuatKhMttSlddDtl> findAllByIdSlddIn(List<Long> ids);
}
