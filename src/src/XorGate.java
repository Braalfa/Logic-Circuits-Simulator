import java.awt.*;

public class XorGate extends Component {

    public XorGate() {
        super(2, new Point(60,16), new Point(1,9), new Point(1,23));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= (input1 || input2)&&(!input1 || !input2);
        return output;
    }
}
