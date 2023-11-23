package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbannghiemthubaoquan;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinKeLot;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstDtlRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Log4j2
public class HhBbNghiemthuKlstHdrServiceImpl extends BaseServiceImpl implements HhBbNghiemthuKlstHdrService {

    @Autowired
    private HhBbNghiemthuKlstRepository hhBbNghiemthuKlstRepository;
    @Autowired
    private HhBbNghiemthuKlstDtlRepository hhBbNghiemthuKlstDtlRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;
    @Autowired
    private HhQdGiaoNvuNhapxuatService hhQdGiaoNvuNhapxuatService;

    @Autowired
    private KtNganLoRepository ktNganLoRepository;
    @Autowired
    private KtNganKhoRepository ktNganKhoRepository;
    @Autowired
    private HhHopDongRepository hhHopDongRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private HttpServletRequest req;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Override
    public Page<HhBbNghiemthuKlstHdr> searchPage(HhBbNghiemthuKlstHdrReq req) {
        return null;
    }

    @Override
    public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(req.getIdQdGiaoNvNh());
        if (!qdNxOptional.isPresent())
            throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
//        Map<String, String> lishLoaiKho = getListDanhMucChung("LOAI_KHO");
        // Add danh sach file dinh kem o Master
        HhBbNghiemthuKlstHdr dataMap = new HhBbNghiemthuKlstHdr();
        BeanUtils.copyProperties(req, dataMap);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setNam(qdNxOptional.get().getNamNhap());
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setCapDvi(userInfo.getCapDvi());

        dataMap.setNam(LocalDate.now().getYear());
        dataMap.setId(Long.parseLong(req.getSoBbNtBq().split("/")[0]));

//        if(!StringUtils.isEmpty(req.getMaLoKho())){
//            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(req.getMaLoKho());
//            BigDecimal tichLuongTkLt = nganLo.getTichLuongTkLt();
//            BigDecimal tichLuongKdLt = nganLo.getTichLuongKdLt();
//            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
//            dataMap.setTichLuong(tichLuong.doubleValue());
//            dataMap.setLhKho(lishLoaiKho.get(nganLo.getLoaikhoId()));
//        }
//        if(!StringUtils.isEmpty(req.getMaNganKho())){
//            KtNganKho ktNganKho = ktNganKhoRepository.findByMaNgankho(req.getMaNganKho());
//            BigDecimal tichLuongTkLt = ktNganKho.getTichLuongTkLt();
//            BigDecimal tichLuongKdLt = ktNganKho.getTichLuongKdLt();
//            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
//            dataMap.setLhKho(lishLoaiKho.get(ktNganKho.getLoaikhoId()));
//            dataMap.setTichLuong(tichLuong.doubleValue());
//        }


