package by.kos.randomcat;

public class CatImage {
  private String url;

  public CatImage(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return "CatImage{" +
        "url='" + url + '\'' +
        '}';
  }
}
