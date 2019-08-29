public class Component_Factory {
    public static Component getComponent(ComponentType c){
        switch (c){
            case NOR:
                return new NorGate();
            case NOT:
                return new NotGate();
            case OR:
                return new OrGate();
            case XNOR:
                return new XnorGate();
            case XOR:
                return new XorGate();
            case NAND:
                return new NandGate();
            default:
                return new AndGate();
        }

    }
}
