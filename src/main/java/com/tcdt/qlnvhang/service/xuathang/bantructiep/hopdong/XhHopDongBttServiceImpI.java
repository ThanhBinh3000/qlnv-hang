package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhHopDongBttServiceImpI extends BaseServiceImpl implements XhHopDongBttService {

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private XhHopDongBttDtlRepository xhHopDongBttDtlRepository;

    @Autowired
    private XhHopDongBttDviRepository xhHopDongBttDviRepository;

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhHopDongBttHdr> searchPage(XhHopDongBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhHopDongBttHdr> page = xhHopDongBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        page.getContent().forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });
        return page;
    }

    @Override
    public XhHopDongBttHdr create(XhHopDongBttHdrReq req) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
        if (DataUtils.isNullObject(req.getIdHd())) {
            if (optional.isPresent()) {
                throw new Exception("Hợp đồng số" + req.getSoHd() + "đã tồn tại");
            }
        }

        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            if(!DataUtils.isNullObject(req.getSoQdPd())){
                Optional<XhQdPdKhBttDtl> checkQdKh = xhQdPdKhBttDtlRepository.findById(req.getIdQdPdDtl());
                if (!checkQdKh.isPresent()){
                    throw new Exception("Số quyết định phê duyệt kế hoạch bán trực tiếp " + checkQdKh.get().getSoQdPd() + "Không tồn tại");
                }else {
                    checkQdKh.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
                    xhQdPdKhBttDtlRepository.save(checkQdKh.get());
                }
            }
        }else {
            if(!DataUtils.isNullObject(req.getSoQdKq())) {
                Optional<XhKqBttHdr> checkQdKq = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
                if (!checkQdKq.isPresent()){
                    throw new Exception("Số quyết định phê duyệt kết quả bán trực tiếp " + req.getSoQdKq() + "Không tồn tại");
                }else {
                    checkQdKq.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
                    xhKqBttHdrRepository.save(checkQdKq.get());
                }
            }
        }

        XhHopDongBttHdr data = new XhHopDongBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setTrangThai(Contains.DU_THAO);
        data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
        data.setMaDvi(userInfo.getDvql());
        data.setNguoiTaoId(getUser().getId());
        data.setNgayTao(LocalDate.now());
        if(!ObjectUtils.isEmpty(req.getListMaDviTsan())){
            data.setMaDviTsan(String.join(",",req.getListMaDviTsan()));
        }

        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getCanCuPhapLy())) {
            List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhHopDongBttHdr.TABLE_NAME+ "_CAN_CU");
            created.setCanCuPhapLy(canCuPhapLy);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }
        if (!DataUtils.isNullOrEmpty(req.getFilePhuLuc())) {
            List<FileDinhKem> filePhuLuc = fileDinhKemService.saveListFileDinhKem(req.getFilePhuLuc(), created.getId(), XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC");
            created.setFilePhuLuc(filePhuLuc);
        }
        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            if(!DataUtils.isNullObject(data.getIdQdPdDtl())){
                Optional<XhQdPdKhBttDtl> xhQdPdKhBttDtl = xhQdPdKhBttDtlRepository.findById(data.getIdQdPdDtl());
                if (xhQdPdKhBttDtl.isPresent()){
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyTt(data.getIdQdPdDtl());
                    xhQdPdKhBttDtl.get().setSlHdChuaKy(slHdChuaKy);
                    xhQdPdKhBttDtlRepository.save(xhQdPdKhBttDtl.get());
                }
            }
          this.saveDetailChiCuc(req, created.getId());
        }else {
            if(!DataUtils.isNullObject(data.getSoQdKq())) {
                Optional<XhKqBttHdr> xhKqBttHdr = xhKqBttHdrRepository.findBySoQdKq(data.getSoQdKq());
                if(xhKqBttHdr.isPresent()){
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKykq(data.getSoQdKq());
                    xhKqBttHdr.get().setSlHdChuaKy(slHdChuaKy);
                    xhKqBttHdrRepository.save(xhKqBttHdr.get());
                }
            }

          this.saveDetailCuc(req, created.getId());
        }


        return created;
    }

//    saveDetail cấp cục
    void saveDetailCuc(XhHopDongBttHdrReq req, Long idHdr) {
            xhHopDongBttDtlRepository.deleteAllByIdHdr(idHdr);
            for (XhHopDongBttDtlReq dtlReq : req.getChildren()) {
                XhHopDongBttDtl dtl = new XhHopDongBttDtl();
                BeanUtils.copyProperties(dtlReq, dtl, "id");
                dtl.setIdHdr(idHdr);
                XhHopDongBttDtl create = xhHopDongBttDtlRepository.save(dtl);
                List<XhHopDongBttDtl> phuLucDtl = xhHopDongBttDtlRepository.findAllByIdHdDtl(dtlReq.getId());
                if (!DataUtils.isNullOrEmpty(phuLucDtl)) {
                    phuLucDtl.forEach(s -> {
                        s.setIdHdDtl(create.getId());
                    });
                    xhHopDongBttDtlRepository.saveAll(phuLucDtl);
                }
                xhHopDongBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
                for (XhHopDongBttDviReq dviReq : dtlReq.getChildren()){
                    XhHopDongBttDvi dvi = new XhHopDongBttDvi();
                    BeanUtils.copyProperties(dviReq, dvi, "id");
                    dvi.setId(null);
                    dvi.setIdDtl(dtl.getId());
                    xhHopDongBttDviRepository.save(dvi);
                }
            }

            for (XhHopDongBttDtlReq phuLucReq : req.getPhuLucDtl()) {
                XhHopDongBttDtl phuLuc = new XhHopDongBttDtl();
                BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
                phuLuc.setId(null);
                phuLuc.setIdHdr(idHdr);
                xhHopDongBttDtlRepository.save(phuLuc);
            }
    }

