import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;

public class XorGate extends Component {

    public XorGate() {
        super(2, new Point(61,16), new Point(0,9), new Point(0,23), new Image("imgs/xor.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= (input1 || input2)&&(!input1 || !input2);
        return output;
    }
}
