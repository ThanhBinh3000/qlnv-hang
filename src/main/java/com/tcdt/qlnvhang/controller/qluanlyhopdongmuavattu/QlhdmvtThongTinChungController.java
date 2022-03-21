package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtThongTinChungVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtThongTinChungDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdmvtThongTinChungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdmvtThongTinChung")
public class QlhdmvtThongTinChungController {

	@Autowired
	private QlhdmvtThongTinChungService qlhdmvtThongTinChungService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdmvtThongTinChungVO vO) {
		return qlhdmvtThongTinChungService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdmvtThongTinChungService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdmvtThongTinChungUpdateVO vO) {
		qlhdmvtThongTinChungService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdmvtThongTinChungDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdmvtThongTinChungService.getById(id);
	}

	@GetMapping
	public Page<QlhdmvtThongTinChungDTO> query(@Valid QlhdmvtThongTinChungQueryVO vO) {
		return qlhdmvtThongTinChungService.query(vO);
	}
}
