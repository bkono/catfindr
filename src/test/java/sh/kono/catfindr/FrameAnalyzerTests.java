package sh.kono.catfindr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FrameAnalyzerTests {
  private Frame trainedImage;
  private Frame inputFrame;

  @Before
  public void setUp() {
    ClassPathResource trainedResource = new ClassPathResource("training_image.txt");
    ClassPathResource inputResource = new ClassPathResource("cat_input_frame.txt");
    try {
      trainedImage = new Frame(trainedResource.getInputStream());
      inputFrame = new Frame(inputResource.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Test
  public void trainedImageCharCount() {
    // magic 225 value is "known"
    assertEquals(225, trainedImage.charCount());
  }

  @Test
  public void inputFrameCharCount() {
    // magic 10000 value is "known"
    assertEquals(10000, inputFrame.charCount());
  }

  @Test
  public void findMatchesHasRightCount() {
    FrameAnalyzer subject = new FrameAnalyzer(trainedImage, inputFrame);
    assertEquals(6, subject.findMatches().size());
  }

  @Test
  public void findMatchesHonorsConfidence() {
    float minConfidence = 0.85f;
    FrameAnalyzer subject = new FrameAnalyzer(trainedImage, inputFrame, minConfidence);
    ArrayList<Match> matches = subject.findMatches();
    int returnedSize = matches.size();
    assertEquals(1, returnedSize);
    matches.removeIf(match -> match.getConfidence() < minConfidence);
    assertEquals(returnedSize, matches.size());
  }
}
