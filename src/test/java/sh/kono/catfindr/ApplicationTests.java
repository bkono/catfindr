package sh.kono.catfindr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ApplicationTests {

  @Autowired
  private MockMvc mvc;

	@Test
  public void findsCorrectCountOfMatchesInFile() throws Exception {
	  ClassPathResource resource = new ClassPathResource("cat_input_frame.txt");
    MockMultipartFile mockMultipartFile = new MockMultipartFile("frame", "cat_input_frame.txt", "text/plain", resource.getInputStream());
    this.mvc.perform(
        fileUpload("/find")
            .file(mockMultipartFile)
            .param("min_confidence", "0.75"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(6)));
  }

}

