package sh.kono.catfindr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FrameAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(FrameAnalyzer.class);
  private final Frame trainedImage;
  private final Frame inputFrame;
  private final float minConfidence;

  public FrameAnalyzer(Frame trainedImage, Frame inputFrame) {
    this(trainedImage, inputFrame, 0.75f);
  }

  public FrameAnalyzer(Frame trainedImage, Frame inputFrame, float minConfidence) {
    this.trainedImage = trainedImage;
    this.inputFrame = inputFrame;
    this.minConfidence = minConfidence;
  }

  public ArrayList<Match> findMatches() {
    ArrayList<Match> matches = new ArrayList<>();
    for (int row = 0; row < inputFrame.rowCount() - trainedImage.rowCount(); row++) {
      for (int col = 0; col < inputFrame.colCount() - trainedImage.colCount(); col++) {
        // we have enough rowCount and columns available for a potential match
        // assess matches based on this row & column position
        int matchedChars = matchedCharsAt(row, col);
        float confidence = (float)matchedChars / trainedImage.charCount();
        if (confidence >= minConfidence) {
          matches.add(new Match(row, col, confidence));
        }
      }
    }

    return matches;
  }

  private int matchedCharsAt(int row, int col) {
    int matchedChars = 0;

    for (int trainedRow = 0; trainedRow < trainedImage.rowCount(); trainedRow++) {
      for (int trainedCol = 0; trainedCol < trainedImage.colCount(); trainedCol++) {
        // treat incoming row and col as offset value
        char trainedChar = trainedImage.getCharAt(trainedRow, trainedCol);
        char inputChar = inputFrame.getCharAt(row + trainedRow, col + trainedCol);
        if (trainedChar == inputChar) {
          matchedChars++;
        }
      }
    }

    return matchedChars;
  }
}

