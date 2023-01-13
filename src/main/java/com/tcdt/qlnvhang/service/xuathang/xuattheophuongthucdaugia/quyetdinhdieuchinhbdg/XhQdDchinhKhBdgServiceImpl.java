package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet.XhQdPdKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet.XhQdPdKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhQdDchinhKhBdgServiceImpl extends BaseServiceImpl implements XhQdDchinhKhBdgService {

    @Autowired
    private XhQdDchinhKhBdgHdrRepository xhQdDchinhKhBdgHdrRepository;

    @Autowired
    private XhQdDchinhKhBdgDtlRepository xhQdDchinhKhBdgDtlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlRepository xhQdDchinhKhBdgPlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlDtlRepository xhQdDchinhKhBdgPlDtlRepository;

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private KeHoachService keHoachService;

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

    @Override
    public Page<XhQdDchinhKhBdgHdr> searchPage(XhQdDchinhKhBdgReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdDchinhKhBdgHdr> data = xhQdDchinhKhBdgHdrRepository.searchPage(
                req.getNamKh(),
                req.getSoQdDc(),
                req.getTrichYeu(),
                Contains.convertDateToString(req.getNgayKyQdTu()),
                Contains.convertDateToString(req.getNgayKyQdDen()),
                req.getSoTrHdr(),
                req.getLoaiVthh(),
                req.getTrangThai(),
                req.getMaDvi(),
                pageable);
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public List<XhQdDchinhKhBdgHdr> searchAll(XhQdDchinhKhBdgReq req) {
        return null;
    }

    @Override
    public XhQdDchinhKhBdgHdr create(XhQdDchinhKhBdgReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();

        List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdPd());
        if (!checkSoQd.isEmpty()) {
            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
        }

        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBdgHdr.class);
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setMaDvi(userInfo.getDvql());
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        saveDetail(req, dataMap);
        return dataMap;
    }

    void saveDetail(XhQdDchinhKhBdgReq req, XhQdDchinhKhBdgHdr dataMap) {
        xhQdDchinhKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
        for (XhQdPdKhBdgDtlReq dx : req.getDsDeXuat()) {
            XhQdDchinhKhBdgDtl qd = ObjectMapperUtils.map(dx, XhQdDchinhKhBdgDtl.class);
            xhQdDchinhKhBdgPlRepository.deleteByIdQdDtl(qd.getId());
            qd.setId(null);
            qd.setIdQdHdr(dataMap.getId());
            qd.setTrangThai(Contains.CHUACAPNHAT);
            xhQdDchinhKhBdgDtlRepository.save(qd);
            for (XhQdPdKhBdgPlReq gtList : dx.getChildren()) {
                XhQdDchinhKhBdgPl gt = ObjectMapperUtils.map(gtList, XhQdDchinhKhBdgPl.class);
                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(gt.getId());
                gt.setId(null);
                gt.setIdQdDtl(qd.getId());
                gt.setTrangThai(Contains.CHUACAPNHAT);
                xhQdDchinhKhBdgPlRepository.save(gt);
                for (XhQdPdKhBdgPlDtlReq ddNhap : gtList.getChildren()) {
                    XhQdDchinhKhBdgPlDtl dataDdNhap = new ModelMapper().map(ddNhap, XhQdDchinhKhBdgPlDtl.class);
                    dataDdNhap.setId(null);
                    dataDdNhap.setIdQdHdr(dataMap.getId());
                    dataDdNhap.setIdPhanLo(gt.getId());
                    xhQdDchinhKhBdgPlDtlRepository.save(dataDdNhap);
                }
            }
        }
    }

    @Override
    public XhQdDchinhKhBdgHdr update(XhQdDchinhKhBdgReq req) throws Exception {
        return null;
    }

    @Override
    public XhQdDchinhKhBdgHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi");
        }
        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        List<XhQdDchinhKhBdgDtl> xhQdPdKhBdgDtlList = new ArrayList<>();
        for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlRepository.findAllByIdQdHdr(id)) {
            List<XhQdDchinhKhBdgPl> xhQdPdKhBdgPlList = new ArrayList<>();
            for (XhQdDchinhKhBdgPl dsg : xhQdDchinhKhBdgPlRepository.findByIdQdDtl(dtl.getId())) {
                List<XhQdDchinhKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdDchinhKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
                xhQdPdKhBdgPlDtlList.forEach(f -> {
                    f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
                    f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                    f.setTenNhakho(mapDmucDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
                    f.setTenLoaiVthh(hashMapDmHh.get(f.getLoaiVthh()));
                    f.setTenCloaiVthh(hashMapDmHh.get(f.getCloaiVthh()));
                });
                dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
                dsg.setTenDiemKho(mapDmucDvi.get(dsg.getMaDiemKho()));
                dsg.setTenNhakho(mapDmucDvi.get(dsg.getMaNhaKho()));
                dsg.setTenNganKho(mapDmucDvi.get(dsg.getMaNganKho()));
                dsg.setTenLoKho(mapDmucDvi.get(dsg.getMaLoKho()));
                dsg.setTenLoaiVthh(hashMapLoaiHdong.get(dsg.getTenLoaiVthh()));
                dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
                dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
                dsg.setChildren(xhQdPdKhBdgPlDtlList);
                xhQdPdKhBdgPlList.add(dsg);
            }
            ;
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhQdPdKhBdgPlList);
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            xhQdPdKhBdgDtlList.add(dtl);
        }
        qOptional.get().setChildren(xhQdPdKhBdgDtlList);
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
        return qOptional.get();
    }

    @Override
    public XhQdDchinhKhBdgHdr approve(XhQdDchinhKhBdgReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
        }

        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }

        List<XhQdDchinhKhBdgDtl> xhQdPdKhBdgDtl = xhQdDchinhKhBdgDtlRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(xhQdPdKhBdgDtl)) {
            for (XhQdDchinhKhBdgDtl dtl : xhQdPdKhBdgDtl) {
                List<XhQdDchinhKhBdgPl> byIdQdDtl = xhQdDchinhKhBdgPlRepository.findByIdQdDtl(dtl.getId());
                for (XhQdDchinhKhBdgPl phanLo : byIdQdDtl) {
                    xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(phanLo.getId());
                }
                xhQdDchinhKhBdgPlRepository.deleteByIdQdDtl(dtl.getId());
            }
            xhQdDchinhKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtl);
        }
        xhQdDchinhKhBdgHdrRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        for (Long id : listMulti) {
            this.delete(id);
        }
    }

    @Override
    public void export(XhQdDchinhKhBdgReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdDchinhKhBdgHdr> page = this.searchPage(req);
        List<XhQdDchinhKhBdgHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BĐG", "ngày ký QĐ", "Trích yếu", "Số KH/ Tờ trình", "Mã tổng hợp", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Trạng Thái"};
        String fileName = "danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBdgHdr pduyet = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = pduyet.getNamKh();
            objs[2] = pduyet.getSoQdPd();
            objs[3] = pduyet.getNgayKyQd();
            objs[4] = pduyet.getTrichYeu();
            objs[5] = pduyet.getSoTrHdr();
            objs[6] = pduyet.getIdThHdr();
            objs[7] = pduyet.getTenLoaiVthh();
            objs[8] = pduyet.getTenCloaiVthh();
            objs[9] = pduyet.getSoDviTsan();
            objs[10] = pduyet.getSlHdDaKy();
            objs[11] = pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

