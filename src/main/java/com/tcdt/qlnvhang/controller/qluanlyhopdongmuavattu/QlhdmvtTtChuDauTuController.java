package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtTtChuDauTuDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdmvtTtChuDauTuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdmvtTtChuDauTu")
public class QlhdmvtTtChuDauTuController {

	@Autowired
	private QlhdmvtTtChuDauTuService qlhdmvtTtChuDauTuService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdmvtTtChuDauTuVO vO) {
		return qlhdmvtTtChuDauTuService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdmvtTtChuDauTuService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdmvtTtChuDauTuUpdateVO vO) {
		qlhdmvtTtChuDauTuService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdmvtTtChuDauTuDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdmvtTtChuDauTuService.getById(id);
	}

	@GetMapping
	public Page<QlhdmvtTtChuDauTuDTO> query(@Valid QlhdmvtTtChuDauTuQueryVO vO) {
		return qlhdmvtTtChuDauTuService.query(vO);
	}
}
