package com.tcdt.qlnvhang.service.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbtinhkho.XhBienBanTinhKhoReq;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoCtRes;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoRes;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class XhBienBanTinhKhoServiceImpl implements XhBienBanTinhKhoService {

    private final String MA_DS = "/BBTK-CCDTVP";
    @Autowired
    XhBienBanTinhKhoRepository xhBienBanTinhKhoRepository;

    @Autowired
    XhBienBanTinhKhoCtRepository xhBienBanTinhKhoCtRepository;

    @Autowired
    XhQdGiaoNvuXuatRepository xhQdGiaoNvuXuatRepository;

    @Autowired
    KtDiemKhoRepository ktDiemKho;

    @Autowired
    KtNhaKhoRepository ktNhaKho;

    @Autowired
    KtNganKhoRepository ktNganKho;

    @Autowired
    KtNganLoRepository ktNganLo;

    @Autowired
    QlnvDmVattuRepository qlnvDmVattuRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhBienBanTinhKhoRes create(XhBienBanTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanTinhKho item = new XhBienBanTinhKho();
        item.setQdgnvnxId(req.getQdId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(req.getCapDvi());
        Long count = xhBienBanTinhKhoRepository.getMaxId();
        if (count == null) count = 1L;
        item.setSoBienBan(count.intValue() + 1 + "/" + LocalDate.now().getYear() + MA_DS);
        item.setSoLuongXuat(req.getSoLuongXuat());
        item.setSlConlaiXuatcuoi(req.getSoLuongThucTeConLai());
        item.setMaDiemkho(req.getMaDiemkho());
        item.setMaNhakho(req.getMaNhakho());
        item.setMaNgankho(req.getMaNgankho());
        item.setMaLokho(req.getMaNganlo());
        item.setMaLoaiHangHoa(req.getLoaiHangHoa());
        item.setMaChungLoaiHangHoa(req.getChungLoaiHangHoa());
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());

        double slNhap = 0;
        double slSoSach = 0;
        double slThuaConLaiXuatCuoi = 0;
        double slThieuConLaiXuatCuoi = 0;
        //cần lấy tỉ lệ hao dôi từ biên bản hao dôi

        //tính số lượng
        Object[] data = xhBienBanTinhKhoRepository.getHangTrongKho(item.getMaLokho(), item.getMaChungLoaiHangHoa()).get(0);
        if (data != null || data.length > 0) {
            slNhap = ((BigDecimal) data[10]).doubleValue();
            slSoSach = ((BigDecimal) data[3]).doubleValue();
            if (slSoSach > item.getSlConlaiXuatcuoi()) {
                slThuaConLaiXuatCuoi = slSoSach - item.getSlConlaiXuatcuoi();
                slThieuConLaiXuatCuoi = 0;
            } else {
                slThuaConLaiXuatCuoi = 0;
                slThieuConLaiXuatCuoi = item.getSlConlaiXuatcuoi() - slSoSach;
            }
        }
        item.setSoLuongNhap(slNhap);
        item.setSlConlaiSosach(slSoSach);
        item.setSlThuaConlai(slThuaConLaiXuatCuoi);
        item.setSlThieuConlai(slThieuConLaiXuatCuoi);

        xhBienBanTinhKhoRepository.save(item);
        List<XhBienBanTinhKhoCt> ds = req.getDs().stream().map(q -> {
            XhBienBanTinhKhoCt xhBienBanTinhKhoCt = new XhBienBanTinhKhoCt();
            BeanUtils.copyProperties(q, xhBienBanTinhKhoCt, "id");
            return xhBienBanTinhKhoCt;
        }).collect(Collectors.toList());
        xhBienBanTinhKhoCtRepository.saveAll(ds);

        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public XhBienBanTinhKhoRes update(XhBienBanTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanTinhKho item = xhBienBanTinhKhoRepository.findById(req.getId()).get();
        item.setQdgnvnxId(req.getQdId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(req.getCapDvi());
        item.setSoLuongXuat(req.getSoLuongXuat());
        item.setSlConlaiXuatcuoi(req.getSoLuongThucTeConLai());
        item.setMaDiemkho(req.getMaDiemkho());
        item.setMaNhakho(req.getMaNhakho());
        item.setMaNgankho(req.getMaNgankho());
        item.setMaLokho(req.getMaNganlo());
        item.setMaLoaiHangHoa(req.getLoaiHangHoa());
        item.setMaChungLoaiHangHoa(req.getChungLoaiHangHoa());
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());

        //tinh lai so luong
        Object[] data = xhBienBanTinhKhoRepository.getHangTrongKho(item.getMaLokho(), item.getMaChungLoaiHangHoa()).get(0);
        if (data != null || data.length > 0) {
            item.setSoLuongNhap(((BigDecimal) data[10]).doubleValue());
            item.setSlConlaiSosach(((BigDecimal) data[3]).doubleValue());
            if (item.getSlConlaiSosach() > item.getSlConlaiXuatcuoi()) {
                item.setSlThuaConlai(item.getSlConlaiSosach() - item.getSlConlaiXuatcuoi());
                item.setSlThieuConlai(0);
            } else {
                item.setSlThuaConlai(0);
                item.setSlThieuConlai(item.getSlConlaiXuatcuoi() - item.getSlConlaiSosach());
            }
        }

        xhBienBanTinhKhoRepository.save(item);
        List<XhBienBanTinhKhoCt> ds = req.getDs().stream().map(q -> {
            XhBienBanTinhKhoCt xhBienBanTinhKhoCt = new XhBienBanTinhKhoCt();
            BeanUtils.copyProperties(q, xhBienBanTinhKhoCt, "id");
            return xhBienBanTinhKhoCt;
        }).collect(Collectors.toList());
        xhBienBanTinhKhoCtRepository.saveAll(ds);

        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public XhBienBanTinhKhoRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        List<XhBienBanTinhKhoCtRes> ds = xhBienBanTinhKhoCtRepository.findAllByBbTinhKhoId(item.getId()).stream().map(p -> this.buildResponseCt(p)).collect(Collectors.toList());
        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds);
        return result;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Biên bản tịnh kho đã đã duyệt");
        }
        xhBienBanTinhKhoCtRepository.deleteAllByBbTinhKhoId(item.getId());
        xhBienBanTinhKhoRepository.delete(item);
        return true;
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        xhBienBanTinhKhoCtRepository.deleteByBbTinhKhoIdIn(req.getIds());
        xhBienBanTinhKhoRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        String trangThai = item.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else {
            throw new Exception("Bad request.");
        }

        return true;
    }

    @Override
    public Page<XhBienBanTinhKhoRes> search(XhBienBanTinhKhoSearchReq req) throws Exception {
        return null;
    }

    @Override
    public boolean exportToExcel(XhBienBanTinhKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        return false;
    }

    private XhBienBanTinhKhoRes buildResponse(XhBienBanTinhKho xhBienBanTinhKho) {
        XhBienBanTinhKhoRes xhBienBanTinhKhoRes = new XhBienBanTinhKhoRes();
        xhBienBanTinhKhoRes.setId(xhBienBanTinhKho.getId());
        xhBienBanTinhKhoRes.setSoQd(xhQdGiaoNvuXuatRepository.getOne(xhBienBanTinhKho.getQdgnvnxId()).getSoQuyetDinh());
        xhBienBanTinhKhoRes.setLoaiHH(qlnvDmVattuRepository.findByMa(xhBienBanTinhKho.getMaLoaiHangHoa()).getTen());
        xhBienBanTinhKhoRes.setChungLoaiHH(qlnvDmVattuRepository.findByMa(xhBienBanTinhKho.getMaChungLoaiHangHoa()).getTen());
        xhBienBanTinhKhoRes.setTenDvi(xhBienBanTinhKho.getCapDvi());
        xhBienBanTinhKhoRes.setMaDvi(xhBienBanTinhKho.getMaDvi());
        xhBienBanTinhKhoRes.setDiemKho(ktDiemKho.findByMaDiemkhoIn(Collections.singleton(xhBienBanTinhKho.getMaDiemkho())).get(0).getTenDiemkho());
        xhBienBanTinhKhoRes.setDiemKho(ktNhaKho.findByMaNhakhoIn(Collections.singleton(xhBienBanTinhKho.getMaNhakho())).get(0).getTenNhakho());
        xhBienBanTinhKhoRes.setDiemKho(ktNganKho.findByMaNgankhoIn(Collections.singleton(xhBienBanTinhKho.getMaNgankho())).get(0).getTenNgankho());
        xhBienBanTinhKhoRes.setDiemKho(ktNganLo.findByMaNganloIn(Collections.singleton(xhBienBanTinhKho.getMaLokho())).get(0).getTenNganlo());
        xhBienBanTinhKhoRes.setSoBienBan(xhBienBanTinhKho.getSoBienBan());
        xhBienBanTinhKhoRes.setSoLuongNhap(xhBienBanTinhKho.getSoLuongNhap());
        xhBienBanTinhKhoRes.setSoLuongXuat(xhBienBanTinhKho.getSoLuongXuat());
        xhBienBanTinhKhoRes.setSlConlaiSosach(xhBienBanTinhKho.getSlConlaiSosach());
        xhBienBanTinhKhoRes.setSlConlaiXuatcuoi(xhBienBanTinhKho.getSlConlaiXuatcuoi());
        xhBienBanTinhKhoRes.setSlThuaConlai(xhBienBanTinhKho.getSlThuaConlai());
        xhBienBanTinhKhoRes.setSlThieuConlai(xhBienBanTinhKho.getSlThieuConlai());
        xhBienBanTinhKhoRes.setNguyenNhan(xhBienBanTinhKho.getNguyenNhan());
        xhBienBanTinhKhoRes.setKienNghi(xhBienBanTinhKho.getKienNghi());
        xhBienBanTinhKhoRes.setNgayLapPhieu(xhBienBanTinhKho.getNgayTao());
        xhBienBanTinhKhoRes.setTrangThai(xhBienBanTinhKho.getTrangThai());
        xhBienBanTinhKhoRes.setTenTrangThai(TrangThaiEnum.getTenById(xhBienBanTinhKhoRes.getTrangThai()));
        xhBienBanTinhKhoRes.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(xhBienBanTinhKhoRes.getTrangThai()));
        return xhBienBanTinhKhoRes;
    }

    private XhBienBanTinhKhoCtRes buildResponseCt(XhBienBanTinhKhoCt xhBienBanTinhKhoct) {
        XhBienBanTinhKhoCtRes xhBienBanTinhKhoCtRes = new XhBienBanTinhKhoCtRes();
        BeanUtils.copyProperties(xhBienBanTinhKhoct, xhBienBanTinhKhoCtRes);
        return xhBienBanTinhKhoCtRes;
    }
}
