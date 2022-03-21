package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtTtDonViCc;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdmvtTtDonViCcRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtTtDonViCcDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdmvtTtDonViCcService {

	@Autowired
	private QlhdmvtTtDonViCcRepository qlhdmvtTtDonViCcRepository;

	public String save(QlhdmvtTtDonViCcVO vO) {
		QlhdmvtTtDonViCc bean = new QlhdmvtTtDonViCc();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdmvtTtDonViCcRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdmvtTtDonViCcRepository.deleteById(id);
	}

	public void update(String id, QlhdmvtTtDonViCcUpdateVO vO) {
		QlhdmvtTtDonViCc bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdmvtTtDonViCcRepository.save(bean);
	}

	public QlhdmvtTtDonViCcDTO getById(String id) {
		QlhdmvtTtDonViCc original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdmvtTtDonViCcDTO> query(QlhdmvtTtDonViCcQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdmvtTtDonViCcDTO toDTO(QlhdmvtTtDonViCc original) {
		QlhdmvtTtDonViCcDTO bean = new QlhdmvtTtDonViCcDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdmvtTtDonViCc requireOne(String id) {
		return qlhdmvtTtDonViCcRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
