package com.tcdt.qlnvhang.service.nhaphang.vattu.bienbaogiaonhan;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbangiaonhan.NhBbGiaoNhanVtCt;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVt;
import com.tcdt.qlnvhang.entities.nhaphang.vattu.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.enums.LoaiDaiDienEnum;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan.NhBbGiaoNhanVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan.NhBbGiaoNhanVtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanguihang.NhBienBanGuiHangRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan.NhBbGiaoNhanVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.bienbangiaonhan.NhBbGiaoNhanVtCtRes;
import com.tcdt.qlnvhang.response.vattu.bienbangiaonhan.NhBbGiaoNhanVtRes;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class NhBbGiaoNhanVtServiceImpl extends BaseServiceImpl implements NhBbGiaoNhanVtService {

    private static final String SHEET_BIEN_BAN_GIAO_NHAN_VAT_TU = "Biên bản giao nhận vật tư";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NAM_NHAP = "Năm Nhập";
    private static final String NGAY_BIEN_BAN = "Ngày Biên Bản";
    private static final String SO_HOP_DONG = "Số Hợp Đồng";
    private static final String BEN_GIAO = "Bên Giao";
    private static final String TRANG_THAI = "Trạng Thái";

    private final NhBbGiaoNhanVtRepository nhBbGiaoNhanVtRepository;
    private final NhBbGiaoNhanVtCtRepository nhBbGiaoNhanVtCtRepository;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final FileDinhKemService fileDinhKemService;
    private final HhHopDongRepository hhHopDongRepository;
    private final NhBienBanGuiHangRepository nhBienBanGuiHangRepository;
    private final NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
    private final NhBbKtNhapKhoVtRepository bbKtNhapKhoVtRepository;
    private final HttpServletRequest req;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBbGiaoNhanVtRes create(NhBbGiaoNhanVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoBb(null, req);

        NhBbGiaoNhanVt item = new NhBbGiaoNhanVt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBKTNK", userInfo.getMaPbb()));
        nhBbGiaoNhanVtRepository.save(item);

        List<NhBbGiaoNhanVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbGiaoNhanVt.TABLE_NAME));
        item.setCanCus(fileDinhKemService.saveListFileDinhKem(req.getCanCus(), item.getId(), NhBbGiaoNhanVt.CAN_CU));
        return this.buildResponse(item);
    }

    private NhBbGiaoNhanVtRes buildResponse(NhBbGiaoNhanVt item) throws Exception {
        NhBbGiaoNhanVtRes res = new NhBbGiaoNhanVtRes();
        List<NhBbGiaoNhanVtCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhBbGiaoNhanVtCt ct : item.getChiTiets()) {
            chiTiets.add(new NhBbGiaoNhanVtCtRes(ct));
        }

        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
        res.setMaDvi(donvi.getMaDvi());
        res.setTenDvi(donvi.getTenDvi());
        res.setMaQhns(donvi.getMaQhns());

        res.setChiTiets(chiTiets);
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

        if (item.getBbGuiHangId() != null) {
            Optional<NhBienBanGuiHang> bbGh = nhBienBanGuiHangRepository.findById(item.getBbGuiHangId());
            if (!bbGh.isPresent()) {
                throw new Exception("Không tìm thấy biên bản gửi hàng");
            }
            res.setSoBbGh(bbGh.get().getSoBienBan());
        }

        if (item.getHoSKyThuatId() != null) {
            Optional<NhHoSoKyThuat> hskt = nhHoSoKyThuatRepository.findById(item.getHoSKyThuatId());
            if (!hskt.isPresent()) {
                throw new Exception("Không tìm thấy hồ sơ kỹ thuật");
            }
            res.setSoBbGh(hskt.get().getSoBienBan());
        }

        if (item.getBbKtNhapKhoId() != null) {
            Optional<NhBbKtNhapKhoVt> bbKtNk = bbKtNhapKhoVtRepository.findById(item.getBbKtNhapKhoId());
            if (!bbKtNk.isPresent()) {
                throw new Exception("Không tìm thấy biên bản kết thúc nhập kho");
            }
            res.setSoBbKtNk(bbKtNk.get().getSoBienBan());
        }
        res.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME)));
        res.setCanCus(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.CAN_CU)));
        return res;
    }

    private List<NhBbGiaoNhanVtCt> saveListChiTiet(Long parentId,
                                                    List<NhBbGiaoNhanVtCtReq> chiTietReqs,
                                                    Map<Long, NhBbGiaoNhanVtCt> mapChiTiet) throws Exception {
        List<NhBbGiaoNhanVtCt> chiTiets = new ArrayList<>();
        for (NhBbGiaoNhanVtCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBbGiaoNhanVtCt chiTiet = new NhBbGiaoNhanVtCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBbGiaoNhanVtId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            nhBbGiaoNhanVtCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBbGiaoNhanVtRes update(NhBbGiaoNhanVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        this.validateSoBb(optional.get(), req);

        NhBbGiaoNhanVt item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        nhBbGiaoNhanVtRepository.save(item);
        Map<Long, NhBbGiaoNhanVtCt> mapChiTiet = nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhBbGiaoNhanVtCt::getId, Function.identity()));

        List<NhBbGiaoNhanVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            nhBbGiaoNhanVtCtRepository.deleteAll(mapChiTiet.values());
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbGiaoNhanVt.TABLE_NAME));
        item.setCanCus(fileDinhKemService.saveListFileDinhKem(req.getCanCus(), item.getId(), NhBbGiaoNhanVt.CAN_CU));
        return this.buildResponse(item);
    }

    @Override
    public NhBbGiaoNhanVtRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        NhBbGiaoNhanVt item = optional.get();
        item.setChiTiets(nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdIn(Collections.singleton(item.getId())));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME)));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản giao nhận không tồn tại.");

        NhBbGiaoNhanVt item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }
        nhBbGiaoNhanVtCtRepository.deleteByBbGiaoNhanVtIdIn(Collections.singleton(item.getId()));
        nhBbGiaoNhanVtRepository.delete(item);
        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME));
        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbGiaoNhanVt.CAN_CU));
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản giao nhận không tồn tại.");

        NhBbGiaoNhanVt phieu = optional.get();
        boolean success = this.updateStatus(phieu, stReq, userInfo);
        if (success)
            nhBbGiaoNhanVtRepository.save(phieu);

        return success;
    }

    @Override
    public Page<NhBbGiaoNhanVtRes> search(NhBbGiaoNhanVtSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        List<Object[]> data = nhBbGiaoNhanVtRepository.search(req);
        Set<Long> ids = data.stream()
                .map(o -> (NhBbGiaoNhanVt) o[0])
                .map(NhBbGiaoNhanVt::getId).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(new ArrayList<>(), pageable, nhBbGiaoNhanVtRepository.count(req));
        }
        Map<Long, NhBbGiaoNhanVtCt> mapCt = nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdInAndLoaiDaiDien(ids, LoaiDaiDienEnum.BEN_GIAO.getId())
                .stream().collect(Collectors.toMap(NhBbGiaoNhanVtCt::getBbGiaoNhanVtId, Function.identity(), (o1, o2) -> o2));

        List<NhBbGiaoNhanVtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhBbGiaoNhanVtRes response = new NhBbGiaoNhanVtRes();
            NhBbGiaoNhanVt item = (NhBbGiaoNhanVt) o[0];
            Long qdNhapId = (Long) o[1];
            String soQdNhap = (String) o[2];
            Long hopDongId = (Long) o[3];
            String soHd = (String) o[4];
            NhBbGiaoNhanVtCt daiDienBenGiao = mapCt.get(item.getId());
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setHopDongId(hopDongId);
            response.setSoHopDong(soHd);
            if (daiDienBenGiao != null) {
                response.setBenGiao(daiDienBenGiao.getDaiDien());
            }
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, nhBbGiaoNhanVtRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        nhBbGiaoNhanVtCtRepository.deleteByBbGiaoNhanVtIdIn(req.getIds());
        nhBbGiaoNhanVtRepository.deleteByIdIn(req.getIds());
        fileDinhKemService.deleteMultiple(req.getIds(), Arrays.asList(NhBbGiaoNhanVt.TABLE_NAME, NhBbGiaoNhanVt.CAN_CU));
        return true;
    }

    @Override
    public boolean exportToExcel(NhBbGiaoNhanVtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhBbGiaoNhanVtRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NAM_NHAP,
                NGAY_BIEN_BAN, SO_HOP_DONG, BEN_GIAO, TRANG_THAI};
        String filename = "Danh_sach_bien_giao_nhan.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                NhBbGiaoNhanVtRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBienBan();
                objs[2] = item.getSoQuyetDinhNhap();
                objs[3] = item.getNam();
                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayKy());
                objs[5] = item.getSoHopDong();
                objs[6] = item.getBenGiao();
                objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_GIAO_NHAN_VAT_TU, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = nhBbGiaoNhanVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }

    private void validateSoBb(NhBbGiaoNhanVt update, NhBbGiaoNhanVtReq req) throws Exception {
        String so = req.getSoBienBan();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(NhBbGiaoNhanVt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số biên bản " + so + " đã tồn tại");
        }
    }
}
