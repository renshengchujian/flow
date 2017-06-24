package com.zbeninfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbeninfo.util.XmlToJsonUtil;
import com.zbeninfo.util.XmlUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
@SpringBootApplication
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }

    @Value("${multipart.location}")
    public String location;

    @RequestMapping(value = {"/"})
    public String index(ModelMap modelMap) {
        return "index";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        String jsonStr = null;
        try {
            File saveFile = new File(location, file.getOriginalFilename());
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
            out.write(file.getBytes());
            out.flush();
            out.close();

            JSONObject json = XmlToJsonUtil.readXMLFile(saveFile);
            jsonStr = JSON.toJSONString(json);//转字符串再转json对象,消除null
        } catch (Exception e) {
            System.out.println(e);
        }
        return JSONObject.parseObject(jsonStr);
    }

    @RequestMapping("/saveData")
    @ResponseBody
    public String saveData(@RequestParam("flow") String flow) {
        try {
            XmlUtil.saveXMLfromFlow(flow, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/download")
    public ResponseEntity<InputStreamResource> download()
        throws IOException {
        File flowFile = new File(location, "IVR.xml");
        FileSystemResource file = new FileSystemResource(flowFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition",
            String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(new InputStreamResource(file.getInputStream()));
    }


}
