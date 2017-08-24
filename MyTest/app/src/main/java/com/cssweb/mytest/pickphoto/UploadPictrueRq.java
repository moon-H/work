//package com.cssweb.mytest.pickphoto;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import com.skcc.corpay.integration.css.vo.Request;
//
//public class UploadPictrueRq extends Request {
//    private String name;
//    private MultipartFile photo;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public MultipartFile getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(MultipartFile photo) {
//        this.photo = photo;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("UploadPictrueRq [name=");
//        builder.append(name);
//        builder.append(", photo=");
//        builder.append(photo);
//        builder.append("]");
//        return builder.toString();
//    }
//}
