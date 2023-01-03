package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbancbkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoRepository;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class NhBienBanChuanBiKhoServiceImpl extends BaseServiceImpl implements NhBienBanChuanBiKhoService {

    @Autowired
    private NhBienBanChuanBiKhoRepository nhBienBanChuanBiKhoRepository;

    @Autowired
    private final NhBienBanChuanBiKhoCtRepository nhBienBanChuanBiKhoCtRepository;

    @Autowired
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private final UserInfoRepository userInfoRepository;

    private final HttpServletRequest req;

    @Override
    public Page<NhBienBanChuanBiKho> searchPage(NhBienBanChuanBiKhoReq req) {
        return null;
    }

    @Override
    public List<NhBienBanChuanBiKho> searchAll(NhBienBanChuanBiKhoReq req) {
        return null;
    }

    @Override
    @Transactional
    public NhBienBanChuanBiKho create(NhBienBanChuanBiKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        NhBienBanChuanBiKho item = new NhBienBanChuanBiKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setIdKyThuatVien(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.parseLong(req.getSoBienBan().split("/")[0]));
        nhBienBanChuanBiKhoRepository.save(item);
        saveDetail(req,item.getId());
        return item;
    }

    @Transactional
    void saveDetail(NhBienBanChuanBiKhoReq req,Long id){
        nhBienBanChuanBiKhoCtRepository.deleteAllByIdBbChuanBiKho(id);
        for(NhBienBanChuanBiKhoCtReq ctReq : req.getChildren()){
            NhBienBanChuanBiKhoCt ct = new NhBienBanChuanBiKhoCt();
            BeanUtils.copyProperties(req,ct,"id");
            ct.setId(null);
            ct.setIdBbChuanBiKho(id);
            nhBienBanChuanBiKhoCtRepository.save(ct);
        }
    }

    @Override
    @Transactional
    public NhBienBanChuanBiKho update(NhBienBanChuanBiKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
        }

        NhBienBanChuanBiKho item = optional.get();
        if(!Objects.equals(item.getNguoiTaoId(), userInfo.getId())){
            throw new Exception("Bạn không có quyền sửa dữ liệu");
        }
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBienBanChuanBiKhoRepository.save(item);
        this.saveDetail(req,item.getId());
        return item;
    }

    @Override
    public NhBienBanChuanBiKho detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
        }
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        NhBienBanChuanBiKho item = optional.get();
        item.setChildren(nhBienBanChuanBiKhoCtRepository.findAllByIdBbChuanBiKho(item.getId()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenDiemKho(listDanhMucDvi.get(item.getMaDiemKho()));
        item.setTenNhaKho(listDanhMucDvi.get(item.getMaNhaKho()));
        item.setTenNganKho(listDanhMucDvi.get(item.getMaNganKho()));
        item.setTenLoKho(listDanhMucDvi.get(item.getMaLoKho()));
        item.setTenKyThuatVien(ObjectUtils.isEmpty(item.getIdKyThuatVien()) ? null : userInfoRepository.findById(item.getIdKyThuatVien()).get().getFullName());
        item.setTenThuKho(ObjectUtils.isEmpty(item.getIdThuKho()) ? null : userInfoRepository.findById(item.getIdThuKho()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? null : userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        return item;
    }

    @Override
    public NhBienBanChuanBiKho approve(NhBienBanChuanBiKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }

        NhBienBanChuanBiKho item = optional.get();
        String trangThai = req.getTrangThai() + item.getTrangThai();

        if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId()).equals(trangThai)
        ) {
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId()).equals(trangThai)
        ) {
            item.setIdThuKho(userInfo.getId());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai)
        ) {
            item.setNgayPduyet(new Date());
            item.setNguoiPduyetId(userInfo.getId());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        item.setTrangThai(req.getTrangThai());
        nhBienBanChuanBiKhoRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
        }

        NhBienBanChuanBiKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }
        nhBienBanChuanBiKhoCtRepository.deleteAllByIdBbChuanBiKho(item.getId());
        nhBienBanChuanBiKhoRepository.delete(item);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(NhBienBanChuanBiKhoReq req, HttpServletResponse response) throws Exception {
        return false;
    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBienBanChuanBiKhoRes create(NhBienBanChuanBiKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoBb(null, req);
//
//        NhBienBanChuanBiKho item = new NhBienBanChuanBiKho();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBCBK", userInfo.getMaPbb()));
//        nhBienBanChuanBiKhoRepository.save(item);
//
//        List<NhBienBanChuanBiKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//
//        return this.buildResponse(item);
//    }
//
//    private NhBienBanChuanBiKhoRes buildResponse(NhBienBanChuanBiKho item) throws Exception {
//        NhBienBanChuanBiKhoRes res = new NhBienBanChuanBiKhoRes();
//        List<NhBienBanChuanBiKhoCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhBienBanChuanBiKhoCt ct : item.getChiTiets()) {
//            chiTiets.add(new NhBienBanChuanBiKhoCtRes(ct));
//        }
//        res.setChiTiets(chiTiets);
//        String tongSo = Optional.ofNullable(item.getTongSo()).map(BigDecimal::toString).orElse(BigDecimal.ZERO.toString());
//        res.setTongSoBangChu(MoneyConvert.doctienBangChu(tongSo, null));
//        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
//        if (!CollectionUtils.isEmpty(maVatTus)) {
//            Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
//            if (CollectionUtils.isEmpty(vatTus))
//                throw new Exception("Không tìm thấy vật tư");
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
//                    .ifPresent(v -> res.setTenVatTu(v.getTen()));
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
//                    .ifPresent(v -> res.setTenVatTuCha(v.getTen()));
//        }
//
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        if (item.getHopDongId() != null) {
//            Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(item.getHopDongId());
//            if (!hopDong.isPresent()) {
//                throw new Exception("Không tìm thấy hợp đồng");
//            }
//            res.setSoHopDong(hopDong.get().getSoHd());
//        }
//
//        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
//        res.setMaDvi(donvi.getMaDvi());
//        res.setTenDvi(donvi.getTenDvi());
//        res.setMaQhns(donvi.getMaQhns());
//        return res;
//    }
//
//    private List<NhBienBanChuanBiKhoCt> saveListChiTiet(Long parentId,
//                                                     List<NhBienBanChuanBiKhoCtReq> chiTietReqs,
//                                                     Map<Long, NhBienBanChuanBiKhoCt> mapChiTiet) throws Exception {
//        List<NhBienBanChuanBiKhoCt> chiTiets = new ArrayList<>();
//        for (NhBienBanChuanBiKhoCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhBienBanChuanBiKhoCt chiTiet = new NhBienBanChuanBiKhoCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setBbChuanBiKhoId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            nhBienBanChuanBiKhoCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBienBanChuanBiKhoRes update(NhBienBanChuanBiKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//
//        NhBienBanChuanBiKho item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        nhBienBanChuanBiKhoRepository.save(item);
//        Map<Long, NhBienBanChuanBiKhoCt> mapChiTiet = nhBienBanChuanBiKhoCtRepository.findByBbChuanBiKhoIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhBienBanChuanBiKhoCt::getId, Function.identity()));
//
//        List<NhBienBanChuanBiKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            nhBienBanChuanBiKhoCtRepository.deleteAll(mapChiTiet.values());
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhBienBanChuanBiKhoRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        NhBienBanChuanBiKho item = optional.get();
//        item.setChiTiets(nhBienBanChuanBiKhoCtRepository.findByBbChuanBiKhoIdIn(Collections.singleton(item.getId())));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        NhBienBanChuanBiKho item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa biên bản đã đã duyệt");
//        }
//        nhBienBanChuanBiKhoCtRepository.deleteByBbChuanBiKhoIdIn(Collections.singleton(item.getId()));
//        nhBienBanChuanBiKhoRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        NhBienBanChuanBiKho bb = optional.get();
//
//        String trangThai = bb.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId());
//            bb.setNguoiGuiDuyetId(userInfo.getId());
//            bb.setNgayGuiDuyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            bb.setNguoiGuiDuyetId(userInfo.getId());
//            bb.setNgayGuiDuyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//            bb.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//            bb.setLyDoTuChoi(stReq.getLyDo());
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        nhBienBanChuanBiKhoRepository.save(bb);
//
//        return true;
//    }
//
//    @Override
//    public Page<NhBienBanChuanBiKhoRes> search(NhBienBanChuanBiKhoSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = nhBienBanChuanBiKhoRepository.search(req);
//        List<NhBienBanChuanBiKhoRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhBienBanChuanBiKhoRes response = new NhBienBanChuanBiKhoRes();
//            NhBienBanChuanBiKho item = (NhBienBanChuanBiKho) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            String tenVatTu = (String) o[3];
//            String tenVatTuCha = (String) o[4];
//            KtNganLo nganLo = (KtNganLo) o[5];
//            Long hopDongId = o[6] != null ? (Long) o[6] : null;
//            String soHopDong = o[7] != null ? (String) o[7] : null;
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setTenVatTu(tenVatTu);
//            response.setTenVatTuCha(tenVatTuCha);
//            response.setHopDongId(hopDongId);
//            response.setSoHopDong(soHopDong);
//            this.thongTinNganLo(response, nganLo);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, nhBienBanChuanBiKhoRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        nhBienBanChuanBiKhoCtRepository.deleteByBbChuanBiKhoIdIn(req.getIds());
//        nhBienBanChuanBiKhoRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhBienBanChuanBiKhoSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhBienBanChuanBiKhoRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY,
//                LOAI_HANG_HOA, CHUNG_LOAI_HANG_HOA, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_bien_ban_chuan_bi_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhBienBanChuanBiKhoRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayTao());
//                objs[4] = item.getTenVatTuCha();
//                objs[5] = item.getTenVatTu();
//                objs[6] = item.getTenDiemKho();
//                objs[7] = item.getTenNhaKho();
//                objs[8] = item.getTenNganKho();
//                objs[9] = item.getTenNganLo();
//                objs[10] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_CHUAN_BI_KHO, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhBienBanChuanBiKhoRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoBb(NhBienBanChuanBiKho update, NhBienBanChuanBiKhoReq req) throws Exception {
//        String so = req.getSoBienBan();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
//            Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findFirstBySoBienBan(so);
//            Long updateId = Optional.ofNullable(update).map(NhBienBanChuanBiKho::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(NhBienBanChuanBiKhoRes item, KtNganLo nganLo) {
//        if (nganLo != null) {
//            item.setTenNganLo(nganLo.getTenNganlo());
//            KtNganKho nganKho = nganLo.getParent();
//            if (nganKho == null)
//                return;
//
//            item.setTenNganKho(nganKho.getTenNgankho());
//            item.setMaNganKho(nganKho.getMaNgankho());
//            KtNhaKho nhaKho = nganKho.getParent();
//            if (nhaKho == null)
//                return;
//
//            item.setTenNhaKho(nhaKho.getTenNhakho());
//            item.setMaNhaKho(nhaKho.getMaNhakho());
//            KtDiemKho diemKho = nhaKho.getParent();
//            if (diemKho == null)
//                return;
//
//            item.setTenDiemKho(diemKho.getTenDiemkho());
//            item.setMaDiemKho(diemKho.getMaDiemkho());
//        }
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhBienBanChuanBiKhoSearchReq countReq = new NhBienBanChuanBiKhoSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(nhBienBanChuanBiKhoRepository.count(countReq));
//        return count;
//    }
}
