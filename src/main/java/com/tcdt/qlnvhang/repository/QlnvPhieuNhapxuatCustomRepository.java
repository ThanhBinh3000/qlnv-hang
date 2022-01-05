package com.tcdt.qlnvhang.repository;

import javax.persistence.EntityManager;

import com.tcdt.qlnvhang.table.QlnvPhieuNhapxuatHdr;
import com.tcdt.qlnvhang.table.QlnvTonghopHang;
import com.tcdt.qlnvhang.util.Contains;

public class QlnvPhieuNhapxuatCustomRepository {
	private EntityManager entityManager;

	public QlnvPhieuNhapxuatCustomRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void updateApprove(QlnvPhieuNhapxuatHdr objHdr) {
		// Retrieve the movie with this ID
		if (objHdr != null) {
			try {
				// Start a transaction because we're going to change the database
				entityManager.getTransaction().begin();

				// Update trang thai phe duyet
				entityManager.persist(objHdr);
				if (objHdr.getTrangThai().equals(Contains.DUYET)) {
					QlnvTonghopHang qTonghopHang = new QlnvTonghopHang();
					qTonghopHang.setIdPhieuNhapxuat(objHdr.getId());
					qTonghopHang.setMaDvi(objHdr.getMaDvi());
					qTonghopHang.setMaKho(objHdr.getMaKho());
					qTonghopHang.setMaNgan(objHdr.getMaNgan());
					qTonghopHang.setMaLo(objHdr.getMaLo());
				}

				// Commit the transaction
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}