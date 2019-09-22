package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Xnor
 */
public class XnorGate extends Component {
    /**
     * Constructor de AndGate
     */
    public XnorGate() {

        super(2,  new Point(67,16), new Point(0,10), new Point(0,23), new Image("imgs/xnor.png"), ComponentType.XNOR);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output = (input1 && input2) || (!input1 && !input2);
        return output;
    }
}
