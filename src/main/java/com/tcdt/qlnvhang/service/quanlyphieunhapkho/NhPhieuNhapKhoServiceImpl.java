package com.tcdt.qlnvhang.service.quanlyphieunhapkho;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.vattu.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
import org.springframework.data.domain.*;
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

@Service
@Log4j2
public class NhPhieuNhapKhoServiceImpl extends BaseServiceImpl implements NhPhieuNhapKhoService {

    private static final String SHEET_PHIEU_NHAP_HANG_LUONG_THUC = "Phiếu nhập hàng lương thực";
    private static final String STT = "STT";
    private static final String SO_PHIEU = "Số Phiếu";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_NHAP_KHO = "Ngày Nhập Kho";
    private static final String DIEM_KHO = "Điểm KHo";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Ngăn Lô";

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private QlpktclhPhieuKtChatLuongRepository phieuKtChatLuongRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhPhieuNhapKhoRes create(NhPhieuNhapKhoReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        this.validateSoPhieu(null, req);

        NhPhieuNhapKho phieu = new NhPhieuNhapKho();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgayTao(LocalDate.now());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        phieu.setMaDvi(userInfo.getDvql());
        phieu.setCapDvi(userInfo.getCapDvi());
        nhPhieuNhapKhoRepository.save(phieu);

        List<NhPhieuNhapKhoCtReq> hangHoaReqs = req.getHangHoaList();
        List<NhPhieuNhapKhoCt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, new HashMap<>());
        phieu.setHangHoaList(hangHoaList);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME);
        phieu.setChungTus(fileDinhKems);

        return this.buildResponse(phieu);
    }

    private List<NhPhieuNhapKhoCt> saveListHangHoa(Long phieuNhapKhoId, List<NhPhieuNhapKhoCtReq> hangHoaReqs, Map<Long, NhPhieuNhapKhoCt> mapHangHoa) throws Exception {
        List<NhPhieuNhapKhoCt> hangHoaList = new ArrayList<>();
        Set<String> maVatTus = hangHoaReqs.stream().map(NhPhieuNhapKhoCtReq::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));
        for (NhPhieuNhapKhoCtReq req : hangHoaReqs) {
            Long id = req.getId();
            NhPhieuNhapKhoCt hangHoa = new NhPhieuNhapKhoCt();
            QlnvDmVattu vatTu = qlnvDmVattus.stream().filter(v -> v.getMa().equals(req.getMaVatTu())).findFirst().orElse(null);
            if (vatTu == null)
                throw new Exception("Hàng Hóa không tồn tại.");

            if (id != null) {
                hangHoa = mapHangHoa.get(id);
                if (hangHoa == null)
                    throw new Exception("Chi tiết hàng Hóa không tồn tại.");
                mapHangHoa.remove(id);
            }

            BeanUtils.copyProperties(req, hangHoa, "id");
            hangHoa.setPhieuNkId(phieuNhapKhoId);
            hangHoa.setMaVatTu(req.getMaVatTu());
            hangHoaList.add(hangHoa);
        }

        if (!CollectionUtils.isEmpty(hangHoaList))
            nhPhieuNhapKhoCtRepository.saveAll(hangHoaList);

        return hangHoaList;
    }

    private NhPhieuNhapKhoRes buildResponse(NhPhieuNhapKho phieu) throws Exception {
        Set<String> maVatTus = phieu.getHangHoaList().stream().map(NhPhieuNhapKhoCt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        NhPhieuNhapKhoRes response = new NhPhieuNhapKhoRes();
        BeanUtils.copyProperties(phieu, response);
        response.setTenTrangThai(TrangThaiEnum.getTenById(phieu.getTrangThai()));
        response.setTenTrangThai(TrangThaiEnum.getTrangThaiDuyetById(phieu.getTrangThai()));
        List<NhPhieuNhapKhoCt> hangHoaList = phieu.getHangHoaList();

        BigDecimal tongSoLuong = BigDecimal.ZERO;
        BigDecimal tongSoTien = BigDecimal.ZERO;

        for (NhPhieuNhapKhoCt hangHoa : hangHoaList) {
            NhPhieuNhapKhoCtRes hangHoaRes = new NhPhieuNhapKhoCtRes();
            BeanUtils.copyProperties(hangHoa, hangHoaRes);

            qlnvDmVattus.stream().filter(v -> v.getMa().equals(hangHoa.getMaVatTu())).findFirst().ifPresent(t -> {
                hangHoaRes.setTenVatTu(t.getTen());
            });
            response.getHangHoaRes().add(hangHoaRes);

            tongSoLuong = tongSoLuong.add(Optional.ofNullable(hangHoa.getSoLuongThucNhap()).orElse(BigDecimal.ZERO));
            tongSoTien = tongSoTien.add(Optional.ofNullable(hangHoa.getThanhTien()).orElse(BigDecimal.ZERO));
        }

        Map<String, QlnvDmDonvi> mapDmucDvi = getMapDvi();
        QlnvDmDonvi qlnvDmDonvi = mapDmucDvi.get(phieu.getMaDvi());
        if (qlnvDmDonvi == null)
            throw new Exception("Bad request.");

        response.setMaDvi(qlnvDmDonvi.getMaDvi());
        response.setTenDvi(qlnvDmDonvi.getTenDvi());
        response.setMaQhns(qlnvDmDonvi.getMaQhns());
        response.setTongSoLuong(tongSoLuong);
        response.setTongSoTien(tongSoTien);
        response.setTongSoLuongBangChu(MoneyConvert.docSoLuong(tongSoLuong.toString(), null));
        response.setTongSoTienBangChu(MoneyConvert.doctienBangChu(tongSoTien.toString(), null));

        if (phieu.getPhieuKtClId() != null) {
            QlpktclhPhieuKtChatLuong phieuKtChatLuong = phieuKtChatLuongRepository.findById(phieu.getPhieuKtClId())
                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu kiểm tra chất lượng"));

            response.setSoPhieuKtCl(phieuKtChatLuong.getSoPhieu());
        }

        if (phieu.getHoSoKyThuatId() != null) {
            NhHoSoKyThuat hskt = nhHoSoKyThuatRepository.findById(phieu.getHoSoKyThuatId())
                    .orElseThrow(() -> new Exception("Không tìm thấy biên bản hồ sơ kỹ thuật"));

            response.setSoBbHoSoKyThuat(hskt.getSoBienBan());
        }

        if (phieu.getQdgnvnxId() != null) {
            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(phieu.getQdgnvnxId());
            if (!qdNhap.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
        }

        KtNganLo nganLo = null;
        if (StringUtils.hasText(phieu.getMaNganLo())) {
            nganLo = ktNganLoRepository.findFirstByMaNganlo(phieu.getMaNganLo());
        }

        this.thongTinNganLo(response, nganLo);
        response.setChungTus(fileDinhKemService.search(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME)));
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhPhieuNhapKhoRes update(NhPhieuNhapKhoReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhPhieuNhapKho> optionalQd = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Phiếu nhập kho không tồn tại.");

        this.validateSoPhieu(optionalQd.get(), req);

        NhPhieuNhapKho phieu = optionalQd.get();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgaySua(LocalDate.now());
        phieu.setNguoiSuaId(userInfo.getId());
        nhPhieuNhapKhoRepository.save(phieu);

        Map<Long, NhPhieuNhapKhoCt> mapHangHoa = nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId())
                .stream().collect(Collectors.toMap(NhPhieuNhapKhoCt::getId, Function.identity()));

        List<NhPhieuNhapKhoCtReq> hangHoaReqs = req.getHangHoaList();
        List<NhPhieuNhapKhoCt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, mapHangHoa);
        phieu.setHangHoaList(hangHoaList);

        if (!CollectionUtils.isEmpty(mapHangHoa.values()))
            nhPhieuNhapKhoCtRepository.deleteAll(mapHangHoa.values());

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), NhPhieuNhapKho.TABLE_NAME);
        phieu.setChungTus(fileDinhKems);

        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        NhPhieuNhapKho phieu = optional.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<NhPhieuNhapKhoCt> hangHoaList = nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId());
        if (!CollectionUtils.isEmpty(hangHoaList)) {
            nhPhieuNhapKhoCtRepository.deleteAll(hangHoaList);
        }

        nhPhieuNhapKhoRepository.delete(phieu);
        fileDinhKemService.delete(phieu.getId(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
        return true;
    }

    @Override
    public NhPhieuNhapKhoRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        NhPhieuNhapKho phieu = optional.get();
        phieu.setHangHoaList(nhPhieuNhapKhoCtRepository.findAllByPhieuNkId(phieu.getId()));
        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        NhPhieuNhapKho phieu = optional.get();
        return this.updateStatus(req, phieu, userInfo);
    }

    public boolean updateStatus(StatusReq req, NhPhieuNhapKho phieu, UserInfo userInfo) throws Exception {
        String trangThai = phieu.getTrangThai();
        if (QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.MOI_TAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId());
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(LocalDate.now());

        } else if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;
            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.DA_DUYET.getId());
            phieu.setNguoiPheDuyetId(userInfo.getId());
            phieu.setNgayPheDuyet(LocalDate.now());
        } else if (QdPheDuyetKqlcntVtStatus.TU_CHOI.getId().equals(req.getTrangThai())) {
            if (!QdPheDuyetKqlcntVtStatus.CHO_DUYET.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(QdPheDuyetKqlcntVtStatus.TU_CHOI.getId());
            phieu.setNguoiPheDuyetId(userInfo.getId());
            phieu.setNgayPheDuyet(LocalDate.now());
            phieu.setLyDoTuChoi(req.getLyDo());
        } else {
            throw new Exception("Bad request.");
        }

        nhPhieuNhapKhoRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        return nhPhieuNhapKhoRepository.search(req);
    }

    private void thongTinNganLo(NhPhieuNhapKhoRes phieu, KtNganLo nganLo) {
        if (nganLo != null) {
            phieu.setTenNganLo(nganLo.getTenNganlo());
            KtNganKho nganKho = nganLo.getParent();
            if (nganKho == null)
                return;

            phieu.setTenNganKho(nganKho.getTenNgankho());
            phieu.setMaNganKho(nganKho.getMaNgankho());
            KtNhaKho nhaKho = nganKho.getParent();
            if (nhaKho == null)
                return;

            phieu.setTenNhaKho(nhaKho.getTenNhakho());
            phieu.setMaNhaKho(nhaKho.getMaNhakho());
            KtDiemKho diemKho = nhaKho.getParent();
            if (diemKho == null)
                return;

            phieu.setTenDiemKho(diemKho.getTenDiemkho());
            phieu.setMaDiemKho(diemKho.getMaDiemkho());
        }
    }

    @Override
    public BaseNhapHangCount count() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        NhPhieuNhapKhoSearchReq req = new NhPhieuNhapKhoSearchReq();
        this.prepareSearchReq(req, userInfo, null, req.getTrangThais());
        BaseNhapHangCount count = new BaseNhapHangCount();

        count.setTatCa(nhPhieuNhapKhoRepository.count(req));
        return count;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (CollectionUtils.isEmpty(req.getIds()))
            return false;


        nhPhieuNhapKhoCtRepository.deleteByPhieuNkIdIn(req.getIds());
        nhPhieuNhapKhoRepository.deleteByIdIn(req.getIds());
        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(NhPhieuNhapKho.TABLE_NAME));
        return true;
    }

    @Override
    public boolean exportToExcel(NhPhieuNhapKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhPhieuNhapKhoRes> list = this.search(objReq).get().collect(Collectors.toList());

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
            XSSFSheet sheet = workbook.createSheet(SHEET_PHIEU_NHAP_HANG_LUONG_THUC);
            Row row0 = sheet.createRow(0);
            //STT

            ExportExcel.createCell(row0, 0, STT, style, sheet);
            ExportExcel.createCell(row0, 1, SO_PHIEU, style, sheet);
            ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
            ExportExcel.createCell(row0, 3, NGAY_NHAP_KHO, style, sheet);
            ExportExcel.createCell(row0, 4, DIEM_KHO, style, sheet);
            ExportExcel.createCell(row0, 5, NHA_KHO, style, sheet);
            ExportExcel.createCell(row0, 6, NGAN_KHO, style, sheet);
            ExportExcel.createCell(row0, 7, NGAN_LO, style, sheet);
            ExportExcel.createCell(row0, 8, TRANG_THAI, style, sheet);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeight(11);
            style.setFont(font);

            Row row;
            int startRowIndex = 1;

            for (NhPhieuNhapKhoRes item : list) {
                row = sheet.createRow(startRowIndex);
                ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
                ExportExcel.createCell(row, 1, item.getSoPhieu(), style, sheet);
                ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
                ExportExcel.createCell(row, 3, LocalDateTimeUtils.localDateToString(item.getNgayNhapKho()), style, sheet);
                ExportExcel.createCell(row, 4, item.getTenDiemKho(), style, sheet);
                ExportExcel.createCell(row, 5, item.getTenNhaKho(), style, sheet);
                ExportExcel.createCell(row, 6, item.getTenNganKho(), style, sheet);
                ExportExcel.createCell(row, 7, item.getTenNganLo(), style, sheet);
                ExportExcel.createCell(row, 8, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
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

    private void validateSoPhieu(NhPhieuNhapKho update, NhPhieuNhapKhoReq req) throws Exception {
        String so = req.getSoPhieu();
        if (!StringUtils.hasText(so))
            return;

        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
            Optional<NhPhieuNhapKho> optional = nhPhieuNhapKhoRepository.findFirstBySoPhieu(so);
            Long updateId = Optional.ofNullable(update).map(NhPhieuNhapKho::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số phiếu " + so + " đã tồn tại");
        }
    }

    @Override
    public SoBienBanPhieuRes getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = nhPhieuNhapKhoRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return new SoBienBanPhieuRes(so, LocalDate.now().getYear());
    }
}
