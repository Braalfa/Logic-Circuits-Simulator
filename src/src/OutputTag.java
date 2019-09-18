import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class OutputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();

    public OutputTag(AnchorPane parent, Point coords, Component component) {
        super(parent,coords, component);
        this.updateId();
        this.display(this.getId());
        this.coords.setLocation(coords.getX(),coords.getY()-this.getHeight()/2);
    }

    protected void addNextTag(Tag nextTag) {
        this.nextTag.add(nextTag);
    }

    public OutputTag clone(AnchorPane parent, Component component, Point diplacement){
        OutputTag clone= new OutputTag(parent, coords, component);
        clone.setLayoutX(this.getLayoutX()+diplacement.getX());
        clone.setLayoutY(this.getLayoutY()+diplacement.getY());
        clone.setLines(this.cloneLines(diplacement));
        clone.setLinesColor(clone.getLinesColor());

        return clone;
    }

    public void updateId(){
        String text;
        if (unusedIds.isEmpty()){
            text="o<"+currentId+">";
            currentId++;
        }else{
            text="o<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.setId(text);
    }
}
