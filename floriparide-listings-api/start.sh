#!/bin/bash
gunicorn -b 0.0.0.0:8888 -w 3 main:root_app