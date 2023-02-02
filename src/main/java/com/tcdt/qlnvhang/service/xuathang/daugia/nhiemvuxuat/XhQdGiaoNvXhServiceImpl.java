package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvXhDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
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
import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl implements XhQdGiaoNvXhService {

    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;

    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;

    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhQdGiaoNvXh> searchPage(XhQdGiaoNvuXuatSearchReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdGiaoNvXh> data  = xhQdGiaoNvXhRepository.searchPage(
                objReq.getNam(),
                objReq.getSoQd(),
                objReq.getLoaiVthh(),
                objReq.getTrichYeu(),
                convertDateToString(objReq.getNgayTaoTu()),
                convertDateToString(objReq.getNgayTaoDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable
        );
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(item ->{
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThaiXh()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
            item.setTenLoaiVthh(mapDmucHh.get(item.getLoaiVthh()));
            item.setTenCloaiVthh(mapDmucHh.get(item.getCloaiVthh()));
        });
        return data;
    }

    @Override
    @Transactional
    public XhQdGiaoNvXh create(XhQdGiaoNvuXuatReq objReq) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }


        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findAllBySoQd(objReq.getSoQd());
        if (optional.isPresent()){
            throw new Exception("Số quyết định đã tồn tại");
        }

        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        XhQdGiaoNvXh dataMap = new XhQdGiaoNvXh();
        BeanUtils.copyProperties(objReq, dataMap, "id");

        dataMap.setNguoiTaoId(userInfo.getId());
        dataMap.setNgayTao(new Date());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiXh(Contains.CHUACAPNHAT);
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : mapDmucDvi.get(userInfo.getDvql()));
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(dataMap);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())){
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),XhQdGiaoNvXh.TABLE_NAME);
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems  =fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),dataMap.getId(),XhQdGiaoNvXh.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveDetail(dataMap, objReq);
        return created;
    }

    public void saveDetail (XhQdGiaoNvXh dataMap, XhQdGiaoNvuXuatReq objReq){
        for (XhQdGiaoNvuXuatCtReq req : objReq.getCts()){
            XhQdGiaoNvXhDtl dtl = new ModelMapper().map(req, XhQdGiaoNvXhDtl.class);
            dtl.setId(null);
            dtl.setIdQdHdr(dataMap.getId());
            dtl.setTrangThai(Contains.CHUACAPNHAT);
            xhQdGiaoNvXhDtlRepository.save(dtl);
            for (XhQdGiaoNvXhDdiemReq ddiemReq : req.getXhQdGiaoNvXhDdiemList()){
                XhQdGiaoNvXhDdiem ddiem = new ModelMapper().map(ddiemReq, XhQdGiaoNvXhDdiem.class);
                ddiem.setId(null);
                ddiem.setIdDtl(dtl.getId());
                xhQdGiaoNvXhDdiemRepository.save(ddiem);
            }
        }
    }

    @Override
    @Transactional
    public XhQdGiaoNvXh update (XhQdGiaoNvuXuatReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(objReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không cần thấy dữ liệu cần sửa");

        XhQdGiaoNvXh data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id");

        data.setNgaySua(new Date());
        data.setNguoiSuaId(userInfo.getId());
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(data);
        fileDinhKemService.delete(objReq.getId(),  Lists.newArrayList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())){
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(),XhQdGiaoNvXh.TABLE_NAME);
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),data.getId(),XhQdGiaoNvXh.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        List<XhQdGiaoNvXhDtl> listDtl  = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
        List<Long> listId = listDtl.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> listDd = xhQdGiaoNvXhDdiemRepository.findAllByIdDtlIn(listId);
        xhQdGiaoNvXhDdiemRepository.deleteAll(listDd);
        this.saveDetail(data, objReq);
        return created;
    }

    @Override
    public XhQdGiaoNvXh detail(String ids) throws Exception {
        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }

        XhQdGiaoNvXh data = optional.get();
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }

        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
        List<Long> listId = listDtl.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> listDd = xhQdGiaoNvXhDdiemRepository.findAllByIdDtlIn(listId);
        for (XhQdGiaoNvXhDtl dtl : listDtl) {
            dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            listDd.forEach(item ->{
                item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
                item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
                item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
                item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
            });
            dtl.setHhQdGiaoNvNhDdiemList(listDd);
        }
        data.setHhQdGiaoNvNhangDtlList(listDtl);

        return data;
    }

    @Override
    @Transactional()
    public void delete(IdSearchReq idSearchReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();

        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()){
            throw new Exception("Xóa thất bại, bản ghi không tồn tại");
        }

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
                && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }

        XhQdGiaoNvXh data = optional.get();

        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
        if (!CollectionUtils.isEmpty(listDtl)){
            xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
        }
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhQdGiaoNvXh.TABLE_NAME));
        xhQdGiaoNvXhRepository.delete(data);
    }

    @Override
    @Transactional()
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()){
            throw new Exception("Xóa thất bại bản ghi không tồn tại");
        }

        for (XhQdGiaoNvXh xhQdGiaoNvXh : list){
            if (!xhQdGiaoNvXh.getTrangThai().equals(Contains.DUTHAO)
            && !xhQdGiaoNvXh.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
            }
        }

        List<Long> listIdHdr = list.stream().map(XhQdGiaoNvXh::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdrIn(listIdHdr);
        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhQdGiaoNvXh.TABLE_NAME));
        xhQdGiaoNvXhRepository.deleteAll(list);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean updateStatus(StatusReq statusReq) throws Exception {
      UserInfo userInfo = SecurityContextService.getUser();
      if (StringUtils.isEmpty(statusReq.getId())){
          throw new Exception("Không tìm thấy dữ liệu");
      }

      Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(Long.valueOf(statusReq.getId()));
      if (!optional.isPresent())
          throw new Exception("Không tìm thấy dữ liệu.");

      XhQdGiaoNvXh data = optional.get();
      String status = statusReq.getTrangThai()+optional.get().getTrangThai();
      switch (status){
          case Contains.CHODUYET_LDC + Contains.DUTHAO:
          case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
              data.setNguoiGuiDuyetId(userInfo.getId());
              data.setNgayGuiDuyet(getDateTimeNow());
              break;
          case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
              data.setNguoiPduyetId(userInfo.getId());
              data.setNgayPduyet(getDateTimeNow());
              data.setLyDoTuChoi(statusReq.getLyDo());
              break;
          case Contains.BAN_HANH + Contains.CHODUYET_LDC:
              data.setNguoiPduyetId(userInfo.getId());
              data.setNgayPduyet(getDateTimeNow());
              break;
          default:
              throw new Exception("Phê duyệt không thành công");
      }
        optional.get().setTrangThai(statusReq.getTrangThai());
        xhQdGiaoNvXhRepository.save(data);
        return true;
    }

    @Override
    public void exportToExcel(XhQdGiaoNvuXuatSearchReq searchReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        searchReq.setMaDvi(userInfo.getDvql());
        Page<XhQdGiaoNvXh> page = this.searchPage(searchReq);
        List<XhQdGiaoNvXh> data = page.getContent();

        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hàng hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_xuat_hang.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i =0; i < data.size(); i ++) {
            XhQdGiaoNvXh qdXh = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qdXh.getNam();
            objs[2] = qdXh.getSoQd();
            objs[3] = qdXh.getNgayTao();
            objs[4] = qdXh.getSoHd();
            objs[5] = qdXh.getTenLoaiVthh();
            objs[6] = qdXh.getTenCloaiVthh();
            objs[7] = qdXh.getTgianGnhan();
            objs[8] = qdXh.getTrichYeu();
            objs[9] = qdXh.getBbTinhKho();
            objs[10] = qdXh.getBbHaoDoi();
            objs[11] = qdXh.getTenTrangThai();
            objs[12] = qdXh.getTenTrangThaiXh();
            dataList.add(objs);

        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }


