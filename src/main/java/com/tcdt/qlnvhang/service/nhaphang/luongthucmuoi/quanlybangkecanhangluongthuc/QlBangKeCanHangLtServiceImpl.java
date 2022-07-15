package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeChCtLt;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRepository;
import com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc.QlBangKeChCtLtRepository;
import com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeChCtLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class QlBangKeCanHangLtServiceImpl extends BaseServiceImpl implements QlBangKeCanHangLtService {

    private static final String SHEET_BANG_KE_CAN_HANG_LUONG_THUC = "Bảng kê cân hàng lương thực";
    private static final String STT = "STT";
    private static final String SO_BANG_KE = "Số Bảng Kê";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String SO_PHIEU_NHAP = "Số Phiếu Nhập";
    private static final String NGAY_NHAP_KHO = "Ngày Nhập Kho";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Autowired
    private QlBangKeCanHangLtRepository qlBangKeCanHangLtRepository;

    @Autowired
    private QlBangKeChCtLtRepository qlBangKeChCtLtRepository;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private QlnvDmVattuRepository qlnvDmVattuRepository;

    @Autowired
    private HttpServletRequest req;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlBangKeCanHangLtRes create(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");



        this.validateSoBb(null, req);
        QlBangKeCanHangLt item = new QlBangKeCanHangLt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoBangKe(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BKCH", userInfo.getMaPbb()));
        qlBangKeCanHangLtRepository.save(item);

        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, new HashMap<>());
        item.setChiTiets(chiTiets);

        this.updateThucNhapPnk(item);
        return this.buildResponse(item);
    }

    /*
     * Update số lượng thực nhập vào phiếu nhập kho
     */
    private void updateThucNhapPnk(QlBangKeCanHangLt item) throws Exception {
        Long pnkId = item.getQlPhieuNhapKhoLtId();
        NhPhieuNhapKho phieuNhapKho = null;
        if (pnkId != null) {
            phieuNhapKho = nhPhieuNhapKhoRepository.findById(pnkId)
                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu nhập kho"));
        }

        String maVatTu = item.getMaVatTu();
        if (phieuNhapKho == null || !StringUtils.hasText(maVatTu))
            return;

        NhPhieuNhapKhoCt pnkCt = nhPhieuNhapKhoCtRepository.findFirstByPhieuNkIdAndMaVatTu(phieuNhapKho.getId(), maVatTu);
        if (pnkCt != null) {
            pnkCt.setSoLuongThucNhap(item.getTongTrongLuongBaoBi());
            nhPhieuNhapKhoCtRepository.save(pnkCt);
        }
    }
    private QlBangKeCanHangLtRes buildResponse(QlBangKeCanHangLt item) throws Exception {

        QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
        BeanUtils.copyProperties(item, response);
        response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
        response.setMaDvi(donvi.getMaDvi());
        response.setTenDvi(donvi.getTenDvi());

        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(maVatTus)) {
            Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
            if (CollectionUtils.isEmpty(vatTus))
                throw new Exception("Không tìm thấy vật tư");
            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
                    .ifPresent(v -> response.setTenVatTu(v.getTen()));
            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
                    .ifPresent(v -> response.setTenVatTuCha(v.getTen()));
        }
        List<QlBangKeChCtLt> chiTiets = item.getChiTiets();
        List<QlBangKeChCtLtRes> chiTietResList = new ArrayList<>();
        BigDecimal tongTrongLuongCaBi = BigDecimal.ZERO;
        for (QlBangKeChCtLt chiTiet : chiTiets) {
            QlBangKeChCtLtRes chiTietRes = new QlBangKeChCtLtRes();
            BeanUtils.copyProperties(chiTiet, chiTietRes);
            chiTietResList.add(chiTietRes);
            tongTrongLuongCaBi = tongTrongLuongCaBi.add(Optional.ofNullable(chiTiet.getTrongLuongCaBi()).orElse(BigDecimal.ZERO));
        }
        response.setChiTiets(chiTietResList);

        response.setTongTrongLuongCaBi(tongTrongLuongCaBi);
        BigDecimal tongTrongLuongHangTruBi = tongTrongLuongCaBi.subtract(Optional.ofNullable(item.getTongTrongLuongBaoBi()).orElse(BigDecimal.ZERO));
        response.setTongTrongLuongHangTruBiBangChu(MoneyConvert.docSoLuong(tongTrongLuongHangTruBi.toString(), null));

        if (item.getQlPhieuNhapKhoLtId() != null) {
            NhPhieuNhapKho phieuNhapKho = nhPhieuNhapKhoRepository.findById(item.getQlPhieuNhapKhoLtId())
                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu nhập kho"));

            response.setSoPhieuNhapKho(phieuNhapKho.getSoPhieu());
        }

        if (item.getQdgnvnxId() != null) {
            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
            if (!qdNhap.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
        }

        KtNganLo nganLo = null;
        if (StringUtils.hasText(item.getMaNganLo())) {
            nganLo = ktNganLoRepository.findFirstByMaNganlo(item.getMaNganLo());
        }

        this.thongTinNganLo(response, nganLo);
        return response;
    }

    private List<QlBangKeChCtLt> saveListChiTiet(Long parentId, List<QlBangKeChCtLtReq> chiTietReqs, Map<Long, QlBangKeChCtLt> mapChiTiet) throws Exception {
        List<QlBangKeChCtLt> chiTiets = new ArrayList<>();
        for (QlBangKeChCtLtReq req : chiTietReqs) {
            Long id = req.getId();
            QlBangKeChCtLt chiTiet = new QlBangKeChCtLt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Bảng kê chi tiết hàng Hóa không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setQlBangKeCanHangLtId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            qlBangKeChCtLtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QlBangKeCanHangLtRes update(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null)
            return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        this.validateSoBb(optional.get(), req);
        QlBangKeCanHangLt item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        qlBangKeCanHangLtRepository.save(item);

        Map<Long, QlBangKeChCtLt> map = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId())
                .stream().collect(Collectors.toMap(QlBangKeChCtLt::getId, Function.identity()));

        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, map);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(map.values()))
            qlBangKeChCtLtRepository.deleteAll(map.values());

        this.updateThucNhapPnk(item);
        return this.buildResponse(item);
    }

    @Override
    public QlBangKeCanHangLtRes detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        item.setChiTiets(qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId()));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        QlBangKeCanHangLt item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã ban hành");
        }

        List<QlBangKeChCtLt> chiTiets = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId());
        if (!CollectionUtils.isEmpty(chiTiets)) {
            qlBangKeChCtLtRepository.deleteAll(chiTiets);
        }

        qlBangKeCanHangLtRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Quyết định không tồn tại.");

        QlBangKeCanHangLt bangKe = optional.get();
        return this.updateStatus(req, bangKe, userInfo);
    }

    public boolean updateStatus(StatusReq stReq, QlBangKeCanHangLt bangKe, UserInfo userInfo) throws Exception {
        String trangThai = bangKe.getTrangThai();
        if (TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.DU_THAO_TRINH_DUYET.getId());
            bangKe.setNguoiGuiDuyetId(userInfo.getId());
            bangKe.setNgayGuiDuyet(LocalDate.now());
        } else if (TrangThaiEnum.LANH_DAO_DUYET.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
                return false;
            bangKe.setTrangThai(TrangThaiEnum.LANH_DAO_DUYET.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (TrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.LANH_DAO_DUYET.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.BAN_HANH.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
        } else if (TrangThaiEnum.TU_CHOI.getId().equals(stReq.getTrangThai())) {
            if (!TrangThaiEnum.DU_THAO_TRINH_DUYET.getId().equals(trangThai))
                return false;

            bangKe.setTrangThai(TrangThaiEnum.TU_CHOI.getId());
            bangKe.setNguoiPheDuyetId(userInfo.getId());
            bangKe.setNgayPheDuyet(LocalDate.now());
            bangKe.setLyDoTuChoi(stReq.getLyDo());
        }  else {
            throw new Exception("Bad request.");
        }

        qlBangKeCanHangLtRepository.save(bangKe);
        return true;
    }

    @Override
    public Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = qlBangKeCanHangLtRepository.search(req);
        List<QlBangKeCanHangLtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
            QlBangKeCanHangLt item = (QlBangKeCanHangLt) o[0];
            Long pnkId = (Long) o[1];
            String soPnk = (String) o[2];
            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
            Long qdNhapId = (Long) o[4];
            String soQdNhap = (String) o[5];
            String maVatTu = (String) o[6];
            String tenVatTu = (String) o[7];
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQlPhieuNhapKhoLtId(pnkId);
            response.setSoPhieuNhapKho(soPnk);
            this.thongTinNganLo(response, nganLo);
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setMaVatTu(maVatTu);
            response.setTenVatTu(tenVatTu);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, qlBangKeCanHangLtRepository.count(req));
    }

    private void thongTinNganLo(QlBangKeCanHangLtRes item, KtNganLo nganLo) {
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

    @Override
    public BaseNhapHangCount count() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        QlBangKeCanHangLtSearchReq req = new QlBangKeCanHangLtSearchReq();
        this.prepareSearchReq(req, userInfo, null, req.getTrangThais());
        BaseNhapHangCount count = new BaseNhapHangCount();

        count.setTatCa(qlBangKeCanHangLtRepository.count(req));
        return count;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (CollectionUtils.isEmpty(req.getIds()))
            return false;


        qlBangKeChCtLtRepository.deleteByQlBangKeCanHangLtIdIn(req.getIds());
        qlBangKeCanHangLtRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(QlBangKeCanHangLtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<QlBangKeCanHangLtRes> list = this.search(objReq).get().collect(Collectors.toList());

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
            XSSFSheet sheet = workbook.createSheet(SHEET_BANG_KE_CAN_HANG_LUONG_THUC);
            Row row0 = sheet.createRow(0);
            //STT

            ExportExcel.createCell(row0, 0, STT, style, sheet);
            ExportExcel.createCell(row0, 1, SO_BANG_KE, style, sheet);
            ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
            ExportExcel.createCell(row0, 3, SO_PHIEU_NHAP, style, sheet);
            ExportExcel.createCell(row0, 4, NGAY_NHAP_KHO, style, sheet);
            ExportExcel.createCell(row0, 5, DIEM_KHO, style, sheet);
            ExportExcel.createCell(row0, 6, NHA_KHO, style, sheet);
            ExportExcel.createCell(row0, 7, NGAN_KHO, style, sheet);
            ExportExcel.createCell(row0, 8, NGAN_LO, style, sheet);
            ExportExcel.createCell(row0, 9, TRANG_THAI, style, sheet);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeight(11);
            style.setFont(font);

            Row row;
            int startRowIndex = 1;

            for (QlBangKeCanHangLtRes item : list) {
                row = sheet.createRow(startRowIndex);
                ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
                ExportExcel.createCell(row, 1, item.getSoBangKe(), style, sheet);
                ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
                ExportExcel.createCell(row, 3, item.getSoPhieuNhapKho(), style, sheet);
                ExportExcel.createCell(row, 4, LocalDateTimeUtils.localDateToString(item.getNgayNhap()), style, sheet);
                ExportExcel.createCell(row, 5, item.getTenDiemKho(), style, sheet);
                ExportExcel.createCell(row, 6, item.getTenNhaKho(), style, sheet);
                ExportExcel.createCell(row, 7, item.getTenNganKho(), style, sheet);
                ExportExcel.createCell(row, 8, item.getTenNganLo(), style, sheet);
                ExportExcel.createCell(row, 9, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
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

    private void validateSoBb(QlBangKeCanHangLt update, QlBangKeCanHangLtReq req) throws Exception {
        String so = req.getSoBangKe();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBangKe()) && !update.getSoBangKe().equalsIgnoreCase(so))) {
            Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findFirstBySoBangKe(so);
            Long updateId = Optional.ofNullable(update).map(QlBangKeCanHangLt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số bảng kê " + so + " đã tồn tại");
        }
    }

    @Override
    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = qlBangKeCanHangLtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }
}
