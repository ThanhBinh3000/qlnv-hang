package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhQdGiaoNvNhapKhacService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.DmVattuDTO;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class HhQdGiaoNvNhapKhacServiceImpl extends BaseServiceImpl implements HhQdGiaoNvNhapKhacService {
    private static final String CAN_CU = "_CAN_CU";
    @Autowired
    private HhQdGiaoNvNhapKhacHdrRepository hhQdGiaoNvNhapKhacHdrRepository;
    @Autowired
    private HhQdPdNhapKhacDtlRepository hhQdPdNhapKhacDtlRepository;
    @Autowired
    private HhQdPdNhapKhacHdrRepository hhQdPdNhapKhacHdrRepository;
    @Autowired
    private BienBanLayMauKhacRepository bienBanLayMauKhacRepository;
    @Autowired
    private PhieuKnghiemCluongHangKhacRepository phieuKnghiemCluongHangKhacRepository;
    @Autowired
    FileDinhKemService fileDinhKemService;

    @Override
    public Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhQdGiaoNvuNhapKhacSearch req) {
        req.setTuNgayQdStr(convertFullDateToString(req.getTuNgayQd()));
        req.setDenNgayQdStr(convertFullDateToString(req.getDenNgayQd()));
//        req.setTuNgayDuyetStr(convertFullDateToString(req.getTuNgayDuyet()));
//        req.setDenNgayDuyetStr(convertFullDateToString(req.getDenNgayDuyet()));
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.search(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
//            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
//            f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
        });
        return data;
    }

    @Override
    public Page<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBb(HhQdGiaoNvuNhapKhacSearch req) {
        req.setTuNgayLmStr(convertFullDateToString(req.getTuNgayLm()));
        req.setDenNgayLmStr(convertFullDateToString(req.getDenNgayLm()));
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Page<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.dsQdNvuDuocLapBb(req, pageable);
        for (HhQdGiaoNvuNhapHangKhacHdr item : data) {
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenLoaiVthh(mapVthh.get(item.getLoaiVthh()));
            List<HhQdPdNhapKhacDtl> dtlList = hhQdPdNhapKhacDtlRepository.findAllByIdHdr(item.getIdQdPdNk());
            dtlList.forEach(dtl -> {
                dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
                dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
                dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
                dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
                dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
                dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()));
                if(dtl.getIdBbLayMau() != null){
                dtl.setBbLayMau(bienBanLayMauKhacRepository.findById(dtl.getIdBbLayMau()).get());
                }
                if(!StringUtils.isEmpty(dtl.getMaLoKho())){
                    dtl.setPKnghiemClHang(phieuKnghiemCluongHangKhacRepository.findByMaLoKho(dtl.getMaLoKho()).isPresent() ? phieuKnghiemCluongHangKhacRepository.findByMaLoKho(dtl.getMaLoKho()).get() : null);
                }
            });
            item.setDtlList(dtlList);
        }
        return data;
    }

    @Override
    @Transactional
    public HhQdGiaoNvuNhapHangKhacHdr themMoi (HhQdGiaoNvuNhapKhacHdrReq req) throws Exception {


        if(!StringUtils.isEmpty(req.getSoQd())){
            List<HhQdGiaoNvuNhapHangKhacHdr> checkSoQd = hhQdGiaoNvNhapKhacHdrRepository.findBySoQd(req.getSoQd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + req.getSoQd() + " đã tồn tại");
            }
        }


        HhQdGiaoNvuNhapHangKhacHdr dataMap = ObjectMapperUtils.map(req, HhQdGiaoNvuNhapHangKhacHdr.class);

        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setNguoiTao(getUser().getUsername());
//        dataMap.setLastest(req.getLastest());
        dataMap.setMaDvi(getUser().getDvql());
        luuFile(req, dataMap);
        hhQdGiaoNvNhapKhacHdrRepository.save(dataMap);
        Optional<HhQdPdNhapKhacHdr> qdPdHdr = hhQdPdNhapKhacHdrRepository.findById(dataMap.getIdQdPdNk());
        qdPdHdr.get().setLastest(true);
        hhQdPdNhapKhacHdrRepository.save(qdPdHdr.get());
        // Update trạng thái tổng hợp dxkhclnt
