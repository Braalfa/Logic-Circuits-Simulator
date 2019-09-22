package Component;

import javafx.scene.image.Image;

import java.awt.*;
/**
 * Esta clase representa una compuerta logica tipo Nand
 */
public class NandGate extends Component {
    /**
     * Constructor de NandGate
     */
    public NandGate() {
        super(2,  new Point(72,16), new Point(0,8), new Point(0,25), new Image("imgs/nand.png"), ComponentType.NAND);
    }
    /**
     * Se calcula el valor del output de la compuerta logica
     * @return Valor booleano de la salida del componente
     * @throws NullPointerException
     */
    @Override
    public boolean calculate() throws NullPointerException {
        output= !(input1 && input2 );
        return output;
    }
}
