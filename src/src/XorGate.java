public class XorGate extends Component {

    public XorGate() {

        super(2, outputCoords, inputCoordsl, inputCoords2);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= (input1 || input2)&&(!input1 || !input2);
        return output;
    }
}