//     saveDetail cấp chi cục
    void saveDetailChiCuc (XhHopDongBttHdrReq req, Long idHdr) {
        xhHopDongBttDviRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongBttDviReq dviReq : req.getXhHopDongBttDviList()){
            XhHopDongBttDvi dvi = new XhHopDongBttDvi();
            BeanUtils.copyProperties(dviReq, dvi, "id");
            dvi.setIdHdr(idHdr);
            xhHopDongBttDviRepository.save(dvi);
        }
    }

    @Override
    public XhHopDongBttHdr update(XhHopDongBttHdrReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy hợp đồng kế hoạch bán trực tiếp");


        if (DataUtils.isNullObject(req.getIdHd())) {
            if (!optional.get().getSoHd().equals(req.getSoHd())) {
                Optional<XhHopDongBttHdr> qOpHdong = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
                if (qOpHdong.isPresent())
                    throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
            }
            if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
                if(!optional.get().getSoQdPd().equals(req.getSoQdPd())){
                    Optional<XhQdPdKhBttDtl> checkQdKh = xhQdPdKhBttDtlRepository.findById(req.getIdQdPdDtl());
                    if (!checkQdKh.isPresent())
                        throw new Exception("Số quyết định phê duyệt kế hoạch bán trực tiếp " + checkQdKh.get().getSoQdPd() + "Không tồn tại");
                }
            }else {
                if(!optional.get().getSoQdKq().equals(req.getSoQdKq())){
                    Optional<XhKqBttHdr> checkQdkq = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
                    if (!checkQdkq.isPresent())
                        throw new Exception("Số quyết định phê duyệt kết quả bán trực tiếp " + req.getSoQdKq() + "Không tồn tại");
                }
            }
        }
        XhHopDongBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "trangThaiXh", "ngayTao", "NguoiTaoId");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());
        if(!ObjectUtils.isEmpty(req.getListMaDviTsan())){
            data.setMaDviTsan(String.join(",",req.getListMaDviTsan()));
        }

        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(data);
        fileDinhKemService.delete(created.getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(req.getCanCuPhapLy(), created.getId(), XhHopDongBttHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCuPhapLy(canCuPhapLy);

        fileDinhKemService.delete(created.getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        fileDinhKemService.delete(created.getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME + "_BAN_HANH"));
        List<FileDinhKem> filePhuLuc = fileDinhKemService.saveListFileDinhKem(req.getFilePhuLuc(), created.getId(), XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC");
        created.setFilePhuLuc(filePhuLuc);

        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            this.saveDetailChiCuc(req, created.getId());
        }else {
            this.saveDetailCuc(req, created.getId());
        }
        return created;
    }

    @Override
    public XhHopDongBttHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new UnsupportedOperationException("Không tìm thấy hợp đồng kế hoạch bán trực tiếp");
        }

        XhHopDongBttHdr data = optional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            List <XhHopDongBttDvi> dataDtlDvi = xhHopDongBttDviRepository.findAllByIdHdr(id);
            dataDtlDvi.forEach(f ->{
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
                f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
            });
            data.setXhHopDongBttDviList(dataDtlDvi);
        }else {
            List<XhHopDongBttDtl> dataDtlList = new ArrayList<>();
            for (XhHopDongBttDtl dataDtl : xhHopDongBttDtlRepository.findAllByIdHdr(id)){
                List<XhHopDongBttDvi> dataDtlDvi = xhHopDongBttDviRepository.findAllByIdDtl(dataDtl.getId());
                dataDtlDvi.forEach(f ->{
                    f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
                    f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
                });
                dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi())?null:hashMapDvi.get(dataDtl.getMaDvi()));
                dataDtl.setChildren(dataDtlDvi);
