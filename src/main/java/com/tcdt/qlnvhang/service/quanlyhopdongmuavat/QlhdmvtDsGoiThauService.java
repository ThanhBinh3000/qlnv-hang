package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdmvtDsGoiThau;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdmvtDsGoiThauRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtDsGoiThauDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdmvtDsGoiThauService {

	@Autowired
	private QlhdmvtDsGoiThauRepository qlhdmvtDsGoiThauRepository;

	public String save(QlhdmvtDsGoiThauVO vO) {
		QlhdmvtDsGoiThau bean = new QlhdmvtDsGoiThau();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdmvtDsGoiThauRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdmvtDsGoiThauRepository.deleteById(id);
	}

	public void update(String id, QlhdmvtDsGoiThauUpdateVO vO) {
		QlhdmvtDsGoiThau bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdmvtDsGoiThauRepository.save(bean);
	}

	public QlhdmvtDsGoiThauDTO getById(String id) {
		QlhdmvtDsGoiThau original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdmvtDsGoiThauDTO> query(QlhdmvtDsGoiThauQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdmvtDsGoiThauDTO toDTO(QlhdmvtDsGoiThau original) {
		QlhdmvtDsGoiThauDTO bean = new QlhdmvtDsGoiThauDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdmvtDsGoiThau requireOne(String id) {
		return qlhdmvtDsGoiThauRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
