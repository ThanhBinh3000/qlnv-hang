package com.tcdt.qlnvhang.service.nhaphang.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoSearchReq;
import com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCtRes;
import com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoRes;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.MoneyConvert;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class NhBienBanChuanBiKhoServiceImpl extends BaseServiceImpl implements NhBienBanChuanBiKhoService {
    private final NhBienBanChuanBiKhoRepository nhBienBanChuanBiKhoRepository;
    private final NhBienBanChuanBiKhoCtRepository nhBienBanChuanBiKhoCtRepository;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final HhHopDongRepository hhHopDongRepository;
    private final HttpServletRequest req;

    private static final String SHEET_BIEN_BAN_CHUAN_BI_KHO = "Biên bản chuẩn bị kho";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY = "Ngày";
    private static final String LOAI_HANG_HOA = "Loại Hàng Hóa";
    private static final String CHUNG_LOAI_HANG_HOA = "Chủng Loại Hàng Hóa";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBienBanChuanBiKhoRes create(NhBienBanChuanBiKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoBb(null, req);

        NhBienBanChuanBiKho item = new NhBienBanChuanBiKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBCBK", userInfo.getMaPbb()));
        nhBienBanChuanBiKhoRepository.save(item);

        List<NhBienBanChuanBiKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    private NhBienBanChuanBiKhoRes buildResponse(NhBienBanChuanBiKho item) throws Exception {
        NhBienBanChuanBiKhoRes res = new NhBienBanChuanBiKhoRes();
        List<NhBienBanChuanBiKhoCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhBienBanChuanBiKhoCt ct : item.getChiTiets()) {
            chiTiets.add(new NhBienBanChuanBiKhoCtRes(ct));
        }
        res.setChiTiets(chiTiets);
        String tongSo = Optional.ofNullable(item.getTongSo()).map(BigDecimal::toString).orElse(BigDecimal.ZERO.toString());
        res.setTongSoBangChu(MoneyConvert.doctienBangChu(tongSo, null));
        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(maVatTus)) {
            Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
            if (CollectionUtils.isEmpty(vatTus))
                throw new Exception("Không tìm thấy vật tư");
            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
                    .ifPresent(v -> res.setTenVatTu(v.getTen()));
            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
                    .ifPresent(v -> res.setTenVatTuCha(v.getTen()));
        }

        if (item.getQdgnvnxId() != null) {
            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
            if (!qdNhap.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
        }

        if (item.getHopDongId() != null) {
            Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(item.getHopDongId());
            if (!hopDong.isPresent()) {
                throw new Exception("Không tìm thấy hợp đồng");
            }
            res.setSoHopDong(hopDong.get().getSoHd());
        }

        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
        res.setMaDvi(donvi.getMaDvi());
        res.setTenDvi(donvi.getTenDvi());
        res.setMaQhns(donvi.getMaQhns());
        return res;
    }

    private List<NhBienBanChuanBiKhoCt> saveListChiTiet(Long parentId,
                                                     List<NhBienBanChuanBiKhoCtReq> chiTietReqs,
                                                     Map<Long, NhBienBanChuanBiKhoCt> mapChiTiet) throws Exception {
        List<NhBienBanChuanBiKhoCt> chiTiets = new ArrayList<>();
        for (NhBienBanChuanBiKhoCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBienBanChuanBiKhoCt chiTiet = new NhBienBanChuanBiKhoCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBbChuanBiKhoId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            nhBienBanChuanBiKhoCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBienBanChuanBiKhoRes update(NhBienBanChuanBiKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        this.validateSoBb(optional.get(), req);

        NhBienBanChuanBiKho item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        nhBienBanChuanBiKhoRepository.save(item);
        Map<Long, NhBienBanChuanBiKhoCt> mapChiTiet = nhBienBanChuanBiKhoCtRepository.findByBbChuanBiKhoIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhBienBanChuanBiKhoCt::getId, Function.identity()));

        List<NhBienBanChuanBiKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            nhBienBanChuanBiKhoCtRepository.deleteAll(mapChiTiet.values());
        return this.buildResponse(item);
    }

    @Override
    public NhBienBanChuanBiKhoRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        NhBienBanChuanBiKho item = optional.get();
        item.setChiTiets(nhBienBanChuanBiKhoCtRepository.findByBbChuanBiKhoIdIn(Collections.singleton(item.getId())));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        NhBienBanChuanBiKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }
        nhBienBanChuanBiKhoCtRepository.deleteByBbChuanBiKhoIdIn(Collections.singleton(item.getId()));
        nhBienBanChuanBiKhoRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        NhBienBanChuanBiKho phieu = optional.get();

        boolean success = this.updateStatus(phieu, stReq, userInfo);
        if (success)
            nhBienBanChuanBiKhoRepository.save(phieu);

        return success;
    }

    @Override
    public Page<NhBienBanChuanBiKhoRes> search(NhBienBanChuanBiKhoSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = nhBienBanChuanBiKhoRepository.search(req);
        List<NhBienBanChuanBiKhoRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhBienBanChuanBiKhoRes response = new NhBienBanChuanBiKhoRes();
            NhBienBanChuanBiKho item = (NhBienBanChuanBiKho) o[0];
            Long qdNhapId = (Long) o[1];
            String soQdNhap = (String) o[2];
            String tenVatTu = (String) o[3];
            String tenVatTuCha = (String) o[4];
            KtNganLo nganLo = (KtNganLo) o[5];
            Long hopDongId = o[6] != null ? (Long) o[6] : null;
            String soHopDong = o[7] != null ? (String) o[7] : null;
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setTenVatTu(tenVatTu);
            response.setTenVatTuCha(tenVatTuCha);
            response.setHopDongId(hopDongId);
            response.setSoHopDong(soHopDong);
            this.thongTinNganLo(response, nganLo);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, nhBienBanChuanBiKhoRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        nhBienBanChuanBiKhoCtRepository.deleteByBbChuanBiKhoIdIn(req.getIds());
        nhBienBanChuanBiKhoRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(NhBienBanChuanBiKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhBienBanChuanBiKhoRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY,
                LOAI_HANG_HOA, CHUNG_LOAI_HANG_HOA, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
        String filename = "Danh_sach_bien_ban_chuan_bi_kho.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                NhBienBanChuanBiKhoRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBienBan();
                objs[2] = item.getSoQuyetDinhNhap();
                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayTao());
                objs[4] = item.getTenVatTuCha();
                objs[5] = item.getTenVatTu();
                objs[6] = item.getTenDiemKho();
                objs[7] = item.getTenNhaKho();
                objs[8] = item.getTenNganKho();
                objs[9] = item.getTenNganLo();
                objs[10] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_CHUAN_BI_KHO, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = nhBienBanChuanBiKhoRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }

    private void validateSoBb(NhBienBanChuanBiKho update, NhBienBanChuanBiKhoReq req) throws Exception {
        String so = req.getSoBienBan();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<NhBienBanChuanBiKho> optional = nhBienBanChuanBiKhoRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(NhBienBanChuanBiKho::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số biên bản " + so + " đã tồn tại");
        }
    }

    private void thongTinNganLo(NhBienBanChuanBiKhoRes item, KtNganLo nganLo) {
        if (nganLo != null) {
            item.setTenNganLo(nganLo.getTenNganlo());
            KtNganKho nganKho = nganLo.getParent();
            if (nganKho == null)
                return;

            item.setTenNganKho(nganKho.getTenNgankho());
            item.setMaNganKho(nganKho.getMaNgankho());
            KtNhaKho nhaKho = nganKho.getParent();
            if (nhaKho == null)
                return;

            item.setTenNhaKho(nhaKho.getTenNhakho());
            item.setMaNhaKho(nhaKho.getMaNhakho());
            KtDiemKho diemKho = nhaKho.getParent();
            if (diemKho == null)
                return;

            item.setTenDiemKho(diemKho.getTenDiemkho());
            item.setMaDiemKho(diemKho.getMaDiemkho());
        }
    }
}
