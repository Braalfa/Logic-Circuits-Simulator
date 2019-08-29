public class OrGate extends Component {

    public OrGate() {

        super(2, outputCoords, inputCoordsl, inputCoords2);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 || input2;
        return output;
    }
}
