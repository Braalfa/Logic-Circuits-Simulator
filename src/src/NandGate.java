import javafx.scene.image.Image;

import java.awt.*;

public class NandGate extends Component {

    public NandGate() {
        super(2,  new Point(72,16), new Point(1,12), new Point(1,27), new Image("imgs/nand.png"));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !(input1 && input2 );
        return output;
    }
}