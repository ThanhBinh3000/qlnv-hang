package com.tcdt.qlnvhang.service.quanlyphieunhapkholuongthuc;

import com.google.common.collect.Sets;
import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuongRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtReq;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRes;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class QlPhieuNhapKhoLtServiceImpl extends BaseServiceImpl implements QlPhieuNhapKhoLtService {

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
    private QlPhieuNhapKhoLtRepository qlPhieuNhapKhoLtRepository;

    @Autowired
    private QlPhieuNhapKhoHangHoaLtRepository qlPhieuNhapKhoHangHoaLtRepository;

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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlPhieuNhapKhoLtRes create(QlPhieuNhapKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        this.validateSoPhieu(null, req);

        QlPhieuNhapKhoLt phieu = new QlPhieuNhapKhoLt();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgayTao(LocalDate.now());
        phieu.setNguoiTaoId(userInfo.getId());
        phieu.setTrangThai(QlPhieuNhapKhoLtStatus.DU_THAO.getId());
        phieu.setMaDvi(userInfo.getDvql());
        phieu.setCapDvi(userInfo.getCapDvi());
        qlPhieuNhapKhoLtRepository.save(phieu);

        List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs = req.getHangHoaList();
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, new HashMap<>());
        phieu.setHangHoaList(hangHoaList);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), QlPhieuNhapKhoLt.TABLE_NAME);
        phieu.setChungTus(fileDinhKems);

        return this.buildResponse(phieu);
    }

    private List<QlPhieuNhapKhoHangHoaLt> saveListHangHoa(Long phieuNhapKhoId, List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs, Map<Long, QlPhieuNhapKhoHangHoaLt> mapHangHoa) throws Exception {
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = new ArrayList<>();
        Set<String> maVatTus = hangHoaReqs.stream().map(QlPhieuNhapKhoHangHoaLtReq::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));
        for (QlPhieuNhapKhoHangHoaLtReq req : hangHoaReqs) {
            Long id = req.getId();
            QlPhieuNhapKhoHangHoaLt hangHoa = new QlPhieuNhapKhoHangHoaLt();
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
            hangHoa.setQlPhieuNhapKhoLtId(phieuNhapKhoId);
            hangHoa.setMaVatTu(req.getMaVatTu());
            hangHoaList.add(hangHoa);
        }

        if (!CollectionUtils.isEmpty(hangHoaList))
            qlPhieuNhapKhoHangHoaLtRepository.saveAll(hangHoaList);

        return hangHoaList;
    }

    private QlPhieuNhapKhoLtRes buildResponse(QlPhieuNhapKhoLt phieu) throws Exception {
        Set<String> maVatTus = phieu.getHangHoaList().stream().map(QlPhieuNhapKhoHangHoaLt::getMaVatTu).collect(Collectors.toSet());
        Set<QlnvDmVattu> qlnvDmVattus = !CollectionUtils.isEmpty(maVatTus) ? Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus)) : new HashSet<>();

        QlPhieuNhapKhoLtRes response = new QlPhieuNhapKhoLtRes();
        BeanUtils.copyProperties(phieu, response);
        response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(phieu.getTrangThai()));
        response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(phieu.getTrangThai()));
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = phieu.getHangHoaList();

        BigDecimal tongSoLuong = BigDecimal.ZERO;
        BigDecimal tongSoTien = BigDecimal.ZERO;

        for (QlPhieuNhapKhoHangHoaLt hangHoa : hangHoaList) {
            QlPhieuNhapKhoHangHoaLtRes hangHoaRes = new QlPhieuNhapKhoHangHoaLtRes();
            BeanUtils.copyProperties(hangHoa, hangHoaRes);

            qlnvDmVattus.stream().filter(v -> v.getMa().equals(hangHoa.getMaVatTu())).findFirst().ifPresent(t -> {
                hangHoaRes.setTenVatTu(t.getTen());
            });
            response.getHangHoaRes().add(hangHoaRes);

            tongSoLuong = tongSoLuong.add(Optional.ofNullable(hangHoa.getSoLuongThucNhap()).orElse(BigDecimal.ZERO));
            tongSoTien = tongSoTien.add(Optional.ofNullable(hangHoa.getThanhTien()).orElse(BigDecimal.ZERO));
        }

        response.setTongSoLuong(tongSoLuong);
        response.setTongSoTien(tongSoTien);
        response.setTongSoLuongBangChu(MoneyConvert.docSoLuong(tongSoLuong.toString(), null));
        response.setTongSoTienBangChu(MoneyConvert.doctienBangChu(tongSoTien.toString(), null));

        if (phieu.getPhieuKtClId() != null) {
            QlpktclhPhieuKtChatLuong phieuKtChatLuong = phieuKtChatLuongRepository.findById(phieu.getPhieuKtClId())
                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu kiểm tra chất lượng"));

            response.setSoPhieuKtCl(phieuKtChatLuong.getSoPhieu());
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
        response.setChungTus(fileDinhKemService.search(phieu.getId(), Collections.singleton(QlPhieuNhapKhoLt.TABLE_NAME)));
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlPhieuNhapKhoLtRes update(QlPhieuNhapKhoLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optionalQd = qlPhieuNhapKhoLtRepository.findById(req.getId());
        if (!optionalQd.isPresent())
            throw new Exception("Phiếu nhập kho không tồn tại.");

        this.validateSoPhieu(optionalQd.get(), req);

        QlPhieuNhapKhoLt phieu = optionalQd.get();
        BeanUtils.copyProperties(req, phieu, "id");
        phieu.setNgaySua(LocalDate.now());
        phieu.setNguoiSuaId(userInfo.getId());
        qlPhieuNhapKhoLtRepository.save(phieu);

        Map<Long, QlPhieuNhapKhoHangHoaLt> mapHangHoa = qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId())
                .stream().collect(Collectors.toMap(QlPhieuNhapKhoHangHoaLt::getId, Function.identity()));

        List<QlPhieuNhapKhoHangHoaLtReq> hangHoaReqs = req.getHangHoaList();
        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = this.saveListHangHoa(phieu.getId(), hangHoaReqs, mapHangHoa);
        phieu.setHangHoaList(hangHoaList);

        if (!CollectionUtils.isEmpty(mapHangHoa.values()))
            qlPhieuNhapKhoHangHoaLtRepository.deleteAll(mapHangHoa.values());

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getChungTus(), phieu.getId(), QlPhieuNhapKhoLt.TABLE_NAME);
        phieu.setChungTus(fileDinhKems);

        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        if (QdPheDuyetKqlcntVtStatus.DA_DUYET.getId().equals(phieu.getTrangThai())) {
            throw new Exception("Không thể xóa quyết định đã duyệt");
        }

        List<QlPhieuNhapKhoHangHoaLt> hangHoaList = qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId());
        if (!CollectionUtils.isEmpty(hangHoaList)) {
            qlPhieuNhapKhoHangHoaLtRepository.deleteAll(hangHoaList);
        }

        qlPhieuNhapKhoLtRepository.delete(phieu);
        fileDinhKemService.delete(phieu.getId(), Collections.singleton(QlPhieuNhapKhoLt.TABLE_NAME));
        return true;
    }

    @Override
    public QlPhieuNhapKhoLtRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        phieu.setHangHoaList(qlPhieuNhapKhoHangHoaLtRepository.findAllByQlPhieuNhapKhoLtId(phieu.getId()));
        return this.buildResponse(phieu);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlPhieuNhapKhoLt phieu = optional.get();
        return this.updateStatus(req, phieu, userInfo);
    }

    public boolean updateStatus(StatusReq req, QlPhieuNhapKhoLt phieu, UserInfo userInfo) throws Exception {
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

        qlPhieuNhapKhoLtRepository.save(phieu);
        return true;
    }

    @Override
    public Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        return qlPhieuNhapKhoLtRepository.search(req);
    }

    private void thongTinNganLo(QlPhieuNhapKhoLtRes phieu, KtNganLo nganLo) {
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
        QlPhieuNhapKhoLtSearchReq req = new QlPhieuNhapKhoLtSearchReq();
        this.prepareSearchReq(req, userInfo, null, req.getTrangThais());
        BaseNhapHangCount count = new BaseNhapHangCount();

        count.setTatCa(qlPhieuNhapKhoLtRepository.count(req));
        return count;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (CollectionUtils.isEmpty(req.getIds()))
            return false;


        qlPhieuNhapKhoHangHoaLtRepository.deleteByQlPhieuNhapKhoLtIdIn(req.getIds());
        qlPhieuNhapKhoLtRepository.deleteByIdIn(req.getIds());
        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(QlPhieuNhapKhoLt.TABLE_NAME));
        return true;
    }

    @Override
    public boolean exportToExcel(QlPhieuNhapKhoLtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<QlPhieuNhapKhoLtRes> list = this.search(objReq).get().collect(Collectors.toList());

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

            for (QlPhieuNhapKhoLtRes item : list) {
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

    private void validateSoPhieu(QlPhieuNhapKhoLt update, QlPhieuNhapKhoLtReq req) throws Exception {
        String so = req.getSoPhieu();
        if (!StringUtils.hasText(so))
            return;

        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
            Optional<QlPhieuNhapKhoLt> optional = qlPhieuNhapKhoLtRepository.findFirstBySoPhieu(so);
            Long updateId = Optional.ofNullable(update).map(QlPhieuNhapKhoLt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số phiếu " + so + " đã tồn tại");
        }
    }
}