        hhBbNghiemthuKlstRepository.save(dataMap);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), HhBbNghiemthuKlstHdr.TABLE_NAME);
        }
        saveDetail(req, dataMap.getId());
        return dataMap;
    }

    @Transactional
    void saveDetail(HhBbNghiemthuKlstHdrReq req, Long id){
        hhBbNghiemthuKlstDtlRepository.deleteAllByHdrId(id);
        for (HhBbNghiemthuKlstDtlReq hhBbNghiemthuKlstDtlReq : req.getDetail()) {
            HhBbNghiemthuKlstDtl ct = new HhBbNghiemthuKlstDtl();
            BeanUtils.copyProperties(hhBbNghiemthuKlstDtlReq, ct,"id");
            ct.setId(null);
            ct.setHdrId(id);
            hhBbNghiemthuKlstDtlRepository.save(ct);
        }
    }

    @Override
    public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent()){
			throw new Exception("Không tìm thấy dữ liệu cần sửa");
		}


        Optional<NhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getIdQdGiaoNvNh());
        if (!qdNxOptional.isPresent()){
			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
		}
        Map<String, String> lishLoaiKho = getListDanhMucChung("LOAI_KHO");

        HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
        BeanUtils.copyProperties(objReq,dataDTB,"id");
        if(!StringUtils.isEmpty(dataDTB.getMaLoKho())){
            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(dataDTB.getMaLoKho());
            BigDecimal tichLuongTkLt = nganLo.getTichLuongTkLt();
            BigDecimal tichLuongKdLt = nganLo.getTichLuongKdLt();
            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
            dataDTB.setTichLuong(tichLuong.doubleValue());
            dataDTB.setLhKho(lishLoaiKho.get(nganLo.getLoaikhoId()));
        }
        if(StringUtils.isEmpty(dataDTB.getMaLoKho()) && !StringUtils.isEmpty(dataDTB.getMaNganKho())){
            KtNganKho ktNganKho = ktNganKhoRepository.findByMaNgankho(dataDTB.getMaNganKho());
            BigDecimal tichLuongTkLt = ktNganKho.getTichLuongTkLt();
            BigDecimal tichLuongKdLt = ktNganKho.getTichLuongKdLt();
            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
            dataDTB.setLhKho(lishLoaiKho.get(ktNganKho.getLoaikhoId()));
            dataDTB.setTichLuong(tichLuong.doubleValue());
        }
        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSuaId(getUser().getId());;
        dataDTB.setNam(qdNxOptional.get().getNamNhap());
        dataDTB.setMaDvi(userInfo.getDvql());
        dataDTB.setCapDvi(userInfo.getCapDvi());
        hhBbNghiemthuKlstRepository.save(dataDTB);
        fileDinhKemService.delete(dataDTB.getId(), Lists.newArrayList(HhBbNghiemthuKlstHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(objReq.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataDTB.getId(), HhBbNghiemthuKlstHdr.TABLE_NAME);
        }
        saveDetail(objReq, dataDTB.getId());
		return dataDTB;
    }

    @Override
    public HhBbNghiemthuKlstHdr detail(Long id) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();

        if (ObjectUtils.isEmpty(userInfo)) {
            throw new Exception("401 Author");
        }

        if (ObjectUtils.isEmpty(id)) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(id);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        HhBbNghiemthuKlstHdr hhBbNghiemthuKlstHdr = qOptional.get();
