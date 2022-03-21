package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtTtChuDauTu;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdmvtTtChuDauTuService {

	@Autowired
	private QlhdmvtTtChuDauTuRepository qlhdmvtTtChuDauTuRepository;

	public String save(QlhdmvtTtChuDauTuVO vO) {
		QlhdmvtTtChuDauTu bean = new QlhdmvtTtChuDauTu();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdmvtTtChuDauTuRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdmvtTtChuDauTuRepository.deleteById(id);
	}

	public void update(String id, QlhdmvtTtChuDauTuUpdateVO vO) {
		QlhdmvtTtChuDauTu bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdmvtTtChuDauTuRepository.save(bean);
	}

	public QlhdmvtTtChuDauTuDTO getById(String id) {
		QlhdmvtTtChuDauTu original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdmvtTtChuDauTuDTO> query(QlhdmvtTtChuDauTuQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdmvtTtChuDauTuDTO toDTO(QlhdmvtTtChuDauTu original) {
		QlhdmvtTtChuDauTuDTO bean = new QlhdmvtTtChuDauTuDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdmvtTtChuDauTu requireOne(String id) {
		return qlhdmvtTtChuDauTuRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
