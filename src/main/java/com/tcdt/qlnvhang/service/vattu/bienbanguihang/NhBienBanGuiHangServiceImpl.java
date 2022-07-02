package com.tcdt.qlnvhang.service.vattu.bienbanguihang;

import com.tcdt.qlnvhang.entities.vattu.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.vattu.bienbanguihang.NhBienBanGuiHangCt;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanguihang.NhBienBanGuiHangCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbanguihang.NhBienBanGuiHangRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;
import com.tcdt.qlnvhang.response.vattu.bienbanguihang.NhBienBanGuiHangCtRes;
import com.tcdt.qlnvhang.response.vattu.bienbanguihang.NhBienBanGuiHangRes;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhHopDongHdr;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NhBienBanGuiHangServiceImpl extends BaseServiceImpl implements NhBienBanGuiHangService {
    private final NhBienBanGuiHangRepository bienBanGuiHangRepository;
    private final NhBienBanGuiHangCtRepository bienBanGuiHangCtRepository;
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    private final HhHopDongRepository hhHopDongRepository;

    private static final String SHEET_BIEN_BAN_GUI_HANG = "Biên bản gửi hàng";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NAM_NHAP = "Năm Nhập";
    private static final String NGAY_GUI = "Ngày Gửi";
    private static final String BEN_NHAN = "Bên Nhận";
    private static final String BEN_GIAO = "Bên Giao";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    public NhBienBanGuiHangRes create(NhBienBanGuiHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateSoBb(null, req);

        NhBienBanGuiHang item = new NhBienBanGuiHang();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        bienBanGuiHangRepository.save(item);

        List<NhBienBanGuiHangCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
        item.setChiTiets(chiTiets);

        return this.buildResponse(item);
    }

    private NhBienBanGuiHangRes buildResponse(NhBienBanGuiHang item) throws Exception {
        NhBienBanGuiHangRes res = new NhBienBanGuiHangRes();
        List<NhBienBanGuiHangCtRes> chiTiets = new ArrayList<>();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        for (NhBienBanGuiHangCt bienBanGuiHangCt : item.getChiTiets()) {
            chiTiets.add(new NhBienBanGuiHangCtRes(bienBanGuiHangCt));
        }
        res.setChiTiets(chiTiets);

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
        return res;
    }
    
    private List<NhBienBanGuiHangCt> saveListChiTiet(Long parentId,
                                                         List<NhBienBanGuiHangCtReq> chiTietReqs,
                                                         Map<Long, NhBienBanGuiHangCt> mapChiTiet) throws Exception {
        List<NhBienBanGuiHangCt> chiTiets = new ArrayList<>();
        for (NhBienBanGuiHangCtReq req : chiTietReqs) {
            Long id = req.getId();
            NhBienBanGuiHangCt chiTiet = new NhBienBanGuiHangCt();

            if (id != null) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Biên bản gửi hàng chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBienBanGuiHangId(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            bienBanGuiHangCtRepository.saveAll(chiTiets);

        return chiTiets;
    }
    
    @Override
    public NhBienBanGuiHangRes update(NhBienBanGuiHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        this.validateSoBb(optional.get(), req);

        NhBienBanGuiHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        bienBanGuiHangRepository.save(item);
        Map<Long, NhBienBanGuiHangCt> mapChiTiet = bienBanGuiHangCtRepository.findByBienBanGuiHangIdIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(NhBienBanGuiHangCt::getId, Function.identity()));

        List<NhBienBanGuiHangCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
        item.setChiTiets(chiTiets);

        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            bienBanGuiHangCtRepository.deleteAll(mapChiTiet.values());
        return this.buildResponse(item);
    }

    @Override
    public NhBienBanGuiHangRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        NhBienBanGuiHang item = optional.get();
        item.setChiTiets(bienBanGuiHangCtRepository.findByBienBanGuiHangIdIn(Collections.singleton(item.getId())));
        return this.buildResponse(item);
    }

    @Override
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        NhBienBanGuiHang item = optional.get();
        if (TrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã ban hành");
        }
        bienBanGuiHangCtRepository.deleteByBienBanGuiHangIdIn(Collections.singleton(item.getId()));
        bienBanGuiHangRepository.delete(item);
        return true;
    }

    @Override
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        NhBienBanGuiHang phieu = optional.get();

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

        bienBanGuiHangRepository.save(phieu);
        return true;
    }

    @Override
    public Page<NhBienBanGuiHangRes> search(NhBienBanGuiHangSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvi(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = bienBanGuiHangRepository.search(req);
        List<NhBienBanGuiHangRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhBienBanGuiHangRes response = new NhBienBanGuiHangRes();
            NhBienBanGuiHang item = (NhBienBanGuiHang) o[0];
            Long qdNhapId = (Long) o[1];
            String soQdNhap = (String) o[2];
            BeanUtils.copyProperties(item, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            response.setNamNhap(Optional.ofNullable(item.getThoiGian()).map(LocalDateTime::getYear).orElse(LocalDateTime.now().getYear()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, bienBanGuiHangRepository.count(req));
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        bienBanGuiHangCtRepository.deleteByBienBanGuiHangIdIn(req.getIds());
        bienBanGuiHangRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(NhBienBanGuiHangSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvi(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<NhBienBanGuiHangRes> list = this.search(objReq).get().collect(Collectors.toList());

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
            XSSFSheet sheet = workbook.createSheet(SHEET_BIEN_BAN_GUI_HANG);
            Row row0 = sheet.createRow(0);
            //STT

            ExportExcel.createCell(row0, 0, STT, style, sheet);
            ExportExcel.createCell(row0, 1, SO_BIEN_BAN, style, sheet);
            ExportExcel.createCell(row0, 2, SO_QUYET_DINH_NHAP, style, sheet);
            ExportExcel.createCell(row0, 3, NAM_NHAP, style, sheet);
            ExportExcel.createCell(row0, 4, NGAY_GUI, style, sheet);
            ExportExcel.createCell(row0, 5, BEN_NHAN, style, sheet);
            ExportExcel.createCell(row0, 6, BEN_GIAO, style, sheet);
            ExportExcel.createCell(row0, 7, TRANG_THAI, style, sheet);

            style = workbook.createCellStyle();
            font = workbook.createFont();
            font.setFontHeight(11);
            style.setFont(font);

            Row row;
            int startRowIndex = 1;

            for (NhBienBanGuiHangRes item : list) {
                row = sheet.createRow(startRowIndex);
                ExportExcel.createCell(row, 0, startRowIndex, style, sheet);
                ExportExcel.createCell(row, 1, item.getSoBienBan(), style, sheet);
                ExportExcel.createCell(row, 2, item.getSoQuyetDinhNhap(), style, sheet);
                ExportExcel.createCell(row, 3, Optional.ofNullable(item.getThoiGian()).map(LocalDateTime::getYear).orElse(LocalDate.now().getYear()), style, sheet);
                ExportExcel.createCell(row, 4, LocalDateTimeUtils.localDateToString(item.getNgayGui()), style, sheet);
                ExportExcel.createCell(row, 5, item.getBenNhan(), style, sheet);
                ExportExcel.createCell(row, 6, item.getBenGiao(), style, sheet);
                ExportExcel.createCell(row, 7, TrangThaiEnum.getTenById(item.getTrangThai()), style, sheet);
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

    private void validateSoBb(NhBienBanGuiHang update, NhBienBanGuiHangReq req) throws Exception {
        String so = req.getSoBienBan();
        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
            Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findFirstBySoBienBan(so);
            Long updateId = Optional.ofNullable(update).map(NhBienBanGuiHang::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số biên bản " + so + " đã tồn tại");
        }
    }
}
