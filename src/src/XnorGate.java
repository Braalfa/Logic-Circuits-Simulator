import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.*;

public class XnorGate extends Component {

    public XnorGate() {

        super(2,  new Point(67,16), new Point(1,10), new Point(1,22), new Image("imgs/xnor.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output = (input1 && input2) || (!input1 && !input2);
        return output;
    }
}
