package com.tcdt.qlnvhang.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcdt.qlnvhang.repository.QlnvPhieuNhapxuatHdrRepository;
import com.tcdt.qlnvhang.repository.QlnvTonghopKhoRepository;
import com.tcdt.qlnvhang.table.QlnvPhieuNhapxuatDtl;
import com.tcdt.qlnvhang.table.QlnvPhieuNhapxuatHdr;
import com.tcdt.qlnvhang.table.QlnvTonghopHang;
import com.tcdt.qlnvhang.util.Contains;

@Component
public class AsyncProduct {
	static Logger logger = Logger.getLogger(AsyncProduct.class.getName());
	@Autowired
	QlnvPhieuNhapxuatHdrRepository qlnvPhieuNhapxuatHdrRepository;
	@Autowired
	QlnvTonghopKhoRepository qlnvTonghopKhoRepository;
	@Autowired
	HandlingNumberProduct handlingNumberProduct;

	synchronized public void handling(String type, QlnvPhieuNhapxuatHdr qNhapxuatHdr) throws Exception {
		if (type.equals(Contains.PHIEU_NHAP)) {
			updateApproveImport(qNhapxuatHdr);
		} else if (type.equals(Contains.PHIEU_XUAT)) {
			updateApproveExport(qNhapxuatHdr);
		}
	}

	@Transactional
	private void updateApproveImport(QlnvPhieuNhapxuatHdr qNhapxuatHdr) {
		qlnvPhieuNhapxuatHdrRepository.save(qNhapxuatHdr);

		List<QlnvPhieuNhapxuatDtl> dtls = qNhapxuatHdr.getChildren();

		Map<String, QlnvTonghopHang> hashMapDMap = new HashMap<String, QlnvTonghopHang>();
		for (QlnvPhieuNhapxuatDtl nhapxuatDtl : dtls) {
			StringBuffer sbf = new StringBuffer();
			sbf.append(qNhapxuatHdr.getMaLo());
			sbf.append(qNhapxuatHdr.getMaNgan());
			sbf.append(qNhapxuatHdr.getMaKho());
			sbf.append(nhapxuatDtl.getMaHhoa());
			sbf.append(nhapxuatDtl.getDviTinh());
			if (!hashMapDMap.containsKey(sbf.toString())) {
				QlnvTonghopHang qTonghopHang = new QlnvTonghopHang();
				qTonghopHang.setIdPhieuNhapxuat(qNhapxuatHdr.getId());
				qTonghopHang.setMaDvi(qNhapxuatHdr.getMaDvi());
				qTonghopHang.setMaKho(qNhapxuatHdr.getMaKho());
				qTonghopHang.setMaNgan(qNhapxuatHdr.getMaNgan());
				qTonghopHang.setMaLo(qNhapxuatHdr.getMaLo());
				qTonghopHang.setMaHanghoa(nhapxuatDtl.getMaHhoa());
				qTonghopHang.setDviTinh(nhapxuatDtl.getDviTinh());
				qTonghopHang.setTongNhap(nhapxuatDtl.getSoLuongThucte());

				hashMapDMap.put(sbf.toString(), qTonghopHang);
			} else {
				BigDecimal tongNhap = hashMapDMap.get(sbf.toString()).getTongNhap();
				hashMapDMap.get(sbf.toString()).setTongNhap(tongNhap.add(nhapxuatDtl.getSoLuongThucte()));
			}
		}

		for (String key : hashMapDMap.keySet()) {
			QlnvTonghopHang thHang = hashMapDMap.get(key);
			if (!handlingNumberProduct.checkProduct(qNhapxuatHdr.getMaLo(), qNhapxuatHdr.getMaNgan(),
					qNhapxuatHdr.getMaKho(), thHang.getMaHanghoa(), thHang.getDviTinh()))
				qlnvTonghopKhoRepository.save(hashMapDMap.get(key));
			else
				handlingNumberProduct.plusProduct(thHang.getTongNhap(), qNhapxuatHdr.getMaLo(),
						qNhapxuatHdr.getMaNgan(), qNhapxuatHdr.getMaKho(), thHang.getMaHanghoa(), thHang.getDviTinh());
		}
	}

	@Transactional
	private void updateApproveExport(QlnvPhieuNhapxuatHdr qNhapxuatHdr) {
		qlnvPhieuNhapxuatHdrRepository.save(qNhapxuatHdr);

		List<QlnvPhieuNhapxuatDtl> dtls = qNhapxuatHdr.getChildren();

		Map<String, QlnvTonghopHang> hashMapDMap = new HashMap<String, QlnvTonghopHang>();
		for (QlnvPhieuNhapxuatDtl nhapxuatDtl : dtls) {
			StringBuffer sbf = new StringBuffer();
			sbf.append(qNhapxuatHdr.getMaLo());
			sbf.append(qNhapxuatHdr.getMaNgan());
			sbf.append(qNhapxuatHdr.getMaKho());
			sbf.append(nhapxuatDtl.getMaHhoa());
			sbf.append(nhapxuatDtl.getDviTinh());
			if (!hashMapDMap.containsKey(sbf.toString())) {
				QlnvTonghopHang qTonghopHang = new QlnvTonghopHang();
				qTonghopHang.setIdPhieuNhapxuat(qNhapxuatHdr.getId());
				qTonghopHang.setMaDvi(qNhapxuatHdr.getMaDvi());
				qTonghopHang.setMaKho(qNhapxuatHdr.getMaKho());
				qTonghopHang.setMaNgan(qNhapxuatHdr.getMaNgan());
				qTonghopHang.setMaLo(qNhapxuatHdr.getMaLo());
				qTonghopHang.setMaHanghoa(nhapxuatDtl.getMaHhoa());
				qTonghopHang.setDviTinh(nhapxuatDtl.getDviTinh());
				qTonghopHang.setTongXuat(nhapxuatDtl.getSoLuongThucte());

				hashMapDMap.put(sbf.toString(), qTonghopHang);
			} else {
				BigDecimal tongXuat = hashMapDMap.get(sbf.toString()).getTongXuat();
				hashMapDMap.get(sbf.toString()).setTongXuat(tongXuat.add(nhapxuatDtl.getSoLuongThucte()));
			}
		}

		for (String key : hashMapDMap.keySet()) {
			QlnvTonghopHang thHang = hashMapDMap.get(key);
			if (!handlingNumberProduct.checkProduct(qNhapxuatHdr.getMaLo(), qNhapxuatHdr.getMaNgan(),
					qNhapxuatHdr.getMaKho(), thHang.getMaHanghoa(), thHang.getDviTinh()))
				qlnvTonghopKhoRepository.save(hashMapDMap.get(key));
			else
				handlingNumberProduct.minusProduct(thHang.getTongXuat(), qNhapxuatHdr.getMaLo(),
						qNhapxuatHdr.getMaNgan(), qNhapxuatHdr.getMaKho(), thHang.getMaHanghoa(), thHang.getDviTinh());
		}
	}

}
