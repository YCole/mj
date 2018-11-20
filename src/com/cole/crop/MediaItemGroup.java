
package cole.crop;

import java.util.ArrayList;

public class MediaItemGroup {
    public String title; 
    public int bucketId;
    public int startPortLine = 0;
    public int startLandLine = 0;
    public int startItem = 0;
    public int itemCount = 0;
    public int groupType = 0;//0 common 1 location 2 face
    public int imageid = 0;
    public Path firstPath;

    public ArrayList<Path> items = new ArrayList<Path>();

    public void addItem(Path path) {
        items.add(path);
        itemCount++;
    }
}
