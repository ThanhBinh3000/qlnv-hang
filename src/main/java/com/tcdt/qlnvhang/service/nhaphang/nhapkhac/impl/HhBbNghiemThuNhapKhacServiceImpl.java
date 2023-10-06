package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuKtcl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacSearch;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhBbanNghiemThuDtlReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstDtlReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBbanNghiemThuDtl;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class HhBbNghiemThuNhapKhacServiceImpl extends BaseServiceImpl implements HhBbNghiemThuNhapKhacService {
    @Autowired
    private HhBbNghiemThuNhapKhacRepository hhBbNghiemThuNhapKhacRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private HhBbNghiemThuNhapKhacDtlRepository hhBbNghiemThuNhapKhacDtlRepository;
    @Autowired
    private KtNganLoRepository ktNganLoRepository;
    @Autowired
    private KtNganKhoRepository ktNganKhoRepository;
    @Autowired
    private HhQdGiaoNvNhapKhacHdrRepository hhQdGiaoNvNhapKhacHdrRepository;
    @Autowired
    private HhQdPdNhapKhacDtlRepository hhQdPdNhapKhacDtlRepository;
    @Override
    public Page<HhQdGiaoNvuNhapHangKhacHdr> timKiem(HhBbNghiemThuNhapKhacSearch req) throws Exception {
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
        req.setTuNgayKTStr(convertFullDateToString(req.getTuNgayKT()));
        req.setDenNgayKTStr(convertFullDateToString(req.getDenNgayKT()));
        req.setTuNgayLPStr(convertFullDateToString(req.getTuNgayLP()));
        req.setDenNgayLPStr(convertFullDateToString(req.getDenNgayLP()));
        req.setTrangThaiQdnk(Contains.BAN_HANH);
        Page<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.searchBbNtBq(req, pageable);
        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
        data.forEach( f -> {
            List<HhQdPdNhapKhacDtl> listDiaDiem = hhQdPdNhapKhacDtlRepository.findAllByIdHdr(f.getIdQdPdNk());
            for (HhQdPdNhapKhacDtl dtl: listDiaDiem) {
                dtl.setTenDiemKho(hashMapDmdv.get(dtl.getMaDiemKho()));
                dtl.setTenNganKho(hashMapDmdv.get(dtl.getMaNganKho()));
                dtl.setTenLoKho(hashMapDmdv.get(dtl.getMaLoKho()));
                List<HhBbNghiemThuNhapKhac> bbNghiemThuNhapKhacs = hhBbNghiemThuNhapKhacRepository.findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKho(f.getId(), dtl.getMaLoKho(), dtl.getMaNganKho());
                bbNghiemThuNhapKhacs.forEach( item ->  {
                    item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
                });
                dtl.setBbNghiemThuNhapKhacList(bbNghiemThuNhapKhacs);
            }
            f.setDtlList(listDiaDiem);
        });
        return data;
    }

    @Override
    public HhBbNghiemThuNhapKhac themMoi(HhBbNghiemThuNhapKhacReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhBbNghiemThuNhapKhac> optional = hhBbNghiemThuNhapKhacRepository.findBySoBbNtBq(objReq.getSoBbNtBq());
        if(optional.isPresent()){
            throw new Exception("Số biên bản đã tồn tại");
        }
        HhBbNghiemThuNhapKhac data = ObjectMapperUtils.map(objReq, HhBbNghiemThuNhapKhac.class);
        data.setNguoiTao(userInfo.getUsername());
        data.setTenNguoiTao(userInfo.getFullName());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        HhBbNghiemThuNhapKhac created = hhBbNghiemThuNhapKhacRepository.save(data);
        luuFile(objReq, created);
        luuChiTiet(objReq, created);
        return created;
    }

    @Override
    public HhBbNghiemThuNhapKhac capNhat(HhBbNghiemThuNhapKhacReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        Optional<HhBbNghiemThuNhapKhac> optional = hhBbNghiemThuNhapKhacRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa.");
        }
        if (!StringUtils.isEmpty(objReq.getSoBbNtBq())) {
            Optional<HhBbNghiemThuNhapKhac> deXuat = hhBbNghiemThuNhapKhacRepository.findBySoBbNtBq(objReq.getSoBbNtBq());
            if (deXuat.isPresent() && (!deXuat.get().getId().equals(objReq.getId()))) {
                throw new Exception("Số đề xuất " + objReq.getSoBbNtBq() + " đã tồn tại");
            }
        }
        BeanUtils.copyProperties(objReq, optional.get());
        optional.get().setNgaySua(getDateTimeNow());
        optional.get().setNguoiSua(getUser().getUsername());
        luuFile(objReq, optional.get());
        luuChiTiet(objReq, optional.get());
        return hhBbNghiemThuNhapKhacRepository.save(optional.get());
    }

    @Override
    public HhBbNghiemThuNhapKhac chiTiet(Long id) throws Exception {
        Optional<HhBbNghiemThuNhapKhac> qOptional = hhBbNghiemThuNhapKhacRepository.findById(id);
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
        qOptional.get().setFileDinhKems(fileDinhKemService.search(qOptional.get().getId(), Collections.singletonList(HhBbNghiemThuNhapKhac.TABLE_NAME)));
        List<HhBbNghiemThuNhapKhacDtl> listDtl = hhBbNghiemThuNhapKhacDtlRepository.findAllByIdHdr(qOptional.get().getId());
        qOptional.get().setChildren(hhBbNghiemThuNhapKhacDtlRepository.findAllByIdHdr(qOptional.get().getId()));
//        qOptional.get().setDviChuDongThucHien(listDtl.stream().filter(item -> item.getType().equals(Contains.CHU_DONG)).collect(Collectors.toList()));
//        qOptional.get().setDmTongCucPdTruocThucHien(listDtl.stream().filter(item -> item.getType().equals(Contains.PHE_DUYET_TRUOC)).collect(Collectors.toList()));
        return qOptional.get();
    }

    @Override
    public HhBbNghiemThuNhapKhac pheDuyet(StatusReq stReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");
        if (StringUtils.isEmpty(stReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhBbNghiemThuNhapKhac> qOptional = hhBbNghiemThuNhapKhacRepository.findById(stReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        String status = stReq.getTrangThai() + qOptional.get().getTrangThai();
        if (!qOptional.get().getLoaiVthh().startsWith("02")) {
            switch (status) {
                case Contains.CHODUYET_TK + Contains.DUTHAO:
                case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
                case Contains.CHODUYET_TK + Contains.TUCHOI_KT:
                case Contains.CHODUYET_TK + Contains.TUCHOI_LDCC:
                    qOptional.get().setNguoiGuiDuyet(userInfo.getUsername());
                    qOptional.get().setNgayGuiDuyet(new Date());
                    break;
                case Contains.CHODUYET_KT + Contains.CHODUYET_TK:
                case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
                    qOptional.get().setThuKho(userInfo.getUsername());
                    qOptional.get().setTenThuKho(userInfo.getFullName());
                    qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                    qOptional.get().setNgayPduyet(new Date());
                    break;
                case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
                case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
                    qOptional.get().setKeToan(userInfo.getUsername());
                    qOptional.get().setTenKeToan(userInfo.getFullName());
                    qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                    qOptional.get().setNgayPduyet(new Date());
                    break;
                case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                    qOptional.get().setNguoiPduyet(userInfo.getUsername());
                    qOptional.get().setTenNguoiPduyet(userInfo.getFullName());
                    qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                    qOptional.get().setNgayPduyet(new Date());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        }else {
            switch (status) {
                case Contains.CHODUYET_TK + Contains.DUTHAO:
                case Contains.CHODUYET_TK + Contains.TUCHOI_TK:
                case Contains.CHODUYET_TK + Contains.TUCHOI_LDCC:
                    qOptional.get().setNguoiGuiDuyet(userInfo.getUsername());
                    qOptional.get().setNgayGuiDuyet(new Date());
                    break;
                case Contains.CHODUYET_LDCC + Contains.CHODUYET_TK:
                case Contains.TUCHOI_TK + Contains.CHODUYET_TK:
                    qOptional.get().setKeToan(userInfo.getUsername());
                    qOptional.get().setTenKeToan(userInfo.getFullName());
                    qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                    qOptional.get().setNgayPduyet(new Date());
                    break;
                case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                    qOptional.get().setNguoiPduyet(userInfo.getUsername());
                    qOptional.get().setTenNguoiPduyet(userInfo.getFullName());
                    qOptional.get().setLdoTuChoi(stReq.getLyDoTuChoi());
                    qOptional.get().setNgayPduyet(new Date());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
        }
        qOptional.get().setTrangThai(stReq.getTrangThai());
        return hhBbNghiemThuNhapKhacRepository.save(qOptional.get());
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
    @Transactional
    public void xoa(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<HhBbNghiemThuNhapKhac> qOptional = hhBbNghiemThuNhapKhacRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }
        if (!qOptional.get().getTrangThai().equals(Contains.DUTHAO)
                && !qOptional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)) {
            throw new Exception("Chỉ thực hiện xóa với bản ghi ở trạng thái bản nháp hoặc từ chối");
        }
//        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_CTGD"));
//        fileDinhKemService.delete(qOptional.get().getId(), Lists.newArrayList(HhNkPhieuKtcl.TABLE_NAME + "_KTCL"));
//        hhNkPhieuKtclCtRepository.deleteAllByPhieuKtChatLuongId(qOptional.get().getId());
        hhBbNghiemThuNhapKhacRepository.delete(qOptional.get());
    }

    @Override
    public void exportBbNtBq(HhBbNghiemThuNhapKhacSearch req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<HhQdGiaoNvuNhapHangKhacHdr> page = timKiem(req);
        List<HhQdGiaoNvuNhapHangKhacHdr> data = page.getContent();
        String title = "Danh sách lập biên bản nghiệm thu bản quản lần đầu nhập hàng dự trữ quốc gia";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH trước ngày", "Điểm kho", "Ngăn lô kho",
                "Số BB NT kê lót, BQLĐ", "Ngày lập biên bản", "Ngày kết thúc NT kê lót, BQLĐ", "Tổng kinh phí thực tế (đ)", "Tổng kinh phí TC PD (đ)", "Trạng thái"};
        String filename = "Ds_bb_ntbq_lan_dau.xlsx";
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
                    objsc[7] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getNgayTao();
                    objsc[8] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getNgayNghiemThu();
                    objsc[9] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getTongKinhPhiThucTe();
                    objsc[10] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getTongKinhPhiGiao();
                    objsc[11] = qd.getDtlList().get(i).getBbNghiemThuNhapKhacList().get(k).getTenTrangThai();
                    dataList.add(objsc);
                }
            }
            ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
            ex.export();
        }
    }

    @Override
    public List<HhBbNghiemThuNhapKhac> timKiemBbtheoMaNganLo(HhBbNghiemThuNhapKhacSearch objReq) throws Exception {
        return hhBbNghiemThuNhapKhacRepository.findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKhoAndTrangThai(objReq.getIdQdGiaoNvnh(), objReq.getMaLoKho(), objReq.getMaNganKho(), Contains.DADUYET_LDCC);
    }

    @Override
    public List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBbNtBqLd(HhBbNghiemThuNhapKhacSearch objReq) throws Exception {
        List<HhQdGiaoNvuNhapHangKhacHdr> data = hhQdGiaoNvNhapKhacHdrRepository.dsQdNvuDuocLapBbNtBqLd(objReq);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
//        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
//            f.setTenDvi(mapDmucDvi.get(f.getMaDviDxuat()));
        });
        return data;
    }

    @Override
    public ReportTemplateResponse preview(HhBbNghiemThuNhapKhacReq objReq) throws Exception {
        HhBbNghiemThuNhapKhac optional = chiTiet(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        if(optional.getLoaiVthh().startsWith("02")){
            optional.setTenBaoCao("BIÊN BẢN CHUẨN BỊ KHO");
        }else{
            optional.setTenBaoCao("BIÊN BẢN NGHIỆM THU BẢO QUẢN LẦN ĐẦU NHẬP HÀNG DỰ TRỮ QUỐC GIA");
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

    private void luuFile(HhBbNghiemThuNhapKhacReq objReq, HhBbNghiemThuNhapKhac created) {
        fileDinhKemService.delete(created.getId(), Lists.newArrayList(HhBbNghiemThuNhapKhac.TABLE_NAME));
        if (!DataUtils.isNullObject(objReq.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), HhBbNghiemThuNhapKhac.TABLE_NAME);
        }
    }

    private void luuChiTiet(HhBbNghiemThuNhapKhacReq objReq, HhBbNghiemThuNhapKhac created) {
//        if (objReq.getDviChuDongThucHien() != null) {
//            AtomicReference<BigDecimal> tongKinhPhiThucTe = new AtomicReference<>(BigDecimal.ZERO);
//            objReq.getDviChuDongThucHien().forEach(i -> {
//                HhBbNghiemThuNhapKhacDtl dtl = ObjectMapperUtils.map(i, HhBbNghiemThuNhapKhacDtl.class);
//                dtl.setIdHdr(created.getId());
//                dtl.setType(Contains.CHU_DONG);
//                tongKinhPhiThucTe.updateAndGet(v -> v.add(i.getTongGiaTri()));
//                hhBbNghiemThuNhapKhacDtlRepository.save(dtl);
//            });
//            created.setTongKinhPhiThucTe(tongKinhPhiThucTe.get());
//        }
//        if (objReq.getDmTongCucPdTruocThucHien() != null) {
//            AtomicReference<BigDecimal> tongKinhPhiThucTe = new AtomicReference<>(BigDecimal.ZERO);
//            objReq.getDmTongCucPdTruocThucHien().forEach(i -> {
//                HhBbNghiemThuNhapKhacDtl dtl = ObjectMapperUtils.map(i, HhBbNghiemThuNhapKhacDtl.class);
//                dtl.setIdHdr(created.getId());
//                dtl.setType(Contains.PHE_DUYET_TRUOC);
//                tongKinhPhiThucTe.updateAndGet(v -> v.add(i.getTongGiaTri()));
//                hhBbNghiemThuNhapKhacDtlRepository.save(dtl);
//            });
//            created.setTongKinhPhiGiao(tongKinhPhiThucTe.get());
//        }




        hhBbNghiemThuNhapKhacDtlRepository.deleteAllByIdHdr(objReq.getId());
        for (HhBbanNghiemThuDtlReq hhBbanNghiemThuDtlReq : objReq.getDetail()) {
            HhBbNghiemThuNhapKhacDtl ct = new HhBbNghiemThuNhapKhacDtl();
            BeanUtils.copyProperties(hhBbanNghiemThuDtlReq, ct,"id");
            ct.setId(null);
            ct.setIdHdr(created.getId());
            hhBbNghiemThuNhapKhacDtlRepository.save(ct);
        }
        hhBbNghiemThuNhapKhacRepository.save(created);
    }
}
