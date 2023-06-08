package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdNvXhBttServiceImpI extends BaseServiceImpl implements XhQdNvXhBttService {

    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;

    @Autowired
    private XhQdNvXhBttDtlRepository xhQdNvXhBttDtlRepository;

    @Autowired
    private XhQdNvXhBttDviRepository xhQdNvXhBttDviRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Override
    public Page<XhQdNvXhBttHdr> searchPage(XhQdNvXhBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdNvXhBttHdr> data = xhQdNvXhBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
            f.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhQdNvXhBttHdr create(XhQdNvXhBttHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (!StringUtils.isEmpty(req.getSoQdNv())){
            List<XhQdNvXhBttHdr> checkSoQd = xhQdNvXhBttHdrRepository.findBySoQdNv(req.getSoQdNv());
            if (!checkSoQd.isEmpty()) throw new Exception("Số quyết định" + req.getSoQdNv() + "đã tồn tại");
        }

        XhHopDongBttHdr dataHd = new XhHopDongBttHdr();
        XhQdPdKhBttHdr dataQdPdKq = new XhQdPdKhBttHdr();
        if (req.getPhanLoai().equals("01")){
            Optional<XhHopDongBttHdr> qOptionalHd = xhHopDongBttHdrRepository.findById(req.getIdHd());
            if (!qOptionalHd.isPresent()){
                throw new Exception("Không tìm thấy hợp đồng bán trực tiếp");
            }
            dataHd = qOptionalHd.get();
        }else {
                Optional<XhQdPdKhBttHdr> qOptionalQdPdKh = xhQdPdKhBttHdrRepository.findById(req.getIdQdPd());
                if (!qOptionalQdPdKh.isPresent()){
                    throw new Exception("Không tìm thấy quyết định phê duyệt kế hoạch bán trực tiếp");
                }
            dataQdPdKq = qOptionalQdPdKh.get();
        }

        XhQdNvXhBttHdr  dataMap = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(LocalDate.now());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
        dataMap.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        dataMap.setMaDvi(userInfo.getDvql());
        if(!ObjectUtils.isEmpty(req.getListMaDviTsan())){
            dataMap.setMaDviTsan(String.join(",",req.getListMaDviTsan()));
        }
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(dataMap);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME+ "_CAN_CU");
            created.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }
        if (req.getPhanLoai().equals("01")){
            dataHd.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            xhHopDongBttHdrRepository.save(dataHd);
        }

        this.saveDetail(req, dataMap.getId());
        return created;
    }

    public void saveDetail(XhQdNvXhBttHdrReq req, Long idHdr){
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdNvXhBttDtlReq dtlReq : req.getChildren()){
            XhQdNvXhBttDtl dtl = new XhQdNvXhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhQdNvXhBttDtlRepository.save(dtl);
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdNvXhBttDviReq dviReq : dtlReq.getChildren()){
                XhQdNvXhBttDvi dvi = new XhQdNvXhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setId(null);
                dvi.setIdDtl(dtl.getId());
                xhQdNvXhBttDviRepository.save(dvi);
            }
        }
    }

    @Override
    public XhQdNvXhBttHdr update(XhQdNvXhBttHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhQdNvXhBttHdr> qOptional = xhQdNvXhBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()) throw new Exception("Không tìm thấy quyết định giao nhiệm vụ xuất hàng bán trực tiếp");

        if (!StringUtils.isEmpty(req.getSoQdNv())) {
            if (!req.getSoQdNv().equals(qOptional.get().getSoQdNv())) {
                List<XhQdNvXhBttHdr> checkSoQdNv = xhQdNvXhBttHdrRepository.findBySoQdNv(req.getSoQdNv());
                if (!checkSoQdNv.isEmpty()) {
                    throw new Exception("Số quyết định " + req.getSoQdNv() + " đã tồn tại");
                }
            }
        }

        XhQdNvXhBttHdr data = qOptional.get();
        if (req.getPhanLoai().equals("01")){
            Optional<XhHopDongBttHdr> qOptionalHd = xhHopDongBttHdrRepository.findById(req.getIdHd());
            if (!qOptionalHd.isPresent()){
                throw new Exception("Không tìm thấy hợp đồng bán trực tiếp");
            }
        }else {
            Optional<XhQdPdKhBttHdr> qOptionalQdNv = xhQdPdKhBttHdrRepository.findById(req.getIdQdPd());
            if (!qOptionalQdNv.isPresent()) {
                throw new Exception("Không tim thấy quyết định giao nhiệm vụ xuất hàng");
            }
        }
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());
        if(!ObjectUtils.isEmpty(req.getListMaDviTsan())){
            data.setMaDviTsan(String.join(",",req.getListMaDviTsan()));
        }

        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdNvXhBttHdr.TABLE_NAME + "_BAN_HANH"));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME + "_CAN_CU");
        created.setFileDinhKem(fileDinhKem);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdNvXhBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, data.getId());
        return created;
    }

    @Override
    public XhQdNvXhBttHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhQdNvXhBttHdr> qOptional = xhQdNvXhBttHdrRepository.findById(id);
        if (!qOptional.isPresent()) throw new UnsupportedOperationException("Không tim thấy quyết định giao nhiệm vụ xuất hàng");

        XhQdNvXhBttHdr data = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
        if (!DataUtils.isNullObject(data.getMaDviTsan())) {
            data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
        }
        List<XhHopDongBttHdr> hopDongBttHdrList = xhHopDongBttHdrRepository.findAllByIdQdNv(id);
        hopDongBttHdrList.forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });
        data.setListHopDongBtt(hopDongBttHdrList);

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdNvXhBttHdr.TABLE_NAME+ "_CAN_CU"));
        data.setFileDinhKem(fileDinhKem);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdNvXhBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);

        List<XhQdNvXhBttDtl> dtlList  = xhQdNvXhBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhQdNvXhBttDtl dtl : dtlList){
            List<XhQdNvXhBttDvi> dviList = xhQdNvXhBttDviRepository.findAllByIdDtl(dtl.getId());
            for (XhQdNvXhBttDvi dvi :dviList){
                dvi.setTenDiemKho(StringUtils.isEmpty(dvi.getMaDiemKho())?null:hashMapDvi.get(dvi.getMaDiemKho()));
                dvi.setTenNhaKho(StringUtils.isEmpty(dvi.getMaNhaKho())?null:hashMapDvi.get(dvi.getMaNhaKho()));
                dvi.setTenNganKho(StringUtils.isEmpty(dvi.getMaNganKho())?null:hashMapDvi.get(dvi.getMaNganKho()));
                dvi.setTenLoKho(StringUtils.isEmpty(dvi.getMaLoKho())?null:hashMapDvi.get(dvi.getMaLoKho()));
            }
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())?null:hashMapDvi.get(dtl.getMaDvi()));
            dtl.setChildren(dviList);
        }
        data.setChildren(dtlList);
        return  data;
    }

    @Override
    public XhQdNvXhBttHdr approve(XhQdNvXhBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
           throw new Exception("Bad request. ");
        }
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhQdNvXhBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        if(req.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
            data.setTrangThaiHd(req.getTrangThai());
        }else {
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    data.setNguoiGuiDuyetId(userInfo.getId());
                    data.setNgayGuiDuyet(LocalDate.now());
                    break;
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(LocalDate.now());
                    data.setLyDoTuChoi(req.getLyDoTuChoi());
                    break;
                case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(req.getTrangThai());
            if (req.getTrangThai().equals(Contains.BAN_HANH) && data.getPhanLoai().equals("01")){
                Optional<XhHopDongBttHdr> optionalHd = xhHopDongBttHdrRepository.findById(data.getIdHd());
                if (optionalHd.isPresent()){
                    optionalHd.get().setSoQdNv(data.getSoQdNv());
                    optionalHd.get().setIdQdNv(data.getId());
                    xhHopDongBttHdrRepository.save(optionalHd.get());
                }
            }
        }
        xhQdNvXhBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }
        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        XhQdNvXhBttHdr hdr = optional.get();
        if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối ");
        }
        List<XhQdNvXhBttDtl> dtlList = xhQdNvXhBttDtlRepository.findAllByIdHdr(id);
        for (XhQdNvXhBttDtl dtl : dtlList){
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhQdNvXhBttDtlRepository.deleteAllByIdHdr(id);
        xhQdNvXhBttHdrRepository.delete(hdr);
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhQdNvXhBttHdr.TABLE_NAME));
        if(hdr.getPhanLoai().equals("01")){
            Optional<XhHopDongBttHdr> hopDongBttHdr = xhHopDongBttHdrRepository.findById(hdr.getIdHd());
            if (hopDongBttHdr.isPresent()){
                hopDongBttHdr.get().setIdHd(null);
                hopDongBttHdr.get().setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
                xhHopDongBttHdrRepository.save(hopDongBttHdr.get());
            }
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }
        List<XhQdNvXhBttHdr> list = xhQdNvXhBttHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhQdNvXhBttHdr hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdNvXhBttHdr> page = this.searchPage(req);
        List<XhQdNvXhBttHdr> data = page.getContent();
        String title="Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT","Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hành hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String filename="danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhQdNvXhBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getNamKh();
            objs[2]=null;
            objs[3]=hdr.getNgayTao();
            objs[4]=hdr.getSoHd();
            objs[5]=hdr.getTenLoaiVthh();
            objs[6]=hdr.getTenCloaiVthh();
            objs[7]=hdr.getTgianGnhan();
            objs[8]=hdr.getTrichYeu();
            objs[9] = null;
            objs[10]= null;
            objs[11]=hdr.getTenTrangThai();
            objs[12]=hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
