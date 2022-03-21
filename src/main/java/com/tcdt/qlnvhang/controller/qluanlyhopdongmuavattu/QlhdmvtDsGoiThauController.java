package com.tcdt.qlnvhang.controller.qluanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauQueryVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauUpdateVO;
import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdmvtDsGoiThauVO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdmvtDsGoiThauDTO;
import com.tcdt.qlnvhang.service.quanlyhopdongmuavat.QlhdmvtDsGoiThauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/qlhdmvtDsGoiThau")
public class QlhdmvtDsGoiThauController {

	@Autowired
	private QlhdmvtDsGoiThauService qlhdmvtDsGoiThauService;

	@PostMapping
	public String save(@Valid @RequestBody QlhdmvtDsGoiThauVO vO) {
		return qlhdmvtDsGoiThauService.save(vO).toString();
	}

	@DeleteMapping("/{id}")
	public void delete(@Valid @NotNull @PathVariable("id") String id) {
		qlhdmvtDsGoiThauService.delete(id);
	}

	@PutMapping("/{id}")
	public void update(@Valid @NotNull @PathVariable("id") String id,
					   @Valid @RequestBody QlhdmvtDsGoiThauUpdateVO vO) {
		qlhdmvtDsGoiThauService.update(id, vO);
	}

	@GetMapping("/{id}")
	public QlhdmvtDsGoiThauDTO getById(@Valid @NotNull @PathVariable("id") String id) {
		return qlhdmvtDsGoiThauService.getById(id);
	}

	@GetMapping
	public Page<QlhdmvtDsGoiThauDTO> query(@Valid QlhdmvtDsGoiThauQueryVO vO) {
		return qlhdmvtDsGoiThauService.query(vO);
	}
}
