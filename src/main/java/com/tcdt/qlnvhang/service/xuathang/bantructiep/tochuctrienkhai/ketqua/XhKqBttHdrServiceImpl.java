package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.ketqua;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDdiem;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttTchuc;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttTchucRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttTchucReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class XhKqBttHdrServiceImpl extends BaseServiceImpl implements XhKqBttHdrService {

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;

    @Autowired
    private XhKqBttDtlRepository xhKqBttDtlRepository;

    @Autowired
    private XhKqBttDdiemRepository xhKqBttDdiemRepository;

    @Autowired
    private XhKqBttTchucRepository xhKqBttTchucRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhKqBttHdr> searchPage(XhKqBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhKqBttHdr> data = xhKqBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });
        return data;
    }

    @Override
    public XhKqBttHdr create(XhKqBttHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (!StringUtils.isEmpty(req.getSoQdKq())){
            Optional<XhKqBttHdr> qOptional = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (qOptional.isPresent()) throw new Exception("Số quyết định " + req.getSoQdKq() + " kết quả chào giá đã tồn tại");
        }

        XhKqBttHdr data = new XhKqBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(getUser().getId());
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        data.setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        XhKqBttHdr created =  xhKqBttHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            List<FileDinhKem> FileCanCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhKqBttHdr.TABLE_NAME);
            created.setFileCanCu(FileCanCu);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileQdDaKy())) {
            List<FileDinhKem> fileQdDaKy = fileDinhKemService.saveListFileDinhKem(req.getFileQdDaKy(), created.getId(), XhKqBttHdr.TABLE_NAME+ "_DA_KY");
            created.setFileQdDaKy(fileQdDaKy);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileQd())) {
            List<FileDinhKem> fileQuyetDinh = fileDinhKemService.saveListFileDinhKem(req.getFileQd(), created.getId(), XhKqBttHdr.TABLE_NAME+ "_QUYET_DINH");
            created.setFileQd(fileQuyetDinh);
        }

        this.saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhKqBttHdrReq req, Long idHdr){
        xhKqBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhKqBttDtlReq dtlReq : req.getChildren()){
            XhKqBttDtl dtl = new XhKqBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhKqBttDtlRepository.save(dtl);
            xhKqBttDdiemRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhKqBttDdiemReq ddiemReq: dtlReq.getChildren()){
                XhKqBttDdiem ddiem = new XhKqBttDdiem();
                BeanUtils.copyProperties(ddiemReq, ddiem, "id");
                ddiem.setIdDtl(dtl.getId());
                xhKqBttDdiemRepository.save(ddiem);
                xhKqBttTchucRepository.deleteAllByIdDdiem(ddiemReq.getId());
                for (XhKqBttTchucReq tchucReq : ddiemReq.getChildren()){
                    XhKqBttTchuc tchuc = new XhKqBttTchuc();
                    BeanUtils.copyProperties(tchucReq, tchuc, "id");
                    tchuc.setIdDdiem(ddiem.getId());
                    XhKqBttTchuc create =  xhKqBttTchucRepository.save(tchuc);
                    if (!DataUtils.isNullObject(tchucReq.getFileDinhKems())) {
                        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(tchucReq.getFileDinhKems()), create.getId(), XhKqBttTchuc.TABLE_NAME);
                        create.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
            }
        }
    }

    @Override
    public XhKqBttHdr update(XhKqBttHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Quyết định phê duyệt kết quả chào giá không tồn tại");

        XhKqBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "trangThaiHd", "trangThaiXh");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());
        XhKqBttHdr created = xhKqBttHdrRepository.save(data);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME));
        List<FileDinhKem> FileCanCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhKqBttHdr.TABLE_NAME);
        created.setFileCanCu(FileCanCu);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME + "_DA_KY"));
        List<FileDinhKem> fileQdDaKy = fileDinhKemService.saveListFileDinhKem(req.getFileQdDaKy(), created.getId(), XhKqBttHdr.TABLE_NAME + "_DA_KY");
        created.setFileQdDaKy(fileQdDaKy);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME + "_QUYET_DINH"));
        List<FileDinhKem> fileQuyetDinh = fileDinhKemService.saveListFileDinhKem(req.getFileQd(), created.getId(), XhKqBttHdr.TABLE_NAME + "_QUYET_DINH");
        created.setFileQd(fileQuyetDinh);

       this.saveDetail(req, data.getId());
       return created;
    }

    @Override
    public XhKqBttHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");


        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(id);
        if (!optional.isPresent()) throw new UnsupportedOperationException("Quyết định phê duyệt kết quả chào giá không tồn tại");

        XhKqBttHdr data = optional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
        data.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiXh()));

        List<XhHopDongBttHdr> dataHd = xhHopDongBttHdrRepository.findAllByIdQdKq(id);
        dataHd.forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });
        data.setListHopDongBtt(dataHd);
        List<XhKqBttDtl> xhKqBttDtlList = new ArrayList<>();
        for (XhKqBttDtl dtl : xhKqBttDtlRepository.findAllByIdHdr(id)){
            List<XhKqBttDdiem> xhKqBttDdiemList = new ArrayList<>();
            for (XhKqBttDdiem ddiem : xhKqBttDdiemRepository.findAllByIdDtl(dtl.getId())){
                List<XhKqBttTchuc> xhKqBttTchucList = xhKqBttTchucRepository.findAllByIdDdiem(ddiem.getId());
                for (XhKqBttTchuc xhKqBttTchuc : xhKqBttTchucList){
                    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(xhKqBttTchuc.getId(), Arrays.asList(XhKqBttTchuc.TABLE_NAME));
                    if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                        xhKqBttTchuc.setFileDinhKems(fileDinhKem.get(0));
                    }
                }
                ddiem.setChildren(xhKqBttTchucList);
                ddiem.setTenDiemKho(StringUtils.isEmpty(ddiem.getMaDiemKho())?null:hashMapDvi.get(ddiem.getMaDiemKho()));
                ddiem.setTenNhaKho(StringUtils.isEmpty(ddiem.getMaNhaKho())?null:hashMapDvi.get(ddiem.getMaNhaKho()));
                ddiem.setTenNganKho(StringUtils.isEmpty(ddiem.getMaNganKho())?null:hashMapDvi.get(ddiem.getMaNganKho()));
                ddiem.setTenLoKho(StringUtils.isEmpty(ddiem.getMaLoKho())?null:hashMapDvi.get(ddiem.getMaLoKho()));
                xhKqBttDdiemList.add(ddiem);
            }
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())?null:hashMapDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhKqBttDdiemList);
            xhKqBttDtlList.add(dtl);
        }
        List<FileDinhKem> FileCanCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBttHdr.TABLE_NAME));
        data.setFileCanCu(FileCanCu);

        List<FileDinhKem> fileQdDaKy = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBttHdr.TABLE_NAME + "_DA_KY"));
        data.setFileQdDaKy(fileQdDaKy);

        List<FileDinhKem> fileQuyetDinh = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBttHdr.TABLE_NAME + "_QUYET_DINH"));
        data.setFileQd(fileQuyetDinh);

        data.setChildren(xhKqBttDtlList);
        return data;
    }

    @Override
    public XhKqBttHdr approve(XhKqBttHdrReq req) throws Exception {
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhKqBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        if(req.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
            data.setTrangThaiHd(req.getTrangThai());
        } else {
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    data.setNguoiGuiDuyetId(userInfo.getId());
                    data.setNgayGuiDuyet(LocalDate.now());
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(LocalDate.now());
                    data.setLyDoTuChoi(req.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(LocalDate.now());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(req.getTrangThai());
            if (req.getTrangThai().equals(Contains.BAN_HANH)) {
                Optional<XhQdPdKhBttDtl> qdPdKhBttDtl = xhQdPdKhBttDtlRepository.findById(data.getIdPdKhDtl());
                if (qdPdKhBttDtl.isPresent()){
                    qdPdKhBttDtl.get().setSoQdKq(data.getSoQdKq());
                    qdPdKhBttDtl.get().setIdQdKq(data.getId());
                    xhQdPdKhBttDtlRepository.save(qdPdKhBttDtl.get());
                }
            }
        }
        return xhKqBttHdrRepository.save(data);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhKqBttHdr> optional = xhKqBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (!optional.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
            throw new Exception("Chỉ được xóa bản ghi khi ở trạng thái là dự thảo");
        }
        XhKqBttHdr hdr = optional.get();
        List<XhKqBttDtl> xhKqBttDtlList = xhKqBttDtlRepository.findAllByIdHdr(hdr.getId());
        if (!CollectionUtils.isEmpty(xhKqBttDtlList)){
            for (XhKqBttDtl dtl : xhKqBttDtlList){
                List<XhKqBttDdiem> xhKqBttDdiemList = xhKqBttDdiemRepository.findAllByIdDtl(dtl.getId());
                for (XhKqBttDdiem ddiem : xhKqBttDdiemList){
                    xhKqBttTchucRepository.deleteAllByIdDdiem(ddiem.getId());
                }
                xhKqBttDdiemRepository.deleteAllByIdDtl(dtl.getId());
            }
            xhKqBttDtlRepository.deleteAll(xhKqBttDtlList);
        }
        xhKqBttHdrRepository.delete(hdr);
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME + "_DA_KY"));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhKqBttHdr.TABLE_NAME + "_QUYET_DINH"));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (ObjectUtils.isEmpty(listMulti)){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        listMulti.forEach(item ->{
            try {
                this.delete(item);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void export(XhKqBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhKqBttHdr> page = this.searchPage(req);
        List<XhKqBttHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kết quả chào giá";
        String[] rowsName = new String[]{"STT", "Số QĐ PDKQ chào giá", "Ngày ký QĐ", "Đơn vị", "Số QĐ PDKH bán trực tiếp", "Loại hàng hóa", "Chủng loại hành hóa", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kq-chao-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhKqBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoQdKq();
            objs[2] = hdr.getNgayKy();
            objs[3] = hdr.getTenDvi();
            objs[4] = hdr.getSoQdPd();
            objs[5] = hdr.getTenLoaiVthh();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public XhKqBttTchuc detailToChuc(Long id) throws Exception {
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi.");
        }
        Optional<XhKqBttTchuc> byIdTc = xhKqBttTchucRepository.findById(id);
        if (!byIdTc.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại.");
        }
        XhKqBttTchuc tchuc = byIdTc.get();
        XhKqBttDdiem ddiem = xhKqBttDdiemRepository.findById(tchuc.getIdDdiem()).get();
        tchuc.setXhKqBttDdiem(ddiem);
        return tchuc;
    }
}
