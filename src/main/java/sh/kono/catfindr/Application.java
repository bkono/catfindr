package sh.kono.catfindr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@SpringBootApplication
public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private static final Frame trainedImage;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	static {
	  // using this approach to ensure the image is set when using @SpringBootTest
	  trainedImage = initTrainedImage();
  }

  private static Frame initTrainedImage() {
    ClassPathResource resource = new ClassPathResource("training_image.txt");
    Frame frame = null;
    try {
      frame = new Frame(resource.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      logger.error("unable to read training image!!");
      System.exit(1);
    }

    return frame;
  }

  @GetMapping("/")
  public String index() {
	  return "Welcome to CatFindr. Please use the /find endpoint by POST'ing a text matrix as the \"frame\" parameter, and providing a min_confidence query string value.";
  }

  @PostMapping("/find")
  public ArrayList<Match> find(@RequestParam("frame") MultipartFile frame, @RequestParam("min_confidence") float minConfidence) {
	  if (frame.isEmpty()) {
	    throw new IllegalArgumentException("provided frame cannot be empty");
    }

    try {
      Frame input = new Frame(frame.getInputStream());
      FrameAnalyzer analyzer = new FrameAnalyzer(trainedImage, input, minConfidence);

      return analyzer.findMatches();

    } catch (IOException e) {
      e.printStackTrace();

      throw new UnprocessableEntityException();
    }
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  private class UnprocessableEntityException extends RuntimeException {}
}

