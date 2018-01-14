package sh.kono.catfindr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Frame {
  private final ArrayList<char[]> image = new ArrayList<>();
  public Frame(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String line;
    int maxColCount = 0;
    while((line = reader.readLine()) != null) {
      char[] chars = line.toCharArray();
      if (chars.length > maxColCount) {
        maxColCount = chars.length;
      }
      getImage().add(line.toCharArray());
    }

    // even out the width of every row
    normalizeImageRowLengths(maxColCount);

    reader.close();
  }

  // interesting note, starting on row 3 of the trained image there are intermittently 14
  // instead of 15 colCount. took some dancing to handle this scenario and ensure a consistent width
  private void normalizeImageRowLengths(int maxColCount) {
    for (int i = 0; i < image.size(); i++) {
      char[] row = image.get(i);
      int rowLength = row.length;
      int diff = maxColCount - rowLength;
      if (diff > 0) {
        char[] newRow = Arrays.copyOf(row, rowLength+diff);
        for (int j = 0; j < diff; j++) {
          newRow[rowLength + j] = Character.MIN_VALUE;
        }

        image.set(i, newRow);
      }
    }
  }

  public int charCount() {
    return rowCount() * colCount();
  }

  public int rowCount() {
    return getImage().size();
  }

  public int colCount() {
    return getImage().get(0).length;
  }

  public char getCharAt(int row, int col) {
    if (row >= rowCount() || col >= colCount()) {
      return Character.MIN_VALUE;
    }

    return getImage().get(row)[col];
  }

  public ArrayList<char[]> getImage() {
    return image;
  }
}