//
//    @Transactional
//    public XhQdDchinhKhBdgHdr createLuongThuc (XhQdDchinhKhBdgReq req) throws Exception{
//        UserInfo userInfo= SecurityContextService.getUser();
//        if (req.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(req.getLoaiVthh()))
//            throw new Exception("Loại vật tư hàng hóa không phù hợp");
//
//        if(!StringUtils.isEmpty(req.getSoQdPd())){
//            List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdPd());
//            if (!checkSoQd.isEmpty()) {
//                throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
//            }
//        }
//
//        if (req.getPhanLoai().equals("TH")){
//            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
//            if (!qOptionalTh.isPresent()){
//                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
//            }
//        }else {
//            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
//            if(!byId.isPresent()){
//                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
//            }
//        }
//        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBdgHdr.class);
//        dataMap.setTrangThai(Contains.DUTHAO);
//        dataMap.setLastest(req.getLastest());
//        dataMap.setMaDvi(userInfo.getDepartment());
//        XhQdDchinhKhBdgHdr created=xhQdDchinhKhBdgHdrRepository.save(dataMap);
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(),dataMap.getId(),XhQdDchinhKhBdgHdr.TABLE_NAME);
//        created.setFileDinhKems(fileDinhKems);
//
//        // Update trạng thái tổng hợp dxkhclnt
//        if(req.getPhanLoai().equals("TH")){
//            xhThopDxKhBdgRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
//        }else{
////            xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(req.getSoTrHdr()), Contains.DADUTHAO_QD);
////            xhDxKhBanDauGiaRepository.updateSoQdPd(Arrays.asList(req.getSoTrHdr()),dataMap.getSoQdPd());
////            xhDxKhBanDauGiaRepository.updateNgayKyQd(Arrays.asList(req.getSoTrHdr()),dataMap.getNgayKyQd());
//        }
//
//        saveDetail(req,dataMap);
//        return created;
//    }
//
//    @Transactional
//    XhQdDchinhKhBdgHdr createVatTu(XhQdDchinhKhBdgReq req) throws Exception{
//        UserInfo userInfo= SecurityContextService.getUser();
//
//        List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdPd());
//        if (!checkSoQd.isEmpty()) {
//            throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
//        }
//
//        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBdgHdr.class);
//        dataMap.setTrangThai(Contains.DUTHAO);
//        dataMap.setLastest(req.getLastest());
//        dataMap.setMaDvi(userInfo.getDepartment());
//        XhQdDchinhKhBdgHdr created=xhQdDchinhKhBdgHdrRepository.save(dataMap);
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(),dataMap.getId(),XhQdDchinhKhBdgHdr.TABLE_NAME);
//        created.setFileDinhKems(fileDinhKems);
//
//        saveDetail(req,dataMap);
//
//        return created;
//    }
//
//
//    public void validateData(XhQdDchinhKhBdgHdr objHdr) throws Exception{
//        for (XhQdDchinhKhBdgDtl dtl : objHdr.getChildren()){
//            for (XhQdDchinhKhBdgPl dsgthau : dtl.getChildren()){
//                BigDecimal aLong =xhDxKhBanDauGiaRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
//                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKh(),objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP" );
//                if (soLuongTotal.compareTo(nhap) >0){
//                    throw new Exception(dsgthau.getTenDvi()+ "Đã nhập quá số lượng chỉ tiêu vui lòng nhập lại");
//                }
//            }
//        }
//    }
//
//    @Transactional
//    public XhQdDchinhKhBdgHdr update(XhQdDchinhKhBdgReq req) throws Exception {
//        // Vật tư
//        if(req.getLoaiVthh().startsWith("02")){
//            return updateVatTu(req);
//        }else{
//            // Lương thực
//            return updateLuongThuc(req);
//        }
//    }
//
//    @Transactional
//    public XhQdDchinhKhBdgHdr updateLuongThuc(XhQdDchinhKhBdgReq req) throws Exception{
//        if (StringUtils.isEmpty(req.getId())){
//            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//        }
//
//        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(req.getId());
//        if (!qOptional.isPresent()){
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//
//        if(!StringUtils.isEmpty(req.getSoQdPd())){
//            if (!req.getSoQdPd().equals(qOptional.get().getSoQdPd())) {
//                List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdPd());
//                if (!checkSoQd.isEmpty()) {
//                    throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
//                }
//            }
//        }
//
//        if (req.getPhanLoai().equals("TH")){
//            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(req.getIdThHdr());
//            if (!qOptionalTh.isPresent()){
//                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
//            }
//        }else {
//            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(req.getIdTrHdr());
//            if (!byId.isPresent()){
//                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
//            }
//        }
//
//        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
//        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBdgHdr.class);
//
//        updateObjectToObject(dataDB, dataMap);
//
//        xhQdDchinhKhBdgHdrRepository.save(dataDB);
//        this.saveDetail(req,dataMap);
//        return dataDB;
//
//    }
//
//    @Transactional
//    public XhQdDchinhKhBdgHdr updateVatTu(XhQdDchinhKhBdgReq req) throws Exception{
//        if (StringUtils.isEmpty(req.getId())){
//            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
//        }
//
//        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(req.getId());
//        if (!qOptional.isPresent()){
//            throw new Exception("Không tìm thấy dữ liệu cần sửa");
//        }
//
//        if (!qOptional.get().getSoQdPd().equals(req.getSoQdPd())){
//            List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdPd());
//            if (!checkSoQd.isEmpty()){
//                throw new Exception("Số quết định" + req.getSoQdPd() + " đã tồn tại" );
//            }
//        }
//        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
//        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(req, XhQdDchinhKhBdgHdr.class);
//
//        updateObjectToObject(dataDB, dataMap);
//
//        xhQdDchinhKhBdgHdrRepository.save(dataDB);
//
//        xhQdDchinhKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
//        XhQdDchinhKhBdgDtl qdDtl = new XhQdDchinhKhBdgDtl();
//        qdDtl.setId(null);
//        qdDtl.setIdQdHdr(dataDB.getId());
//        qdDtl.setMaDvi(getUser().getDvql());
//        qdDtl.setTrangThai(Contains.CHUACAPNHAT);
//        xhQdDchinhKhBdgDtlRepository.save(qdDtl);
//
//        this.saveDetail(req, dataDB);
//        return dataDB;
//    }
//
//    public XhQdDchinhKhBdgHdr detail (String ids) throws Exception {

