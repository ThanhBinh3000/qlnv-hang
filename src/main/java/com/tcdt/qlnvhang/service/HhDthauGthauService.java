package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.object.HhDthauGthauReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
public class HhDthauGthauService extends BaseServiceImpl {

	@Autowired
	private HhDthauGthauRepository hhDthauGthauRepository;

	@Autowired
	private HttpServletRequest request;

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
		Map<String,String> mapVthh = getListDanhMucHangHoa();
		optional.get().setTenVthh( StringUtils.isEmpty(optional.get().getLoaiVthh()) ? null : mapVthh.get(optional.get().getLoaiVthh()));
		optional.get().setTenCloaiVthh( StringUtils.isEmpty(optional.get().getCloaiVthh()) ? null :mapVthh.get(optional.get().getCloaiVthh()));
		return optional.get();
	}

}