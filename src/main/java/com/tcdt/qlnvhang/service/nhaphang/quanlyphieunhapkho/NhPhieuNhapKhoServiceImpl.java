package com.tcdt.qlnvhang.service.nhaphang.quanlyphieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieukiemtrachatluonghangluongthuc.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.NhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt1Repository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class NhPhieuNhapKhoServiceImpl extends BaseServiceImpl implements NhPhieuNhapKhoService {

    private static final String SHEET_PHIEU_NHAP_KHO = "Phiếu nhập kho";
    private static final String STT = "STT";
    private static final String SO_PHIEU = "Số Phiếu";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_NHAP_KHO = "Ngày Nhập Kho";
    private static final String DIEM_KHO = "Điểm KHo";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private NhPhieuKtChatLuongRepository phieuKtraRepo;


    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private NhPhieuKtChatLuongRepository phieuKtChatLuongRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;

    @Autowired
    private NhPhieuNhapKhoCt1Repository nhPhieuNhapKhoCt1Repository;

    @Override
    public Page<NhPhieuNhapKho> searchPage(NhPhieuNhapKhoReq req) {
        return null;
    }

    @Override
    public List<NhPhieuNhapKho> searchAll(NhPhieuNhapKhoReq req) {
        return null;
    }

    @Override
    @Transactional()
    public NhPhieuNhapKho create(NhPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }
        NhPhieuNhapKho phieu = new NhPhieuNhapKho();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgayTao(LocalDate.now());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        phieu.setMaDvi(userInfo.getDvql());
        phieu.setNam(LocalDate.now().getYear());
        phieu.setId(Long.valueOf(phieu.getSoPhieuNhapKho().split("/")[0]));
        nhPhieuNhapKhoRepository.save(phieu);
        this.saveCtiet(phieu.getId(),req);
        return phieu;
    }

    @Override
    @Transactional()
    public NhPhieuNhapKho update(NhPhieuNhapKhoReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhPhieuNhapKho> optionalQd = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Phiếu nhập kho không tồn tại.");

        NhPhieuNhapKho phieu = optionalQd.get();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgaySua(LocalDate.now());
        phieu.setNguoiSuaId(userInfo.getId());
        nhPhieuNhapKhoRepository.save(phieu);
        this.saveCtiet(phieu.getId(),req);
        return phieu;
    }

    @Transactional()
    void saveCtiet(Long idHdr , NhPhieuNhapKhoReq req){
        nhPhieuNhapKhoCtRepository.deleteAllByIdPhieuNkHdr(idHdr);
        if(!ObjectUtils.isEmpty(req.getHangHoaList())){
            for(NhPhieuNhapKhoCtReq obj : req.getHangHoaList()){
                NhPhieuNhapKhoCt data = new NhPhieuNhapKhoCt();
                BeanUtils.copyProperties(obj,data,"id");
                data.setIdPhieuNkHdr(idHdr);
                nhPhieuNhapKhoCtRepository.save(data);
            }
        }
    }

    @Override
    public NhPhieuNhapKho detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Quyết định không tồn tại.");
        }

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

        NhPhieuNhapKho data = optional.get();
