package com.tcdt.qlnvhang.service.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVt;
import com.tcdt.qlnvhang.entities.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtSearchReq;
import com.tcdt.qlnvhang.response.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCtRes;
import com.tcdt.qlnvhang.response.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtRes;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
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
    private static final String TRANG_THAI = "Trạng Thái";;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBbKtNhapKhoVtRes create(NhBbKtNhapKhoVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoBb(null, req);

        NhBbKtNhapKhoVt item = new NhBbKtNhapKhoVt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBienBan(String.format("%s/%s/%s", item.getSo(), item.getNam(), "BBKTNK"));
        nhBbKtNhapKhoVtRepository.save(item);

        List<NhBbKtNhapKhoVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbKtNhapKhoVt.TABLE_NAME));
        return this.buildResponse(item);
    }

    private NhBbKtNhapKhoVtRes buildResponse(NhBbKtNhapKhoVt item) throws Exception {
        NhBbKtNhapKhoVtRes res = new NhBbKtNhapKhoVtRes();
        List<NhBbKtNhapKhoVtCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhBbKtNhapKhoVtCt ct : item.getChiTiets()) {
            chiTiets.add(new NhBbKtNhapKhoVtCtRes(ct));
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

        if (item.getBbChuanBiKhoId() != null) {
            Optional<NhBienBanChuanBiKho> bienBanChuanBiKho = nhBienBanChuanBiKhoRepository.findById(item.getBbChuanBiKhoId());
            if (!bienBanChuanBiKho.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            res.setSoBbChuanBiKho(bienBanChuanBiKho.get().getSoBienBan());
        }
        return res;
    }

    private List<NhBbKtNhapKhoVtCt> saveListChiTiet(Long parentId,
                                                        List<NhBbKtNhapKhoVtCtReq> chiTietReqs,
                                                        Map<Long, NhBbKtNhapKhoVtCt> mapChiTiet) throws Exception {
        List<NhBbKtNhapKhoVtCt> chiTiets = new ArrayList<>();
        for (NhBbKtNhapKhoVtCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBbKtNhapKhoVtCt chiTiet = new NhBbKtNhapKhoVtCt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBbKtNhapKhoId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            nhBbKtNhapKhoVtCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBbKtNhapKhoVtRes update(NhBbKtNhapKhoVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        this.validateSoBb(optional.get(), req);

        NhBbKtNhapKhoVt item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        nhBbKtNhapKhoVtRepository.save(item);
        Map<Long, NhBbKtNhapKhoVtCt> mapChiTiet = nhBbKtNhapKhoVtCtRepository.findByBbKtNhapKhoIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhBbKtNhapKhoVtCt::getId, Function.identity()));

        List<NhBbKtNhapKhoVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            nhBbKtNhapKhoVtCtRepository.deleteAll(mapChiTiet.values());
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbKtNhapKhoVt.TABLE_NAME));

        return this.buildResponse(item);
    }

    @Override
    public NhBbKtNhapKhoVtRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");

        NhBbKtNhapKhoVt item = optional.get();
        item.setChiTiets(nhBbKtNhapKhoVtCtRepository.findByBbKtNhapKhoIdIn(Collections.singleton(item.getId())));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbKtNhapKhoVt.TABLE_NAME)));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhBbKtNhapKhoVt item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã ban hành");
        }
        nhBbKtNhapKhoVtCtRepository.deleteByBbKtNhapKhoIdIn(Collections.singleton(item.getId()));
        nhBbKtNhapKhoVtRepository.delete(item);
        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbKtNhapKhoVt.TABLE_NAME));
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhBbKtNhapKhoVt phieu = optional.get();

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

        nhBbKtNhapKhoVtRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhBbKtNhapKhoVtRes> search(NhBbKtNhapKhoVtSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = nhBbKtNhapKhoVtRepository.search(req);
        List<NhBbKtNhapKhoVtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhBbKtNhapKhoVtRes response = new NhBbKtNhapKhoVtRes();
            NhBbKtNhapKhoVt item = (NhBbKtNhapKhoVt) o[0];
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

        return new PageImpl<>(responses, pageable, nhBbKtNhapKhoVtRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        nhBbKtNhapKhoVtCtRepository.deleteByBbKtNhapKhoIdIn(req.getIds());
        nhBbKtNhapKhoVtRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(NhBbKtNhapKhoVtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhBbKtNhapKhoVtRes> list = this.search(objReq).get().collect(Collectors.toList());

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
            XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_KET_THUC_NHAP_KHO_VAT_TU);
            Row row0 = sheet.createRow(0);
            //STT

            ExportExcel.createCell(row0, 0, STT, style, sheet);
            ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
            ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
            ExportExcel.createCell(row0, 3, NGAY_NHAP_DAY_KHO, style, sheet);
            ExportExcel.createCell(row0, 4, DIEM_KHO, style, sheet);
            ExportExcel.createCell(row0, 5, NHA_KHO, style, sheet);
            ExportExcel.createCell(row0, 6, NGAN_KHO, style, sheet);
            ExportExcel.createCell(row0, 7, NGAN_LO, style, sheet);
            ExportExcel.createCell(row0, 8, NGAN_LO, style, sheet);
            ExportExcel.createCell(row0, 9, TRANG_THAI, style, sheet);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeight(11);
            style.setFont(font);

            Row row;
            int startRowIndex = 1;

            for (NhBbKtNhapKhoVtRes item : list) {
                row = sheet.createRow(startRowIndex);
                ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
                ExportExcel.createCell(row, 1, item.getSoBienBan(), style, sheet);
                ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
                ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayKetThucKho()), style, sheet);
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
        Integer so = nhBbKtNhapKhoVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }

    private void validateSoBb(NhBbKtNhapKhoVt update, NhBbKtNhapKhoVtReq req) throws Exception {
        String so = req.getSoBienBan();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<NhBbKtNhapKhoVt> optional = nhBbKtNhapKhoVtRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(NhBbKtNhapKhoVt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số biên bản " + so + " đã tồn tại");
        }
    }

    private void thongTinNganLo(NhBbKtNhapKhoVtRes item, KtNganLo nganLo) {
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
