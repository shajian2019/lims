package com.zzhb.controller.swgl;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.config.Props;
import com.zzhb.mapper.JournalMapper;
import com.zzhb.service.AttachmentService;
import com.zzhb.utils.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileUploadController{

    @Autowired
    Props props;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    JournalMapper journalMapper;

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
                    String dir = props.getUploadPath();
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
        String dir = props.getUploadPath();
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
     */
    @GetMapping("/downloadFile")
    public void downLoad(@RequestParam Map<String, String> params,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String fileId = "";
        String fileName = "";
        //1.获取要下载的文件的绝对路径
        List<Map<String,String>> attlist = journalMapper.getAtt(params);
        if(attlist.size() > 0){
            Map<String,String> attmap = attlist.get(0);
            fileId = attmap.get("url");
            fileName = attmap.get("attach_name");
        }
        String realPath = props.getUploadPath()+fileId;
        response.reset();
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream;charset=utf-8");
        //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.setHeader("content-disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb2312"),"ISO-8859-1"));
        InputStream in = new FileInputStream(realPath);//获取文件输入流
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer,0,len);//将缓冲区的数据输出到客户端浏览器
        }
        in.close();
    }
}
