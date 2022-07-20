package com.tcdt.qlnvhang.service.nhaphang.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.vattu.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.vattu.hosokythuat.NhHoSoKyThuatCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatCtRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatCtReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;
import com.tcdt.qlnvhang.response.vattu.hosokythuat.NhHoSoKyThuatCtRes;
import com.tcdt.qlnvhang.response.vattu.hosokythuat.NhHoSoKyThuatRes;
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
@RequiredArgsConstructor
@Log4j2
public class NhHoSoKyThuatServiceImpl extends BaseServiceImpl implements NhHoSoKyThuatService {

    private final NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
    private final NhHoSoKyThuatCtRepository nhHoSoKyThuatCtRepository;
    private final FileDinhKemService fileDinhKemService;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final HhHopDongRepository hhHopDongRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final HttpServletRequest req;
    private static final String SHEET_HO_SO_KY_THUAT = "Hồ Sơ Kỹ Thuật";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY = "Ngày";
    private static final String LOAI_HANG_HOA = "Loại Hàng Hóa";
    private static final String CHUNG_LOAI_HANG_HO = "Chủng Loại Hàng Hóa";
    private static final String KET_LUAT = "Kết Luận";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhHoSoKyThuatRes create(NhHoSoKyThuatReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoBb(null, req);

        NhHoSoKyThuat item = new NhHoSoKyThuat();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "HSKT", userInfo.getMaPbb()));
        nhHoSoKyThuatRepository.save(item);

        List<NhHoSoKyThuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhHoSoKyThuat.TABLE_NAME));
        item.setFdkCanCus(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhHoSoKyThuat.CAN_CU));

        return this.buildResponse(item);
    }

    private List<NhHoSoKyThuatCt> saveListChiTiet(Long parentId,
                                                     List<NhHoSoKyThuatCtReq> chiTietReqs,
                                                     Map<Long, NhHoSoKyThuatCt> mapChiTiet) throws Exception {
        List<NhHoSoKyThuatCt> chiTiets = new ArrayList<>();
        for (NhHoSoKyThuatCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhHoSoKyThuatCt chiTiet = new NhHoSoKyThuatCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản gửi hàng chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setHoSoKyThuatId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            nhHoSoKyThuatCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    private NhHoSoKyThuatRes buildResponse(NhHoSoKyThuat item) throws Exception {
        NhHoSoKyThuatRes res = new NhHoSoKyThuatRes();
        List<NhHoSoKyThuatCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhHoSoKyThuatCt ct : item.getChiTiets()) {
            chiTiets.add(new NhHoSoKyThuatCtRes(ct));
        }
        res.setChiTiets(chiTiets);
        res.setFileDinhKems(item.getFileDinhKems());
        res.setFdkCanCus(item.getFdkCanCus());
        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
        res.setTenDvi(donvi.getTenDvi());
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
            Optional<HhHopDongHdr> hd = hhHopDongRepository.findById(item.getHopDongId());
            if (!hd.isPresent()) {
                throw new Exception("Không tìm thấy hợp đồng");
            }
            res.setSoHopDong(hd.get().getSoHd());
        }

        if (item.getBienBanGiaoMauId() != null) {
            //TODO: Bieen ban giao mau
        }
        return res;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhHoSoKyThuatRes update(NhHoSoKyThuatReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        this.validateSoBb(optional.get(), req);

        NhHoSoKyThuat item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        nhHoSoKyThuatRepository.save(item);
        Map<Long, NhHoSoKyThuatCt> mapChiTiet = nhHoSoKyThuatCtRepository.findByHoSoKyThuatIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhHoSoKyThuatCt::getId, Function.identity()));

        List<NhHoSoKyThuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            nhHoSoKyThuatCtRepository.deleteAll(mapChiTiet.values());

        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhHoSoKyThuat.TABLE_NAME));
        item.setFdkCanCus(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhHoSoKyThuat.CAN_CU));
        return this.buildResponse(item);
    }

    @Override
    public NhHoSoKyThuatRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhHoSoKyThuat item = optional.get();
        item.setChiTiets(nhHoSoKyThuatCtRepository.findByHoSoKyThuatIdIn(Collections.singleton(item.getId())));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhHoSoKyThuat.TABLE_NAME)));
        item.setFdkCanCus(fileDinhKemService.search(item.getId(), Collections.singleton(NhHoSoKyThuat.CAN_CU)));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhHoSoKyThuat item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã ban hành");
        }
        nhHoSoKyThuatCtRepository.deleteByHoSoKyThuatIdIn(Collections.singleton(item.getId()));
        nhHoSoKyThuatRepository.delete(item);
        fileDinhKemService.delete(item.getId(), Arrays.asList(NhHoSoKyThuat.TABLE_NAME, NhHoSoKyThuat.CAN_CU));
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhHoSoKyThuat phieu = optional.get();

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

        nhHoSoKyThuatRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhHoSoKyThuatRes> search(NhHoSoKyThuatSearchReq req) throws Exception {
        // TODO: Bien ban giao mau
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = nhHoSoKyThuatRepository.search(req);
        List<NhHoSoKyThuatRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhHoSoKyThuatRes response = new NhHoSoKyThuatRes();
            NhHoSoKyThuat item = (NhHoSoKyThuat) o[0];
            Long qdNhapId = (Long) o[1];
            String soQdNhap = (String) o[2];
            String maVatTu = (String) o[3];
            String tenVatTu = (String) o[4];
            String maVatTuCha = (String) o[5];
            String tenVatTuCha = (String) o[6];
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setMaVatTu(maVatTu);
            response.setTenVatTu(tenVatTu);
            response.setMaVatTuCha(maVatTuCha);
            response.setTenVatTuCha(tenVatTuCha);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, nhHoSoKyThuatRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        nhHoSoKyThuatCtRepository.deleteByHoSoKyThuatIdIn(req.getIds());
        nhHoSoKyThuatRepository.deleteByIdIn(req.getIds());
        fileDinhKemService.deleteMultiple(req.getIds(), Arrays.asList(NhHoSoKyThuat.TABLE_NAME, NhHoSoKyThuat.CAN_CU));
        return true;
    }

    @Override
    public boolean exportToExcel(NhHoSoKyThuatSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhHoSoKyThuatRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY,
                LOAI_HANG_HOA, CHUNG_LOAI_HANG_HO, KET_LUAT, TRANG_THAI};
        String filename = "Danh_sach_ho_so_ky_thuat.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                NhHoSoKyThuatRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBienBan();
                objs[2] = item.getSoQuyetDinhNhap();
                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayKiemTra());
                objs[4] = item.getTenVatTuCha();
                objs[5] = item.getTenVatTu();
                objs[6] = item.getKetLuan();
                objs[7] = TrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_HO_SO_KY_THUAT, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    private void validateSoBb(NhHoSoKyThuat update, NhHoSoKyThuatReq req) throws Exception {
        String so = req.getSoBienBan();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(NhHoSoKyThuat::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số biên bản " + so + " đã tồn tại");
        }
    }

    @Override
    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = nhHoSoKyThuatRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }
}
