package com.tcdt.qlnvhang.service.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
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
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
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
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBCBK", userInfo.getMaPBb()));
        nhBienBanChuanBiKhoRepository.save(item);

        List<NhBienBanChuanBiKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    private NhBienBanChuanBiKhoRes buildResponse(NhBienBanChuanBiKho item) throws Exception {
        NhBienBanChuanBiKhoRes res = new NhBienBanChuanBiKhoRes();
        List<NhBienBanChuanBiKhoCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
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
        return res;
    }

    private List<NhBienBanChuanBiKhoCt> saveListChiTiet(Long parentId,
                                                     List<NhBienBanChuanBiKhoCtReq> chiTietReqs,
                                                     Map<Long, NhBienBanChuanBiKhoCt> mapChiTiet) throws Exception {
        List<NhBienBanChuanBiKhoCt> chiTiets = new ArrayList<>();
        for (NhBienBanChuanBiKhoCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBienBanChuanBiKhoCt chiTiet = new NhBienBanChuanBiKhoCt();

            if (id != null) {
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
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã ban hành");
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

        nhBienBanChuanBiKhoRepository.save(phieu);
        return true;
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
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setTenVatTu(tenVatTu);
            response.setTenVatTuCha(tenVatTuCha);
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

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            //STYLE
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(11);
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_CHUAN_BI_KHO);
            Row row0 = sheet.createRow(0);
            //STT

            ExportExcel.createCell(row0, 0, STT, style, sheet);
            ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
            ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
            ExportExcel.createCell(row0, 3, NGAY, style, sheet);
            ExportExcel.createCell(row0, 4, LOAI_HANG_HOA, style, sheet);
            ExportExcel.createCell(row0, 5, CHUNG_LOAI_HANG_HOA, style, sheet);
            ExportExcel.createCell(row0, 6, DIEM_KHO, style, sheet);
            ExportExcel.createCell(row0, 7, NHA_KHO, style, sheet);
            ExportExcel.createCell(row0, 8, NGAN_KHO, style, sheet);
            ExportExcel.createCell(row0, 9, NGAN_LO, style, sheet);
            ExportExcel.createCell(row0, 10, TRANG_THAI, style, sheet);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeight(11);
            style.setFont(font);

            Row row;
            int startRowIndex = 1;

            for (NhBienBanChuanBiKhoRes item : list) {
                row = sheet.createRow(startRowIndex);
                ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
                ExportExcel.createCell(row, 1, item.getSoBienBan(), style, sheet);
                ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
                ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayTao()), style, sheet);
                ExportExcel.createCell(row, 4, item.getTenVatTuCha(), style, sheet);
                ExportExcel.createCell(row, 5, item.getTenVatTu(), style, sheet);
                ExportExcel.createCell(row, 6, item.getTenDiemKho(), style, sheet);
                ExportExcel.createCell(row, 7, item.getTenNhaKho(), style, sheet);
                ExportExcel.createCell(row, 8, item.getTenNganKho(), style, sheet);
                ExportExcel.createCell(row, 9, item.getTenNganLo(), style, sheet);
                ExportExcel.createCell(row, 10, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
                startRowIndex++;
            }

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
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
