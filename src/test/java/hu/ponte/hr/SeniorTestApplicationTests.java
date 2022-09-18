package hu.ponte.hr;

import hu.ponte.hr.entity.Doc;
import hu.ponte.hr.repository.DocRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SeniorTestApplicationTests
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DocRepository docRepository;


	@Test
	public void getListImages() throws Exception {

		Doc doc = new Doc();
		doc.setSign("signService.sign(multipartFile.getBytes())");
		doc.setName("multipartFile.getOriginalFilename()");
		doc.setExtension(".jpg");
		doc.setSize(1000l);
		doc.setMimeType("/jpg");
		doc.setFolderPath("");
		docRepository.save(doc);

		mockMvc.perform(get("/api/images/meta"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		doc = new Doc();
		doc.setSign("signService.sign(multipartFile.getBytes())");
		doc.setName("nameX");
		doc.setExtension(".jpg");
		doc.setSize(1000l);
		doc.setMimeType("/jpg");
		doc.setFolderPath("");
		docRepository.save(doc);

		mockMvc.perform(get("/api/images/meta"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].name").value("nameX"));

	}

}

