public class XnorGate extends Component {

    public XnorGate() {

        super(2, outputCoords, inputCoordsl, inputCoords2);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output = (input1 && input2) || (!input1 && !input2);
    }
}
