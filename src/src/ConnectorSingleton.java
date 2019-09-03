import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;

public class ConnectorSingleton {
    private AnchorPane parent;
    private Tag startTag;
    private Tag endTag;
    private ArrayList<Bounds> bounds;
    private static ConnectorSingleton instance=null;

    private ConnectorSingleton(AnchorPane parent, Tag startTag, Tag endTag){
        this.parent=parent;
        this.startTag=startTag;
        this.endTag=endTag;
    }

    public static ConnectorSingleton getInstance(Tag startTag, Tag endTag){
        if(instance==null){
            instance= new ConnectorSingleton((AnchorPane) startTag.getParent(),startTag, endTag);
        }else{
            instance.startTag=startTag;
            instance.endTag=endTag;
        }
        return instance;
    }

    private void updateBounds(Tag start,Tag end){
        ObservableList<Node> nodes=parent.getChildren();
        ArrayList<Bounds> bounds= new ArrayList<>();
        for(Node node: nodes){
            if ((node instanceof Component) && (node!= start.getComponent() && node!=end.getComponent())){
                bounds.add(node.getBoundsInParent());
            }
        }
        this.bounds=bounds;

    }
    public void autoConnect(Tag start, Tag end) {
        Point startPoint = start.getLastLinePoint();
        Point endPoint = end.getLastLinePoint();
        start.removeLastLine();
        Line[] lines = null;
        int linesIndex = 1;
        this.updateBounds(start,end);
        long timeStart=java.lang.System.currentTimeMillis();
        long currentTime=timeStart;
        boolean outOfTime=false;
        while (lines == null && !outOfTime) {
            lines = this.createPath(linesIndex, linesIndex, (int) startPoint.getX() ,
                    (int) startPoint.getY() , (int) endPoint.getX() , (int) endPoint.getY() , 1, start, end);
            if (lines == null) {
                lines = this.createPath(linesIndex, linesIndex, (int) startPoint.getX() ,
                        (int) startPoint.getY() , (int) endPoint.getX() , (int) endPoint.getY() , -1, start, end);
            }
            linesIndex++;
            System.out.print(linesIndex+"\n");
            currentTime=java.lang.System.currentTimeMillis();
            if(currentTime-timeStart>1000){
                outOfTime=true;
            }
        }
        if (outOfTime){
            startTag.setNextTag(null);
        }else {
            for (Line line : lines) {
                line.setVisible(true);
            }
        }
    }
    public Line[] createPath(int numberlines, int orgLines, int ax, int ay, int bx, int by, int direction, Tag start, Tag end ) {
        if (numberlines == 1) {
            if (ax == bx || by == ay) {
                Line[] result = new Line[orgLines];
                Line line= this.createLine(ax, ay, bx, by);
                if (!this.overlaps(line,start, end)) {
                    result[orgLines-numberlines]=line;
                    return result;
                }else{
                    parent.getChildren().remove(line);
                    return null;
                }
            } else {
                return null;
            }
        } else {
            Line[] lines = null;
            int middle;
            boolean done1 = false;
            boolean done2 = false;
            int lim1 = this.getDownLimit(ax);
            int lim2 = this.getUpLimit(ax);
            int lim3 = this.getRigthLimit(ay);
            int lim4 = this.getLeftLimit(ay);
            int iterator = 0;
            Line line = null;
            if (direction == 1) {
                middle = (by - ay) / 2;
                while (lines == null && !done1 && !done2) {
                    if (ay + middle + iterator < lim1) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax, ay + middle + iterator);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle + iterator, bx, by, -1, start, end);
                        }
                    } else{
                        done1=true;
                    }
                    if (lines == null && (ay + middle - iterator > lim2)) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax, ay + middle - iterator);
                        if (!this.overlaps(line,start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax, ay + middle - iterator, bx, by, -1,start, end);
                        }
                    } else {
                        done2 = true;
                    }
                    iterator+=1;
                }
            } else {
                middle = (bx - ax) / 2;
                while (lines == null && !done1 && !done2) {
                    if (ax + middle + iterator < lim3) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle + iterator, ay);
                        if (!this.overlaps(line, start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle + iterator, ay, bx, by, 1, start, end);
                        }
                    } else {
                        done1 = true;
                    }
                    if (lines == null && (ax + middle - iterator > lim4)) {
                        parent.getChildren().remove(line);
                        line = this.createLine(ax, ay, ax + middle - iterator, ay);

                        if (!this.overlaps(line, start, end)) {
                            lines = createPath(numberlines - 1, orgLines, ax + middle - iterator, ay, bx, by, 1, start, end);
                        }
                    } else {
                        done2 = true;
                    }
                    iterator += 1;
                }
            }
            if (lines != null) {
                lines[orgLines - numberlines] = line;
            } else {
                parent.getChildren().remove(line);
            }
            return lines;
        }
    }
    private Line createLine(double ax, double ay, double bx, double by){
        Line line = new Line();
        line.setStartX(ax);
        line.setStartY(ay);
        line.setEndX(bx);
        line.setEndY(by);
        parent.getChildren().add(line);
        return line;
    }
    private boolean overlaps(Line line, Tag start,Tag end){
        boolean result=false;
        Bounds startBn = start.getComponent().getLayoutBounds();
        Bounds endBn = end.getComponent().getLayoutBounds();

        if( line.intersects(startBn.getMinX()+1,startBn.getMinY(),startBn.getMaxX()-startBn.getMinX()-2, startBn.getMaxY()-startBn.getMinY())  ||
                line.intersects(endBn.getMinX()+1,endBn.getMinY(),endBn.getMaxX()-endBn.getMinX()-2, endBn.getMaxY()-endBn.getMinY())){
            result=true;

        }
        return result;

    }

    private int getRigthLimit(int posY){
        int limX=(int) parent.getWidth();
        for(Bounds bound: bounds){
            if(bound.getMinY()<=posY && bound.getMaxY()>=posY){
                if(limX>bound.getMinX()){
                    limX= (int) bound.getMinX();
                }
            }
        }
        return limX;
    }
    private int getLeftLimit( int posY){
        int limX=0;
        for(Bounds bound: bounds){
            if(bound.getMinY()<=posY && bound.getMaxY()>=posY){
                if(limX<bound.getMaxX()){
                    limX= (int) bound.getMinX();
                }
            }
        }
        return limX;
    }
    private int getDownLimit( int posX){
        int limY=(int) parent.getHeight();
        for(Bounds bound: bounds){
            if(bound.getMinX()<=posX && bound.getMaxX()>=posX){
                if(limY>bound.getMinY()){
                    limY= (int) bound.getMinY();
                }
            }
        }
        return limY;
    }
    private int getUpLimit(int posX){
        int limY=0;
        for(Bounds bound: bounds){
            if(bound.getMinX()<=posX && bound.getMaxX()>=posX){
                if(limY<bound.getMaxY()){
                    limY= (int) bound.getMaxY();
                }
            }
        }
        return limY;
    }
}
