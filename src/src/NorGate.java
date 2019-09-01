import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.*;

public class NorGate extends Component {

    public NorGate() {

        super(2, new Point(56,15), new Point(0,10), new Point(0, 23), new Image("imgs/nor.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1 && !input2;
        return output;
    }
}
