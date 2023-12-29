package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jasper.HeaderColumn;
import com.tcdt.qlnvhang.jasper.JasperReport;
import com.tcdt.qlnvhang.jasper.JasperReportManager;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcTcDtlPreviewReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcTcHdrPreviewReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucDtlReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenTongCucHdrReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.ReportTemplateService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class THKeHoachDieuChuyenTongCucServiceImpl extends BaseServiceImpl {
    @Autowired
    private DcnbKeHoachDcHdrRepository dcHdrRepository;
    @Autowired
    private THKeHoachDieuChuyenTongCucHdrRepository tongCucHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenTongCucDtlRepository thKeHoachDieuChuyenTongCucDtlRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private THKeHoachDieuChuyenCucKhacCucDtlRepository thKeHoachDieuChuyenCucKhacCucDtlRepository;

    @Autowired
    private THKeHoachDieuChuyenCucHdrRepository thKeHoachDieuChuyenCucHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenNoiBoCucDtlRepository thKeHoachDieuChuyenNoiBoCucDtlRepository;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private ReportTemplateService reportTemplateService;

    public Page<THKeHoachDieuChuyenTongCucHdr> searchPage(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        req.setMaDVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<THKeHoachDieuChuyenTongCucHdr> search = tongCucHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr save(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        THKeHoachDieuChuyenTongCucHdr data = new THKeHoachDieuChuyenTongCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDVi(currentUser.getUser().getDvql());
        data.setTenDVi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.CHUATAO_QD);
        data.setNgayTongHop(objReq.getNgayTongHop());
        data.setThoiGianTongHop(objReq.getThoiGianTongHop());
        data.setLoaiHangHoa(objReq.getLoaiHangHoa());
        data.setChungLoaiHangHoa(objReq.getChungLoaiHangHoa());
        data.setTenLoaiHangHoa(objReq.getTenLoaiHangHoa());
        data.setNamKeHoach(objReq.getNamKeHoach());
        data.setLoaiDieuChuyen(objReq.getLoaiDieuChuyen());
        List<THKeHoachDieuChuyenTongCucDtl> chiTiet = new ArrayList<>();
        TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
        objReq.setThKeHoachDieuChuyenTongCucDtls(createPlan(currentUser, tongHopSearch));
        if (objReq.getThKeHoachDieuChuyenTongCucDtls() != null && !objReq.getThKeHoachDieuChuyenTongCucDtls().isEmpty()) {
            for (ThKeHoachDieuChuyenTongCucDtlReq ct : objReq.getThKeHoachDieuChuyenTongCucDtls()) {
                THKeHoachDieuChuyenTongCucDtl ctTongHop = new THKeHoachDieuChuyenTongCucDtl();
                ObjectMapperUtils.map(ct, ctTongHop);
                chiTiet.add(ctTongHop);
            }
        }
        data.setThKeHoachDieuChuyenTongCucDtls(chiTiet);
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(data);
        if (created.getId() > 0) {
            List<Long> danhSachKeHoach = chiTiet.stream().map(THKeHoachDieuChuyenTongCucDtl::getThKhDcHdrId)
                    .collect(Collectors.toList());
            thKeHoachDieuChuyenCucHdrRepository.updateIdTongHop(created.getId(), danhSachKeHoach);
        }
        if (chiTiet.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        } else {
            for (THKeHoachDieuChuyenTongCucDtl ct : chiTiet) {
                ct.setHdrId(created.getId());
            }
        }
        created.setMaTongHop(created.getId());
        THKeHoachDieuChuyenTongCucHdr createdSave = tongCucHdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), createdSave.getId(), THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return createdSave;
    }

    public List<THKeHoachDieuChuyenTongCucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenTongCucHdr> allById = tongCucHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            data.getThKeHoachDieuChuyenTongCucDtls().forEach(data1 -> {
                if (data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                    Hibernate.initialize(data1.getThKeHoachDieuChuyenCucHdr());
                }
                if (data.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                    List<Long> listId = Arrays.stream(data1.getThKeHoachDieuChuyenCucKhacCucDtl().getDcnbKeHoachDcHdrId().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                    data1.getThKeHoachDieuChuyenCucHdr().getThKeHoachDieuChuyenCucKhacCucDtls().clear();
                    data1.getThKeHoachDieuChuyenCucKhacCucDtl().setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                }
            });
        });
        return allById;
    }


    public List<THKeHoachDieuChuyenTongCucHdr> danhSachMaTongHop(TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<THKeHoachDieuChuyenTongCucHdr> danhSachMaTongHop = tongCucHdrRepository.filterMaTongHop(req);
        return danhSachMaTongHop;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<THKeHoachDieuChuyenTongCucDtl> list = thKeHoachDieuChuyenTongCucDtlRepository.findByHdrId(data.getId());
        list.forEach(e -> {
            e.getThKeHoachDieuChuyenCucHdr().setIdThTongCuc(null);
            thKeHoachDieuChuyenCucHdrRepository.save(e.getThKeHoachDieuChuyenCucHdr());
        });
        thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(list);
        tongCucHdrRepository.delete(data);
    }


    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<THKeHoachDieuChuyenTongCucHdr> list = tongCucHdrRepository.findAllById(idSearchReq.getIds());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(THKeHoachDieuChuyenTongCucHdr::getId).collect(Collectors.toList());
        fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<THKeHoachDieuChuyenTongCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenTongCucDtlRepository.findAllByHdrIdIn(listId);
        listTongHopKeHoach.forEach(e -> {
            e.getThKeHoachDieuChuyenCucHdr().setIdThTongCuc(null);
            thKeHoachDieuChuyenCucHdrRepository.save(e.getThKeHoachDieuChuyenCucHdr());
        });
        thKeHoachDieuChuyenTongCucDtlRepository.deleteAll(listTongHopKeHoach);
        tongCucHdrRepository.deleteAll(list);
    }

    @Transactional
    public THKeHoachDieuChuyenTongCucHdr update(CustomUserDetails currentUser, ThKeHoachDieuChuyenTongCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenTongCucHdr> optional = tongCucHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        List<THKeHoachDieuChuyenTongCucHdr> maTongHop = tongCucHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
//        if (!maTongHop.isEmpty() && objReq.getMaTongHop().split("/").length == 1) {
//            if (!maTongHop.get(0).getId().equals(objReq.getId())) {
//                throw new Exception("Mã tổng hợp đã tồn tại");
//            }
//        }
        THKeHoachDieuChuyenTongCucHdr data = optional.get();
        THKeHoachDieuChuyenTongCucHdr dataMap = new ModelMapper().map(objReq, THKeHoachDieuChuyenTongCucHdr.class);
        BeanUtils.copyProperties(data, dataMap);
        dataMap.setTrichYeu(objReq.getTrichYeu());
        THKeHoachDieuChuyenTongCucHdr created = tongCucHdrRepository.save(dataMap);
        data.setNguoiSuaId(currentUser.getUser().getId());
        created = tongCucHdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), THKeHoachDieuChuyenTongCucHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }

    @Transactional
    public List<ThKeHoachDieuChuyenTongCucDtlReq> createPlan(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(), "01");
        List<ThKeHoachDieuChuyenTongCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            req.setMaDVi(cqt.getMaDvi());
            if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CHI_CUC_TRONG_1_CUC)) {
                List<THKeHoachDieuChuyenCucHdr> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucHdrRepository.findByDonViAndTrangThaiTongCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, req.getThoiGianTongHop());
                for (THKeHoachDieuChuyenCucHdr entry : dcnbKeHoachDcHdrs) {
                    Hibernate.initialize(entry.getThKeHoachDieuChuyenCucKhacCucDtls());
                    THKeHoachDieuChuyenCucHdr khhc = SerializationUtils.clone(entry);
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ModelMapper().map(khhc, ThKeHoachDieuChuyenTongCucDtlReq.class);
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    chiTiet.setMaCucDxuat(req.getMaDVi());
                    chiTiet.setTenCucDxuat(cqt.getTenDvi());
                    chiTiet.setSoDxuat(khhc.getSoDeXuat());
                    chiTiet.setThKhDcHdrId(khhc.getId());
                    chiTiet.setNgayTrinhDuyetTc(entry.getNgayDuyetLdc());
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(req.getMaDVi(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CHI_CUC_TRONG_1_CUC, Contains.DADUYET_LDCC, req.getThoiGianTongHop(), khhc.getId());
                    chiTiet.setTongDuToanKp(tongDuToanKp == null ? 0 : tongDuToanKp);
                    result.add(chiTiet);
                }
            } else if (req.getLoaiDieuChuyen().equals(Contains.GIUA_2_CUC_DTNN_KV)) {
                List<THKeHoachDieuChuyenCucKhacCucDtl> dcnbKeHoachDcHdrs = thKeHoachDieuChuyenCucKhacCucDtlRepository.findByDonViAndTrangThaiAndLoaiDcCuc(req.getMaDVi(), Contains.DADUYET_LDC, Contains.GIUA_2_CUC_DTNN_KV, req.getThoiGianTongHop());
                Map<String, List<THKeHoachDieuChuyenCucKhacCucDtl>> postsPerType = dcnbKeHoachDcHdrs.stream()
                        .collect(groupingBy(THKeHoachDieuChuyenCucKhacCucDtl::getMaCucNhan));
                for (Map.Entry<String, List<THKeHoachDieuChuyenCucKhacCucDtl>> entry : postsPerType.entrySet()) {
                    ThKeHoachDieuChuyenTongCucDtlReq chiTiet = new ThKeHoachDieuChuyenTongCucDtlReq();
                    List<THKeHoachDieuChuyenCucKhacCucDtl> thkh = entry.getValue();
                    chiTiet.setMaCucNhan(entry.getKey());
                    chiTiet.setTenCucNhan(thkh.get(0).getTenCucNhan());
                    chiTiet.setMaCucDxuat(cqt.getMaDvi());
                    chiTiet.setTenCucDxuat(cqt.getTenDvi());
                    chiTiet.setId(null);
                    chiTiet.setHdrId(null);
                    thkh.forEach(data -> {
                        chiTiet.setThKhDcDtlId(data.getId());
                        chiTiet.setSoDxuat(data.getTHKeHoachDieuChuyenCucHdr().getSoDeXuat());
                        chiTiet.setNgayTrinhDuyetTc(data.getTHKeHoachDieuChuyenCucHdr().getNgayDuyetLdc());
                        chiTiet.setTrichYeu(data.getTHKeHoachDieuChuyenCucHdr().getTrichYeu());
                        chiTiet.setThKhDcHdrId(data.getHdrId());
                        List<THKeHoachDieuChuyenCucKhacCucDtl> chiTietKh = thKeHoachDieuChuyenCucKhacCucDtlRepository.findAllByHdrIdAndId(chiTiet.getThKhDcHdrId(), chiTiet.getThKhDcDtlId());
                        chiTiet.setThKeHoachDieuChuyenCucKhacCucDtls(chiTietKh);
                        chiTietKh.forEach(data2 -> {
                            List<Long> listId = Arrays.asList(data2.getDcnbKeHoachDcHdrId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                            List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcHdrRepository.findByIdIn(listId);
                            data2.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                        });
                    });
                    Long tongDuToanKp = dcnbKeHoachDcDtlRepository.findByMaDviCucAndTypeAndLoaiDcTongCucCuc(req.getMaDVi(), chiTiet.getMaCucNhan(), Contains.DIEU_CHUYEN, Contains.GIUA_2_CUC_DTNN_KV, Contains.DADUYET_LDCC, req.getThoiGianTongHop(), chiTiet.getThKhDcHdrId());
                    chiTiet.setTongDuToanKp(tongDuToanKp == null ? 0 : tongDuToanKp);
                    result.add(chiTiet);
                }
            }
        }
        if (result.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }
        return result;
    }

    @Transactional
    public void export(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<THKeHoachDieuChuyenTongCucHdr> page = this.searchPage(currentUser, objReq);
        List<THKeHoachDieuChuyenTongCucHdr> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch điều chuyển";
        String[] rowsName = new String[]{"STT", "Năm KH", "Mã tổng hợp", "Loại điều chuyển", "Ngày tổng hợp", "Nội dung tổng hợp", "Số QĐ PD KHĐC", "Trạng thái",};
        String fileName = "danh-sach-tong-hop-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            THKeHoachDieuChuyenTongCucHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getMaTongHop();
            objs[3] = dx.getLoaiDieuChuyen();
            objs[4] = dx.getNgayTongHop();
            objs[5] = dx.getTrichYeu();
            objs[6] = dx.getSoQddc();
            objs[7] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public void preview(CustomUserDetails currentUser, DcnbQuyetDinhDcTcHdrPreviewReq objReq, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        if(objReq.getFileType() == null){
            objReq.setFileType("pdf");
        }
        if ("pdf".equalsIgnoreCase(objReq.getFileType())) {
            fileTypePdf(objReq, response, map);
        }
        if ("docx".equalsIgnoreCase(objReq.getFileType())) {
            fileTypeDocx(objReq, response, map);
        }
        if ("xlsx".equalsIgnoreCase(objReq.getFileType())) {
            fileTypeXlsx(objReq, response, map);
        }
    }

    private void fileTypeXlsx(DcnbQuyetDinhDcTcHdrPreviewReq objReq, HttpServletResponse response, Map map) throws Exception {
        JasperReport jasperReport = getJasperReportTo1(objReq.getNam(), objReq.getListDtl().get(0), 1, objReq.getType());
        byte[] xlsxb = reportTemplateService.exportReport(map, objReq.getFileType(), objReq.getListDtl().get(0).getListData(), jasperReport);
        List<InputStream> listFile = new ArrayList<>();
        listFile.add(new ByteArrayInputStream(xlsxb));
        XSSFWorkbook mergedWorkbook = new XSSFWorkbook();
        XSSFSheet mergedSheet = mergedWorkbook.createSheet("Data");
        if (objReq.getListDtl().size() > 1) {
            for (int i = 1; i < objReq.getListDtl().size(); i++) {
                DcnbQuyetDinhDcTcDtlPreviewReq dtl = objReq.getListDtl().get(i);
                JasperReport jasperReportN = getJasperReportToN(objReq.getNam(), dtl, (i + 1), objReq.getType());
                byte[] xlsxN = reportTemplateService.exportReport(map, objReq.getFileType(), dtl.getListData(), jasperReportN);
                listFile.add(new ByteArrayInputStream(xlsxN));
            }
        }
        int rowNum = 0;
        for (InputStream fis : listFile) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Sao chép dữ liệu từ mỗi sheet
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                XSSFRow sourceRow = sheet.getRow(i);
                XSSFRow newRow = mergedSheet.createRow(rowNum++);

                if (sourceRow != null) {
                    for (int j = sourceRow.getFirstCellNum(); j < sourceRow.getLastCellNum(); j++) {
                        XSSFCell sourceCell = sourceRow.getCell(j);
                        if (sourceCell != null) {
                            XSSFCell newCell = newRow.createCell(j);

                            // Sao chép giá trị và style
                            copyCellValueAndStyle(sourceCell, newCell,workbook, mergedWorkbook);
                        }
                    }
                }
            }
            copyMergedRegions(sheet, mergedSheet, rowNum - (sheet.getLastRowNum() + 1));
            mergedSheet.createRow(rowNum++);
            mergedSheet.createRow(rowNum++);
            mergedSheet.createRow(rowNum++);
        }
        mergedWorkbook.write(response.getOutputStream());
    }
    private static void copyMergedRegions(XSSFSheet sourceSheet, XSSFSheet destSheet, int destRowNumStart) {
        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sourceSheet.getMergedRegion(i);
            CellRangeAddress newMergedRegion = new CellRangeAddress(
                    mergedRegion.getFirstRow() + destRowNumStart,
                    mergedRegion.getLastRow() + destRowNumStart,
                    mergedRegion.getFirstColumn(),
                    mergedRegion.getLastColumn());
            destSheet.addMergedRegion(newMergedRegion);
        }
    }

    private static CellStyle cloneStyleToWorkbook(CellStyle sourceStyle, Workbook destWorkbook, Workbook mergeWorkbook) {
        CellStyle newStyle = mergeWorkbook.createCellStyle();
        if(newStyle != null){
            newStyle.setAlignment(sourceStyle.getAlignment());
            newStyle.setVerticalAlignment(sourceStyle.getVerticalAlignment());

            // Sao chép các thuộc tính màu nền và mẫu
            newStyle.setFillForegroundColor(sourceStyle.getFillForegroundColor());
            newStyle.setFillPattern(sourceStyle.getFillPattern());

            // Sao chép các thuộc tính biên
            newStyle.setBorderBottom(sourceStyle.getBorderBottom());
            newStyle.setBorderTop(sourceStyle.getBorderTop());
            newStyle.setBorderLeft(sourceStyle.getBorderLeft());
            newStyle.setBorderRight(sourceStyle.getBorderRight());
            newStyle.setBottomBorderColor(sourceStyle.getBottomBorderColor());
            newStyle.setTopBorderColor(sourceStyle.getTopBorderColor());
            newStyle.setLeftBorderColor(sourceStyle.getLeftBorderColor());
            newStyle.setRightBorderColor(sourceStyle.getRightBorderColor());

            // Sao chép các thuộc tính font
            Font sourceFont = destWorkbook.getFontAt(sourceStyle.getFontIndex());
            Font newFont = destWorkbook.createFont();
            newFont.setBold(sourceFont.getBold());
            newFont.setItalic(sourceFont.getItalic());
            newFont.setFontHeight(sourceFont.getFontHeight());
            newFont.setFontName(sourceFont.getFontName());
            newFont.setUnderline(sourceFont.getUnderline());
            newFont.setColor(sourceFont.getColor());
            newStyle.setFont(newFont);

            // Sao chép các thuộc tính định dạng dữ liệu (nếu có)
            newStyle.setDataFormat(destWorkbook.createDataFormat().getFormat(sourceStyle.getDataFormatString()));

            // Sao chép các thuộc tính wrap text (tự động xuống dòng)
            newStyle.setWrapText(sourceStyle.getWrapText());

            // Sao chép các thuộc tính khác như độ rộng cố định, xoay text, v.v.
            newStyle.setShrinkToFit(sourceStyle.getShrinkToFit());
            newStyle.setRotation(sourceStyle.getRotation());
        }
        return newStyle;
    }
    private static void copyCellValueAndStyle(Cell sourceCell, Cell newCell,Workbook destWorkbook, Workbook mergeWorkbook) {
        // Sao chép style từ cell nguồn
        CellStyle newCellStyle = cloneStyleToWorkbook(sourceCell.getCellStyle(), destWorkbook,mergeWorkbook);
        if(newCellStyle != null){
            newCell.setCellStyle(newCellStyle);
        }

        // Sao chép giá trị cell
        switch (sourceCell.getCellType()) {
            case STRING:
                newCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                newCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                break;
        }
    }
    private void fileTypeDocx(DcnbQuyetDinhDcTcHdrPreviewReq objReq, HttpServletResponse response, Map map) throws Exception {
        JasperReport jasperReport = getJasperReportTo1(objReq.getNam(), objReq.getListDtl().get(0), 1, objReq.getType());
        byte[] docb = reportTemplateService.exportReport(map, objReq.getFileType(), objReq.getListDtl().get(0).getListData(), jasperReport);
        XWPFDocument doc1 = new XWPFDocument(new ByteArrayInputStream(docb));
        if (objReq.getListDtl().size() > 1) {
            for (int i = 1; i < objReq.getListDtl().size(); i++) {
                DcnbQuyetDinhDcTcDtlPreviewReq dtl = objReq.getListDtl().get(i);
                JasperReport jasperReportN = getJasperReportToN(objReq.getNam(), dtl, (i + 1), objReq.getType());
                byte[] docbN = reportTemplateService.exportReport(map, objReq.getFileType(), dtl.getListData(), jasperReportN);
                XWPFDocument docN = new XWPFDocument(new ByteArrayInputStream(docbN));
                for (XWPFParagraph para : docN.getParagraphs()) {
                    doc1.createParagraph().createRun().setText(para.getText());
                }
            }
        }
        doc1.write(response.getOutputStream());
    }

    private void fileTypePdf(DcnbQuyetDinhDcTcHdrPreviewReq objReq, HttpServletResponse response, Map map) throws Exception {
        JasperReport jasperReport = getJasperReportTo1(objReq.getNam(), objReq.getListDtl().get(0), 1, objReq.getType());
        byte[] pdf = reportTemplateService.exportReport(map, objReq.getListDtl().get(0).getListData(), jasperReport);
        List<InputStream> listFile = new ArrayList<>();
        listFile.add(new ByteArrayInputStream(pdf));
        if (objReq.getListDtl().size() > 1) {
            for (int i = 1; i < objReq.getListDtl().size(); i++) {
                DcnbQuyetDinhDcTcDtlPreviewReq dtl = objReq.getListDtl().get(i);
                JasperReport jasperReportN = getJasperReportToN(objReq.getNam(), dtl, (i + 1), objReq.getType());
                byte[] pdfN = reportTemplateService.exportReport(map, dtl.getListData(), jasperReportN);
                listFile.add(new ByteArrayInputStream(pdfN));
            }
        }
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", String.format("attachment; filename=data.pdf"));
        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationStream(response.getOutputStream());
        merger.addSources(listFile);
        merger.mergeDocuments(null);
    }

    private JasperReport getJasperReportToN(Integer nam, DcnbQuyetDinhDcTcDtlPreviewReq dtl, int i, String type) {
        JasperReport jasperReport = new JasperReport();
        JasperReportManager.createQueryString(jasperReport, "select * from user");
        JasperReportManager.createProperty(jasperReport, "com.jaspersoft.studio.data.defaultdataadapter", "Orcale");

        JasperReportManager.createBackground(jasperReport, null, null);
        JasperReportManager.addTableTitle(jasperReport, i + ". Cục DTNN KV " + dtl.getTenCuc() + " - Số tờ trình: " + dtl.getSoToTrinh() + ", dự toán kinh phí đề xuất: " + dtl.getTongDuToanKinhPhi() + " (triệu đồng)", false);
        JasperReportManager.createPageHeader(jasperReport, null, null);
        List<HeaderColumn> headerColumns = new ArrayList<>();
        headerColumns.add(new HeaderColumn("STT", 0, "stt", "java.lang.String"));
        HeaderColumn ccdexdc = new HeaderColumn("Chi cục đề xuất điều chuyển", 0);
        ccdexdc.getChildren().add(new HeaderColumn("Chi cục DTNN", 1, "tenChiCucXuat", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Lô kho", 1, "tenLoKho", "java.lang.String", 200));
        ccdexdc.getChildren().add(new HeaderColumn("Loại hàng", 1, "tenLoaiVthh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Chủng loại hàng", 1, "tenCloaiVthh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Năm nhập kho", 1, "namNhap", "java.lang.Double"));
        ccdexdc.getChildren().add(new HeaderColumn("ĐVT", 1, "donViTinh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Tồn kho", 1, "tonKho", "java.lang.Double"));
        ccdexdc.getChildren().add(new HeaderColumn("SL điều chuyền", 1, "soLuongDc", "java.lang.Double"));
        if ("CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Dự toán kinh phí đề xuất (triệu đồng)", 0, "duToanKphi", "java.lang.Double", 100, true));
        }
        headerColumns.add(ccdexdc);

        headerColumns.add(new HeaderColumn("Thời gian dự kiến điều chuyển", 0, "thoiGianDkDc", "java.time.LocalDate", 100));
        headerColumns.add(new HeaderColumn("Lý do điều chuyển", 0, "lyDo", "java.lang.String"));
        if ("CHI_CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Dự toán kinh phí đề xuất (triệu đồng)", 0, "duToanKphi", "java.lang.Double", 100, true));
            HeaderColumn ccndcd = new HeaderColumn("Chi cục nhận điều chỉnh đến", 0);
            ccndcd.getChildren().add(new HeaderColumn("Chi cục DTNN", 1, "tenChiCucNhan", "java.lang.String"));
            ccndcd.getChildren().add(new HeaderColumn("Điểm kho", 1, "tenDiemKhoNhan", "java.lang.String"));
            ccndcd.getChildren().add(new HeaderColumn("Lô kho", 1, "tenLoKhoNhan", "java.lang.String", 200));
            ccndcd.getChildren().add(new HeaderColumn("Tích lượng khả dụng", 1, "tichLuongKd", "java.lang.Double"));
            ccndcd.getChildren().add(new HeaderColumn("SL nhập điều chuyển", 1, "soLuongPhanBo", "java.lang.Double"));
            headerColumns.add(ccndcd);
        }
        if ("CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Chi cục nhận điều chỉnh đến", 0, "tenChiCucNhan", "java.lang.String", 100));
        }

        JasperReportManager.setColumnHeader(jasperReport, headerColumns);
        return jasperReport;
    }

    private JasperReport getJasperReportTo1(Integer nam, DcnbQuyetDinhDcTcDtlPreviewReq dtl, int i, String type) {
        JasperReport jasperReport = new JasperReport();
        JasperReportManager.createQueryString(jasperReport, "select * from user");
        JasperReportManager.createProperty(jasperReport, "com.jaspersoft.studio.data.defaultdataadapter", "Orcale");

        JasperReportManager.createBackground(jasperReport, null, null);
        if ("CHI_CUC".equals(type)) {
            JasperReportManager.addTitle(jasperReport, "TỔNG HỢP KẾ HOẠCH ĐIỀU CHUYỂN NỘI BỘ GIỮA CÁC CHI CỤC TRONG CÙNG 1 CỤC - Năm " + nam, null, true);
        }
        if ("CUC".equals(type)) {
            JasperReportManager.addTitle(jasperReport, "TỔNG HỢP KẾ HOẠCH ĐIỀU CHUYỂN NỘI BỘ GIỮA 2 CỤC DTNN KHU VỰC - Năm " + nam, null, true);
        }
        JasperReportManager.addTableTitle(jasperReport, i + ". Cục DTNN KV " + dtl.getTenCuc() + " - Số tờ trình: " + dtl.getSoToTrinh() + ", dự toán kinh phí đề xuất: " + dtl.getTongDuToanKinhPhi() + " (triệu đồng)", false);
        JasperReportManager.createPageHeader(jasperReport, null, null);
        List<HeaderColumn> headerColumns = new ArrayList<>();
        headerColumns.add(new HeaderColumn("STT", 0, "stt", "java.lang.String", 40));
        HeaderColumn ccdexdc = new HeaderColumn("Chi cục đề xuất điều chuyển", 0);
        ccdexdc.getChildren().add(new HeaderColumn("Chi cục DTNN", 1, "tenChiCucXuat", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Lô kho", 1, "tenLoKho", "java.lang.String",200));
        ccdexdc.getChildren().add(new HeaderColumn("Loại hàng", 1, "tenLoaiVthh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Chủng loại hàng", 1, "tenCloaiVthh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Năm nhập kho", 1, "namNhap", "java.lang.Double"));
        ccdexdc.getChildren().add(new HeaderColumn("ĐVT", 1, "donViTinh", "java.lang.String"));
        ccdexdc.getChildren().add(new HeaderColumn("Tồn kho", 1, "tonKho", "java.lang.Double"));
        ccdexdc.getChildren().add(new HeaderColumn("SL điều chuyền", 1, "soLuongDc", "java.lang.Double"));
        if("CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Dự toán kinh phí đề xuất (triệu đồng)", 0, "duToanKphi", "java.lang.Double", 100, true));
        }
        headerColumns.add(ccdexdc);

        headerColumns.add(new HeaderColumn("Thời gian dự kiến điều chuyển", 0, "thoiGianDkDc", "java.time.LocalDate", 100));
        headerColumns.add(new HeaderColumn("Lý do điều chuyển", 0, "lyDo", "java.lang.String"));
        if ("CHI_CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Dự toán kinh phí đề xuất (triệu đồng)", 0, "duToanKphi", "java.lang.Double", 100, true));
            HeaderColumn ccndcd = new HeaderColumn("Chi cục nhận điều chỉnh đến", 0);
            ccndcd.getChildren().add(new HeaderColumn("Chi cục DTNN", 1, "tenChiCucNhan", "java.lang.String"));
            ccndcd.getChildren().add(new HeaderColumn("Điểm kho", 1, "tenDiemKhoNhan", "java.lang.String"));
            ccndcd.getChildren().add(new HeaderColumn("Lô kho", 1, "tenLoKhoNhan", "java.lang.String", 200));
            ccndcd.getChildren().add(new HeaderColumn("Tích lượng khả dụng", 1, "tichLuongKd", "java.lang.Double"));
            ccndcd.getChildren().add(new HeaderColumn("SL nhập điều chuyển", 1, "soLuongPhanBo", "java.lang.Double"));
            headerColumns.add(ccndcd);
        }
        if ("CUC".equals(type)) {
            headerColumns.add(new HeaderColumn("Chi cục nhận điều chỉnh đến", 0, "tenChiCucNhan", "java.lang.String", 100));
        }

        JasperReportManager.setColumnHeader(jasperReport, headerColumns);
        return jasperReport;
    }
}

