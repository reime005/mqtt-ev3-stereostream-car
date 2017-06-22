package de.ostfalia;

import de.ostfalia.remotecar.Constants;
import de.ostfalia.remotecar.mqtt.MqttPublishClient;
import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.VideoControlMessage;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX based test application instead of Unity3D
 */
public class Main extends Application {
    private TextArea textQoS;
    private TextArea textBrokerPort;
    private TextArea textBrokerIp;
    private TextArea textVideoPort;
    private TextArea textVideoIp;
    private TextArea textVideoWidth;
    private TextArea textVideoHeight;
    private TextArea textVideoFps;
    private int qos = Integer.parseInt(Constants.DEFAULT_PUBLISH_QOS);
    private static MqttPublishClient client;

    private BooleanProperty upPressed = new SimpleBooleanProperty(false);
    private BooleanProperty downPressed = new SimpleBooleanProperty(false);
    private BooleanProperty leftPressed = new SimpleBooleanProperty(false);
    private BooleanProperty rightPressed = new SimpleBooleanProperty(false);

    private BooleanBinding leftForward = leftPressed.and(upPressed);
    private BooleanBinding rightForward = rightPressed.and(upPressed);
    private BooleanBinding leftBackward = leftPressed.and(downPressed);
    private BooleanBinding rightBackward = rightPressed.and(downPressed);

    public static void main(String[] args) {
        launch(args);
    }

