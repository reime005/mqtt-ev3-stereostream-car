#!/bin/sh

gst-launch-1.0 -e -v udpsrc port=1337 ! application/x-rtp, encoding-name=H264, payload=96 ! rtph264depay ! h264parse ! avdec_h264 ! autovideosink sync=false async=false
