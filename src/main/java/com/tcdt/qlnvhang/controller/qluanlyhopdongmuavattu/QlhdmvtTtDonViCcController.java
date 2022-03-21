package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtTtDonViCcVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtTtDonViCcDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdmvtTtDonViCcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdmvtTtDonViCc")
public class QlhdmvtTtDonViCcController {

	@Autowired
	private QlhdmvtTtDonViCcService qlhdmvtTtDonViCcService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdmvtTtDonViCcVO vO) {
		return qlhdmvtTtDonViCcService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdmvtTtDonViCcService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdmvtTtDonViCcUpdateVO vO) {
		qlhdmvtTtDonViCcService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdmvtTtDonViCcDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdmvtTtDonViCcService.getById(id);
	}

	@GetMapping
	public Page<QlhdmvtTtDonViCcDTO> query(@Valid QlhdmvtTtDonViCcQueryVO vO) {
		return qlhdmvtTtDonViCcService.query(vO);
	}
}
