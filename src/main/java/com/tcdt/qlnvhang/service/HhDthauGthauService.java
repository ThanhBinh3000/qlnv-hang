package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.repository.*;
import com.tcdt.qlnvhang.request.object.HhDthauGthauReq;
import com.tcdt.qlnvhang.request.object.HhDthauNthauDuthauReq;
import com.tcdt.qlnvhang.request.object.HhDxuatKhLcntDsgthauDtlCtietReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HhDthauGthauService extends BaseServiceImpl {

	@Autowired
	private HhDthauGthauRepository hhDthauGthauRepository;

	@Autowired
	HhDthauNthauDuthauRepository hhDthauNthauDuthauRepository;

	@Autowired
	HhDthauDdiemNhapRepository hhDthauDdiemNhapRepository;

	@Autowired
	HhQdKhlcntDsgthauRepository hhQdKhlcntDsgthauRepository;

	@Autowired
	private HttpServletRequest request;

	@Transactional(rollbackOn = Exception.class)
	public HhDthauGthau create(HhDthauGthauReq objReq){

 		HhDthauGthau dataMap = new ModelMapper().map(objReq, HhDthauGthau.class);

	    hhDthauGthauRepository.save(dataMap);

		hhQdKhlcntDsgthauRepository.updateGoiThau(dataMap.getIdGoiThau(), objReq.getTrangThaiLuu(),null);

//		if(objReq.getTrangThaiLuu().equals(Contains.HOANTHANHCAPNHAT)){
//			hhQdKhlcntDsgthauRepository.updateGoiThau(dataMap.getIdGoiThau(), Contains.HOANTHANHCAPNHAT,null);
//		}else{
//			hhQdKhlcntDsgthauRepository.updateGoiThau(dataMap.getIdGoiThau(), Contains.DANGCAPNHAT,null);
//		}

		// Delete all and add new nha thau
		hhDthauNthauDuthauRepository.deleteAllByIdDtGt(dataMap.getIdGoiThau());
		for(HhDthauNthauDuthauReq nthauDthau : objReq.getNthauDuThauList()){
			HhDthauNthauDuthau dataDtl = new ModelMapper().map(nthauDthau, HhDthauNthauDuthau.class);
			dataDtl.setIdDtGt(dataMap.getIdGoiThau());
			hhDthauNthauDuthauRepository.save(dataDtl);
		}

		for(HhDxuatKhLcntDsgthauDtlCtietReq ddNhap : objReq.getDiaDiemNhap()){
			HhDthauDdiemNhap dd = new ModelMapper().map(ddNhap, HhDthauDdiemNhap.class);
			dd.setIdDtGt(dataMap.getIdGoiThau());
			hhDthauDdiemNhapRepository.save(dd);
		}

		return dataMap;
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
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

		optional.get().setNthauDuThauList(hhDthauNthauDuthauRepository.findByIdDtGt(optional.get().getIdGoiThau()));
		optional.get().setDiaDiemNhapList(hhDthauDdiemNhapRepository.findByIdDtGt(optional.get().getIdGoiThau()));
		List<HhDthauDdiemNhap> dDiemNhap = hhDthauDdiemNhapRepository.findByIdDtGt(optional.get().getIdGoiThau());
		dDiemNhap.forEach(s->{
			s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
			s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()));
		});
		optional.get().setDiaDiemNhapList(dDiemNhap);
		return optional.get();
	}

}