//        Map<String, String> lishLoaiKho = getListDanhMucChung("LOAI_KHO");
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        hhBbNghiemthuKlstHdr.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(hhBbNghiemthuKlstHdr.getTrangThai()));
        hhBbNghiemthuKlstHdr.setTenDvi(listDanhMucDvi.get(hhBbNghiemthuKlstHdr.getMaDvi()));
        hhBbNghiemthuKlstHdr.setTenNguoiTao(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getNguoiTaoId()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getNguoiTaoId()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenKeToan(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getIdKeToan()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getIdKeToan()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenThuKho(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getIdThuKho()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getIdThuKho()).get().getFullName());
        hhBbNghiemthuKlstHdr.setTenNguoiPduyet(ObjectUtils.isEmpty(hhBbNghiemthuKlstHdr.getNguoiPduyetId()) ? null : userInfoRepository.findById(hhBbNghiemthuKlstHdr.getNguoiPduyetId()).get().getFullName());
//        if(!StringUtils.isEmpty(hhBbNghiemthuKlstHdr.getMaLoKho())){
//            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(hhBbNghiemthuKlstHdr.getMaLoKho());
//            BigDecimal tichLuongTkLt = nganLo.getTichLuongTkLt();
//            BigDecimal tichLuongKdLt = nganLo.getTichLuongKdLt();
//            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
//            hhBbNghiemthuKlstHdr.setTichLuong(tichLuong.doubleValue());
//            hhBbNghiemthuKlstHdr.setLhKho(lishLoaiKho.get(nganLo.getLoaikhoId()));
//        }
//        if(!StringUtils.isEmpty(hhBbNghiemthuKlstHdr.getMaNganKho())){
//            KtNganKho ktNganKho = ktNganKhoRepository.findByMaNgankho(hhBbNghiemthuKlstHdr.getMaNganKho());
//            BigDecimal tichLuongTkLt = ktNganKho.getTichLuongTkLt();
//            BigDecimal tichLuongKdLt = ktNganKho.getTichLuongKdLt();
//            BigDecimal tichLuong = tichLuongTkLt.subtract(tichLuongKdLt).max(BigDecimal.ZERO);
//            hhBbNghiemthuKlstHdr.setLhKho(lishLoaiKho.get(ktNganKho.getLoaikhoId()));
//            hhBbNghiemthuKlstHdr.setTichLuong(tichLuong.doubleValue());
//        }
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhHopDongHdr.TABLE_NAME));
        hhBbNghiemthuKlstHdr.setListFileDinhKem(fileDinhKem);
        hhBbNghiemthuKlstHdr.setChildren(hhBbNghiemthuKlstDtlRepository.findAllByHdrId(hhBbNghiemthuKlstHdr.getId()));
        return hhBbNghiemthuKlstHdr;
    }

    @Override
    public Object getDataKho(String maDvi) throws Exception {
        try {
            if (!StringUtils.isEmpty(maDvi)) {
                Map<String, String> listLoaiKho = getListDanhMucChung("LOAI_KHO");
                if (maDvi.length() == 14) { //ma kho
                    KtNganKho ktNganKho = ktNganKhoRepository.findByMaNgankho(maDvi);
                    ktNganKho.setLhKho(listLoaiKho.get(ktNganKho.getLoaikhoId()));
                    return ktNganKho;
                } else {
                    KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(maDvi);
                    nganLo.setLhKho(listLoaiKho.get(nganLo.getLoaikhoId()));
                    return nganLo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HhBbNghiemthuKlstHdr approve(HhBbNghiemthuKlstHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        HhBbNghiemthuKlstHdr phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId()).equals(status)
        ) {
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId()).equals(status)
        ) {
            phieu.setIdThuKho(userInfo.getId());
            phieu.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId()).equals(status)
        ) {
            phieu.setIdKeToan(userInfo.getId());
            phieu.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(status)
        ) {
            phieu.setNgayPduyet(new Date());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setLyDoTuChoi(req.getLyDoTuChoi());
        }
        else {
            throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        hhBbNghiemthuKlstRepository.save(phieu);
        return phieu;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp");
        }
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhBbNghiemthuKlstHdr.TABLE_NAME));
        hhBbNghiemthuKlstRepository.delete(qOptional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        for (Long aLong : listMulti) {
            delete(aLong);
        }
    }

    @Override
    public void export(HhBbNghiemthuKlstHdrReq req, HttpServletResponse response) throws Exception {
    }

    @Override
    public void exportBbNtBq(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        searchReq.setMaDvi(userInfo.getDvql());
        Page<NhQdGiaoNvuNhapxuatHdr> page = hhQdGiaoNvuNhapxuatService.searchPage(searchReq);
        List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

        String title = "Danh sách lập biên bản nghiệm thu bản quản lần đầu";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho", "Lô kho",
                "Số BB NT kê lót, BQLĐ", "Ngày lập biên bản", "Ngày kết thúc NT kê lót, BQLĐ", "Tổng kinh phí thực tế (đ)", "Tổng kinh phí TC PD (đ)", "Trạng thái"};
        String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_nhap_xuat.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNamNhap();
            objs[3] = qd.getTgianNkho();
            dataList.add(objs);
            for (int j = 0; j < qd.getDtlList().get(0).getChildren().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDtlList().get(0).getChildren().get(j).getTenDiemKho();
                objsb[5] = qd.getDtlList().get(0).getChildren().get(j).getTenLoKho();
                dataList.add(objsb);
                for (int k = 0; k < qd.getDtlList().get(0).getListBienBanNghiemThuBq().size(); k++) {
                    if (checkValidateExportBbNtBq(qd.getDtlList().get(0).getChildren().get(j), qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k))) {
                        objsc = new Object[rowsName.length];
                        objsc[6] = qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k).getSoBbNtBq();
                        objsc[7] = qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k).getNgayTao();
                        objsc[8] = qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k).getNgayNghiemThu();
                        objsc[11] = qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k).getTenTrangThai();
                        dataList.add(objsc);
                    }
                }
            }
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public void exportPktCl(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        searchReq.setMaDvi(userInfo.getDvql());
        Page<NhQdGiaoNvuNhapxuatHdr> page = hhQdGiaoNvuNhapxuatService.searchPage(searchReq);
        List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

        String title = "Danh sách phiếu kiểm tra chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm KH", "Thời hạn NH", "Điểm kho", "Lô kho",
                "BB NTBQ lần đầu", "Số phiếu KTCL", "Ngày giám định", "Kết quả đánh giá", "Số phiếu nhập kho", "Ngày nhâ kho", "Trạng thái"};
        String filename = "Danh_sach_phieu_kiem_tra_chat_luong.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNamNhap();
            objs[3] = qd.getTgianNkho();
            dataList.add(objs);
            for (int j = 0; j < qd.getDtlList().get(0).getChildren().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDtlList().get(0).getChildren().get(j).getTenDiemKho();
                objsb[5] = qd.getDtlList().get(0).getChildren().get(j).getTenLoKho();
                dataList.add(objsb);
                for (int k = 0; k < qd.getDtlList().get(0).getListBienBanNghiemThuBq().size(); k++) {
                    if (checkValidateExportBbNtBq(qd.getDtlList().get(0).getChildren().get(j), qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k))) {
                        objsc = new Object[rowsName.length];
                        objsc[6] = qd.getDtlList().get(0).getListBienBanNghiemThuBq().get(k).getSoBbNtBq();
                        dataList.add(objsc);
                        for (int t = 0; t < qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().size(); t++){
                            objsc = new Object[rowsName.length];
                            objsc[7] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getSoPhieu();
                            objsc[8] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getNgayGdinh();
                            objsc[9] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getKqDanhGia();
//                            if(!StringUtils.isEmpty(qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getPhieuNhapKho())){
//                                objsc[10] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getPhieuNhapKho().getSoPhieuNhapKho();
//                                objsc[11] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getPhieuNhapKho().getNgayNhapKho();
//                            }
                            objsc[12] = qd.getDtlList().get(0).getChildren().get(j).getListPhieuKtraCl().get(t).getTenTrangThai();
                            dataList.add(objsc);
                        }
                    }
                }
            }
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    private boolean checkValidateExportBbNtBq(NhQdGiaoNvuNxDdiem dataDdiem, HhBbNghiemthuKlstHdr dataBb) {
        if (dataDdiem.getMaLoKho() != null && dataBb.getMaLoKho() != null) {
            if (dataDdiem.getMaLoKho().equals(dataBb.getMaLoKho())) {
                return true;
            }
        } else if (dataDdiem.getMaNganKho() != null && dataBb.getMaNganKho() != null) {
            if (dataDdiem.getMaNganKho().equals(dataBb.getMaNganKho())) {
                return true;
            }
        } else if (dataDdiem.getMaNhaKho() != null && dataBb.getMaNhaKho() != null) {
            if (dataDdiem.getMaNhaKho().equals(dataBb.getMaNhaKho())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ReportTemplateResponse preview(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        HhBbNghiemThuKlstPreview object = new HhBbNghiemThuKlstPreview();
        HhBbNghiemthuKlstHdr bbNghiemthuKlstHdr = this.detail(objReq.getId());
        BeanUtils.copyProperties(bbNghiemthuKlstHdr, object);
        object.setNgayTaoFull(convertDate(bbNghiemthuKlstHdr.getNgayTao()));
        String[] parts = Objects.requireNonNull(convertDate(bbNghiemthuKlstHdr.getNgayTao())).split("/");
        object.setNgayTao(parts[0]);
        object.setThangTao(parts[1]);
        object.setNamTao(parts[2]);
        Double tongKp = 0D;
        for (HhBbNghiemthuKlstDtl child : bbNghiemthuKlstHdr.getChildren()) {
            if(child.getIsParent() != null && child.getIsParent() && child.getTongGiaTri() != null && child.getType() != null && child.getType().equals("TH")) {
                tongKp += child.getTongGiaTri();
            }
        }
        object.setTongGiaTri(new BigDecimal(tongKp));
        object.setTongGiaTriBc(NumberToWord.convert(object.getTongGiaTri().longValue()));
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

//	@Override
//	public HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
//			throw new Exception("Loại vật tư hàng hóa không phù hợp");
//		this.validateSoBb(null, objReq);
//
//		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
//		if (!qdNxOptional.isPresent())
//			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
//
//		// Add danh sach file dinh kem o Master
//		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
//		if (objReq.getFileDinhKems() != null) {
//			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
//			fileDinhKemList.forEach(f -> {
//				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
//				f.setCreateDate(new Date());
//			});
//		}
//
//		HhBbNghiemthuKlstHdr dataMap = new ModelMapper().map(objReq, HhBbNghiemthuKlstHdr.class);
//		dataMap.setNgayTao(getDateTimeNow());
//		dataMap.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//		dataMap.setNguoiTao(getUser().getUsername());
//		dataMap.setChildren1(fileDinhKemList);
//
//		// Add thong tin chung
//		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
//		dataMap.setChildren(dtls1);
//
//		dataMap.setNam(qdNxOptional.get().getNamNhap());
//		dataMap.setMaDvi(userInfo.getDvql());
//		dataMap.setCapDvi(userInfo.getCapDvi());
//
//		dataMap.setSo(getSo());
//		dataMap.setNam(LocalDate.now().getYear());
//		dataMap.setSoBb(String.format("%s/%s/%s-%s", dataMap.getSo(), dataMap.getNam(), "BBNTBQ", userInfo.getMaPbb()));
//
//		hhBbNghiemthuKlstRepository.save(dataMap);
//
//		return this.buildResponse(dataMap);
//
//	}
//
//	private void validateSoBb(HhBbNghiemthuKlstHdr update, HhBbNghiemthuKlstHdrReq req) throws Exception {
//		String soBB = req.getSoBb();
//		if (!StringUtils.hasText(soBB))
//			return;
//
//		if (update == null || (StringUtils.hasText(update.getSoBb()) && !update.getSoBb().equalsIgnoreCase(soBB))) {
//			Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findFirstBySoBb(soBB);
//			Long updateId = Optional.ofNullable(update).map(HhBbNghiemthuKlstHdr::getId).orElse(null);
//			if (optional.isPresent() && !optional.get().getId().equals(updateId))
//				throw new Exception("Số biên bản " + soBB + " đã tồn tại");
//		}
//	}
//
//	@Override
//	public HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(objReq.getId()))
//			throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//
//		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(objReq.getId()));
//		if (!qOptional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu cần sửa");
//
//		if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
//			throw new Exception("Loại vật tư hàng hóa không phù hợp");
//
//		this.validateSoBb(qOptional.get(), objReq);
//
//		Optional<HhQdGiaoNvuNhapxuatHdr> qdNxOptional = hhQdGiaoNvuNhapxuatRepository.findById(objReq.getQdgnvnxId());
//		if (!qdNxOptional.isPresent())
//			throw new Exception("Quyết định giao nhiệm vụ nhập xuất không tồn tại");
//
//		// Add danh sach file dinh kem o Master
//		List<FileDKemJoinKeLot> fileDinhKemList = new ArrayList<FileDKemJoinKeLot>();
//		if (objReq.getFileDinhKems() != null) {
//			fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinKeLot.class);
//			fileDinhKemList.forEach(f -> {
//				f.setDataType(HhBbNghiemthuKlstHdr.TABLE_NAME);
//				f.setCreateDate(new Date());
//			});
//		}
//
//		HhBbNghiemthuKlstHdr dataDTB = qOptional.get();
//		HhBbNghiemthuKlstHdr dataMap = ObjectMapperUtils.map(objReq, HhBbNghiemthuKlstHdr.class);
//		dataMap.setSo(dataDTB.getSo());
//		dataMap.setNam(dataDTB.getNam());
//
//		updateObjectToObject(dataDTB, dataMap);
//
//		dataDTB.setNgaySua(getDateTimeNow());
//		dataDTB.setNguoiSua(getUser().getUsername());
//		dataDTB.setChildren1(fileDinhKemList);
//
//		// Add thong tin chung
//		List<HhBbNghiemthuKlstDtl> dtls1 = ObjectMapperUtils.mapAll(objReq.getDetail(), HhBbNghiemthuKlstDtl.class);
//		dataDTB.setChildren(dtls1);
//
//		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);
//		dataDTB.setNam(qdNxOptional.get().getNamNhap());
//		dataDTB.setMaDvi(userInfo.getDvql());
//		dataDTB.setCapDvi(userInfo.getCapDvi());
//		hhBbNghiemthuKlstRepository.save(dataDTB);
//
//		return this.buildResponse(dataDTB);
//
//	}
//
//	@Override
//	public HhBbNghiemthuKlstHdr detail(String ids) throws Exception {
//
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//
//		if (StringUtils.isEmpty(ids))
//			throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//		Optional<HhBbNghiemthuKlstHdr> qOptional = hhBbNghiemthuKlstRepository.findById(Long.parseLong(ids));
//
//		if (!qOptional.isPresent())
//			throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//		// Quy doi don vi kg = tan
//		List<HhBbNghiemthuKlstDtl> dtls = ObjectMapperUtils.mapAll(qOptional.get().getChildren(),
//				HhBbNghiemthuKlstDtl.class);
//		UnitScaler.formatList(dtls, Contains.DVT_TAN);
//
//		return this.buildResponse(qOptional.get());
//	}
//
//	@Override
//	public Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req)
//			throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		int page = objReq.getPaggingReq().getPage();
//		int limit = objReq.getPaggingReq().getLimit();
//		Pageable pageable = PageRequest.of(page, limit);
//		this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//
//		Page<HhBbNghiemthuKlstHdr> qhKho = hhBbNghiemthuKlstRepository
//				.findAll(HhBbNghiemthuKlstSpecification.buildSearchQuery(objReq), pageable);
//
//		List<HhBbNghiemthuKlstHdr> data = qhKho.getContent();
//		if (CollectionUtils.isEmpty(data))
//			return qhKho;
//
//		// Quyet dinh giao nhiem vu nhap hang
//		Set<Long> qdNhapIds = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getQdgnvnxId)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//		Map<Long, HhQdGiaoNvuNhapxuatHdr> mapQdNhap = new HashMap<>();
//		if (!CollectionUtils.isEmpty(qdNhapIds)) {
//			mapQdNhap = hhQdGiaoNvuNhapxuatRepository.findByIdIn(qdNhapIds)
//					.stream().collect(Collectors.toMap(HhQdGiaoNvuNhapxuatHdr::getId, Function.identity()));
//		}
//
//		// Ngan lo
//		Set<String> maNganLos = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getMaNganLo)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//		Map<String, KtNganLo> mapNganLo = new HashMap<>();
//		if (!CollectionUtils.isEmpty(maNganLos)) {
//			mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
//					.stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
//		}
//
//		Set<Long> hopDongIds = data.stream()
//				.map(HhBbNghiemthuKlstHdr::getHopDongId)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//
//		Map<Long, HhHopDongHdr> mapHopDong = new HashMap<>();
//		if (!CollectionUtils.isEmpty(hopDongIds)) {
//			mapHopDong = hhHopDongRepository.findByIdIn(hopDongIds)
//					.stream().collect(Collectors.toMap(HhHopDongHdr::getId, Function.identity()));
//		}
//
//		for (HhBbNghiemthuKlstHdr hdr : data) {
//			HhQdGiaoNvuNhapxuatHdr qdNhap = hdr.getQdgnvnxId() != null ? mapQdNhap.get(hdr.getQdgnvnxId()) : null;
//			KtNganLo nganLo = StringUtils.hasText(hdr.getMaNganLo()) ? mapNganLo.get(hdr.getMaNganLo()) : null;
//			HhHopDongHdr hopDong = hdr.getHopDongId() != null ? mapHopDong.get(hdr.getHopDongId()) : null;
//			this.buildResponseForList(hdr, qdNhap, nganLo, hopDong);
//		}
//
//		return qhKho;
//	}
//
//	@Override
//	public boolean approve(StatusReq stReq) throws Exception {
//
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(stReq.getId()))
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(Long.valueOf(stReq.getId()));
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu");
//
//		HhBbNghiemthuKlstHdr bb = optional.get();
//
//		String trangThai = bb.getTrangThai();
//		if (NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//
//		} else if (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(trangThai))
//				return false;
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//			bb.setNguoiGuiDuyet(userInfo.getUsername());
//			bb.setNgayGuiDuyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_TK.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TK.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//			if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//				return false;
//
//			bb.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//			bb.setNguoiPduyet(userInfo.getUsername());
//			bb.setNgayPduyet(getDateTimeNow());
//			bb.setLdoTuchoi(stReq.getLyDo());
//		} else {
//			throw new Exception("Bad request.");
//		}
//
//		hhBbNghiemthuKlstRepository.save(bb);
//		return true;
//	}
//
//	@Override
//	public void delete(IdSearchReq idSearchReq) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (StringUtils.isEmpty(idSearchReq.getId()))
//			throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
//		Optional<HhBbNghiemthuKlstHdr> optional = hhBbNghiemthuKlstRepository.findById(idSearchReq.getId());
//
//		if (!optional.isPresent())
//			throw new Exception("Không tìm thấy dữ liệu cần xoá");
//
//		if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
//				&& !optional.get().getTrangThai().equals(Contains.TU_CHOI))
//			throw new Exception("Chỉ thực hiện xóa với biên bản ở trạng thái bản nháp hoặc từ chối");
//
//		hhBbNghiemthuKlstRepository.delete(optional.get());
//	}
//
//	@Override
//	public boolean exportToExcel(HhBbNghiemthuKlstSearchReq objReq, HttpServletResponse response) throws Exception {
//		objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//		List<HhBbNghiemthuKlstHdr> list = this.colection(objReq, null).get().collect(Collectors.toList());
//
//		if (CollectionUtils.isEmpty(list))
//			return true;
//
//		String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_NGHIEM_THU, DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO,
//				CHI_PHI_THUC_HIEN_TRONG_NAM, CHI_PHI_THUC_HIEN_NAM_TRUOC, TONG_GIA_TRI, TRANG_THAI};
//		String filename = "Danh_sach_bien_ban_nghiem_thu_bao_quan_lan_dau.xlsx";
//
//		List<Object[]> dataList = new ArrayList<Object[]>();
//		Object[] objs = null;
//
//		try {
//			for (int i = 0; i < list.size(); i++) {
//				HhBbNghiemthuKlstHdr item = list.get(i);
//				objs = new Object[rowsName.length];
//				objs[0] = i;
//				objs[1] = item.getSoBb();
//				objs[2] = item.getSoQuyetDinhNhap();
//				objs[3] = convertDateToString(item.getNgayNghiemThu());
//				objs[4] = item.getTenDiemKho();
//				objs[5] = item.getTenNhaKho();
//				objs[6] = item.getTenNganKho();
//				objs[7] = item.getTenNganLo();
//				objs[8] = Optional.ofNullable(item.getChiPhiThucHienTrongNam()).orElse(BigDecimal.valueOf(0D));
//				objs[9] = Optional.ofNullable(item.getChiPhiThucHienNamTruoc()).orElse(BigDecimal.valueOf(0D));
//				objs[10] = item.getTongGiaTri();
//				objs[11] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//				dataList.add(objs);
//			}
//
//			ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_NGHIEM_THU_BAO_QUAN_LAN_DAU_NHAP, filename, rowsName, dataList, response);
//			ex.export();
//		} catch (Exception e) {
//			log.error("Error export", e);
//			return false;
//		}
//		return true;
//	}
//
//	private void buildResponseForList(HhBbNghiemthuKlstHdr bb, HhQdGiaoNvuNhapxuatHdr qdNhap,
//									  KtNganLo ktNganLo, HhHopDongHdr hopDong) {
//		this.baseResponse(bb);
//		if (qdNhap != null) {
//			bb.setSoQuyetDinhNhap(qdNhap.getSoQd());
//		}
//		this.thongTinNganLo(bb, ktNganLo);
//		if (hopDong != null) {
//			bb.setSoHopDong(hopDong.getSoHd());
//		}
//
//	}
//
//	private HhBbNghiemthuKlstHdr buildResponse(HhBbNghiemthuKlstHdr bb) throws Exception {
//
//		this.baseResponse(bb);
//		if (bb.getQdgnvnxId() != null) {
//			Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(bb.getQdgnvnxId());
//			if (!qdNhap.isPresent()) {
//				throw new Exception("Không tìm thấy quyết định nhập");
//			}
//			bb.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//		}
//
//		if (bb.getHopDongId() != null) {
//			Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(bb.getHopDongId());
//			if (!hopDong.isPresent()) {
//				throw new Exception("Không tìm thấy hợp đồng");
//			}
//			bb.setSoHopDong(hopDong.get().getSoHd());
//		}
//
//		QlnvDmDonvi donvi = getDviByMa(bb.getMaDvi(), req);
//		bb.setMaDvi(donvi.getMaDvi());
//		bb.setTenDvi(donvi.getTenDvi());
//		bb.setMaQhns(donvi.getMaQhns());
//
//		if (!StringUtils.hasText(bb.getMaNganLo()))
//			return bb;
//
//		KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(bb.getMaNganLo());
//		this.thongTinNganLo(bb, nganLo);
//		return bb;
//	}
//
//	private void baseResponse(HhBbNghiemthuKlstHdr bb) {
//		bb.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(bb.getTrangThai()));
//		bb.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(bb.getTrangThai()));
//		BigDecimal chiPhiTn = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getThanhTienTn)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal chiPhiNt = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getThanhTienQt)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		BigDecimal tongGiaTri = bb.getChildren().stream()
//				.map(HhBbNghiemthuKlstDtl::getTongGtri)
//				.filter(Objects::nonNull)
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//
//		bb.setChiPhiThucHienTrongNam(chiPhiTn);
//		bb.setChiPhiThucHienNamTruoc(chiPhiNt);
//		bb.setTongGiaTri(tongGiaTri);
//		bb.setTongGiaTriBangChu(MoneyConvert.doctienBangChu(tongGiaTri.toString(), null));
//	}
//
//	private void thongTinNganLo(HhBbNghiemthuKlstHdr bb, KtNganLo nganLo) {
//		if (nganLo != null) {
//			bb.setTenNganLo(nganLo.getTenNganlo());
//			KtNganKho nganKho = nganLo.getParent();
//			if (nganKho == null)
//				return;
//
//			bb.setTenNganKho(nganKho.getTenNgankho());
//			bb.setMaNganKho(nganKho.getMaNgankho());
//			KtNhaKho nhaKho = nganKho.getParent();
//			if (nhaKho == null)
//				return;
//
//			bb.setTenNhaKho(nhaKho.getTenNhakho());
//			bb.setMaNhaKho(nhaKho.getMaNhakho());
//			KtDiemKho diemKho = nhaKho.getParent();
//			if (diemKho == null)
//				return;
//
//			bb.setTenDiemKho(diemKho.getTenDiemkho());
//			bb.setMaDiemKho(diemKho.getMaDiemkho());
//		}
//	}
//
//	@Transactional
//	@Override
//	public boolean deleteMultiple(DeleteReq req) throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//
//		if (CollectionUtils.isEmpty(req.getIds()))
//			return false;
//		hhBbNghiemthuKlstRepository.deleteByIdIn(req.getIds());
//		return true;
//	}
//
//	@Override
//	public Integer getSo() throws Exception {
//		UserInfo userInfo = UserUtils.getUserInfo();
//		Integer so = hhBbNghiemthuKlstRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//		so = Optional.ofNullable(so).orElse(0);
//		so = so + 1;
//		return so;
//	}
//
//	@Override
//	public BaseNhapHangCount count(Set<String> maDvis) {
//		HhBbNghiemthuKlstSearchReq countReq = new HhBbNghiemthuKlstSearchReq();
//		countReq.setMaDvis(maDvis);
//		BaseNhapHangCount count = new BaseNhapHangCount();
//
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_THOC);
//		count.setThoc((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_GAO);
//		count.setGao((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		countReq.setLoaiVthh(Contains.LOAI_VTHH_MUOI);
//		count.setMuoi((int) hhBbNghiemthuKlstRepository.count(HhBbNghiemthuKlstSpecification.buildSearchQuery(countReq)));
//		return count;
//	}
}
