import java.awt.*;

public class OrGate extends Component {

    public OrGate() {

        super(2,  new Point(51,17), new Point(1,10), new Point(1,24));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 || input2;
        return output;
    }
}
