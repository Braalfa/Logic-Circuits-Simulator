import java.awt.*;

public class NorGate extends Component {

    public NorGate() {

        super(2, new Point(55,15), new Point(1,10), new Point(1, 24));
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1 && !input2;
        return output;
    }
}
