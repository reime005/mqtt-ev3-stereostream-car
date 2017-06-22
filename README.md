# mqtt-ev3-stereostream-car

This project is part of my master thesis. The main purpose is to remotely control a LEGO Mindstorms EV3 robot using MQTT as the communication protocol. 
* The server module is designed to run on a NVIDIA TK1, to act as the main control component (MQTT subscribe client)
* A stereoscopic camera (ZED) is connected via USB 3.0, to send a UDP/gstreamer based stream to the HTC Vive Virtual Reality glass
* The client module was built with JavaFX, to act as a MQTT publish client
* The common module contains the messages and abstract classes for the other modules