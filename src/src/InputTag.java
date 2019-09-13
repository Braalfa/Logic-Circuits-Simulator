import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class InputTag extends Tag {
    private static int currentId=0;
    private static ArrayList<Integer> unusedIds = new ArrayList<>();
    private int inputNumber;

    public InputTag(AnchorPane parent, Point coords, Component component, int inputNumber) {
        super(parent,coords, component);
        String text;
        if (unusedIds.isEmpty()){
            text="i<"+currentId+">";
            currentId++;
        }else{
            text="i<"+unusedIds.get(0)+">";
            unusedIds.remove(0);
        }
        this.setId(text);
        this.display(text);
        this.coords.setLocation(coords.getX()-this.getWidth(),coords.getY()-this.getHeight()/2);
        this.inputNumber=inputNumber;

    }

    public int getInputNumber() {
        return inputNumber;
    }

    protected void addNextTag(Tag nextTag) {
        superTree.connect(this.getComponent(), nextTag.getComponent(), inputNumber);
        this.nextTag.add(nextTag);
    }


}
