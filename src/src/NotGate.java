import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.*;

public class NotGate extends Component {

    public NotGate() {

        super(1, new Point(74,16), new Point(0,16), null, new Image("imgs/not.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1;
        return output;
    }
}
