package com.tcdt.qlnvhang.service.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.entities.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtRepository;
import com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.request.search.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiSearchReq;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.response.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtRes;
import com.tcdt.qlnvhang.response.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiRes;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class NhPhieuNhapKhoTamGuiServiceImpl extends BaseServiceImpl implements NhPhieuNhapKhoTamGuiService {
    private final NhPhieuNhapKhoTamGuiRepository nhPhieuNhapKhoTamGuiRepository;
    private final NhPhieuNhapKhoTamGuiCtRepository phieuNhapKhoTamGuiCtRepository;
    private final FileDinhKemService fileDinhKemService;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final KtNganLoRepository ktNganLoRepository;
    private static final String SHEET_PHIEU_NHAP_KHO_TAM_GUI = "Phiếu nhập kho tạm gửi";
    private static final String STT = "STT";
    private static final String SO_PHIEU = "Số Phiếu";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_NHAP_KHO = "Ngày Nhập Kho";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhPhieuNhapKhoTamGuiRes create(NhPhieuNhapKhoTamGuiReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoPhieu(null, req);

        NhPhieuNhapKhoTamGui item = new NhPhieuNhapKhoTamGui();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setSo(getSo());
        item.setNam(LocalDate.now().getYear());
        item.setSoPhieu(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "PTG", userInfo.getMaPBb()));
        nhPhieuNhapKhoTamGuiRepository.save(item);

        List<NhPhieuNhapKhoTamGuiCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
        item.setFileDinhKems(fileDinhKems);

        return this.buildResponse(item);
    }

    private List<NhPhieuNhapKhoTamGuiCt> saveListChiTiet(Long parentId,
                                                      List<NhPhieuNhapKhoTamGuiCtReq> chiTietReqs,
                                                      Map<Long, NhPhieuNhapKhoTamGuiCt> mapChiTiet) throws Exception {
        List<NhPhieuNhapKhoTamGuiCt> chiTiets = new ArrayList<>();
        for (NhPhieuNhapKhoTamGuiCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhPhieuNhapKhoTamGuiCt chiTiet = new NhPhieuNhapKhoTamGuiCt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Phiếu nhập kho tạm gửi chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setPhieuNkTgId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            phieuNhapKhoTamGuiCtRepository.saveAll(chiTiets);

        return chiTiets;
    }


    private NhPhieuNhapKhoTamGuiRes buildResponse(NhPhieuNhapKhoTamGui item) throws Exception {
        NhPhieuNhapKhoTamGuiRes res = new NhPhieuNhapKhoTamGuiRes();
        List<NhPhieuNhapKhoTamGuiCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhPhieuNhapKhoTamGuiCt phieuNhapKhoTamGuiCt : item.getChiTiets()) {
            chiTiets.add(new NhPhieuNhapKhoTamGuiCtRes(phieuNhapKhoTamGuiCt));
        }
        res.setChiTiets(chiTiets);

        if (item.getQdgnvnxId() != null) {
            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
            if (!qdNhap.isPresent()) {
                throw new Exception("Không tìm thấy quyết định nhập");
            }
            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
        }
        res.setFileDinhKems(item.getFileDinhKems());
        KtNganLo nganLo = null;
        if (StringUtils.hasText(item.getMaNganLo())) {
            nganLo = ktNganLoRepository.findFirstByMaNganlo(item.getMaNganLo());
        }

        this.thongTinNganLo(res, nganLo);
        return res;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public NhPhieuNhapKhoTamGuiRes update(NhPhieuNhapKhoTamGuiReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");

        this.validateSoPhieu(optional.get(), req);

        NhPhieuNhapKhoTamGui item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        nhPhieuNhapKhoTamGuiRepository.save(item);
        Map<Long, NhPhieuNhapKhoTamGuiCt> mapChiTiet = phieuNhapKhoTamGuiCtRepository.findByPhieuNkTgIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhPhieuNhapKhoTamGuiCt::getId, Function.identity()));

        List<NhPhieuNhapKhoTamGuiCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);
        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            phieuNhapKhoTamGuiCtRepository.deleteAll(mapChiTiet.values());

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
        item.setFileDinhKems(fileDinhKems);
        return this.buildResponse(item);
    }

    @Override
    public NhPhieuNhapKhoTamGuiRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");

        NhPhieuNhapKhoTamGui item = optional.get();
        item.setChiTiets(phieuNhapKhoTamGuiCtRepository.findByPhieuNkTgIdIn(Collections.singleton(item.getId())));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhPhieuNhapKhoTamGui.TABLE_NAME)));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");

        NhPhieuNhapKhoTamGui item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã ban hành");
        }
        phieuNhapKhoTamGuiCtRepository.deleteByPhieuNkTgIdIn(Collections.singleton(item.getId()));
        nhPhieuNhapKhoTamGuiRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");

        NhPhieuNhapKhoTamGui phieu = optional.get();

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

        nhPhieuNhapKhoTamGuiRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhPhieuNhapKhoTamGuiRes> search(NhPhieuNhapKhoTamGuiSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = nhPhieuNhapKhoTamGuiRepository.search(req);
        List<NhPhieuNhapKhoTamGuiRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhPhieuNhapKhoTamGuiRes response = new NhPhieuNhapKhoTamGuiRes();
            NhPhieuNhapKhoTamGui item = (NhPhieuNhapKhoTamGui) o[0];
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

        return new PageImpl<>(responses, pageable, nhPhieuNhapKhoTamGuiRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        phieuNhapKhoTamGuiCtRepository.deleteByPhieuNkTgIdIn(req.getIds());
        nhPhieuNhapKhoTamGuiRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(NhPhieuNhapKhoTamGuiSearchReq objReq, HttpServletResponse response) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhPhieuNhapKhoTamGuiRes> list = this.search(objReq).get().collect(Collectors.toList());

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
            XSSFSheet sheet = workbook.createSheet(SHEET_PHIEU_NHAP_KHO_TAM_GUI);
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

            for (NhPhieuNhapKhoTamGuiRes item : list) {
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

    private void thongTinNganLo(NhPhieuNhapKhoTamGuiRes item, KtNganLo nganLo) {
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

    private void validateSoPhieu(NhPhieuNhapKhoTamGui update, NhPhieuNhapKhoTamGuiReq req) throws Exception {
        String so = req.getSoPhieu();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
            Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findFirstBySoPhieu(so);
            Long updateId = Optional.ofNullable(update).map(NhPhieuNhapKhoTamGui::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số phiếu " + so + " đã tồn tại");
        }
    }

    @Override
    public Integer getSo() throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Integer so = nhPhieuNhapKhoTamGuiRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
        so = Optional.ofNullable(so).orElse(0);
        so = so + 1;
        return so;
    }
}
