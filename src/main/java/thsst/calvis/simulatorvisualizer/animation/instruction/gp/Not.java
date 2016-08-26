package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Not extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        String value0 = "";
        String result = "";

        if( tokens[0].getType() == Token.REG ) {
            value0 = finder.getRegister(tokens[0].getValue());
            result = registers.get(tokens[0].getValue());
        }
        else if( tokens[0].getType() == Token.MEM ) {
            try {
                value0 = finder.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }

            try {
                result = memory.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }
        }

        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle augendRectangle = this.createRectangle(tokens[0], width, height);

        if ( desRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            augendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            augendRectangle.setY(Y);
            augendRectangle.setArcWidth(10);
            augendRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, augendRectangle, equalCircle);

            int desSize = 0;
            if ( tokens[0].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[0]);
            else
                desSize = memory.getBitSize(tokens[0]);

            String flagsAffected = "Affected flags: none";
            Text detailsText = new Text(X, Y*2, flagsAffected);
            Text desLabelText = this.createLabelText(X, Y, tokens[0]);
            Text desValueText = new Text(X, Y, result);
            Text augendLabelText = this.createLabelText(X, Y, tokens[0]);
            Text augendValueText = new Text(X, Y, "!" + value0);

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, desLabelText, desValueText,
                    augendLabelText, augendValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();
            TranslateTransition augendLabelTransition = new TranslateTransition();
            TranslateTransition augendTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
            TranslateTransition plusTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desTransition.setInterpolator(Interpolator.LINEAR);
            desTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(augendRectangle.translateYProperty()
                    .add(augendRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Equal sign label static
            equalTransition.setNode(equalText);
            equalTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 70 + augendRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            equalTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            equalTransition.toXProperty().bind(equalTransition.fromXProperty());
            equalTransition.toYProperty().bind(equalTransition.fromYProperty());

            // Augend label static
            augendLabelTransition.setNode(augendLabelText);
            augendLabelTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendLabelText.getLayoutBounds().getWidth()) / 2));
            augendLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            augendLabelTransition.toXProperty().bind(augendLabelTransition.fromXProperty());
            augendLabelTransition.toYProperty().bind(augendLabelTransition.fromYProperty());

            // Augend value static
            augendTransition.setNode(augendValueText);
            augendTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendValueText.getLayoutBounds().getWidth()) / 2));
            augendTransition.fromYProperty().bind(augendRectangle.translateYProperty()
                    .add(augendRectangle.getLayoutBounds().getHeight() / 1.5));
            augendTransition.toXProperty().bind(augendTransition.fromXProperty());
            augendTransition.toYProperty().bind(augendTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desTransition.play();
            equalTransition.play();
            augendLabelTransition.play();
            augendTransition.play();
        }
    }
}