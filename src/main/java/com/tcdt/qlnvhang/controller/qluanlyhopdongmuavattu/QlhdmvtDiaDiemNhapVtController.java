package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtDiaDiemNhapVtDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdmvtDiaDiemNhapVtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdmvtDiaDiemNhapVt")
public class QlhdmvtDiaDiemNhapVtController {

	@Autowired
	private QlhdmvtDiaDiemNhapVtService qlhdmvtDiaDiemNhapVtService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdmvtDiaDiemNhapVtVO vO) {
		return qlhdmvtDiaDiemNhapVtService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdmvtDiaDiemNhapVtService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdmvtDiaDiemNhapVtUpdateVO vO) {
		qlhdmvtDiaDiemNhapVtService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdmvtDiaDiemNhapVtDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdmvtDiaDiemNhapVtService.getById(id);
	}

	@GetMapping
	public Page<QlhdmvtDiaDiemNhapVtDTO> query(@Valid QlhdmvtDiaDiemNhapVtQueryVO vO) {
		return qlhdmvtDiaDiemNhapVtService.query(vO);
	}
}
