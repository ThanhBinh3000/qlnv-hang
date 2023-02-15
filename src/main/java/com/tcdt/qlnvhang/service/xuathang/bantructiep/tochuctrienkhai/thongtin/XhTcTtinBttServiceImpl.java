package com.tcdt.qlnvhang.service.xuathang.bantructiep.tochuctrienkhai.thongtin;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttRepository;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhCgiaReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBttReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class XhTcTtinBttServiceImpl extends BaseServiceImpl  {

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Autowired
    private XhQdPdKhBttDtlRepository xhQdPdKhBttDtlRepository;

    @Autowired
    private XhTcTtinBttRepository xhTcTtinBttRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhQdPdKhBttDtl> selectPage(SearchXhTcTtinBttReq req) throws Exception{
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdPdKhBttDtl> dtl = xhQdPdKhBttDtlRepository.selectPage(
                req.getNamKh(),
                convertDateToString(req.getNgayCgiaTu()),
                convertDateToString(req.getNgayCgiadDen()),
                req.getLoaiVthh(),
                req.getMaDvi(),
                req.getTochucCanhan(),
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        dtl.getContent().forEach(f ->{

            try {
                XhQdPdKhBttHdr hdr = xhQdPdKhBttHdrRepository.findById(f.getIdQdHdr()).get();

                hdr.setTenLoaiVthh(hashMapVthh.get(hdr.getLoaiVthh()));
                hdr.setTenCloaiVthh(hashMapVthh.get(hdr.getCloaiVthh()));

                f.setHdr(hdr);
            } catch (Exception e){
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
    public List<XhTcTtinBtt> create (XhCgiaReq ObjReq) throws Exception{
        Optional<XhQdPdKhBttDtl> byId = xhQdPdKhBttDtlRepository.findById(ObjReq.getIdDtl());
        XhQdPdKhBttDtl dtl = byId.get();
        if (!byId.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }else {
            dtl.setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            xhQdPdKhBttDtlRepository.save(dtl);
        }
        dtl.setPthucBanTrucTiep(ObjReq.getPthucBanTrucTiep());
        dtl.setDiaDiemChaoGia(ObjReq.getDiaDiemChaoGia());
        dtl.setNgayMkho(ObjReq.getNgayMkho());
        dtl.setNgayKthuc(ObjReq.getNgayKthuc());
        dtl.setLoaiVthh(ObjReq.getLoaiVthh());
        dtl.setCloaiVthh(ObjReq.getCloaiVthh());
        dtl.setMoTaHangHoa(ObjReq.getMoTaHangHoa());
        dtl.setGhiChu(ObjReq.getGhiChu());
        dtl.setNgayNhanCgia(getDateTimeNow());
        xhTcTtinBttRepository.deleteAllByIdDtl(ObjReq.getIdDtl());
        List<XhTcTtinBtt> bttList = new ArrayList<>();
        for (XhTcTtinBttReq bttReq : ObjReq.getChildren()){
            XhTcTtinBtt btt = new XhTcTtinBtt();
            BeanUtils.copyProperties(bttReq,btt,"id");
            btt.setIdDtl(ObjReq.getIdDtl());
            XhTcTtinBtt save = xhTcTtinBttRepository.save(btt);
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(bttReq.getFileDinhKems()), btt.getId(), XhTcTtinBtt.TABLE_NAME);
            btt.setFileDinhKems(fileDinhKems.get(0));
            bttList.add(btt);
        }
        if (dtl.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKems(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
            dtl.setFileDinhKemUyQuyen(fileDinhKems);
        }
        if (dtl.getPthucBanTrucTiep().equals(Contains.MUA_LE)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(ObjReq.getFileDinhKems(), dtl.getId(), XhQdPdKhBttDtl.TABLE_NAME);
            dtl.setFileDinhKemMuaLe(fileDinhKems);
        }
        xhQdPdKhBttDtlRepository.save(dtl);
        return bttList;
    }


//    public List<XhTcTtinBtt> detail (Long id) throws Exception{
//
//        if (ObjectUtils.isEmpty(id)) {
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        Optional<XhQdPdKhBttDtl> byId = xhQdPdKhBttDtlRepository.findById(id);
//        if (!byId.isPresent()){
//            throw new Exception("Bản ghi không tồn tại");
//        }
//
//        XhQdPdKhBttDtl dtl = byId.get();
//        List<XhTcTtinBtt> byIdTt = xhTcTtinBttRepository.findAllByIdDtl(id);
//        for (XhTcTtinBtt btt : byIdTt){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
//            btt.setFileDinhKems(fileDinhKems.get(0));
//        }
//        if (dtl.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            dtl.setFileDinhKemUyQuyen(fileDinhKems);
//        }
//        if (dtl.getPthucBanTrucTiep().equals(Contains.MUA_LE)){
//            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
//            dtl.setFileDinhKemMuaLe(fileDinhKems);
//        }
//        return byIdTt;
//    }

    public XhQdPdKhBttDtl detail(Long id) throws Exception{
        Optional<XhQdPdKhBttDtl> qOptional = xhQdPdKhBttDtlRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        XhQdPdKhBttDtl data = qOptional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        List<XhTcTtinBtt> byId = xhTcTtinBttRepository.findAllByIdDtl(data.getId());
        for (XhTcTtinBtt btt : byId){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(btt.getId(), Arrays.asList(XhTcTtinBtt.TABLE_NAME));
            btt.setFileDinhKems(fileDinhKems.get(0));
        }
        if (data.getPthucBanTrucTiep().equals(Contains.UY_QUYEN)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
            data.setFileDinhKemUyQuyen(fileDinhKems);
        }
        if (data.getPthucBanTrucTiep().equals(Contains.MUA_LE)){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdPdKhBttDtl.TABLE_NAME));
            data.setFileDinhKemMuaLe(fileDinhKems);
        }
        data.setXhTcTtinBttList(byId);
        return data;
    }



    public void approve (XhCgiaReq stReq) throws Exception{
        Optional<XhQdPdKhBttDtl> optional = xhQdPdKhBttDtlRepository.findById(stReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((NhapXuatHangTrangThaiEnum.HOANTHANHCAPNHAT.getId() + NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId()).equals(status)) {
            optional.get().setTrangThai(stReq.getTrangThai());
        }else{
            throw new Exception("Cập nhật không thành công");
        }
        xhQdPdKhBttDtlRepository.save(optional.get());
    }

}
