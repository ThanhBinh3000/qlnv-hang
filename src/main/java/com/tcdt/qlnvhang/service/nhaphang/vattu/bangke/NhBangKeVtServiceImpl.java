package com.tcdt.qlnvhang.service.nhaphang.vattu.bangke;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.vattu.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.entities.vattu.bangke.NhBangKeVtCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bangke.NhBangKeVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.bangke.NhBangKeVtCtRes;
import com.tcdt.qlnvhang.response.vattu.bangke.NhBangKeVtRes;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class NhBangKeVtServiceImpl extends BaseServiceImpl implements NhBangKeVtService {
    private final NhBangKeVtRepository bangKeVtRepository;
    private final NhBangKeVtCtRepository bangKeVtCtRepository;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final NhPhieuNhapKhoRepository phieuNhapKhoRepository;
    private final QlnvDmVattuRepository qlnvDmVattuRepository;
    private final HhHopDongRepository hhHopDongRepository;

    private static final String SHEET_BANG_KE_NHAP_VAT_TU = "Bảng kê nhập vật tư";
    private static final String STT = "STT";
    private static final String SO_BANG_KE = "Số Bảng Kê";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_TAO_BANG_KE = "Ngày Tạo Bảng Kê";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    public NhBangKeVtRes create(NhBangKeVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoPhieu(null, req);

        NhBangKeVt item = new NhBangKeVt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBangKe(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BKNVT", userInfo.getMaPbb()));
        bangKeVtRepository.save(item);

        List<NhBangKeVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    private List<NhBangKeVtCt> saveListChiTiet(Long parentId,
                                               List<NhBangKeVtCtReq> chiTietReqs,
                                               Map<Long, NhBangKeVtCt> mapChiTiet) throws Exception {
        List<NhBangKeVtCt> chiTiets = new ArrayList<>();
        for (NhBangKeVtCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBangKeVtCt chiTiet = new NhBangKeVtCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Phiếu nhập kho vật tư chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBangKeVtId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            bangKeVtCtRepository.saveAll(chiTiets);

        return chiTiets;
    }


    private NhBangKeVtRes buildResponse(NhBangKeVt item) throws Exception {
        NhBangKeVtRes res = new NhBangKeVtRes();
        List<NhBangKeVtCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhBangKeVtCt ct : item.getChiTiets()) {
            chiTiets.add(new NhBangKeVtCtRes(ct));
        }
        res.setChiTiets(chiTiets);

        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<QlnvDmVattu> vatTus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));

        if (CollectionUtils.isEmpty(vatTus))
            throw new Exception("Không tìm thấy vật tư");

        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
                .ifPresent(v -> res.setTenVatTu(v.getTen()));
        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
                .ifPresent(v -> res.setTenVatTuCha(v.getTen()));

        if (item.getQdgnvnxId() != null) {
            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
            if (!qdNhap.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
        }

        if (item.getPhieuNhapKhoId() != null) {
            Optional<NhPhieuNhapKho> phieuNhapKho = phieuNhapKhoRepository.findById(item.getPhieuNhapKhoId());
            if (!phieuNhapKho.isPresent()) {
                throw new Exception("Không tìm thấy phiếu nhập kho");
            }
            res.setSoPhieuNhapKho(phieuNhapKho.get().getSoPhieu());
        }

        if (item.getHopDongId() != null) {
            Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findById(item.getHopDongId());
            if (!qOpHdong.isPresent())
                throw new Exception("Hợp đồng không tồn tại");

            res.setSoHopDong(qOpHdong.get().getSoHd());
        }

        return res;
    }

    @Override
    public NhBangKeVtRes update(NhBangKeVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho vật tư không tồn tại.");

        this.validateSoPhieu(optional.get(), req);

        NhBangKeVt item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        bangKeVtRepository.save(item);
        Map<Long, NhBangKeVtCt> mapChiTiet = bangKeVtCtRepository.findByBangKeVtIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhBangKeVtCt::getId, Function.identity()));

        List<NhBangKeVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            bangKeVtCtRepository.deleteAll(mapChiTiet.values());


        return this.buildResponse(item);
    }

    @Override
    public NhBangKeVtRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho vật tư không tồn tại.");

        NhBangKeVt item = optional.get();
        item.setChiTiets(bangKeVtCtRepository.findByBangKeVtIdIn(Collections.singleton(item.getId())));
        return this.buildResponse(item);
    }

    @Override
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho vật tư không tồn tại.");

        NhBangKeVt item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã ban hành");
        }
        bangKeVtCtRepository.deleteByBangKeVtIdIn(Collections.singleton(item.getId()));
        bangKeVtRepository.delete(item);
        return true;
    }

    @Override
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho vật tư không tồn tại.");

        NhBangKeVt phieu = optional.get();

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

        bangKeVtRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhBangKeVtRes> search(NhBangKeVtSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = bangKeVtRepository.search(req);
        List<NhBangKeVtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhBangKeVtRes response = new NhBangKeVtRes();
            NhBangKeVt item = (NhBangKeVt) o[0];
            Long qdNhapId = (Long) o[1];
            String soQdNhap = (String) o[2];
            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            this.thongTinNganLo(response, nganLo);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, bangKeVtRepository.count(req));
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        bangKeVtCtRepository.deleteByBangKeVtIdIn(req.getIds());
        bangKeVtRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(NhBangKeVtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhBangKeVtRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_BANG_KE, SO_QUYET_DINH_NHAP, NGAY_TAO_BANG_KE,
                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
        String filename = "Danh_sach_bang_ke_nhap_vat_tu.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                NhBangKeVtRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBangKe();
                objs[2] = item.getSoQuyetDinhNhap();
                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayTaoBangKe());
                objs[4] = item.getTenDiemKho();
                objs[5] = item.getTenNhaKho();
                objs[6] = item.getTenNganKho();
                objs[7] = item.getTenNganLo();
                objs[8] = TrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BANG_KE_NHAP_VAT_TU, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = bangKeVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }

    private void validateSoPhieu(NhBangKeVt update, NhBangKeVtReq req) throws Exception {
        String so = req.getSoBangKe();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBangKe()) && !update.getSoBangKe().equalsIgnoreCase(so))) {
            Optional<NhBangKeVt> optional = bangKeVtRepository.findFirstBySoBangKe(so);
            Long updateId = Optional.ofNullable(update).map(NhBangKeVt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số bảng kê " + so + " đã tồn tại");
        }
    }

    private void thongTinNganLo(NhBangKeVtRes item, KtNganLo nganLo) {
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
