package hu.ponte.hr.services;

import hu.ponte.hr.config.ConfigNameConst;
import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.controller.exception.exception.PonteSignatureException;
import hu.ponte.hr.entity.Doc;
import hu.ponte.hr.repository.DocRepository;
import hu.ponte.hr.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImageStore {
    @Autowired
    Environment environment;
    @Autowired
    private DocRepository docRepository;

    @Autowired
    private SignService signService;

    @Transactional
    public Doc store(MultipartFile multipartFile) throws IOException, SignatureException {

        Doc docWithId = mapToDoc(multipartFile);

        File directory = new File(docWithId.getFolderPath());
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(docWithId.getFilePath());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (Exception e) {
            log.error("FileOutputStream: save path: {}", file.getPath());
            throw new PonteSignatureException("FileOutputStream: save path: {}", e);
        }
        log.info("FileOutputStream: save path: {}", file.getPath());
        return docWithId;
    }

    private Doc mapToDoc(MultipartFile multipartFile) throws SignatureException, IOException {
        Doc doc = new Doc();
        doc.setSign(signService.sign(multipartFile.getBytes()));
        doc.setName(multipartFile.getOriginalFilename());
        doc.setExtension(FileUtils.getExtensionWithDot(multipartFile.getOriginalFilename()));
        doc.setSize(multipartFile.getSize());
        doc.setMimeType(multipartFile.getContentType());
        doc.setFolderPath(environment.getProperty(ConfigNameConst.UPLOAD_FILE_FOLDER_PATH));
        Doc docWithId = docRepository.save(doc);
        return docWithId;
    }

    @Transactional
    public List<ImageMeta> getAllImageMeta() {

        List<ImageMeta> ret = new ArrayList<>();
        List<Doc> docs = docRepository.findAll();

        for (Doc d : docs) {
            ret.add(ImageMeta.builder().id(String.valueOf(d.getId())).name(d.getName()).digitalSign(d.getSign()).mimeType(d.getMimeType()).build());
        }

        return ret;
    }

    @Transactional
    public String getDocPathById(String docId) throws FileNotFoundException {
        String filePath;
        Optional<Doc> doc = docRepository.findById(Long.valueOf(docId));
        if (doc.isPresent()) {
            filePath = doc.get().getFilePath();
        } else {
            log.error("EntityNotFoundException: getDocPathById inputs:  docId {}", docId);
            throw new EntityNotFoundException("Doc:" + docId.toString());
        }
        if (Files.notExists(Path.of(filePath))) {
            log.error("FileNotFoundException: getDocPathById:  filePath {}", filePath);
            throw new FileNotFoundException(filePath);
        }
        return filePath;
    }

}
