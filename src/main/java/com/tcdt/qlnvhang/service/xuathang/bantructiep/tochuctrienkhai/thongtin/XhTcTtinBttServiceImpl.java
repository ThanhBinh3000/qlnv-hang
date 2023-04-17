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
//                set tên hàng hóa và số quyết định PD để tìm kiếm cho quyết định PD kết quả chào giá
                f.setSoQdPd(hdr.getSoQdPd());
                f.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
                f.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(f.getTrangThai()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
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
            dtl.setThoiHanBan(ObjReq.getThoiHanBan());
            dtl.setTypeSoQdKq(false);
            if (ObjReq.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)) {
                if (!DataUtils.isNullOrEmpty(ObjReq.getFileDinhKemUyQuyen())) {
                    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKemUyQuyen(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
                    dtl.setFileDinhKemUyQuyen(fileDinhKemList);
                }
            }
            if (ObjReq.getPthucBanTrucTiep().equals(Contains.BAN_LE)) {
                if (!DataUtils.isNullOrEmpty(ObjReq.getFileDinhKemMuaLe())) {
                    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKemMuaLe(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
                    dtl.setFileDinhKemMuaLe(fileDinhKemList);
                }
            }
            xhQdPdKhBttDtlRepository.save(dtl);
        }
        xhTcTtinBttRepository.deleteAllByIdDviDtl(ObjReq.getIdDviDtl());
        List<XhTcTtinBtt> bttList = new ArrayList<>();
        for (XhTcTtinBttReq chaoGiaReq : ObjReq.getChildren()) {
            XhTcTtinBtt chaoGiaList = new XhTcTtinBtt();
            BeanUtils.copyProperties(chaoGiaReq, chaoGiaList, "id");
            chaoGiaList.setId(null);
            chaoGiaList.setIdDviDtl(ObjReq.getIdDviDtl());
            chaoGiaList.setIdQdPdDtl(ObjReq.getIdDtl());
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
        String title = " Danh sách thông tin triển khai kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Số QĐ phê duyệt KH bán trực tiếp", "Phương thức bán trực tiếp", "Ngày nhận chào giá/Ngày ủy quyền", "Số QĐ PD KQ chào giá", "Loại hàng hóa", "Chủng loại hàng hóa", "Trạng thái"};
        String fileName = "Danh-sach-thong-tin-trien-khai-ke-hoach-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdPdKhBttDtl dtl = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = null;
            objs[2] = dtl.getPthucBanTrucTiep();
            objs[3] = dtl.getNgayNhanCgia();
            objs[4] = dtl.getSoQdKq();
            objs[5] = null;
            objs[6] = null;
            objs[7] = dtl.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

}