//        hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(req.getDetails().get(0).getSoDxuat()), Contains.DADUTHAO_QD);

        saveDetail(req,dataMap);
        return dataMap;
    }

    @Transactional
    void saveDetail(HhQdGiaoNvuNhapKhacHdrReq objReq, HhQdGiaoNvuNhapHangKhacHdr dataMap){
        try {
//            hhQdPdNhapKhacDtlRepository.deleteAllByIdHdr(dataMap.getId());
//            for (HhDxuatKhNhapKhacHdrReq dxHdr : objReq.getDetails()){
//                dxHdr.getDetails().forEach(dtl ->{
//                    HhQdPdNhapKhacDtl qd = ObjectMapperUtils.map(dtl, HhQdPdNhapKhacDtl.class);
//                    qd.setId(null);
//                    qd.setIdHdr(dataMap.getId());
//                    qd.setIdDxHdr(dxHdr.getId());
//                    qd.setTrangThai(Contains.CHUACAPNHAT);
//                    qd.setTongSlNhap(dxHdr.getTongSlNhap());
//                    qd.setTongThanhTien(dxHdr.getTongThanhTien());
//                    qd.setCloaiVthh(dxHdr.getCloaiVthh());
//                    hhQdPdNhapKhacDtlRepository.save(qd);
//                });
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public HhQdGiaoNvuNhapHangKhacHdr capNhat (HhQdGiaoNvuNhapKhacHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<HhQdGiaoNvuNhapHangKhacHdr> qOptional = hhQdGiaoNvNhapKhacHdrRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if(!StringUtils.isEmpty(objReq.getSoQd())){
            if (!objReq.getSoQd().equals(qOptional.get().getSoQd())) {
                List<HhQdGiaoNvuNhapHangKhacHdr> checkSoQd = hhQdGiaoNvNhapKhacHdrRepository.findBySoQd(objReq.getSoQd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + objReq.getSoQd() + " đã tồn tại");
                }
            }
        }


        HhQdGiaoNvuNhapHangKhacHdr dataDB = qOptional.get();
        HhQdGiaoNvuNhapHangKhacHdr dataMap = ObjectMapperUtils.map(objReq, HhQdGiaoNvuNhapHangKhacHdr.class);

        updateObjectToObject(dataDB, dataMap);
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSua(getUser().getUsername());
        luuFile(objReq, dataDB);
        hhQdGiaoNvNhapKhacHdrRepository.save(dataDB);
        this.saveDetail(objReq,dataDB);

        return dataDB;
    }

    private void luuFile(HhQdGiaoNvuNhapKhacHdrReq req, HhQdGiaoNvuNhapHangKhacHdr created) {
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + CAN_CU));
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + CAN_CU);
        }
        if (!DataUtils.isNullObject(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME);
        }
    }

    @Override
    public HhQdGiaoNvuNhapHangKhacHdr chiTiet (Long id) throws Exception {
        Optional<HhQdGiaoNvuNhapHangKhacHdr> qOptional = hhQdGiaoNvNhapKhacHdrRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Map<String, DmVattuDTO> mapVthh = getListObjectDanhMucHangHoa(qOptional.get().getMaDvi().substring(0, 4));
        Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenLoaiHinhNx(mapLoaiHinhNx.get(qOptional.get().getLoaiHinhNx()));
        qOptional.get().setTenKieuNx(mapKieuNx.get(qOptional.get().getKieuNx()));
        qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()).getTen());
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setFileCanCu(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + CAN_CU)));
        qOptional.get().setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME)));
        List<HhQdPdNhapKhacDtl> dtls = new ArrayList<>();
        if(qOptional.get().getId() != null){
            dtls = hhQdPdNhapKhacDtlRepository.findAllByIdHdr(qOptional.get().getIdQdPdNk());
        }else{

        }
        dtls.forEach(dtl -> {
            dtl.setTenLoaiHangHoa(mapVthh.get(qOptional.get().getLoaiVthh()).getTen());
            dtl.setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()).getTen());
            dtl.setDvt(mapVthh.get(qOptional.get().getLoaiVthh()).getMaDviTinh());
            dtl.setTenCuc(mapDmucDvi.get(dtl.getMaCuc()));
            dtl.setTenChiCuc(mapDmucDvi.get(dtl.getMaChiCuc()));
            dtl.setTenDiemKho(mapDmucDvi.get(dtl.getMaDiemKho()));
            dtl.setTenNhaKho(mapDmucDvi.get(dtl.getMaNhaKho()));
            dtl.setTenNganKho(mapDmucDvi.get(dtl.getMaNganKho()));
            dtl.setTenLoKho(mapDmucDvi.get(dtl.getMaLoKho()));
            dtl.setTenNganLoKho(mapDmucDvi.get(dtl.getMaLoKho()) + " - " + mapDmucDvi.get(dtl.getMaNganKho()));
            dtl.setTenCloaiVthh(mapVthh.get(dtl.getCloaiVthh()).getTen());
        });
        qOptional.get().setDtlList(dtls);
        return qOptional.get();
    }

    @Override
    public HhQdGiaoNvuNhapHangKhacHdr pheDuyet(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdGiaoNvuNhapHangKhacHdr> qOptional = hhQdGiaoNvNhapKhacHdrRepository.findById(stReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = stReq.getTrangThai() + qOptional.get().getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        qOptional.get().setTrangThai(stReq.getTrangThai());
        return hhQdGiaoNvNhapKhacHdrRepository.save(qOptional.get());
    }

    @Override
    @Transactional
    public void xoa(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhQdGiaoNvuNhapHangKhacHdr> qOptional = hhQdGiaoNvNhapKhacHdrRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)
                && !qOptional.get().getTrangThai().equals(Contains.TU_CHOI_CBV)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_TP)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
        }
