package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Nor
 */
public class NorGate extends Component {
    /**
     * Constructor de NorGate
     */
    public NorGate() {

        super(2, new Point(56,15), new Point(0,10), new Point(0, 23), new Image("imgs/nor.png"), ComponentType.NOR);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= !input1 && !input2;
        return output;
    }
}
