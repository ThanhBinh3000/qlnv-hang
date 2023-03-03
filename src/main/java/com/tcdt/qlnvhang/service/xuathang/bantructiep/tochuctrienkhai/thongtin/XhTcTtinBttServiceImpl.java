package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhCgiaReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class XhTcTtinBttServiceImpl extends BaseServiceImpl {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> selectPage(SearchXhTcTtinBttReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBttDtl> dtl = xhQdPdKhBttDtlRepository.search(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        dtl.getContent().forEach(f -> {
            try {
                XhQdPdKhBttHdr hdr = xhQdPdKhBttHdrRepository.findById(f.getIdQdHdr()).get();
                hdr.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
                hdr.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));
                f.setXhQdPdKhBttHdr(hdr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(f.getTrangThai()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return dtl;
    }

    @Transactional()
    public List<XhTcTtinBtt> create(XhCgiaReq ObjReq) throws Exception {
        Optional<XhQdPdKhBttDtl> byId = xhQdPdKhBttDtlRepository.findById(ObjReq.getIdDtl());
        XhQdPdKhBttDtl dtl = byId.get();
        if (!byId.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        } else {
            dtl.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            dtl.setPthucBanTrucTiep(ObjReq.getPthucBanTrucTiep());
            dtl.setDiaDiemChaoGia(ObjReq.getDiaDiemChaoGia());
            dtl.setNgayNhanCgia(getDateTimeNow());
            dtl.setNgayMkho(ObjReq.getNgayMkho());
            dtl.setNgayKthuc(ObjReq.getNgayKthuc());
            dtl.setGhiChu(ObjReq.getGhiChu());
            dtl.setMaDvi(getUser().getDvql());
            dtl.setLoaiVthh(ObjReq.getLoaiVthh());
            dtl.setCloaiVthh(ObjReq.getCloaiVthh());
            if (ObjReq.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKems(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
                dtl.setFileDinhKemUyQuyen(fileDinhKems);
            }
            if (ObjReq.getPthucBanTrucTiep().equals(Contains.MUA_LE)) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKems(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
                dtl.setFileDinhKemMuaLe(fileDinhKems);
            }
            xhQdPdKhBttDtlRepository.save(dtl);
        }
        xhTcTtinBttRepository.deleteAllByIdDtl(ObjReq.getIdDtl());
        List<XhTcTtinBtt> bttList = new ArrayList<>();
        for (XhTcTtinBttReq chaoGiaReq : ObjReq.getChildren()) {
            XhTcTtinBtt chaoGiaList = new XhTcTtinBtt();
            BeanUtils.copyProperties(chaoGiaReq, chaoGiaList, "id");
            chaoGiaList.setId(null);
            chaoGiaList.setIdDtl(ObjReq.getIdDtl());
            XhTcTtinBtt save = xhTcTtinBttRepository.save(chaoGiaList);

            if (!DataUtils.isNullObject(chaoGiaReq.getFileDinhKems())) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(chaoGiaReq.getFileDinhKems()), save.getId(), XhTcTtinBtt.TABLE_NAME);
                chaoGiaList.setFileDinhKems(fileDinhKems.get(0));
            }

            bttList.add(chaoGiaList);

        }
        return bttList;
    }

    public void approve(XhCgiaReq stReq) throws Exception {
        Optional<XhQdPdKhBttDtl> optional = xhQdPdKhBttDtlRepository.findById(stReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThai(stReq.getTrangThai());
        } else {
            throw new Exception("Cập nhật không thành công");
        }
        xhQdPdKhBttDtlRepository.save(optional.get());
    }

    public void export(SearchXhTcTtinBttReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdPdKhBttDtl> page = this.selectPage(req);
        List<XhQdPdKhBttDtl> data = page.getContent();
        String title = "Danh sách thông tin triển khai kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Số QĐ phê duyệt Kh bán trực tiếp", "Đơn vị", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PD KQ chào giá", "Loại hàng hóa", "Chủng loại hành hóa", "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String filename = "danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dtl.getSoQdPd();
            objs[2] = dtl.getTenDvi();
            objs[3] = dtl.getPthucBanTrucTiep();
            objs[4] = dtl.getNgayNhanCgia();
            objs[5] = dtl.getSoQdKq();
            objs[6] = dtl.getTenLoaiVthh();
            objs[7] = dtl.getTenCloaiVthh();
            objs[8] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }


//    public List<XhTcTtinBtt> detail (Long id) throws Exception{
//
//        if (ObjectUtils.isEmpty(id)) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        Optional<XhQdPdKhBttHdr> byId = xhQdPdKhBttHdrRepository.findById(id);
//        if (!byId.isPresent()){
//            throw new Exception("Bản ghi không tồn tại");
//        }
//
//        XhQdPdKhBttHdr hdr = byId.get();
//        List<XhTcTtinBtt> byIdTt = xhTcTtinBttRepository.findAllByIdHdr(id);
//        for (XhTcTtinBtt btt : byIdTt){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
//            btt.setFileDinhKems(fileDinhKems.get(0));
//        }
//        if (hdr.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(hdr.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            hdr.setFileDinhKemUyQuyen(fileDinhKems);
//        }
//        if (hdr.getPthucBanTrucTiep().equals(Contains.MUA_LE)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(hdr.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            hdr.setFileDinhKemMuaLe(fileDinhKems);
//        }
//        return byIdTt;
//    }

//    public XhQdPdKhBttDtl detail(Long id) throws Exception{
//        Optional<XhQdPdKhBttDtl> qOptional = xhQdPdKhBttDtlRepository.findById(id);
//
//        if (!qOptional.isPresent()){
//            throw new UnsupportedOperationException("Không tồn tại bản ghi");
//        }
//
//        XhQdPdKhBttDtl data = qOptional.get();
//
//        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
//        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
//
//        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
//        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
//        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
//        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
//        List<XhQdPdKhBttDvi> byIdDvi = xhQdPdKhBttDviRepository.findByIdQdDtl(data.getId());
//        for (XhQdPdKhBttDvi dvi : byIdDvi){
//            dvi.setTenDvi(hashMapDvi.get(dvi.getMaDvi()));
//        }
//        data.setChildren(byIdDvi);
//        List<XhTcTtinBtt> byId = xhTcTtinBttRepository.findAllByIdDtl(data.getId());
//
//        for (XhTcTtinBtt btt : byId){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
//            btt.setFileDinhKems(fileDinhKems.get(0));
//        }
//        if (data.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            data.setFileDinhKemUyQuyen(fileDinhKems);
//        }
//        if (data.getPthucBanTrucTiep().equals(Contains.MUA_LE)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            data.setFileDinhKemMuaLe(fileDinhKems);
//        }
//        data.setXhTcTtinBttList(byId);
//        return data;
//    }
}