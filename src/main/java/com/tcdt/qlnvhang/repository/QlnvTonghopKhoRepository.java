package com.tcdt.qlnvhang.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvhang.table.QlnvTonghopHang;

@Repository
public interface QlnvTonghopKhoRepository extends CrudRepository<QlnvTonghopHang, Long> {

	@Query(value = "SELECT tb.* FROM qlnv_tonghop_hang tb "
			+ "WHERE tb.ma_lo=?1 AND tb.ma_ngan=?2 AND tb.ma_kho=?3 AND tb.ma_hanghoa=?4 AND tb.dvi_tinh=?5 and ROWNUM = 1", nativeQuery = true)
	QlnvTonghopHang findByParam(String maLo, String maNgan, String maKho, String maHhoa, String dvTinh);

}
