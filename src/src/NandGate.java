import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.*;

public class NandGate extends Component {

    public NandGate() {
        super(2,  new Point(72,16), new Point(0,8), new Point(0,25), new Image("imgs/nand.png"), ComponentType.NAND);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !(input1 && input2 );
        return output;
    }
}
