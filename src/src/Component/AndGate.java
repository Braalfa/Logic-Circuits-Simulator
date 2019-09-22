package Component;

import javafx.scene.image.Image;

import java.awt.*;

/**
 * Esta clase representa una compuerta logica tipo And
 */
public class AndGate extends Component {
    /**
     * Constructor de AndGate
     */
    public AndGate() {
        super(2,  new Point(62,15), new Point(0,8), new Point(0,25), new Image("imgs/and.png"), ComponentType.AND) ;
    }

    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= input1 && input2;
        return output;
    }
}