//              Phụ lục
                if (!DataUtils.isNullObject(data.getIdHd())) {
                    Optional<XhHopDongBttDtl> byIdHdDtl = xhHopDongBttDtlRepository.findById(dataDtl.getIdHdDtl());
                    if (!DataUtils.isNullObject(byIdHdDtl)) {
                        dataDtl.setTenDviHd(hashMapDvi.get(byIdHdDtl.get().getMaDvi()));
                        dataDtl.setDiaChiHd(byIdHdDtl.get().getDiaChi());
                    }
                }
                List<XhHopDongBttHdr> phuLucList = new ArrayList<>();
                for (XhHopDongBttHdr phuLuc : xhHopDongBttHdrRepository.findAllByIdHd(id)) {
                    List<XhHopDongBttDtl> phuLucDtlList = xhHopDongBttDtlRepository.findAllByIdHdr(phuLuc.getId());
                    phuLucDtlList.forEach(f -> {
                        f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
                    });
                    phuLuc.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(phuLuc.getTrangThai()));
                    phuLuc.setPhuLucDtl(phuLucDtlList);
                    phuLucList.add(phuLuc);
                }
                data.setPhuLuc(phuLucList);

                List<FileDinhKem> filePhuLuc = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME+ "_PHU_LUC"));
                data.setFilePhuLuc(filePhuLuc);
                //  hết Phụ lục
                dataDtlList.add(dataDtl);
            }
            data.setChildren(dataDtlList);
        }

        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        if (!DataUtils.isNullObject(data.getMaDviTsan())) {
            data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
        }

        List<FileDinhKem> canCuPhapLy = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME+ "_CAN_CU"));
        data.setCanCuPhapLy(canCuPhapLy);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);

        return data;
    }


    @Override
    public XhHopDongBttHdr approve(XhHopDongBttHdrReq req) throws Exception {
        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhHopDongBttHdr data = optional.get();
            String status = req.getTrangThai() + data.getTrangThai();
            if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
                data.setNgayGuiDuyet(LocalDate.now());
                data.setNguoiGuiDuyetId(getUser().getId());
                data.setNguoiPduyetId(getUser().getId());
                data.setNgayPduyet(LocalDate.now());

            } else {
                throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(req.getTrangThai());
        XhHopDongBttHdr create = xhHopDongBttHdrRepository.save(data);
        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            if(!DataUtils.isNullObject(data.getIdQdPdDtl())){
                Optional<XhQdPdKhBttDtl> xhQdPdKhBttDtl = xhQdPdKhBttDtlRepository.findById(data.getIdQdPdDtl());
                if (xhQdPdKhBttDtl.isPresent()){
                    Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyTt(data.getIdQdPdDtl());
                    xhQdPdKhBttDtl.get().setSlHdDaKy(slHdongDaKy);
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKyTt(data.getIdQdPdDtl());
                    xhQdPdKhBttDtl.get().setSlHdChuaKy(slHdChuaKy);
                    xhQdPdKhBttDtlRepository.save(xhQdPdKhBttDtl.get());
                }
            }

        }else {
            if(!DataUtils.isNullObject(data.getSoQdKq())) {
                Optional<XhKqBttHdr> xhKqBttHdr = xhKqBttHdrRepository.findBySoQdKq(data.getSoQdKq());
                if(xhKqBttHdr.isPresent()){
                    Integer slHdongDaKy = xhHopDongBttHdrRepository.countSlHopDongDaKyKq(data.getSoQdKq());
                    xhKqBttHdr.get().setSlHdDaKy(slHdongDaKy);
                    Integer slHdChuaKy = xhHopDongBttHdrRepository.countSlHopDongChuaKykq(data.getSoQdKq());
                    xhKqBttHdr.get().setSlHdChuaKy(slHdChuaKy);
                    xhKqBttHdrRepository.save(xhKqBttHdr.get());
                }
            }
        }
        return create;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
        }

        if(Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            xhHopDongBttDviRepository.deleteAllByIdHdr(optional.get().getId());
        }else {
            List<XhHopDongBttDtl> dtlList = xhHopDongBttDtlRepository.findAllByIdHdr(id);
            for (XhHopDongBttDtl dtl : dtlList){
                xhHopDongBttDviRepository.deleteAllByIdDtl(dtl.getId());
            }
            xhHopDongBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        }
        xhHopDongBttHdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME+"_CAN_CU"));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME));
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME+"_PHU_LUC"));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (Objects.isNull(listMulti)) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        for (Long id : listMulti) {
            this.delete(id);
        }
    }

    @Override
    public void export(XhHopDongBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhHopDongBttHdr> page = this.searchPage(req);
        List<XhHopDongBttHdr> data = page.getContent();
        String title = "Danh sách hợp đồng bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Số hợp đồng", "Tên hợp đồng", "Ngày ký hợp đồng", "Mã đơn vị tài sản", "Số lượng (Kg)", "Thành tiền đã bao gồm thuế VAT(đ)", "Trạng thái HD", "Số QĐ giao NV XH", "Trạng thái XH"};
        String fileName = "danh-sach-dx-pd-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhHopDongBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getSoHd();
            objs[2] = hdr.getTenHd();
            objs[3] = hdr.getNgayHluc();
            objs[4] = hdr.getMaDviTsan();
            objs[5] = hdr.getSoLuongBanTrucTiep();
            objs[6] = null;
            objs[7] = hdr.getTenTrangThai();
            objs[8] = null;
            objs[9] = hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
