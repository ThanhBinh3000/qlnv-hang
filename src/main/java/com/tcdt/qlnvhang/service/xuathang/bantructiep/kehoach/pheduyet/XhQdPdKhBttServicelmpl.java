package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
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
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;


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
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
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

        if (!StringUtils.isEmpty(req.getSoQdPd())){
            List<XhQdPdKhBttHdr> checkSoQd = xhQdPdKhBttHdrRepository.findBySoQdPd(req.getSoQdPd());
            if (!checkSoQd.isEmpty()){
                throw new Exception("Số quyết định" + req.getSoQdPd() + " đã tồn tại ");
            }
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

        XhQdPdKhBttHdr dataMap = new XhQdPdKhBttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setLastest(false);
        dataMap.setMaDvi(getUser().getDvql());
        xhQdPdKhBttHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), dataMap.getId(), XhQdPdKhBttHdr.TABLE_NAME);
        dataMap.setFileDinhKem(fileDinhKems);

        if (req.getPhanLoai().equals("TH")){
            dataTh.setTrangThai(Contains.DADUTHAO_QD);
            xhThopDxKhBttRepository.save(dataTh);
        }else {
            dataDx.setTrangThaiTh(Contains.DADUTHAO_QD);
            dataDx.setSoQdPd(dataMap.getSoQdPd());
            dataDx.setNgayKyQd(dataMap.getNgayKyQd());
            xhDxKhBanTrucTiepHdrRepository.save(dataDx);
        }
        saveDetail(req, dataMap.getId());
        return dataMap;
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
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdPdKhBttHdr> qOptional = xhQdPdKhBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!StringUtils.isEmpty(req.getSoQdPd())) {
            if (!req.getSoQdPd().equals(qOptional.get().getSoQdPd())) {
                List<XhQdPdKhBttHdr> checkSoQd = xhQdPdKhBttHdrRepository.findBySoQdPd(req.getSoQdPd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + req.getSoQdPd() + " đã tồn tại");
                }
            }
        }

        if (req.getPhanLoai().equals("TH")){
            Optional<XhThopDxKhBttHdr> qOptionalTh = xhThopDxKhBttRepository.findById(req.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán trực tiếp");
            }
        }else {
            Optional<XhDxKhBanTrucTiepHdr>  qOptionalDx = xhDxKhBanTrucTiepHdrRepository.findById(req.getIdTrHdr());
            if (!qOptionalDx.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán trực tiếp");
            }
        }

        XhQdPdKhBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
        xhQdPdKhBttHdrRepository.save(dataDB);
        this.saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhQdPdKhBttHdr detail(Long id) throws Exception {
       if (StringUtils.isEmpty(id)){
           throw new Exception("Bản ghi không tồn tại");
       }

       Optional<XhQdPdKhBttHdr> qOptional = xhQdPdKhBttHdrRepository.findById(id);

       if (!qOptional.isPresent()){
           throw new UnsupportedOperationException("Bản ghi không tồn tại");
       }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhQdPdKhBttHdr data = qOptional.get();

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        List<XhQdPdKhBttDtl> xhQdPdKhBttDtlList = new ArrayList<>();
        for (XhQdPdKhBttDtl dtl : xhQdPdKhBttDtlRepository.findAllByIdQdHdr(id)){
            List<XhQdPdKhBttDvi> xhQdPdKhBttDviList = new ArrayList<>();
            for (XhQdPdKhBttDvi dvi : xhQdPdKhBttDviRepository.findByIdQdDtl(dtl.getId())) {
                List<XhQdPdKhBttDviDtl> xhQdPdKhBttDviDtlList = xhQdPdKhBttDviDtlRepository.findByIdDvi(dvi.getId());
                xhQdPdKhBttDviDtlList.forEach(f ->{
                    f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                    f.setTenNhakho(hashMapDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
                });
                dvi.setTenDvi(hashMapDvi.get(dvi.getMaDvi()));
                dvi.setChildren(xhQdPdKhBttDviDtlList);
                xhQdPdKhBttDviList.add(dvi);
            }
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : hashMapDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhQdPdKhBttDviList);
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            xhQdPdKhBttDtlList.add(dtl);
        }
        data.setChildren(xhQdPdKhBttDtlList);
        return data;
    }

    @Override
    public XhQdPdKhBttHdr approve(XhQdPdKhBttHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhQdPdKhBttHdr dataDB = detail(req.getId());
        String status = req.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                dataDB.setNgayPduyet(getDateTimeNow());
                dataDB.setNguoiPduyetId(getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(req.getTrangThai());
        if (req.getTrangThai().equals(Contains.BAN_HANH)) {
            if (dataDB.getPhanLoai().equals("TH")) {
                Optional<XhThopDxKhBttHdr> qOptionalTh = xhThopDxKhBttRepository.findById(dataDB.getIdThHdr());
                if (qOptionalTh.isPresent()) {
                    if (qOptionalTh.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    xhThopDxKhBttRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
                } else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }
            } else {
                Optional<XhDxKhBanTrucTiepHdr> qOptionalTr = xhDxKhBanTrucTiepHdrRepository.findById(dataDB.getIdTrHdr());
                if (qOptionalTr.isPresent()) {
                    if (qOptionalTr.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                    // Update trạng thái tờ trình
                    qOptionalTr.get().setTrangThaiTh(Contains.DABANHANH_QD);
                    xhDxKhBanTrucTiepHdrRepository.save(qOptionalTr.get());
                } else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }
            this.cloneProject(dataDB.getId());
        }
        XhQdPdKhBttHdr createCheck = xhQdPdKhBttHdrRepository.save(dataDB);
        return createCheck;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
        }

        Optional<XhQdPdKhBttHdr> optional = xhQdPdKhBttHdrRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }

        List<XhQdPdKhBttDtl> xhQdPdKhBttDtls = xhQdPdKhBttDtlRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(xhQdPdKhBttDtls)){
            for (XhQdPdKhBttDtl dtl : xhQdPdKhBttDtls){
                List<XhQdPdKhBttDvi> byIdQdDtl = xhQdPdKhBttDviRepository.findByIdQdDtl(dtl.getId());
                for (XhQdPdKhBttDvi dvi : byIdQdDtl ){
                    xhQdPdKhBttDviDtlRepository.deleteAllByIdDvi(dvi.getId());
                }
                xhQdPdKhBttDviRepository.deleteByIdQdDtl(dtl.getId());
            }
            xhQdPdKhBttDtlRepository.deleteAll(xhQdPdKhBttDtls);
        }
        xhQdPdKhBttHdrRepository.delete(optional.get());

        if (optional.get().getPhanLoai().equals("TH")){
            xhThopDxKhBttRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        }else {
            xhDxKhBanTrucTiepHdrRepository.updateStatusTh(optional.get().getIdTrHdr(), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
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
            objs[9] = hdr.getMaDviTsan();
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
        for (XhQdPdKhBttDtl dtl : hdr.getChildren()){
            XhQdPdKhBttDtl dtlClone = new XhQdPdKhBttDtl();
            BeanUtils.copyProperties(dtl, dtlClone);
            dtlClone.setId(null);
            dtlClone.setIdQdHdr(hdrClone.getId());
            xhQdPdKhBttDtlRepository.save(dtlClone);
            for (XhQdPdKhBttDvi dvi : dtlClone.getChildren()){
                XhQdPdKhBttDvi dviClone = new XhQdPdKhBttDvi();
                BeanUtils.copyProperties(dvi, dviClone);
                dviClone.setId(null);
                dviClone.setIdQdDtl(dtlClone.getId());
                xhQdPdKhBttDviRepository.save(dviClone);
                for (XhQdPdKhBttDviDtl dviDtl : dviClone.getChildren()){
                    XhQdPdKhBttDviDtl dviDtlClone = new XhQdPdKhBttDviDtl();
                    BeanUtils.copyProperties(dviDtl, dviDtlClone);
                    dviDtlClone.setId(null);
                    dviDtlClone.setIdDvi(dviClone.getId());
                    xhQdPdKhBttDviDtlRepository.save(dviDtlClone);
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

        XhQdPdKhBttDtl dtl = byId.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        XhQdPdKhBttHdr hdr = xhQdPdKhBttHdrRepository.findById(dtl.getIdQdHdr()).get();
        hdr.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
        hdr.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));
        dtl.setHdr(hdr);
        List<XhTcTtinBtt> byIdTt = xhTcTtinBttRepository.findAllByIdDtl(dtl.getId());
        for (XhTcTtinBtt btt : byIdTt){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
            btt.setFileDinhKems(fileDinhKems.get(0));
        }
        dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
        dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi())? null : hashMapDvi.get(dtl.getMaDvi()));
        dtl.setTenLoaiVthh(StringUtils.isEmpty(dtl.getLoaiVthh())? null : hashMapVthh.get(dtl.getLoaiVthh()));
        dtl.setTenCloaiVthh(StringUtils.isEmpty(dtl.getCloaiVthh())? null : hashMapVthh.get(dtl.getCloaiVthh()));
        dtl.setFileDinhKemMuaLe(fileDinhKemService.search(dtl.getId(), Collections.singleton(XhQdPdKhBttDtl.TABLE_NAME)));
        dtl.setFileDinhKemUyQuyen(fileDinhKemService.search(dtl.getId(), Collections.singleton(XhQdPdKhBttDtl.TABLE_NAME)));
        dtl.setXhTcTtinBttList(byIdTt);
     return dtl;
    }
}