//        data.setKetQuaKiemTra(qlpktclhKetQuaKiemTraRepo.findAllByPhieuKtChatLuongId(data.getId()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(listDanhMucHangHoa.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(listDanhMucHangHoa.get(data.getCloaiVthh()));
        data.setTenDvi(listDanhMucDvi.get(data.getMaDvi()));
        data.setTenDiemKho(listDanhMucDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(listDanhMucDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(listDanhMucDvi.get(data.getMaNganKho()));
        data.setTenLoKho(listDanhMucDvi.get(data.getMaLoKho()));
        data.setTenNguoiTao(ObjectUtils.isEmpty(data.getNguoiTaoId()) ? null : userInfoRepository.findById(data.getNguoiTaoId()).get().getFullName());
        data.setTenNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
        data.setHangHoaList(nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(data.getId()));
        return data;
    }

    @Override
    public NhPhieuNhapKho approve(NhPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhPhieuNhapKho phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                phieu.setNguoiGuiDuyetId(userInfo.getId());
                phieu.setNgayGuiDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(LocalDate.now());
                phieu.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        nhPhieuNhapKhoRepository.save(phieu);
        return phieu;
    }

    @Override
    @Transactional()
    public void delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Quyết định không tồn tại.");
        }

        NhPhieuNhapKho phieu = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<NhPhieuNhapKhoCt> hangHoaList = nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(phieu.getId());
        if (!CollectionUtils.isEmpty(hangHoaList)) {
            nhPhieuNhapKhoCtRepository.deleteAll(hangHoaList);
        }

        nhPhieuNhapKhoRepository.delete(phieu);
        fileDinhKemService.delete(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(NhPhieuNhapKhoReq req) throws Exception {
        return false;
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhPhieuNhapKhoRes create(NhPhieuNhapKhoReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        this.validateSoPhieu(null, req);
//
//        NhPhieuNhapKho phieu = new NhPhieuNhapKho();
//        BeanUtils.copyProperties(req, phieu, "id");
//        phieu.setNgayTao(LocalDate.now());
//        phieu.setNguoiTaoId(userInfo.getId());
//        phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        phieu.setMaDvi(userInfo.getDvql());
//        phieu.setCapDvi(userInfo.getCapDvi());
//        phieu.setSo(getSo());
//        phieu.setNam(LocalDate.now().getYear());
//        phieu.setSoPhieu(String.format("%s/%s/%s-%s", phieu.getSo(), phieu.getNam(), "PN", userInfo.getMaPbb()));
//        nhPhieuNhapKhoRepository.save(phieu);
//
//        List<NhPhieuNhapKhoCtReq> hangHoaReqs = req.getHangHoaList();
//        List<NhPhieuNhapKhoCt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, new HashMap<>());
//        phieu.setHangHoaList(hangHoaList);
//
//        List<NhPhieuNhapKhoCt1> ct1List = this.saveCt1(phieu.getId(), req.getPhieuKtClIds());
//        phieu.setChiTiet1s(ct1List);
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME);
//        phieu.setChungTus(fileDinhKems);
//
//        return this.buildResponse(phieu);
//    }
//
//    private List<NhPhieuNhapKhoCt1> saveCt1(Long phieuNhapKhoId, List<Long> phieuKtClIds) {
//        nhPhieuNhapKhoCt1Repository.deleteByPhieuNkIdIn(Collections.singleton(phieuNhapKhoId));
//
//        List<NhPhieuNhapKhoCt1> ct1List = new ArrayList<>();
//        for (Long phieuKtClId : phieuKtClIds) {
//            NhPhieuNhapKhoCt1 ct1 = new NhPhieuNhapKhoCt1();
//            ct1.setPhieuNkId(phieuNhapKhoId);
//            ct1.setPhieuKtclId(phieuKtClId);
//            ct1List.add(ct1);
//        }
//
//        if (!CollectionUtils.isEmpty(ct1List))
//            nhPhieuNhapKhoCt1Repository.saveAll(ct1List);
//
//        return ct1List;
//    }
//
//    private List<NhPhieuNhapKhoCt> saveListHangHoa(Long phieuNhapKhoId, List<NhPhieuNhapKhoCtReq> hangHoaReqs, Map<Long, NhPhieuNhapKhoCt> mapHangHoa) throws Exception {
//        List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();
//        Set<String> maVatTus = hangHoaReqs.stream().map(NhPhieuNhapKhoCtReq::getMaVatTu).collect(Collectors.toSet());
//        Set<QlnvDmVattu> qlnvDmVattus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));
//        for (NhPhieuNhapKhoCtReq req : hangHoaReqs) {
//            Long id = req.getId();
//            NhPhieuNhapKhoCt hangHoa = new NhPhieuNhapKhoCt();
//            QlnvDmVattu vatTu = qlnvDmVattus.stream().filter(v -> v.getMa().equals(req.getMaVatTu())).findFirst().orElse(null);
//            if (vatTu == null)
//                throw new Exception("Hàng Hóa không tồn tại.");
//
//            if (id != null && id > 0) {
//                hangHoa = mapHangHoa.get(id);
//                if (hangHoa == null)
//                    throw new Exception("Chi tiết hàng Hóa không tồn tại.");
//                mapHangHoa.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, hangHoa, "id");
//            hangHoa.setPhieuNkId(phieuNhapKhoId);
//            hangHoa.setMaVatTu(req.getMaVatTu());
//            hangHoaList.add(hangHoa);
//        }
//
//        if (!CollectionUtils.isEmpty(hangHoaList))
//            nhPhieuNhapKhoCtRepository.saveAll(hangHoaList);
//
//        return hangHoaList;
//    }
//
//    private NhPhieuNhapKhoRes buildResponse(NhPhieuNhapKho phieu) throws Exception {
//        Set<String> maVatTus = phieu.getHangHoaList().stream().map(NhPhieuNhapKhoCt::getMaVatTu).collect(Collectors.toSet());
//        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();
//
//        NhPhieuNhapKhoRes response = new NhPhieuNhapKhoRes();
//        BeanUtils.copyProperties(phieu, response);
//        response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(phieu.getTrangThai()));
//        response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(phieu.getTrangThai()));
//        List<NhPhieuNhapKhoCt> hangHoaList = phieu.getHangHoaList();
//
//        BigDecimal tongSoLuong = BigDecimal.ZERO;
//        BigDecimal tongSoTien = BigDecimal.ZERO;
//
//        for (NhPhieuNhapKhoCt hangHoa : hangHoaList) {
//            NhPhieuNhapKhoCtRes hangHoaRes = new NhPhieuNhapKhoCtRes();
//            BeanUtils.copyProperties(hangHoa, hangHoaRes);
//
//            qlnvDmVattus.stream().filter(v -> v.getMa().equals(hangHoa.getMaVatTu())).findFirst().ifPresent(t -> {
//                hangHoaRes.setTenVatTu(t.getTen());
//            });
//            response.getHangHoaRes().add(hangHoaRes);
//
//            tongSoLuong = tongSoLuong.add(Optional.ofNullable(hangHoa.getSoLuongThucNhap()).orElse(BigDecimal.ZERO));
//            tongSoTien = tongSoTien.add(Optional.ofNullable(hangHoa.getThanhTien()).orElse(BigDecimal.ZERO));
//        }
//
//        Map<String, QlnvDmDonvi> mapDmucDvi = getMapDvi();
//        QlnvDmDonvi qlnvDmDonvi = mapDmucDvi.get(phieu.getMaDvi());
//        if (qlnvDmDonvi == null)
//            throw new Exception("Bad request.");
//
//        response.setMaDvi(qlnvDmDonvi.getMaDvi());
//        response.setTenDvi(qlnvDmDonvi.getTenDvi());
//        response.setMaQhns(qlnvDmDonvi.getMaQhns());
//        response.setTongSoLuong(tongSoLuong);
//        response.setTongSoTien(tongSoTien);
//        response.setTongSoLuongBangChu(MoneyConvert.docSoLuong(tongSoLuong.toString(), null));
//        response.setTongSoTienBangChu(MoneyConvert.doctienBangChu(tongSoTien.toString(), null));
//
//        if (!CollectionUtils.isEmpty(phieu.getChiTiet1s())) {
//            List<Long> phieuCtClIds = phieu.getChiTiet1s().stream()
//                    .map(NhPhieuNhapKhoCt1::getPhieuKtclId)
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            if (!CollectionUtils.isEmpty(phieuCtClIds)) {
//                List<QlpktclhPhieuKtChatLuong> phieuKtChatLuongs = phieuKtChatLuongRepository.findByIdIn(phieuCtClIds);
//                for (NhPhieuNhapKhoCt1 ct1 : phieu.getChiTiet1s()) {
//                    phieuKtChatLuongs.stream().filter(p -> p.getId().equals(ct1.getPhieuKtclId()))
//                            .findFirst().ifPresent(p -> ct1.setSoPhieuKtCl(p.getSoPhieu()));
//                }
//            }
//            response.setChiTiet1s(phieu.getChiTiet1s());
//            response.setPhieuKtClIds(response.getChiTiet1s().stream().map(NhPhieuNhapKhoCt1::getPhieuKtclId).collect(Collectors.toList()));
//        }
//
//        if (phieu.getHoSoKyThuatId() != null) {
//            NhHoSoKyThuat hskt = nhHoSoKyThuatRepository.findById(phieu.getHoSoKyThuatId())
//                    .orElseThrow(() -> new Exception("Không tìm thấy biên bản hồ sơ kỹ thuật"));
//
//            response.setSoBbHoSoKyThuat(hskt.getSoBienBan());
//        }
//
//        if (phieu.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(phieu.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        KtNganLo nganLo = null;
//        if (StringUtils.hasText(phieu.getMaNganLo())) {
//            nganLo = ktNganLoRepository.findFirstByMaNganlo(phieu.getMaNganLo());
//        }
//
//        this.thongTinNganLo(response, nganLo);
//        response.setChungTus(fileDinhKemService.search(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME)));
//        return response;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhPhieuNhapKhoRes update(NhPhieuNhapKhoReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<NhPhieuNhapKho> optionalQd = nhPhieuNhapKhoRepository.findById(req.getId());
//        if (!optionalQd.isPresent())
//            throw new Exception("Phiếu nhập kho không tồn tại.");
//
//        this.validateSoPhieu(optionalQd.get(), req);
//
//        NhPhieuNhapKho phieu = optionalQd.get();
//        BeanUtils.copyProperties(req, phieu, "id");
//        phieu.setNgaySua(LocalDate.now());
//        phieu.setNguoiSuaId(userInfo.getId());
//        nhPhieuNhapKhoRepository.save(phieu);
//
//        Map<Long, NhPhieuNhapKhoCt> mapHangHoa = nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId())
//                .stream().collect(Collectors.toMap(NhPhieuNhapKhoCt::getId, Function.identity()));
//
//        List<NhPhieuNhapKhoCtReq> hangHoaReqs = req.getHangHoaList();
//        List<NhPhieuNhapKhoCt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, mapHangHoa);
//        phieu.setHangHoaList(hangHoaList);
//
//        if (!CollectionUtils.isEmpty(mapHangHoa.values()))
//            nhPhieuNhapKhoCtRepository.deleteAll(mapHangHoa.values());
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME);
//        phieu.setChungTus(fileDinhKems);
//
//        return this.buildResponse(phieu);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định không tồn tại.");
//
//        NhPhieuNhapKho phieu = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(phieu.getTrangThai())) {
//            throw new Exception("Không thể xóa quyết định đã duyệt");
//        }
//
//        List<NhPhieuNhapKhoCt> hangHoaList = nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId());
//        if (!CollectionUtils.isEmpty(hangHoaList)) {
//            nhPhieuNhapKhoCtRepository.deleteAll(hangHoaList);
//        }
//
//        nhPhieuNhapKhoRepository.delete(phieu);
//        fileDinhKemService.delete(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
//        return true;
//    }
//
//    @Override
//    public NhPhieuNhapKhoRes detail(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định không tồn tại.");
//
//        NhPhieuNhapKho phieu = optional.get();
//        phieu.setHangHoaList(nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId()));
//        phieu.setChiTiet1s(nhPhieuNhapKhoCt1Repository.findByPhieuNkIdIn(Collections.singleton(id)));
//        return this.buildResponse(phieu);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định không tồn tại.");
//
//        NhPhieuNhapKho phieu = optional.get();
//
//        String trangThai = phieu.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            phieu.setNguoiGuiDuyetId(userInfo.getId());
//            phieu.setNgayGuiDuyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            phieu.setNguoiPduyetId(userInfo.getId());
//            phieu.setNgayPduyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            phieu.setNguoiPduyetId(userInfo.getId());
//            phieu.setNgayPduyet(LocalDate.now());
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        return nhPhieuNhapKhoRepository.search(req);
//    }
//
//    private void thongTinNganLo(NhPhieuNhapKhoRes phieu, KtNganLo nganLo) {
//        if (nganLo != null) {
//            phieu.setTenNganLo(nganLo.getTenNganlo());
//            KtNganKho nganKho = nganLo.getParent();
//            if (nganKho == null)
//                return;
//
//            phieu.setTenNganKho(nganKho.getTenNgankho());
//            phieu.setMaNganKho(nganKho.getMaNgankho());
//            KtNhaKho nhaKho = nganKho.getParent();
//            if (nhaKho == null)
//                return;
//
//            phieu.setTenNhaKho(nhaKho.getTenNhakho());
//            phieu.setMaNhaKho(nhaKho.getMaNhakho());
//            KtDiemKho diemKho = nhaKho.getParent();
//            if (diemKho == null)
//                return;
//
//            phieu.setTenDiemKho(diemKho.getTenDiemkho());
//            phieu.setMaDiemKho(diemKho.getMaDiemkho());
//        }
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhPhieuNhapKhoSearchReq countReq = new NhPhieuNhapKhoSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//        count.setThoc(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//        count.setGao(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//        count.setMuoi(nhPhieuNhapKhoRepository.count(countReq));
//        countReq.setLoaiVthh(Contains.LOAI_VTHH_VATTU);
//        count.setVatTu(nhPhieuNhapKhoRepository.count(countReq));
//        return count;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        if (CollectionUtils.isEmpty(req.getIds()))
//            return false;
//
//
//        nhPhieuNhapKhoCtRepository.deleteByPhieuNkIdIn(req.getIds());
//        nhPhieuNhapKhoRepository.deleteByIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhPhieuNhapKhoSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhPhieuNhapKhoRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_PHIEU, SO_QUYET_DINH_NHAP, NGAY_NHAP_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_phieu_nhap_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhPhieuNhapKhoRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoPhieu();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayNhapKho());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_PHIEU_NHAP_KHO, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//        return true;
//    }
//
//    private void validateSoPhieu(NhPhieuNhapKho update, NhPhieuNhapKhoReq req) throws Exception {
//        String so = req.getSoPhieu();
//        if (!StringUtils.hasText(so))
//            return;
//
//        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
//            Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findFirstBySoPhieu(so);
//            Long updateId = Optional.ofNullable(update).map(NhPhieuNhapKho::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số phiếu " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhPhieuNhapKhoRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
}
