package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVt;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdmvtDiaDiemNhapVtService {

	@Autowired
	private QlhdmvtDiaDiemNhapVtRepository qlhdmvtDiaDiemNhapVtRepository;

	public String save(QlhdmvtDiaDiemNhapVtVO vO) {
		QlhdmvtDiaDiemNhapVt bean = new QlhdmvtDiaDiemNhapVt();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdmvtDiaDiemNhapVtRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdmvtDiaDiemNhapVtRepository.deleteById(id);
	}

	public void update(String id, QlhdmvtDiaDiemNhapVtUpdateVO vO) {
		QlhdmvtDiaDiemNhapVt bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdmvtDiaDiemNhapVtRepository.save(bean);
	}

	public QlhdmvtDiaDiemNhapVtDTO getById(String id) {
		QlhdmvtDiaDiemNhapVt original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdmvtDiaDiemNhapVtDTO> query(QlhdmvtDiaDiemNhapVtQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdmvtDiaDiemNhapVtDTO toDTO(QlhdmvtDiaDiemNhapVt original) {
		QlhdmvtDiaDiemNhapVtDTO bean = new QlhdmvtDiaDiemNhapVtDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdmvtDiaDiemNhapVt requireOne(String id) {
		return qlhdmvtDiaDiemNhapVtRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
