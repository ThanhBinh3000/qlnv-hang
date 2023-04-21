package com.tcdt.qlnvhang.service.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgPlDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgPlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhQdDchinhKhBdgHdr> searchPage(XhQdDchinhKhBdgReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdDchinhKhBdgHdr> data = xhQdDchinhKhBdgHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhQdDchinhKhBdgHdr create(XhQdDchinhKhBdgReq req) throws Exception {

        if (!StringUtils.isEmpty(req.getSoQdDc())){
            List<XhQdDchinhKhBdgHdr> checkSoQdDc = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(req.getSoQdDc());
            if (!checkSoQdDc.isEmpty()){
                throw new Exception("Số quyết định điều chỉnh" + req.getSoQdDc() + "đã tồn tại");
            }
        }

        XhQdDchinhKhBdgHdr dataMap = new XhQdDchinhKhBdgHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setMaDvi(getUser().getDvql());
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(dataMap);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME+ "_BAN_HANH");
            created.setFileDinhKem(fileDinhKem);
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        saveDetail(req, dataMap.getId());
        return created;
    }

    void saveDetail(XhQdDchinhKhBdgReq req, Long idHdr){
        xhQdDchinhKhBdgDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhQdDchinhKhBdgDtlReq  dtlReq : req.getChildren()){
            XhQdDchinhKhBdgDtl dtl = new XhQdDchinhKhBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            dtl.setLoaiVthh(req.getLoaiVthh());
            dtl.setCloaiVthh(req.getCloaiVthh());
            xhQdDchinhKhBdgDtlRepository.save(dtl);
            xhQdDchinhKhBdgPlRepository.deleteByIdDcDtl(dtlReq.getId());
            for (XhQdDchinhKhBdgPlReq plReq : dtlReq.getChildren()){
                XhQdDchinhKhBdgPl pl = new XhQdDchinhKhBdgPl();
                BeanUtils.copyProperties(plReq, pl, "id");
                pl.setIdDcDtl(dtl.getId());
                xhQdDchinhKhBdgPlRepository.save(pl);
                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(plReq.getId());
                for (XhQdDchinhKhBdgPlDtlReq plDtlReq : plReq.getChildren()){
                    XhQdDchinhKhBdgPlDtl plDtl = new XhQdDchinhKhBdgPlDtl();
                    BeanUtils.copyProperties(plDtlReq, plDtl, "id");
                    plDtl.setIdPhanLo(pl.getId());
                    xhQdDchinhKhBdgPlDtlRepository.save(plDtl);
                }
            }
        }
    }

    @Override
    public XhQdDchinhKhBdgHdr update(XhQdDchinhKhBdgReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
        XhQdDchinhKhBdgHdr created = xhQdDchinhKhBdgHdrRepository.save(dataDB);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH"));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH");
        created.setFileDinhKem(fileDinhKem);

        fileDinhKemService.delete(dataDB.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdDchinhKhBdgHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhQdDchinhKhBdgHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        XhQdDchinhKhBdgHdr data = qOptional.get();

        data.setTenLoaiVthh(hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapDmHh.get(data.getCloaiVthh()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdDchinhKhBdgHdr.TABLE_NAME+ "_BAN_HANH"));
        data.setFileDinhKem(fileDinhKem);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdDchinhKhBdgHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);

        List<XhQdDchinhKhBdgDtl> xhQdDchinhKhBdgDtlList = new ArrayList<>();
        for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(id)){
            List<XhQdDchinhKhBdgPl> xhQdDchinhKhBdgPlList = new ArrayList<>();
            for (XhQdDchinhKhBdgPl pl : xhQdDchinhKhBdgPlRepository.findByIdDcDtl(dtl.getId())){
                List<XhQdDchinhKhBdgPlDtl> xhQdDchinhKhBdgPlDtlList = xhQdDchinhKhBdgPlDtlRepository.findByIdPhanLo(pl.getId());
                xhQdDchinhKhBdgPlDtlList.forEach(f ->{
                    f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho())? null : mapDmucDvi.get(f.getMaDiemKho()));
                    f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho())? null : mapDmucDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho())? null : mapDmucDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho())? null : mapDmucDvi.get(f.getMaLoKho()));
                });
                pl.setTenDvi(StringUtils.isEmpty(pl.getMaDvi())? null : mapDmucDvi.get(pl.getMaDvi()));
                pl.setChildren(xhQdDchinhKhBdgPlDtlList);
                xhQdDchinhKhBdgPlList.add(pl);
            };
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setTenLoaiVthh(StringUtils.isEmpty(dtl.getLoaiVthh())? null : hashMapDmHh.get(dtl.getLoaiVthh()));
            dtl.setTenCloaiVthh(StringUtils.isEmpty(dtl.getCloaiVthh())? null : hashMapDmHh.get(dtl.getCloaiVthh()));
            dtl.setChildren(xhQdDchinhKhBdgPlList);
            xhQdDchinhKhBdgDtlList.add(dtl);
        }
        data.setChildren(xhQdDchinhKhBdgDtlList);
        return data;
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
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái dự thảo");
        }
        XhQdDchinhKhBdgHdr hdr = optional.get();

        List<XhQdDchinhKhBdgDtl> xhQdDchinhKhBdgDtlList = xhQdDchinhKhBdgDtlRepository.findAllByIdHdr(hdr.getId());
        if (!CollectionUtils.isEmpty(xhQdDchinhKhBdgDtlList)){
            for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlList){
                List<XhQdDchinhKhBdgPl> xhQdDchinhKhBdgPlList = xhQdDchinhKhBdgPlRepository.findByIdDcDtl(dtl.getId());
                for (XhQdDchinhKhBdgPl pl : xhQdDchinhKhBdgPlList){
                    xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(pl.getId());
                }
                xhQdDchinhKhBdgPlRepository.deleteByIdDcDtl(dtl.getId());
            }
            xhQdDchinhKhBdgDtlRepository.deleteAll(xhQdDchinhKhBdgDtlList);
        }
        xhQdDchinhKhBdgHdrRepository.delete(hdr);
        fileDinhKemService.delete(hdr.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME));
        fileDinhKemService.delete(hdr.getId(), Collections.singleton(XhQdDchinhKhBdgHdr.TABLE_NAME + "_BAN_HANH"));

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)) {
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhQdDchinhKhBdgHdr hdr : list){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái dự thảo");
            }else {
                this.delete(hdr.getId());
            }
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
        String title = " Danh sách quyết định điều chỉnh bán đấu giá ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ điều chỉnh KH BĐG", "Ngày ký QĐ điều chỉnh", "Số QĐ gốc", "Trích yếu", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Thời gian giao nhận", "Trạng thái"};
        String fileName = "Danh-sach-quyet-dinh-dieu-chinh-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdDchinhKhBdgHdr hdr = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hdr.getNam();
            objs[2] = hdr.getSoQdDc();
            objs[3] = hdr.getNgayKyDc();
            objs[4] = hdr.getSoQdGoc();
            objs[5] = hdr.getTrichYeu();
            objs[6] = hdr.getTenCloaiVthh();
            objs[7] = hdr.getSlDviTsan();
            objs[8] = null;
            objs[9] = null;
            objs[10] = hdr.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