//    }
//

//
//    public XhQdDchinhKhBdgHdr approve(StatusReq stReq) throws Exception{
//        if (StringUtils.isEmpty(stReq.getId())){
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        XhQdDchinhKhBdgHdr detail = detail(String.valueOf(stReq.getId()));
//        if(detail.getLoaiVthh().startsWith("02")){
//            return this.approveVatTu(stReq,detail);
//        }else{
//            return this.approveLT(stReq,detail);
//        }
//    }
//
//    @Transactional(rollbackOn = Exception.class)
//    XhQdDchinhKhBdgHdr approveVatTu(StatusReq stReq, XhQdDchinhKhBdgHdr dataDB) throws Exception {
//        String status = stReq.getTrangThai() + dataDB.getTrangThai();
//        switch (status) {
//            case Contains.CHODUYET_LDV + Contains.DUTHAO:
//            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
//                dataDB.setNguoiGuiDuyet(getUser().getUsername());
//                dataDB.setNgayGuiDuyet(getDateTimeNow());
//            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
//                dataDB.setNguoiPduyet(getUser().getUsername());
//                dataDB.setNgayPduyet(getDateTimeNow());
//                dataDB.setLdoTuchoi(stReq.getLyDo());
//            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
//            case Contains.BAN_HANH + Contains.DADUYET_LDV:
//                dataDB.setNguoiPduyet(getUser().getUsername());
//                dataDB.setNgayPduyet(getDateTimeNow());
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công");
//        }
//        dataDB.setTrangThai(stReq.getTrangThai());
//        if (stReq.getTrangThai().equals(Contains.BAN_HANH)){
//            Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
//            if (qOptional.isPresent()){
//                if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
//                    throw new Exception("Đề xuất này đã được quyết định ");
//                }
//                // Update trạng thái tờ trình
////                xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
//            }else {
//                throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
//            }
////            this.cloneProject(dataDB.getId());
////            this.cloneForToChucBdg(dataDB);
//        }
//        XhQdDchinhKhBdgHdr createCheck = xhQdDchinhKhBdgHdrRepository.save(dataDB);
//        return createCheck;
//    }
//
//    @Transactional(rollbackOn = Exception.class)
//    XhQdDchinhKhBdgHdr approveLT(StatusReq stReq, XhQdDchinhKhBdgHdr dataDB) throws Exception{
//        String status = stReq.getTrangThai() + dataDB.getTrangThai();
//        switch (status) {
//            case Contains.BAN_HANH + Contains.DUTHAO:
//                dataDB.setNguoiPduyet(getUser().getUsername());
//                dataDB.setNgayPduyet(getDateTimeNow());
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công");
//        }
//        dataDB.setTrangThai(stReq.getTrangThai());
//        if (stReq.getTrangThai().equals(Contains.BAN_HANH)){
//            if (dataDB.getPhanLoai().equals("TH")){
//                Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
//                if (qOptional.isPresent()){
//                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
//                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
//                    }
//                    xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
//                }else {
//                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
//                }
//            }else {
//                Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
//                if (qOptional.isPresent()){
//                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
//                        throw new Exception("Đề xuất này đã được quyết định");
//                    }
//                  // Update trạng thái tờ trình
////                    xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
//                }else {
//                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
//                }
//            }
//      /*      this.cloneProject(dataDB.getId());
//            this.cloneForToChucBdg(dataDB);*/
////            this.validateData(dataDB);
//        }
//        XhQdDchinhKhBdgHdr createCheck = xhQdDchinhKhBdgHdrRepository.save(dataDB);
//        return createCheck;
//    }
//
//    private void cloneProject(Long idClone) throws Exception {
//        XhQdDchinhKhBdgHdr hdr = this.detail(idClone.toString());
//        XhQdDchinhKhBdgHdr hdrClone = new XhQdDchinhKhBdgHdr();
//        BeanUtils.copyProperties(hdr, hdrClone);
//        hdrClone.setId(null);
//        hdrClone.setLastest(true);
//        hdrClone.setIdGoc(hdr.getId());
//        xhQdDchinhKhBdgHdrRepository.save(hdrClone);
//        for (XhQdDchinhKhBdgDtl dx : hdr.getChildren()){
//            XhQdDchinhKhBdgDtl dxClone = new XhQdDchinhKhBdgDtl();
//            BeanUtils.copyProperties(dx, dxClone);
//            dxClone.setId(null);
//            dxClone.setIdQdHdr(hdrClone.getId());
//            xhQdDchinhKhBdgDtlRepository.save(dxClone);
//            for (XhQdDchinhKhBdgPl phanLo : dx.getChildren()){
//                XhQdDchinhKhBdgPl phanLoClone = new XhQdDchinhKhBdgPl();
//                BeanUtils.copyProperties(phanLo, phanLoClone);
//                phanLoClone.setId(null);
//                phanLoClone.setIdQdDtl(dxClone.getId());
//                xhQdDchinhKhBdgPlRepository.save(phanLoClone);
//                for (XhQdDchinhKhBdgPlDtl dsDdNhap : phanLoClone.getChildren()){
//                    XhQdDchinhKhBdgPlDtl dsDdNhapClone = new XhQdDchinhKhBdgPlDtl();
//                    BeanUtils.copyProperties(dsDdNhap, dsDdNhapClone);
//                    dsDdNhapClone.setId(null);
//                    dsDdNhapClone.setIdPhanLo(phanLoClone.getId());
//                    xhQdDchinhKhBdgPlDtlRepository.save(dsDdNhapClone);
//                }
//            }
//        }
//    }

}
