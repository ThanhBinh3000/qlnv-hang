package com.tcdt.qlnvhang.service.banhang;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinBhHopDong;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhPhuLucRepository;
import com.tcdt.qlnvhang.repository.HhQdPduyetKqlcntHdrRepository;
import com.tcdt.qlnvhang.repository.banhang.BhHopDongDdiemNhapKhoRepository;
import com.tcdt.qlnvhang.repository.banhang.BhHopDongDtlRepository;
import com.tcdt.qlnvhang.repository.banhang.BhHopDongRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.banhang.BhDdiemNhapKhoReq;
import com.tcdt.qlnvhang.request.object.banhang.BhHopDongDtlReq;
import com.tcdt.qlnvhang.request.object.banhang.BhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.banhang.BhHopDongSearchReq;
import com.tcdt.qlnvhang.secification.BhHopDongSpecification;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BhHopDongService extends BaseServiceImpl {
    @Autowired
    private BhHopDongRepository bhHopDongRepository;

    @Autowired
    private BhHopDongDtlRepository bhHopDongDtlRepository;

    @Autowired
    private HhPhuLucRepository hhPhuLucRepository;

    @Autowired
    private BhHopDongDdiemNhapKhoRepository bhHopDongDdiemNhapKhoRepository;

    @Autowired
    private HhQdPduyetKqlcntHdrRepository hhQdPduyetKqlcntHdrRepository;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public BhHopDongHdr create(BhHopDongHdrReq objReq) throws Exception {
        if(objReq.getLoaiVthh().startsWith("02")){
            return createVatTu(objReq);
        }else{
            return createLuongThuc(objReq);
        }
    }

    public BhHopDongHdr createVatTu(BhHopDongHdrReq objReq) throws Exception {

        Optional<BhHopDongHdr> qOpHdong = bhHopDongRepository.findBySoHd(objReq.getSoHd());
        if (qOpHdong.isPresent())
            throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");

        Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
        if (!checkSoQd.isPresent())
            throw new Exception(
                    "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        BhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, BhHopDongHdr.class);

        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.TAO_MOI);
        dataMap.setMaDvi(userInfo.getDvql());

        // File dinh kem cua goi thau
        List<FileDKemJoinBhHopDong> dtls2 = new ArrayList<FileDKemJoinBhHopDong>();
        if (objReq.getFileDinhKems() != null) {
            dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinBhHopDong.class);
            dtls2.forEach(f -> {
                f.setDataType(BhHopDongHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        dataMap.setFileDinhKems(dtls2);

        bhHopDongRepository.save(dataMap);

        for (BhHopDongDtlReq cTietReq :objReq.getBhHopDongDtlReqList()){
            BhHopDongDtl cTiet=new ModelMapper().map(cTietReq,BhHopDongDtl.class);
            cTiet.setId(null);
            cTiet.setIdHdr(dataMap.getId());
            bhHopDongDtlRepository.save(cTiet);
        }


        for(BhDdiemNhapKhoReq ddNhapRq : objReq.getBhDdiemNhapKhoReqList()){
            BhHopDongDdiemNhapKho ddNhap = ObjectMapperUtils.map(ddNhapRq, BhHopDongDdiemNhapKho.class);
            ddNhap.setIdHdongHdr(dataMap.getId());
            bhHopDongDdiemNhapKhoRepository.save(ddNhap);
        }
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        dataMap.setDonViTinh(StringUtils.isEmpty(dataMap.getLoaiVthh()) ? null : mapVthh.get(dataMap.getDonViTinh()));
        return dataMap;
    }

    public BhHopDongHdr createLuongThuc(BhHopDongHdrReq objReq) throws Exception {
        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
            throw new Exception("Loại vật tư hàng hóa không phù hợp");

        Optional<BhHopDongHdr> qOpHdong = bhHopDongRepository.findBySoHd(objReq.getSoHd());
        if (qOpHdong.isPresent())
            throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");

        Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
        if (!checkSoQd.isPresent())
            throw new Exception(
                    "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        BhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, BhHopDongHdr.class);

        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.TAO_MOI);
        dataMap.setMaDvi(userInfo.getDvql());

        // File dinh kem cua goi thau
        List<FileDKemJoinBhHopDong> dtls2 = new ArrayList<FileDKemJoinBhHopDong>();
        if (objReq.getFileDinhKems() != null) {
            dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinBhHopDong.class);
            dtls2.forEach(f -> {
                f.setDataType(BhHopDongHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        dataMap.setFileDinhKems(dtls2);

        bhHopDongRepository.save(dataMap);

        for (BhHopDongDtlReq cTietReq :objReq.getBhHopDongDtlReqList()){
            BhHopDongDtl cTiet=new ModelMapper().map(cTietReq,BhHopDongDtl.class);
            cTiet.setId(null);
            cTiet.setIdHdr(dataMap.getId());
            bhHopDongDtlRepository.save(cTiet);
        }

        for(BhDdiemNhapKhoReq ddNhapRq : objReq.getBhDdiemNhapKhoReqList()){
            BhHopDongDdiemNhapKho ddNhap = ObjectMapperUtils.map(ddNhapRq, BhHopDongDdiemNhapKho.class);
            ddNhap.setIdHdongHdr(dataMap.getId());
            bhHopDongDdiemNhapKhoRepository.save(ddNhap);
        }

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        dataMap.setDonViTinh(StringUtils.isEmpty(dataMap.getLoaiVthh()) ? null : mapVthh.get(dataMap.getDonViTinh()));
        return dataMap;
    }


    public BhHopDongHdr update(BhHopDongHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<BhHopDongHdr> qOptional = bhHopDongRepository.findById(objReq.getId());
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (!qOptional.get().getSoHd().equals(objReq.getSoHd())) {
            Optional<BhHopDongHdr> qOpHdong = bhHopDongRepository.findBySoHd(objReq.getSoHd());
            if (qOpHdong.isPresent())
                throw new Exception("Hợp đồng số " + objReq.getSoHd() + " đã tồn tại");
        }

        if (!qOptional.get().getCanCu().equals(objReq.getCanCu())) {
            Optional<HhQdPduyetKqlcntHdr> checkSoQd = hhQdPduyetKqlcntHdrRepository.findBySoQd(objReq.getCanCu());
            if (!checkSoQd.isPresent())

                throw new Exception(
                        "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getCanCu() + " không tồn tại");
        }

        BhHopDongHdr dataDB = qOptional.get();
        BhHopDongHdr dataMap = ObjectMapperUtils.map(objReq, BhHopDongHdr.class);

        updateObjectToObject(dataDB, dataMap);

        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSua(getUser().getUsername());
        // File dinh kem cua goi thau
        List<FileDKemJoinBhHopDong> dtls2 = new ArrayList<FileDKemJoinBhHopDong>();
        if (objReq.getFileDinhKems() != null) {
            dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinBhHopDong.class);
            dtls2.forEach(f -> {
                f.setDataType(BhHopDongHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        dataDB.setFileDinhKems(dtls2);

//		UnitScaler.reverseFormatList(dataMap.getChildren(), Contains.DVT_TAN);

        bhHopDongRepository.save(dataDB);
        bhHopDongDtlRepository.deleteAllByIdHdr(dataDB.getId());
        for (BhHopDongDtlReq cTietReq :objReq.getBhHopDongDtlReqList()){
            BhHopDongDtl cTiet=new ModelMapper().map(cTietReq,BhHopDongDtl.class);
            cTiet.setId(null);
            cTiet.setIdHdr(dataDB.getId());
            bhHopDongDtlRepository.save(cTiet);
        }
        List<BhHopDongDtlReq> dtlReqList = objReq.getBhHopDongDtlReqList();
        List<HhDviLquan> dtls1 = ObjectMapperUtils.mapAll(dtlReqList, HhDviLquan.class);
    	dataDB.setHhDviLquanList(dtls1);
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        dataDB.setDonViTinh(StringUtils.isEmpty(dataDB.getLoaiVthh()) ? null : mapVthh.get(dataDB.getDonViTinh()));
        return dataDB;
    }

    public BhHopDongHdr detail(String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<BhHopDongHdr> qOptional = bhHopDongRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        // Quy doi don vi kg = tan
//		List<BhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), BhHopDongDtl.class);
//		for (BhHopDongDtl dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}
        Map<String, String> mapDmucDvi = getMapTenDvi();
        Map<String,String> hashMapDviLquan = getListDanhMucDviLq("NT");

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        qOptional.get().setTenVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(StringUtils.isEmpty(qOptional.get().getMaDvi()) ? null :mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setDonViTinh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getDonViTinh()));
        qOptional.get().setHhPhuLucHdongList(hhPhuLucRepository.findBySoHd(qOptional.get().getSoHd()));
        qOptional.get().setBhDdiemNhapKhoList(bhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdr(Long.parseLong(ids)));
        qOptional.get().setBhHopDongDtlList(bhHopDongDtlRepository.findAllByIdHdr(Long.parseLong(ids)));
        qOptional.get().setTenNthau(hashMapDviLquan.get(String.valueOf(Double.parseDouble(qOptional.get().getIdNthau().toString()))));

        return qOptional.get();
    }

    public BhHopDongHdr findBySoHd(StrSearchReq strSearchReq) throws Exception {
        if (StringUtils.isEmpty(strSearchReq.getStr()))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<BhHopDongHdr> qOptional = bhHopDongRepository.findBySoHd(strSearchReq.getStr());

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        // Quy doi don vi kg = tan
//		List<BhHopDongDtl> dtls2 = ObjectMapperUtils.mapAll(qOptional.get().getChildren(), BhHopDongDtl.class);
//		for (BhHopDongDtl dtl : dtls2) {
//			UnitScaler.formatList(dtl.getChildren(), Contains.DVT_TAN);
//		}

        BhHopDongHdr dataDB = qOptional.get();
        Map<String,String> mapVthh = getListDanhMucHangHoa();
        dataDB.setDonViTinh(StringUtils.isEmpty(dataDB.getLoaiVthh()) ? null : mapVthh.get(dataDB.getDonViTinh()));
        return this.detail(qOptional.get().getId().toString());
    }

    public Page<BhHopDongHdr> selectPage(BhHopDongSearchReq req, HttpServletResponse response) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<BhHopDongHdr> page = bhHopDongRepository.select(
                req.getLoaiVthh(),
                req.getSoHd(),
                req.getTenHd(),
                req.getNhaCcap(),
                convertDateToString(req.getTuNgayKy()),
                convertDateToString(req.getDenNgayKy()),
                req.getTrangThai(),
                pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();

        Set<Long> hopDongIds = page.getContent().stream().map(BhHopDongHdr::getId).collect(Collectors.toSet());
        Map<Long, List<BhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = bhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
                .stream().collect(Collectors.groupingBy(BhHopDongDdiemNhapKho::getIdHdongHdr));

        Map<Long, List<BhHopDongDtl>> bhHdDtls = bhHopDongDtlRepository.findAllByIdHdrIn(hopDongIds)
                .stream().collect(Collectors.groupingBy(BhHopDongDtl::getIdHdr));

        Map<String,String> mapVthh = getListDanhMucHangHoa();

        page.getContent().forEach(f -> {
            f.setTenDvi( mapDmucDvi.get(f.getMaDvi()));
            f.setTenVthh(mapDmucHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh( mapDmucHh.get(f.getCloaiVthh()));
            List<BhHopDongDdiemNhapKho> diaDiemNhapKhos = diaDiemNhapKhoMap.get(f.getId()) != null ? diaDiemNhapKhoMap.get(f.getId()) : new ArrayList<>();
            List<BhHopDongDtl> bhHopDongDtls = bhHdDtls.get(f.getId());
            if (!CollectionUtils.isEmpty(diaDiemNhapKhos)) {
                diaDiemNhapKhos.forEach(d ->  {
                    d.setTenDvi(mapDmucDvi.get(d.getMaDvi()));
                });
                f.setBhDdiemNhapKhoList(diaDiemNhapKhos);
                f.setBhHopDongDtlList(bhHopDongDtls);
            }
            f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
        });
        return page;
    }


    public Page<BhHopDongHdr> colection(BhHopDongSearchReq objReq, HttpServletRequest req) throws Exception {
        int page = PaginationSet.getPage(objReq.getPaggingReq().getPage());
        int limit = PaginationSet.getLimit(objReq.getPaggingReq().getLimit());
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());

        Page<BhHopDongHdr> dataPage = bhHopDongRepository.findAll(BhHopDongSpecification.buildSearchQuery(objReq),
                pageable);

        Set<Long> hopDongIds = dataPage.getContent().stream().map(BhHopDongHdr::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(hopDongIds))
            return dataPage;

        Map<Long, List<BhHopDongDdiemNhapKho>> diaDiemNhapKhoMap = bhHopDongDdiemNhapKhoRepository.findAllByIdHdongHdrIn(hopDongIds)
                .stream().collect(Collectors.groupingBy(BhHopDongDdiemNhapKho::getIdHdongHdr));

        // Lay danh muc dung chung
        Map<String, String> mapDmucDvi = getMapTenDvi();
        for (BhHopDongHdr hdr : dataPage.getContent()) {
            hdr.setTenDvi(mapDmucDvi.get(hdr.getMaDvi()));
            hdr.setBhDdiemNhapKhoList(diaDiemNhapKhoMap.get(hdr.getId()));
        }
        return dataPage;
    }


    public BhHopDongHdr approve(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        Optional<BhHopDongHdr> optional = bhHopDongRepository.findById(Long.valueOf(stReq.getId()));
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu");

        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
//			case Contains.DUYET + Contains.MOI_TAO:
//				optional.get().setNguoiGuiDuyet(getUser().getUsername());
//				optional.get().setNgayGuiDuyet(getDateTimeNow());
//				break;
//			case Contains.TU_CHOI + Contains.CHO_DUYET:
//				optional.get().setNguoiPduyet(getUser().getUsername());
//				optional.get().setNgayPduyet(getDateTimeNow());
//				optional.get().setLdoTuchoi(stReq.getLyDo());
//				break;
            case Contains.DUYET + Contains.MOI_TAO:
                optional.get().setNguoiPduyet(getUser().getUsername());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(stReq.getTrangThai());

        // TODO: Cap nhat lai tinh trang hien thoi cua kho sau khi phe duyet quyet dinh
        // giao nhiem vu nhap hang
        return bhHopDongRepository.save(optional.get());
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<BhHopDongHdr> optional = bhHopDongRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xoá");

        if (!optional.get().getTrangThai().equals(Contains.TAO_MOI)
                && !optional.get().getTrangThai().equals(Contains.TU_CHOI))
            throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");

        bhHopDongRepository.delete(optional.get());

    }

    public  void exportList(BhHopDongSearchReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq=new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<BhHopDongHdr> page=this.selectPage(objReq,response);
        List<BhHopDongHdr> data= page.getContent();

        String title="Danh sách hợp đồng mua";
        String[] rowsName=new String[]{"STT","Số HĐ","Tên hợp đồng","Ngày ký","Loại hàng hóa","Chủng loại hàng hóa","Chủ đầu tư","Nhà cung cấp","Giá trị hợp đồng","Trạng thái"};
        String fileName="danh-sach-hop-dong.xlsx";
        List<Object[]> dataList=new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            BhHopDongHdr hd=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hd.getSoHd();
            objs[2]=hd.getTenHd();
            objs[3]=hd.getNgayKy();
            objs[4]=hd.getLoaiVthh();
            objs[5]=hd.getCloaiVthh();
            objs[6]=hd.getMaDvi();
            objs[7]=hd.getIdNthau();
            objs[8]=hd.getGtriHdSauVat();
            objs[9]=hd.getTrangThai();
            dataList.add(objs);

        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public void deleteListId(List<Long> listId){
        bhHopDongDtlRepository.deleteAllByIdHdrIn(listId);
        fileDinhKemService.deleteMultiple(listId, Lists.newArrayList("BH_HOP_DONG_HDR"));
        bhHopDongRepository.deleteAllByIdIn(listId);
    }



}
