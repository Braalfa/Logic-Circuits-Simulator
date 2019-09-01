import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class InputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();

    public InputTag(AnchorPane parent, Point coords, Component component) {
        super(parent,coords, component);
        String text;
        if (unusedIds.isEmpty()){
            text="i<"+currentId+">";
            currentId++;
        }else{
            text="i<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.display(text);
        this.coords.setLocation(coords.getX()-this.getWidth(),coords.getY()-this.getHeight()/2);

    }



}
