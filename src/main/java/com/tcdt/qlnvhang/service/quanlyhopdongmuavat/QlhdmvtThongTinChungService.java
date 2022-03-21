package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtThongTinChung;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdmvtThongTinChungRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtThongTinChungDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdmvtThongTinChungService {

	@Autowired
	private QlhdmvtThongTinChungRepository qlhdmvtThongTinChungRepository;

	public String save(QlhdmvtThongTinChungVO vO) {
		QlhdmvtThongTinChung bean = new QlhdmvtThongTinChung();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdmvtThongTinChungRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdmvtThongTinChungRepository.deleteById(id);
	}

	public void update(String id, QlhdmvtThongTinChungUpdateVO vO) {
		QlhdmvtThongTinChung bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdmvtThongTinChungRepository.save(bean);
	}

	public QlhdmvtThongTinChungDTO getById(String id) {
		QlhdmvtThongTinChung original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdmvtThongTinChungDTO> query(QlhdmvtThongTinChungQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdmvtThongTinChungDTO toDTO(QlhdmvtThongTinChung original) {
		QlhdmvtThongTinChungDTO bean = new QlhdmvtThongTinChungDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdmvtThongTinChung requireOne(String id) {
		return qlhdmvtThongTinChungRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
