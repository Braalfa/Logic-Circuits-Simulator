package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Not
 */
public class NotGate extends Component {
    /**
     * Constructor de AndGate
     */
    public NotGate() {

        super(1, new Point(74,16), new Point(0,16), null, new Image("imgs/not.png"),  ComponentType.NOT);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1;
        return output;
    }
}
