package com.tcdt.qlnvhang.service.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import com.tcdt.qlnvhang.entities.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtRepository;
import com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtReq;
import com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtRes;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
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
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BhThongBaoBdgKtServiceImpl extends BaseServiceImpl implements BhThongBaoBdgKtService {
    private final BhThongBaoBdgKtRepository bhThongBaoBdgKtRepository;
    private final BhQdPheDuyetKhBdgThongTinTaiSanRepository bhQdPheDuyetKhBdgThongTinTaiSanRepository;
    private final FileDinhKemService fileDinhKemService;

    private static final String SHEET_THONG_BAO_BAN_DAU_GIA_KHONG_THANH = "Thông báo bán đấu giá không thành";
    private static final String STT = "STT";
    private static final String SO_THONG_BAO_BDG_KHONG_THANH = "Số Thông Báo BĐG Không Thành";
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
    public BhThongBaoBdgKtRes create(BhThongBaoBdgKtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.validateMaThongBao(null, req);

        BhThongBaoBdgKt item = new BhThongBaoBdgKt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setCapDvi(userInfo.getCapDvi());
        item.setNam(LocalDate.now().getYear());
        bhThongBaoBdgKtRepository.save(item);

        List<BhQdPheDuyetKhBdgThongTinTaiSan> cts = this.saveCts(item.getId(), item.getThongBaoBdgId());
        item.setCts(cts);

        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), BhThongBaoBdgKt.TABLE_NAME));
        return this.buildResponse(item);
    }

    private List<BhQdPheDuyetKhBdgThongTinTaiSan> saveCts(Long parentId, Long thongBaoBdgId) {

        List<BhQdPheDuyetKhBdgThongTinTaiSan> phanLoTaiSans = bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgIdIn(Collections.singleton(thongBaoBdgId));
        if (CollectionUtils.isEmpty(phanLoTaiSans))
            return new ArrayList<>();

        phanLoTaiSans.forEach(p -> {
            p.setThongBaoBdgKtId(parentId);
        });

        if (!CollectionUtils.isEmpty(phanLoTaiSans))
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.saveAll(phanLoTaiSans);

        return phanLoTaiSans;
    }


    private BhThongBaoBdgKtRes buildResponse(BhThongBaoBdgKt item) throws Exception {
        BhThongBaoBdgKtRes res = new BhThongBaoBdgKtRes();
        BeanUtils.copyProperties(item, res);
        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        res.setCts(item.getCts().stream().map(BhQdPheDuyetKhBdgThongTinTaiSanResponse::new).collect(Collectors.toList()));

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        res.setTenVatTuCha(mapVthh.get(item.getMaVatTuCha()));
        this.setThongTinDonVi(res, item.getMaDvi());
        return res;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BhThongBaoBdgKtRes update(BhThongBaoBdgKtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<BhThongBaoBdgKt> optional = bhThongBaoBdgKtRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Thông báo bán đấu giá không thành không tồn tại.");

        this.validateMaThongBao(optional.get(), req);

        BhThongBaoBdgKt item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        bhThongBaoBdgKtRepository.save(item);

        // Bien ban phan lo tai san
        List<BhQdPheDuyetKhBdgThongTinTaiSan> cts = this.saveCts(item.getId(), item.getThongBaoBdgId());
        item.setCts(cts);
        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), BhThongBaoBdgKt.TABLE_NAME));
        return this.buildResponse(item);
    }

    @Override
    public BhThongBaoBdgKtRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhThongBaoBdgKt> optional = bhThongBaoBdgKtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Thông báo bán đấu giá không thành không tồn tại.");

        BhThongBaoBdgKt item = optional.get();
        item.setCts(bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgKtIdIn(Collections.singleton(item.getId())));
        return this.buildResponse(item);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhThongBaoBdgKt> optional = bhThongBaoBdgKtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Thông báo bán đấu giá không thành không tồn tại.");

        BhThongBaoBdgKt item = optional.get();
        if (NhapXuatHangTrangThaiEnum.BAN_HANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Thông báo bán đấu giá không thành đã ban hành");
        }

        fileDinhKemService.delete(item.getId(), Collections.singleton(BhThongBaoBdgKt.TABLE_NAME));
        bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgKtIdIn(Collections.singleton(item.getId())).forEach(p -> {
                    p.setThongBaoBdgKtId(null);
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.save(p);
        });
        bhThongBaoBdgKtRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatusQd(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<BhThongBaoBdgKt> optional = bhThongBaoBdgKtRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Thông báo bán đấu giá không thành không tồn tại.");

        BhThongBaoBdgKt phieu = optional.get();

        String trangThai = phieu.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(LocalDate.now());
        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            phieu.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setNgayPduyet(LocalDate.now());
            phieu.setLyDoTuChoi(stReq.getLyDo());
        }  else {
            throw new Exception("Bad request.");
        }

        bhThongBaoBdgKtRepository.save(phieu);
        return true;
    }

    @Override
    public Page<BhThongBaoBdgKtRes> search(BhThongBaoBdgKtSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<Object[]> data = bhThongBaoBdgKtRepository.search(req, pageable);
        List<BhThongBaoBdgKtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            BhThongBaoBdgKtRes response = new BhThongBaoBdgKtRes();
            BhThongBaoBdgKt item = (BhThongBaoBdgKt) o[0];
            Long thongBaoBdgId = o[1] != null ? (Long) o[1] : null;
            String maThongBaoBdg = o[2] != null ? (String) o[2] : null;
            String hinhThucDauGia = o[3] != null ? (String) o[3] : null;
            String phuongThucDauGia = o[4] != null ? (String) o[4] : null;
            String maVatTuCha = o[5] != null ? (String) o[5] : null;
            String tenVatTuCha = o[6] != null ? (String) o[6] : null;
            Long qdPdKhBdgId = o[7] != null ? (Long) o[7] : null;
            String soQdPdKhBdg = o[8] != null ? (String) o[8] : null;
            Long qdPdKqBdgId = o[9] != null ? (Long) o[9] : null;
            String soQdPdKqBdg = o[10] != null ? (String) o[10] : null;

            BeanUtils.copyProperties(item, response);

            response.setThongBaoBdgId(thongBaoBdgId);
            response.setMaThongBaoBdg(maThongBaoBdg);
            response.setHinhThucDauGia(hinhThucDauGia);
            response.setPhuongThucDauGia(phuongThucDauGia);

            response.setMaVatTuCha(maVatTuCha);
            response.setTenVatTuCha(tenVatTuCha);

            response.setQdPdKhBdgId(qdPdKhBdgId);
            response.setSoQdPdKhBdg(soQdPdKhBdg);

            response.setQdPdKqBdgId(qdPdKqBdgId);
            response.setSoQdPdKqBdg(soQdPdKqBdg);
            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, bhThongBaoBdgKtRepository.count(req));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(BhThongBaoBdgKt.TABLE_NAME));
        bhQdPheDuyetKhBdgThongTinTaiSanRepository.findByThongBaoBdgKtIdIn(req.getIds()).forEach(p -> {
            p.setThongBaoBdgKtId(null);
            bhQdPheDuyetKhBdgThongTinTaiSanRepository.save(p);
        });
        bhThongBaoBdgKtRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    public boolean exportToExcel(BhThongBaoBdgKtSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<BhThongBaoBdgKtRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[] { STT, SO_THONG_BAO_BDG_KHONG_THANH, NGAY_TO_CHUC_BDG,
                TRICH_YEU, SO_QD_PHE_DUYET_KH_BDG, MA_THONG_BAO_BDG, HINH_THUC_DAU_GIA, PHUONG_THUC_DAU_GIA,
                LOAI_HANG_HOA, NAM_KE_HOACH, SO_QD_PHE_DUYET_KQ_BDG, TRANG_THAI};
        String filename = "Danh_sach_thong_bao_ban_dau_gia_khong_thanh.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                BhThongBaoBdgKtRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getMaThongBao();
                objs[2] = LocalDateTimeUtils.localDateToString(item.getNgayToChuc());
                objs[3] = item.getTrichYeu();
                objs[4] = item.getSoQdPdKhBdg();
                objs[5] = item.getMaThongBaoBdg();
                objs[6] = item.getHinhThucDauGia();
                objs[7] = item.getPhuongThucDauGia();
                objs[8] = item.getTenVatTuCha();
                objs[9] = item.getNam();
                objs[10] = item.getSoQdPdKqBdg();
                objs[11] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_THONG_BAO_BAN_DAU_GIA_KHONG_THANH, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    private void validateMaThongBao(BhThongBaoBdgKt update, BhThongBaoBdgKtReq req) throws Exception {
        String maThongBao = req.getMaThongBao();
        if (!StringUtils.hasText(maThongBao))
            return;
        if (update == null || (StringUtils.hasText(update.getMaThongBao()) && !update.getMaThongBao().equalsIgnoreCase(maThongBao))) {
            Optional<BhThongBaoBdgKt> optional = bhThongBaoBdgKtRepository.findFirstByMaThongBao(maThongBao);
            Long updateId = Optional.ofNullable(update).map(BhThongBaoBdgKt::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Mã thông báo " + maThongBao + " đã tồn tại");
        }
    }
}
