package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Xor
 */
public class XorGate extends Component {
    /**
     * Constructor de AndGate
     */
    public XorGate() {
        super(2, new Point(61,16), new Point(0,9), new Point(0,23), new Image("imgs/xor.png"), ComponentType.XOR);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= (input1 || input2)&&(!input1 || !input2);
        return output;
    }
}
