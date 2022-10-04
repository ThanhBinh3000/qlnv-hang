package com.tcdt.qlnvhang.service.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgCtReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReq;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReqExt;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgCtRes;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgRes;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BhQdPheDuyetKqbdgServiceImpl extends BaseServiceImpl implements BhQdPheDuyetKqbdgService {
    private final BhQdPheDuyetKqbdgRepository bhQdPheDuyetKqbdgRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final BhQdPheDuyetKhBdgThongTinTaiSanRepository bhQdPheDuyetKhBdgThongTinTaiSanRepository;
    private final FileDinhKemService fileDinhKemService;

    private final KtNganLoRepository ktNganLoRepository;
    private final KtDiemKhoRepository ktDiemKhoRepository;
    private final KtNhaKhoRepository ktNhaKhoRepository;
    private final QlnvDmDonviRepository dmDonviRepository;

    private final KtNganKhoRepository ktNganKhoRepository;

    private static final String SHEET_QUYET_DINH_PHE_DUYET_KET_QUA_BAN_DAU_GIA = "Quyết định phê duyệt kết quả bán đấu giá";
    private static final String STT = "STT";
    private static final String SO_QD_PHE_DUYET_KQ_BDG = "Số QĐ Phê Duyệt KQ BĐG";
    private static final String NGAY_KY = "Ngày Ký";
    private static final String TRICH_YEU = "Trích Yếu";
    private static final String NGAY_TO_CHUC_BDG = "Ngày Tổ Chức BĐG";
    private static final String SO_QD_PHE_DUYET_KH_BDG = "Số QĐ Phê Duyệt KH BĐG";
    private static final String MA_THONG_BAO_BDG = "Mã Thông Báo BĐG";
    private static final String HINH_THUC_DAU_GIA = "Hình Thức Đấu Giá";
    private static final String PHUONG_THUC_DAU_GIA = "Phương Thức Đấu Giá";
    private static final String LOAI_HANG_HOA = "Loại Hàng Hóa";
    private static final String NAM_KE_HOACH = "Năm Kế Hoạch";
    private static final String SO_TB_DAU_GIA_KHONG_THANH = "Số TB Đấu Giá Không Thành";
    private static final String SO_BIEN_BAN_DAU_GIA = "Số Biên Bản Đấu Giá";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BhQdPheDuyetKqbdgRes create(BhQdPheDuyetKqbdgReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoQd(null, req);

        BhQdPheDuyetKqbdg item = new BhQdPheDuyetKqbdg();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setNam(LocalDate.now().getYear());
        bhQdPheDuyetKqbdgRepository.save(item);

        Map<Long, BhQdPheDuyetKhBdgThongTinTaiSan> mapChiTiet1 = bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgIdIn(Collections.singleton(item.getThongBaoBdgId()))
                .stream().collect(Collectors.toMap(BhQdPheDuyetKhBdgThongTinTaiSan::getId, Function.identity()));

        List<BhQdPheDuyetKhBdgThongTinTaiSan> cts = this.saveListChiTiet1(item.getId(), req.getCts(), mapChiTiet1);
        item.setCts(cts);
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), BhQdPheDuyetKqbdg.TABLE_NAME));
        return this.buildResponse(item);
    }

    private List<BhQdPheDuyetKhBdgThongTinTaiSan> saveListChiTiet1(Long parentId,
                                                  List<BhQdPheDuyetKqbdgCtReq> chiTietReqs,
                                                  Map<Long, BhQdPheDuyetKhBdgThongTinTaiSan> mapChiTiet) throws Exception {
        List<BhQdPheDuyetKhBdgThongTinTaiSan> chiTiets = new ArrayList<>();
        for (BhQdPheDuyetKqbdgCtReq req : chiTietReqs) {
            Long id = req.getId();
            if (id != null && id > 0) {
                BhQdPheDuyetKhBdgThongTinTaiSan chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Phân lô tài sản không tồn tại.");
                mapChiTiet.remove(id);
                chiTiet.setQdPheDuyetKqbdgId(parentId);
                chiTiet.setDonGiaTrungDauGia(req.getDonGiaTrungDauGia());
                chiTiet.setTrungDauGia(req.getTrungDauGia());
                chiTiets.add(chiTiet);
            }
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.saveAll(chiTiets);

        return chiTiets;
    }


    private BhQdPheDuyetKqbdgRes buildResponse(BhQdPheDuyetKqbdg item) throws Exception {
        BhQdPheDuyetKqbdgRes res = new BhQdPheDuyetKqbdgRes();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        List<BhQdPheDuyetKqbdgCtRes> cts = item.getCts().stream().map(BhQdPheDuyetKqbdgCtRes::new).collect(Collectors.toList());
        this.buildThongTinKho(cts);
        res.setCts(cts);
        Set<String> maVatTus = Collections.singleton(item.getLoaiVthh());
        Set<QlnvDmVattu> vatTus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));

        if (CollectionUtils.isEmpty(vatTus))
            throw new Exception("Không tìm thấy vật tư");

        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getLoaiVthh())).findFirst()
                .ifPresent(v -> res.setTenVatTuCha(v.getTen()));

        this.setThongTinDonVi(res, item.getMaDvi());
        return res;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BhQdPheDuyetKqbdgRes update(BhQdPheDuyetKqbdgReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<BhQdPheDuyetKqbdg> optional = bhQdPheDuyetKqbdgRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        this.validateSoQd(optional.get(), req);

        BhQdPheDuyetKqbdg item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        bhQdPheDuyetKqbdgRepository.save(item);

        Map<Long, BhQdPheDuyetKhBdgThongTinTaiSan> mapChiTiet1 = bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgIdIn(Collections.singleton(item.getThongBaoBdgId()))
                .stream().collect(Collectors.toMap(BhQdPheDuyetKhBdgThongTinTaiSan::getId, Function.identity()));

        // Bien ban phan lo
        List<BhQdPheDuyetKhBdgThongTinTaiSan> chiTiets = this.saveListChiTiet1(item.getId(), req.getCts(), mapChiTiet1);
        item.setCts(chiTiets);
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), BhQdPheDuyetKqbdg.TABLE_NAME));
        return this.buildResponse(item);
    }

    @Override
    public BhQdPheDuyetKqbdgRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhQdPheDuyetKqbdg> optional = bhQdPheDuyetKqbdgRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhQdPheDuyetKqbdg item = optional.get();
        item.setCts(bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByQdPheDuyetKqbdgIdIn(Collections.singleton(item.getId())));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(BhQdPheDuyetKqbdg.TABLE_NAME)));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhQdPheDuyetKqbdg> optional = bhQdPheDuyetKqbdgRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhQdPheDuyetKqbdg item = optional.get();
        if (NhapXuatHangTrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Biên bản bán đấu giá đã ban hành");
        }
        bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByQdPheDuyetKqbdgIdIn(Collections.singleton(item.getId())).forEach(p -> {
            p.setQdPheDuyetKqbdgId(null);
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.save(p);
        });
        fileDinhKemService.delete(item.getId(), Collections.singleton(BhQdPheDuyetKqbdg.TABLE_NAME));
        bhQdPheDuyetKqbdgRepository.delete(item);

        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhQdPheDuyetKqbdg> optional = bhQdPheDuyetKqbdgRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhQdPheDuyetKqbdg phieu = optional.get();
        String trangThai = phieu.getTrangThai();

        if (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId());
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(LocalDate.now());
        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
                return false;
            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
                return false;
            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
            phieu.setLyDoTuChoi(stReq.getLyDo());
        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
            phieu.setLyDoTuChoi(stReq.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }
        return false;
    }

    @Override
    public Page<BhQdPheDuyetKqbdgRes> search(BhQdPheDuyetKqbdgSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = bhQdPheDuyetKqbdgRepository.search(req, pageable);
        List<BhQdPheDuyetKqbdgRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            BhQdPheDuyetKqbdgRes response = new BhQdPheDuyetKqbdgRes();
            BhQdPheDuyetKqbdg item = (BhQdPheDuyetKqbdg) o[0];
            Long thongBaoBdgId = o[1] != null ? (Long) o[1] : null;
            String maThongBaoBdg = o[2] != null ? (String) o[2] : null;
            String hinhThucDauGia = o[3] != null ? (String) o[3] : null;
            String phuongThucDauGia = o[4] != null ? (String) o[4] : null;
            Long bbBanDauGiaId = o[5] != null ? (Long) o[5] : null;
            String soBienBanDauGia = o[6] != null ? (String) o[6] : null;
            LocalDate ngayToChuc = o[7] != null ? (LocalDate) o[7] : null;
            String maVatTuCha = o[8] != null ? (String) o[8] : null;
            String tenVatTuCha = o[9] != null ? (String) o[9] : null;
            Long qdPdKhBdgId = o[10] != null ? (Long) o[10] : null;
            String soQdPdKhBdg = o[11] != null ? (String) o[11] : null;

            BeanUtils.copyProperties(item, response);
            response.setThongBaoBdgId(thongBaoBdgId);
            response.setMaThongBaoBdg(maThongBaoBdg);
            response.setHinhThucDauGia(hinhThucDauGia);
            response.setPhuongThucDauGia(phuongThucDauGia);
            response.setBienBanBdgId(bbBanDauGiaId);
            response.setSoBienBanBdg(soBienBanDauGia);
            response.setNgayToChuc(ngayToChuc);
            response.setMaVatTuCha(maVatTuCha);
            response.setTenVatTuCha(tenVatTuCha);
            response.setQdPheDuyetKhBdgId(qdPdKhBdgId);
            response.setSoQdPheDuyetKhBdg(soQdPdKhBdg);
            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, bhQdPheDuyetKqbdgRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (CollectionUtils.isEmpty(req.getIds()))
            return false;

        bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByQdPheDuyetKqbdgIdIn(req.getIds()).forEach(p -> {
            p.setQdPheDuyetKqbdgId(null);
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.save(p);
        });
        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(BhQdPheDuyetKqbdg.TABLE_NAME));
        bhQdPheDuyetKqbdgRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(BhQdPheDuyetKqbdgSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<BhQdPheDuyetKqbdgRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_QD_PHE_DUYET_KQ_BDG, NGAY_KY, TRICH_YEU, NGAY_TO_CHUC_BDG,
                SO_QD_PHE_DUYET_KH_BDG, MA_THONG_BAO_BDG, HINH_THUC_DAU_GIA, PHUONG_THUC_DAU_GIA,
                LOAI_HANG_HOA, NAM_KE_HOACH, SO_TB_DAU_GIA_KHONG_THANH, SO_BIEN_BAN_DAU_GIA, TRANG_THAI};
        String filename = "Danh_sach_bien_ban_ban_dau_gia.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                BhQdPheDuyetKqbdgRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoQuyetDinh();
                objs[2] = LocalDateTimeUtils.localDateToString(item.getNgayKy());
                objs[3] = item.getTrichYeu();
                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayToChuc());
                objs[5] = item.getSoQdPheDuyetKhBdg();
                objs[6] = item.getMaThongBaoBdg();
                objs[7] = item.getHinhThucDauGia();
                objs[8] = item.getPhuongThucDauGia();
                objs[9] = item.getTenVatTuCha();
                objs[10] = item.getNam();
                objs[11] = item.getMaThongBaoBdgKt();
                objs[12] = item.getSoBienBanBdg();
                objs[13] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_QUYET_DINH_PHE_DUYET_KET_QUA_BAN_DAU_GIA, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    private void validateSoQd(BhQdPheDuyetKqbdg update, BhQdPheDuyetKqbdgReq req) throws Exception {
        String soQd = req.getSoQuyetDinh();
        if (!StringUtils.hasText(soQd))
            return;
        if (update == null || (StringUtils.hasText(update.getSoQuyetDinh()) && !update.getSoQuyetDinh().equalsIgnoreCase(soQd))) {
            Optional<BhQdPheDuyetKqbdg> optional = bhQdPheDuyetKqbdgRepository.findFirstBySoQuyetDinh(soQd);
            Long updateId = Optional.ofNullable(update).map(BhQdPheDuyetKqbdg::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số quyết định " + soQd + " đã tồn tại");
        }
    }
    @Override
    public Page<BhQdPheDuyetKqbdg> listData(BhQdPheDuyetKqbdgSearchReqExt reqExt, Pageable pageable) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        reqExt.setDvql(userInfo.getDvql());
        reqExt.toString();
        return bhQdPheDuyetKqbdgRepository.search(reqExt, pageable);
    }

    private void buildThongTinKho(List<BhQdPheDuyetKqbdgCtRes> responses) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(responses)) return;
        List<String> maLoKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaLoKho).collect(Collectors.toList());
        List<String> maNhaKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaNhaKho).collect(Collectors.toList());
        List<String> maDiemKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaDiemKho).collect(Collectors.toList());
        Set<String> maChiCucList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaChiCuc).collect(Collectors.toSet());
        Set<String> maNganKhoList = responses.stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::getMaNganKho).collect(Collectors.toSet());


        Map<String, KtNganLo> mapNganLo = ktNganLoRepository.findByMaNganloIn(maLoKhoList)
                .stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));

        Map<String, KtDiemKho> mapDiemKho = ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhoList)
                .stream().collect(Collectors.toMap(KtDiemKho::getMaDiemkho, Function.identity()));

        Map<String, KtNhaKho> mapNhaKho = ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhoList)
                .stream().collect(Collectors.toMap(KtNhaKho::getMaNhakho, Function.identity()));

        Map<String, QlnvDmDonvi> mapChiCuc = dmDonviRepository.findByMaDviIn(maChiCucList)
                .stream().collect(Collectors.toMap(QlnvDmDonvi::getMaDvi, Function.identity()));

        Map<String, KtNganKho> mapNganKho = ktNganKhoRepository.findByMaNgankhoIn(maNganKhoList)
                .stream().collect(Collectors.toMap(KtNganKho::getMaNgankho, Function.identity()));

        for (BhQdPheDuyetKhBdgThongTinTaiSanResponse item : responses) {
            KtNganLo nganLo = mapNganLo.get(item.getMaLoKho());
            KtNhaKho nhaKho = mapNhaKho.get(item.getMaNhaKho());
            KtDiemKho diemKho = mapDiemKho.get(item.getMaDiemKho());
            QlnvDmDonvi chiCuc = mapChiCuc.get(item.getMaChiCuc());
            KtNganKho nganKho = mapNganKho.get(item.getMaNganKho());
            if (nganLo != null) item.setTenLoKho(nganLo.getTenNganlo());
            if (nhaKho != null) item.setTenNhaKho(nhaKho.getTenNhakho());
            if (diemKho != null) item.setTenDiemKho(diemKho.getTenDiemkho());
            if (chiCuc != null) item.setTenChiCuc(chiCuc.getTenDvi());
            if (nganKho != null) item.setTenNganKho(nganKho.getTenNgankho());
        }
    }
}
