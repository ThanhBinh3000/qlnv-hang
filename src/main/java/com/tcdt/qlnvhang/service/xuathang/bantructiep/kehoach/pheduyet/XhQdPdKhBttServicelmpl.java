package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhQdPdKhBttServicelmpl extends BaseServiceImpl implements XhQdPdKhBttService {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private XhQdPdKhBttDviRepository xhQdPdKhBttDviRepository;

    @Autowired
    private XhQdPdKhBttDviDtlRepository xhQdPdKhBttDviDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;

    @Autowired
    private XhThopDxKhBttDtlRepository xhThopDxKhBttDtlRepository;

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Override
    public Page<XhQdPdKhBttHdr> searchPage(XhQdPdKhBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBttHdr> data = xhQdPdKhBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())?null:hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapVthh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            List<XhQdPdKhBttDtl> dtlList = xhQdPdKhBttDtlRepository.findAllByIdQdHdr(f.getId());
            dtlList.forEach(s ->{
                s.setTenDvi(hashMapDvi.get(s.getMaDvi()));
            });
            f.setChildren(dtlList);
        });
        return data;
    }

    @Override
    public XhQdPdKhBttHdr create(XhQdPdKhBttHdrReq req) throws Exception {
        if(req == null) return null;

        if (!StringUtils.isEmpty(req.getSoQdPd())){
            Optional<XhQdPdKhBttHdr> checkSoQd = xhQdPdKhBttHdrRepository.findBySoQdPd(req.getSoQdPd());
            if (checkSoQd.isPresent()) throw new Exception("Số quyết định" + req.getSoQdPd() + " đã tồn tại ");
        }

        XhThopDxKhBttHdr dataTh = new XhThopDxKhBttHdr();
        XhDxKhBanTrucTiepHdr dataDx = new XhDxKhBanTrucTiepHdr();
        if (req.getPhanLoai().equals("TH")) {
            Optional<XhThopDxKhBttHdr> qOptionalTh = xhThopDxKhBttRepository.findById(req.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán trực tiếp");
            }
            dataTh = qOptionalTh.get();
        }else {
            Optional<XhDxKhBanTrucTiepHdr> qOptionalDx = xhDxKhBanTrucTiepHdrRepository.findById(req.getIdTrHdr());
            if (!qOptionalDx.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán trực tiếp");
            }
            dataDx = qOptionalDx.get();
        }

        XhQdPdKhBttHdr data = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(LocalDate.now());
        data.setNguoiTaoId(getUser().getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setLastest(false);
        data.setMaDvi(getUser().getDvql());
        XhQdPdKhBttHdr created =  xhQdPdKhBttHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME+ "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        if (req.getPhanLoai().equals("TH")){
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            dataTh.setSoQdPd(data.getSoQdPd());
            dataTh.setIdSoQdPd(data.getId());
            xhThopDxKhBttRepository.save(dataTh);
        }else {
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            xhDxKhBanTrucTiepHdrRepository.save(dataDx);
        }

        saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhQdPdKhBttHdrReq req, Long idQdHdr){
        xhQdPdKhBttDtlRepository.deleteAllByIdQdHdr(idQdHdr);
        for (XhQdPdKhBttDtlReq dtlReq : req.getChildren()){
            XhQdPdKhBttDtl dtl = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdQdHdr(idQdHdr);
            dtl.setSoQdPd(req.getSoQdPd());
            dtl.setTrangThai(Contains.CHUACAPNHAT);
            xhQdPdKhBttDtlRepository.save(dtl);
            xhQdPdKhBttDviRepository.deleteByIdQdDtl(dtlReq.getId());
            for (XhQdPdKhBttDviReq dviReq : dtlReq.getChildren()){
                XhQdPdKhBttDvi dvi = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setIdQdDtl(dtl.getId());
                xhQdPdKhBttDviRepository.save(dvi);
                xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dviReq.getId());
                for (XhQdPdKhBttDviDtlReq dviDtlReq : dviReq.getChildren()) {
                    XhQdPdKhBttDviDtl dviDtl = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(dviDtlReq, dviDtl, "id");
                    dviDtl.setIdDvi(dvi.getId());
                    xhQdPdKhBttDviDtlRepository.save(dviDtl);
                }
            }
        }
    }

    @Override
    public XhQdPdKhBttHdr update(XhQdPdKhBttHdrReq req) throws Exception {
        if (req == null) return null;

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdPdKhBttHdr> qOptional = xhQdPdKhBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        XhQdPdKhBttHdr data = qOptional.get();
        if (req.getPhanLoai().equals("TH")){
            Optional<XhThopDxKhBttHdr> qOptionalTh = xhThopDxKhBttRepository.findById(req.getIdThHdr());
            XhThopDxKhBttHdr dataTh = qOptionalTh.get();
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán trực tiếp");
            }else {
                dataTh.setSoQdPd(req.getSoQdPd());
                dataTh.setIdSoQdPd(data.getId());
                xhThopDxKhBttRepository.save(dataTh);
            }
        }

        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(LocalDate.now());
        data.setNguoiSuaId(getUser().getId());
        XhQdPdKhBttHdr created =  xhQdPdKhBttHdrRepository.save(data);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH"));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME + "_BAN_HANH");
        created.setFileDinhKem(fileDinhKem);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdPdKhBttHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdPdKhBttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, data.getId());
        return created;
    }

    @Override
    public XhQdPdKhBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi");
        }

        Optional<XhQdPdKhBttHdr> qOptional = xhQdPdKhBttHdrRepository.findById(id);
        if (!qOptional.isPresent()){
           throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhQdPdKhBttHdr data = qOptional.get();

        List<XhQdPdKhBttDtl> dataDtlList = new ArrayList<>();
        for (XhQdPdKhBttDtl dataDtl : xhQdPdKhBttDtlRepository.findAllByIdQdHdr(id)){
            List<XhQdPdKhBttDvi> dataDtlDviList = new ArrayList<>();
            for (XhQdPdKhBttDvi dataDtlDvi : xhQdPdKhBttDviRepository.findAllByIdQdDtl(dataDtl.getId())) {
                List<XhQdPdKhBttDviDtl> dataDtlDviDtlList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDtlDvi.getId());
                dataDtlDviDtlList.forEach(f ->{
                    f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())?null:hashMapDvi.get(f.getMaDiemKho()));
                    f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())?null:hashMapDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())?null:hashMapDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())?null:hashMapDvi.get(f.getMaLoKho()));
                });
                dataDtlDvi.setTenDvi(StringUtils.isEmpty(dataDtlDvi.getMaDvi())?null:hashMapDvi.get(dataDtlDvi.getMaDvi()));
                dataDtlDvi.setChildren(dataDtlDviDtlList);
                dataDtlDviList.add(dataDtlDvi);
            }
            dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi())?null:hashMapDvi.get(dataDtl.getMaDvi()));
            dataDtl.setChildren(dataDtlDviList);
            dataDtlList.add(dataDtl);
        }
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi())?null:hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME+ "_BAN_HANH"));
        data.setFileDinhKem(fileDinhKem);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);

        data.setChildren(dataDtlList);
        return data;
    }

    @Override
    public XhQdPdKhBttHdr approve(XhQdPdKhBttHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhQdPdKhBttHdr data = detail(req.getId());
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                data.setNgayPduyet(LocalDate.now());
                data.setNguoiPduyetId(getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        if (req.getTrangThai().equals(Contains.BAN_HANH)) {
            if (data.getPhanLoai().equals("TH")) {
                Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(data.getIdThHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    xhThopDxKhBttRepository.updateTrangThai(data.getIdThHdr(), Contains.DABANHANH_QD);
//                    List<XhThopDxKhBttDtl> dtlList = xhThopDxKhBttDtlRepository.findByIdThopHdr(data.getIdThHdr());
//                    for (XhThopDxKhBttDtl dtl : dtlList){
//                        Optional<XhDxKhBanTrucTiepHdr> optionalDx = xhDxKhBanTrucTiepHdrRepository.findById(dtl.getIdDxHdr());
//                        if (optionalDx.isPresent()){
//                            optionalDx.get().setSoQdPd(data.getSoQdPd());
//                            optionalDx.get().setIdSoQdPd(data.getId());
//                            optionalDx.get().setNgayKyQd(data.getNgayPduyet());
//                            xhDxKhBanTrucTiepHdrRepository.save(optionalDx.get());
//                        }
//                    }
                } else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }
            }
            else {
                Optional<XhDxKhBanTrucTiepHdr> qOptional = xhDxKhBanTrucTiepHdrRepository.findById(data.getIdTrHdr());
                if (qOptional.isPresent()) {
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                    qOptional.get().setTrangThaiTh(Contains.DABANHANH_QD);
                    qOptional.get().setSoQdPd(data.getSoQdPd());
                    qOptional.get().setIdSoQdPd(data.getId());
                    qOptional.get().setNgayKyQd(data.getNgayPduyet());
                    xhDxKhBanTrucTiepHdrRepository.save(qOptional.get());
                } else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }

            this.cloneProject(data.getId());
        }
        XhQdPdKhBttHdr createCheck = xhQdPdKhBttHdrRepository.save(data);
        return createCheck;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
        }
        Optional<XhQdPdKhBttHdr> optional = xhQdPdKhBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }

        XhQdPdKhBttHdr data = optional.get();
        List<XhQdPdKhBttDtl> dataQdDtlList = xhQdPdKhBttDtlRepository.findAllByIdQdHdr(data.getId());
        if (!CollectionUtils.isEmpty(dataQdDtlList)){
            for (XhQdPdKhBttDtl dataQdDtl : dataQdDtlList){
                List<XhQdPdKhBttDvi> dataQdDviList = xhQdPdKhBttDviRepository.findAllByIdQdDtl(dataQdDtl.getId());
                for (XhQdPdKhBttDvi dataQdDvi : dataQdDviList ){
                    xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dataQdDvi.getId());
                }
                xhQdPdKhBttDviRepository.deleteByIdQdDtl(dataQdDtl.getId());
            }
            xhQdPdKhBttDtlRepository.deleteAll(dataQdDtlList);
        }
        xhQdPdKhBttHdrRepository.delete(data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdPdKhBttHdr.TABLE_NAME));
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdPdKhBttHdr.TABLE_NAME+"_BAN_HANH"));
        if (data.getPhanLoai().equals("TH")){
            Optional<XhThopDxKhBttHdr> qOptionalTh = xhThopDxKhBttRepository.findById(data.getIdThHdr());
            XhThopDxKhBttHdr dataTh = qOptionalTh.get();
            dataTh.setIdSoQdPd(null);
            dataTh.setSoQdPd(null);
            dataTh.setTrangThai(NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
            xhThopDxKhBttRepository.save(dataTh);
        }else {
            Optional<XhDxKhBanTrucTiepHdr> qOptionalTr = xhDxKhBanTrucTiepHdrRepository.findById(data.getIdTrHdr());
            qOptionalTr.get().setTrangThaiTh(NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
            xhDxKhBanTrucTiepHdrRepository.save(qOptionalTr.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }
        List<XhQdPdKhBttHdr> listHdr = xhQdPdKhBttHdrRepository.findAllByIdIn(listMulti);
        if (listHdr.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        for (XhQdPdKhBttHdr hdr : listHdr){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái dự thảo");
            }else {
                this.delete(hdr.getId());
            }
        }
    }

    @Override
    public void export(XhQdPdKhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttHdr> page = this.searchPage(req);
        List<XhQdPdKhBttHdr> data = page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch bán trưc tiếp";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BTT", "Ngày ký QĐ", "Trích yếu", "Số KH/Tờ trình", "Mã tổng hợp", "Loại hàng hóa", "Chủng loại hành hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Trạng thái"};
        String fileName = "danh-sach-dx-pd-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNamKh();
            objs[2] = hdr.getSoQdPd();
            objs[3] = hdr.getNgayKyQd();
            objs[4] = hdr.getTrichYeu();
            objs[5] = hdr.getSoTrHdr();
            objs[6] = hdr.getIdThHdr();
            objs[7] = hdr.getTenLoaiVthh();
            objs[8] = hdr.getTenCloaiVthh();
            objs[9] = hdr.getSlDviTsan();
            objs[10] = hdr.getSoHopDong();
            objs[11] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    private void cloneProject(Long idClone) throws Exception {
        XhQdPdKhBttHdr hdr = this.detail(idClone);
        XhQdPdKhBttHdr hdrClone = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(hdr, hdrClone);
        hdrClone.setId(null);
        hdrClone.setLastest(true);
        hdrClone.setIdGoc(hdr.getId());
        xhQdPdKhBttHdrRepository.save(hdrClone);
        for (XhQdPdKhBttDtl dx : hdr.getChildren()) {
            XhQdPdKhBttDtl dxClone = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dx, dxClone);
            dxClone.setId(null);
            dxClone.setIdQdHdr(hdrClone.getId());
            xhQdPdKhBttDtlRepository.save(dxClone);
            for (XhQdPdKhBttDvi pl : dxClone.getChildren()) {
                XhQdPdKhBttDvi plClone = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(pl, plClone);
                plClone.setId(null);
                plClone.setIdQdDtl(dxClone.getId());
                xhQdPdKhBttDviRepository.save(plClone);
                for (XhQdPdKhBttDviDtl plDtl : pl.getChildren()) {
                    XhQdPdKhBttDviDtl plDtlClone = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(plDtl, plDtlClone);
                    plDtlClone.setId(null);
                    plDtlClone.setIdDvi(plClone.getId());
                    xhQdPdKhBttDviDtlRepository.save(plDtlClone);
                }
            }
        }
    }

    public XhQdPdKhBttDtl detailDtl(Long id) throws Exception{
        if (ObjectUtils.isEmpty(id)){
            throw new Exception("Không tồn tại bản ghi.");
        }
        Optional<XhQdPdKhBttDtl> byId = xhQdPdKhBttDtlRepository.findById(id);
        if (!byId.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại.");
        }

        XhQdPdKhBttDtl dataDtl = byId.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhQdPdKhBttHdr data = xhQdPdKhBttHdrRepository.findById(dataDtl.getIdQdHdr()).get();
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapVthh.get(data.getCloaiVthh()));
        dataDtl.setXhQdPdKhBttHdr(data);


        List<XhQdPdKhBttDvi> dataDviList = xhQdPdKhBttDviRepository.findAllByIdQdDtl(dataDtl.getId());
         for (XhQdPdKhBttDvi dataDvi : dataDviList){
            List<XhQdPdKhBttDviDtl> dataDviDtlList = xhQdPdKhBttDviDtlRepository.findAllByIdDvi(dataDvi.getId());
            for (XhQdPdKhBttDviDtl dataDviDtl : dataDviDtlList){
                List<XhTcTtinBtt> byIdTt = xhTcTtinBttRepository.findAllByIdDviDtl(dataDviDtl.getId());
                for (XhTcTtinBtt btt : byIdTt){
                    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
                    if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                        btt.setFileDinhKems(fileDinhKems.get(0));
                    }
                }
                dataDviDtl.setTenDiemKho(StringUtils.isEmpty(dataDviDtl.getMaDiemKho())?null:hashMapDvi.get(dataDviDtl.getMaDiemKho()));
                dataDviDtl.setTenNhaKho(StringUtils.isEmpty(dataDviDtl.getMaNhaKho())?null:hashMapDvi.get(dataDviDtl.getMaNhaKho()));
                dataDviDtl.setTenNganKho(StringUtils.isEmpty(dataDviDtl.getMaNganKho())?null:hashMapDvi.get(dataDviDtl.getMaNganKho()));
                dataDviDtl.setTenLoKho(StringUtils.isEmpty(dataDviDtl.getMaLoKho())?null:hashMapDvi.get(dataDviDtl.getMaLoKho()));
                dataDviDtl.setTenLoaiVthh(StringUtils.isEmpty(dataDviDtl.getLoaiVthh())?null:hashMapVthh.get(dataDviDtl.getLoaiVthh()));
                dataDviDtl.setTenCloaiVthh(StringUtils.isEmpty(dataDviDtl.getCloaiVthh())?null:hashMapVthh.get(dataDviDtl.getCloaiVthh()));
                dataDviDtl.setChildren(byIdTt);
            }
             dataDvi.setTenDvi(StringUtils.isEmpty(dataDvi.getMaDvi())?null:hashMapDvi.get(dataDvi.getMaDvi()));
             dataDvi.setChildren(dataDviDtlList);
        }
        dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi())?null:hashMapDvi.get(dataDtl.getMaDvi()));
        dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
        dataDtl.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThaiXh()));
        dataDtl.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThaiHd()));
        dataDtl.setChildren(dataDviList);

        List<XhHopDongBttHdr> dataHd = xhHopDongBttHdrRepository.findAllByIdQdPdDtl(dataDtl.getId());
        dataHd.forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
        });
        dataDtl.setListHopDongBtt(dataHd);

        if (!DataUtils.isNullObject(dataDtl.getPthucBanTrucTiep())) {
            if (dataDtl.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
                List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
                dataDtl.setFileDinhKemUyQuyen(fileDinhKem);
            }

            if (dataDtl.getPthucBanTrucTiep().equals(Contains.BAN_LE)){
                List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
                dataDtl.setFileDinhKemMuaLe(fileDinhKem);
            }

        }

        return dataDtl;
    }
}
