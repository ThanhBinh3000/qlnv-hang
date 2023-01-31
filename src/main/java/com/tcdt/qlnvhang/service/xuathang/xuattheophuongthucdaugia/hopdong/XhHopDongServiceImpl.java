package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.hopdong;

import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDdiemNhapKho;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.repository.HhQdPduyetKqlcntHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.hopdong.XhHopDongHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhHopDongServiceImpl extends BaseServiceImpl implements XhHopDongService {
    @Autowired
    private XhHopDongRepository xhHopDongRepository;

    @Autowired
    private XhHopDongDtlRepository xhHopDongDtlRepository;

    @Autowired
    private XhHopDongDdiemNhapKhoRepository xhHopDongDdiemNhapKhoRepository;

    @Autowired
    private XhKqBdgHdrRepository xhKqBdgHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhHopDongHdr> searchPage(XhHopDongHdrReq req) throws Exception {
        return null;
    }

    @Override
    public List<XhHopDongHdr> searchAll(XhHopDongHdrReq req) {
        return null;
    }

    @Override
    public XhHopDongHdr create(XhHopDongHdrReq req) throws Exception {
        Optional<XhHopDongHdr> qOpHdong = xhHopDongRepository.findBySoHd(req.getSoHd());
        if (qOpHdong.isPresent()) {
            throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
        }

        Optional<XhKqBdgHdr> checkSoQd = xhKqBdgHdrRepository.findBySoQdKq(req.getSoQdKq());
        if (!checkSoQd.isPresent()) {
            throw new Exception("Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + req.getSoQdKq() + " không tồn tại");
        }

        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        XhHopDongHdr dataMap = new XhHopDongHdr();
        BeanUtils.copyProperties(req, dataMap, "id");

        dataMap.setNguoiTaoId(userInfo.getId());
        dataMap.setNgayTao(new Date());
        dataMap.setTrangThai(Contains.DU_THAO);
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setMaDviTsan(String.join(",",req.getListMaDviTsan()));

        xhHopDongRepository.save(dataMap);
        saveDetail(req,dataMap.getId());
        return dataMap;
    }

    @Override
    public XhHopDongHdr update(XhHopDongHdrReq req) throws Exception {

        UserInfo userInfo = getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHopDongHdr> qOptional = xhHopDongRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!qOptional.get().getSoHd().equals(req.getSoHd())) {
            Optional<XhHopDongHdr> qOpHdong = xhHopDongRepository.findBySoHd(req.getSoHd());
            if (qOpHdong.isPresent())
                throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
        }

        if (!qOptional.get().getSoQdKq().equals(req.getSoQdKq())) {
            Optional<XhKqBdgHdr> checkSoQd = xhKqBdgHdrRepository.findBySoQdKq(req.getSoQdKq());
            if (!checkSoQd.isPresent())

                throw new Exception(
                        "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + req.getSoQdKq() + " không tồn tại");
        }

        XhHopDongHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req,dataDB, "id");

        dataDB.setNgaySua(new Date());
        dataDB.setNguoiSuaId(userInfo.getId());


        xhHopDongRepository.save(dataDB);

        saveDetail(req,dataDB.getId());
        return dataDB;
    }

    void saveDetail(XhHopDongHdrReq req,Long idHdr){
        xhHopDongDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHopDongDtlReq dtlReq : req.getChildren()) {
            XhHopDongDtl dtl = new XhHopDongDtl();
            BeanUtils.copyProperties(dtlReq,dtl,"id");
            dtl.setIdHdr(idHdr);
            xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl((dtlReq.getId()));
            for (XhDdiemNhapKhoReq nhapKhoReq : dtlReq.getChildren()) {
                XhHopDongDdiemNhapKho nhapKho = new XhHopDongDdiemNhapKho();
                BeanUtils.copyProperties(nhapKhoReq,nhapKho,"id");
                nhapKho.setIdDtl(dtl.getId());
                xhHopDongDdiemNhapKhoRepository.save(nhapKho);
            }
        }
    }

    @Override
    public XhHopDongHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Optional<XhHopDongHdr> qOptional = xhHopDongRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        XhHopDongHdr data = qOptional.get();
        Map<String, String> mapDmucDvi = getMapTenDvi();
        Map<String,String> hashMapDviLquan = getListDanhMucDviLq("NT");

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        return data;
    }

    @Override
    public XhHopDongHdr approve(XhHopDongHdrReq req) throws Exception {
        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhHopDongHdr> optional = xhHopDongRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = req.getTrangThai() + optional.get().getTrangThai();
        if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
            optional.get().setNguoiPduyetId(getUser().getId());
            optional.get().setNgayPduyet(getDateTimeNow());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(req.getTrangThai());

        return xhHopDongRepository.save(optional.get());
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        Optional<XhHopDongHdr> optional = xhHopDongRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)){
            throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
        }
        xhHopDongRepository.delete(optional.get());
        for (XhHopDongDtl hdDtl : xhHopDongDtlRepository.findAllByIdHdr(optional.get().getId()) ) {
            xhHopDongDdiemNhapKhoRepository.deleteAllByIdDtl(hdDtl.getId());
        }
        xhHopDongDtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if(Objects.isNull(listMulti)){
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        }
        for (Long id: listMulti) {
            this.delete(id);
        }
    }

    @Override
    public void export(XhHopDongHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq=new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhHopDongHdr> page = this.searchPage(req);
        List<XhHopDongHdr> data= page.getContent();

        String title="Danh sách hợp đồng mua";
        String[] rowsName=new String[]{"STT","Số HĐ","Tên hợp đồng","Ngày ký","Loại hàng hóa","Chủng loại hàng hóa","Chủ đầu tư","Nhà cung cấp","Giá trị hợp đồng","Trạng thái"};
        String fileName="danh-sach-hop-dong.xlsx";
        List<Object[]> dataList=new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            XhHopDongHdr hd=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hd.getSoHd();
            objs[2]=hd.getTenHd();
//            objs[3]=hd.getNgayKy();
            objs[4]=hd.getLoaiVthh();
            objs[5]=hd.getCloaiVthh();
            objs[6]=hd.getMaDvi();
//            objs[7]=hd.getIdNthau();
//            objs[8]=hd.getGtriHdSauVat();
            objs[9]=hd.getTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

//
//    public XhHopDongHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
//        if (StringUtils.isEmpty(strSearchReq.getStr()))
//            throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//        Optional<XhHopDongHdr> qOptional = bhHopDongRepository.findBySoHd(strSearchReq.getStr());
//
//        if (!qOptional.isPresent())
//            throw new UnsupportedOperationException("Không tồn tại bản ghi");
//
//        // Quy doi don vi kg = tan
////		List<BhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), BhHopDongDtl.class);
////		for (BhHopDongDtl dtl : dtls2) {
////			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
////		}
//
//        XhHopDongHdr dataDB = qOptional.get();
//        Map<String,String> mapVthh = getListDanhMucHangHoa();
//        dataDB.setDonViTinh(StringUtils.isEmpty(dataDB.getLoaiVthh()) ? null : mapVthh.get(dataDB.getDonViTinh()));
//        return this.detail(qOptional.get().getId().toString());
//    }
//
//    public Page<XhHopDongHdr> selectPage(BhHopDongSearchReq req, HttpServletResponse response) throws Exception {
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
//        Page<XhHopDongHdr> page = bhHopDongRepository.select(
//                req.getLoaiVthh(),
//                req.getSoHd(),
//                req.getTenHd(),
//                req.getNhaCcap(),
//                convertDateToString(req.getTuNgayKy()),
//                convertDateToString(req.getDenNgayKy()),
//                req.getTrangThai(),
//                pageable);
//        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
//        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
//
//        Set<Long> hopDongIds = page.getContent().stream().map(XhHopDongHdr::getId).collect(Collectors.toSet());
//        Map<Long, List<BhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = bhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
//                .stream().collect(Collectors.groupingBy(BhHopDongDdiemNhapKho::getIdHdongHdr));
//
//        Map<Long, List<BhHopDongDtl>> bhHdDtls = bhHopDongDtlRepository.findAllByIdHdrIn(hopDongIds)
//                .stream().collect(Collectors.groupingBy(BhHopDongDtl::getIdHdr));
//
//        Map<String,String> mapVthh = getListDanhMucHangHoa();
//
//        page.getContent().forEach(f -> {
//            f.setTenDvi( mapDmucDvi.get(f.getMaDvi()));
//            f.setTenVthh(mapDmucHh.get(f.getLoaiVthh()));
//            f.setTenCloaiVthh( mapDmucHh.get(f.getCloaiVthh()));
//            List<BhHopDongDdiemNhapKho> diaDiemNhapKhos = diaDiemNhapKhoMap.get(f.getId()) != null ? diaDiemNhapKhoMap.get(f.getId()) : new ArrayList<>();
//            List<BhHopDongDtl> bhHopDongDtls = bhHdDtls.get(f.getId());
//            if (!CollectionUtils.isEmpty(diaDiemNhapKhos)) {
//                diaDiemNhapKhos.forEach(d ->  {
//                    d.setTenDvi(mapDmucDvi.get(d.getMaDvi()));
//                });
//                f.setBhDdiemNhapKhoList(diaDiemNhapKhos);
//                f.setBhHopDongDtlList(bhHopDongDtls);
//            }
//            f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
//        });
//        return page;
//    }
//
//
//    public Page<XhHopDongHdr> colection(BhHopDongSearchReq req, HttpServletRequest req) throws Exception {
//        int page = PaginationSet.getPage(req.getPaggingReq().getPage());
//        int limit = PaginationSet.getLimit(req.getPaggingReq().getLimit());
//        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
//
//        Page<XhHopDongHdr> dataPage = bhHopDongRepository.findAll(BhHopDongSpecification.buildSearchQuery(req),
//                pageable);
//
//        Set<Long> hopDongIds = dataPage.getContent().stream().map(XhHopDongHdr::getId).collect(Collectors.toSet());
//        if (CollectionUtils.isEmpty(hopDongIds))
//            return dataPage;
//
//        Map<Long, List<BhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = bhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
//                .stream().collect(Collectors.groupingBy(BhHopDongDdiemNhapKho::getIdHdongHdr));
//
//        // Lay danh muc dung chung
//        Map<String, String> mapDmucDvi = getMapTenDvi();
//        for (XhHopDongHdr hdr : dataPage.getContent()) {
//            hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
//            hdr.setBhDdiemNhapKhoList(diaDiemNhapKhoMap.get(hdr.getId()));
//        }
//        return dataPage;
//    }
//


}
