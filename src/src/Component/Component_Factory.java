package Component;
/**
 * Esta clase se encarga de implementar el patron de diseño Factory para crear Componentes
 */
public class Component_Factory {
    /**
     * Metodo estatico que crea componentes
     * @param c Tipo de componente
     * @return Componente generado
     */
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
