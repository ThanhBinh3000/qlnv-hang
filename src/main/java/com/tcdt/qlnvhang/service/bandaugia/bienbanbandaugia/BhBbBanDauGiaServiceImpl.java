package com.tcdt.qlnvhang.service.bandaugia.bienbanbandaugia;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGiaCt;
import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSan;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia.BhBbBanDauGiaCtRepository;
import com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia.BhBbBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.bandaugia.kehoachbanhangdaugia.BanDauGiaPhanLoTaiSanRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaCt1Req;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaCtReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaReq;
import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia.BhBbBanDauGiaCtRes;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia.BhBbBanDauGiaRes;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BhBbBanDauGiaServiceImpl extends BaseServiceImpl implements BhBbBanDauGiaService {
    private final BhBbBanDauGiaRepository bhBbBanDauGiaRepository;
    private final BhBbBanDauGiaCtRepository bhBbBanDauGiaCtRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final BanDauGiaPhanLoTaiSanRepository banDauGiaPhanLoTaiSanRepository;

    private static final String SHEET_BIEN_BAN_BAN_DAU_GIA = "Biên bản bán đấu giá";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN_BDG = "Số Biên bản BĐG";
    private static final String DON_VI = "Đơn Vị";
    private static final String NGAY_TO_CHUC_BDG = "Ngày Tổ Chức BĐG";
    private static final String TRICH_YEU = "Trích Yếu";
    private static final String SO_QD_PHE_DUYET_KH_BDG = "Số QĐ Phê Duyệt KH BĐG";
    private static final String MA_THONG_BAO_BDG = "Mã Thông Báo BĐG";
    private static final String HINH_THUC_DAU_GIA = "Hình Thức Đấu Giá";
    private static final String PHUONG_THUC_DAU_GIA = "Phương Thức Đấu Giá";
    private static final String LOAI_HANG_HOA = "Loại Hàng Hóa";
    private static final String NAM_KE_HOACH = "Năm Kế Hoạch";
    private static final String SO_QD_PHE_DUYET_KQ_BDG = "Số QĐ Phê Duyệt KQ BĐG";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BhBbBanDauGiaRes create(BhBbBanDauGiaReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoPhieu(null, req);

        BhBbBanDauGia item = new BhBbBanDauGia();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BDG", userInfo.getMaPbb()));
        bhBbBanDauGiaRepository.save(item);

        List<BhBbBanDauGiaCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), new HashMap<>());
        item.setCts(chiTiets);

        List<BanDauGiaPhanLoTaiSan> ct1s = this.saveListChiTiet1(item.getId(), req.getCt1s(), new HashMap<>());
        item.setCt1s(ct1s);
        return this.buildResponse(item);
    }

    private List<BhBbBanDauGiaCt> saveListChiTiet(Long parentId,
                                               List<BhBbBanDauGiaCtReq> chiTietReqs,
                                               Map<Long, BhBbBanDauGiaCt> mapChiTiet) throws Exception {
        List<BhBbBanDauGiaCt> chiTiets = new ArrayList<>();
        for (BhBbBanDauGiaCtReq req : chiTietReqs) {
            Long id = req.getId();
            BhBbBanDauGiaCt chiTiet = new BhBbBanDauGiaCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản bán đấu giá chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBbBanDauGiaId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            bhBbBanDauGiaCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    private List<BanDauGiaPhanLoTaiSan> saveListChiTiet1(Long parentId,
                                                  List<BhBbBanDauGiaCt1Req> chiTietReqs,
                                                  Map<Long, BanDauGiaPhanLoTaiSan> mapChiTiet) throws Exception {
        List<BanDauGiaPhanLoTaiSan> chiTiets = new ArrayList<>();
        for (BhBbBanDauGiaCt1Req req : chiTietReqs) {
            Long id = req.getId();
            BanDauGiaPhanLoTaiSan chiTiet = new BanDauGiaPhanLoTaiSan();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Phân lô tài sản không tồn tại.");
                mapChiTiet.remove(id);
            }

            chiTiet.setBbBanDauGiaId(parentId);
            chiTiet.setSoLanTraGia(req.getSoLanTraGia());
            chiTiet.setDonGiaCaoNhat(req.getDonGiaCaoNhat());
            chiTiet.setThanhTien(req.getThanhTien());
            chiTiet.setTraGiaCaoNhat(req.getTraGiaCaoNhat());
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            banDauGiaPhanLoTaiSanRepository.saveAll(chiTiets);

        return chiTiets;
    }


    private BhBbBanDauGiaRes buildResponse(BhBbBanDauGia item) throws Exception {
        BhBbBanDauGiaRes res = new BhBbBanDauGiaRes();
        List<BhBbBanDauGiaCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (BhBbBanDauGiaCt ct : item.getCts()) {
            chiTiets.add(new BhBbBanDauGiaCtRes(ct));
        }
        res.setCt(chiTiets);

        Set<String> maVatTus = Collections.singleton(item.getMaVatTuCha());
        Set<QlnvDmVattu> vatTus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));

        if (CollectionUtils.isEmpty(vatTus))
            throw new Exception("Không tìm thấy vật tư");

        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
                .ifPresent(v -> res.setTenVatTuCha(v.getTen()));

        this.setThongTinDonVi(res, item.getMaDvi());
        return res;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BhBbBanDauGiaRes update(BhBbBanDauGiaReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<BhBbBanDauGia> optional = bhBbBanDauGiaRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        this.validateSoPhieu(optional.get(), req);

        BhBbBanDauGia item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        bhBbBanDauGiaRepository.save(item);
        Map<Long, BhBbBanDauGiaCt> mapChiTiet = bhBbBanDauGiaCtRepository.findByBbBanDauGiaIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(BhBbBanDauGiaCt::getId, Function.identity()));

        List<BhBbBanDauGiaCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), mapChiTiet);
        item.setCts(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            bhBbBanDauGiaCtRepository.deleteAll(mapChiTiet.values());

        Map<Long, BanDauGiaPhanLoTaiSan> mapChiTiet1 = banDauGiaPhanLoTaiSanRepository.findByBbBanDauGiaIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(BanDauGiaPhanLoTaiSan::getId, Function.identity()));

        // Bien ban phan lo
        List<BanDauGiaPhanLoTaiSan> chiTiet1s = this.saveListChiTiet1(item.getId(), req.getCt1s(), mapChiTiet1);
        item.setCt1s(chiTiet1s);

        return this.buildResponse(item);
    }

    @Override
    public BhBbBanDauGiaRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhBbBanDauGia> optional = bhBbBanDauGiaRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhBbBanDauGia item = optional.get();
        item.setCts(bhBbBanDauGiaCtRepository.findByBbBanDauGiaIdIn(Collections.singleton(item.getId())));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhBbBanDauGia> optional = bhBbBanDauGiaRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhBbBanDauGia item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Biên bản bán đấu giá đã ban hành");
        }
        bhBbBanDauGiaCtRepository.deleteByBbBanDauGiaIdIn(Collections.singleton(item.getId()));
        bhBbBanDauGiaRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhBbBanDauGia> optional = bhBbBanDauGiaRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản bán đấu giá không tồn tại.");

        BhBbBanDauGia phieu = optional.get();

        String trangThai = phieu.getTrangThai();
        if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getId());
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(LocalDate.now());
        } else if (TrangThaiEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
                return false;
            phieu.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
        } else if (TrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.LANH_DAO_DUYET.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(TrangThaiEnum.BAN_HANH.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
        } else if (TrangThaiEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(TrangThaiEnum.TU_CHOI.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
            phieu.setLyDoTuChoi(stReq.getLyDo());
        }  else {
            throw new Exception("Bad request.");
        }

        bhBbBanDauGiaRepository.save(phieu);
        return true;
    }

    @Override
    public Page<BhBbBanDauGiaRes> search(BhBbBanDauGiaSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = bhBbBanDauGiaRepository.search(req, pageable);
        List<BhBbBanDauGiaRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            BhBbBanDauGiaRes response = new BhBbBanDauGiaRes();
            BhBbBanDauGia item = (BhBbBanDauGia) o[0];
            Long thongBaoBdgId = o[1] != null ? (Long) o[1] : null;
            String maThongBaoBdg = o[2] != null ? (String) o[2] : null;

            BeanUtils.copyProperties(item, response);
            response.setThongBaoBdgId(thongBaoBdgId);
            response.setMaThongBaoBdg(maThongBaoBdg);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, bhBbBanDauGiaRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        bhBbBanDauGiaCtRepository.deleteByBbBanDauGiaIdIn(req.getIds());
        bhBbBanDauGiaRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(BhBbBanDauGiaSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<BhBbBanDauGiaRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_BIEN_BAN_BDG, DON_VI, NGAY_TO_CHUC_BDG,
                TRICH_YEU, SO_QD_PHE_DUYET_KH_BDG, MA_THONG_BAO_BDG, HINH_THUC_DAU_GIA, PHUONG_THUC_DAU_GIA,
                LOAI_HANG_HOA, NAM_KE_HOACH, SO_QD_PHE_DUYET_KQ_BDG, TRANG_THAI};
        String filename = "Danh_sach_bien_ban_ban_dau_gia.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                BhBbBanDauGiaRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBienBan();
                objs[2] = item.getTenDvi();
                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayToChuc());
                objs[4] = item.getTrichYeu();
                objs[5] = item.getSoQdPdKhBdg();
                objs[6] = item.getMaThongBaoBdg();
                objs[7] = item.getHinhThucDauGia();
                objs[8] = item.getPhuongThucDauGia();
                objs[9] = item.getTenVatTuCha();
                objs[10] = item.getNam();
                objs[11] = item.getSoQdPdKqBdg();
                objs[12] = TrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_BAN_DAU_GIA, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = bhBbBanDauGiaRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }

    private void validateSoPhieu(BhBbBanDauGia update, BhBbBanDauGiaReq req) throws Exception {
        String so = req.getSoBienBan();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<BhBbBanDauGia> optional = bhBbBanDauGiaRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(BhBbBanDauGia::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số Biên bản bán đấu giá " + so + " đã tồn tại");
        }
    }
}
