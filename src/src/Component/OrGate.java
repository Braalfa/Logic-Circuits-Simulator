package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Or
 */
public class OrGate extends Component {
    /**
     * Constructor de AndGate
     */
    public OrGate() {

        super(2,  new Point(51,17), new Point(0,10), new Point(0,23), new Image("imgs/or.png"), ComponentType.OR);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 || input2;
        return output;
    }
}
