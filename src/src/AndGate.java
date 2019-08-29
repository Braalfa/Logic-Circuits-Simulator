public class AndGate extends Component {

    public AndGate() {

        super(2, outputCoords, inputCoordsl, inputCoords2);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 && input2;
        return output;
    }
}
