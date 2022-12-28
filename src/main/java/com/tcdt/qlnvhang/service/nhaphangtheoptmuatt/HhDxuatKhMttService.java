package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttCcxdgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhDxuatKhMttSlddRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttCcxdg;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSldd;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HhDxuatKhMttService extends BaseServiceImpl {
    @Autowired
    private HhDxuatKhMttRepository hhDxuatKhMttRepository;

    @Autowired
    private HhDxuatKhMttSlddRepository hhDxuatKhMttSlddRepository;

    @Autowired
    private HhDxuatKhMttCcxdgRepository hhDxuatKhMttCcxdgRepository;
    @Autowired
    HhDxuatKhMttSlddDtlRepository hhDxuatKhMttSlddDtlRepository;

    Long shgtNext = new Long(0);

    public Page<HhDxuatKhMttHdr> searchPage(SearchHhDxKhMttHdrReq objReq)throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhDxuatKhMttHdr> data = hhDxuatKhMttRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoDxuat(),
                Contains.convertDateToString(objReq.getNgayTaoTu()),
                Contains.convertDateToString(objReq.getNgayTaoDen()),
                Contains.convertDateToString(objReq.getNgayDuyetTu()),
                Contains.convertDateToString(objReq.getNgayDuyetDen()),
                objReq.getTrichYeu(),
                objReq.getNoiDungTh(),
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                objReq.getTrangThaiTh(),
                objReq.getMaDvi(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();

        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh())?null:hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh())?null:hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Transactional
    public HhDxuatKhMttHdr create(HhDxuatKhMttHdrReq objReq) throws Exception {
        if(!StringUtils.isEmpty(objReq.getSoDxuat())){
            Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findBySoDxuat(objReq.getSoDxuat());
            if (qOptional.isPresent()){
                throw new Exception("Số đề xuất " + objReq.getSoDxuat() + " đã tồn tại");
            }
        }
        // Add danh sach file dinh kem o Master
        List<FileDKemJoinDxKhMttHdr> fileDinhKemList = new ArrayList<FileDKemJoinDxKhMttHdr>();
        if (objReq.getFileDinhKemReq() != null) {
            fileDinhKemList = ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhMttHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhMttHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }
        HhDxuatKhMttHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhMttHdr.class);
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiTh(Contains.CHUATONGHOP);
        dataMap.setNguoiTao(getUser().getUsername());
        dataMap.setFileDinhKems(fileDinhKemList);
        this.validateData(dataMap,dataMap.getTrangThai());
        hhDxuatKhMttRepository.save(dataMap);


        this.saveDetail(objReq,dataMap.getId());

        // Lưu quyết định căn cứ
        for (HhDxuatKhMttCcxdgReq cc : objReq.getCcXdgReq()){
            HhDxuatKhMttCcxdg data = ObjectMapperUtils.map(cc, HhDxuatKhMttCcxdg.class);
            List<FileDKemJoinDxKhMttCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhMttCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhMttCcxdg.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdDxKhmtt(dataMap.getId());

            hhDxuatKhMttCcxdgRepository.save(data);
        }
        return dataMap;
    }

  @Transactional
  void saveDetail(HhDxuatKhMttHdrReq objReq, Long idHdr){
      HhDxuatKhMttHdr dataMap = new ModelMapper().map(objReq, HhDxuatKhMttHdr.class);
        hhDxuatKhMttSlddRepository.deleteAllByIdDxKhmtt(idHdr);
        for ( HhDxuatKhMttSlddReq sldd: objReq.getDsSlddReq()){
            HhDxuatKhMttSldd data = new ModelMapper().map(sldd, HhDxuatKhMttSldd.class);
            data.setId(null);
            data.setIdDxKhmtt(idHdr);
            BigDecimal thanhTien = data.getDonGia().multiply(data.getSoLuong());
            data.setThanhTien(thanhTien);
            hhDxuatKhMttSlddRepository.save(data);
            hhDxuatKhMttSlddDtlRepository.deleteAllByIdDiaDiem(sldd.getId());
            for (HhDxuatKhMttSlddDtlReq slddDtlReq : sldd.getChildren()){
                HhDxuatKhMttSlddDtl slddDtl = new ModelMapper().map(slddDtlReq, HhDxuatKhMttSlddDtl.class);
                slddDtl.setIdDiaDiem(data.getId());
                hhDxuatKhMttSlddDtlRepository.save(slddDtl);
                dataMap.setDonGiaVat(slddDtl.getDonGiaVat());
            }
        }
  }

    public void validateData(HhDxuatKhMttHdr objHdr,String trangThai) throws Exception {
            if(trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())){
                HhDxuatKhMttHdr dXuat = hhDxuatKhMttRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(),NhapXuatHangTrangThaiEnum.DUTHAO.getId());
                if(!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())){
                    throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
                }
            }
            if(trangThai.equals(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId())) {
                for(HhDxuatKhMttSldd chiCuc : objHdr.getDsSlddDtlList()){
                    BigDecimal aLong = hhDxuatKhMttRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), chiCuc.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
                    BigDecimal soLuongTotal = aLong.add(chiCuc.getSoLuong());
                    if (chiCuc.getSoLuongChiTieu() == null){
                        throw new Exception("Hiện chưa có số lượng chỉ tiêu kế hoạch năm, vui lòng nhập lại");
                    }
                    if(soLuongTotal.compareTo(chiCuc.getSoLuongChiTieu()) > 0){
                        throw new Exception(chiCuc.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
                    }
                }
            }

    }

    @Transactional
    public HhDxuatKhMttHdr update(HhDxuatKhMttHdrReq objReq) throws  Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("không tìm thấy dữ liệu cần sửa");

        if (!StringUtils.isEmpty(objReq.getSoDxuat())){
            Optional<HhDxuatKhMttHdr> deXuat = hhDxuatKhMttRepository.findBySoDxuat(objReq.getSoDxuat());
            if (deXuat.isPresent()){
                if (!deXuat.get().getId().equals(objReq.getId())){
                    throw new Exception("Số đề xuất" + objReq.getSoDxuat()+ "đã tồn tại");
                }
            }
        }

        List<FileDKemJoinDxKhMttHdr> fileDinhKemList = new ArrayList<>();
        if (objReq.getFileDinhKemReq() != null){
            fileDinhKemList= ObjectMapperUtils.mapAll(objReq.getFileDinhKemReq(), FileDKemJoinDxKhMttHdr.class);
            fileDinhKemList.forEach(f -> {
                f.setDataType(HhDxuatKhMttHdr.TABLE_NAME);
                f.setCreateDate(new Date());
            });
        }

        HhDxuatKhMttHdr dataDTB = qOptional.get();
        HhDxuatKhMttHdr dataMap = ObjectMapperUtils.map(objReq, HhDxuatKhMttHdr.class);

        updateObjectToObject(dataDTB, dataMap);

        dataDTB.setNgaySua(getDateTimeNow());
        dataDTB.setNguoiSua(getUser().getUsername());
        dataDTB.setFileDinhKems(fileDinhKemList);

        hhDxuatKhMttRepository.save(dataDTB);

        this.saveDetail(objReq,dataDTB.getId());

        hhDxuatKhMttCcxdgRepository.deleteAllByIdDxKhmtt(dataDTB.getId());
        for (HhDxuatKhMttCcxdgReq cc : objReq.getCcXdgReq()){
            HhDxuatKhMttCcxdg data = ObjectMapperUtils.map(cc, HhDxuatKhMttCcxdg.class);
            List<FileDKemJoinDxKhMttCcxdg> detailChild = new ArrayList<>();
            if (data.getChildren() != null) {
                detailChild = ObjectMapperUtils.mapAll(data.getChildren(), FileDKemJoinDxKhMttCcxdg.class);
                detailChild.forEach(f -> {
                    f.setDataType(HhDxuatKhMttCcxdg.TABLE_NAME);
                    f.setCreateDate(new Date());
                });
            }
            data.setChildren(detailChild);
            data.setIdDxKhmtt(dataDTB.getId());

            hhDxuatKhMttCcxdgRepository.save(data);
        }

        return dataDTB;
    }

    public HhDxuatKhMttHdr detail(Long ids) throws Exception{
        Optional<HhDxuatKhMttHdr> qOptional = hhDxuatKhMttRepository.findById(ids);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String,String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        qOptional.get().setTenLoaiVthh( StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh( StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null :mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        qOptional.get().setCcXdgDtlList(hhDxuatKhMttCcxdgRepository.findByIdDxKhmtt(qOptional.get().getId()));
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));

        List<HhDxuatKhMttSldd> dsSlDdList = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(qOptional.get().getId());
        for(HhDxuatKhMttSldd dsG : dsSlDdList){
            dsG.setTenDvi(mapDmucDvi.get(dsG.getMaDvi()));
            dsG.setTenCloaiVthh(mapVthh.get(dsG.getCloaiVthh()));

            List<HhDxuatKhMttSlddDtl> listDdNhap = hhDxuatKhMttSlddDtlRepository.findByIdDiaDiem(dsG.getId());
            listDdNhap.forEach( f -> {
                f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
                f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : mapDmucDvi.get(f.getMaDiemKho()));
            });
            dsG.setChildren(listDdNhap);
        }
        qOptional.get().setDsSlddDtlList (dsSlDdList);
        return qOptional.get();
    }

    @Transactional
    public HhDxuatKhMttHdr approve(StatusReq stReq) throws Exception {
        if (StringUtils.isEmpty(stReq.getId()))
            throw new Exception("Không tìm thấy dữ liệu");

        HhDxuatKhMttHdr optional = this.detail(stReq.getId());
        String status = stReq.getTrangThai() + optional.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                this.validateData(optional,Contains.CHODUYET_TP);
                optional.setNguoiGuiDuyet(getUser().getUsername());
                optional.setNgayGuiDuyet(getDateTimeNow());
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.setNguoiPduyet(getUser().getUsername());
                optional.setNgayPduyet(getDateTimeNow());
                optional.setLdoTuchoi(stReq.getLyDo());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                this.validateData(optional,stReq.getTrangThai());
                optional.setNguoiPduyet(getUser().getUsername());
                optional.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }

        optional.setTrangThai(stReq.getTrangThai());
        return hhDxuatKhMttRepository.save(optional);
    }

    public  void export(SearchHhDxKhMttHdrReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhDxuatKhMttHdr> page=this.searchPage(objReq);
        List<HhDxuatKhMttHdr> data=page.getContent();

        String title="Danh sách đề xuất kế hoạch mua trực tiếp";
        String[] rowsName=new String[]{"STT","Số kế hoạch/đề xuất","Năm kế hoạch","Ngày tạo","Ngày duyệt","Trích yếu","Số QĐ giao chỉ tiêu","Loại hàng hóa","Chủng loại hàng hóa","Số lượng(tấn)","Trạng thái đề xuất","Mã tổng hợp"};
        String fileName="danh-sach-dx-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhDxuatKhMttHdr dx=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=dx.getSoDxuat();
            objs[2]=dx.getNamKh();
            objs[3]=dx.getNgayTao();
            objs[4]=dx.getNgayPduyet();
            objs[5]=dx.getTrichYeu();
            objs[6]=dx.getSoQd();
            objs[7]=dx.getTenLoaiVthh();
            objs[8]=dx.getTenCloaiVthh();
            objs[9]=dx.getTongSoLuong();
            objs[10]=dx.getTenTrangThai();
            objs[11]=dx.getTenTrangThaiTh();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }



    public void delete(IdSearchReq idSearchReq) throws Exception{
      if (StringUtils.isEmpty(idSearchReq.getId()))
          throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
      Optional<HhDxuatKhMttHdr> optional = hhDxuatKhMttRepository.findById(idSearchReq.getId());

      if (!optional.isPresent()){
          throw new Exception("Không tìm thấy dữ liệu cần xoá");
      }
      if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_TP)
              && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)){
          throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
      }
      List<HhDxuatKhMttSldd> goiThau = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(idSearchReq.getId());
      if(goiThau != null && goiThau.size() > 0){
          for (HhDxuatKhMttSldd ct : goiThau) {
              List<HhDxuatKhMttSlddDtl> dsCtiet = hhDxuatKhMttSlddDtlRepository.findByIdDiaDiem(ct.getId());
              hhDxuatKhMttSlddDtlRepository.deleteAll(dsCtiet);

          }
          hhDxuatKhMttSlddRepository.deleteAll(goiThau);
      }
      hhDxuatKhMttSlddRepository.deleteAllByIdDxKhmtt(idSearchReq.getId());
      List<HhDxuatKhMttCcxdg> cc=hhDxuatKhMttCcxdgRepository.findByIdDxKhmtt(idSearchReq.getId());
      if (cc != null && cc.size() > 0){
          hhDxuatKhMttCcxdgRepository.deleteAll(cc);
      }
      hhDxuatKhMttRepository.delete(optional.get());
  }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<HhDxuatKhMttHdr> listDx = hhDxuatKhMttRepository.findByIdIn(idSearchReq.getIdList());
        if (listDx.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        for (HhDxuatKhMttHdr dx:listDx) {
            if (!dx.getTrangThai().equals(Contains.DUTHAO)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDV)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_TP)
                    && !dx.getTrangThai().equals(Contains.TUCHOI_LDC)){
                throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
            }
            List<HhDxuatKhMttSldd> goiThau = hhDxuatKhMttSlddRepository.findAllByIdDxKhmtt(dx.getId());
            List<Long> listGoiThau = goiThau.stream().map(HhDxuatKhMttSldd::getId).collect(Collectors.toList());
            hhDxuatKhMttSlddDtlRepository.deleteAllByIdDiaDiemIn(listGoiThau);
        }
        hhDxuatKhMttSlddRepository.deleteAllByIdDxKhmttIn(idSearchReq.getIdList());
        hhDxuatKhMttCcxdgRepository.deleteAllByIdDxKhmttIn(idSearchReq.getIdList());
        hhDxuatKhMttRepository.deleteAllByIdIn(idSearchReq.getIdList());

    }





    public BigDecimal countSoLuongKeHoachNam(CountKhMttSlReq req) throws Exception {
        return hhDxuatKhMttRepository.countSLDalenKh(req.getYear(),req.getLoaiVthh(), req.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
    }
}
