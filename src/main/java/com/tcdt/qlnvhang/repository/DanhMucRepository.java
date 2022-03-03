package com.tcdt.qlnvhang.repository;

import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvhang.table.QlnvDanhMuc;

public interface DanhMucRepository extends CrudRepository<QlnvDanhMuc, Long> {

	Iterable<QlnvDanhMuc> findByTrangThai(String trangThai);

}
