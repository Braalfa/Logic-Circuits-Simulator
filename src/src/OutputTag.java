import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class OutputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();

    public OutputTag(AnchorPane parent, Point coords, Component component) {
        super(parent,coords, component);
        String text;
        if (unusedIds.isEmpty()){
            text="o<"+currentId+">";
            currentId++;
        }else{
            text="o<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.display(text);
        coords.setLocation(coords.getX(),coords.getY()-this.getHeight()/2);
    }

}
