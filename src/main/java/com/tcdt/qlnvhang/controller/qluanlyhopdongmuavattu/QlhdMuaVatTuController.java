package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdMuaVatTuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdMuaVatTu")
public class QlhdMuaVatTuController {

	@Autowired
	private QlhdMuaVatTuService qlhdMuaVatTuService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdMuaVatTuVO vO) {
		return qlhdMuaVatTuService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdMuaVatTuService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdMuaVatTuUpdateVO vO) {
		qlhdMuaVatTuService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdMuaVatTuDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdMuaVatTuService.getById(id);
	}

	@GetMapping
	public Page<QlhdMuaVatTuDTO> query(@Valid QlhdMuaVatTuQueryVO vO) {
		return qlhdMuaVatTuService.query(vO);
	}
}
