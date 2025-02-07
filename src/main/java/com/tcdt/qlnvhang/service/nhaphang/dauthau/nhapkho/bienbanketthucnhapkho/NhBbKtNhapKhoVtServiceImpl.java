package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanketthucnhapkho.NhBbKtNhapKhoVt;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtRepository;
import com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class NhBbKtNhapKhoVtServiceImpl extends BaseServiceImpl implements NhBbKtNhapKhoVtService {
    private final NhBbKtNhapKhoVtRepository nhBbKtNhapKhoVtRepository;
    private final NhBbKtNhapKhoVtCtRepository nhBbKtNhapKhoVtCtRepository;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final FileDinhKemService fileDinhKemService;
    private final NhBienBanChuanBiKhoRepository nhBienBanChuanBiKhoRepository;
    private final HttpServletRequest req;

    private static final String SHEET_BIEN_BAN_KET_THUC_NHAP_KHO_VAT_TU = "Biên bản kết thúc nhập kho vật tư";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_NHAP_DAY_KHO = "Ngày Nhập Đầy Kho";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TEN_NGUOI_GIAO = "Tên Người Giao";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    public Page<NhBbKtNhapKhoVt> searchPage(NhBbKtNhapKhoVtReq req) {
        return null;
    }

    @Override
    public NhBbKtNhapKhoVt create(NhBbKtNhapKhoVtReq req) throws Exception {
        return null;
    }

    @Override
    public NhBbKtNhapKhoVt update(NhBbKtNhapKhoVtReq req) throws Exception {
        return null;
    }

    @Override
    public NhBbKtNhapKhoVt detail(Long id) throws Exception {
        return null;
    }

    @Override
    public NhBbKtNhapKhoVt approve(NhBbKtNhapKhoVtReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhBbKtNhapKhoVtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBbKtNhapKhoVtRes create(NhBbKtNhapKhoVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoBb(null, req);
//
//        NhBbKtNhapKhoVt item = new NhBbKtNhapKhoVt();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBKTNK", userInfo.getMaPbb()));
//        nhBbKtNhapKhoVtRepository.save(item);
//
//        List<NhBbKtNhapKhoVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbKtNhapKhoVt.TABLE_NAME));
//        return this.buildResponse(item);
//    }
//
//    private NhBbKtNhapKhoVtRes buildResponse(NhBbKtNhapKhoVt item) throws Exception {
//        NhBbKtNhapKhoVtRes res = new NhBbKtNhapKhoVtRes();
//        List<NhBbKtNhapKhoVtCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhBbKtNhapKhoVtCt ct : item.getChiTiets()) {
//            chiTiets.add(new NhBbKtNhapKhoVtCtRes(ct));
//        }
//
//        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
//        res.setMaDvi(donvi.getMaDvi());
//        res.setTenDvi(donvi.getTenDvi());
//        res.setMaQhns(donvi.getMaQhns());
//
//        res.setChiTiets(chiTiets);
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
//        if (item.getBbChuanBiKhoId() != null) {
//            Optional<NhBienBanChuanBiKho> bienBanChuanBiKho = nhBienBanChuanBiKhoRepository.findById(item.getBbChuanBiKhoId());
//            if (!bienBanChuanBiKho.isPresent()) {
//                throw new Exception("Không tìm thấy biên bản chuẩn bị kho");
//            }
//            res.setSoBbChuanBiKho(bienBanChuanBiKho.get().getSoBienBan());
//        }
//        return res;
//    }
//
//    private List<NhBbKtNhapKhoVtCt> saveListChiTiet(Long parentId,
//                                                        List<NhBbKtNhapKhoVtCtReq> chiTietReqs,
//                                                        Map<Long, NhBbKtNhapKhoVtCt> mapChiTiet) throws Exception {
//        List<NhBbKtNhapKhoVtCt> chiTiets = new ArrayList<>();
//        for (NhBbKtNhapKhoVtCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhBbKtNhapKhoVtCt chiTiet = new NhBbKtNhapKhoVtCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setBbKtNhapKhoId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            nhBbKtNhapKhoVtCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBbKtNhapKhoVtRes update(NhBbKtNhapKhoVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//
//        NhBbKtNhapKhoVt item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        nhBbKtNhapKhoVtRepository.save(item);
//        Map<Long, NhBbKtNhapKhoVtCt> mapChiTiet = nhBbKtNhapKhoVtCtRepository.findByBbKtNhapKhoIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhBbKtNhapKhoVtCt::getId, Function.identity()));
//
//        List<NhBbKtNhapKhoVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            nhBbKtNhapKhoVtCtRepository.deleteAll(mapChiTiet.values());
//        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbKtNhapKhoVt.TABLE_NAME));
//
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhBbKtNhapKhoVtRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        NhBbKtNhapKhoVt item = optional.get();
//        item.setChiTiets(nhBbKtNhapKhoVtCtRepository.findByBbKtNhapKhoIdIn(Collections.singleton(item.getId())));
//        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbKtNhapKhoVt.TABLE_NAME)));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản kết thúc nhập kho không tồn tại.");
//
//        NhBbKtNhapKhoVt item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa biên bản đã đã duyệt");
//        }
//        nhBbKtNhapKhoVtCtRepository.deleteByBbKtNhapKhoIdIn(Collections.singleton(item.getId()));
//        nhBbKtNhapKhoVtRepository.delete(item);
//        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbKtNhapKhoVt.TABLE_NAME));
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản kết thúc nhập kho không tồn tại.");
//
//        NhBbKtNhapKhoVt bb = optional.get();
//
//        String trangThai = bb.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
//            bb.setNguoiGuiDuyetId(userInfo.getId());
//            bb.setNgayGuiDuyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId());
//            bb.setNguoiGuiDuyetId(userInfo.getId());
//            bb.setNgayGuiDuyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
//            bb.setNguoiPduyetId(userInfo.getId());
//            bb.setNgayPduyet(new Date());
//            bb.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//
//            bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId());
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
//        nhBbKtNhapKhoVtRepository.save(bb);
//        return true;
//    }
//
//    @Override
//    public Page<NhBbKtNhapKhoVtRes> search(NhBbKtNhapKhoVtSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = nhBbKtNhapKhoVtRepository.search(req);
//        List<NhBbKtNhapKhoVtRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhBbKtNhapKhoVtRes response = new NhBbKtNhapKhoVtRes();
//            NhBbKtNhapKhoVt item = (NhBbKtNhapKhoVt) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            this.thongTinNganLo(response, nganLo);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, nhBbKtNhapKhoVtRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        nhBbKtNhapKhoVtCtRepository.deleteByBbKtNhapKhoIdIn(req.getIds());
//        nhBbKtNhapKhoVtRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhBbKtNhapKhoVtSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhBbKtNhapKhoVtRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_NHAP_DAY_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_bien_ban_ket-thuc_nhap_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhBbKtNhapKhoVtRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayKetThucKho());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_KET_THUC_NHAP_KHO_VAT_TU, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//        return true;
//    }
//
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhBbKtNhapKhoVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoBb(NhBbKtNhapKhoVt update, NhBbKtNhapKhoVtReq req) throws Exception {
//        String so = req.getSoBienBan();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
//            Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findFirstBySoBienBan(so);
//            Long updateId = Optional.ofNullable(update).map(NhBbKtNhapKhoVt::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(NhBbKtNhapKhoVtRes item, KtNganLo nganLo) {
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
//        NhBbKtNhapKhoVtSearchReq countReq = new NhBbKtNhapKhoVtSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(nhBbKtNhapKhoVtRepository.count(countReq));
//        return count;
//    }
}