//    private final XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
//    private final XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
//    private final XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
//    private final XhHopDongHdrRepository hopDongRepository;
//    private final FileDinhKemService fileDinhKemService;
//    private final KtNganLoRepository ktNganLoRepository;
//
//    private static final String SHEET_QUYET_DINH_GIAO_NHIEM_VU_XUAT_HANG = "Quyết định giao nhiệm vụ xuất hàng";
//    private static final String STT = "STT";
//    private static final String SO_QUYET_DINH = "Số Quyết Định";
//    private static final String TRICH_YEU = "Trích Yếu";
//    private static final String NGAY_QUYET_DINH = "Ngày Quyết Định";
//    private static final String NAM_XUAT = "Năm Xuất";
//    private static final String TRANG_THAI = "Trạng Thái";
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhQdGiaoNvuXuatRes create(XhQdGiaoNvuXuatReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoQuyetDinh(null, req);
//
//        XhQdGiaoNvuXuat item = new XhQdGiaoNvuXuat();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        xhQdGiaoNvXhRepository.save(item);
//
//        List<XhQdGiaoNvuXuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), new HashMap<>());
//        item.setCts(chiTiets);
//        item.setCt1s(this.saveListChiTiet1(item.getId(), req.getHopDongIds()));
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhQdGiaoNvuXuat.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
//    }
//
//    private List<XhQdGiaoNvuXuatCt1> saveListChiTiet1(Long parentId, List<Long> hopDongIds) {
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(Collections.singleton(parentId));
//        List<XhQdGiaoNvuXuatCt1> ct1s = new ArrayList<>();
//        hopDongIds.forEach(id -> {
//            XhQdGiaoNvuXuatCt1 ct1 = new XhQdGiaoNvuXuatCt1();
//            ct1.setHopDongId(id);
//            ct1.setQdgnvxId(parentId);
//            ct1s.add(ct1);
//
//        });
//
//        if (!CollectionUtils.isEmpty(ct1s))
//            xhQdGiaoNvXhDdiemRepository.saveAll(ct1s);
//        return ct1s;
//    }
//
//    private List<XhQdGiaoNvuXuatCt> saveListChiTiet(Long parentId,
//                                               List<XhQdGiaoNvuXuatCtReq> chiTietReqs,
//                                               Map<Long, XhQdGiaoNvuXuatCt> mapChiTiet) throws Exception {
//        List<XhQdGiaoNvuXuatCt> chiTiets = new ArrayList<>();
//        for (XhQdGiaoNvuXuatCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            XhQdGiaoNvuXuatCt chiTiet = new XhQdGiaoNvuXuatCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Quyết định giao nhiệm vụ xuất chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setQdgnvxId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            xhQdGiaoNvXhDtlRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//
//    private XhQdGiaoNvuXuatRes buildResponse(XhQdGiaoNvuXuat item) throws Exception {
//        XhQdGiaoNvuXuatRes res = new XhQdGiaoNvuXuatRes();
//        List<XhQdGiaoNvuXuatCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//
//        Set<String> maNganLos = item.getCts().stream().map(XhQdGiaoNvuXuatCt::getMaNganLo).collect(Collectors.toSet());
//        Map<String, KtNganLo> mapNganLo = new HashMap<>();
//        if (!CollectionUtils.isEmpty(maNganLos)) {
//            mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
//                    .stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
//        }
//
//        Map<String,String> mapVthh = getListDanhMucHangHoa();
//        for (XhQdGiaoNvuXuatCt ct : item.getCts()) {
//            XhQdGiaoNvuXuatCtRes xhQdGiaoNvuXuatCtRes = new XhQdGiaoNvuXuatCtRes(ct);
//            xhQdGiaoNvuXuatCtRes.setTenVatTuCha(mapVthh.get(ct.getMaVatTuCha()));
//            xhQdGiaoNvuXuatCtRes.setTenVatTu(mapVthh.get(ct.getMaVatTu()));
//            xhQdGiaoNvuXuatCtRes.setMaVatTuCha(ct.getMaVatTuCha());
//            xhQdGiaoNvuXuatCtRes.setMaVatTu(ct.getMaVatTu());
//            this.thongTinNganLo(xhQdGiaoNvuXuatCtRes, mapNganLo.get(ct.getMaNganLo()));
//            chiTiets.add(xhQdGiaoNvuXuatCtRes);
//        }
//        res.setCts(chiTiets);
//        this.setThongTinDonVi(res, item.getMaDvi());
//
//        res.setHopDongIds(item.getCt1s().stream().map(XhQdGiaoNvuXuatCt1::getHopDongId).collect(Collectors.toList()));
//        List<XhHopDongHdr> hopDongs = hopDongRepository.findAllById(res.getHopDongIds());
//        res.setHopDongs(hopDongs.stream().map(h -> new IdAndNameDto(h.getId(), h.getSoHd())).collect(Collectors.toList()));
//        return res;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhQdGiaoNvuXuatRes update(XhQdGiaoNvuXuatReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        this.validateSoQuyetDinh(optional.get(), req);
//
//        XhQdGiaoNvuXuat item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        xhQdGiaoNvXhRepository.save(item);
//
//        Map<Long, XhQdGiaoNvuXuatCt> mapChiTiet = xhQdGiaoNvXhDtlRepository.findByQdgnvxIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(XhQdGiaoNvuXuatCt::getId, Function.identity()));
//
//        List<XhQdGiaoNvuXuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), mapChiTiet);
//        item.setCts(chiTiets);
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            xhQdGiaoNvXhDtlRepository.deleteAll(mapChiTiet.values());
//
//        item.setCt1s(this.saveListChiTiet1(item.getId(), req.getHopDongIds()));
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhQdGiaoNvuXuat.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public XhQdGiaoNvuXuatRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        item.setCts(xhQdGiaoNvXhDtlRepository.findByQdgnvxIdIn(Collections.singleton(item.getId())));
//        item.setCt1s(xhQdGiaoNvXhDdiemRepository.findByQdgnvxIdIn(Collections.singleton(item.getId())));
//        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(XhQdGiaoNvuXuat.TABLE_NAME)));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa Quyết định giao nhiệm vụ xuất đã đã duyệt");
//        }
//        xhQdGiaoNvXhDtlRepository.deleteByQdgnvxIdIn(Collections.singleton(item.getId()));
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(Collections.singleton(item.getId()));
//        xhQdGiaoNvXhRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        String trangThai = item.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId());
//            item.setNguoiGuiDuyetId(userInfo.getId());
//            item.setNgayGuiDuyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        }else if (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<XhQdGiaoNvuXuatRes> search(XhQdGiaoNvuXuatSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<XhQdGiaoNvuXuatRes> responses = new ArrayList<>();
//        xhQdGiaoNvXhRepository.search(req, userInfo.getCapDvi()).forEach(item -> {
//            XhQdGiaoNvuXuatRes response = new XhQdGiaoNvuXuatRes();
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            responses.add(response);
//        });
//
//        return new PageImpl<>(responses, pageable, xhQdGiaoNvXhRepository.count(req, userInfo.getCapDvi()));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        xhQdGiaoNvXhDtlRepository.deleteByQdgnvxIdIn(req.getIds());
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(XhQdGiaoNvuXuat.TABLE_NAME));
//        xhQdGiaoNvXhRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(XhQdGiaoNvuXuatSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<XhQdGiaoNvuXuatRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_QUYET_DINH, TRICH_YEU, NGAY_QUYET_DINH,
//                NAM_XUAT, TRANG_THAI};
//        String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_xuat_hang.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                XhQdGiaoNvuXuatRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoQuyetDinh();
//                objs[2] = item.getTrichYeu();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayQuyetDinh());
//                objs[4] = item.getNamXuat();
//                objs[5] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_QUYET_DINH_GIAO_NHIEM_VU_XUAT_HANG, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = xhQdGiaoNvXhRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoQuyetDinh(XhQdGiaoNvuXuat update, XhQdGiaoNvuXuatReq req) throws Exception {
//        String so = req.getSoQuyetDinh();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoQuyetDinh()) && !update.getSoQuyetDinh().equalsIgnoreCase(so))) {
//            Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findFirstBySoQuyetDinh(so);
//            Long updateId = Optional.ofNullable(update).map(XhQdGiaoNvuXuat::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số Quyết định giao nhiệm vụ xuất " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(XhQdGiaoNvuXuatCtRes item, KtNganLo nganLo) {
//        if (nganLo != null) {
//            item.setTenNganLo(nganLo.getTenNganlo());
//            KtNganKho nganKho = nganLo.getParent();
//            if (nganKho == null)
//                return;
//
//            item.setTenNganKho(nganKho.getTenNgankho());
//            item.setMaNganKho(nganKho.getMaNgankho());
//            KtNhaKho nhaKho = nganKho.getParent();
//            if (nhaKho == null)
//                return;
//
//            item.setTenNhaKho(nhaKho.getTenNhakho());
//            item.setMaNhaKho(nhaKho.getMaNhakho());
//            KtDiemKho diemKho = nhaKho.getParent();
//            if (diemKho == null)
//                return;
//
//            item.setTenDiemKho(diemKho.getTenDiemkho());
//            item.setMaDiemKho(diemKho.getMaDiemkho());
//        }
//    }
}
