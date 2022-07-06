package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntDtlCtietRepository;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntDtlRepository;
import com.tcdt.qlnvhang.repository.HhDchinhDxKhLcntHdrRepository;
import com.tcdt.qlnvhang.repository.HhDxuatKhLcntHdrRepository;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.DchinhDxKhLcntDtlReq;
import com.tcdt.qlnvhang.request.object.DchinhDxKhLcntHdrReq;
import com.tcdt.qlnvhang.request.search.QlnvQdLcntHdrDChinhSearchReq;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.UnitScaler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.tcdt.qlnvhang.service.SecurityContextService.getUser;
import static com.tcdt.qlnvhang.service.impl.BaseServiceImpl.convertDateToString;
import static com.tcdt.qlnvhang.service.impl.BaseServiceImpl.getDateTimeNow;

@Service
public class DchinhDxuatKhLcntService {

	@Autowired
	private HhDchinhDxKhLcntHdrRepository hdrRepository;

	@Autowired
	private HhDchinhDxKhLcntDtlRepository dtlRepository;

	@Autowired
	private HhDchinhDxKhLcntDtlCtietRepository dtlCtietRepository;

	@Autowired
	private HhDxuatKhLcntHdrService hhDxuatKhLcntHdrService;

	@Autowired
	private HhDxuatKhLcntHdrRepository hhDxuatKhLcntHdrRepository;

	public Page<HhDchinhDxKhLcntHdr> getAllPage(QlnvQdLcntHdrDChinhSearchReq objReq) throws Exception {
		int page = objReq.getPaggingReq().getPage();
		int limit = objReq.getPaggingReq().getLimit();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		return hdrRepository.selectPage(objReq.getNamKh(),objReq.getSoQdinh(), objReq.getTrichYeu(), convertDateToString(objReq.getTuNgayQd()),convertDateToString(objReq.getDenNgayQd()), pageable);
	}

	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr save (DchinhDxKhLcntHdrReq objReq) throws Exception {
		System.out.println(objReq.getSoQdinhGoc());
		Optional<HhDxuatKhLcntHdr> dXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(objReq.getSoQdinhGoc());
		if (!dXuat.isPresent()){
			throw new Exception("Không tìm thấy số đề xuất để điều chỉnh kế hoạch lựa chọn nhà thầu");
		}

		// Add danh sach file dinh kem o Master
		List<FileDKemJoinDxKhLcntHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhLcntHdr>();
		if (objReq.getFileDinhKem() != null) {
			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKem(), FileDKemJoinDxKhLcntHdr.class);
			fileDinhKemList.forEach(f -> {
				f.setDataType(HhDxuatKhLcntHdr.TABLE_NAME);
				f.setCreateDate(new Date());
			});
		}

		HhDchinhDxKhLcntHdr dataMap = new ModelMapper().map(objReq, HhDchinhDxKhLcntHdr.class);
		dataMap.setNgayTao(getDateTimeNow());
		dataMap.setTrangThai(Contains.TAO_MOI);
		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setFileDinhKem(fileDinhKemList);

		hdrRepository.save(dataMap);

		for(DchinhDxKhLcntDtlReq dCxDtlReq : objReq.getDetail()){
			HhDchinhDxKhLcntDtl dCxDtl =  new ModelMapper().map(dCxDtlReq, HhDchinhDxKhLcntDtl.class);
			dCxDtl.setIdHdr(dataMap.getId());
			dtlRepository.save(dCxDtl);
		}
		return dataMap;
	}


	@Transactional(rollbackOn = Exception.class)
	public HhDchinhDxKhLcntHdr approve (StatusReq stReq) throws Exception {
		if (StringUtils.isEmpty(stReq.getId()))
			throw new Exception("Không tìm thấy dữ liệu");
		Optional<HhDchinhDxKhLcntHdr> qdLcnt = hdrRepository.findById(Long.valueOf(stReq.getId()));
		if (!qdLcnt.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		qdLcnt.get().setTrangThai(stReq.getTrangThai());
		String status = stReq.getTrangThai() + qdLcnt.get().getTrangThai();
		switch(status) {
			case Contains.CHO_DUYET + Contains.MOI_TAO:
			case Contains.CHO_DUYET + Contains.TU_CHOI:
				qdLcnt.get().setNguoiGuiDuyet(getUser().getUsername());
				qdLcnt.get().setNgayGuiDuyet(getDateTimeNow());
				break;
			case Contains.TU_CHOI + Contains.CHO_DUYET:
				qdLcnt.get().setLdoTuchoi(stReq.getLyDo());
				break;
			case Contains.DUYET + Contains.CHO_DUYET:
				qdLcnt.get().setNguoiPduyet(getUser().getUsername());
				qdLcnt.get().setNgayPduyet(getDateTimeNow());
			case Contains.BAN_HANH + Contains.DUYET:
				qdLcnt.get().setNguoiPduyet(getUser().getUsername());
				qdLcnt.get().setNgayPduyet(getDateTimeNow());
				break;
			default:
				break;
		}
		if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
			Optional<HhDxuatKhLcntHdr> dXuat = hhDxuatKhLcntHdrRepository.findBySoDxuat(qdLcnt.get().getSoQdinhGoc());
			dXuat.get().setLastest(false);
			hhDxuatKhLcntHdrRepository.save(dXuat.get());
			HhDxuatKhLcntHdr dXuatNew = new ModelMapper().map(qdLcnt.get(), HhDxuatKhLcntHdr.class);
			dXuatNew.setLastest(true);
			dXuatNew.setId(null);
			hhDxuatKhLcntHdrRepository.save(dXuatNew);
		}
		return hdrRepository.save(qdLcnt.get());
	}



}