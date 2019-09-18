import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.*;

public class AndGate extends Component {

    public AndGate() {
        super(2,  new Point(62,15), new Point(0,8), new Point(0,25), new Image("imgs/and.png"), ComponentType.AND) ;
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 && input2;
        return output;
    }
}
