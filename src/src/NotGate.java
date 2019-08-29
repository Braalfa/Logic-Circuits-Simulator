import javafx.scene.image.Image;

import java.awt.*;

public class NotGate extends Component {

    public NotGate() {

        super(1, new Point(73,16), new Point(1,16), null, new Image("imgs/not.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1;
        return output;
    }
}
