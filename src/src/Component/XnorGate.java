package Component;

import javafx.scene.image.Image;

import java.awt.*;

public class XnorGate extends Component {

    public XnorGate() {

        super(2,  new Point(67,16), new Point(0,10), new Point(0,23), new Image("imgs/xnor.png"), ComponentType.XNOR);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output = (input1 && input2) || (!input1 && !input2);
        return output;
    }
}
