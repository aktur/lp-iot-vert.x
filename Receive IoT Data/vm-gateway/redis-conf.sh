#!/bin/bash

multipass --verbose exec gateway -- sudo -- bash << EOF
sed -i "s/bind 127.0.0.1/bind 0.0.0.0/" /etc/redis/redis.conf

/etc/init.d/redis-server restart
EOF
