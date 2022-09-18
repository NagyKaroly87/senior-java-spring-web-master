package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SignatureException;

@Slf4j
@Component
@RequestMapping("api/file")
public class UploadController
{

    @Autowired
    private ImageStore imageStore;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) throws IOException, SignatureException {

        log.info("handleFormUpload start");
       imageStore.store(file);

        return "ok";
    }
}
