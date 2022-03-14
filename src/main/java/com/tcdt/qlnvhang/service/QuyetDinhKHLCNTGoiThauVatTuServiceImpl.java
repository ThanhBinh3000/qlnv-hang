package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.entities.QuyetDinhKHLCNTGoiThauVatTu;
import com.tcdt.qlnvhang.entities.QuyetDinhKHLCNTVatTu;
import com.tcdt.qlnvhang.repository.QuyetDinhKHLCNTGoiThauVatTuRepository;
import com.tcdt.qlnvhang.repository.QuyetDinhKHLCNTVatTuRepository;
import com.tcdt.qlnvhang.request.QuyetDinhKHLCNTGoiThauVatTuReq;
import com.tcdt.qlnvhang.request.QuyetDinhKHLCNTVatTuReq;
import com.tcdt.qlnvhang.response.QuyetDinhKHLCNTGoiThauVatTuRes;
import com.tcdt.qlnvhang.response.QuyetDinhKHLCNTVatTuRes;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuyetDinhKHLCNTGoiThauVatTuServiceImpl implements QuyetDinhKHLCNTGoiThauVatTuService {
	@Autowired
	private DataUtils dataUtils;

	@Autowired
	private QuyetDinhKHLCNTVatTuRepository quyetDinhKHLCNTVatTuRepo;

	@Autowired
	private QuyetDinhKHLCNTGoiThauVatTuRepository goiThauVatTuRepo;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QuyetDinhKHLCNTVatTuRes create(QuyetDinhKHLCNTVatTuReq req) throws Exception {
		if (req == null) throw new Exception("Bad Request");

		UserInfo userInfo = UserUtils.getUserInfo();

		QuyetDinhKHLCNTVatTu quyetDinh = dataUtils.mapObject(req, QuyetDinhKHLCNTVatTu.class);
		quyetDinh.setNgayTao(LocalDate.now());
		quyetDinh.setNguoiTaoId(userInfo.getId());

		quyetDinh = quyetDinhKHLCNTVatTuRepo.save(quyetDinh);

		QuyetDinhKHLCNTVatTuRes response = dataUtils.mapObject(quyetDinh, QuyetDinhKHLCNTVatTuRes.class);

		if (CollectionUtils.isEmpty(req.getGoiThau())) return response;

		List<QuyetDinhKHLCNTGoiThauVatTu> goiThauVatTuList = req.getGoiThau().stream().map(item -> {
			QuyetDinhKHLCNTGoiThauVatTu goiThauVatTu = dataUtils.mapObject(item, QuyetDinhKHLCNTGoiThauVatTu.class);
			goiThauVatTu.setNguoiTaoId(userInfo.getId());
			goiThauVatTu.setNgayTao(LocalDate.now());
			return goiThauVatTu;
		}).collect(Collectors.toList());

		goiThauVatTuList = goiThauVatTuRepo.saveAll(goiThauVatTuList);

		List<QuyetDinhKHLCNTGoiThauVatTuRes> goiThauVatTuRes = goiThauVatTuList.stream()
				.map(item -> dataUtils.mapObject(item, QuyetDinhKHLCNTGoiThauVatTuRes.class))
				.collect(Collectors.toList());

		response.setGoiThauList(goiThauVatTuRes);
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public QuyetDinhKHLCNTVatTuRes update(QuyetDinhKHLCNTVatTuReq req) throws Exception {
		if (req == null) throw new Exception("Bad Request");

		Optional<QuyetDinhKHLCNTVatTu> quyetDinhOpt = quyetDinhKHLCNTVatTuRepo.findById(req.getId());

		if (!quyetDinhOpt.isPresent())
			throw new Exception("Quyết định kế hoạch lựa chọn nhà thầu vật tư không tồn tại");

		UserInfo userInfo = UserUtils.getUserInfo();

		QuyetDinhKHLCNTVatTu quyetDinh = quyetDinhOpt.get();

		BeanUtils.copyProperties(req, quyetDinh);

		quyetDinh.setNguoiSuaId(userInfo.getId());
		quyetDinh.setNgaySua(LocalDate.now());

		quyetDinh = quyetDinhKHLCNTVatTuRepo.save(quyetDinh);

		QuyetDinhKHLCNTVatTuRes response = dataUtils.mapObject(quyetDinh, QuyetDinhKHLCNTVatTuRes.class);

		List<QuyetDinhKHLCNTGoiThauVatTuReq> goiThauListReq = req.getGoiThau();

		if (CollectionUtils.isEmpty(goiThauListReq)) return response;

		List<QuyetDinhKHLCNTGoiThauVatTu> goiThauVatTuList = goiThauVatTuRepo.findByQuyetDinhId(quyetDinh.getId());

		//Update quyết định KHLCNT vật tư Gói thầu
		Map<Long, QuyetDinhKHLCNTGoiThauVatTuReq> goiThauVatTuReqMap = goiThauListReq.stream()
				.collect(Collectors
						.toMap(QuyetDinhKHLCNTGoiThauVatTuReq::getId, Function.identity(), (existing, replacement) -> existing));

		for (QuyetDinhKHLCNTGoiThauVatTu goiThauVatTu : goiThauVatTuList) {
			QuyetDinhKHLCNTGoiThauVatTuReq goiThauVatTuReq = goiThauVatTuReqMap.get(goiThauVatTu.getId());

			//Xóa gói thầu nếu trong request không tồn tại
			if (goiThauVatTuReq == null) {
				goiThauVatTuRepo.delete(goiThauVatTu);
				continue;
			}

			BeanUtils.copyProperties(goiThauVatTuReq, goiThauVatTu);
		}

		goiThauVatTuList = goiThauVatTuRepo.saveAll(goiThauVatTuList);

		List<QuyetDinhKHLCNTGoiThauVatTuRes> goiThauVatTuRes = goiThauVatTuList
				.stream()
				.map(item -> dataUtils.mapObject(item, QuyetDinhKHLCNTGoiThauVatTuRes.class)).collect(Collectors.toList());

		response.setGoiThauList(goiThauVatTuRes);

		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(Long id) throws Exception {
		Optional<QuyetDinhKHLCNTVatTu> quyetDinhOpt = quyetDinhKHLCNTVatTuRepo.findById(id);

		if (!quyetDinhOpt.isPresent())
			throw new Exception("Quyết định kế hoạch lựa chọn nhà thầu vật tư không tồn tại");

		quyetDinhKHLCNTVatTuRepo.delete(quyetDinhOpt.get());
	}


}
