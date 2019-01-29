package com.zzhb.zzoa.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.config.Props;
import com.zzhb.zzoa.service.AttachmentService;
import com.zzhb.zzoa.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileUploadController{

    @Autowired
    Props props;

    @Autowired
    AttachmentService attachmentService;

    private static List<String> uploadSuffix = new ArrayList<>();
    /**
     * 上传多个文件
     * @param request
     * @return
     */
//    @CrossOrigin
    @RequestMapping("/ajax/upload_file")
    @ResponseBody
    public Object ajaxUploadFile(HttpServletRequest request) {
        //将request请求的上下文转换为MultipartHttpServletRequest
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String uploadfileName = "";
        String errorMsg = null;
        String fileName = null;
//        List<String> errorList = new ArrayList<>();
        List<String> msgList = new ArrayList<>();
        try {
            //获取文件
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//            multipartFile = null;//获取文件名
            boolean uploadFlag = true;
            for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
                MultipartFile multipartFile = set.getValue();// 文件名
                uploadfileName = multipartFile.getOriginalFilename();
                if(StringUtils.isEmpty(uploadfileName)){
                    continue;
                }

                fileName = this.storeIOcFile(multipartRequest, multipartFile);//没有c://tmpzzoa路径
                //数据库就保存该路径
                if(StringUtils.isEmpty(fileName)){
                    errorMsg = "文件上传失败";
                    uploadFlag = false;
                    break;
                }
                msgList.add(fileName.replace("\\","/"));
            }
            if(!uploadFlag){
//                return addResultMapMsg(false,errorMsg);
                return "文件上传失败";
            }else{
                if(!msgList.isEmpty()){
//                    return addResultMapMsg(true,msgList);
                    String type = uploadfileName.substring(uploadfileName.indexOf(".")+1,uploadfileName.length());
                    JSONObject js = new JSONObject();
                    js.put("attach_name",uploadfileName);//文件名
                    js.put("attach_type",type);//文件类型
                    js.put("url",fileName);//文件的相对路径
                    //附件进行入库处理
                    Map<String,String> params = new HashMap<>();
                    String uuid = UUIDUtil.creatUUID();
                    params.put("uuid",uuid);
                    params.put("attach_name",uploadfileName);//文件名
                    params.put("attach_type",type);//文件类型
                    params.put("url",fileName);//文件的相对路径
                    Integer result = attachmentService.insertAttaInfo(params);
                    js.put("result",result);
                    js.put("uuid",uuid);//将主键返回给前台
                    return js;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return addResultMapMsg(false,"文件上传失败");
        return "文件上传失败";
    }

    private String storeIOc(HttpServletRequest request, MultipartFile file) {
        String result = "";
        if (file == null) {
            return "";
        }
        String fileName = "";
        String logImageName = "";
        if (file.isEmpty()) {
            //result = "文件未上传";
            result = "1";
        } else {
            String _fileName = file.getOriginalFilename();
            String suffix = _fileName.substring(_fileName.lastIndexOf(".")).toLowerCase();
            if(!StringUtils.isEmpty(suffix)){
                if(suffix.equalsIgnoreCase(".xls") || suffix.equalsIgnoreCase(".xlsx") || suffix.equalsIgnoreCase(".txt")|| suffix.equalsIgnoreCase(".png")
                        || suffix.equalsIgnoreCase(".doc") || suffix.equalsIgnoreCase(".docx") || suffix.equalsIgnoreCase(".pdf")
                        || suffix.equalsIgnoreCase(".ppt") || suffix.equalsIgnoreCase(".pptx")|| suffix.equalsIgnoreCase(".gif")
                        || suffix.equalsIgnoreCase(".jpg")|| suffix.equalsIgnoreCase(".jpeg")|| suffix.equalsIgnoreCase(".bmp")
                        || suffix.equalsIgnoreCase(".zip")|| suffix.equalsIgnoreCase(".rar")|| suffix.equalsIgnoreCase(".7z") || suffix.equalsIgnoreCase(".shp") || suffix.equalsIgnoreCase(".dbf")){
                    // /**使用UUID生成文件名称**/
                    logImageName = UUID.randomUUID().toString() +"_"+ _fileName;
                    String todayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String uploadDir = request.getParameter("uploadDir");
                    String resultStr =  File.separator + uploadDir + File.separator + todayString + File.separator +logImageName;
                    String dir = props.getTempPath();
                    fileName = dir + resultStr;
                    File restore = new File(fileName);
                    if(!restore.getParentFile().exists()){
                        restore.getParentFile().mkdirs();
                    }
                    try {
                        //System.out.println(3+"=="+fileName);
                        file.transferTo(restore);
                        //System.out.println(4+"=="+restore);
                        result = resultStr;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }else{
                    //result = "文件格式不对，只能上传ppt、ptx、doc、docx、xls、xlsx、pdf、png、jpg、jpeg、gif、bmp格式";
                    result = "3";
                }
            }
        }
        return result;
    }

    private String storeIOcFile(HttpServletRequest request, MultipartFile file) {
        String result = "";
        String _fileName = file.getOriginalFilename();
        // /**使用UUID生成文件名称**/
        String logImageName = UUID.randomUUID().toString() +"_"+ _fileName;
        String todayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String uploadDir = request.getParameter("uploadDir");
        String dir = props.getTempPath();
        String resultStr =  File.separator + File.separator + todayString + File.separator +logImageName;
        String fileName = dir + resultStr;
        File restore = new File(fileName);
        if(!restore.getParentFile().exists()){
            restore.getParentFile().mkdirs();
        }
        try {
            //System.out.println(3+"=="+fileName);
            file.transferTo(restore);
            //System.out.println(4+"=="+restore);
            result = resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 文件下载接口
     * @param filePath  文件上传时，返回的相对路径
     * @param response
     * @param isOnLine  传入true，表示打开，但是打开的是浏览器能识别的文件，比如图片、pdf，word等无法打开
     *                  传入false,只是下载，如果不传入这个参数默认为false
     * @throws Exception
     */
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET)
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        isOnLine = false;//只是下载
        String dir = props.getTempPath();
        File f = new File(dir+filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        String fileName = f.getName();
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + dir+filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }
}
