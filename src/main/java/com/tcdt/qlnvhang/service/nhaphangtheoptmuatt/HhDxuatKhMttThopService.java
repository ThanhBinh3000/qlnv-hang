package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;


import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttTChiThopReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.HhDxKhMttThopHdrReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttThopReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
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
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhDxuatKhMttThopService extends BaseServiceImpl {

    @Autowired
    private HhDxuatKhMttThopRepository hhDxuatKhMttThopRepository;

    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhDxuatKhMttSlddRepository hhDxuatKhMttSlddRepository;

    @Autowired
    private HhDxuatKhMttThopDtlRepository hhDxuatKhMttThopDtlRepository;

    @Autowired
    private HhDxuatKhMttCcxdgRepository hhDxuatKhMttCcxdgRepository;


    public Page<HhDxKhMttThopHdr> searchPage(SearchHhDxKhMttThopReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxKhMttThopHdr> data = hhDxuatKhMttThopRepository.searchPage(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh(),
                objReq.getNoiDung(),
                Contains.convertDateToString(objReq.getNgayThopTu()),
                Contains.convertDateToString(objReq.getNgayThopDen()),
//                Contains.convertDateToString(objReq.getNgayKyQdTu()),
//                Contains.convertDateToString(objReq.getNgayKyQdDen()),
                objReq.getTrangThai(),
                pageable);
        Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public HhDxKhMttThopHdr sumarryData(HhDxKhMttTChiThopReq objReq, HttpServletRequest req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        List<HhDxuatKhMttHdr> dxuatList = hhDxuatKhMttRepository.listTongHop(objReq.getNamKh(), objReq.getLoaiVthh(), objReq.getCloaiVthh(), userInfo.getDvql());

        if (dxuatList.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        HhDxKhMttThopHdr thopHdr = new HhDxKhMttThopHdr();

        Map<String, String> mapDmuc = getMapCategory();

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();

        thopHdr.setNamKh(objReq.getNamKh());
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setTenLoaiVthh(listDanhMucHangHoa.get(objReq.getLoaiVthh()));
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());
        thopHdr.setTenCloaiVthh(listDanhMucHangHoa.get(objReq.getCloaiVthh()));

        List<HhDxKhMttThopDtl> thopDtls = new ArrayList<HhDxKhMttThopDtl>();
        String tChuanCluong = "";
        for (HhDxuatKhMttHdr dXuat : dxuatList) {
            HhDxKhMttThopDtl thopDtl = new HhDxKhMttThopDtl();
            thopDtl.setIdDxHdr(dXuat.getId());
            thopDtl.setMaDvi(dXuat.getMaDvi());
            thopDtl.setTenDvi(getDviByMa(dXuat.getMaDvi(), req).getTenDvi());
            thopDtl.setSoDxuat(dXuat.getSoDxuat());
            thopDtl.setNgayPduyet(dXuat.getNgayPduyet());
            thopDtl.setDiaChiDvi(dXuat.getDiaChiDvi());
            thopDtl.setTenDuAn(dXuat.getTenDuAn());
            thopDtl.setTrichYeu(dXuat.getTrichYeu());
            thopDtl.setTongSoLuong(dXuat.getTongSoLuong());
            thopDtl.setTongTienVat(dXuat.getTongTienVat());

//            List<HhDxuatKhMttSldd> dtlslDd = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(dXuat.getId());
//            BigDecimal soLuong = BigDecimal.ZERO;
//            BigDecimal tongTien = BigDecimal.ZERO;
//            for (HhDxuatKhMttSldd gthauDtl : dtlslDd) {
//                soLuong = soLuong.add(gthauDtl.getSoLuong());
//                tongTien = tongTien.add(gthauDtl.getThanhTien() == null ? BigDecimal.ZERO : gthauDtl.getThanhTien());
//            }
//            thopDtl.setTongSoLuong(soLuong);
            tChuanCluong = tChuanCluong.concat(dXuat.getTchuanCluong() + "");
            thopDtls.add(thopDtl);
        }
        thopHdr.setTchuanCluong(tChuanCluong);
        thopHdr.setHhDxKhMttThopDtls(thopDtls);
        return thopHdr;
    }


    public HhDxKhMttThopHdr create(HhDxKhMttThopHdrReq objReq, HttpServletRequest req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();

        HhDxKhMttThopHdr thopHdr = sumarryData(objReq, req);
        thopHdr.setNgayTao(getDateTimeNow());
        thopHdr.setNguoiTao(getUser().getUsername());
        thopHdr.setNoiDung(objReq.getNoiDung());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNgayThop(new Date());
        thopHdr.setId(objReq.getId());
        thopHdr.setSoQdCc(objReq.getSoQdCc());
        thopHdr.setMaDvi(userInfo.getDvql());
        hhDxuatKhMttThopRepository.save(thopHdr);
        for (HhDxKhMttThopDtl dtl : thopHdr.getHhDxKhMttThopDtls()) {
            dtl.setIdThopHdr(thopHdr.getId());
            hhDxuatKhMttThopDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getHhDxKhMttThopDtls().size() > 0) {
            List<String> soDxuatList = thopHdr.getHhDxKhMttThopDtls().stream().map(HhDxKhMttThopDtl::getSoDxuat)
                    .collect(Collectors.toList());
            List<HhDxuatKhMttHdr> list = hhDxuatKhMttRepository.findBySoDxuatIn(soDxuatList);
            for (HhDxuatKhMttHdr bdg : list){
                bdg.setMaThop(String.valueOf(thopHdr.getId()));
                hhDxuatKhMttRepository.save(bdg);
            }
            hhDxuatKhMttRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP);
        }
return thopHdr;
    }

    @Transactional()
    public HhDxKhMttThopHdr update(HhDxKhMttThopHdrReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        HhDxKhMttThopHdr dataDTB = qOptional.get();
        HhDxKhMttThopHdr dataMap = ObjectMapperUtils.map(objReq, HhDxKhMttThopHdr.class);

        updateObjectToObject(dataDTB, dataMap);
        hhDxuatKhMttThopRepository.save(dataDTB);
        return dataDTB;

    }

    public HhDxKhMttThopHdr detail (String ids) throws Exception{
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<HhDxKhMttThopHdr> qOptional = hhDxuatKhMttThopRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        HhDxKhMttThopHdr hdrThop = qOptional.get();

        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

        hdrThop.setTenLoaiVthh(hashMapDmHh.get(hdrThop.getLoaiVthh()));
        hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));

        List<HhDxKhMttThopDtl> listTh = hhDxuatKhMttThopDtlRepository.findByIdThopHdr(hdrThop.getId());
        Map<String, String> mapDmucDvi = getMapTenDvi();
        listTh.forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
        });
        hdrThop.setHhDxKhMttThopDtls(listTh);

        return hdrThop;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<HhDxKhMttThopHdr> optional = hhDxuatKhMttThopRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        List<HhDxKhMttThopDtl> listDls= hhDxuatKhMttThopDtlRepository.findByIdThopHdr(optional.get().getId());
        if(!CollectionUtils.isEmpty(listDls)){
            List<Long> idDxList = listDls.stream().map(HhDxKhMttThopDtl::getIdDxHdr).collect(Collectors.toList());
            List<HhDxuatKhMttHdr> listDxHdr = hhDxuatKhMttRepository.findByIdIn(idDxList);
            if(!CollectionUtils.isEmpty(listDxHdr)){
                listDxHdr.stream().map(item ->{
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    return item;
                }).collect(Collectors.toList());
            }
            hhDxuatKhMttRepository.saveAll(listDxHdr);
        }
        hhDxuatKhMttThopDtlRepository.deleteAll(listDls);
        hhDxuatKhMttThopRepository.delete(optional.get());
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhDxKhMttThopHdr> listThop= hhDxuatKhMttThopRepository.findAllByIdIn(idSearchReq.getIdList());
        for (HhDxKhMttThopHdr thopHdr: listThop){
            List<HhDxKhMttThopDtl> listDls= hhDxuatKhMttThopDtlRepository.findByIdThopHdr(thopHdr.getId());
            if(!CollectionUtils.isEmpty(listDls)){
                List<Long> idDxList = listDls.stream().map(HhDxKhMttThopDtl::getIdDxHdr).collect(Collectors.toList());
                List<HhDxuatKhMttHdr> listDxHdr = hhDxuatKhMttRepository.findByIdIn(idDxList);
                if(!CollectionUtils.isEmpty(listDxHdr)){
                    listDxHdr.stream().map(item ->{
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        return item;
                    }).collect(Collectors.toList());
                }
                hhDxuatKhMttRepository.saveAll(listDxHdr);
            }
            hhDxuatKhMttThopDtlRepository.deleteAll(listDls);
        }
        hhDxuatKhMttThopRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchHhDxKhMttThopReq searchReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<HhDxKhMttThopHdr> page = this.searchPage(searchReq);
        List<HhDxKhMttThopHdr> data = page.getContent();

        String title = "Danh sách tổng hợp kế hoạch mua trực tiếp";
        String[] rowsName = new String[] { "STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch","Chủng loại hàng hóa", "Trạng thái", "Số QĐ phê duyêt mua trực tiếp"};
        String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhDxKhMttThopHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getId();
            objs[2] = dx.getNgayThop();
            objs[3] = dx.getNoiDung();
            objs[4] = dx.getNamKh();
            objs[5] = dx.getTenCloaiVthh();
            objs[6] = dx.getTenTrangThai();
            objs[7] = dx.getSoQdPduyet();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }



}
