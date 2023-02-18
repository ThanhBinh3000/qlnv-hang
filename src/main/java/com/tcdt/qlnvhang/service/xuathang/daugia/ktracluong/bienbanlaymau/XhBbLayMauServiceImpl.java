package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
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
import java.util.*;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class XhBbLayMauServiceImpl extends BaseServiceImpl implements XhBbLayMauService {

	private final XhBbLayMauRepository xhBbLayMauRepository;

	private final FileDinhKemService fileDinhKemService;

	private final XhBbLayMauCtRepository xhBbLayMauCtRepository;

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
		data.setNam(new Date().getYear());
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
		data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()));
		data.setTenNhaKho(mapDmucDvi.get(data.getMaNganKho()));
		data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()));
		data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()));
		data.setChildren(xhBbLayMauCtRepository.findByBbLayMauId(id));

		return data;
	}

	@Override
	public XhBbLayMau approve(XhBbLayMauRequest req) throws Exception {
		return null;
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


	/*@Override
	public XhBbLayMauResponse create(XhBbLayMauRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		XhBbLayMau theEntity = xhBbLayMauRequestMapper.toEntity(req);
		theEntity.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
		theEntity.setNgayTao(new Date());
		theEntity.setNguoiTaoId(userInfo.getId());
		theEntity.setMaDvi(userInfo.getDvql());
		theEntity.setCapDvi(userInfo.getCapDvi());
		theEntity.setSo(getSo());
		theEntity.setSoBienBan(String.format("%s/%s/%s-%s", theEntity.getSo(), theEntity.getNam(), "BBLM", userInfo.getMaPbb()));

		theEntity = xhBbLayMauRepository.save(theEntity);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), theEntity.getId(), XhBbLayMau.TABLE_NAME);
		theEntity.setFileDinhKems(fileDinhKems);


		if (!CollectionUtils.isEmpty(req.getChiTietList())) {
			theEntity.setChiTietList(this.saveChiTietList(req, theEntity));
		}
		XhBbLayMauResponse response = xhBbLayMauResponseMapper.toDto(theEntity);
		return response;
	}

	private List<XhBbLayMauCt> saveChiTietList(XhBbLayMauRequest req, XhBbLayMau theEntity) {
		//Clean data before save
		this.deleteChiTiets(Collections.singleton(theEntity.getId()));
		//Save chi tiết
		List<XhBbLayMauCt> chiTietList = ctRequestMapper.toEntity(req.getChiTietList());

		for (XhBbLayMauCt entry : chiTietList) {
			entry.setXhBbLayMauId(theEntity.getId());
		}
		chiTietList = ctRepository.saveAll(chiTietList);
		return chiTietList;
	}

	private void deleteChiTiets(Set<Long> xhBbLayMauIds) {
		List<XhBbLayMauCt> ctList = ctRepository.findByXhBbLayMauIdIn(xhBbLayMauIds);
		if (!CollectionUtils.isEmpty(ctList)) {
			ctRepository.deleteAll(ctList);
		}
	}


	@Override
	public XhBbLayMauResponse update(XhBbLayMauRequest req) throws Exception {
		if (req == null) return null;

		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");

		Optional<XhBbLayMau> optional = xhBbLayMauRepository.findById(req.getId());
		if (!optional.isPresent())
			throw new Exception("Biên bản lấy mẫu không tồn tại");
		XhBbLayMau theEntity = optional.get();

		log.info("Update Biên bản lấy mẫu");
		xhBbLayMauRequestMapper.partialUpdate(theEntity, req);

		theEntity.setNgaySua(new Date());
		theEntity.setNguoiSuaId(userInfo.getId());
		theEntity = xhBbLayMauRepository.save(theEntity);

		log.info("Save file dinh kem");
		List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), theEntity.getId(), XhBbLayMau.TABLE_NAME);
		theEntity.setFileDinhKems(fileDinhKems);

		theEntity.setChiTietList(this.saveChiTietList(req, theEntity));

		return xhBbLayMauResponseMapper.toDto(theEntity);
	}

	@Override
	public boolean delete(Long id) throws Exception {
		return this.deleteMultiple(Collections.singletonList(id));
	}

	@Override
	public boolean deleteMultiple(List<Long> ids) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		List<XhBbLayMau> xhBbLayMauList = xhBbLayMauRepository.findByIdIn(ids);
		if (CollectionUtils.isEmpty(xhBbLayMauList)) {
			throw new Exception("Biên bản lấy mẫu không tồn tại");
		}

		if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");
		log.info("Delete file dinh kem");
		fileDinhKemService.deleteMultiple(ids, Collections.singleton(XhBbLayMau.TABLE_NAME));

		log.info("Delete Chi tiết");
		this.deleteChiTiets(xhBbLayMauList.stream().map(XhBbLayMau::getId).collect(Collectors.toSet()));

		log.info("Delete Biên bản lấy mẫu");
		xhBbLayMauRepository.deleteAll(xhBbLayMauList);
		return true;
	}

	@Override
	public Page<XhBbLayMauSearchResponse> search(XhBbLayMauSearchRequest req) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		return xhBbLayMauRepository.search(req, req.getPageable());
	}

	@Override
	public XhBbLayMauResponse detail(Long id) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");


		Optional<XhBbLayMau> optional = xhBbLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Biên bản lấy mẫu không tồn tại");
		XhBbLayMau xhBbLayMau = optional.get();

		//Chi tiết
		List<XhBbLayMauCt> ctList = ctRepository.findByXhBbLayMauIdIn(Collections.singleton(xhBbLayMau.getId()));
		if (!CollectionUtils.isEmpty(ctList)) {
			xhBbLayMau.setChiTietList(ctList);
		}

		XhBbLayMauResponse response = xhBbLayMauResponseMapper.toDto(xhBbLayMau);
		//Trạng thái
		response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(xhBbLayMau.getTrangThai()));
		response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(xhBbLayMau.getTrangThai()));
		//Vật tư
		QlnvDmVattu vatTuCha = dmVattuRepository.findByMa(xhBbLayMau.getMaVatTuCha());
		if (vatTuCha != null) {
			response.setTenVatTuCha(vatTuCha.getTen());
		}

		QlnvDmVattu chungLoaiHangHoa = dmVattuRepository.findByMa(xhBbLayMau.getMaVatTu());
		if (chungLoaiHangHoa != null) {
			response.setTenVatTu(chungLoaiHangHoa.getTen());
		}
		//Đơn vị
		if (!StringUtils.isEmpty(response.getDonViKiemNghiem())) {
			response.setTenDonViKiemNghiem(this.getMapTenDvi().get(response.getDonViKiemNghiem()));
		}

		response.setFileDinhKems(fileDinhKemService.search(xhBbLayMau.getId(), Collections.singleton(XhBbLayMau.TABLE_NAME)));

		//Build thông tin kho
		this.buildThongTinKho(response);


		return response;
	}

	private void buildThongTinKho(XhBbLayMauResponse response) {
		KtDiemKho diemKho = ktDiemKhoRepository.findByMaDiemkho(response.getMaDiemKho());
		KtNhaKho nhaKho = ktNhaKhoRepository.findByMaNhakho(response.getMaNhaKho());
		KtNganKho nganKho = ktNganKhoRepository.findByMaNgankho(response.getMaNganKho());
		KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(response.getMaNganLo());
		if (diemKho != null) {
			response.setTenDiemKho(diemKho.getTenDiemkho());
		}
		if (nhaKho != null) {
			response.setTenNhaKho(nhaKho.getTenNhakho());
		}
		if (nganKho != null) {
			response.setTenNganKho(nganKho.getTenNgankho());
		}
		if (nganLo != null) {
			response.setTenNganKho(nganLo.getTenNganlo());
		}
	}

	@Override
	public XhBbLayMauResponse updateTrangThai(Long id, String trangThaiId) throws Exception {
		UserInfo userInfo = SecurityContextService.getUser();
		if (userInfo == null) throw new Exception("Bad request.");
		if (StringUtils.isEmpty(trangThaiId)) throw new Exception("trangThaiId không được để trống");

		Optional<XhBbLayMau> optional = xhBbLayMauRepository.findById(id);
		if (!optional.isPresent())
			throw new Exception("Biên bản lấy mẫu không tồn tại");
		XhBbLayMau xhBbLayMau = optional.get();
		//validate Trạng Thái
		String trangThai = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(trangThaiId);
		if (StringUtils.isEmpty(trangThai)) throw new Exception("Trạng thái không tồn tại");
		xhBbLayMau.setTrangThai(trangThaiId);
		xhBbLayMau = xhBbLayMauRepository.save(xhBbLayMau);
		return xhBbLayMauResponseMapper.toDto(xhBbLayMau);
	}

	@Override
	public boolean exportToExcel(XhBbLayMauSearchRequest req, HttpServletResponse response) throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
		req.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
		List<XhBbLayMauSearchResponse> list = this.search(req).get().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list))
			return true;

		String[] rowsName = new String[]{ExcelHeaderConst.STT,
				ExcelHeaderConst.SO_BIEN_BAN,
				ExcelHeaderConst.NGAY_LAY_MAU,
				ExcelHeaderConst.DIEM_KHO,
				ExcelHeaderConst.NHA_KHO,
				ExcelHeaderConst.NGAN_KHO,
				ExcelHeaderConst.LO_KHO,
				ExcelHeaderConst.TRANG_THAI
		};
		String filename = "bien_ban_lay_mau.xlsx";

		List<Object[]> dataList = new ArrayList<>();

		try {
			for (int i = 0; i < list.size(); i++) {
				dataList.add(list.get(i).toExcel(rowsName, i));
			}

			ExportExcel ex = new ExportExcel(SHEET_NAME, filename, rowsName, dataList, response);
			ex.export();
		} catch (Exception e) {
			log.error("Error export", e);
			return false;
		}
		return true;
	}

	@Override
	public Integer getSo() throws Exception {
		UserInfo userInfo = UserUtils.getUserInfo();
		Integer so = xhBbLayMauRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
		so = Optional.ofNullable(so).orElse(0);
		so = so + 1;
		return so;
	}*/
}
