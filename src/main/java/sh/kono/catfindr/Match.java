package sh.kono.catfindr;

public class Match {
  private int row;
  private int col;
  private float confidence;

  public Match(int row, int col, float confidence) {
    this.row = row;
    this.col = col;
    this.confidence = confidence;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public float getConfidence() {
    return confidence;
  }
}
