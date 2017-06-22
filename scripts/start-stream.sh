#!/bin/sh

gst-launch-1.0 -vvv v4l2src device=/dev/video0 ! "video/x-raw, format=(string)I420, width=(int)2560, height=(int)720, framerate=30/1" ! omxh264enc iframeinterval=1 ! 'video/x-h264, stream-format=(string)byte-stream' ! rtph264pay ! udpsink host=192.168.0.1 port=1337
