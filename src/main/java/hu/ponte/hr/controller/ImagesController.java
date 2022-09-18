package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() throws IOException {
        log.info("listImages start");
		return imageStore.getAllImageMeta();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        log.info("getImage start");
        String path = imageStore.getDocPathById(id);
        log.info("getImage return path:  {}",path);
        response.sendRedirect("http://localhost:8080/" + path);
    }
}
