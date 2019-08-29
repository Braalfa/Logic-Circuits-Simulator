import java.awt.*;

public class AndGate extends Component {

    public AndGate() {

        super(2,  new Point(62,16), new Point(1,9), new Point(1,25));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 && input2;
        return output;
    }
}
