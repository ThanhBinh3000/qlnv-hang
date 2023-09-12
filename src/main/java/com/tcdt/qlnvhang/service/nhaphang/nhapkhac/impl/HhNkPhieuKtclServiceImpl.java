package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtcl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtclCt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclSearch;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkPhieuKtclService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class HhNkPhieuKtclServiceImpl extends BaseServiceImpl implements HhNkPhieuKtclService {
    @Autowired
    private HhNkPhieuKtclRepository hhNkPhieuKtclRepository;
    @Autowired
    private HhNkPhieuKtclCtRepository hhNkPhieuKtclCtRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private HhQdGiaoNvNhapKhacHdrRepository hhQdGiaoNvNhapKhacHdrRepository;
    @Autowired
    private HhQdPdNhapKhacDtlRepository hhQdPdNhapKhacDtlRepository;
    @Autowired
    private HhBbNghiemThuNhapKhacRepository hhBbNghiemThuNhapKhacRepository;
    @Override
    public Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhNkPhieuKtclSearch req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Pageable pageable= PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        if (userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setMaDviChiCuc(userInfo.getDvql());
        } else {
            req.setMaDvi(userInfo.getDvql());
        }
        req.setTuNgayGDStr(convertFullDateToString(req.getTuNgayGD()));
        req.setDenNgayGDStr(convertFullDateToString(req.getDenNgayGD()));
        req.setTuNgayLPStr(convertFullDateToString(req.getTuNgayLP()));
        req.setDenNgayLPStr(convertFullDateToString(req.getDenNgayLP()));
        Page<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.searchPhieuKtcl(req, pageable);
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.forEach( f -> {
            List<HhQdPdNhapKhacDtl> listDiaDiem = hhQdPdNhapKhacDtlRepository.findAllByIdHdr(f.getIdQdPdNk());
            for (HhQdPdNhapKhacDtl dtl: listDiaDiem) {
                dtl.setTenDiemKho(hashMapDmdv.get(dtl.getMaDiemKho()));
                dtl.setTenNganKho(hashMapDmdv.get(dtl.getMaNganKho()));
                dtl.setTenLoKho(hashMapDmdv.get(dtl.getMaLoKho()));
                List<HhBbNghiemThuNhapKhac> bbNghiemThuNhapKhacs = hhBbNghiemThuNhapKhacRepository.findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKho(f.getId(), dtl.getMaLoKho(), dtl.getMaNganKho());
                bbNghiemThuNhapKhacs.forEach( item ->  {
                    HhNkPhieuKtcl phieuKtcl = hhNkPhieuKtclRepository.findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKhoAndSoBbNtBq(f.getId(), dtl.getMaLoKho(), dtl.getMaNganKho(), item.getSoBbNtBq());
                    if (phieuKtcl != null) {
                        phieuKtcl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(phieuKtcl.getTrangThai()));
                        item.setPhieuKtcl(phieuKtcl);
                    }
                });
                dtl.setBbNghiemThuNhapKhacList(bbNghiemThuNhapKhacs);
            }
            f.setDtlList(listDiaDiem);
        });
        return data;
    }

    @Override
    public List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapPhieuKtcl(HhNkPhieuKtclSearch req) throws Exception {
        List<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.dsQdNvuDuocLapPhieuKtcl(req);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
        });
        return data;
    }

    @Override
    public HhNkPhieuKtcl timKiemPhieuKtclTheoMaNganLo(HhNkPhieuKtclSearch req) throws Exception {
        HhNkPhieuKtcl data = hhNkPhieuKtclRepository.findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKhoAndTrangThai(req.getIdQdGiaoNvnh(), req.getMaLoKho(), req.getMaNganKho(), Contains.DADUYET_LDCC);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:mapVthh.get(data.getCloaiVthh()));
        return data;
    }

    @Override
    @Transactional
    public HhNkPhieuKtcl themMoi(HhNkPhieuKtclReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        HhNkPhieuKtcl data = ObjectMapperUtils.map(objReq, HhNkPhieuKtcl.class);
        data.setNguoiTao(userInfo.getUsername());
        data.setTenNguoiTao(userInfo.getFullName());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        HhNkPhieuKtcl created = hhNkPhieuKtclRepository.save(data);
        luuFile(objReq, created);
        luuChiTiet(objReq, created);
        return created;
    }

    @Override
    @Transactional
    public HhNkPhieuKtcl capNhat(HhNkPhieuKtclReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhNkPhieuKtcl> optional = hhNkPhieuKtclRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa.");
        }
        BeanUtils.copyProperties(objReq, optional.get());
        optional.get().setNgaySua(getDateTimeNow());
        optional.get().setNguoiSua(getUser().getUsername());
        luuFile(objReq, optional.get());
        luuChiTiet(objReq, optional.get());
        return hhNkPhieuKtclRepository.save(optional.get());
    }

    @Override
    public HhNkPhieuKtcl chiTiet(Long id) throws Exception {
        Optional<HhNkPhieuKtcl> qOptional = hhNkPhieuKtclRepository.findById(id);
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh())?null:mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        qOptional.get().setTenDiemKho(StringUtils.isEmpty(qOptional.get().getMaDiemKho()) ? null : mapDmucDvi.get(qOptional.get().getMaDiemKho()));
        qOptional.get().setTenNhaKho(StringUtils.isEmpty(qOptional.get().getMaNhaKho()) ? null : mapDmucDvi.get(qOptional.get().getMaNhaKho()));
        qOptional.get().setTenNganKho(StringUtils.isEmpty(qOptional.get().getMaNganKho()) ? null : mapDmucDvi.get(qOptional.get().getMaNganKho()));
        qOptional.get().setTenLoKho(StringUtils.isEmpty(qOptional.get().getMaLoKho()) ? null : mapDmucDvi.get(qOptional.get().getMaLoKho()));
        qOptional.get().setFileDinhKemCtgd(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhNkPhieuKtcl.TABLE_NAME + "_CTGD")));
        qOptional.get().setFileDinhKemKtcl(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhNkPhieuKtcl.TABLE_NAME + "_KTCL")));
        List<HhNkPhieuKtclCt> listCt = hhNkPhieuKtclCtRepository.findAllByPhieuKtChatLuongId(qOptional.get().getId());
        qOptional.get().setListChiTieu(listCt);
        return qOptional.get();
    }

    @Override
    public HhNkPhieuKtcl pheDuyet(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhNkPhieuKtcl> qOptional = hhNkPhieuKtclRepository.findById(stReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        String status = stReq.getTrangThai() + qOptional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                qOptional.get().setNguoiGuiDuyet(getUser().getUsername());
                qOptional.get().setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                qOptional.get().setNguoiPduyet(getUser().getUsername());
                qOptional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        qOptional.get().setTrangThai(stReq.getTrangThai());
        return hhNkPhieuKtclRepository.save(qOptional.get());
    }

    @Override
    @Transactional
    public void xoa(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhNkPhieuKtcl> qOptional = hhNkPhieuKtclRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)) {
            throw new Exception("Chỉ thực hiện xóa với bản ghi ở trạng thái bản nháp hoặc từ chối");
        }
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_CTGD"));
        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_KTCL"));
        hhNkPhieuKtclCtRepository.deleteAllByPhieuKtChatLuongId(qOptional.get().getId());
        hhNkPhieuKtclRepository.delete(qOptional.get());
    }

    @Override
    public void export(HhNkPhieuKtclSearch req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhQdGiaoNvuNhapHangKhacHdr> page = timKiem(req);
        List<HhQdGiaoNvuNhapHangKhacHdr> data = page.getContent();
        String title = "Danh sách phiếu kiểm tra chất lượng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho", "Ngăn lô kho",
                "Số BB NT kê lót, BQLĐ", "Số phiếu KTCL", "Ngày giám định", "Kết quả đánh giá",
                "Số phiếu nhập kho", "Ngày nhập kho", "Trạng thái"};
        String filename = "Ds_phieu_ktcl.xlsx";
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
                objsb[5] = qd.getDtlList().get(j).getTenLoKho() + " - " + qd.getDtlList().get(j).getTenNganKho();
                dataList.add(objsb);
                for (int k = 0; k < qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().size(); k++) {
                    objsc = new Object[rowsName.length];
                    objsc[6] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getSoBbNtBq();
                    if(qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getPhieuKtcl() != null) {
                        objsc[7] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getPhieuKtcl().getSoPhieu();
                        objsc[8] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getPhieuKtcl().getNgayGdinh();
                        objsc[9] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getPhieuKtcl().getKqDanhGia();
                        objsc[12] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getPhieuKtcl().getTenTrangThai();
                    }
                    dataList.add(objsc);
                }
            }
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(HhNkPhieuKtclSearch objReq) throws Exception {
        HhNkPhieuKtcl optional = chiTiet(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        String filePath = "/Users/vunt/Downloads/Print/"+objReq.getReportTemplateRequest().getFileName();
//        byte[] byteArray = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

    private void luuFile(HhNkPhieuKtclReq objReq, HhNkPhieuKtcl created){
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_CTGD"));
        if (!DataUtils.isNullObject(objReq.getFileDinhKemCtgd())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemCtgd(), created.getId(), HhNkPhieuKtcl.TABLE_NAME + "_CTGD");
        }
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_KTCL"));
        if (!DataUtils.isNullObject(objReq.getFileDinhKemKtcl())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemKtcl(), created.getId(), HhNkPhieuKtcl.TABLE_NAME + "_KTCL");
        }
    }

    private void luuChiTiet(HhNkPhieuKtclReq objReq, HhNkPhieuKtcl created){
        hhNkPhieuKtclCtRepository.deleteAllByPhieuKtChatLuongId(created.getId());
        if (objReq.getChiTieu() != null) {
            objReq.getChiTieu().forEach(i -> {
                HhNkPhieuKtclCt dtl = ObjectMapperUtils.map(i, HhNkPhieuKtclCt.class);
                dtl.setPhieuKtChatLuongId(created.getId());
                hhNkPhieuKtclCtRepository.save(dtl);
            });
        }
    }
}
