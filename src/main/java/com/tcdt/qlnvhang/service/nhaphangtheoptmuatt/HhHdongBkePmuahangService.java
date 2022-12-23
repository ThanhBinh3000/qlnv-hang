package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;


import com.tcdt.qlnvhang.entities.FileDKemJoinHopDongMtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhHdongBkePmuahangService extends BaseServiceImpl {

    @Autowired
    HhHdongBkePmuahangRepository hhHdongBkePmuahangRepository;

    @Autowired
    HhThongTinDviDtuCcapRepository hhThongTinDviDtuCcapRepository;

    @Autowired
    HhDiaDiemGiaoNhanHangRepository hhDiaDiemGiaoNhanHangRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    HhPhuLucHopDongMttRepository hhPhuLucHopDongMttRepository;

    @Autowired
    HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;


    public Page<HhHdongBkePmuahangHdr> selectPage(SearchHhHdongBkePmh req, HttpServletResponse response) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
        UserInfo userInfo = UserUtils.getUserInfo();
        String dvql = userInfo.getDvql();
        Page<HhHdongBkePmuahangHdr> page;
        page = hhHdongBkePmuahangRepository.selectAll(req.getLoaiVthh(), req.getSoHd(), req.getTenHd(), req.getNhaCcap(), req.getNamHd(), convertDateToString(req.getTuNgayKy()), convertDateToString(req.getDenNgayKy()),  dvql, req.getTrangThai(),   pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();

        page.forEach(f ->{
            f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(mapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(mapVthh.get(f.getCloaiVthh()));
            f.setDonViTinh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapVthh.get(f.getDonViTinh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
        });

        return page;
    }

    @Transactional
    public HhHdongBkePmuahangHdr create(HhHdongBkePmuahangReq objReq) throws Exception{

        if (!StringUtils.isEmpty(objReq.getSoHd())){
            Optional<HhHdongBkePmuahangHdr> qOpHdong = hhHdongBkePmuahangRepository.findBySoHd(objReq.getSoHd());
            if (qOpHdong.isPresent()){
                throw new Exception("Số hợp đồng" + objReq.getSoHd() + " đã tồn tại ");
            }
        }

        Optional<HhQdPduyetKqcgHdr> checkSoQd = hhQdPduyetKqcgRepository.findBySoQd(objReq.getSoQdKqMtt());
        if (!checkSoQd.isPresent()){
            throw new Exception(
                    "Số quyết định phê duyệt kết quả lựa chọn nhà thầu " + objReq.getSoQdKqMtt() + " không tồn tại");
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        HhHdongBkePmuahangHdr dataMap = ObjectMapperUtils.map(objReq, HhHdongBkePmuahangHdr.class);

        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setMaDvi(userInfo.getDvql());

        List<FileDKemJoinHopDongMtt> dtls2 = new ArrayList<>();
        if (objReq.getFileDinhKems() != null){
            dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDongMtt.class);
            dtls2.forEach(f ->{
                f.setDataType(HhHdongBkePmuahangHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        dataMap.setFileDinhKems(dtls2);

        hhHdongBkePmuahangRepository.save(dataMap);

        this.saveDataChildren(dataMap, objReq);

        Optional<HhQdPduyetKqcgHdr> bySoQd = hhQdPduyetKqcgRepository.findById(dataMap.getIdQdKqMtt());
        if (bySoQd.isPresent()){
            bySoQd.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            hhQdPduyetKqcgRepository.save(bySoQd.get());
        }
return dataMap;
    }

    @Transactional()
    void saveDataChildren(HhHdongBkePmuahangHdr dataMap, HhHdongBkePmuahangReq objReq){
        hhThongTinDviDtuCcapRepository.deleteAllByIdHdr(dataMap.getId());
        for (HhThongTinDviDtuCcapReq ccapReq : objReq.getDetail()){
            HhThongTinDviDtuCcap ccap = new HhThongTinDviDtuCcap();
            BeanUtils.copyProperties(ccapReq, ccap,"id");
            ccap.setIdHdr(dataMap.getId());
            ccap.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
            hhThongTinDviDtuCcapRepository.save(ccap);
            hhDiaDiemGiaoNhanHangRepository.deleteAllByIdHdongDtl(dataMap.getId());
            for (HhDiaDiemGiaoNhanHangReq nhanHangReq : ccapReq.getChildren()){
                HhDiaDiemGiaoNhanHang nhanHang = new HhDiaDiemGiaoNhanHang();
                BeanUtils.copyProperties(nhanHangReq, nhanHang,"id");
                nhanHang.setIdHdongDtl(ccap.getId());
                nhanHang.setTrangThai(TrangThaiAllEnum.CHUA_TAO_QD.getId());
                hhDiaDiemGiaoNhanHangRepository.save(nhanHang);
            }
        }
    }

    @Transactional
    public HhHdongBkePmuahangHdr update (HhHdongBkePmuahangReq objReq) throws  Exception {
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu ");
        }

        Optional<HhHdongBkePmuahangHdr> qOptional = hhHdongBkePmuahangRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!StringUtils.isEmpty(objReq.getSoHd())){
            if (!objReq.getSoHd().equals(qOptional.get().getSoHd())) {
                Optional<HhHdongBkePmuahangHdr> qOpHdong = hhHdongBkePmuahangRepository.findBySoHd(objReq.getSoHd());
                if (qOpHdong.isPresent())
                    throw new Exception("Hợp đồng số" + objReq.getSoHd() + " đã tồn tại");
            }
        }

        if (!qOptional.get().getSoQdKqMtt().equals(objReq.getSoQdKqMtt())) {
            Optional<HhQdPduyetKqcgHdr> checkSoQd = hhQdPduyetKqcgRepository.findBySoQd(objReq.getSoQdKqMtt());
            if (!checkSoQd.isPresent())
                throw new Exception("Số quyết định kết quả mua trực tiếp" + objReq.getSoQdKqMtt() + " không tồn tại ");

        }

        HhHdongBkePmuahangHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(objReq, dataDB);

        List<FileDKemJoinHopDongMtt> dtls2 = new ArrayList<>();
        if (objReq.getFileDinhKems() != null){
            dtls2 = ObjectMapperUtils.mapAll(objReq.getFileDinhKems(), FileDKemJoinHopDongMtt.class);
            dtls2.forEach(f -> {
                f.setDataType(HhHdongBkePmuahangHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        dataDB.setFileDinhKems(dtls2);

        hhHdongBkePmuahangRepository.save(dataDB);
        this.saveDataChildren(dataDB, objReq);
        return dataDB;
    }

    public  HhHdongBkePmuahangHdr detail(String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<HhHdongBkePmuahangHdr> qOptional = hhHdongBkePmuahangRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");


        Map<String, String> mapDmucDvi = getMapTenDvi();
        Map<String, String> mapVthh = getListDanhMucHangHoa();

        qOptional.get().setTenLoaiVthh(mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setDonViTinh( mapVthh.get(qOptional.get().getDonViTinh()));
//        qOptional.get().setHhPhuLucHdongList(hhPhuLucRepository.findBySoHd(qOptional.get().getSoHd()));

        List<HhThongTinDviDtuCcap> allByIdHdr = hhThongTinDviDtuCcapRepository.findAllByIdHdr(qOptional.get().getId());
        allByIdHdr.forEach(item -> {
            List<HhDiaDiemGiaoNhanHang> allByIdHdongDtl = hhDiaDiemGiaoNhanHangRepository.findAllByIdHdongDtl(item.getId());
            allByIdHdongDtl.forEach(s -> {
                s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
                s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()));
            });
            item.setChildren(allByIdHdongDtl);
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
        });
        qOptional.get().setDetails(allByIdHdr);
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

        return qOptional.get();
    }

    public  HhHdongBkePmuahangHdr approve(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        Optional<HhHdongBkePmuahangHdr> optional = hhHdongBkePmuahangRepository.findById(Long.valueOf(stReq.getId()));
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu");

        String status = stReq.getTrangThai() + optional.get().getTrangThai();
        if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
            optional.get().setNguoiPduyet(getUser().getUsername());
            optional.get().setNgayPduyet(getDateTimeNow());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setNgayKy(getDateTimeNow());
        optional.get().setTrangThai(stReq.getTrangThai());

        return hhHdongBkePmuahangRepository.save(optional.get());
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<HhHdongBkePmuahangHdr> optional = hhHdongBkePmuahangRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xoá");

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("Chỉ thực hiện xóa thông tin đấu thầu ở trạng thái bản nháp hoặc từ chối");
        }

        List<HhThongTinDviDtuCcap>  hopdong = hhThongTinDviDtuCcapRepository.findAllByIdHdr(idSearchReq.getId());
        if (hopdong != null && hopdong.size() > 0) {
            for (HhThongTinDviDtuCcap ct : hopdong) {
                List<HhDiaDiemGiaoNhanHang> hopDongDtl = hhDiaDiemGiaoNhanHangRepository.findAllByIdHdongDtl(ct.getId());
                hhDiaDiemGiaoNhanHangRepository.deleteAll(hopDongDtl);
            }
            hhThongTinDviDtuCcapRepository.deleteAll(hopdong);
        }
        hhThongTinDviDtuCcapRepository.deleteAllByIdHdr(idSearchReq.getId());
        hhHdongBkePmuahangRepository.delete(optional.get());

    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhHdongBkePmuahangHdr> optional = hhHdongBkePmuahangRepository.findByIdIn(idSearchReq.getIdList());
        if (optional.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhHdongBkePmuahangHdr hd: optional ) {
            if (!hd.getTrangThai().equals(Contains.DUTHAO)){
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            List<HhThongTinDviDtuCcap> dviDtuCcaps = hhThongTinDviDtuCcapRepository.findAllByIdHdr(hd.getId());
            List<Long> longList = dviDtuCcaps.stream().map(HhThongTinDviDtuCcap::getId).collect(Collectors.toList());
            hhDiaDiemGiaoNhanHangRepository.deleteAllByIdHdongDtlIn(longList);
        }
        hhThongTinDviDtuCcapRepository.deleteAllByIdHdrIn(idSearchReq.getIdList());
        hhHdongBkePmuahangRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void exportList(SearchHhHdongBkePmh objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhHdongBkePmuahangHdr> page = this.selectPage(objReq, response);
        List<HhHdongBkePmuahangHdr> data = page.getContent();

        String title = "Danh sách hợp đồng mua";
        String[] rowsName = new String[]{"STT", "Năm HĐ", "QĐ PD KH MTT", "SL HĐ cần ký", "SL HĐ đã ký", "Thời hạn nhập kho", "Loại hành hóa", "Chủng loại hành hóa", "Tổng giá trị hợp đồng", "Trạng thái", "Trạng thái NH"};
        String fileName = "danh-sach-hop-dong.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhHdongBkePmuahangHdr hd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = hd.getNamHd();
            objs[2] = hd.getSoQdPdKhMtt();
            objs[3] = hd.getSoQdKqMtt();
            objs[4] = null;
            objs[5] = null;
            objs[6] = hd.getTgianNkho();
            objs[7] = hd.getTenLoaiVthh();
            objs[8] = hd.getTenCloaiVthh();
            objs[9] = null;
            objs[10] = hd.getTrangThai();
            objs[11] = null;
            dataList.add(objs);

        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }



//    public Page<HhHdongBkePmuahangHdr> searchPage(SearchHhHdongBkePmh objReq) throws Exception{
//        UserInfo userInfo = SecurityContextService.getUser();
//        Pageable pageable= PageRequest.of(objReq.getPaggingReq().getPage(),
//                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
//        Page<HhHdongBkePmuahangHdr> data= hhHdongBkePmuahangRepository.searchPage(
//                objReq.getNamHd(),
//                objReq.getSoHdong(),
//                objReq.getDviMua(),
//                objReq.getTenHdong(),
//                Contains.convertDateToString(objReq.getNgayKyHdTu()),
//                Contains.convertDateToString(objReq.getNgayKyHdDen()),
//                objReq.getTrangThaiHd(),
//                objReq.getTrangThaiNh(),
//                userInfo.getDvql(),
//                pageable);
//        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
//        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
//        List<HhHdongBkePmuahangHdr> hhHdr=data.getContent();
//        hhHdr.forEach(f -> {
//            List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(f.getId(),Contains.CUNG_CAP);
//            List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(f.getId(), Contains.DAU_TU);
//            List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(f.getId());
//            f.setThongTinDviCungCap(listTtCc);
//            f.setThongTinChuDauTu(listTtDtu);
//            f.setDiaDiemGiaoNhanHangList(listDdNh);
//            f.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
//            f.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiNh()));
//            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDmdv.get(f.getMaDvi()));
//            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
//            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
//            for (HhThongTinDviDtuCcap dviDtu :listTtDtu){
//                f.setBenMua(dviDtu.getTenDvi());
//                f.setDDiemBmua(dviDtu.getDiaChi());
//            }
//            for (HhThongTinDviDtuCcap dviCcap :listTtCc){
//                f.setBenBan(dviCcap.getTenDvi());
//            }
//            for (HhDiaDiemGiaoNhanHang giaTri :listDdNh){
//                f.setGiaTriHd(giaTri.getThanhTien());
//            }
//
//        });
//        return data;
//    }
//
//    @Transactional
//    public HhHdongBkePmuahangHdr save(HhHdongBkePmuahangReq objReq) throws Exception{
//        UserInfo userInfo =SecurityContextService.getUser();
//        if (userInfo == null){
//            throw new Exception("Bad request.");
//        }
//        Optional<HhHdongBkePmuahangHdr> optional =hhHdongBkePmuahangRepository.findAllBySoHdong(objReq.getSoHdong());
//        if(optional.isPresent()){
//            throw new Exception("Số hợp đồng đã tồn tại");
//        }
//        HhHdongBkePmuahangHdr data = new ModelMapper().map(objReq,HhHdongBkePmuahangHdr.class);
//        data.setNgayTao(new Date());
//        data.setNguoiTao(userInfo.getUsername());
//        data.setTrangThaiHd(Contains.DUTHAO);
//        data.setTrangThaiNh(null);
//        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
//        data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
//        data.setMaDvi(userInfo.getDvql());
//        HhHdongBkePmuahangHdr created= hhHdongBkePmuahangRepository.save(data);
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),"HH_HDONG_BKE_PMUAHANG_HDR");
//        created.setFileDinhKems(fileDinhKems);
//        List<FileDinhKem> canCuPhapLy = fileDinhKemService.saveListFileDinhKem(objReq.getCanCuPhapLy(),data.getId(),"HH_HDONG_BKE_PMUAHANG_HDR");
//        created.setFileDinhKems(canCuPhapLy);
//        this.saveCtiet(objReq,data);
//        return data;
//    }
//
//    public void saveCtiet(HhHdongBkePmuahangReq objReq, HhHdongBkePmuahangHdr data){
//        for (HhThongTinDviDtuCcapReq thongTinDviDtuCcap :objReq.getThongTinChuDauTu()){
//            HhThongTinDviDtuCcap thongTin=new ModelMapper().map(thongTinDviDtuCcap,HhThongTinDviDtuCcap.class);
//            thongTin.setId(null);
//            thongTin.setIdHdr(data.getId());
//            thongTin.setType(Contains.DAU_TU);
//            hhThongTinDviDtuCcapRepository.save(thongTin);
//        }
//        for (HhThongTinDviDtuCcapReq thongTinDviDtuCcap :objReq.getThongTinDviCungCap()){
//            HhThongTinDviDtuCcap thongTin=new ModelMapper().map(thongTinDviDtuCcap,HhThongTinDviDtuCcap.class);
//            thongTin.setId(null);
//            thongTin.setIdHdr(data.getId());
//            thongTin.setType(Contains.CUNG_CAP);
//            hhThongTinDviDtuCcapRepository.save(thongTin);
//        }
//        for (HhDiaDiemGiaoNhanHangReq diaDiemGiaoNhanHangList: objReq.getDiaDiemGiaoNhanHangList()){
//            HhDiaDiemGiaoNhanHang diaDiemGiaoNhanHang =new ModelMapper().map(diaDiemGiaoNhanHangList,HhDiaDiemGiaoNhanHang.class);
//            diaDiemGiaoNhanHang.setId(null);
//            diaDiemGiaoNhanHang.setIdHdr(data.getId());
//            BigDecimal thanhTien =diaDiemGiaoNhanHang.getDonGiaVat().multiply(diaDiemGiaoNhanHang.getSoLuong());
//            diaDiemGiaoNhanHang.setThanhTien(thanhTien);
//            hhDiaDiemGiaoNhanHangRepository.save(diaDiemGiaoNhanHang);
//        }
//    }
//
//    @Transactional
//    public HhHdongBkePmuahangHdr update(HhHdongBkePmuahangReq objReq) throws Exception{
//        UserInfo userInfo =SecurityContextService.getUser();
//        if (userInfo == null){
//            throw new Exception("Bad request.");
//        }
//        Optional<HhHdongBkePmuahangHdr>optional = hhHdongBkePmuahangRepository.findById(objReq.getId());
//        if (!optional.isPresent()){
//            throw new Exception("id không tồn tại");
//        }
//        Optional<HhHdongBkePmuahangHdr> soHdong =hhHdongBkePmuahangRepository.findAllBySoHdong(objReq.getSoHdong());
//        if(soHdong.isPresent()){
//            if(!soHdong.get().getId().equals(objReq.getId())) {
//                throw new Exception("Số hợp đồng đã tồn tại");
//            }
//        }
//        HhHdongBkePmuahangHdr data=optional.get();
//        HhHdongBkePmuahangHdr dataMap = new ModelMapper().map(objReq,HhHdongBkePmuahangHdr.class);
//        updateObjectToObject(data,dataMap);
//        data.setNgaySua(new Date());
//        data.setNguoiSua(userInfo.getUsername());
//        HhHdongBkePmuahangHdr created= hhHdongBkePmuahangRepository.save(data);
//        List<HhThongTinDviDtuCcap> thongTinDviDtuCcap = hhThongTinDviDtuCcapRepository.findAllByIdHdr(objReq.getId());
//        List<HhDiaDiemGiaoNhanHang> diemGiaoNhanHang = hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(objReq.getId());
//        hhThongTinDviDtuCcapRepository.deleteAll(thongTinDviDtuCcap);
//        hhDiaDiemGiaoNhanHangRepository.deleteAll(diemGiaoNhanHang);
//        this.saveCtiet(objReq,data);
//        return created;
//    }
//
//    public HhHdongBkePmuahangHdr detail(String ids) throws Exception{
//        Optional<HhHdongBkePmuahangHdr> optional = hhHdongBkePmuahangRepository.findById(Long.valueOf(ids));
//        if (!optional.isPresent()){
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        HhHdongBkePmuahangHdr data= optional.get();
//        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
//        Map<String,String> hashMapDmdv = getListDanhMucDvi(null,null,"01");
//        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh())?null:hashMapDmhh.get(data.getLoaiVthh()));
//        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh())?null:hashMapDmhh.get(data.getCloaiVthh()));
//        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
//        data.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiNh()));
//        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDmdv.get(data.getMaDvi()));
//        List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(data.getId(),Contains.CUNG_CAP);
//        List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(data.getId(), Contains.DAU_TU);
//        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(data.getId());
//        data.setThongTinDviCungCap(listTtCc);
//        data.setThongTinChuDauTu(listTtDtu);
//        data.setDiaDiemGiaoNhanHangList(listDdNh);
//
//        return data;
//    }
//
//    @Transient
//    public void delete(IdSearchReq idSearchReq) throws Exception{
//        Optional<HhHdongBkePmuahangHdr> optional= hhHdongBkePmuahangRepository.findById(idSearchReq.getId());
//        if (!optional.isPresent()){
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        if (!optional.get().getTrangThaiHd().equals(Contains.DUTHAO)){
//            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
//        }
//        if (!optional.get().getTrangThaiNh().equals(Contains.DUTHAO)){
//            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
//        }
//        HhHdongBkePmuahangHdr data = optional.get();
//
//        List<HhThongTinDviDtuCcap> listTt=hhThongTinDviDtuCcapRepository.findAllByIdHdr(data.getId());
//        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdr(data.getId());
//        List<HhPhuLucHopDongMtt> hhPhuLuc = hhPhuLucHopDongMttRepository.findAllByIdHdHdr(data.getId());
//        hhPhuLucHopDongMttRepository.deleteAll(hhPhuLuc);
//        hhDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
//        hhThongTinDviDtuCcapRepository.deleteAll(listTt);
//        hhHdongBkePmuahangRepository.delete(data);
//
//    }
//
//    @Transient
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
//        List<HhHdongBkePmuahangHdr> list= hhHdongBkePmuahangRepository.findAllByIdIn(idSearchReq.getIdList());
//        if (list.isEmpty()){
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        for (HhHdongBkePmuahangHdr bkePmuahangHdr:list){
//            if (!bkePmuahangHdr.getTrangThaiHd().equals(Contains.DUTHAO)){
//                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
//            }
//        }
//        for (HhHdongBkePmuahangHdr bkePmuahangHdr  : list){
//            if (!bkePmuahangHdr.getTrangThaiNh().equals(Contains.DUTHAO)){
//                throw new Exception("Chỉ thực hiên xóa với quyết định trạng thái bản nháp hoặc từ chối");
//            }
//        }
//        List<Long> listIdHdr=list.stream().map(HhHdongBkePmuahangHdr::getId).collect(Collectors.toList());
//        List<HhThongTinDviDtuCcap> listTt=hhThongTinDviDtuCcapRepository.findAllByIdHdrIn(listIdHdr);
//        List<HhDiaDiemGiaoNhanHang> listDdNh=hhDiaDiemGiaoNhanHangRepository.findAllByIdHdrIn(listIdHdr);
//        List<HhPhuLucHopDongMtt> hhPhuLuc = hhPhuLucHopDongMttRepository.findAllByIdHdHdrIn(listIdHdr);
//        hhPhuLucHopDongMttRepository.deleteAll(hhPhuLuc);
//        hhDiaDiemGiaoNhanHangRepository.deleteAll(listDdNh);
//        hhThongTinDviDtuCcapRepository.deleteAll(listTt);
//        hhHdongBkePmuahangRepository.deleteAll(list);
//
//    }
//
//    public  void export(SearchHhHdongBkePmh objReq, HttpServletResponse response) throws Exception{
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        objReq.setPaggingReq(paggingReq);
//        Page<HhHdongBkePmuahangHdr> page=this.searchPage(objReq);
//        List<HhHdongBkePmuahangHdr> data=page.getContent();
//
//        String title="Danh sách hợp đồng mua trực tiếp";
//        String[] rowsName=new String[]{"STT","Năm kế hoạch","Số hợp đồng","Ngày ký","Loại hang hóa","Chủng loại hàng hóa","Giá trị hợp đồng","Chủ đầu tư","Nhà thầu bên mua","Địa chỉ bên mua","Trạng thái"};
//        String fileName="danh-sach-hop-dong-mua-truc-tiep.xlsx";
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs=null;
//        for (int i=0;i<data.size();i++){
//            HhHdongBkePmuahangHdr dx=data.get(i);
//            List<HhThongTinDviDtuCcap> listTtCc=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(dx.getId(),Contains.CUNG_CAP);
//            List<HhThongTinDviDtuCcap> listTtDtu=hhThongTinDviDtuCcapRepository.findAllByIdHdrAndType(dx.getId(), Contains.DAU_TU);
//            objs=new Object[rowsName.length];
//            objs[0]=i;
//            objs[1]=dx.getNamHd();
//            objs[2]=dx.getSoHdong();
//            objs[3]=dx.getNgayHluc();
//            objs[4]=dx.getTenLoaiVthh();
//            objs[5]=dx.getTenCloaiVthh();
//            objs[6]=dx.getThanhTien();
//            for (HhThongTinDviDtuCcap dt:listTtDtu){
//                objs[7]=dt.getTenDvi();
//            }
//            for (HhThongTinDviDtuCcap cc:listTtCc){
//                objs[8]=cc.getTenDvi();
//                objs[9]=cc.getDiaChi();
//            }
//            objs[10]=dx.getTenTrangThaiHd();
//            dataList.add(objs);
//        }
//        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
//        ex.export();
//    }
//
//    public HhHdongBkePmuahangHdr approve(StatusReq statusReq) throws Exception{
//        UserInfo userInfo=SecurityContextService.getUser();
//        if(StringUtils.isEmpty(statusReq.getId())){
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//        Optional<HhHdongBkePmuahangHdr> optional =hhHdongBkePmuahangRepository.findById(Long.valueOf(statusReq.getId()));
//        if (!optional.isPresent()){
//            throw new Exception("Không tìm thấy dữ liệu");
//        }
//
//        String status= statusReq.getTrangThai()+optional.get().getTenTrangThaiHd();
//        switch (status){
//            case Contains.DAKY + Contains.DUTHAO:
//                optional.get().setNguoiKy(getUser().getUsername());
//                optional.get().setNgayKy(getDateTimeNow());
//                break;
//            default:
//                throw new Exception("Phê duyệt không thành công");
//        }
//        optional.get().setTrangThaiHd(statusReq.getTrangThai());
//        HhHdongBkePmuahangHdr created = hhHdongBkePmuahangRepository.save(optional.get());
//        return created;
//    }

}
