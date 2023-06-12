package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class DcnbKeHoachNhapXuatService extends BaseServiceImpl {

    @Autowired
    private DcnbKeHoachNhapXuatRepository hdrRepository;

    @Autowired
    private DcnbBienBanLayMauHdrRepository dcnbBienBanLayMauHdrRepository;

    @Autowired
    private DcnbPhieuKnChatLuongHdrRepository dcnbPhieuKnChatLuongHdrRepository;

    @Autowired
    private DcnbBangKeCanHangHdrRepository dcnbBangKeCanHangHdrRepository;

    @Autowired
    private DcnbBienBanTinhKhoHdrRepository dcnbBienBanTinhKhoHdrRepository;

    @Autowired
    private DcnbBBNTBQHdrRepository dcnbBBNTBQHdrRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Transactional
    public DcnbDataLink saveOrUpdate(DcnbDataLink objReq) throws Exception {
//        if(Objects.isNull(objReq.getIdKhDcDtl())){
//            throw new Exception("IdKhDcDtl is null");
//        }else if(Objects.isNull(objReq.getIdHdr())) {
//            throw new Exception("IdHdr is null");
//        } else if (DataUtils.isNullOrEmpty(objReq.getTableName())){
//            throw new Exception("Table name is null");
//        }
//        if(checkHasKeHoachNx(objReq.getIdKhDcDtl(),objReq.getTableName())){
//            throw new Exception("ID KH DTL : "+objReq.getIdKhDcDtl()+", Table Name :"+ objReq.getTableName()+" already exist");
//        }
//        Optional<DcnbKeHoachDcDtl> byId = dcnbKeHoachDcDtlRepository.findById(objReq.getIdKhDcDtl());
//        if(!byId.isPresent()){
//            throw new Exception("Kế hoạch DTL " + objReq.getIdKhDcDtl() + " is null");
//        }
//        DcnbKeHoachDcDtl dtl = byId.get();
        DcnbDataLink save = hdrRepository.save(objReq);
        return save;
    }

    public DcnbDataLink detail(Long id) throws Exception {
        Optional<DcnbDataLink> byId = hdrRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }else{
            throw new Exception("Data is null");
        }
    }

    public DcnbDataLink detailKhDtl(Long idKhDtl) throws Exception {
        DcnbDataLink keHoachNhapXuat = new DcnbDataLink();
//        List<DcnbKeHoachDcDtlTT> idKhDcDtlList = hdrRepository.findByIdKhDcDtl(idKhDtl);
//        if(idKhDcDtlList != null && !idKhDcDtlList.isEmpty()){
//            keHoachNhapXuat.setIdKhDcDtl(idKhDtl);
//            // Loop để set cái dữ liệu HDR vào 1 Kế hoạch nhập xuất theo Table Name
//            idKhDcDtlList.forEach( khDtl -> {
//                //Exam
//                switch (khDtl.getTableName()){
//                    // Biên bản lấy mẫu
//                    case DcnbBienBanLayMauHdr.TABLE_NAME:
//                        Optional<DcnbBienBanLayMauHdr> dcnbBienBanLayMauHdr = dcnbBienBanLayMauHdrRepository.findById(khDtl.getIdHdr());
//                        dcnbBienBanLayMauHdr.ifPresent(khDtl::setDcnbBienBanLayMauHdr);
//                        break;
//                    // Biên bản .....
//                    case DcnbBBNTBQHdr.TABLE_NAME:
//                        Optional<DcnbBBNTBQHdr> dcnbBBNTBQHdr = dcnbBBNTBQHdrRepository.findById(khDtl.getIdHdr());
//                        dcnbBBNTBQHdr.ifPresent(khDtl::setDcnbBBNTBQHdr);
//                        break;
//                    case DcnbPhieuKnChatLuongHdr.TABLE_NAME:
//                        Optional<DcnbPhieuKnChatLuongHdr> dcnbPhieuKnChatLuongHdr = dcnbPhieuKnChatLuongHdrRepository.findById(khDtl.getIdHdr());
//                        dcnbPhieuKnChatLuongHdr.ifPresent(khDtl::setDcnbPhieuKnChatLuongHdr);
//                        break;
//                    case DcnbBangKeCanHangHdr.TABLE_NAME:
//                        Optional<DcnbBangKeCanHangHdr> dcnbBangKeCanHangHdr = dcnbBangKeCanHangHdrRepository.findById(khDtl.getIdHdr());
//                        dcnbBangKeCanHangHdr.ifPresent(khDtl::setDcnbBangKeCanHangHdr);
//                        break;
//                    case DcnbBienBanTinhKhoHdr.TABLE_NAME:
//                        Optional<DcnbBienBanTinhKhoHdr> dcnbBienBanTinhKhoHdr = dcnbBienBanTinhKhoHdrRepository.findById(khDtl.getIdHdr());
//                        dcnbBienBanTinhKhoHdr.ifPresent(khDtl::setDcnbBienBanTinhKhoHdr);
//                        break;
//                    default:
//                        break;
//                }
//            });
//        }
        return keHoachNhapXuat;
    }

    public boolean checkHasKeHoachNx(Long idKhDtl,String tableName){
//        Optional<DcnbKeHoachDcDtlTT> byIdKhDcDtlAndTableName = hdrRepository.findByIdKhDcDtlAndTableName(idKhDtl, tableName);
//        if(byIdKhDcDtlAndTableName.isPresent()){
//            return true;
//        }
        return false;
    }

}
