package com.tcdt.qlnvhang.service.quanlyhopdongmuavat;

import com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu.QlhdMuaVatTu;
import com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu.QlhdMuaVatTuRepository;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class QlhdMuaVatTuService {

	@Autowired
	private QlhdMuaVatTuRepository qlhdMuaVatTuRepository;

	public String save(QlhdMuaVatTuVO vO) {
		QlhdMuaVatTu bean = new QlhdMuaVatTu();
		BeanUtils.copyProperties(vO, bean);
		bean = qlhdMuaVatTuRepository.save(bean);
		return bean.getID();
	}

	public void delete(String id) {
		qlhdMuaVatTuRepository.deleteById(id);
	}

	public void update(String id, QlhdMuaVatTuUpdateVO vO) {
		QlhdMuaVatTu bean = requireOne(id);
		BeanUtils.copyProperties(vO, bean);
		qlhdMuaVatTuRepository.save(bean);
	}

	public QlhdMuaVatTuDTO getById(String id) {
		QlhdMuaVatTu original = requireOne(id);
		return toDTO(original);
	}

	public Page<QlhdMuaVatTuDTO> query(QlhdMuaVatTuQueryVO vO) {
		throw new UnsupportedOperationException();
	}

	private QlhdMuaVatTuDTO toDTO(QlhdMuaVatTu original) {
		QlhdMuaVatTuDTO bean = new QlhdMuaVatTuDTO();
		BeanUtils.copyProperties(original, bean);
		return bean;
	}

	private QlhdMuaVatTu requireOne(String id) {
		return qlhdMuaVatTuRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
	}
}
