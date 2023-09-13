package com.tcdt.qlnvhang.service.khoahoccongnghebaoquan;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuuRepository;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.KhCnNghiemThuThanhLyRepository;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.KhCnTienDoThucHienRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnCongTrinhNghienCuu;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnNghiemThuThanhLy;
import com.tcdt.qlnvhang.table.khoahoccongnghebaoquan.KhCnTienDoThucHien;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMau;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KhCnCongTrinhNghienCuuService extends BaseServiceImpl {


    @Autowired
    private KhCnCongTrinhNghienCuuRepository khCnCongTrinhNghienCuuRepository;

    @Autowired
    private KhCnTienDoThucHienRepository khCnTienDoThucHienRepository;

    @Autowired
    private KhCnNghiemThuThanhLyRepository khCnNghiemThuThanhLyRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<KhCnCongTrinhNghienCuu> searchPage(SearchKhCnCtrinhNcReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<KhCnCongTrinhNghienCuu> data = khCnCongTrinhNghienCuuRepository.searchPage(objReq, pageable);
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });

        return data;
    }

    @Transactional
    public KhCnCongTrinhNghienCuu save(KhCnCongTrinhNghienCuuReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        Optional<KhCnCongTrinhNghienCuu> optional = khCnCongTrinhNghienCuuRepository.findAllByMaDeTai(objReq.getMaDeTai());
        if (optional.isPresent()) {
            throw new Exception("Mã đề tài đã tồn tại");
        }
        Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
        KhCnCongTrinhNghienCuu data = new ModelMapper().map(objReq, KhCnCongTrinhNghienCuu.class);
        data.setMaDvi(userInfo.getDvql());
        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
        KhCnCongTrinhNghienCuu created = khCnCongTrinhNghienCuuRepository.save(data);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), data.getId(), KhCnCongTrinhNghienCuu.TABLE_NAME);
        List<FileDinhKem> fileTienDoTh = fileDinhKemService.saveListFileDinhKem(objReq.getFileTienDoTh(), data.getId(), KhCnTienDoThucHien.TABLE_NAME);
        List<FileDinhKem> fileNghiemThuTl = fileDinhKemService.saveListFileDinhKem(objReq.getFileNghiemThuTl(), data.getId(), KhCnNghiemThuThanhLy.TABLE_NAME);
        created.setFileDinhKem(fileDinhKem);
        created.setFileTienDoTh(fileTienDoTh);
        created.setFileNghiemThuTl(fileNghiemThuTl);
        this.saveCtiet(data, objReq);
        return created;
    }

    @Transactional
    public KhCnCongTrinhNghienCuu update(KhCnCongTrinhNghienCuuReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        Optional<KhCnCongTrinhNghienCuu> optional = khCnCongTrinhNghienCuuRepository.findById(objReq.getId());
        Optional<KhCnCongTrinhNghienCuu> soQd = khCnCongTrinhNghienCuuRepository.findAllByMaDeTai(objReq.getMaDeTai());
        if (soQd.isPresent()) {
            if (!soQd.get().getId().equals(objReq.getId())) {
                throw new Exception("Mã đề tài đã tồn tại");
            }
        }
        KhCnCongTrinhNghienCuu data = optional.get();
        KhCnCongTrinhNghienCuu dataMap = new ModelMapper().map(objReq, KhCnCongTrinhNghienCuu.class);
        updateObjectToObject(data, dataMap);
        KhCnCongTrinhNghienCuu created = khCnCongTrinhNghienCuuRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(KhCnCongTrinhNghienCuu.TABLE_NAME));
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(KhCnTienDoThucHien.TABLE_NAME));
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(KhCnCongTrinhNghienCuu.TABLE_NAME));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), KhCnCongTrinhNghienCuu.TABLE_NAME);
        List<FileDinhKem> fileTienDoTh = fileDinhKemService.saveListFileDinhKem(objReq.getFileTienDoTh(), created.getId(), KhCnTienDoThucHien.TABLE_NAME);
        List<FileDinhKem> fileNghiemThuTl = fileDinhKemService.saveListFileDinhKem(objReq.getFileNghiemThuTl(), created.getId(), KhCnNghiemThuThanhLy.TABLE_NAME);
        created.setFileDinhKem(fileDinhKem);
        created.setFileTienDoTh(fileTienDoTh);
        created.setFileNghiemThuTl(fileNghiemThuTl);
        List<KhCnTienDoThucHien> tienDoThucHien = khCnTienDoThucHienRepository.findAllByIdHdr(data.getId());
        khCnTienDoThucHienRepository.deleteAll(tienDoThucHien);
        List<KhCnNghiemThuThanhLy> nghiemThuThanhLy = khCnNghiemThuThanhLyRepository.findAllByIdHdr(data.getId());
        khCnNghiemThuThanhLyRepository.deleteAll(nghiemThuThanhLy);
        this.saveCtiet(data, objReq);
        return created;
    }

    public void saveCtiet(KhCnCongTrinhNghienCuu data, KhCnCongTrinhNghienCuuReq objReq) {
        for (KhCnTienDoThucHienReq tienDoThucHienReq : objReq.getTienDoThucHien()) {
            KhCnTienDoThucHien tienDoThucHien = new ModelMapper().map(tienDoThucHienReq, KhCnTienDoThucHien.class);
            tienDoThucHien.setId(null);
            tienDoThucHien.setIdHdr(data.getId());
            khCnTienDoThucHienRepository.save(tienDoThucHien);
        }
        for (KhCnNghiemThuThanhLyReq nghiemThuThanhLyReq : objReq.getChildren()) {
            KhCnNghiemThuThanhLy nghiemThuThanhLy = new ModelMapper().map(nghiemThuThanhLyReq, KhCnNghiemThuThanhLy.class);
            nghiemThuThanhLy.setId(null);
            nghiemThuThanhLy.setIdHdr(data.getId());
            khCnNghiemThuThanhLyRepository.save(nghiemThuThanhLy);
        }
    }

    public KhCnCongTrinhNghienCuu detail(String ids) throws Exception {
        Optional<KhCnCongTrinhNghienCuu> optional = khCnCongTrinhNghienCuuRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        KhCnCongTrinhNghienCuu data = optional.get();
//        Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapCapDeTai = getListDanhMucChung("CAP_DE_TAI");
        Map<String, String> mapNguonVon = getListDanhMucChung("NGUON_VON");
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDvi.get(data.getMaDvi()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<KhCnTienDoThucHien> tienDoThucHien = khCnTienDoThucHienRepository.findAllByIdHdr(data.getId());
        tienDoThucHien.forEach(item -> {
            item.setTenTrangThaiTd(TrangThaiAllEnum.getLabelById(item.getTrangThaiTd()));
        });
        data.setTenCapDeTai(mapCapDeTai.get(data.getCapDeTai()));
        data.setTenNguonVon(mapNguonVon.get(data.getNguonVon()));
        data.setTongChiPhiStr(docxToPdfConverter.convertBigDecimalToStrNotDecimal(data.getTongChiPhi()));
        List<KhCnNghiemThuThanhLy> nghiemThuThanhLy = khCnNghiemThuThanhLyRepository.findAllByIdHdr(data.getId());
        data.setChildren(nghiemThuThanhLy);
        data.setTienDoThucHien(tienDoThucHien);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Collections.singleton(KhCnCongTrinhNghienCuu.TABLE_NAME));
        data.setFileDinhKem(fileDinhKem);
        List<FileDinhKem> fileTienDoTh = fileDinhKemService.search(data.getId(), Collections.singleton(KhCnTienDoThucHien.TABLE_NAME));
        data.setFileTienDoTh(fileTienDoTh);
        List<FileDinhKem> fileNghiemThuTl = fileDinhKemService.search(data.getId(), Collections.singleton(KhCnNghiemThuThanhLy.TABLE_NAME));
        data.setFileNghiemThuTl(fileNghiemThuTl);
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<KhCnCongTrinhNghienCuu> optional = khCnCongTrinhNghienCuuRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        KhCnCongTrinhNghienCuu data = optional.get();
        List<KhCnTienDoThucHien> tienDoThucHien = khCnTienDoThucHienRepository.findAllByIdHdr(data.getId());
        khCnTienDoThucHienRepository.deleteAll(tienDoThucHien);
        List<KhCnNghiemThuThanhLy> nghiemThuThanhLy = khCnNghiemThuThanhLyRepository.findAllByIdHdr(data.getId());
        khCnNghiemThuThanhLyRepository.deleteAll(nghiemThuThanhLy);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(KhCnCongTrinhNghienCuu.TABLE_NAME));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(KhCnTienDoThucHien.TABLE_NAME));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(KhCnNghiemThuThanhLy.TABLE_NAME));
        khCnCongTrinhNghienCuuRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<KhCnCongTrinhNghienCuu> list = khCnCongTrinhNghienCuuRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listIdHdr = list.stream().map(KhCnCongTrinhNghienCuu::getId).collect(Collectors.toList());
        List<KhCnTienDoThucHien> tienDoThucHien = khCnTienDoThucHienRepository.findAllByIdHdrIn(listIdHdr);
        khCnTienDoThucHienRepository.deleteAll(tienDoThucHien);
        List<KhCnNghiemThuThanhLy> nghiemThuThanhLy = khCnNghiemThuThanhLyRepository.findAllByIdHdrIn(listIdHdr);
        khCnNghiemThuThanhLyRepository.deleteAll(nghiemThuThanhLy);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(KhCnCongTrinhNghienCuu.TABLE_NAME));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(KhCnTienDoThucHien.TABLE_NAME));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(KhCnNghiemThuThanhLy.TABLE_NAME));
        khCnCongTrinhNghienCuuRepository.deleteAll(list);

    }

    public void export(SearchKhCnCtrinhNcReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<KhCnCongTrinhNghienCuu> page = this.searchPage(objReq);
        List<KhCnCongTrinhNghienCuu> data = page.getContent();

        String title = "Danh sách công trình nghiên cứu";
        String[] rowsName = new String[]{"STT", "Mã đề tài", "Tên đề tài", "Cấp đề tài", "Từ năm", "Đến năm", "Trang Thái"};
        String fileName = "danh-sach-cong-trinh-nghien-cuu.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            KhCnCongTrinhNghienCuu dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getMaDeTai();
            objs[2] = dx.getTenDeTai();
            objs[3] = dx.getCapDeTai();
            objs[4] = dx.getNgayKyTu();
            objs[5] = dx.getNgayKyDen();
            objs[6] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public KhCnCongTrinhNghienCuu approve(StatusReq statusReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<KhCnCongTrinhNghienCuu> optional = khCnCongTrinhNghienCuuRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHO_DUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
                optional.get().setNguoiGduyetId(userInfo.getId());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(getUser().getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuChoi(statusReq.getLyDo());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                optional.get().setNguoiPduyetId(getUser().getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        KhCnCongTrinhNghienCuu created = khCnCongTrinhNghienCuuRepository.save(optional.get());
        return created;
    }

    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
            reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
            ReportTemplate model = findByTenFile(reportTemplateRequest);
            byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            KhCnCongTrinhNghienCuu detail = this.detail(DataUtils.safeToString(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

}
