package com.tcdt.qlnvhang.common;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcdt.qlnvhang.repository.QlnvTonghopKhoRepository;
import com.tcdt.qlnvhang.table.QlnvTonghopHang;

@Component
public class HandlingNumberProduct {
	@Autowired
	QlnvTonghopKhoRepository qlnvTonghopKhoRepository;

	public boolean checkProduct(String maLo, String maNgan, String maKho, String maHhoa, String dvTinh) {
		QlnvTonghopHang qTonghopHang = qlnvTonghopKhoRepository.findByParam(maLo, maNgan, maKho, maHhoa, dvTinh);
		if (ObjectUtils.isNotEmpty(qTonghopHang))
			return true;
		return false;
	}

	public boolean minusProduct(BigDecimal soLuong, String maLo, String maNgan, String maKho, String maHhoa,
			String dvTinh) {
		QlnvTonghopHang qTonghopHang = qlnvTonghopKhoRepository.findByParam(maLo, maNgan, maKho, maHhoa, dvTinh);
		BigDecimal tongXuat = Optional.ofNullable(qTonghopHang.getTongXuat()).orElse(BigDecimal.ZERO).add(soLuong);
		BigDecimal tonKho = Optional.ofNullable(qTonghopHang.getTongNhap()).orElse(BigDecimal.ZERO).subtract(tongXuat);

		qTonghopHang.setTon(tonKho);
		qTonghopHang.setTongXuat(tongXuat);
		qlnvTonghopKhoRepository.save(qTonghopHang);

		return true;
	}

	public boolean plusProduct(BigDecimal soLuong, String maLo, String maNgan, String maKho, String maHhoa,
			String dvTinh) {
		QlnvTonghopHang qTonghopHang = qlnvTonghopKhoRepository.findByParam(maLo, maNgan, maKho, maHhoa, dvTinh);
		BigDecimal tongNhap = Optional.ofNullable(qTonghopHang.getTongNhap()).orElse(BigDecimal.ZERO).add(soLuong);
		BigDecimal tonKho = tongNhap.subtract(Optional.ofNullable(qTonghopHang.getTongXuat()).orElse(BigDecimal.ZERO));

		qTonghopHang.setTon(tonKho);
		qTonghopHang.setTongNhap(tongNhap);
		qlnvTonghopKhoRepository.save(qTonghopHang);

		return true;
	}
}
