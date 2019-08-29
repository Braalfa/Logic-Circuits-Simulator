public class NotGate extends Component {

    public NotGate() {

        super(1, outputCoords, inputCoordsl, null);
    }

    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1;
        return output;
    }
}