//        if(qOptional.get().getIdTh() != null){
//
//        }else{
//            hhDxuatKhNhapKhacHdrRepository.updateStatusInList(Arrays.asList(qOptional.get().getSoDxuat()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
//        }
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + CAN_CU));
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME));
        Optional<HhQdPdNhapKhacHdr> qdPdHdr = hhQdPdNhapKhacHdrRepository.findById(qOptional.get().getIdQdPdNk());
        qdPdHdr.get().setLastest(false);
        hhQdPdNhapKhacHdrRepository.save(qdPdHdr.get());
        hhQdGiaoNvNhapKhacHdrRepository.delete(qOptional.get());
    }

    @Override
    @Transactional
    public void xoaNhieu(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList())) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        List<HhQdGiaoNvuNhapHangKhacHdr> listHdr = hhQdGiaoNvNhapKhacHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (listHdr.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhQdGiaoNvuNhapHangKhacHdr hdr: listHdr) {
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                    && !hdr.getTrangThai().equals(Contains.TU_CHOI_CBV)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + CAN_CU));
            fileDinhKemService.delete(hdr.getId(), Lists.newArrayList(HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME));
            hhQdGiaoNvNhapKhacHdrRepository.delete(hdr);
        }
    }

    @Override
    public void xuatFile(HhQdGiaoNvuNhapKhacSearch req , HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhQdGiaoNvuNhapHangKhacHdr> page = this.timKiem(req);
        List<HhQdGiaoNvuNhapHangKhacHdr> data = page.getContent();

        String title = "Danh sách quyết định giao nhiệm vụ nhập hàng";
        String[] rowsName = new String[]{"STT", "Năm KH", "Số quyết định", "Ngày ký quyết định", "Loại hàng hóa", "Tổng SL nhập", "ĐVT", "Thời gian nhập kho muộn nhất", "Trích yếu", "Số BB nhập đầy kho/kết thúc nhập kho", "Trạng thái QĐ", "Trạng thái nhập hàng"};
        String filename = "Danh_sach_quyet_dinh_giao_nv_nhap_hang.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhQdGiaoNvuNhapHangKhacHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQd();
            objs[3] = dx.getNgayQd();
            objs[4] = dx.getTenLoaiVthh();
            objs[5] = dx.getTongSlNhap();
            objs[6] = dx.getDvt();
            objs[7] = dx.getTgianNkMnhat();
            objs[8] = dx.getTrichYeu();
            objs[9] = "";
            objs[10] = dx.getTenTrangThai();
            objs[11] = dx.getTenTrangThaiNh();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }


    @Override
    public void xuatFileBbLm(HhQdGiaoNvuNhapKhacSearch req, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(userInfo.getDvql());
        Page<HhQdGiaoNvuNhapHangKhacHdr> page = this.dsQdNvuDuocLapBb(req);
        List<HhQdGiaoNvuNhapHangKhacHdr> data = page.getContent();

        String title = "Danh sách biên bản lấy mẫu";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho",
                "Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB nhập đầy kho", "Ngày nhập đầy kho", "Trạng thái"};
        String filename = "Danh_sach_bien_ban_lay_mau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            HhQdGiaoNvuNhapHangKhacHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNam();
            objs[3] = qd.getTgianNkMnhat();
            dataList.add(objs);
            for (int j = 0; j < qd.getDtlList().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDtlList().get(j).getTenDiemKho();
                dataList.add(objsb);
                if (qd.getDtlList().get(j).getBbLayMau() != null) {
                    objsc = new Object[rowsName.length];
                    objsb[5] = qd.getDtlList().get(j).getTenNganLoKho();
                    objsc[6] = qd.getDtlList().get(j).getBbLayMau().getSoBienBan();
                    objsc[7] = qd.getDtlList().get(j).getBbLayMau().getNgayLayMau();
                    objsc[8] = "";
                    objsc[9] = "";
                    objsc[10] = qd.getDtlList().get(j).getTenTrangThai();
                    dataList.add(objsc);
                }
            }
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();

    }

    @Override
    public void xuatFilePkncl(HhQdGiaoNvuNhapKhacSearch req, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(userInfo.getDvql());
        Page<HhQdGiaoNvuNhapHangKhacHdr> page = this.dsQdNvuDuocLapBb(req);
        List<HhQdGiaoNvuNhapHangKhacHdr> data = page.getContent();

        String title = "Danh sách lập và ký phiếu kiểm nghiệm chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu KNCL", "Ngày kiểm nghiệm", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB nhập đầy kho", "Ngày nhập đầy kho", "Trạng thái"};
        String filename = "danh-sach-lap-va-ky-phieu-kiem-nghiem-chat-luong.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            HhQdGiaoNvuNhapHangKhacHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNam();
            objs[3] = qd.getTgianNkMnhat();
            dataList.add(objs);
            for (int j = 0; j < qd.getDtlList().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDtlList().get(j).getTenDiemKho();
                dataList.add(objsb);
                if (qd.getDtlList().get(j).getBbLayMau() != null) {
                    objsc = new Object[rowsName.length];
                    objsb[5] = qd.getDtlList().get(j).getTenNganLoKho();
                    objsb[6] = qd.getDtlList().get(j).getPKnghiemClHang().getSoPhieuKiemNghiemCl();
                    objsb[7] = qd.getDtlList().get(j).getPKnghiemClHang().getNgayKnghiem();
                    objsc[8] = qd.getDtlList().get(j).getBbLayMau().getSoBienBan();
                    objsc[9] = qd.getDtlList().get(j).getBbLayMau().getNgayLayMau();
                    objsc[10] = "";
                    objsc[11] = "";
                    objsc[12] = qd.getDtlList().get(j).getTenTrangThai();
                    dataList.add(objsc);
                }
            }
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();

    }

    @Override
    public ReportTemplateResponse preview(HhQdGiaoNvuNhapKhacSearch objReq) throws Exception {
        HhQdGiaoNvuNhapHangKhacHdr optional = chiTiet(objReq.getId());
//        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        String filePath = "/Users/vunt/Downloads/Print/"+objReq.getReportTemplateRequest().getFileName();
        byte[] byteArray = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }


}
