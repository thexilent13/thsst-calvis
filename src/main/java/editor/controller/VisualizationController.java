package editor.controller;

import configuration.model.engine.CalvisFormattedInstruction;
import editor.model.AssemblyComponent;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import simulatorvisualizer.model.AnimationMap;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.EnvironmentBag;

/**
 * Created by Jennica on 07/02/2016.
 */
public class VisualizationController extends AssemblyComponent {

    private Tab tab;
    private ScrollPane scrollPane;
    private CalvisFormattedInstruction currentInstruction;
    private AnimationMap animationMap;
    private EnvironmentBag oldEnvironment;

    public VisualizationController() {
        this.tab = new Tab();
        this.tab.setText("Visualization");
        this.scrollPane = new ScrollPane();
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.tab.setContent(scrollPane);

    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        attachCalvisInstruction(currentInstruction);
        Platform.runLater(
                new Thread() {
                    public void run() {
//                        tab.setContent(null);
                        scrollPane.setContent(null);
                        animate();
                    }
                }
        );

    }

    public void animate() {
        // find the appropriate animation for currentInstruction
        String name = this.currentInstruction.getName();

        System.out.println("name: " + name);

        // get CalvisAnimation from the Animation Map
        CalvisAnimation animation = this.animationMap.get(name);

        if ( animation == null ) {
            // no animation
            System.out.println("no animation");
        } else {
            animation.setCurrentInstruction(this.currentInstruction);
            animation.animate(scrollPane);
        }
    }

    @Override
    public void refresh() {
    }

    @Override
    public void build() {
        this.animationMap = new AnimationMap();
    }


    public void attachCalvisInstruction(CalvisFormattedInstruction CalvisInstruction) {
        this.currentInstruction = CalvisInstruction;
    }

    public void setOldEnvironment(EnvironmentBag environment){
        this.oldEnvironment = environment;
    }

}