    private void startClient(String ip, String port) {
        new Thread(() -> {
            if (client != null && client.isConnected()) {
                System.out.println("Client is already connected");
                return;
            }

            try {
                qos = Integer.parseInt(textQoS.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                textQoS.setText(Constants.DEFAULT_PUBLISH_QOS);
                qos = Integer.parseInt(Constants.DEFAULT_PUBLISH_QOS);
            }

            try {
                client = new MqttPublishClient(ip, Integer.valueOf(port), qos);
            } catch (Exception e) {
                client = new MqttPublishClient();
                System.out.println("To start with a custom IP, add the option: ");
                System.out.println("IP:PORT");
            }
            client.connect();
        }).start();
    }

    private void closeConnection() {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    private void refreshCamera() {
        if (client != null && client.isConnected()) {
            client.sendVideoControlMsg(new VideoControlMessage(textVideoIp.getText(),
                    Integer.parseInt(textVideoWidth.getText()),
                    Integer.parseInt(textVideoHeight.getText()),
                    Integer.parseInt(textVideoPort.getText()),
                    Integer.parseInt(textVideoFps.getText())));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Car Control");

        textBrokerIp = new TextArea(Constants.DEFAULT_BROKER_IP);
        textBrokerIp.setMaxSize(170, 70);

        textBrokerPort = new TextArea("1883");
        textBrokerPort.setMaxSize(70, 70);

        textQoS = new TextArea(Constants.DEFAULT_PUBLISH_QOS);
        textQoS.setMaxSize(35, 70);

        textVideoIp = new TextArea(Constants.DEFAULT_CONTROLLER_IP);
        textVideoIp.setMaxSize(170, 70);

        textVideoPort = new TextArea(Constants.DEFAULT_VIDEO_UDP_PORT);
        textVideoPort.setMaxSize(70, 70);

        textVideoWidth = new TextArea(Constants.DEFAULT_VIDEO_WIDTH);
        textVideoWidth.setMaxSize(70, 70);

        textVideoHeight = new TextArea(Constants.DEFAULT_VIDEO_HEIGHT);
        textVideoHeight.setMaxSize(70, 70);

        textVideoFps = new TextArea(Constants.DEFAULT_VIDEO_FPS);
        textVideoFps.setMaxSize(35, 70);

        final Button btnRefresh = new Button("Camera Refresh");
        btnRefresh.setOnAction(event -> {
            refreshCamera();
        });

        final Button btnConnect = new Button("Connect");
        btnConnect.setOnAction(event -> {
            textBrokerIp.setDisable(true);
            textBrokerPort.setDisable(true);
            textQoS.setDisable(true);
            textVideoIp.setDisable(true);
            textVideoPort.setDisable(true);
            textVideoWidth.setDisable(true);
            textVideoHeight.setDisable(true);
            textVideoFps.setDisable(true);
            btnRefresh.setDisable(false);
            startClient(textBrokerIp.getText(), textBrokerPort.getText());
        });

        final Button btnDisconnect = new Button("Disconnect");
        btnDisconnect.setOnAction(event -> {
            textBrokerIp.setDisable(false);
            textBrokerPort.setDisable(false);
            textQoS.setDisable(false);
            textVideoIp.setDisable(false);
            textVideoPort.setDisable(false);
            textVideoWidth.setDisable(false);
            textVideoHeight.setDisable(false);
            textVideoFps.setDisable(false);
            btnRefresh.setDisable(false);
            closeConnection();
        });

        btnRefresh.setDisable(true);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(new Text("BROKER IP \n(10.0.0.2)"), 0, 0);
        grid.add(new Text("QoS \n(0-2)"), 1, 0);
        grid.add(new Text("PORT \n(1883)"), 2, 0);

        grid.add(textBrokerIp, 0, 1);
        grid.add(textQoS, 1, 1);
        grid.add(textBrokerPort, 2, 1);

        grid.add(btnConnect, 0, 2);
        grid.add(btnDisconnect, 0, 3);

        grid.add(new Text("STREAM DST IP \n(10.0.0.1)"), 0, 4);
        grid.add(new Text("PORT \n(1337)"), 1, 4);
        grid.add(new Text("WIDTH \n(1344/2560)"), 2, 4);
        grid.add(new Text("HEIGHT \n(376/720)"), 3, 4);
        grid.add(new Text("FPS \n(0-100)"), 4, 4);

        grid.add(textVideoIp, 0, 5);
        grid.add(textVideoPort, 1, 5);
        grid.add(textVideoWidth, 2, 5);
        grid.add(textVideoHeight, 3, 5);
        grid.add(textVideoFps, 4, 5);

        grid.add(btnRefresh, 0, 6);

        Scene scene = new Scene(grid, 600, 400);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    upPressed.setValue(true);
                    break;

                case DOWN:
                    downPressed.setValue(true);
                    break;

                case LEFT:
                    leftPressed.setValue(true);
                    break;

                case RIGHT:
                    rightPressed.setValue(true);
                    break;
            }

            if (leftForward.get() || rightForward.get() || leftBackward.get() || rightBackward.get()) {
                return;
            } else if (upPressed.get()) {
                drive(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.STRAIGHT);
            } else if (downPressed.get()) {
                drive(MotionControlMessage.SpeedParameter.BACKWARD, MotionControlMessage.SteeringParameter.STRAIGHT);
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    upPressed.setValue(false);
                    break;

                case DOWN:
                    downPressed.setValue(false);
                    break;

                case LEFT:
                    leftPressed.setValue(false);
                    break;

                case RIGHT:
                    rightPressed.setValue(false);
                    break;
            }

            if (upPressed.get()) {
                drive(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.STRAIGHT);
            } else if (downPressed.get()) {
                drive(MotionControlMessage.SpeedParameter.BACKWARD, MotionControlMessage.SteeringParameter.STRAIGHT);
            } else {
                client.sendMotionControlMsg(new MotionControlMessage(MotionControlMessage.SpeedParameter.STOP, MotionControlMessage.SteeringParameter.STRAIGHT));
            }
        });

        leftForward.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drive(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.LEFT);
        });
        leftBackward.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drive(MotionControlMessage.SpeedParameter.BACKWARD, MotionControlMessage.SteeringParameter.LEFT);
        });
        rightForward.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drive(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.RIGHT);
        });
        rightBackward.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                drive(MotionControlMessage.SpeedParameter.BACKWARD, MotionControlMessage.SteeringParameter.RIGHT);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean connected() {
        if (client == null) {
            return false;
        } else if (!client.isConnected()) {
            return false;
        }
        return true;
    }

    private void drive(MotionControlMessage.SpeedParameter speed, MotionControlMessage.SteeringParameter steering) {
        if (!connected()) {
            System.out.println("Client is not connected");
            return;
        }

        client.sendMotionControlMsg(new MotionControlMessage(speed, steering));
    }

    private void stop(MotionControlMessage.SpeedParameter speed, MotionControlMessage.SteeringParameter steering) {
        if (!connected()) {
            System.out.println("Client is not connected");
            return;
        }

        client.sendMotionControlMsg(new MotionControlMessage(speed, steering));
    }

    @Override
    public void stop() throws Exception {
        closeConnection();
        super.stop();
    }
}
