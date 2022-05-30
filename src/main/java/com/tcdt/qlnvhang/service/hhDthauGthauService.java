package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.object.HhDthauGthauReq;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class hhDthauGthauService {

	@Autowired
	private HhDthauGthauRepository hhDthauGthauRepository;

	@Transactional(rollbackOn = Exception.class)
	public HhDthauGthau create(HhDthauGthauReq objReq){
		HhDthauGthau dataMap = ObjectMapperUtils.map(objReq, HhDthauGthau.class);
		return hhDthauGthauRepository.save(dataMap);
	}

	public HhDthauGthau detail(String id){
		Optional<HhDthauGthau> optional = hhDthauGthauRepository.findById(Long.parseLong(id));
		return optional.get();
	}

	public HhDthauGthau detailByIdGoiThau(String id){
		Optional<HhDthauGthau> optional = hhDthauGthauRepository.findByIdGoiThau(Long.parseLong(id));
		return optional.get();
	}

}