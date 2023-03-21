package com.tcdt.qlnvhang.service.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong.XhHopDongBttHdrReq;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
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
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhHopDongBttHdr> searchPage(XhHopDongBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhHopDongBttHdr> page = xhHopDongBttHdrRepository.searchPage(
                req,
                pageable);
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
        page.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiPhuLuc()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
            f.setTenLoaiHdong(hashMapLoaiHdong.get(f.getLoaiHdong()));
        });
        return page;
    }

    @Override
    public XhHopDongBttHdr create(XhHopDongBttHdrReq req) throws Exception {
        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<XhHopDongBttHdr> qOpHdong = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
        XhHopDongBttHdr dataMap = new XhHopDongBttHdr();

        if (DataUtils.isNullObject(req.getIdHd())) {
            if (qOpHdong.isPresent()) {
                throw new Exception("Hợp đồng số" + req.getSoHd() + "đã tồn tại");
            }

            Optional<XhKqBttHdr> checkSoQd = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
                if (!checkSoQd.isPresent() && !Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())) {
                    throw new Exception("Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
                } else {
                    checkSoQd.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
                    xhKqBttHdrRepository.save(checkSoQd.get());
                }
        }
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNguoiTaoId(userInfo.getId());
        dataMap.setNgayTao(new Date());
        dataMap.setTrangThai(Contains.DU_THAO);
        dataMap.setTrangThaiPhuLuc(Contains.DUTHAO);
        dataMap.setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setMaDviTsan(String.join(",",req.getListMaDviTsan()));

        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(dataMap);
        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), XhHopDongBttHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        if (!DataUtils.isNullObject(req.getIdHd())) {
            if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
                created.setFileDinhKems(fileDinhKems);
            }
        }

        saveDetail(req, dataMap.getId());

        return dataMap;

    }

    void saveDetail(XhHopDongBttHdrReq req, Long idHdr) {
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

//        Bắt đầu Phụ lục DTL
        for (XhHopDongBttDtlReq phuLucReq : req.getPhuLucDtl()) {
            XhHopDongBttDtl phuLuc = new XhHopDongBttDtl();
            BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
            phuLuc.setId(null);
            phuLuc.setIdHdr(idHdr);
            xhHopDongBttDtlRepository.save(phuLuc);
        }
//       Kết thúc Phụ lục DTL
    }

    @Override
    public XhHopDongBttHdr update(XhHopDongBttHdrReq req) throws Exception {
        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> qOptional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        if (DataUtils.isNullObject(req.getIdHd())) {
            if (!qOptional.get().getSoHd().equals(req.getSoHd())) {
                Optional<XhHopDongBttHdr> qOpHdong = xhHopDongBttHdrRepository.findBySoHd(req.getSoHd());
                if (qOpHdong.isPresent())
                    throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
            }
            if (!qOptional.get().getSoQdKq().equals(req.getSoQdKq()) && !Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())) {
                Optional<XhKqBttHdr> checkSoQd = xhKqBttHdrRepository.findBySoQdKq(req.getSoQdKq());
                if (!checkSoQd.isPresent())
                    throw new Exception(
                                "Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
            }

        }
        XhHopDongBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");

        dataDB.setNgaySua(new Date());
        dataDB.setNguoiSuaId(userInfo.getId());

        XhHopDongBttHdr created = xhHopDongBttHdrRepository.save(dataDB);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhHopDongBttHdr.TABLE_NAME);
            dataDB.setFileDinhKem(fileDinhKem.get(0));
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
            dataDB.setFileDinhKems(fileDinhKems);
        }

        if (!DataUtils.isNullObject(req.getIdHd())) {
            if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhHopDongBttHdr.TABLE_NAME);
                dataDB.setFileDinhKems(fileDinhKems);
            }
        }

        saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhHopDongBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<XhHopDongBttHdr> qOptional = xhHopDongBttHdrRepository.findById(id);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        XhHopDongBttHdr data = qOptional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
            data.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiPhuLuc()));
            data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
            data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
            data.setTenLoaiHdong(hashMapLoaiHdong.get(data.getLoaiHdong()));
        if (DataUtils.isNullObject(data.getIdHd())) {
            data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
        }

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        data.setFileDinhKems(fileDinhKem);

        List<XhHopDongBttDtl> allByIdHdr = xhHopDongBttDtlRepository.findAllByIdHdr(data.getId());
        allByIdHdr.forEach(item -> {
            item.setTenDvi(hashMapDvi.get(item.getMaDvi()));
            if (!DataUtils.isNullObject(qOptional.get().getIdHd())) {
                Optional<XhHopDongBttDtl> byIdHdDtl = xhHopDongBttDtlRepository.findById(item.getIdHdDtl());
                System.out.println(byIdHdDtl);
                if (!DataUtils.isNullObject(byIdHdDtl)) {
                    item.setTenDviHd(hashMapDvi.get(byIdHdDtl.get().getMaDvi()));
                    item.setDiaChiHd(byIdHdDtl.get().getDiaChi());
                }
            }
        });
        data.setChildren(allByIdHdr);

        for (XhHopDongBttDtl dtl : allByIdHdr){
            List<XhHopDongBttDvi> hopDongBttDviList = xhHopDongBttDviRepository.findAllByIdDtl(dtl.getId());
            hopDongBttDviList.forEach(f->{
                f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(hashMapDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
                f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
            });
            dtl.setChildren(hopDongBttDviList);
        }

//        Bắt đầu phụ lục
        data.setPhuLucDtl(allByIdHdr);
        if (!DataUtils.isNullObject(data.getIdHd())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhHopDongBttHdr.TABLE_NAME));
            data.setFileDinhKems(fileDinhKems);
        }


        List<XhHopDongBttHdr> phuLucList = new ArrayList<>();
        for (XhHopDongBttHdr phuLuc : xhHopDongBttHdrRepository.findAllByIdHd(id)) {
            List<XhHopDongBttDtl> phuLucDtlList = xhHopDongBttDtlRepository.findAllByIdHdr(phuLuc.getId());
            phuLucDtlList.forEach(f -> {
                f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            });
            phuLuc.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(phuLuc.getTrangThaiPhuLuc()));
            phuLuc.setPhuLucDtl(phuLucDtlList);
            phuLucList.add(phuLuc);
        }
        data.setPhuLuc(phuLucList);
         /*
        Kết thúc Phụ lục
         * */
        return data;
    }


    @Override
    public XhHopDongBttHdr approve(XhHopDongBttHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if (DataUtils.isNullObject(optional.get().getIdHd())) {
            String status = req.getTrangThai() + optional.get().getTrangThai();
            if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
                optional.get().setNguoiPduyetId(getUser().getId());
                optional.get().setNgayPduyet(getDateTimeNow());
            } else {
                throw new Exception("Phê duyệt không thành công");
            }
            optional.get().setTrangThai(req.getTrangThai());
        } else {
            String status = req.getTrangThaiPhuLuc() + optional.get().getTrangThaiPhuLuc();
            if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
                optional.get().setNguoiPduyetId(getUser().getId());
                optional.get().setNgayPduyet(getDateTimeNow());
            } else {
                throw new Exception("Phê duyệt không thành công");
            }
            optional.get().setTrangThaiPhuLuc(req.getTrangThaiPhuLuc());
        }


        return xhHopDongBttHdrRepository.save(optional.get());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongBttHdr> optional = xhHopDongBttHdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (DataUtils.isNullObject(optional.get().getIdHd())) {
            if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
            }
        } else {
            if (!optional.get().getTrangThaiPhuLuc().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
            }
        }

        List<XhHopDongBttDtl> dtlList = xhHopDongBttDtlRepository.findAllByIdHdr(id);
        for (XhHopDongBttDtl dtl : dtlList){
            xhHopDongBttDviRepository.deleteAllByIdDtl(dtl.getId());
        }

        xhHopDongBttHdrRepository.delete(optional.get());
        xhHopDongBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhHopDongBttHdr.TABLE_NAME));
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
            objs[5] = hdr.getSoLuong();
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
