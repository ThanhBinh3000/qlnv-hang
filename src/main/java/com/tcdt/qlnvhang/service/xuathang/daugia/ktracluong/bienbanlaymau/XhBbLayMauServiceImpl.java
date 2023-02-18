package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class XhBbLayMauServiceImpl extends BaseServiceImpl implements XhBbLayMauService {

	private final XhBbLayMauRepository xhBbLayMauRepository;

	private final FileDinhKemService fileDinhKemService;

	private final XhBbLayMauCtRepository xhBbLayMauCtRepository;

	private final UserInfoRepository userInfoRepository;

	@Override
	public Page<XhBbLayMau> searchPage(XhBbLayMauRequest req) throws Exception {
		Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
				req.getPaggingReq().getLimit(), Sort.by("id").descending());
		Page<XhBbLayMau> data = xhBbLayMauRepository.searchPage( req,pageable);
		return data;
	}

	@Override
	public XhBbLayMau create(XhBbLayMauRequest req) throws Exception {

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		XhBbLayMau data = new XhBbLayMau();
		BeanUtils.copyProperties(req,data,"id");

		data.setNguoiTaoId(userInfo.getId());
		data.setNgayTao(new Date());
		data.setTrangThai(Contains.DUTHAO);
		data.setMaDvi(userInfo.getDvql());
		data.setNam(LocalDate.now().getYear());
		data.setId(Long.parseLong(data.getSoBienBan().split("/")[0]));
		data.setIdKtv(userInfo.getId());
		xhBbLayMauRepository.save(data);

		saveDetail(req,data.getId());
		return data;
	}

	void saveDetail(XhBbLayMauRequest req,Long idHdr){
		xhBbLayMauCtRepository.deleteAllByBbLayMauId(idHdr);
		for (XhBbLayMauCtRequest ctReq :req.getChildren()) {
			XhBbLayMauCt ct = new XhBbLayMauCt();
			BeanUtils.copyProperties(ctReq,ct,"id");
			ct.setBbLayMauId(idHdr);
			xhBbLayMauCtRepository.save(ct);
		}
	}

	@Override
	public XhBbLayMau update(XhBbLayMauRequest req) throws Exception {
		if(Objects.isNull(req)){
			throw new Exception("Bad reqeust");
		}

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) {
			throw new Exception("Bad request.");
		}

		Optional<XhBbLayMau> byId = xhBbLayMauRepository.findById(req.getId());
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		XhBbLayMau dataDb = byId.get();
		BeanUtils.copyProperties(req,dataDb,"id");
		dataDb.setNgaySua(new Date());
		dataDb.setNguoiSuaId(userInfo.getId());

		xhBbLayMauRepository.save(dataDb);
		this.saveDetail(req,dataDb.getId());
		return dataDb;
	}

	@Override
	public XhBbLayMau detail(Long id) throws Exception {
		if(Objects.isNull(id)){
			throw new Exception("Bad reqeust");
		}

		Optional<XhBbLayMau> byId = xhBbLayMauRepository.findById(id);
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		Map<String, String> mapDmucHh = getListDanhMucHangHoa();
		Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
		XhBbLayMau data = byId.get();

		data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
		data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));
		data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
		data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
		data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()));
		data.setTenNhaKho(mapDmucDvi.get(data.getMaNganKho()));
		data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()));
		data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()));
		data.setChildren(xhBbLayMauCtRepository.findByBbLayMauId(id));

		return data;
	}

	@Override
	public XhBbLayMau approve(XhBbLayMauRequest req) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();


		if(Objects.isNull(req.getId())){
			throw new Exception("Bad reqeust");
		}

		if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
			throw new Exception("Bad Request");
		}

		Optional<XhBbLayMau> byId = xhBbLayMauRepository.findById(req.getId());
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}
		XhBbLayMau data = byId.get();
		String status = req.getTrangThai() + data.getTrangThai();
		switch (status) {
			case Contains.CHODUYET_LDCC + Contains.DUTHAO:
			case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
				data.setNguoiGuiDuyetId(userInfo.getId());
				data.setNgayGuiDuyet(new Date());
				break;
			case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
				data.setNguoiPduyetId(userInfo.getId());
				data.setNgayPduyet(new Date());
				data.setLyDoTuChoi(req.getLyDoTuChoi());
				break;
			case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
				data.setNguoiPduyetId(userInfo.getId());
				data.setNgayPduyet(new Date());
				break;
			default:
				throw new Exception("Phê duyệt không thành công");
		}
		data.setTrangThai(req.getTrangThai());
		xhBbLayMauRepository.save(data);
		return data;
	}

	@Override
	public void delete(Long id) throws Exception {
		if(Objects.isNull(id)){
			throw new Exception("Bad request");
		}

		Optional<XhBbLayMau> byId = xhBbLayMauRepository.findById(id);
		if(!byId.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu");
		}

		xhBbLayMauRepository.delete(byId.get());
		xhBbLayMauCtRepository.deleteAllByBbLayMauId(byId.get().getId());
	}

	@Override
	public void deleteMulti(List<Long> listMulti) throws Exception {

	}

	@Override
	public void export(XhBbLayMauRequest req, HttpServletResponse response) throws Exception {

	}

}
